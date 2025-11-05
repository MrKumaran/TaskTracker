package in.project.tasktracker.Model.User;
import in.project.tasktracker.Core.Authentication;

public class UserRegisterDto {
    private String userId;
    private String mail;
    private String password;
    private String salt;
    private String userName;

    public UserRegisterDto() {}

    public UserRegisterDto(Builder builder) {
        this.userId = builder.userId;
        this.mail = builder.mail;
        this.password = builder.password;
        this.salt = builder.salt;
        this.userName = builder().userName;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private String userId;
        private String mail;
        private String password;
        private String salt;
        private String userName;

        public Builder setMail(String mail) {
            this.mail = mail;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserRegisterDto build() {
            Authentication authentication = new Authentication();
            if (!authentication.passwordStrengthCheck(this.password)) return null;

            this.userId = authentication.generateUUID();
            this.salt = authentication.generateSalt();
            this.password = authentication.passwordHash(password, salt);

            return new UserRegisterDto(this);
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
