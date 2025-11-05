package in.project.tasktracker.Model.User;

import in.project.tasktracker.Core.Authentication;

public class PasswordUpdateDto {
    private String userId;
    private String oldPassword;
    private String password;
    private String salt;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getOldPassword() {
        return oldPassword;
    }
}
