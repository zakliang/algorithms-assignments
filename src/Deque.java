import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by zak on 1/3/17.
 */


public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first;
    private Node<Item> last;
    private int size;

    public Deque() {
        size = 0;
    }                       // construct an empty deque

    public boolean isEmpty() {
        return first == null;
    }         // is the deque empty?

    public int size() {
        return size;
    }                 // return the number of items on the deque

    public void addFirst(Item item) {
        itemNullCheck(item);
        Node<Item> oldFirst = first;
        first = new Node<Item>();
        first.item = item;
        if (oldFirst == null) {
            last = first;
        } else {
            first.next = oldFirst;
            oldFirst.previous = first;
        }
        size++;

    }   // add the item to the front

    public void addLast(Item item) {
        itemNullCheck(item);
        Node<Item> oldLast = last;
        last = new Node<Item>();
        last.item = item;
        if (oldLast == null) {
            first = last;
        } else {
            oldLast.next = last;
            last.previous = oldLast;
        }
        size++;


    }    // add the item to the end

    public Item removeFirst() {
        if (size <= 0) {
            throw new NoSuchElementException();
        }
        Node<Item> oldFirst = first;
        Item item = oldFirst.item;
        first = oldFirst.next;
        if (first != null) {
            first.previous = null;
        } else {
            last = null;
        }
        cleanup(oldFirst);
        size--;
        return item;
    }         // remove and return the item from the front

    public Item removeLast() {
        if (size <= 0) {
            throw new NoSuchElementException();
        }
        Node<Item> oldLast = last;
        Item item = oldLast.item;

        last = oldLast.previous;
        if (last != null) {
            last.next = null;
        } else {
            first = null;
        }
        cleanup(oldLast);
        size--;
        return item;

    }          // remove and return the item from the end

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }     // return an iterator over items in order from front to end

    public static void main(String[] args) {

    }   // unit testing


    private class Node<Item> {
        private Item item;
        private Node<Item> previous;
        private Node<Item> next;

    }

    private void itemNullCheck(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
    }

    private class DequeIterator implements Iterator<Item> {

        private Node<Item> current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private void cleanup(Node<Item> node) {
        node.next = null;
        node.previous = null;
    }
}

