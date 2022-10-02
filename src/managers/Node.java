package managers;

import java.util.Objects;

public class Node<Task> {
    private Task data;
    private Node<Task> next;
    private Node<Task> prev;

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

    public Task getData() {
        return data;
    }

    public void setData(Task data) {
        this.data = data;
    }

    public Node<Task> getNext() {
        return next;
    }

    public void setNext(Node<Task> next) {
        this.next = next;
    }

    public Node<Task> getPrev() {
        return prev;
    }

    public void setPrev(Node<Task> prev) {
        this.prev = prev;
    }
}
