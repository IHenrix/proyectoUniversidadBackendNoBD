package pe.edu.utp.uni.app.struct;

public class SinglyLinkedList<T> {
    public static class Node<T> { public T value; public Node<T> next; public Node(T v){ this.value=v; } }

    private Node<T> head;
    private int size = 0;

    public Node<T> addLast(T v){
        Node<T> n = new Node<>(v);
        if (head == null) { head = n; }
        else { Node<T> c = head; while (c.next != null) c = c.next; c.next = n; }
        size++; return n;
    }
    public Node<T> addSorted(T v, java.util.Comparator<T> cmp){
        Node<T> n = new Node<>(v);
        if (head == null || cmp.compare(v, head.value) <= 0) {
            n.next = head; head = n; size++; return n;
        }
        Node<T> cur = head;
        while (cur.next != null && cmp.compare(v, cur.next.value) > 0) cur = cur.next;
        n.next = cur.next; cur.next = n; size++; return n;
    }

    public Node<T> head(){ return head; }
    public int size(){ return size; }

    public void setHead(Node<T> newHead) { this.head = newHead; }

    public boolean remove(T value) {
        if (head == null) return false;
        if (head.value == value || (head.value != null && head.value.equals(value))) {
            head = head.next;
            size = Math.max(0, size - 1);
            return true;
        }
        Node<T> prev = head;
        Node<T> cur = head.next;
        while (cur != null) {
            if (cur.value == value || (cur.value != null && cur.value.equals(value))) {
                prev.next = cur.next;
                size = Math.max(0, size - 1);
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }
}
