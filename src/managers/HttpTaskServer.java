package managers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static TasksManager manager;
    private HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        manager = Managers.getDefault();

        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start();
    }

    static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            Gson gson = new Gson();

            URI uri = httpExchange.getRequestURI();
            String path = uri.getPath();
            String query = httpExchange.getRequestURI().getQuery();

            switch (httpExchange.getRequestMethod()) {
                case "GET":
                    if ((path.equals("/tasks/task/")) && (query == null)) {
                        System.out.println("GET /tasks/task");

                        HashMap<Integer, Task> tasks = manager.getTasks();
                        String response = gson.toJson(tasks);

                        httpExchange.sendResponseHeaders(200, 0);

                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else if ((path.equals("/tasks/task/")) && (getParams(query).containsKey("id"))) {
                        System.out.println("GET /tasks/task/?id=");

                        String id = getParams(query).get("id");
                        Task task = manager.getTaskById(Integer.parseInt(id));
                        String response = gson.toJson(task);

                        httpExchange.sendResponseHeaders(200, 0);

                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else if ((path.equals("/tasks/subtask/epic/")) && (getParams(query).containsKey("id"))) {
                        System.out.println("GET /tasks/subtask/epic/?id=");

                        String id = getParams(query).get("id");
                        List<Subtask> epicSubtasks = manager.getEpicSubtasks(Integer.parseInt(id));
                        String response = gson.toJson(epicSubtasks);

                        httpExchange.sendResponseHeaders(200, 0);

                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else if ((path.equals("/tasks/history")) && (query == null)) {
                        System.out.println("GET /tasks/history");

                        List<Task> history = manager.getHistory();
                        String response = gson.toJson(history);

                        httpExchange.sendResponseHeaders(200, 0);

                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else if (path.equals("/tasks/") && (query == null)) {
                        System.out.println("GET /tasks/");

                        TreeSet<Task> prioritizedTasks = manager.getPrioritizedTasks();
                        String response = gson.toJson(prioritizedTasks);

                        httpExchange.sendResponseHeaders(200, 0);

                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                        OutputStream os = httpExchange.getResponseBody();
                        os.close();
                    }
                    break;
                case "POST":
                    if (path.equals("/tasks/task/")) {
                        System.out.println("POST /tasks/task/ Body: {...}");

                        httpExchange.sendResponseHeaders(201, 0);

                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

                        Task task = gson.fromJson(body, Task.class);
                        manager.createTask(task);

                        OutputStream os = httpExchange.getResponseBody();
                        os.close();
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                        OutputStream os = httpExchange.getResponseBody();
                        os.close();
                    }
                    break;
                case "DELETE":
                    if ((path.equals("/tasks/task/")) && (query == null)) {
                        System.out.println("DELETE /tasks/task/");

                        httpExchange.sendResponseHeaders(200, 0);
                        manager.deleteAllTasks();

                        OutputStream os = httpExchange.getResponseBody();
                        os.close();
                    } else if ((path.equals("/tasks/task/")) && (getParams(query).containsKey("id"))) {
                        System.out.println("DELETE /tasks/task/id?=");

                        String id = getParams(query).get("id");
                        manager.deleteTask(Integer.parseInt(id));

                        httpExchange.sendResponseHeaders(200, 0);

                        OutputStream os = httpExchange.getResponseBody();
                        os.close();
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                        OutputStream os = httpExchange.getResponseBody();
                        os.close();
                    }
                    break;
                default:
                    httpExchange.sendResponseHeaders(400, 0);
                    OutputStream os = httpExchange.getResponseBody();
                    os.close();
                    break;
            }
        }

        private static Map<String, String> getParams(String query) {
            Map<String, String> params = new HashMap<>();
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    params.put(entry[0], entry[1]);
                } else {
                    params.put(entry[0], "");
                }
            }
            return params;
        }
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer taskServer = new HttpTaskServer();

//        URI uri = URI.create("http://localhost:8080/tasks/task");
//
//        HashMap<String, String> values = new HashMap<>();
//        values.put("name", "Task");
//        values.put("description", "Description");
//
//        Gson gson = new Gson();
//
//        System.out.println(gson.toJson(values));
//
//        String requestBody = gson.toJson(values);
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                .uri(uri)
//                .build();
//
//        HttpClient client = HttpClient.newHttpClient();
//
//        try {
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println(response.body());
//            System.out.println();
//        } catch (IOException | InterruptedException e) {
//            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и " +
//                    "повторите попытку.");
//        }
    }
}
