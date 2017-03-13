import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by zak on 1/3/17.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int head = -1;
    private int tail = -1;

    public RandomizedQueue() {

        items = (Item[]) new Object[2];
    }             // construct an empty randomized queue

    public boolean isEmpty() {
        return size() == 0;
    }            // is the queue empty?

    public int size() {
        return tail - head;
    }                    // return the number of items on the queue

    public void enqueue(Item item) {
        itemNullCheck(item);

        if (tail == items.length - 1) {
            resize(2 * items.length);
        }

        tail++;
        items[tail] = item;

    }       // add the item

    public Item dequeue() {
        emptyCheck();

        int random = StdRandom.uniform(head + 1, tail + 1);
        Item temp = items[random];
        head++;
        items[random] = items[head];
        items[head] = null;

        if (size() > 0 && size() == items.length / 4) {
            resize(items.length / 2);
        }

        return temp;
    }              // remove and return a random item

    public Item sample() {
        emptyCheck();
        int random = StdRandom.uniform(head + 1, tail + 1);
        Item temp = items[random];
        return temp;
    }             // return (but do not remove) a random item

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }      // return an independent iterator over items in random order

    public static void main(String[] args) {

        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<Integer>();
        for (int i = 1; i <= 100; i++) {
            randomizedQueue.enqueue(i);
        }

        for (int i = 1; i <= 99; i++) {
            randomizedQueue.dequeue();
        }

        System.out.println(randomizedQueue.size());
    }  // unit testing

    private void itemNullCheck(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
    }

    private void emptyCheck() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
    }


    private void resize(int capacity) {
        int size = size();
        assert capacity >= size;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0, j = head; i < size; i++) {
            j++;
            temp[i] = items[j];
        }
        items = temp;
        head = -1;
        tail = size - 1;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private int iterHead = -1;
        private int iterTail = -1;
        private Item[] iterItems;

        public RandomizedQueueIterator() {
            int size = size();
            iterItems = (Item[]) new Object[size];
            for (int i = 0, j = head; i < size(); i++) {
                j++;
                iterItems[i] = items[j];

            }
            iterHead = -1;
            iterTail = size - 1;
        }

        @Override
        public boolean hasNext() {
            return iterHead < iterTail;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int random = StdRandom.uniform(iterHead + 1, iterTail + 1);

            Item temp = iterItems[random];
            iterHead++;
            iterItems[random] = iterItems[iterHead];
            iterItems[iterHead] = temp;
            return temp;

        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
