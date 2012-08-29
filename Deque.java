import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
	private int N; // number of elements on queue
	private Node first; // beginning of queue
	private Node last; // end of queue

	// helper linked list class
	private class Node {
		private Item item;
		private Node prev;
		private Node next;
	}

	public Deque() {
		first = null;
		last = null;
		N = 0;
	}

	public boolean isEmpty() {
		return (first == null && last == null);
	}

	public int size() {
		return N;
	}

	public void addFirst(Item item) {
		if (item == null)
			throw new java.lang.NullPointerException();
		boolean empty = isEmpty();
		Node oldFirst = first;
		first = new Node();
		first.item = item;
		first.next = oldFirst;
		first.prev = null;
		if (empty) {
			last = first;
		} else {
			oldFirst.prev = first;
		}
		++N;
	}

	public void addLast(Item item) {
		if (item == null)
			throw new java.lang.NullPointerException();
		boolean empty = isEmpty();
		Node oldLast = last;
		last = new Node();
		last.item = item;
		last.prev = oldLast;
		if (!empty) {
			oldLast.next = last;
		} else {
			first = last;
		}
		++N;
	}

	public Item removeFirst() {
		if (isEmpty())
			throw new java.util.NoSuchElementException();
		Node next = first.next;
		Item result = first.item;
		if (next != null) {
			next.prev = null;
		}
		if (first == last) {
			last = next;
		}
		first = next;
		--N;
		return result;
	}

	public Item removeLast() {
		if (isEmpty())
			throw new java.util.NoSuchElementException();
		Node prev = last.prev;
		Item result = last.item;
		if (prev != null)
			prev.next = null;
		if (first == last) {
			first = null;
		}
		last = prev;
		--N;
		return result;
	}

	public Iterator<Item> iterator() {
		return new ListIterator();
	}

	// an iterator, doesn't implement remove() since it's optional
	private class ListIterator implements Iterator<Item> {
		private Node current = first;

		public boolean hasNext() {
			return current != null;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext())
				throw new NoSuchElementException();
			Item item = current.item;
			current = current.next;
			return item;
		}
	}

	public static void main(String[] args) {
		Deque<String> q = new Deque<String>();
		for (int i = 0; i < 10; ++i) {
			if (i % 2 == 0) {
				q.addLast(String.valueOf(i));
			} else if (i % 3 == 0) {
				q.removeLast();
			}
		}
		Iterator<String> iter = q.iterator();
		while (iter.hasNext()) {
			StdOut.println(iter.next());
		}
	}
}
