package in.project.tasktracker.Controller;

import in.project.tasktracker.Core.DBManager;
import in.project.tasktracker.Core.ImageManager;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

// This servlet is responsible for maintaining images(profile images)
@WebServlet(name = "ProfileImageUpdater", value = {
        "/upload-profile-pic",
        "/delete-profile-pic"
})
@MultipartConfig
public class ProfileImageUpdater extends HttpServlet {
    private ImageManager imageManager;
    private DBManager dbManager;

    @Override
    public void init() {
        imageManager = ImageManager.getInstance();
        dbManager = DBManager.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String userId = (String) session.getAttribute("user");
        response.setContentType("application/json");
        String path = request.getServletPath();
        if(path.equals("/upload-profile-pic")) {
            try {
                Part filePart = request.getPart("profilePic");

                // retrieving for Removing old asset from cloud
                String oldUrl = dbManager.retrieveProfile(userId).getAvatarURL();

                // adding new asset into cloud and DB url of asset
                String url = imageManager.uploadImage(filePart);
                if(url == null || url.isEmpty()) {
                    returnNone(response);
                    return;
                }
                // adding url to db
                boolean dbUpdated = dbManager.updateProfileUrl(url, userId);
                if(!dbUpdated) {
                    returnNone(response);
                    return;
                }

                // after all success removing user old asset from cloud
                if(oldUrl != null && !oldUrl.isEmpty()) {
                    imageManager.deleteOldImage(oldUrl);
                }
                response.getWriter().write("{\"status\":\"success\", \"fileName\":\"" + url + "\"}");
            } catch (Exception e) {
                e.printStackTrace();
                returnNone(response);
            }
        }
        else if(path.equals("/delete-profile-pic")) {
            String url = dbManager.retrieveProfile(userId).getAvatarURL();
            if(url != null && !url.isEmpty()) {
                imageManager.deleteOldImage(url);
                dbManager.updateProfileUrl(null, userId);
            }
        }

    }

    // method to return none in response to upload request
    private void returnNone(HttpServletResponse response) throws IOException {
        response.getWriter().write("{\"status\":\"failed\", \"fileName\":\"" + "none" + "\"}");
    }
}
