public class Node <E> {
    private E element;
    private Node nextNode;
    private Node previousNode;

    public Node (E element, Node nextNode, Node prevNode){
        this.element = element;
        this.nextNode = nextNode;
        this.previousNode = prevNode;
    }

    public Node (E element){
        this.element = element;
        nextNode = null;
    }

    public E getElement(){
        return element;
    }

    public void setElement (E t){
        this.element = t;
    }

    public Node getNextNode(){
        return nextNode;
    }

    public Node getPrevNode(){
        return previousNode;
    }

    public void setNextNode(Node n){
        this.nextNode = n;
    }

    public void setPrevNode(Node n){
        this.previousNode = n;
    }

    public boolean hasNextNode(){
        return (nextNode != null);
    }

    public boolean hasPrevNode(){
        return (previousNode != null);
    }
}
