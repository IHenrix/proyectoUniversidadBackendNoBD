package pe.edu.utp.uni.app.struct;

public class AVLTree<K extends Comparable<K>, V> {
    private class Node {
        K key;
        V value;
        Node left, right;
        int height;
        Node(K k, V v) { key = k; value = v; height = 1; }
    }
    private Node root;
    public void put(K key, V value) { root = insert(root, key, value); }
    public V get(K key) {
        Node n = search(root, key);
        return n == null ? null : n.value;
    }
    private Node search(Node node, K key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp == 0) return node;
        if (cmp < 0) return search(node.left, key);
        return search(node.right, key);
    }
    private int height(Node n) { return n == null ? 0 : n.height; }
    private int balance(Node n) { return n == null ? 0 : height(n.left) - height(n.right); }
    private Node rightRotate(Node y) {
        Node x = y.left; Node T2 = x.right;
        x.right = y; y.left = T2;
        y.height = 1 + Math.max(height(y.left), height(y.right));
        x.height = 1 + Math.max(height(x.left), height(x.right));
        return x;
    }
    private Node leftRotate(Node x) {
        Node y = x.right; Node T2 = y.left;
        y.left = x; x.right = T2;
        x.height = 1 + Math.max(height(x.left), height(x.right));
        y.height = 1 + Math.max(height(y.left), height(y.right));
        return y;
    }
    private Node insert(Node node, K key, V value) {
        if (node == null) return new Node(key, value);
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = insert(node.left, key, value);
        else if (cmp > 0) node.right = insert(node.right, key, value);
        else { node.value = value; return node; }
        node.height = 1 + Math.max(height(node.left), height(node.right));
        int bf = balance(node);
        if (bf > 1 && key.compareTo(node.left.key) < 0) return rightRotate(node);
        if (bf < -1 && key.compareTo(node.right.key) > 0) return leftRotate(node);
        if (bf > 1 && key.compareTo(node.left.key) > 0) { node.left = leftRotate(node.left); return rightRotate(node); }
        if (bf < -1 && key.compareTo(node.right.key) < 0) { node.right = rightRotate(node.right); return leftRotate(node); }
        return node;
    }
}
