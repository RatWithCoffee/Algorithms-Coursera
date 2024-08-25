package queue;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int head = -1;

    private Item[] queue;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) (new Object[1]);
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return head == -1;
    }

    // return the number of items on the randomized queue
    public int size() {
        return head + 1;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Adding null to the deque is impossible");
        }

        if (head + 2 > queue.length) {
            queue = resize(queue.length * 2);
        }
        queue[++head] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (head == -1) {
            throw new NoSuchElementException();
        }
        if (head + 1 <= queue.length / 4) {
            queue = resize(queue.length / 2);
        }

        int randIndex = StdRandom.uniformInt(head + 1);
        Item retItem = queue[randIndex];
        queue[randIndex] = queue[head];
        queue[head] = null;
        head--;
        return retItem;
    }

    private Item[] resize(int size) {
        Item[] newQueue = (Item[]) new Object[size];
        for (int i = 0; i < Math.min(queue.length, newQueue.length); i++) {
            newQueue[i] = queue[i];
        }
        return newQueue;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (head == -1) {
            throw new NoSuchElementException();
        }
        return queue[StdRandom.uniformInt(head + 1)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandQueueIterator();
    }

    private class RandQueueIterator implements Iterator<Item> {
        private int current;

        private Item[] queueCopy;

        RandQueueIterator() {
            current = head;
            queueCopy = queue.clone();
        }

        @Override
        public boolean hasNext() {
            return current >= 0;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("");
            }
            int itemIndex = StdRandom.uniformInt(current + 1); // change the last item with random item
            Item temp = queueCopy[current];
            queueCopy[current] = queueCopy[itemIndex];
            queueCopy[itemIndex] = temp;
            return queueCopy[current--];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        System.out.println(queue.isEmpty());
        queue.enqueue("a");
        queue.enqueue("b");
        queue.enqueue("c");
        System.out.println(queue.size());

        Iterator<String> iter = queue.iterator();
        System.out.println(iter.hasNext());
        System.out.println(iter.next());

        for (String d : queue) {
            System.out.print(d + " ");
        }
        System.out.println();

        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
        System.out.println(queue.isEmpty());
    }

}