package in.project.tasktracker.Core;

import in.project.tasktracker.Model.Profile;
import in.project.tasktracker.Model.Task;
import in.project.tasktracker.Model.User;
import in.project.tasktracker.Model.UserTasks;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

// This class is responsible for all DB operations
public class DBManager {
    private static final DBManager INSTANCE = new DBManager();
    private Connection con;

    private DBManager() {
        try{
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("java:comp/env/db/tasktracker");
            this.con = dataSource.getConnection();
            this.con.setAutoCommit(false);
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
        try(PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, url);
            ps.setString(2, userId);
            boolean isUpdated = ps.executeUpdate() == 1;
            con.commit();
            return isUpdated;
        } catch (SQLException e) {
            e.printStackTrace();
            rollBack();
            return false;
        }
    }

    // updating profile information
    public boolean updateProfile(User editProfileObject) {
        boolean isPasswordPresent = !(editProfileObject.getPassword() == null || editProfileObject.getPassword().isEmpty());
        String query = "UPDATE authentication SET password = ?, salt = ? WHERE user_id = ?";
        if(isPasswordPresent) {
            try(PreparedStatement ps = con.prepareStatement(query)) {
                    ps.setString(1, editProfileObject.getPassword());
                    ps.setString(2, editProfileObject.getSalt());
                    ps.setString(3, editProfileObject.getUserId());
                isPasswordPresent = ps.executeUpdate() == 1;
            } catch (SQLException e) {
                e.printStackTrace();
                rollBack();
                return false;
            }
            if (!isPasswordPresent) {
                rollBack();
                return false;
            }
        }
        query = "UPDATE profile SET user_name = ? WHERE user_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, editProfileObject.getUserName());
            ps.setString(2, editProfileObject.getUserId());
            ps.executeUpdate();
            con.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            rollBack();
            return false;
        }
    }

    // Task related operations --
    // Getting tasks for user
    public UserTasks retrieveUsersTasks(String user_id) {
        UserTasks userTasks = new UserTasks();
        List<Task> tasks = userTasks.getTasks();
        String query = "SELECT user_id, task_id, task_title, due, isDone, completedAt FROM task WHERE user_id = ? ORDER BY isDone, due, completedAt";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, user_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Task task = new Task();
                task.setUserId(user_id);
                task.setTaskId(rs.getString("task_id"));
                task.setTaskTitle(rs.getString("task_title"));
                task.setDue(
                        LocalDateTime.parse(
                                rs.getString("due").replace(" ", "T")
                        )
                );
                task.setDone(rs.getBoolean("isDone"));
                String completedAt = rs.getString("completedAt");
                task.setCompletedAt( completedAt==null?null:
                        LocalDateTime.parse(
                            completedAt.replace(" ", "T")
                        )
                );
                tasks.add(task);
            }
            con.commit();
        }catch (SQLException e){
            e.printStackTrace();
            rollBack();
        }

        query = "SELECT COUNT(task_id) as Total_Count, SUM(isDone) as Total_Done_Count FROM task WHERE user_id = ?";
        try(PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, user_id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                userTasks.setTasksCount(
                        resultSet.getLong("Total_Count")
                );
                userTasks.setTasksDoneCount(
                        resultSet.getLong("Total_Done_Count")
                );
            }
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            rollBack();
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
        try(PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, task.getUserId());
            ps.setString(2, task.getTaskId());
            ps.setString(3, task.getTaskTitle());
            ps.setTimestamp(4,Timestamp.valueOf(task.getDue()));
            ps.setBoolean(5, task.isDone());
            ps.setTimestamp(6, task.getCompletedAt()!=null?Timestamp.valueOf(task.getCompletedAt()):null);
            ps.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            rollBack();
            return false;
        }
        return true;
    }

    // updating task status
    public boolean updateTaskStatus(String userId, String taskId, boolean isDone) {
        String query = "UPDATE task SET isDone = ?, completedAt = ? WHERE user_Id = ? AND task_Id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)) {
            ps.setBoolean(1, isDone);
            ps.setTimestamp(2, (isDone)?Timestamp.valueOf(LocalDateTime.now()): null);
            ps.setString(3, userId);
            ps.setString(4, taskId);
            boolean isUpdated = ps.executeUpdate() == 1;
            con.commit();
            return isUpdated;
        } catch (SQLException e) {
            e.printStackTrace();
            rollBack();
            return false;
        }
    }

    // deleting task
    public boolean deleteTask(String userId, String taskId) {
        String query = "DELETE FROM task  WHERE user_id = ? AND task_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, userId);
            ps.setString(2, taskId);
            boolean isDeleted = ps.executeUpdate() == 1;
            con.commit();
            return isDeleted;
        } catch (SQLException e) {
            e.printStackTrace();
            rollBack();
            return false;
        }
    }

    // retrieve task
    public Task getTaskByID(String taskId, String userId){
        Task task = new Task();
        String query = "SELECT task_title, due FROM task WHERE user_id = ? AND task_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, userId);
            ps.setString(2, taskId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                task.setTaskTitle(
                        rs.getString("task_title")
                );
                task.setTaskId(taskId);
                task.setUserId(userId);
                task.setDone(false);
                task.setCompletedAt(null);
                task.setDue(LocalDateTime.parse(
                        rs.getString("due").replace(" ", "T")
                ));
            }
        } catch (SQLException e){
            e.printStackTrace();
            rollBack();
        }
        return task;
    }

    // Authentication related operations--
    public String loginViaMail(String mail, String password) {
        String query = "SELECT salt FROM authentication WHERE mail = ?";
        String salt;
        try(PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, mail);
            ResultSet rs =  ps.executeQuery();
            salt = rs.next()?rs.getString("salt") : null;
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        if (salt == null) return null;
        String passwordHash = Authentication.passwordHash(password, salt);
        query = "SELECT user_id FROM authentication WHERE mail = ? AND password = ?";
        try(PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, mail);
            ps.setString(2, passwordHash);
            ResultSet rs = ps.executeQuery();
            con.commit();
            return rs.next()?rs.getString("user_id"): null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean signupViaMail(User user) {
        String query = "INSERT INTO authentication(user_id, mail, password, salt) Values (?,?,?,?)";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getMail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getSalt());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            rollBack();
            return false;
        }
        query = "INSERT INTO profile(user_id, user_name, avatar_url) Values (?,?,?)";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getAvatarURL());
            ps.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // checks whether mail exist in DB
    public boolean isNewMail(String mail) {
        String query = "SELECT 1 FROM authentication WHERE mail = ? LIMIT 1";
        try(PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, mail);
            ResultSet rs = ps.executeQuery();
            con.commit();
            return rs.next();
        } catch (SQLException e) {
            rollBack();
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
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                profile.setUserId(userId);
                profile.setMailId(rs.getString("mail"));
                profile.setUserName(rs.getString("user_name"));
                profile.setAvatarURL(rs.getString("avatar_url"));
            }
            con.commit();
        } catch (SQLException e) {
            rollBack();
            e.printStackTrace();
            return null;
        }
        return profile;
    }

    // Deleting user profile as soon as requested
    public void deleteAccount(String userId) {
        String query = "DELETE FROM authentication WHERE user_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, userId);
            ps.executeUpdate();
            con.commit();
        } catch (SQLException e){
            e.printStackTrace();
            rollBack();
        }
    }

    // common method to rollback db if error occurs
    private void rollBack() {
        try {
            con.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
