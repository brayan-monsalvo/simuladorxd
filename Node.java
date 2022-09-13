public class Node <E> {
    private E element;
    private Node nextNode;

    public Node (E element, Node nextNode){
        this.element = element;
        this.nextNode = nextNode;
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

    public void setNextNode(Node n){
        this.nextNode = n;
    }

    public boolean hasNextNode(){
        return (nextNode != null);
    }
}
