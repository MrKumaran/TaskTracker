package in.project.tasktracker.Model;

// lazy so created new object will be removed later
public class EditProfileObject {
    private String userId;
    private String userName;
    private String mail;
    private String password;
    private String salt;

    public EditProfileObject() {
        userName = null;
        mail = null;
        password = null;
        salt = null;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
