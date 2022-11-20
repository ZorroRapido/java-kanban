package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Subtask> subtasksIncluded;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        setType(TaskType.EPIC);
        subtasksIncluded = new ArrayList<>();
        endTime = getEndTime();
    }

    public Epic(Task task) {
        this(task.getName(), task.getDescription());
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", type='" + getType() + "'" +
                ", name='" + getName() + "'" +
                ", description.length=" + getDescription().length() +
                ", status='" + getStatus().toString() + "'" +
                ", subtasks.size=" + subtasksIncluded.size() +
                ", startTime='" + getStartTime() + "'" +
                ", duration=" + getDuration() +
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

    public List<Subtask> getEpicSubtasks() {
        return this.subtasksIncluded;
    }

    public void deleteAllEpicSubtasks() {
        subtasksIncluded.clear();
    }

    public void deleteEpicSubtask(Subtask subtask) {
        subtasksIncluded.remove(subtask);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void updateEndTime() {
        if (getEpicSubtasks().isEmpty()) {
            setStartTime(null);
            setDuration(0);
            setEndTime(null);
        } else {
            List<Subtask> subtasks = getEpicSubtasks();
            LocalDateTime possibleStartTime = subtasks.get(0).getStartTime();
            LocalDateTime possibleEndTime = subtasks.get(0).getEndTime();

            for (Subtask subtask : subtasks) {
                if (subtask.getStartTime() != null) {
                    if (subtask.getStartTime().isBefore(possibleStartTime)) {
                        possibleStartTime = subtask.getStartTime();
                    }

                    if (subtask.getEndTime().isAfter(possibleEndTime)) {
                        possibleEndTime = subtask.getEndTime();
                    }
                }
            }

            setStartTime(possibleStartTime);

            if (possibleStartTime != null) {
                setDuration(Duration.between(possibleStartTime, possibleEndTime).toMinutes());
            }
            setEndTime(possibleEndTime);
        }
    }
}
