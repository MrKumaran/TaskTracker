package in.project.tasktracker.Model;

import java.time.LocalDateTime;

public class Task {
    private String user_id;
    private String task_id;
    private String task_title;
    private LocalDateTime due;
    private boolean isDone;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    public LocalDateTime getDue() {
        return due;
    }

    public void setDue(LocalDateTime due) {
        this.due = due;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public String toString(){
        return "\nTask {" +
                "\nuser_id: " + user_id +
                "\ntask_id: " + task_id +
                "\ntask_title: " + task_title +
                "\ndue: " + due +
                "\nIsDone: " + isDone +
                "\n};";
    }

}
