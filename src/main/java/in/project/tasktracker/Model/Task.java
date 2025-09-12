package in.project.tasktracker.Model;

import java.time.LocalDateTime;

public class Task {
    private String userId;
    private String taskId;
    private String taskTitle;
    private LocalDateTime due;
    private boolean isDone;
    private LocalDateTime completedAt;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
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

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    @Override
    public String toString(){
        return "\nTask {" +
                "\nuser_id: " + userId +
                "\ntask_id: " + taskId +
                "\ntask_title: " + taskTitle +
                "\ndue: " + due +
                "\nIsDone: " + isDone +
                "\nCompleted At: " + completedAt +
                "\n};";
    }
}
