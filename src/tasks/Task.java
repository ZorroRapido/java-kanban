package tasks;

public class Task {
    private int id;
    private TaskType type;
    private String name;
    private String description;
    private Status status;

    public Task(String name, String description) {
        this.id = 0;
        this.type = TaskType.TASK;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", type='" + type.toString() + "'" +
                ", name='" + name + "'" +
                ", description.length=" + description.length() +
                ", status='" + status.toString() + "'" +
                "}";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status newStatus) {
        for (Status item : Status.values()) {
            if (item.equals(newStatus)) {
                this.status = newStatus;
                break;
            }
        }
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType newType) {
        for (TaskType item : TaskType.values()) {
            if (item.equals(newType)) {
                this.type = newType;
                break;
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
