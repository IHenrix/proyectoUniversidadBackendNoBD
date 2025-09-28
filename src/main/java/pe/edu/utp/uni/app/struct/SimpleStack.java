package pe.edu.utp.uni.app.struct;

public class SimpleStack<T> {
    private static class Node<T> {
        T v;
        Node<T> n;
        Node(T v, Node<T> n) {
            this.v = v;
            this.n = n;
        }
    }
    private Node<T> top;
    public void push(T v) {
        top = new Node<>(v, top);
    }
    public T pop() {
        if (top == null) return null;
        T v = top.v;
        top = top.n;
        return v;
    }
    public boolean isEmpty() {
        return top == null;
    }
}
