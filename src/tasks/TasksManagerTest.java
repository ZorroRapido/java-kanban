package tasks;

import managers.TasksManager;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TasksManagerTest<T extends TasksManager> {
    protected final T manager;
    protected static final String TASK_NAME = "TaskName";
    protected static final String TASK_DESC = "TaskDescription";
    protected static final String EPIC_NAME = "EpicName";
    protected static final String EPIC_DESC = "EpicDescription";
    protected static final String SUBTASK_NAME = "Subtask";
    protected static final String SUBTASK_DESC = "SubtaskDescription";


    protected TasksManagerTest(T manager) {
        this.manager = manager;
    }

    @Test
    public void shouldReturnNewStatusForEmptyEpic() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        manager.createEpic(epic);
        assertEquals(Status.NEW, epic.getStatus(), "Неверный статус.");
    }

    @Test
    public void shouldReturnNewStatusForEpicWithAllNewSubtasks() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        Subtask firstSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);
        Subtask secondSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);

        manager.createEpic(epic);
        manager.createSubtask(firstSubtask);
        manager.createSubtask(secondSubtask);

        assertEquals(Status.NEW, epic.getStatus(), "Неверный статус.");
    }

    @Test
    public void shouldReturnDoneStatusForEpicWithAllDoneSubtasks() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        Subtask firstSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);
        Subtask secondSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);

        manager.createEpic(epic);
        manager.createSubtask(firstSubtask);
        manager.createSubtask(secondSubtask);

        firstSubtask.setStatus(Status.DONE);
        secondSubtask.setStatus(Status.DONE);

        manager.updateSubtask(firstSubtask);
        manager.updateSubtask(secondSubtask);

        assertEquals(Status.DONE, epic.getStatus(), "Неверный статус.");
    }

    @Test
    public void shouldReturnInProgressStatusForEpicWithNewAndDoneSubtasks() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        Subtask firstSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);
        Subtask secondSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);

        manager.createEpic(epic);
        manager.createSubtask(firstSubtask);
        manager.createSubtask(secondSubtask);

        firstSubtask.setStatus(Status.NEW);
        secondSubtask.setStatus(Status.DONE);

        manager.updateSubtask(firstSubtask);
        manager.updateSubtask(secondSubtask);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Неверный статус.");
    }

    @Test
    public void shouldReturnInProgressStatusForEpicWithAllInProgressTasks() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        Subtask firstSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);
        Subtask secondSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);

        manager.createEpic(epic);
        manager.createSubtask(firstSubtask);
        manager.createSubtask(secondSubtask);

        firstSubtask.setStatus(Status.IN_PROGRESS);
        secondSubtask.setStatus(Status.IN_PROGRESS);

        manager.updateSubtask(firstSubtask);
        manager.updateSubtask(secondSubtask);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void createTaskTest() {
        Task task = new Task(TASK_NAME, TASK_DESC);
        manager.createTask(task);

        final Task savedTask = manager.getTaskById(task.getId());

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        assertEquals(Status.NEW, task.getStatus(), "У задачи неверный статус.");

        final HashMap<Integer, Task> tasks = manager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void createEpicTest() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        manager.createEpic(epic);

        final Epic savedEpic = manager.getEpicById(epic.getId());

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        assertEquals(Status.NEW, epic.getStatus(), "У эпика неверный статус.");
        assertEquals(0, epic.getEpicSubtasks().size(), "У эпика неверное кол-во подзадач.");
        assertEquals(0, manager.getSubtasksByEpic(epic).size(), "В менеджере неверное кол-во подзадач эпика.");
        assertEquals(epic.getEpicSubtasks(), manager.getSubtasksByEpic(epic), "Кол-во подзадач эпика в менеджере" +
                "не соответствует ожидаемому.");

        final HashMap<Integer, Epic> epics = manager.getEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    public void createSubtaskTest() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        Subtask subtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);

        manager.createEpic(epic);
        manager.createSubtask(subtask);

        final Subtask savedSubtask = manager.getSubtaskById(subtask.getId());

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        assertEquals(Status.NEW, subtask.getStatus(), "У подзадачи неверный статус.");
        assertNotNull(manager.getEpicById(epic.getId()), "Эпик подзадачи не найден.");

        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(subtask.getId()), "Подзадачи не совпадают.");
    }

    @Test
    public void getTasksTest() {
        Task firstTask = new Task(TASK_NAME, TASK_DESC);
        Task secondTask = new Task(TASK_NAME, TASK_DESC);

        manager.createTask(firstTask);
        manager.createTask(secondTask);

        assertNotNull(firstTask, "Первая задача не найдена.");
        assertNotNull(secondTask, "Вторая задача не найдена.");

        final HashMap<Integer, Task> tasks = manager.getTasks();

        assertEquals(2, tasks.size(), "Неверное количество задач.");
    }

    @Test
    public void getTasksForEmptyTaskMapTest() {
        assertEquals(0, manager.getTasks().size(), "Неверное количество задач.");
    }

    @Test
    public void getEpicsTest() {
        Epic firstEpic = new Epic(EPIC_NAME, EPIC_DESC);
        Epic secondEpic = new Epic(EPIC_NAME, EPIC_DESC);

        manager.createEpic(firstEpic);
        manager.createEpic(secondEpic);

        assertNotNull(firstEpic, "Первый эпик не найден.");
        assertNotNull(secondEpic, "Второй эпик не найден.");

        final HashMap<Integer, Epic> epics = manager.getEpics();

        assertEquals(2, epics.size(), "Неверное количество эпиков.");
    }

    @Test
    public void getEpicsForEmptyEpicMapTest() {
        assertEquals(0, manager.getEpics().size(), "Неверное количество эпиков.");
    }

    @Test
    public void getSubtasksTest() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        Subtask firstSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);
        Subtask secondSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);

        manager.createEpic(epic);
        manager.createSubtask(firstSubtask);
        manager.createSubtask(secondSubtask);

        assertNotNull(firstSubtask, "Первая подзадача не найдена.");
        assertNotNull(secondSubtask, "Вторая подзадача не найдена.");

        assertEquals(2, manager.getSubtasksByEpic(epic).size(), "Неверное количество подзадач эпика в менеджере.");
        assertEquals(2, epic.getEpicSubtasks().size(), "Неверное количество подзадач эпика.");

        final HashMap<Integer, Subtask> subtasks = manager.getSubtasks();

        assertEquals(2, subtasks.size(), "Неверное количество подзадач.");
    }

    @Test
    public void getSubtasksForEmptySubtaskMapTest() {
        assertEquals(0, manager.getSubtasks().size(), "Неверное количество подзадач.");
    }

    @Test
    public void deleteAllTasksTest() {
        Task firstTask = new Task(TASK_NAME, TASK_DESC);
        Task secondTask = new Task(TASK_NAME, TASK_DESC);

        manager.createTask(firstTask);
        manager.createTask(secondTask);

        assertNotNull(firstTask, "Первая задача не найдена.");
        assertNotNull(secondTask, "Вторая задача не найдена.");

        manager.deleteAllTasks();

        assertEquals(0, manager.getTasks().size(), "Неверное количество задач.");
    }

    @Test
    public void deleteAllTasksForEmptyTaskMapTest() {
        assertEquals(0, manager.getTasks().size(), "Неверное количество задач.");
    }

    @Test
    public void deleteAllEpicsTest() {
        Epic firstEpic = new Epic(EPIC_NAME, EPIC_DESC);
        Epic secondEpic = new Epic(EPIC_NAME, EPIC_DESC);

        manager.createEpic(firstEpic);
        manager.createEpic(secondEpic);

        assertNotNull(firstEpic, "Первый эпик не найден.");
        assertNotNull(secondEpic, "Второй эпик не найден.");

        manager.deleteAllEpics();

        assertEquals(0, manager.getEpics().size(), "Неверное количество эпиков.");
    }

    @Test
    public void deleteAllEpicsForEmptySubtaskMapTest() {
        assertEquals(0, manager.getEpics().size(), "Неверное количество эпиков.");
    }

    @Test
    public void deleteAllSubtasksTest() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        Subtask firstSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);
        Subtask secondSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);

        manager.createEpic(epic);
        manager.createSubtask(firstSubtask);
        manager.createSubtask(secondSubtask);

        assertNotNull(epic, "Эпик не найден.");
        assertNotNull(firstSubtask, "Первая подзадача не найдена.");
        assertNotNull(secondSubtask, "Вторая подзадача не найдена.");

        manager.deleteAllSubtasks();

        assertEquals(Status.NEW, epic.getStatus(), "Неверный статус эпика.");
        assertEquals(0, manager.getSubtasks().size(), "Неверное количество подзадач.");
    }

    @Test
    public void deleteAllSubtasksForEmptySubtaskMapTest() {
        assertEquals(0, manager.getSubtasks().size(), "Неверное количество подзадач.");
    }

    @Test
    public void getTaskById() {
        Task task = new Task(TASK_NAME, TASK_DESC);
        manager.createTask(task);

        assertNotNull(task, "Задача не найдена.");

        final int id = task.getId();
        assertEquals(task, manager.getTaskById(id), "Задачи не совпадают.");
    }

    @Test
    public void getTaskByNonexistentId() {
        assertNull(manager.getTaskById(1));
    }

    @Test
    public void getEpicById() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        manager.createEpic(epic);

        assertNotNull(epic, "Эпик не найден.");

        final int id = epic.getId();
        assertEquals(epic, manager.getEpicById(id), "Эпики не совпадают.");
    }

    @Test
    public void getEpicByNonexistentId() {
        assertNull(manager.getEpicById(1));
    }

    @Test
    public void getSubtaskById() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        Subtask subtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);

        manager.createEpic(epic);
        manager.createSubtask(subtask);

        assertNotNull(epic, "Эпик не найден.");
        assertNotNull(subtask, "Подзадача не найдена.");

        final int id = subtask.getId();
        assertEquals(subtask, manager.getSubtaskById(id), "Подзадачи не совпадают.");
    }

    @Test
    public void getSubtaskByNonexistentId() {
        assertNull(manager.getSubtaskById(1));
    }

    @Test
    public void updateTask() {
        Task task = new Task(TASK_NAME, TASK_DESC);
        manager.createTask(task);

        assertNotNull(task, "Задача не найдена.");

        task.setName("TEST");
        task.setStatus(Status.NEW);
        manager.updateTask(task);

        assertEquals(task, manager.getTaskById(task.getId()));
    }

    @Test
    public void updateNonexistentTask() {
        Task task = new Task(TASK_NAME, TASK_DESC);

        manager.updateTask(task);

        assertNull(manager.getTaskById(task.getId()));
    }

    @Test
    public void updateEpic() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        manager.createEpic(epic);

        assertNotNull(epic, "Эпик не найден.");

        epic.setName("TEST");
        manager.updateEpic(epic);

        assertEquals(epic, manager.getEpicById(epic.getId()));
    }

    @Test
    public void updateNonexistentEpic() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);

        manager.updateEpic(epic);

        assertNull(manager.getEpicById(epic.getId()));
    }

    @Test
    public void updateSubtask() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        manager.createEpic(epic);

        Subtask subtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);
        manager.createSubtask(subtask);

        assertNotNull(subtask, "Подзадача не найдена.");

        subtask.setName("TEST");
        subtask.setStatus(Status.NEW);
        manager.updateSubtask(subtask);

        assertEquals(subtask, manager.getSubtaskById(subtask.getId()));
    }

    @Test
    public void updateNonexistentSubtask() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        Subtask subtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);

        manager.updateSubtask(subtask);

        assertNull(manager.getSubtaskById(subtask.getId()));
    }

    @Test
    public void deleteTask() {
        Task task = new Task(TASK_NAME, TASK_DESC);
        manager.createTask(task);

        assertNotNull(task, "Задача не найдена.");

        assertEquals(1, manager.getTasks().size(), "Неверное количество задач.");

        manager.deleteTask(task.getId());

        assertEquals(0, manager.getTasks().size(), "Неверное количество задач.");
    }

    @Test
    public void deleteNonexistentTask() {
        final int id = 1;
        manager.deleteTask(id);
        assertNull(manager.getTaskById(id));
        assertEquals(0, manager.getTasks().size(), "Неверное количество задач.");
    }

    @Test
    public void deleteEpic() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        manager.createEpic(epic);

        assertNotNull(epic, "Эпик не найден.");

        assertEquals(1, manager.getEpics().size(), "Неверное количество эпиков.");

        manager.deleteEpic(epic.getId());

        assertEquals(0, manager.getEpics().size(), "Неверное количество эпиков.");
    }

    @Test
    public void deleteNonexistentEpic() {
        final int id = 1;
        manager.deleteEpic(id);
        assertNull(manager.getEpicById(id));
        assertEquals(0, manager.getEpics().size(), "Неверное количество эпиков.");
    }

    @Test
    public void deleteSubtask() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        Subtask subtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);

        manager.createEpic(epic);
        manager.createSubtask(subtask);

        assertNotNull(epic, "Эпик не найден.");
        assertNotNull(subtask, "Подзадача не найдена.");

        assertEquals(1, manager.getSubtasks().size(), "Неверное количество подзадач.");

        manager.deleteSubtask(subtask.getId());

        assertEquals(0, manager.getSubtasks().size(), "Неверное количество подзадач.");
    }

    @Test
    public void deleteNonexistentSubtask() {
        final int id = 1;
        manager.deleteSubtask(id);
        assertNull(manager.getSubtaskById(id));
        assertEquals(0, manager.getSubtasks().size(), "Неверное количество подзадач.");
    }
}
