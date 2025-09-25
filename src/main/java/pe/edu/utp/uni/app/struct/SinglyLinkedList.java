package pe.edu.utp.uni.app.struct;

public class SinglyLinkedList<T> {
    public static class Node<T> {
        public T value; public Node<T> next;
        public Node(T v){ this.value=v; }
    }
    private Node<T> head; private int size=0;
    public Node<T> addLast(T v){ Node<T> n=new Node<>(v); if(head==null){head=n;} else {Node<T> c=head; while(c.next!=null) c=c.next; c.next=n;} size++; return n; }
    public Node<T> head(){ return head; }
    public int size(){ return size; }
}
