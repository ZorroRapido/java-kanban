public class Subtask extends Task {
    private Epic epic;

    Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
        this.epic.addSubtask(this);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", epicId=" + epic.getId() +
                ", name='" + getName() + "'" +
                ", description.length=" + getDescription().length() +
                ", status='" + getStatus() + "'" +
                "}";
    }

    public Epic getEpic() {
        return epic;
    }
}
