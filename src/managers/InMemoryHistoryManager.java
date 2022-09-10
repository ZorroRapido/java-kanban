package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    private LinkedList<Task> history;

    public InMemoryHistoryManager() {
        history = new LinkedList<>();
    }

    @Override
    public void add(Task task) {
        if (history.size() < 10) {
            history.addFirst(task);
        } else {
            history.pollLast();
            history.addFirst(task);
        }
    }

    @Override
    public LinkedList<Task> getHistory() {
        return history;
    }
}
