public class Queue <E> {
    private Node<E> head;
    private int numElements = 0;

    public Queue(){

    }

    public Queue (E head){
        this.head = new Node <E>(head);
        numElements = (head == null) ? 0 : 1;
    }

    public void add(E element){
        //si la cola esta vacia, entonces el elemento que se quiere ingresar sera la nueva
        //cabeza de la cola

        if(element == null){
            return;
        }

        if(isEmpty()){
            head = new Node <E> (element);
            numElements++;

            return;
        }

        //se recorrera toda la cola hasta llegar al ultimo nodo, para ello se crea
        //un nodo temporal que apuntara inicialmente a la cabeza
        Node <E> temp = head;

        do{
            temp = temp.getNextNode();
        }while(temp.hasNextNode());

        temp.setNextNode(new Node<E>(element));
        numElements++;
    }

    public E element(){
        return head.getElement();
    }

    public E poll(){
        Node <E> h = head;

        head.setNextNode( head.getNextNode() );

        return h.getElement();
    }

    public boolean isEmpty(){
        return (numElements == 0);
    }

    public int getNumberElements(){
        return numElements;
    }
}
