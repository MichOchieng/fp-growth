import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class fpgrowth {
    private HashMap<String, Integer> supportTable;
    private List<List<String>> transactions;
    private Node tree;
    private HashMap<String, Node> headerTable;
    private Integer support;

    public fpgrowth(HashMap<String, Integer> supportTable, List<List<String>> transactions, Integer support) {
        this.supportTable = supportTable;
        this.transactions = transactions;
        this.tree = new Node(null); // Init root of tree
        this.headerTable = new HashMap<String, Node>();
        this.support = support;
    }

    public Node createFpTree() {
        Node root = this.tree;
        // Loop over transactions elements
        for (List<String> list : transactions) {
            insertToTree(root, list);
        }
        System.out.println("head t");
        System.out.println(headerTable);
        return root;
    }

    public void viewFrequenPatterns() {
        for (String key : this.headerTable.keySet()) {
            Integer tempSupport = this.supportTable.get(key);

            if (tempSupport >= this.support) {
                List<String> itemset = new ArrayList<String>();
                itemset.add(key);

                Node conditional = this.headerTable.get(key).getNexNode();
                while (conditional != null) {
                    List<String> conditionalItems = new ArrayList<String>();
                    Node current = conditional;
                    Integer conditionalSupport = current.getSupport();
                    Set<Node> visited = new HashSet<Node>();
                    while (current.getParent().getValue() != null) {
                        if (!visited.contains(current)) {
                            visited.add(current);
                            conditionalItems.add(current.getValue());
                        }
                        current = current.getParent();
                    }
                    if (!conditionalItems.isEmpty()) {
                        Collections.reverse(conditionalItems);
                        conditionalItems.addAll(itemset);
                        System.out.println(conditionalItems.toString() + " : " + conditionalSupport);
                    }
                    conditional = conditional.getNexNode();
                }
                System.out.println(itemset.toString() + " : " + tempSupport);
            }
        }
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