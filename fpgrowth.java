public class fpgrowth {
    class Node {
        private String value;
        private Node nextNode;
        private Node prevNode;
        public Boolean hasNext;
        public Boolean hasPrev;


        public Node(){
            this.hasNext = false;
            this.hasPrev = false;
            this.value   = "";
        }

        public String getValue(){
            return this.value;
        }

        public Node getNextNode() {
            return this.nextNode;
        }

        public Node getPrevNode() {
            return this.prevNode;
        }

        public void setValue(String val){
            this.value = val;
        }

        public void setNextNode(Node n){
            this.nextNode = n;
        }

        public void setPrevNode(Node n){
            this.prevNode = n;
        }
    }


}