package pe.edu.utp.uni.app.struct;

public class DoublyLinkedList<T> {
    public static class Node<T> {
        public T value; public Node<T> prev, next;
        public Node(T v){ this.value=v; }
    }
    private Node<T> head, tail; private int size=0;
    public Node<T> addLast(T v){ Node<T> n=new Node<>(v); if(tail==null){head=tail=n;} else {tail.next=n; n.prev=tail; tail=n;} size++; return n; }
    public Node<T> head(){ return head; }
    public int size(){ return size; }
}
