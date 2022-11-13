package managers;

import com.sun.source.tree.Tree;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTasksManager implements TasksManager {
    protected int counter;

    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Epic> epics;
    protected HashMap<Integer, Subtask> subtasks;
    protected TreeSet<Task> prioritizedTasks;

    protected HistoryManager historyManager;

    public InMemoryTasksManager() {
        counter = 0;
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();

        prioritizedTasks = new TreeSet<>((task1, task2) -> {
            if (task1.getStartTime() == null) {
                return 1;
            }
            if (task2.getStartTime() == null) {
                return -1;
            }
            return task1.getStartTime().compareTo(task2.getStartTime());
        });

        historyManager = Managers.getDefaultHistoryManager();
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public void deleteAllTasks() {
        for (Integer taskId : tasks.keySet())
            historyManager.remove(taskId);
        tasks.clear();
        prioritizedTasks.removeIf(task -> task.getClass() == Task.class);
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubtasks();
        prioritizedTasks.removeIf(task -> task.getClass() == Subtask.class);
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Integer subtaskId : subtasks.keySet())
            historyManager.remove(subtaskId);

        subtasks.clear();
        prioritizedTasks.removeIf(task -> task.getClass() == Subtask.class);

        for (Epic epic : epics.values()) {
            epic.deleteAllEpicSubtasks();
            epic.updateEndTime();
        }
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public void createTask(Task task) {
        if (!checkIsIntersection(task)) {
            task.setId(counter);
            counter++;
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(counter);
        counter++;
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (!checkIsIntersection(subtask)) {
            Epic subtaskEpic = subtask.getEpic();
            if (epics.containsKey(subtaskEpic.getId())) {
                subtask.setId(counter);
                counter++;
                subtasks.put(subtask.getId(), subtask);
                prioritizedTasks.add(subtask);
                subtaskEpic.setStatus(getUpdatedEpicStatus(subtaskEpic));
                subtaskEpic.updateEndTime();
            }
        }
    }

    @Override
    public void updateTask(Task updatedTask) {
        if (!checkIsIntersection(updatedTask)) {
            if (tasks.containsKey(updatedTask.getId())) {
                tasks.put(updatedTask.getId(), updatedTask);
            }
        }
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        if (epics.containsKey(updatedEpic.getId())) {
            updatedEpic.setStatus(getUpdatedEpicStatus(updatedEpic));
            updatedEpic.updateEndTime();
            epics.put(updatedEpic.getId(), updatedEpic);
        }
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        if (!checkIsIntersection(updatedSubtask)) {
            if (subtasks.containsKey(updatedSubtask.getId())) {
                Epic updatedSubtaskEpic = updatedSubtask.getEpic();
                updatedSubtaskEpic.setStatus(getUpdatedEpicStatus(updatedSubtaskEpic));
                prioritizedTasks.remove(updatedSubtask);
                prioritizedTasks.add(updatedSubtask);
                updatedSubtaskEpic.updateEndTime();
                subtasks.put(updatedSubtask.getId(), updatedSubtask);
            }
        }
    }

    @Override
    public void deleteTask(int id) {
        prioritizedTasks.remove(tasks.get(id));
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Iterator<Integer> iterator = subtasks.keySet().iterator();
            while (iterator.hasNext()) {
                Integer nextId = iterator.next();
                if (subtasks.get(nextId).getEpic().getId() == id) {
                    historyManager.remove(nextId);
                    iterator.remove();
                }
            }
            epics.get(id).getEpicSubtasks().forEach(prioritizedTasks::remove);
            epics.get(id).deleteAllEpicSubtasks();
            historyManager.remove(id);
            epics.remove(id);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Epic subtaskEpic = subtasks.get(id).getEpic();
            subtaskEpic.deleteEpicSubtask(subtasks.get(id));
            prioritizedTasks.remove(subtasks.get(id));
            subtasks.remove(id);
            historyManager.remove(id);
            subtaskEpic.setStatus(getUpdatedEpicStatus(subtaskEpic));
            subtaskEpic.updateEndTime();
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksByEpic(Epic epic) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (int subtaskId : subtasks.keySet()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask.getEpic() == epic) {
                epicSubtasks.add(subtask);
            }
        }
        return epicSubtasks;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    private Status getUpdatedEpicStatus(Epic epic) {
        ArrayList<Subtask> epicSubtasks = getSubtasksByEpic(epic);

        if (epicSubtasks.isEmpty()) {
            return Status.NEW;
        }

        int doneCounter = 0;
        int newCounter = 0;

        for (Subtask subtask : epicSubtasks) {
            if (Status.DONE.equals(subtask.getStatus())) {
                doneCounter++;
            } else if (Status.NEW.equals(subtask.getStatus())) {
                newCounter++;
            }
        }

        if (doneCounter == epicSubtasks.size()) {
            return Status.DONE;
        } else if (newCounter == epicSubtasks.size()) {
            return Status.NEW;
        } else {
            return Status.IN_PROGRESS;
        }
    }

    private boolean checkIsIntersection(Task task) {
        boolean isIntersection = false;
        for (Task prioritizedTask : getPrioritizedTasks()) {
            if (prioritizedTask.getStartTime() != null && task.getStartTime() != null) {
                if (prioritizedTask.getStartTime().isAfter(task.getStartTime()) && prioritizedTask.getEndTime().
                        isBefore(task.getEndTime())) {
                    isIntersection = true;
                    break;
                } else if (prioritizedTask.getStartTime().isBefore(task.getStartTime()) && prioritizedTask.getEndTime().
                        isAfter(task.getStartTime())) {
                    isIntersection = true;
                    break;
                } else if (prioritizedTask.getStartTime().isBefore(task.getEndTime()) && prioritizedTask.getEndTime().
                        isAfter(task.getEndTime())) {
                    isIntersection = true;
                    break;
                }
            }
        }
        return isIntersection;
    }
}
