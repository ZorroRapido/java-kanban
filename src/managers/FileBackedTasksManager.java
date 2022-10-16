package managers;

import tasks.Task;
import tasks.Epic;
import tasks.Subtask;

import tasks.TaskType;
import tasks.Status;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;

import java.io.IOException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTasksManager implements TasksManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("data.csv");
        FileBackedTasksManager firstManager = new FileBackedTasksManager(file);

        Task testTask = new Task("Купить билеты в Мексику", "Описание 1");
        Epic testEpic = new Epic("Переезд", "Нужно завершить до 31 августа");
        Subtask testSubtask = new Subtask("Собрать коробки", "Описание П-1", testEpic);

        firstManager.createTask(testTask);
        firstManager.createEpic(testEpic);
        firstManager.createSubtask(testSubtask);

        firstManager.getTaskById(testTask.getId());

        firstManager.getEpicById(testEpic.getId());

        System.out.println("История 1-го менеджера:");
        System.out.println(firstManager.getHistory());

        FileBackedTasksManager secondManager = loadFromFile(file);

        System.out.println("История 2-го менеджера:");
        System.out.println(secondManager.getHistory());
    }

    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("id,type,name,status,description,epic\n");

            for (Task task : tasks.values()) {
                bw.write(toString(task) + "\n");
            }

            for (Epic epic : epics.values()) {
                bw.write(toString(epic) + "\n");
            }

            for (Subtask subtask : subtasks.values()) {
                bw.write(toString(subtask) + "\n");
            }

            bw.write("\n");
            bw.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException(String.format("Произошла ошибка при сохранении данных в файл %s", file.getName()));
        }
    }

    @Override
    public HashMap<Integer, Task> getTasks() {
        HashMap<Integer, Task> tasksToReturn = super.getTasks();
        save();
        return tasksToReturn;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        HashMap<Integer, Epic> epicsToReturn = super.getEpics();
        save();
        return epicsToReturn;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtasks() {
        HashMap<Integer, Subtask> subtasksToReturn = super.getSubtasks();
        save();
        return subtasksToReturn;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task taskToReturn = super.getTaskById(id);
        save();
        return taskToReturn;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epicToReturn = super.getEpicById(id);
        save();
        return epicToReturn;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtaskToReturn = super.getSubtaskById(id);
        save();
        return subtaskToReturn;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task updatedTask) {
        super.updateTask(updatedTask);
        save();
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        super.updateEpic(updatedEpic);
        save();
    }

    @Override
    public void updateSubtask(Subtask updatedSubtask) {
        super.updateSubtask(updatedSubtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public ArrayList<Subtask> getSubtasksByEpic(Epic epic) {
        ArrayList<Subtask> subtasksToReturn = super.getSubtasksByEpic(epic);
        save();
        return subtasksToReturn;
    }

    private String toString(Task task) {
        String outputString = String.join(",", Integer.toString(task.getId()), task.getType().toString(),
                task.getName(), task.getStatus().toString(), task.getDescription());

        switch (task.getType()) {
            case TASK:
            case EPIC:
                return outputString;
            case SUBTASK:
                return outputString + "," + ((Subtask) task).getEpic().getId();
            default:
                return null;
        }
    }

    public static Task fromString(String value, FileBackedTasksManager manager) {
        String[] info = value.split(",");

        if (TaskType.TASK.toString().equals(info[1])) {
            Task newTask = new Task(info[2], info[4]);
            newTask.setId(Integer.parseInt(info[0]));
            newTask.setType(TaskType.valueOf(info[1]));
            newTask.setStatus(Status.valueOf(info[3]));
            return newTask;
        } else if (TaskType.EPIC.toString().equals(info[1])) {
            Task newEpic = new Epic(info[2], info[4]);
            newEpic.setId(Integer.parseInt(info[0]));
            newEpic.setType(TaskType.valueOf(info[1]));
            newEpic.setStatus(Status.valueOf(info[3]));
            return newEpic;
        } else if (TaskType.SUBTASK.toString().equals(info[1])) {
            Task newSubtask = new Subtask(info[2], info[4], manager.epics.get(Integer.parseInt(info[5])));
            newSubtask.setId(Integer.parseInt(info[0]));
            newSubtask.setType(TaskType.valueOf(info[1]));
            newSubtask.setStatus(Status.valueOf(info[3]));
            return newSubtask;
        }

        return null;
    }

    public static String historyToString(HistoryManager historyManager) {
        List<Task> historyTasks = historyManager.getHistory();
        String[] arrayOfIds = new String[historyTasks.size()];

        int counter = 0;
        for (Task task : historyTasks) {
            arrayOfIds[counter] = Integer.toString(task.getId());
            counter++;
        }

        return String.join(",", arrayOfIds);
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> list = new ArrayList<>();
        try {
            String[] ids = value.split(",");
            for (String id : ids) {
                list.add(Integer.parseInt(id));
            }
        } catch (NullPointerException e) {
            System.out.println("Ошибка при обработке строки с историей! Возможно она пуста!");
        }
        return list;
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();

            while (br.ready()) {
                String line = br.readLine();
                if (!line.isEmpty()) {
                    Task task = fromString(line, manager);
                    if (task != null) {
                        switch (task.getType()) {
                            case TASK:
                                manager.createTask(task);
                                break;
                            case EPIC:
                                manager.createEpic((Epic) task);
                                break;
                            case SUBTASK:
                                manager.createSubtask((Subtask) task);
                                break;
                        }
                    }
                } else {
                    String historyLine = br.readLine();
                    List<Integer> ids = historyFromString(historyLine);

                    for (Integer id : ids) {
                        if (manager.tasks.containsKey(id)) {
                            manager.historyManager.add(manager.tasks.get(id));
                        } else if (manager.epics.containsKey(id)) {
                            manager.historyManager.add(manager.epics.get(id));
                        } else if (manager.subtasks.containsKey(id)) {
                            manager.historyManager.add(manager.subtasks.get(id));
                        }
                    }
                }
            }
            return manager;
        }
    }
}
