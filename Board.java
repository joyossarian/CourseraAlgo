import java.util.Iterator;

public class Board {
	private int[][] board;
	private int N;
	private int zeroI;
	private int zeroJ;

	public Board(int[][] blocks) {
		N = blocks.length;
		board = new int[N][N];
		for (int i = 0; i < N; ++i)
			for (int j = 0; j < N; ++j) {
				board[i][j] = blocks[i][j];
				if (board[i][j] == 0) {
					zeroI = i;
					zeroJ = j;
				}
			}
	}

	public int dimension() {
		return N;
	}

	public int hamming() {
		int hamming = 0;
		for (int i = 0; i < N; ++i)
			for (int j = 0; j < N; ++j) {
				if ((i == N - 1) && (j == N - 1)) {
					continue;
				} else if (board[i][j] != N * i + j + 1) {
					++hamming;
				}
			}
		return hamming;
	}

	public int manhattan() {
		int manhattan = 0;
		for (int i = 0; i < N; ++i)
			for (int j = 0; j < N; ++j) {
				if (board[i][j] == 0)
					continue;
				int oj = board[i][j] % N;
				int oi = board[i][j] / N;
				if (oj == 0) {
					oj = N - 1;
					--oi;
				} else {
					--oj;
				}
				manhattan += Math.abs(oi - i) + Math.abs(oj - j);
			}
		return manhattan;
	}

	public boolean isGoal() {
		return (hamming() == 0);
	}

	public Board twin() {
		Board twin = new Board(this.board);
		boolean exchange = false;
		while (!exchange) {
			int i = StdRandom.uniform(N);
			int j = StdRandom.uniform(N);
			int ej = j;
			if (j == N - 1) {
				--ej;
			} else {
				++ej;
			}
			if (board[i][j] != 0 && board[i][ej] != 0) {
				int temp = twin.board[i][j];
				twin.board[i][j] = board[i][ej];
				twin.board[i][ej] = temp;
				exchange = true;
			}
		}
		return twin;
	}

	public boolean equals(Object y) {
		if (y == null)
			return false;
		if (!y.getClass().equals(this.getClass()))
			return false;
		Board another = (Board) y;
		if (another == null)
			return false;
		if (this.N != another.N)
			return false;
		for (int i = 0; i < N; ++i)
			for (int j = 0; j < N; ++j) {
				if (this.board[i][j] != another.board[i][j])
					return false;
			}
		return true;
	}

	private class NeighborIterator implements Iterator<Board> {
		private Queue<Board> neighbors;

		public NeighborIterator() {
			neighbors = new Queue<Board>();
			if (zeroI > 0) {
				Board leftNeighbor = new Board(board);
				leftNeighbor.board[zeroI][zeroJ] = leftNeighbor.board[zeroI - 1][zeroJ];
				leftNeighbor.board[zeroI - 1][zeroJ] = 0;
				--leftNeighbor.zeroI;
				neighbors.enqueue(leftNeighbor);
			}
			if (zeroI < N - 1) {
				Board rightNeighbor = new Board(board);
				rightNeighbor.board[zeroI][zeroJ] = rightNeighbor.board[zeroI + 1][zeroJ];
				rightNeighbor.board[zeroI + 1][zeroJ] = 0;
				++rightNeighbor.zeroI;
				neighbors.enqueue(rightNeighbor);
			}
			if (zeroJ > 0) {
				Board upperNeighbor = new Board(board);
				upperNeighbor.board[zeroI][zeroJ] = upperNeighbor.board[zeroI][zeroJ - 1];
				upperNeighbor.board[zeroI][zeroJ - 1] = 0;
				--upperNeighbor.zeroJ;
				neighbors.enqueue(upperNeighbor);
			}
			if (zeroJ < N - 1) {
				Board downNeighbor = new Board(board);
				downNeighbor.board[zeroI][zeroJ] = downNeighbor.board[zeroI][zeroJ + 1];
				downNeighbor.board[zeroI][zeroJ + 1] = 0;
				++downNeighbor.zeroJ;
				neighbors.enqueue(downNeighbor);
			}
		}

		public boolean hasNext() {
			return (neighbors.isEmpty() == false);
		}

		public Board next() {
			return neighbors.dequeue();
		}

		public void remove() {

		}
	}

	private class NeighborIterable implements Iterable<Board> {
		public Iterator<Board> iterator() {
			NeighborIterator iter = new NeighborIterator();
			return iter;
		}
	}

	public Iterable<Board> neighbors() {
		Iterable<Board> iter = new NeighborIterable();
		return iter;
	}

	public String toString() {
		String result = new String();
		result += N + "\n";
		for (int i = 0; i < N; ++i) {
			for (int j = 0; j < N; ++j) {
				result += board[i][j];
				if (j != N - 1)
					result += " ";
			}
			if (i != N - 1)
				result += "\n";
		}
		return result;
	}

	public static void main(String[] args) {
		StdRandom.setSeed(System.currentTimeMillis());
		int N = 3;
		int[][] blocks = new int[N][N];
		blocks[0][0] = 8;
		blocks[0][1] = 1;
		blocks[0][2] = 3;
		blocks[1][0] = 4;
		blocks[1][1] = 0;
		blocks[1][2] = 2;
		blocks[2][0] = 7;
		blocks[2][1] = 6;
		blocks[2][2] = 5;

		// for (int i = 0; i < blocks.length; ++i) {
		// for (int j = 0; j < blocks[i].length; ++j) {
		// blocks[i][j] = N * i + j;
		// }
		// }
		Board b = new Board(blocks);
		StdOut.println(b + "\nhamming : " + b.hamming() + "\nmanhattan: "
				+ b.manhattan());

		StdOut.println(b.twin());
	}
}
