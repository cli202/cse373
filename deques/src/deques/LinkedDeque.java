package deques;

/**
 * @see Deque
 */
public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    Node<T> front;
    Node<T> back;
    // Feel free to add any additional fields you may need, though.

    public LinkedDeque() {
        size = 0;
        // changed Leon's implementation because we don't know what type of dequeue
        // we are going to implement, so I'll just use generic <T> for now
        front = new Node<T>(null);
        back = new Node<T>(null);
        front.next = back;
        back.prev = front;
    }

    public void addFirst(T item) {
        size += 1;
        Node<T> newNode = new Node<T>(item);
        // front -> front sentinel node <-> ... <-> back sentinel node <- back
        Node<T> everythingElse = front.next;
        // front -> front sentinel node <-> [ ... <-> back sentinel node <- back ]
        // front -> front sentinel node <-> [ everythingElse ]
        front.next = newNode;
        // front -> front sentinel node -> newNode
        newNode.prev = front;
        // front -> front sentinel node <-> newNode
        newNode.next = everythingElse;
        // front -> front sentinel node <-> newNode -> everythingElse
        everythingElse.prev = newNode;
        // front -> front sentinel node <-> newNode <-> everythingElse
        // front -> front sentinel node <-> ... <-> back sentinel node <- back
    }

    public void addLast(T item) {
        size += 1;
        Node<T> newNode = new Node<T>(item);
        // front -> front sentinel node <-> ... <-> back sentinel node <- back
        Node<T> everythingElse = back.prev;
        // front -> front sentinel node <-> [ ... <-> back sentinel node <- back ]
        // front -> front sentinel node <-> [ everythingElse ]
        back.prev = newNode;
        // front -> front sentinel node -> newNode
        newNode.next = back;
        // front -> front sentinel node <-> newNode
        newNode.prev = everythingElse;
        // front -> front sentinel node <-> newNode -> everythingElse
        everythingElse.next = newNode;
        // front -> front sentinel node <-> newNode <-> everythingElse
        // front -> front sentinel node <-> ... <-> back sentinel node <- back
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        Node<T> temp = front.next;
        front.next = front.next.next;
        front.next.prev = front;
        return temp.value;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        Node<T> temp = back.prev;
        back.prev = back.prev.prev;
        back.prev.next = back;
        return temp.value;
    }

    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        if (index <= (size/2)) {
            Node<T> temp = front.next;
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
            return temp.value;
        } else {
            Node<T> temp = back.prev;
            for (int i = 0; i < size - index - 1; i++) {
                temp = temp.prev;
            }
            return temp.value;
        }

    }

    public int size() {
        return size;
    }
}
