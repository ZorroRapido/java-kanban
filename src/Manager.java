import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Manager {
    private int counter;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;

    Manager() {
        counter = 0;
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values())
            epic.deleteAllEpicSubtasks();
    }

    public Task getTaskById(int id) {
        return tasks.getOrDefault(id, null);
    }

    public Epic getEpicById(int id) {
        return epics.getOrDefault(id, null);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.getOrDefault(id, null);
    }

    public void createTask(Task task) {
        task.setId(counter);
        counter++;
        tasks.put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        epic.setId(counter);
        counter++;
        epics.put(epic.getId(), epic);
    }

    public void createSubtask(Subtask subtask) {
        Epic subtaskEpic = subtask.getEpic();
        if (epics.containsKey(subtaskEpic.getId())) {
            subtask.setId(counter);
            counter++;
            subtasks.put(subtask.getId(), subtask);
            subtaskEpic.setStatus(getUpdatedEpicStatus(subtaskEpic));
        }
    }

    public void updateTask(Task updatedTask) {
        if (tasks.containsKey(updatedTask.getId()))
            tasks.put(updatedTask.getId(), updatedTask);
    }

    public void updateEpic(Epic updatedEpic) {
        if (epics.containsKey(updatedEpic.getId())) {
            updatedEpic.setStatus(getUpdatedEpicStatus(updatedEpic));
            epics.put(updatedEpic.getId(), updatedEpic);
        }
    }

    public void updateSubtask(Subtask updatedSubtask) {
        if (subtasks.containsKey(updatedSubtask.getId())) {
            Epic updatedSubtaskEpic = updatedSubtask.getEpic();
            updatedSubtaskEpic.setStatus(getUpdatedEpicStatus(updatedSubtaskEpic));
            subtasks.put(updatedSubtask.getId(), updatedSubtask);
        }
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Iterator<Integer> iterator = subtasks.keySet().iterator();
            while (iterator.hasNext()) {
                if (subtasks.get(iterator.next()).getEpic().getId() == id)
                    iterator.remove();
            }
            getEpicById(id).deleteAllEpicSubtasks();
        }
        epics.remove(id);
    }

    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Epic subtaskEpic = getSubtaskById(id).getEpic();
            subtaskEpic.deleteEpicSubtask(getSubtaskById(id));
            subtasks.remove(id);
            subtaskEpic.setStatus(getUpdatedEpicStatus(subtaskEpic));
        }
    }

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

    private String getUpdatedEpicStatus(Epic epic) {
        ArrayList<Subtask> epicSubtasks = getSubtasksByEpic(epic);

        if (epicSubtasks.isEmpty()) {
            return "NEW";
        }

        int doneCounter = 0;
        int newCounter = 0;

        for (Subtask subtask : epicSubtasks) {
            if (subtask.getStatus().equals("DONE")) {
                doneCounter++;
            } else if (subtask.getStatus().equals("NEW")) {
                newCounter++;
            }
        }

        if (doneCounter == epicSubtasks.size()) {
            return "DONE";
        } else if (newCounter == epicSubtasks.size()) {
            return "NEW";
        } else {
            return "IN_PROGRESS";
        }
    }
}
