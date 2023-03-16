import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class fpgrowth {
    private HashMap<String, Integer> supportTable;
    private List<List<String>> transactions;
    private Node tree;
    private HashMap<String, Node> headerTable;
    private Integer support;
    private static Set<List<String>> set = new HashSet<List<String>>(); // Keeps track of what itemsets have been
                                                                        // visited
    private List<String> patterns;

    public fpgrowth(HashMap<String, Integer> supportTable, List<List<String>> transactions, Integer support) {
        this.supportTable = supportTable;
        this.transactions = transactions;
        this.tree = new Node(null); // Init root of tree
        this.headerTable = new HashMap<String, Node>();
        this.support = support;
        this.patterns = new ArrayList<String>();
    }

    public Node createFpTree() {
        Node root = this.tree;
        for (List<String> list : transactions) {
            insertToTree(root, list);
        }
        return root;
    }

    public void writeToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("MiningResult.txt"));
            writer.write("|FPs| = " + patterns.size());
            writer.newLine();
            for (String string : this.patterns) {
                System.out.println(string);
                writer.write(string);
                writer.newLine();
            }
            System.out.println("Done writing.");
            writer.close();
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println("Error writing to file!");
        }
    }

    public void createFrequentPatterns() {
        // Sort in descending order
        List<String> orderedHeaderTable = new ArrayList<String>(this.supportTable.keySet());
        Collections.sort(orderedHeaderTable, (a, b) -> this.supportTable.get(b) - this.supportTable.get(a));

        // Start making itemsets for each table item
        for (String key : orderedHeaderTable) {
            List<String> prefix = new ArrayList<String>();
            Node current = this.headerTable.get(key);
            /*
             * (1) Follow prefix path
             * (2) Add current node to path
             * (3) Create the itemsets for prefix path and then visit the next node in path
             */
            while (current != null) {
                if (current.getValue() != null) {
                    prefix.add(current.getValue());
                }
                createItemsets(prefix, current);
                current = current.getParent();
            }
        }
    }

    void createItemsets(List<String> prefix, Node node) {
        List<String> itemset = new ArrayList<String>(prefix);
        itemset.add(node.getValue());

        // Sort the itemset to avoid duplicates with items in varying positions
        itemset.removeAll(Collections.singleton(null));
        Collections.sort(itemset);

        Integer tempSup = getSupport(itemset);

        // Strips duplicate values from itemset
        List<String> itemsetWithoutDuplicates = itemset.stream()
                .distinct()
                .collect(Collectors.toList());

        // Only write values above support that we havent seen yet
        if (tempSup >= this.support && !set.contains(itemsetWithoutDuplicates)) {
            set.add(itemsetWithoutDuplicates);
            this.patterns.add(itemsetWithoutDuplicates.toString() + " : " + tempSup.toString());
        }
        // Create itemsets for all child nodes
        for (Node child : node.getChildren().values()) {
            createItemsets(itemset, child);
        }
    }

    // Used to calculate support of itemsets from the above method
    Integer getSupport(List<String> itemset) {
        Integer i = 0;
        for (List<String> transaction : transactions) {
            if (transaction.containsAll(itemset)) {
                i++;
            }
        }
        return i;
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
            if (this.headerTable.get(head) == null) {
                this.headerTable.put(head, tempNode);
            } else {
                Node next = this.headerTable.get(head);
                while (next.getNexNode() != null) { // Find the last node
                    System.out.println(next.getValue());
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