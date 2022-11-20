import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start();

//        KVTaskClient client = new KVTaskClient("http://localhost:8078");
//        client.put("test_key", "test_value");
//        System.out.println(client.load("test_key"));


//
//        HttpTaskServer httpTaskServer = new HttpTaskServer();
//        httpTaskServer.start();
//
//        TasksManager manager = Managers.getDefault();
//
//        Task task = new Task("Задача", "Описание задачи", LocalDateTime.of(2022, 11,
//                15, 18, 0), 10);
//        Epic epic = new Epic("Эпик", "Описание эпика");
//        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", LocalDateTime.of(2022,
//                11, 15, 15, 0), 15, epic);
//
//        manager.createTask(task);
//        manager.createEpic(epic);
//        manager.createSubtask(subtask);
//
//        manager.getTaskById(task.getId());
//        manager.getSubtaskById(subtask.getId());
//
//        System.out.println(manager.getHistory());
//
//        HTTPTaskManager newManager = HTTPTaskManager.loadFromServer("http://localhost:8078");
//
//        System.out.println(newManager.getHistory());
    }
}
