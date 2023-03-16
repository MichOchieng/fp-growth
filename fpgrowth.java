import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class fpgrowth {
    private static HashMap<String, Integer> supportTable;
    private static List<List<String>> transactions;
    private static Node tree;

    public fpgrowth(HashMap<String, Integer> supportTable, List<List<String>> transactions) {
        this.supportTable = supportTable;
        this.transactions = transactions;
        this.tree = new Node(null); // Init root of tree
    }

    public void createFpTree() {
        Node root = fpgrowth.tree;
        // Loop over transactions elements
        for (List<String> list : transactions) {
            insertToTree(root, list);
        }
    }

    public void getFrequentPatterns(Node node, HashMap<String, Integer> suffixPath, Integer minSup) {
        // Find bottom
        if (node.getChildren().isEmpty()) {

        }
    }

    void insertToTree(Node node, List<String> transaction) {
        System.out.println("Current Node value " + node.getValue() + " with support " + node.getSupport());
        // Skip empty transactions
        if (transaction.size() < 1) {
            return;
        }
        String head = transaction.get(0);
        Node tempNode = node.getChildren().get(head);

        if (tempNode == null) {
            // Add new child to parent
            tempNode = new Node(head);
            tempNode.setParent(node);
            node.getChildren().put(head, tempNode);
            System.out.println("Child Node value " + tempNode.getValue() + " with support " + tempNode.getSupport());
        } else {
            // Child already exists, increment the support value
            tempNode.incrementSupport();
            System.out.println("Child Node value " + tempNode.getValue() + " with support " + tempNode.getSupport());
            System.out.println("incremented Support to " + tempNode.getSupport());

        }
        // Run again recursively on the remaining items in transaction
        if (transaction.size() > 1) {
            insertToTree(tempNode, transaction.subList(1, transaction.size()));
        }
    }

    // public void viewTree() {
    // Node root = fpgrowth.tree;

    // // No more children
    // if (root.children.keySet().size() < 1) {
    // System.out.println("Only root");
    // System.out.println(root.getValue());
    // } else {
    // for (String key : root.children.keySet()) {
    // System.out.println("Viewing children of node " +
    // root.children.get(key).getValue());
    // viewTree(root.children.get(key));
    // }
    // }
    // }

    // public void viewTree(Node n) {
    // if (n.children.keySet().size() < 1) {
    // System.out.print("Node value ");
    // System.out.println(n.getValue());
    // } else {
    // for (String key : n.children.keySet()) {
    // System.out.println("Viewing children of node " +
    // n.children.get(key).getValue());
    // viewTree(n.children.get(key));
    // }
    // }
    // }

    class Node {
        private Node parent;
        private HashMap<String, Node> children;
        private String value;
        private Integer support;

        public Node(String value) {
            this.parent = null;
            this.value = value;
            this.support = 1;
            this.children = new HashMap<String, Node>();
        }

        public void incrementSupport() {
            this.support += 1;
        }

        // Getters + setters
        public String getValue() {
            return this.value;
        }

        public Integer getSupport() {
            return this.support;
        }

        public HashMap<String, Node> getChildren() {
            return this.children;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public Node getParent() {
            return this.parent;
        }

    }

}