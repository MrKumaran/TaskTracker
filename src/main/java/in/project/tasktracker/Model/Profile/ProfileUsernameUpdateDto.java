package in.project.tasktracker.Model.Profile;

public class ProfileUsernameUpdateDto {
    private String userId;
    private String userName;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
