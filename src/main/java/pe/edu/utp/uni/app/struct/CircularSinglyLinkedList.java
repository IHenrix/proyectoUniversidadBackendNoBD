package pe.edu.utp.uni.app.struct;

public class CircularSinglyLinkedList<T> {
    public static class Node<T> { public T value; public Node<T> next; public Node(T v){ this.value=v; } }
    private Node<T> tail;
    private int size=0;

    public Node<T> addLast(T v){
        Node<T> n=new Node<>(v);
        if(tail==null){ tail=n; tail.next=tail; }
        else { n.next=tail.next; tail.next=n; tail=n; }
        size++; return n;
    }

    public Node<T> head(){ return tail==null? null : tail.next; }
    public int size(){ return size; }
}
