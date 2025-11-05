package in.project.tasktracker.Model.Profile;

public class ProfileDto {
    private String userName;
    private String avatarUrl;

    public ProfileDto(String userName, String avatarUrl) {
        this.userName = userName;
        this.avatarUrl = avatarUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
