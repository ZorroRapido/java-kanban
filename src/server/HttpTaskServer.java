package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;
import managers.Managers;
import managers.TasksManager;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class HttpTaskServer {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static TasksManager manager;
    private final HttpServer httpServer;

    public HttpTaskServer(int port) throws IOException {
        manager = Managers.getDefault();

        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(port), 0);
        httpServer.createContext("/tasks", new TasksHandler());
    }

    public HttpTaskServer() throws IOException {
        manager = Managers.getDefault();

        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks", new TasksHandler());
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }

    static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            Gson gson = new Gson();

            URI uri = httpExchange.getRequestURI();
            String path = uri.getPath();
            String query = httpExchange.getRequestURI().getQuery();

            try {
                switch (httpExchange.getRequestMethod()) {
                    case "GET":
                        if ((path.equals("/tasks/task/")) && (query == null)) {
                            System.out.println("GET /tasks/task");

                            List<Task> tasks = new ArrayList<>(manager.getTasks().values());
                            String response = gson.toJson(tasks);

                            httpExchange.sendResponseHeaders(200, 0);

                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        } else if ((path.equals("/tasks/task/")) && (getParams(query).containsKey("id"))) {
                            System.out.println("GET /tasks/task/?id=");

                            String id = getParams(query).get("id");
                            Task task = manager.getTaskById(Integer.parseInt(id));
                            String response = task != null ? gson.toJson(task) : "{}";

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
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                        break;
                    case "DELETE":
                        if ((path.equals("/tasks/task/")) && (query == null)) {
                            System.out.println("DELETE /tasks/task/");

                            httpExchange.sendResponseHeaders(200, 0);
                            manager.deleteAllTasks();
                        } else if ((path.equals("/tasks/task/")) && (getParams(query).containsKey("id"))) {
                            System.out.println("DELETE /tasks/task/id?=");

                            String id = getParams(query).get("id");
                            manager.deleteTask(Integer.parseInt(id));

                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                        break;
                    default:
                        httpExchange.sendResponseHeaders(400, 0);
                        break;
                }
            } finally {
                httpExchange.close();
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
}
