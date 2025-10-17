package in.project.tasktracker.Model;


import java.util.ArrayList;
import java.util.List;

public class UserTasks {
    private final List<Task> tasks;
    private long tasksCount;
    private long tasksDoneCount;

    public UserTasks(){
        this.tasks = new ArrayList<>();
        this.tasksCount = -1;
        this.tasksDoneCount = -1;
    }

    public List<Task> getTasks() {
        return tasks;
    }


    public long getTasksCount() {
        return tasksCount;
    }

    public void setTasksCount(long tasksCount) {
        this.tasksCount = tasksCount;
    }

    public long getTasksDoneCount() {
        return tasksDoneCount;
    }

    public void setTasksDoneCount(long tasksDoneCount) {
        this.tasksDoneCount = tasksDoneCount;
    }
}
