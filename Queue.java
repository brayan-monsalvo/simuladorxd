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
        /*Retorna la cabeza de la cola sin retirarla*/
        return head.getElement();
    }

    public E getElement(int index){
        /*Regresa el elemento no. index de la cola*/

        /*si el indice es mayor que el numero de elementos o
        el indice es menor que 0
        */
        if (index > getNumberElements() || index < 0){
            return null;
        }

        //si el indice es 1, se retira la cabeza de la cola 
        if(index == 1){
            return poll();
        }

        Node <E> element = head;
        Node <E> prevNode;
        Node <E> nextNode;
        E temp;

        //este ciclo for ubica a element justo en donde se encuentra el nodo a retirar
        for(int i = 0; i < index-1; i++){
            element = element.getNextNode();
        }

        temp = element.getElement();

        //se guarda el nodo siguiente y previo del elemento a retirar
        prevNode = element.getPrevNode();
        nextNode = element.getNextNode();

        //se establece que el nodo previo tiene como nodo siguiente nextNode
        //y que el nodo siguiente tiene como nodo previo a prevNode
        //"omitiendo" al nodo que se quiere retirar, eliminando asi de la cola
        nextNode.setPrevNode( prevNode );
        prevNode.setNextNode(nextNode);

        numElements--;

        return temp;
    }

    public E poll(){
        Node <E> h = head;

        /* 
         * Si solamente hay un elemento en la cola, se retornara ese elemento, y la cabeza apuntara a nulo
         * porque no hay ningun nodo predecesor o sucesor
        */
        if(getNumberElements() == 1){
            head = null;
            numElements--;
            return h.getElement();
        }

        /*
         * si hay mas de 1 elemento, la cola ahora pasa a ser su sucesor, y se establece que la nueva 
         * cabeza no tiene nodo predecesor
        */
        
        head = head.getNextNode();
        head.setPrevNode(null);
        numElements--;
        return h.getElement();
    }

    public boolean isEmpty(){
        return (numElements == 0);
    }

    public int getNumberElements(){
        return numElements;
    }
    
}