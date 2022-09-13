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

        while(temp.hasNextNode()){
            temp = temp.getNextNode();
        }

        /*El nodo temporal es el ultimo de la cola, a ese nodo se le agrega un nuevo nodo que es 
         * el elemento que se quiere agregar. Despues, a aquel ultimo elemento se le indica que su nodo antecesor
         * es el nodo temporal
        */
        temp.setNextNode(new Node<E>(element));
        temp.getNextNode().setPrevNode(temp);
        numElements++;
    }

    public E element(){
        return head.getElement();
    }

    public E getElement(int index){
        if (index > getNumberElements()){
            return null;
        }

        
        
    }

    public E poll(){
        Node <E> h = head;

        //head.setNextNode( head.getNextNode() );
        head = head.getNextNode();
        head.setPrevNode(null);
        
        return h.getElement();
    }

    public boolean isEmpty(){
        return (numElements == 0);
    }

    public int getNumberElements(){
        return numElements;
    }
}
