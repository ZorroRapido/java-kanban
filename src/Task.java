public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;

    Task(String name, String description) {
        this.id = 0;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
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
        for (Status status : Status.values())
            if (status.equals(newStatus)) {
                this.status = newStatus;
                break;
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
