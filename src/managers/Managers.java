package managers;

public class Managers {
    public static TasksManager getDefault() {
        return new HTTPTaskManager("http://localhost:8078");
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
