package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import server.KVTaskClient;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    public HTTPTaskManager(String url) {
        client = new KVTaskClient(url);
    }

    @Override
    public void save() {
        Type subtasksIncludedType = new TypeToken<ArrayList<Subtask>>() {}.getType();

        Gson taskGson = new GsonBuilder()
                // .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        Gson epicGson = new GsonBuilder()
                .registerTypeAdapter(subtasksIncludedType, new SubtasksIncludedAdapter())
                // .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        Gson subtaskGson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                // .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        if (!tasks.isEmpty()) {
            String serializedTasks = taskGson.toJson(tasks);
            client.put("tasks", serializedTasks);
        }

        if (!epics.isEmpty()) {
            String serializedEpics = epicGson.toJson(epics);
            client.put("epics", serializedEpics);
        }

        if (!subtasks.isEmpty()) {
            String serializedSubtasks = subtaskGson.toJson(subtasks);
            client.put("subtasks", serializedSubtasks);
        }

        String historyString = CSVFormat.historyToString(historyManager);

        if (!historyString.equals("[]") && !historyString.equals("")) {
            client.put("history", historyString);
        }
    }

//    static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
//        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//
//        @Override
//        public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
//            if (localDateTime != null) {
//                jsonWriter.value(localDateTime.format(formatter));
//            }
//        }
//
//        @Override
//        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
//            if (jsonReader.nextString() != null) {
//                return LocalDateTime.parse(jsonReader.nextString(), formatter);
//            }
//            return null;
//        }
//    }

    static class SubtasksIncludedAdapter extends TypeAdapter<ArrayList<Subtask>> {
        @Override
        public void write(final JsonWriter jsonWriter, final ArrayList<Subtask> subtasks) throws IOException {
            List<String> ids = subtasks.stream()
                    .map(task -> Integer.toString(task.getId()))
                    .collect(Collectors.toList());

            jsonWriter.value(String.join(",", ids));
        }

        @Override
        public ArrayList<Subtask> read(final JsonReader jsonReader) throws IOException {
            jsonReader.nextString();
            return new ArrayList<>();
        }
    }

    class EpicAdapter extends TypeAdapter<Epic> {
        @Override
        public void write(final JsonWriter jsonWriter, final Epic epic) throws IOException {
            jsonWriter.value(Integer.toString(epic.getId()));
        }

        @Override
        public Epic read(final JsonReader jsonReader) throws IOException {
            String epicId = jsonReader.nextString();
            return epics.getOrDefault(Integer.parseInt(epicId), null);
        }
    }

    public static HTTPTaskManager loadFromServer(String url) {
        HTTPTaskManager manager = new HTTPTaskManager(url);

        Type tasksType = new TypeToken<HashMap<Integer, Task>>() {
        }.getType();
        Type epicsType = new TypeToken<HashMap<Integer, Epic>>() {
        }.getType();
        Type subtasksType = new TypeToken<HashMap<Integer, Subtask>>() {
        }.getType();
        Type subtasksIncludedType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();

        Gson taskGson = new Gson();
        Gson epicGson = new GsonBuilder()
                .registerTypeAdapter(subtasksIncludedType, new SubtasksIncludedAdapter())
                .create();

        HashMap<Integer, Task> deserializedTasks = taskGson.fromJson(manager.client.load("tasks"), tasksType);
        HashMap<Integer, Epic> deserializedEpics = epicGson.fromJson(manager.client.load("epics"), epicsType);

        TypeAdapter<Epic> epicAdapter = new TypeAdapter<>() {
            @Override
            public void write(final JsonWriter jsonWriter, final Epic epic) throws IOException {
                jsonWriter.value(Integer.toString(epic.getId()));
            }

            @Override
            public Epic read(final JsonReader jsonReader) throws IOException {
                String epicId = jsonReader.nextString();
                return deserializedEpics.getOrDefault(Integer.parseInt(epicId), null);
            }
        };

        Gson subtaskGson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, epicAdapter)
                .create();

        HashMap<Integer, Subtask> deserializedSubtasks = subtaskGson.fromJson(manager.client.load("subtasks"),
                subtasksType);

        if (deserializedSubtasks != null) {
            for (Subtask subtask : deserializedSubtasks.values()) {
                if ((subtask.getEpic() != null) && (deserializedEpics.containsKey(subtask.getEpic().getId()))) {
                    subtask.getEpic().addSubtask(subtask);
                }
            }
        }

        if (deserializedTasks != null) {
            deserializedTasks.values().forEach(manager::createTask);
        }

        if (deserializedEpics != null) {
            deserializedEpics.values().forEach(manager::createEpic);
        }

        if (deserializedSubtasks != null) {
            deserializedSubtasks.values().forEach(manager::createSubtask);
        }

        String history = manager.client.load("history");
        if (!history.equals("")) {
            String[] ids = history.split(",");

            for (String id : ids) {
                int numericalId = Integer.parseInt(id);
                if (manager.getTasks().containsKey(numericalId)) {
                    manager.historyManager.add(deserializedTasks.get(numericalId));
                } else if (manager.getEpics().containsKey(numericalId)) {
                    manager.historyManager.add(deserializedEpics.get(numericalId));
                } else if (manager.getSubtasks().containsKey(numericalId)) {
                    manager.historyManager.add(deserializedSubtasks.get(numericalId));
                }
            }
        }

        return manager;
    }
}
