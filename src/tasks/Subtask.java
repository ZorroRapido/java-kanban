package tasks;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        setType(TaskType.SUBTASK);
        this.epic = epic;
        this.epic.addSubtask(this);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", type='" + getType() + "'" +
                ", epicId=" + epic.getId() +
                ", name='" + getName() + "'" +
                ", description.length=" + getDescription().length() +
                ", status='" + getStatus().toString() + "'" +
                "}";
    }

    public Epic getEpic() {
        return epic;
    }
}
