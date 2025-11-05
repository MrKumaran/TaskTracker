package in.project.tasktracker.Model.Task;

import java.time.LocalDateTime;

public class Task {
    private String userId;
    private String taskId;
    private String taskTitle;
    private LocalDateTime due;
    private boolean isDone;
    private LocalDateTime completedAt;

    // Default constructor
    public Task() {}

    public Task(Builder builder){
        this.userId = builder.userId;
        this.taskId = builder.taskId;
        this.taskTitle = builder.taskTitle;
        this.due = builder.due;
        this.isDone = builder.isDone;
        this.completedAt = builder.completedAt;
    }

    // This has many field, making it perfect candidate for builder pattern
    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private String userId;
        private String taskId;
        private String taskTitle;
        private LocalDateTime due;
        private boolean isDone;
        private LocalDateTime completedAt;

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setTaskId(String taskId) {
            this.taskId = taskId;
            return this;
        }

        public Builder setTaskTitle(String taskTitle) {
            this.taskTitle = taskTitle;
            return this;

        }

        public Builder setDue(LocalDateTime due) {
            this.due = due;
            return this;
        }

        public Builder setDone(boolean done) {
            isDone = done;
            return this;
        }

        public Builder setCompletedAt(LocalDateTime completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        public Task build() {
            return new Task(this);
        }

    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public void setDue(LocalDateTime due) {
        this.due = due;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public LocalDateTime getDue() {
        return due;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isDone() {
        return isDone;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
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
