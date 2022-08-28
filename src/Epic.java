import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Subtask> subtasksIncluded;

    Epic(String name, String description) {
        super(name, description);
        subtasksIncluded = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", description.length=" + getDescription().length() +
                ", status='" + getStatus() + "'" +
                ", subtasks.size=" + subtasksIncluded.size() +
                "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj != null || getClass() != obj.getClass()) return false;
        Epic otherEpic = (Epic) obj;
        return Objects.equals(getId(), otherEpic.getId()) &&
                Objects.equals(getName(), otherEpic.getName()) &&
                Objects.equals(getDescription(), otherEpic.getDescription()) &&
                Objects.equals(getStatus(), otherEpic.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getStatus());
    }

    public void addSubtask(Subtask subtask) {
        this.subtasksIncluded.add(subtask);
    }

    public ArrayList<Subtask> getEpicSubtasks() {
        return this.subtasksIncluded;
    }

    public void deleteAllEpicSubtasks() {
        subtasksIncluded.clear();
    }

    public void deleteEpicSubtask(Subtask subtask) {
        subtasksIncluded.remove(subtask);
    }
}
