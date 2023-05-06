public class BinarySearchTree {
    private Node root;

    private class Node {
        DwellingInfo data;
        Node left;
        Node right;

        public Node(DwellingInfo data) {
            this.data = data;
            left = null;
            right = null;
        }
    }

    public void insert(DwellingInfo data) {
        root = insert(root, data);
    }

    private Node insert(Node node, DwellingInfo data) {
        if (node == null) {
            return new Node(data);
        }

        if (data.getPriceOrRent() < node.data.getPriceOrRent()) {
            node.left = insert(node.left, data);
        } else {
            node.right = insert(node.right, data);
        }

        return node;
    }

    public DwellingInfo search(double value) {
        Node node = search(root, value);
        return node == null ? null : node.data;
    }

    private Node search(Node node, double value) {
        if (node == null || node.data.getPriceOrRent() == value) {
            return node;
        }

        if (value < node.data.getPriceOrRent()) {
            return search(node.left, value);
        } else {
            return search(node.right, value);
        }
    }
}