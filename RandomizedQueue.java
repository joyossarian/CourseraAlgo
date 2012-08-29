import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] q; // queue elements
	private int N = 0; // number of elements on queue
	private int first = 0; // index of first element of queue
	private int last = 0; // index of next available slot

	// cast needed since no generic array creation in Java
	@SuppressWarnings("unchecked")
	public RandomizedQueue() {
		q = (Item[]) new Object[2];
	}

	public boolean isEmpty() {
		return N == 0;
	}

	public int size() {
		return N;
	}

	// resize the underlying array
	@SuppressWarnings("unchecked")
	private void resize(int max) {
		assert max >= N;
		Item[] temp = (Item[]) new Object[max];
		for (int i = 0; i < N; i++) {
			temp[i] = q[(first + i) % q.length];
		}
		q = temp;
		first = 0;
		last = N;
	}

	public void enqueue(Item item) {
		if (item == null)
			throw new java.lang.NullPointerException();
		// double size of array if necessary and recopy to front of array
		if (N == q.length)
			resize(2 * q.length); // double size of array if necessary
		q[last++] = item; // add item
		if (last == q.length)
			last = 0; // wrap-around
		N++;
	}

	// remove the least recently added item
	public Item dequeue() {
		if (isEmpty())
			throw new java.lang.UnsupportedOperationException();
		int index = (first + StdRandom.uniform(N)) % q.length;
		Item result = q[index];
		q[index] = q[first];
		q[first] = null; // to avoid loitering
		N--;
		first++;
		if (first == q.length)
			first = 0; // wrap-around
		// shrink size of array if necessary
		if (N > 0 && N == q.length / 4)
			resize(q.length / 2);
		return result;
	}

	public Item sample() {
		if (isEmpty())
			throw new java.lang.UnsupportedOperationException();
		int index = (first + StdRandom.uniform(N)) % q.length;
		Item result = q[index];
		return result;
	}

	public Iterator<Item> iterator() {
		return new RandomIterator();
	}

	// an iterator, doesn't implement remove() since it's optional
	private class RandomIterator implements Iterator<Item> {

		private int[] order;
		private int index;

		public RandomIterator() {
			order = new int[N];
			for (int i = 0; i < N; ++i) {
				order[i] = (first + i) % q.length;
			}
			StdRandom.shuffle(order);
			index = 0;
		}

		public boolean hasNext() {
			return index < order.length;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext())
				throw new NoSuchElementException();
			Item item = q[order[index++]];
			return item;
		}
	}

	/**
	 * A test client.
	 */
	public static void main(String[] args) {
		RandomizedQueue<String> q = new RandomizedQueue<String>();
		for (int i = 0; i < 10; ++i) {
			q.enqueue(String.valueOf(i));
			if (i % 2 == 0)
				q.dequeue();
		}
		Iterator<String> iter = q.iterator();
		while (iter.hasNext()) {
			StdOut.println(iter.next());
		}
	}
}
