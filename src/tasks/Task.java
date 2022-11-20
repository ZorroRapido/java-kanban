package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Task {
    private int id;
    private TaskType type;
    private String name;
    private String description;
    private Status status;
    private long duration;
    private LocalDateTime startTime;

    public Task(String name, String description) {
        this.id = 0;
        this.type = TaskType.TASK;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = 0;
    }

    public Task(String name, String description, LocalDateTime startTime, int duration) {
        this.id = 0;
        this.type = TaskType.TASK;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", type='" + type.toString() + "'" +
                ", name='" + name + "'" +
                ", description.length=" + description.length() +
                ", status='" + status.toString() + "'" +
                ", startTime='" + startTime + "'" +
                ", duration=" + duration +
                "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(type, otherTask.type) &&
                Objects.equals(name, otherTask.name) &&
                Objects.equals(description, otherTask.description) &&
                Objects.equals(status, otherTask.status) &&
                Objects.equals(startTime, otherTask.startTime) &&
                Objects.equals(duration, otherTask.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, description, status, startTime, duration);
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plus(Duration.of(duration, ChronoUnit.MINUTES));
        }
        return null;
    }
}
