package in.project.tasktracker.Core;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;

// This class is responsible for communicating with cloudinary
// It has method to upload image to cloud and return secure_url
// Another method to delete already present image for user
public class ImageManager {
    private final Cloudinary cloudinary;
    private static final ImageManager INSTANCE = new ImageManager();

    private ImageManager() {
        String cloudnaryCloudName = null;
        String cloudnaryAPIkey = null;
        String cloudnaryAPIsecret = null;
        try{
            Context context = new InitialContext();
            cloudnaryCloudName = (String) context.lookup("java:comp/env/cloudinary.api.cloudName");
            cloudnaryAPIkey = (String) context.lookup("java:comp/env/cloudinary.api.key");
            cloudnaryAPIsecret = (String) context.lookup("java:comp/env/cloudinary.api.secret");
        } catch (NamingException e){
            e.printStackTrace();
        }
        if(cloudnaryCloudName == null || cloudnaryAPIkey == null || cloudnaryAPIsecret == null) cloudinary = null;
        else {
            String cloudinaryURL = "cloudinary://" + cloudnaryAPIkey + ":"+ cloudnaryAPIsecret +"@" + cloudnaryCloudName;
            cloudinary = new Cloudinary(cloudinaryURL);
        }
    }

    public static ImageManager getInstance() {
        return INSTANCE;
    }

    public String uploadImage(String path) throws IOException {
        return cloudinary.uploader()
                        .upload(path, ObjectUtils.emptyMap()).get("secure_url").toString();
    }

    public void deleteOldImage(String publicId) throws IOException {
        cloudinary.uploader()
                .destroy(publicId, ObjectUtils.asMap("invalidate", true));
    }

}
