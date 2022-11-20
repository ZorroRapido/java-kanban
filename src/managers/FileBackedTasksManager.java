package managers;

import tasks.Task;
import tasks.Epic;
import tasks.Subtask;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;

import java.io.IOException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTasksManager {
    private File file;

    public FileBackedTasksManager() {
    }

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("data.csv");
        FileBackedTasksManager firstManager = new FileBackedTasksManager(file);

        Task testTask = new Task("Купить билеты в Мексику", "Описание 1", LocalDateTime.of(2022,
                11, 12, 18, 16), 15);
        Epic testEpic = new Epic("Переезд", "Нужно завершить до 31 августа");
        Subtask testSubtask = new Subtask("Собрать коробки", "Описание П-1", LocalDateTime.of(2022,
                11, 12, 18, 16), 15, testEpic);

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
            bw.write("id,type,name,status,description,start_time,duration,epic\n");

            for (Task task : tasks.values()) {
                bw.write(CSVFormat.toString(task) + "\n");
            }

            for (Epic epic : epics.values()) {
                bw.write(CSVFormat.toString(epic) + "\n");
            }

            for (Subtask subtask : subtasks.values()) {
                bw.write(CSVFormat.toString(subtask) + "\n");
            }

            bw.write("\n");
            bw.write(CSVFormat.historyToString(historyManager));
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

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();

            while (br.ready()) {
                String line = br.readLine();
                if (!line.isEmpty()) {
                    Task task = CSVFormat.fromString(line, manager);
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
                    List<Integer> ids = CSVFormat.historyFromString(historyLine);

                    for (Integer id : ids) {
                        if (manager.getTasks().containsKey(id)) {
                            manager.historyManager.add(manager.getTasks().get(id));
                        } else if (manager.getEpics().containsKey(id)) {
                            manager.historyManager.add(manager.getEpics().get(id));
                        } else if (manager.getSubtasks().containsKey(id)) {
                            manager.historyManager.add(manager.getSubtasks().get(id));
                        }
                    }
                }
            }
            manager.save();
            return manager;
        }
    }
}
