package test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import managers.HTTPTaskManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.KVServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class HTTPTaskManagerTest extends test.TasksManagerTest<HTTPTaskManager> {
    protected static KVServer kvServer;
    protected static HttpTaskServer httpTaskServer;
    protected static HttpClient client;
    protected static final String API_URL = "http://localhost:8080";

    protected HTTPTaskManagerTest() {
        super(new HTTPTaskManager("http://localhost:8078"));
    }

    @BeforeAll
    public static void start() throws IOException {
        kvServer = new KVServer();
        kvServer.start();

        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        client = HttpClient.newHttpClient();
    }

    @Test
    void getTasksEndpointTestNegativeCase() throws IOException, InterruptedException {
        HttpResponse<String> getTasksResponse = getTasks();

        assertEquals(200, getTasksResponse.statusCode(), "Неверный статус-код при запросе всех задач.");
        assertEquals("{}", getTasksResponse.body(), "Неверное тело ответа.");
    }

    @Test
    void getTasksEndpointTestPositiveCase() throws IOException, InterruptedException {
        Task task = new Task(TASK_NAME, TASK_DESC);

        HttpResponse<String> createTaskResponse = createTask(task);
        assertEquals(201, createTaskResponse.statusCode(), "Неверный статус-код при создании задачи.");

        HttpResponse<String> getTasksResponse = getTasks();
        assertEquals(200, getTasksResponse.statusCode(), "Неверный статус-код при запросе всех задач.");

        Type tasksHashMapType = new TypeToken<HashMap<Integer, Task>>() {}.getType();
        final HashMap<Integer, Task> savedTasks = new Gson().fromJson(getTasksResponse.body(), tasksHashMapType);

        assertEquals(1, savedTasks.size(), "Неверное количество задач в списке.");
        assertTrue(savedTasks.containsValue(task), "В списке отсутствует созданная задача.");
    }

    @Test
    void getTaskByIdEndpointTestNegativeCase() throws IOException, InterruptedException {
        HttpResponse<String> getTasksResponse = getTaskById(1);

        assertEquals(404, getTasksResponse.statusCode(), "Неверный статус-код при запросе задачи по id.");
    }

    @Test
    void getTaskByIdEndpointTestPositiveCase() throws IOException, InterruptedException {
        Task task = new Task(TASK_NAME, TASK_DESC);

        HttpResponse<String> createTaskResponse = createTask(task);
        assertEquals(201, createTaskResponse.statusCode(), "Неверный статус код при создании задачи.");

        HttpResponse<String> getTaskByIdResponse = getTaskById(1);
        assertEquals(200, getTaskByIdResponse.statusCode(), "Неверный статус-код при запросе задачи по id.");

        Task savedTask = new Gson().fromJson(getTaskByIdResponse.body(), Task.class);
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void createTaskEndpointTestNegativeCase() throws IOException, InterruptedException {
        HttpRequest createTaskRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(API_URL + "/main/tasks/task"))
                .build();

        HttpResponse<String> createTaskResponse = client.send(createTaskRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, createTaskResponse.statusCode(), "Неверный статус-код при создании задачи.");
    }

    @Test
    void createTaskEndpointTestPositiveCase() throws IOException, InterruptedException {
        Task firstTask = new Task(TASK_NAME, TASK_DESC);
        Task secondTask = new Task("SecondTaskName", "SecondTaskDescription");

        HttpResponse<String> createFirstTaskResponse = createTask(firstTask);
        HttpResponse<String> createSecondTaskResponse = createTask(secondTask);

        assertEquals(201, createFirstTaskResponse.statusCode(), "Неверный статус-код при создании " +
                "первой задачи.");
        assertEquals(201, createSecondTaskResponse.statusCode(), "Неверный статус-код при создании " +
                "второй задачи.");

        HttpResponse<String> getTasksResponse = getTasks();
        assertEquals(200, getTasksResponse.statusCode(), "Неверный статус-код при запросе всех задач.");

        Type tasksHashMapType = new TypeToken<HashMap<Integer, Task>>() {}.getType();
        final HashMap<Integer, Task> tasks = new Gson().fromJson(getTasksResponse.body(), tasksHashMapType);

        assertEquals(2, tasks.size(), "Неверное количество задач в списке.");
        assertTrue(tasks.containsValue(firstTask), "В списке отсутствует первая задача.");
        assertTrue(tasks.containsValue(secondTask), "В списке отсутствует вторая задача.");
    }

    @Test
    void deleteTaskByIdEndpointTest() throws IOException, InterruptedException {
        Task task = new Task(TASK_NAME, TASK_DESC);

        HttpResponse<String> createTaskResponse = createTask(task);
        assertEquals(201, createTaskResponse.statusCode(), "Неверный статус-код при создании задачи.");

        HttpResponse<String> deleteTaskByIdResponse = deleteTaskById(1);
        assertEquals(200, deleteTaskByIdResponse.statusCode(), "Неверный статус-код при удалении " +
                "задачи по id.");

        HttpResponse<String> getTasksResponse = getTasks();

        Type tasksHashMapType = new TypeToken<HashMap<Integer, Task>>() {}.getType();
        final HashMap<Integer, Task> savedTasks = new Gson().fromJson(getTasksResponse.body(), tasksHashMapType);

        assertFalse(savedTasks.containsValue(task), "В списке задач содержится удаленная задача.");
    }

    @Test
    void deleteAllTasksEndpointTest() throws IOException, InterruptedException {
        Task firstTask = new Task(TASK_NAME, TASK_DESC);
        Task secondTask = new Task("SecondTaskName", "SecondTaskDescription");

        HttpResponse<String> createFirstTaskResponse = createTask(firstTask);
        assertEquals(201, createFirstTaskResponse.statusCode(), "Неверный статус-код при создании " +
                        "первой задачи.");

        HttpResponse<String> createSecondTaskResponse = createTask(secondTask);
        assertEquals(201, createSecondTaskResponse.statusCode(), "Неверный статус-код при создании " +
                        "второй задачи.");

        HttpResponse<String> deleteAllTasksResponse = deleteAllTasks();
        assertEquals(200, deleteAllTasksResponse.statusCode(), "Неверный статус-код при удалении " +
                "всех задач");

        HttpResponse<String> getTasksResponse = getTasks();

        Type tasksHashMapType = new TypeToken<HashMap<Integer, Task>>() {}.getType();
        final HashMap<Integer, Task> savedTasks = new Gson().fromJson(getTasksResponse.body(), tasksHashMapType);

        assertEquals(0, savedTasks.size(), "Список задач не пуст.");
    }

    private HttpResponse<String> createTask(Task task) throws IOException, InterruptedException {
        String serializedTask = new Gson().toJson(task);

        HttpRequest createTaskRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(serializedTask))
                .uri(URI.create(API_URL + "/main/tasks/task"))
                .build();

        return client.send(createTaskRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> createEpic(Epic epic) throws IOException, InterruptedException {
        String serializedEpic = new Gson().toJson(epic);

        HttpRequest createEpicRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(serializedEpic))
                .uri(URI.create(API_URL + "/main/tasks/epic"))
                .build();

        return client.send(createEpicRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> createSubtask(Subtask subtask) throws IOException, InterruptedException {
        String serializedSubtask = new Gson().toJson(subtask);

        HttpRequest createSubtaskRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(serializedSubtask))
                .uri(URI.create(API_URL + "/main/tasks/subtask"))
                .build();

        return client.send(createSubtaskRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getTasks() throws IOException, InterruptedException {
        HttpRequest getTasksRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(API_URL + "/main/tasks/task"))
                .build();

        return client.send(getTasksRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getEpics() throws IOException, InterruptedException {
        HttpRequest getEpicsRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(API_URL + "/main/tasks/epic"))
                .build();

        return client.send(getEpicsRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getSubtasks() throws IOException, InterruptedException {
        HttpRequest getSubtasksRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(API_URL + "/main/tasks/subtask"))
                .build();

        return client.send(getSubtasksRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getTaskById(int id) throws IOException, InterruptedException {
        HttpRequest getTaskRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(API_URL + "/main/tasks/task/?id=" + id))
                .build();

        return client.send(getTaskRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getEpicById(int id) throws IOException, InterruptedException {
        HttpRequest getEpicRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(API_URL + "/main/tasks/epic/?id=" + id))
                .build();

        return client.send(getEpicRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getSubtaskById(int id) throws IOException, InterruptedException {
        HttpRequest getSubtaskRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(API_URL + "/main/tasks/subtask/?id=" + id))
                .build();

        return client.send(getSubtaskRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> deleteTaskById(int id) throws IOException, InterruptedException {
        HttpRequest deleteTaskByIdRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(API_URL + "/main/tasks/task/?id=" + id))
                .build();

        return client.send(deleteTaskByIdRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> deleteEpicById(int id) throws IOException, InterruptedException {
        HttpRequest deleteEpicByIdRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(API_URL + "/main/tasks/epic/?id=" + id))
                .build();

        return client.send(deleteEpicByIdRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> deleteSubtaskById(int id) throws IOException, InterruptedException {
        HttpRequest deleteSubtaskByIdRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(API_URL + "/main/tasks/subtask/?id=" + id))
                .build();

        return client.send(deleteSubtaskByIdRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> deleteAllTasks() throws IOException, InterruptedException {
        HttpRequest deleteAllTasksRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(API_URL + "/main/tasks/task"))
                .build();

        return client.send(deleteAllTasksRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> deleteAllEpics() throws IOException, InterruptedException {
        HttpRequest deleteAllEpicsRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(API_URL + "/main/tasks/epic"))
                .build();

        return client.send(deleteAllEpicsRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> deleteAllSubtasks() throws IOException, InterruptedException {
        HttpRequest deleteAllSubtasksRequest = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(API_URL + "/main/tasks/subtask"))
                .build();

        return client.send(deleteAllSubtasksRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getEpicSubtasks(int id) throws IOException, InterruptedException {
        HttpRequest getEpicSubtasksRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(API_URL + "/main/tasks/subtask/epic/?id=" + id))
                .build();

        return client.send(getEpicSubtasksRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getPrioritizedTasks() throws IOException, InterruptedException {
        HttpRequest getEpicSubtasksRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(API_URL + "/main/tasks"))
                .build();

        return client.send(getEpicSubtasksRequest, HttpResponse.BodyHandlers.ofString());
    }

    @AfterEach
    public void clear() throws IOException, InterruptedException {
        deleteAllTasks();

        // добавить очистку подзадач и эпиков
    }

    @AfterAll
    public static void finish() {
        kvServer.stop();
        httpTaskServer.stop();
    }
}
