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
    public void remove(K key) { root = deleteNode(root, key); }
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

    private Node minValueNode(Node n) {
        Node current = n;
        while (current.left != null) current = current.left;
        return current;
    }

    private Node deleteNode(Node root, K key) {
        if (root == null) return root;
        int cmp = key.compareTo(root.key);
        if (cmp < 0) root.left = deleteNode(root.left, key);
        else if (cmp > 0) root.right = deleteNode(root.right, key);
        else {
            if ((root.left == null) || (root.right == null)) {
                Node temp = root.left != null ? root.left : root.right;
                if (temp == null) {
                    root = null;
                } else {
                    root = temp;
                }
            } else {
                Node temp = minValueNode(root.right);
                root.key = temp.key;
                root.value = temp.value;
                root.right = deleteNode(root.right, temp.key);
            }
        }
        if (root == null) return root;
        root.height = 1 + Math.max(height(root.left), height(root.right));
        int bf = balance(root);
        if (bf > 1 && balance(root.left) >= 0) return rightRotate(root);
        if (bf > 1 && balance(root.left) < 0) { root.left = leftRotate(root.left); return rightRotate(root); }
        if (bf < -1 && balance(root.right) <= 0) return leftRotate(root);
        if (bf < -1 && balance(root.right) > 0) { root.right = rightRotate(root.right); return leftRotate(root); }
        return root;
    }
}
