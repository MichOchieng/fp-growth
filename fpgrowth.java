import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class fpgrowth {
    private static HashMap<String, Integer> supportTable;
    private static List<List<String>> transactions;
    private static Node tree;
    private static HashMap<String, Node> headerTable;

    public fpgrowth(HashMap<String, Integer> supportTable, List<List<String>> transactions) {
        this.supportTable = supportTable;
        this.transactions = transactions;
        this.tree = new Node(null); // Init root of tree
        this.headerTable = new HashMap<String, Node>();
    }

    public Node createFpTree() {
        Node root = fpgrowth.tree;
        // Loop over transactions elements
        for (List<String> list : transactions) {
            insertToTree(root, list);
        }
        return root;
    }

    void insertToTree(Node node, List<String> transaction) {
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
            // Add to header table
            if (headerTable.get(head) == null) {
                headerTable.put(head, tempNode);
            } else {
                System.out.println("Adding node " + tempNode.getValue() + " to end of list");
                Node next = headerTable.get(head);
                while (next.getNexNode() != null) { // Find the last node
                    System.out.print(next.getValue());
                    next = next.getNexNode();
                }
                next.setNextNode(tempNode);
            }
        } else {
            // Child already exists, increment the support value
            tempNode.incrementSupport();
        }
        // Run again recursively on the remaining items in transaction
        if (transaction.size() > 1) {
            insertToTree(tempNode, transaction.subList(1, transaction.size()));
        }
    }

    public class Node {
        private Node parent;
        private HashMap<String, Node> children;
        private String value;
        private Integer support;
        private Node next;

        public Node(String value) {
            this.parent = null;
            this.value = value;
            this.support = 1;
            this.children = new HashMap<String, Node>();
            this.next = null;
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

        public Node getNexNode() {
            return this.next;
        }

        public void setNextNode(Node node) {
            this.next = node;
        }

    }

}