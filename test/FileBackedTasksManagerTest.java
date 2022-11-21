import managers.FileBackedTasksManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTasksManagerTest extends TasksManagerTest<FileBackedTasksManager> {
    protected FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager(new File("data.csv")));
    }

    @Override
    @Test
    public void shouldReturnNewStatusForEmptyEpic() {
        super.shouldReturnNewStatusForEmptyEpic();
    }

    @Override
    @Test
    public void shouldReturnNewStatusForEpicWithAllNewSubtasks() {
        super.shouldReturnNewStatusForEpicWithAllNewSubtasks();
    }

    @Override
    @Test
    public void shouldReturnDoneStatusForEpicWithAllDoneSubtasks() {
        super.shouldReturnDoneStatusForEpicWithAllDoneSubtasks();
    }

    @Override
    @Test
    public void shouldReturnInProgressStatusForEpicWithNewAndDoneSubtasks() {
        super.shouldReturnInProgressStatusForEpicWithNewAndDoneSubtasks();
    }

    @Override
    @Test
    public void shouldReturnInProgressStatusForEpicWithAllInProgressTasks() {
        super.shouldReturnInProgressStatusForEpicWithAllInProgressTasks();
    }

    @Override
    @Test
    public void createTaskTest() {
        super.createTaskTest();
    }

    @Override
    @Test
    public void createEpicTest() {
        super.createEpicTest();
    }

    @Override
    @Test
    public void createSubtaskTest() {
        super.createSubtaskTest();
    }

    @Override
    @Test
    public void getTasksTest() {
        super.getTasksTest();
    }

    @Override
    @Test
    public void getTasksForEmptyTaskMapTest() {
        super.getTasksForEmptyTaskMapTest();
    }

    @Override
    @Test
    public void getEpicsTest() {
        super.getEpicsTest();
    }

    @Override
    @Test
    public void getEpicsForEmptyEpicMapTest() {
        super.getEpicsForEmptyEpicMapTest();
    }

    @Override
    @Test
    public void getSubtasksTest() {
        super.getSubtasksTest();
    }

    @Override
    @Test
    public void getSubtasksForEmptySubtaskMapTest() {
        super.getSubtasksForEmptySubtaskMapTest();
    }

    @Override
    @Test
    public void deleteAllTasksTest() {
        super.deleteAllTasksTest();
    }

    @Override
    @Test
    public void deleteAllTasksForEmptyTaskMapTest() {
        super.deleteAllTasksForEmptyTaskMapTest();
    }

    @Override
    @Test
    public void deleteAllEpicsTest() {
        super.deleteAllEpicsTest();
    }

    @Override
    @Test
    public void deleteAllEpicsForEmptySubtaskMapTest() {
        super.deleteAllEpicsForEmptySubtaskMapTest();
    }

    @Override
    @Test
    public void deleteAllSubtasksTest() {
        super.deleteAllSubtasksTest();
    }

    @Override
    @Test
    public void deleteAllSubtasksForEmptySubtaskMapTest() {
        super.deleteAllSubtasksForEmptySubtaskMapTest();
    }

    @Override
    @Test
    public void getTaskById() {
        super.getTaskById();
    }

    @Override
    @Test
    public void getTaskByNonexistentId() {
        super.getTaskByNonexistentId();
    }

    @Override
    @Test
    public void getEpicById() {
        super.getEpicById();
    }

    @Override
    @Test
    public void getEpicByNonexistentId() {
        super.getEpicByNonexistentId();
    }

    @Override
    @Test
    public void getSubtaskById() {
        super.getSubtaskById();
    }

    @Override
    @Test
    public void getSubtaskByNonexistentId() {
        super.getSubtaskByNonexistentId();
    }

    @Override
    @Test
    public void updateTask() {
        super.updateTask();
    }

    @Override
    @Test
    public void updateNonexistentTask() {
        super.updateNonexistentTask();
    }

    @Override
    @Test
    public void updateEpic() {
        super.updateEpic();
    }

    @Override
    @Test
    public void updateNonexistentEpic() {
        super.updateNonexistentEpic();
    }

    @Override
    @Test
    public void updateSubtask() {
        super.updateSubtask();
    }

    @Override
    @Test
    public void updateNonexistentSubtask() {
        super.updateNonexistentSubtask();
    }

    @Override
    @Test
    public void deleteTask() {
        super.deleteTask();
    }

    @Override
    @Test
    public void deleteNonexistentTask() {
        super.deleteNonexistentTask();
    }

    @Override
    @Test
    public void deleteEpic() {
        super.deleteEpic();
    }

    @Override
    @Test
    public void deleteNonexistentEpic() {
        super.deleteNonexistentEpic();
    }

    @Override
    @Test
    public void deleteSubtask() {
        super.deleteSubtask();
    }

    @Override
    @Test
    public void deleteNonexistentSubtask() {
        super.deleteNonexistentSubtask();
    }

    @Override
    @Test
    public void createIntersectingTasksFirstTypeIntersection() {
        super.createIntersectingTasksFirstTypeIntersection();
    }

    @Override
    @Test
    public void createIntersectingTasksSecondTypeIntersection() {
        super.createIntersectingTasksSecondTypeIntersection();
    }

    @Override
    @Test
    public void createIntersectingTasksThirdTypeIntersection() {
        super.createIntersectingTasksThirdTypeIntersection();
    }

    @Override
    @Test
    public void createNonintersectingTasks() {
        super.createNonintersectingTasks();
    }

    @Override
    @Test
    public void createIntersectingSubtasksFirstTypeIntersection() {
        super.createIntersectingSubtasksFirstTypeIntersection();
    }

    @Override
    @Test
    public void createIntersectingSubtasksSecondTypeIntersection() {
        super.createIntersectingSubtasksSecondTypeIntersection();
    }

    @Override
    @Test
    public void createIntersectingSubtasksThirdTypeIntersection() {
        super.createIntersectingSubtasksThirdTypeIntersection();
    }

    @Override
    @Test
    public void createNonintersectingSubtasks() {
        super.createNonintersectingSubtasks();
    }


    @Override
    @Test
    public void getHistory() {
        super.getHistory();
    }

    @Override
    @Test
    public void getHistoryForEmptyTaskList() {
        super.getHistoryForEmptyTaskList();
    }

    @Override
    @Test
    public void getHistoryAfterDoubleCalling() {
        super.getHistoryAfterDoubleCalling();
    }

    @Override
    @Test
    public void getHistoryAfterTaskDeleteBeginning() {
        super.getHistoryAfterTaskDeleteBeginning();
    }

    @Override
    @Test
    public void getHistoryAfterTaskDeleteMiddle() {
        super.getHistoryAfterTaskDeleteMiddle();
    }

    @Override
    @Test
    public void getHistoryAfterTaskDeleteEnd() {
        super.getHistoryAfterTaskDeleteEnd();
    }

    @Test
    public void save() throws IOException {
        Task task = new Task(TASK_NAME, TASK_DESC);
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        Subtask subtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);

        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(subtask);

        manager.getSubtaskById(subtask.getId());
        manager.getTaskById(task.getId());

        final HashMap<Integer, Task> tasks = manager.getTasks();
        final HashMap<Integer, Epic> epics = manager.getEpics();
        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();

        final List<Task> history = manager.getHistory();

        manager.save();
        FileBackedTasksManager.loadFromFile(new File("data.csv"));
        assertEquals(history, manager.getHistory(), "История, загруженная из файла - неверна.");
        assertEquals(tasks, manager.getTasks(), "Задачи, загруженный из файла - неверны.");
        assertEquals(epics, manager.getEpics(), "Эпики, загруженный из файла - неверны.");
        assertEquals(subtasks, manager.getSubtasks(), "Подзадачи, загруженный из файла - неверны.");
    }

    @Test
    public void saveEmptyTaskList() throws IOException {
        manager.save();
        FileBackedTasksManager.loadFromFile(new File("data.csv"));
        assertTrue(manager.getTasks().isEmpty(), "Неверное количество задач.");
        assertTrue(manager.getEpics().isEmpty(), "Неверное количество эпиков.");
        assertTrue(manager.getSubtasks().isEmpty(), "Неверное количество подзадач.");
    }

    @Test
    public void saveWithEpicNoSubtasks() throws IOException {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        manager.createEpic(epic);

        manager.save();
        FileBackedTasksManager.loadFromFile(new File("data.csv"));

        assertEquals(epic, manager.getEpics().get(0), "Эпик не найден.");
        assertTrue(manager.getHistory().isEmpty(), "История не пуста.");
    }

    @Test
    public void saveWithEmptyHistory() throws IOException {
        manager.save();
        FileBackedTasksManager.loadFromFile(new File("data.csv"));
        assertTrue(manager.getHistory().isEmpty(), "История просмотра не пуста.");
    }
}
