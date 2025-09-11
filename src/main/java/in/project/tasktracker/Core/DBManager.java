package in.project.tasktracker.Core;

import in.project.tasktracker.Model.Task;
import in.project.tasktracker.Model.User;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static final DBManager INSTANCE = new DBManager();
    private Connection con;

    public DBManager() {
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

    public List<Task> retrieveUsersTasks(String user_id) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT user_id, task_id, task_title, due, isDone, completedAt FROM task WHERE isDone = 'false' AND user_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, user_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Task task = new Task();
                task.setUser_id(user_id);
                task.setTask_id(rs.getString("task_id"));
                task.setTask_title(rs.getString("task_title"));
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
            try {
                con.rollback();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return tasks;
    }

    public boolean addTask(Task task) {
        String query = "INSERT INTO task(user_id, task_id, task_title, due, isDone, completedAt) VALUES" +
                "(?,?,?,?,?,?)";
        try(PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, task.getUser_id());
            ps.setString(2, task.getTask_id());
            ps.setString(3, task.getTask_title());
            ps.setTimestamp(4,Timestamp.valueOf(task.getDue()));
            ps.setBoolean(5, task.isDone());
            ps.setTimestamp(6, task.getCompletedAt()!=null?Timestamp.valueOf(task.getCompletedAt()):null);
            ps.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try{
                con.rollback();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            return false;
        }
        return true;
    }

    // Authentication --
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
        String query = "INSERT INTO profile(user_id, user_name, avatar_url) Values (?,?,?)";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, user.getUser_id());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getAvatar_url());
            ps.executeUpdate();
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
        query = "INSERT INTO authentication(user_id, mail, password, salt) Values (?,?,?,?)";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, user.getUser_id());
            ps.setString(2, user.getMail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getSalt());
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
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
            e.printStackTrace();
            return false;
        }
    }

    //used only for test purposes
    public User retrieveUser(String userId) {
        User user = new User();
        String query =
                "SELECT mail, password, salt, user_name, avatar_url " +
                "FROM authentication a JOIN profile p USING(user_id)" +
                " WHERE a.user_id = ?";
        try(PreparedStatement ps = con.prepareStatement(query)){
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.setUser_id(userId);
                user.setMail(rs.getString("mail"));
                user.setSalt(rs.getString("salt"));
                user.setPassword(rs.getString("password"));
                user.setUserName(rs.getString("user_name"));
                user.setAvatar_url(rs.getString("avatar_url"));
            }
            else return null;
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                return null;
            }
            e.printStackTrace();
            return null;
        }
        return user;
    }

}
