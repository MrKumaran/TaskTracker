package in.project.tasktracker.Core;

import in.project.tasktracker.Enums.Auth.AuthEnum;
import in.project.tasktracker.Enums.Profile.ProfileEnum;
import in.project.tasktracker.Enums.Task.TaskEnum;
import in.project.tasktracker.Model.Profile.Profile;
import in.project.tasktracker.Model.Profile.ProfileUsernameUpdateDto;
import in.project.tasktracker.Model.Task.Task;
import in.project.tasktracker.Model.Profile.ProfileUpdateDto;
import in.project.tasktracker.Model.Task.UserTasks;
import in.project.tasktracker.Model.User.PasswordUpdateDto;
import in.project.tasktracker.Model.User.UserRegisterDto;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

// This class is responsible for all DB operations
public class DBManager {
    private static final DBManager INSTANCE;

    static {
        INSTANCE = new DBManager();
    }

    private DataSource dataSource;

    private DBManager() {
        try{
            InitialContext context = new InitialContext();
            dataSource = (DataSource) context.lookup("java:comp/env/db/tasktracker");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DBManager getInstance() {
        return INSTANCE;
    }

    // profile url related operations --
    // update new url for user
    public boolean updateProfileUrl(String url, String userId) {
        String query = "UPDATE profile SET avatar_url = ? WHERE user_id = ?";
        try(Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, url);
            ps.setString(2, userId);
            boolean isSuccess = ps.executeUpdate() == 1;
            ps.close();
            return isSuccess;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // updating profile information
    public boolean updateProfile(ProfileUpdateDto editProfileObject) {
        boolean isPasswordPresent = !(editProfileObject.getPassword() == null || editProfileObject.getPassword().isEmpty());
        String query = "UPDATE authentication SET password = ?, salt = ? WHERE user_id = ?";
        try(Connection con = dataSource.getConnection()){
            con.setAutoCommit(false);
            if(isPasswordPresent) {
                try(PreparedStatement ps = con.prepareStatement(query)) {
                    ps.setString(1, editProfileObject.getPassword());
                    ps.setString(2, editProfileObject.getSalt());
                    ps.setString(3, editProfileObject.getUserId());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            query = "UPDATE profile SET user_name = ? WHERE user_id = ?";
            try(PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, null);
                ps.setString(2, editProfileObject.getUserId());
                ps.executeUpdate();
                con.commit();
                con.setAutoCommit(true);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    // Task related operations --
    // Getting tasks for user
    public UserTasks retrieveUsersTasks(String user_id) {
        UserTasks userTasks = new UserTasks();
        List<Task> tasks = userTasks.getTasks();

        String query = "SELECT user_id, task_id, task_title, due, isDone, completedAt FROM task WHERE user_id = ? ORDER BY isDone, due, completedAt";

        try(Connection con = dataSource.getConnection()){
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, user_id);
            ResultSet rs = ps.executeQuery();
            ps.close();
            while (rs.next()) {
                String completedAt = rs.getString("completedAt");
                Task task = Task.builder()
                        .setUserId(user_id)
                        .setTaskId(rs.getString("task_id"))
                        .setTaskTitle(rs.getString("task_title"))
                        .setDue(
                                LocalDateTime.parse(
                                        rs.getString("due").replace(" ", "T")
                                )
                        )
                        .setDone(rs.getBoolean("isDone"))
                        .setCompletedAt(
                                completedAt==null?null:
                                        LocalDateTime.parse(
                                                completedAt.replace(" ", "T")
                                        )
                        )
                        .build();
                tasks.add(task);
            }

            query = "SELECT COUNT(task_id) as Total_Count, SUM(isDone) as Total_Done_Count FROM task WHERE user_id = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, user_id);
            ResultSet resultSet = statement.executeQuery();
            statement.close();
            if(resultSet.next()){
                userTasks.setTasksCount(
                        resultSet.getLong("Total_Count")
                    );
                    userTasks.setTasksDoneCount(
                            resultSet.getLong("Total_Done_Count")
                    );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userTasks;
    }

    // Adding new task for user
    public boolean upsertTask(Task task) {
        String query = "INSERT INTO task(user_id, task_id, task_title, due, isDone, completedAt) VALUES " +
                "(?,?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE " +
                "user_id = VALUES(user_id), " +
                "task_title = VALUES(task_title), " +
                "due = VALUES(due), " +
                "isDone = VALUES(isDone), " +
                "completedAt = VALUES(completedAt)";
        try(Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, task.getUserId());
            ps.setString(2, task.getTaskId());
            ps.setString(3, task.getTaskTitle());
            ps.setTimestamp(4,Timestamp.valueOf(task.getDue()));
            ps.setBoolean(5, task.isDone());
            ps.setTimestamp(6, task.getCompletedAt()!=null?Timestamp.valueOf(task.getCompletedAt()):null);
            boolean isSuccess = ps.executeUpdate() == 1;
            ps.close();
            return isSuccess;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // updating task status - Once done can't change status
    public TaskEnum updateTaskStatus(String userId, String taskId) {
        String query = "UPDATE task SET isDone = ?, completedAt = ? WHERE user_Id = ? AND task_Id = ?";
        try(Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setBoolean(1, true);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(3, userId);
            ps.setString(4, taskId);
            boolean isSuccess = ps.executeUpdate() == 1;
            ps.close();
            return (isSuccess)?TaskEnum.TASK_STATUS_UPDATED
                    :TaskEnum.TASK_NOT_FOUND;
        } catch (SQLException e) {
            e.printStackTrace();
            return TaskEnum.ERROR_OCCURRED;
        }
    }

    // deleting task
    public boolean deleteTask(String userId, String taskId) {
        String query = "DELETE FROM task  WHERE user_id = ? AND task_id = ?";
        try(Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);
            ps.setString(2, taskId);
            boolean isSuccess = ps.executeUpdate() == 1;
            ps.close();
            return isSuccess;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // retrieve task
    public Task getTaskByID(String taskId, String userId){
        Task.Builder taskBuilder = Task.builder();
        String query = "SELECT task_title, due FROM task WHERE user_id = ? AND task_id = ?";

        try(Connection con = dataSource.getConnection()){
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);
            ps.setString(2, taskId);
            ResultSet rs = ps.executeQuery();
            ps.close();
            if(rs.next()) {
                taskBuilder.setTaskTitle(rs.getString("task_title"))
                .setTaskId(taskId)
                .setUserId(userId)
                .setDone(false)
                .setCompletedAt(null)
                .setDue(LocalDateTime.parse(
                        rs.getString("due").replace(" ", "T")
                ));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return taskBuilder.build();
    }

    // Authentication related operations--
    public String loginViaMail(String mail, String password) {
        String query = "SELECT user_id, password, salt FROM authentication WHERE mail = ?";
        try(Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, mail);
            ResultSet rs =  ps.executeQuery();
            ps.close();
            if (rs.next()){
                password = new Authentication().passwordHash(
                        password,
                        rs.getString("salt")
                );
                if (
                        password.equals(
                            rs.getString("password")
                        )
                ) return rs.getString("user_id");
                else return null;
            }
            else return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean signupViaMail(UserRegisterDto userRegisterDto) {
        String query = "INSERT INTO authentication(user_id, mail, password, salt) Values (?,?,?,?)";
        try(Connection con = dataSource.getConnection()){
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userRegisterDto.getUserId());
            ps.setString(2, userRegisterDto.getMail());
            ps.setString(3, userRegisterDto.getPassword());
            ps.setString(4, userRegisterDto.getSalt());
            ps.executeUpdate();

            query = "INSERT INTO profile(user_id, user_name, avatar_url) Values (?,?,?)";
            ps = con.prepareStatement(query);
            ps.setString(1, userRegisterDto.getUserId());
            ps.setString(2, userRegisterDto.getUserName());
            ps.setString(3, null);
            ps.executeUpdate();
            ps.close();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // checks whether mail exist in DB
    public boolean isNewMail(String mail) {
        String query = "SELECT 1 FROM authentication WHERE mail = ? LIMIT 1";
        try(Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, mail);
            ResultSet rs = ps.executeQuery();
            ps.close();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // to get user profile - also using this method for getting existing url
    public Profile retrieveProfile(String userId) {
        Profile profile = new Profile();
        String query =
                "SELECT mail, user_name, avatar_url " +
                "FROM authentication a JOIN profile p USING(user_id)" +
                " WHERE a.user_id = ?";
        try(Connection con = dataSource.getConnection()){

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            ps.close();
            if (rs.next()) {
                profile.setUserId(userId);
                profile.setMailId(rs.getString("mail"));
                profile.setUserName(rs.getString("user_name"));
                profile.setAvatarURL(rs.getString("avatar_url"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return profile;
    }

    // Deleting user profile as soon as requested
    public void deleteAccount(String userId) {
        String query = "DELETE FROM authentication WHERE user_id = ?";
        try(Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ProfileEnum updateUserName(ProfileUsernameUpdateDto profileUsernameUpdateDto) {
        String query = "UPDATE profile SET user_name = ? WHERE user_id = ?";
        try(Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, profileUsernameUpdateDto.getUserName());
            ps.setString(2, profileUsernameUpdateDto.getUserId());
            boolean isSuccess = ps.executeUpdate() == 1;
            ps.close();
            if (isSuccess) return ProfileEnum.USERNAME_UPDATED;
            else return ProfileEnum.PROFILE_NOT_FOUND;
        } catch (SQLException e) {
            e.printStackTrace();
            return ProfileEnum.ERROR_OCCURRED;
        }
    }

    public AuthEnum verifyPassword(String oldPassword, String userId) {
        String query = "SELECT password, salt FROM authentication WHERE user_id = ?";
        try(Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            ps.close();
            if (rs.next()) {
                String password = rs.getString("password");
                String salt = rs.getString("salt");

                return password.equals(
                        new Authentication().passwordHash(oldPassword, salt))
                ? AuthEnum.PASSWORD_MATCH:
                        AuthEnum.CREDENTIALS_NOT_MATCH;

            } else return AuthEnum.USER_NOT_FOUND;
        } catch (SQLException e) {
            e.printStackTrace();
            return AuthEnum.ERROR_OCCURRED;
        }
    }

    public AuthEnum changePassword(PasswordUpdateDto passwordUpdateDto) {
        AuthEnum authEnum = verifyPassword(passwordUpdateDto.getOldPassword(), passwordUpdateDto.getUserId());
        if (!authEnum.equals(AuthEnum.PASSWORD_MATCH)) return authEnum;

        String query = "UPDATE authentication SET password = ?, salt = ? WHERE user_id = ?";
        try(Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, passwordUpdateDto.getPassword());
            ps.setString(2, passwordUpdateDto.getSalt());
            ps.setString(3, passwordUpdateDto.getUserId());
            boolean isSuccess = ps.executeUpdate() == 1;
            return (isSuccess)? AuthEnum.PASSWORD_UPDATED
                    :AuthEnum.USER_NOT_FOUND;
        } catch (SQLException e){
            e.printStackTrace();
            return AuthEnum.ERROR_OCCURRED;
        }
    }
}
