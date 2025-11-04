package in.project.tasktracker.Model.User;

// Having separate Dto never hurt
// Used for auth return sign up, sign in
public class UserAuthReturn {
    private String id;
    private String name;

    public UserAuthReturn(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
