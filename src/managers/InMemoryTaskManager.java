package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class InMemoryTaskManager implements TaskManager {
    private int counter;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    private HistoryManager historyManager;

    public InMemoryTaskManager() {
        counter = 0;
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
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
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubtasks();
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Integer subtaskId : subtasks.keySet())
            historyManager.remove(subtaskId);

        subtasks.clear();

        for (Epic epic : epics.values())
            epic.deleteAllEpicSubtasks();
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
        task.setId(counter);
        counter++;
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(counter);
        counter++;
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        Epic subtaskEpic = subtask.getEpic();
        if (epics.containsKey(subtaskEpic.getId())) {
            subtask.setId(counter);
            counter++;
            subtasks.put(subtask.getId(), subtask);
            subtaskEpic.setStatus(getUpdatedEpicStatus(subtaskEpic));
        }
    }

    @Override
    public void updateTask(Task updatedTask) {
        if (tasks.containsKey(updatedTask.getId())) {
            tasks.put(updatedTask.getId(), updatedTask);
        }
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        if (epics.containsKey(updatedEpic.getId())) {
            updatedEpic.setStatus(getUpdatedEpicStatus(updatedEpic));
            epics.put(updatedEpic.getId(), updatedEpic);
        }
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        if (subtasks.containsKey(updatedSubtask.getId())) {
            Epic updatedSubtaskEpic = updatedSubtask.getEpic();
            updatedSubtaskEpic.setStatus(getUpdatedEpicStatus(updatedSubtaskEpic));
            subtasks.put(updatedSubtask.getId(), updatedSubtask);
        }
    }

    @Override
    public void deleteTask(int id) {
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
            subtasks.remove(id);
            historyManager.remove(id);
            subtaskEpic.setStatus(getUpdatedEpicStatus(subtaskEpic));
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
}
