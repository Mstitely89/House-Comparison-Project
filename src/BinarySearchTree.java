public class BinarySearchTree<T extends Comparable<T>> {
    private Node<T> root;

    private static class Node<T>   {
        private T data;
        private Node<T> left;
        private Node<T> right;

        public Node(T data){
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    public void insert(T data) {
        root = insert(root, data);
    }

    private Node<T> insert(Node<T> node, T data) {
        if (node == null) {
            return new Node<>(data);
        }

        if (data.compareTo(node.data) < 0) {
            node.left = insert(node.left, data);
        } else {
            node.right = insert(node.right, data);
        }

        return node;
    }

    public T search(T value) {
        Node<T> node = search(root, value);
        return node == null ? null : node.data;
    }

    private Node<T> search(Node<T> node, T value) {
        if (node == null || node.data.equals(value)) {
            return node;
        }

        if (value.compareTo(node.data) < 0) {
            return search(node.left, value);
        } else {
            return search(node.right, value);
        }
    }
}