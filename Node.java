public class Node <E> {
    private E element;
    private Node nextNode;

    public Node (E element, Node nextNode){
        this.element = element;
        this.nextNode = nextNode;
    }

    public E getInstruction(){
        return element;
    }

    public void setInstruction (E t){
        this.element = t;
    }

    public Node getNextNode(){
        return nextNode;
    }

    public boolean hasNextNode(){
        return (nextNode != null);
    }
}
