package managers;

import tasks.Task;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private CustomLinkedList<Task> history;
    private Map<Integer, Node<Task>> taskIdToNode;

    public InMemoryHistoryManager() {
        history = new CustomLinkedList<>();
        taskIdToNode = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (taskIdToNode.containsKey(task.getId()))
            remove(task.getId());

        history.linkLast(task);
        taskIdToNode.put(task.getId(), history.tail);
    }

    @Override
    public void remove(int id) {
        if (taskIdToNode.containsKey(id))
            history.removeNode(taskIdToNode.get(id));
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history.getTasks();
    }

    private class CustomLinkedList<Task> {
        private Node<Task> head;
        private Node<Task> tail;

        public CustomLinkedList() {
            head = null;
            tail = null;
        }

        public void linkLast(Task task) {
            final Node<Task> oldTail = tail;
            final Node<Task> newTail = new Node<>(oldTail, task, null);
            tail = newTail;

            if (oldTail == null)
                head = newTail;
            else
                oldTail.next = newTail;
        }

        public ArrayList<Task> getTasks() {
            Node<Task> startPoint = tail;
            ArrayList<Task> tasks = new ArrayList<>();
            while (startPoint != null) {
                tasks.add(startPoint.data);
                startPoint = startPoint.prev;
            }
            return tasks;
        }

        public void removeNode(Node<Task> node) {
            if (node.prev != null)
                node.prev.next = node.next;
            else
                head = node.next;

            if (node.next != null)
                node.next.prev = node.prev;
            else
                tail = node.prev;
        }
    }
}
