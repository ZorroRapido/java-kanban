import jdk.jfr.Description;
import managers.TasksManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        Assertions.assertEquals(Status.NEW, epic.getStatus(), "Неверный статус.");
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
        assertEquals(task, tasks.get(1), "Задачи не совпадают.");
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
        assertEquals(epic, epics.get(1), "Эпики не совпадают.");
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
    public void getTasks() {
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

    // Тесты на работу с историей ...

    @Test
    @Description("Создание двух пересекающихся задач, когда первая начинается после второй, но заканчивается раньше " +
            "её завершения")
    public void createIntersectingTasksFirstTypeIntersection() {
        Task firstTask = new Task(TASK_NAME, TASK_DESC, LocalDateTime.of(2022, 11, 15, 10,
                0), 30);
        Task secondTask = new Task(TASK_NAME, TASK_DESC, LocalDateTime.of(2022, 11, 15, 10,
                15), 5);

        manager.createTask(firstTask);
        manager.createTask(secondTask);

        assertEquals(1, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(firstTask, manager.getTasks().get(1), "В список добавлена неверная задача.");
    }

    @Test
    @Description("Создание двух пересекающихся задач, когда первая начинается до старта второй и заканчивается раньше " +
            "её завершения")
    public void createIntersectingTasksSecondTypeIntersection() {
        Task firstTask = new Task(TASK_NAME, TASK_DESC, LocalDateTime.of(2022, 11, 15, 15,
                0), 10);
        Task secondTask = new Task(TASK_NAME, TASK_DESC, LocalDateTime.of(2022, 11, 15, 15,
                5), 10);

        manager.createTask(firstTask);
        manager.createTask(secondTask);

        assertEquals(1, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(firstTask, manager.getTasks().get(1), "В список добавлена неверная задача.");
    }

    @Test
    @Description("Создание двух пересекающихся задач, когда первая начинается после старта второй и заканчивается после " +
            "её завершения")
    public void createIntersectingTasksThirdTypeIntersection() {
        Task firstTask = new Task(TASK_NAME, TASK_DESC, LocalDateTime.of(2022, 11, 15, 15,
                5), 10);
        Task secondTask = new Task(TASK_NAME, TASK_DESC, LocalDateTime.of(2022, 11, 15, 15,
                0), 10);

        manager.createTask(firstTask);
        manager.createTask(secondTask);

        assertEquals(1, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(firstTask, manager.getTasks().get(firstTask.getId()), "В список добавлена неверная задача.");
    }

    @Test
    public void createNonintersectingTasks() {
        Task firstTask = new Task(TASK_NAME, TASK_DESC, LocalDateTime.of(2022, 11, 15, 15,
                5), 10);
        Task secondTask = new Task(TASK_NAME, TASK_DESC, LocalDateTime.of(2022, 11, 15, 16,
                0), 10);

        manager.createTask(firstTask);
        manager.createTask(secondTask);

        assertEquals(2, manager.getTasks().size(), "Неверное количество задач.");
        assertEquals(firstTask, manager.getTasks().get(firstTask.getId()), "В список добавлена неверная задача.");
        assertEquals(secondTask, manager.getTasks().get(secondTask.getId()), "В список добавлена неверная задача.");
    }

    @Test
    @Description("Создание двух пересекающихся подзадач, когда первая начинается после второй, но заканчивается " +
            "раньше её завершения")
    public void createIntersectingSubtasksFirstTypeIntersection() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);

        Subtask firstSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, LocalDateTime.of(2022, 11, 15,
                10, 0), 30, epic);
        Subtask secondSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, LocalDateTime.of(2022, 11, 15,
                10, 15), 5, epic);

        manager.createEpic(epic);
        manager.createSubtask(firstSubtask);
        manager.createSubtask(secondSubtask);

        assertEquals(1, manager.getSubtasks().size(), "Неверное количество подзадач.");
        assertEquals(firstSubtask, manager.getSubtasks().get(firstSubtask.getId()), "В список добавлена неверная подзадача.");
    }

    @Test
    @Description("Создание двух пересекающихся подзадач, когда первая начинается до старта второй и заканчивается " +
            "раньше её завершения")
    public void createIntersectingSubtasksSecondTypeIntersection() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);

        Subtask firstSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, LocalDateTime.of(2022, 11, 15,
                15, 0), 10, epic);
        Subtask secondSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, LocalDateTime.of(2022, 11, 15,
                15, 5), 10, epic);

        manager.createEpic(epic);
        manager.createSubtask(firstSubtask);
        manager.createSubtask(secondSubtask);

        assertEquals(1, manager.getSubtasks().size(), "Неверное количество подзадач.");
        assertEquals(firstSubtask, manager.getSubtasks().get(firstSubtask.getId()), "В список добавлена неверная подзадача.");
    }

    @Test
    @Description("Создание двух пересекающихся подзадач, когда первая начинается после старта второй и заканчивается " +
            "после её завершения")
    public void createIntersectingSubtasksThirdTypeIntersection() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);

        Subtask firstSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, LocalDateTime.of(2022, 11, 15,
                15, 5), 10, epic);
        Subtask secondSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, LocalDateTime.of(2022, 11, 15,
                15, 0), 10, epic);

        manager.createEpic(epic);
        manager.createSubtask(firstSubtask);
        manager.createSubtask(secondSubtask);

        assertEquals(1, manager.getSubtasks().size(), "Неверное количество подзадач.");
        assertEquals(firstSubtask, manager.getSubtasks().get(firstSubtask.getId()), "В список добавлена неверная подзадача.");
    }

    @Test
    public void createNonintersectingSubtasks() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);

        Subtask firstSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, LocalDateTime.of(2022, 11, 15, 15,
                5), 10, epic);
        Subtask secondSubtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, LocalDateTime.of(2022, 11, 15, 16,
                0), 10, epic);

        manager.createEpic(epic);
        manager.createSubtask(firstSubtask);
        manager.createSubtask(secondSubtask);

        assertEquals(2, manager.getSubtasks().size(), "Неверное количество подзадач.");
        assertEquals(firstSubtask, manager.getSubtasks().get(firstSubtask.getId()), "В список добавлена неверная подзадача.");
        assertEquals(secondSubtask, manager.getSubtasks().get(secondSubtask.getId()), "В список добавлена неверная подзадача.");
    }

    @Test
    public void getHistory() {
        Task task = new Task(TASK_NAME, TASK_DESC);
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        Subtask subtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);

        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(subtask);

        manager.getTaskById(task.getId());
        manager.getSubtaskById(subtask.getId());

        final List<Task> history = manager.getHistory();
        assertEquals(2, history.size(), "В историю попали лишние записи.");
    }

    @Test
    public void getHistoryForEmptyTaskList() {
        assertTrue(manager.getHistory().isEmpty(), "История непустая.");
    }

    @Test
    public void getHistoryAfterDoubleCalling() {
        Task task = new Task(TASK_NAME, TASK_DESC);

        manager.createTask(task);
        manager.getTaskById(task.getId());
        manager.getTaskById(task.getId());

        final List<Task> history = manager.getHistory();
        assertEquals(1, history.size(), "Неверный размер истории.");
        assertTrue(history.contains(task), "История не содержит нужную задачу.");
    }

    @Test
    public void getHistoryAfterTaskDeleteBeginning() {
        Task task = new Task(TASK_NAME, TASK_DESC);
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        Subtask subtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);

        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.createTask(task);

        manager.getTaskById(task.getId());
        manager.getSubtaskById(subtask.getId());

        manager.deleteTask(task.getId());

        final List<Task> history = manager.getHistory();
        assertEquals(1, history.size(), "Неверный размер истории.");
        assertTrue(history.contains(subtask), "История не содержит нужную подзадачу.");
        assertFalse(history.contains(task), "История содержит удаленную задачу.");
    }

    @Test
    public void getHistoryAfterTaskDeleteMiddle() {
        Task task = new Task(TASK_NAME, TASK_DESC);
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        Subtask subtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);

        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.createTask(task);

        manager.getEpicById(epic.getId());
        manager.getTaskById(task.getId());
        manager.getSubtaskById(subtask.getId());

        manager.deleteTask(task.getId());

        final List<Task> history = manager.getHistory();
        assertEquals(2, history.size(), "Неверный размер истории.");
        assertEquals(epic, history.get(1), "Неверный эпик в истории.");
        assertEquals(subtask, history.get(0), "Неверная подзадача в истории.");
    }

    @Test
    public void getHistoryAfterTaskDeleteEnd() {
        Task task = new Task(TASK_NAME, TASK_DESC);
        Epic epic = new Epic(EPIC_NAME, EPIC_DESC);
        Subtask subtask = new Subtask(SUBTASK_NAME, SUBTASK_DESC, epic);

        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.createTask(task);

        manager.getEpicById(epic.getId());
        manager.getTaskById(task.getId());
        manager.getSubtaskById(subtask.getId());

        manager.deleteSubtask(subtask.getId());

        final List<Task> history = manager.getHistory();
        assertEquals(2, history.size(), "Неверный размер истории.");
        assertEquals(epic, history.get(1), "Неверный эпик в истории.");
        assertEquals(task, history.get(0), "Неверная задача в истории.");
    }


}
