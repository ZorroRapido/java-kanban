package managers;

import java.util.Objects;

public class Node<Task> {
    public Task data;
    public Node<Task> next;
    public Node<Task> prev;

    public Node(Node<Task> prev, Task data, Node<Task> next) {
        this.prev = prev;
        this.data = data;
        this.next = next;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Node<Task> newNode = (Node<Task>) obj;
        return Objects.equals(this.data, newNode.data) &&
                Objects.equals(this.prev, newNode.prev) &&
                Objects.equals(this.next, newNode.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, next, prev);
    }
}
