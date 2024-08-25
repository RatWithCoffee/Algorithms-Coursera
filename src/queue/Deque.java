package queue;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node head;
    private Node tail;
    private int size;

    // construct an empty deque
    public Deque() {
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Adding null to the deque is impossible");
        }

        Node newNode = new Node();
        newNode.item = item;
        if (!isEmpty()) {
            head.next = newNode;
            newNode.prev = head;
        } else {
            tail = newNode;
        }
        head = newNode;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Adding null to the deque is impossible");
        }

        Node newNode = new Node();
        newNode.item = item;
        if (!isEmpty()) {
            tail.prev = newNode;
            newNode.next = tail;
        } else {
            head = newNode;
        }
        tail = newNode;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }

        Item retItem = head.item;
        head = head.prev;
        size--;
        return retItem;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }

        Item retItem = tail.item;
        tail = tail.next;
        size--;
        return retItem;
    }


    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node first;
        DequeIterator() {
            first = head;
        }

        @Override
        public boolean hasNext() {
            return first.prev != null;
        }

        @Override
        public Item next() {
            Item retItem = first.item;
            first = first.prev;
            return retItem;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    public static void main(String[] args) {
        Deque<Character> deque = new Deque<>();
        deque.addFirst('a');
        deque.addFirst('b');
        deque.addFirst('c');
        deque.addFirst('d');
        for (Character ch : deque) {
            System.out.print(ch);
        }

    }

    // unit testing (required)
//    public static void main(String[] args) {
//        Deque<String> deque = new Deque<>();
//        System.out.println(deque.isEmpty());
//        deque.addLast("a");
//        deque.addFirst("b");
//        deque.addFirst("c");
//        System.out.println(deque.size());
//
//        Iterator<String> iter = deque.iterator();
//        System.out.println(iter.hasNext());
//        System.out.println(iter.next());
//
//        for (String d : deque) {
//            System.out.print(d + " ");
//        }
//        System.out.println();
//
//        System.out.println(deque.removeFirst());
//        System.out.println(deque.removeLast());
//
//    }

}