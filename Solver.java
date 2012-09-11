import java.util.Comparator;
import java.util.Iterator;

public class Solver {

	private Board initialBoard;
	private int answerMoves;
	private SearchNode answerLastNode;

	private class SearchNode {
		public Board board;
		public int moves;
		public SearchNode prev;
	}

	private class SearchNodeComp implements Comparator<SearchNode> {
		public int compare(SearchNode sn1, SearchNode sn2) {
			int priority1 = sn1.board.manhattan() + sn1.moves;
			int priority2 = sn2.board.manhattan() + sn2.moves;
			if (priority1 < priority2)
				return -1;
			else if (priority1 == priority2)
				return 0;
			else
				return 1;
		}
	}

	private MinPQ<SearchNode> InitSearchNode(Board init) {
		SearchNodeComp snComp = new SearchNodeComp();
		MinPQ<SearchNode> searchNodes = new MinPQ<SearchNode>(1, snComp);
		SearchNode initNode = new SearchNode();
		initNode.board = init;
		initNode.moves = 0;
		initNode.prev = null;
		searchNodes.insert(initNode);
		return searchNodes;
	}

	private void ExtendSearchNode(MinPQ<SearchNode> searchNodes,
			SearchNode curSearchNode) {
		Iterable<Board> iterable = curSearchNode.board.neighbors();
		Iterator<Board> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			SearchNode newSearchNode = new SearchNode();
			newSearchNode.board = iterator.next();
			newSearchNode.moves = curSearchNode.moves + 1;
			newSearchNode.prev = curSearchNode;
			if (curSearchNode.prev == null
					|| !newSearchNode.board.equals(curSearchNode.prev.board))
				searchNodes.insert(newSearchNode);
		}
	}

	public Solver(Board initial) {
		this.answerMoves = -1;
		this.answerLastNode = null;

		MinPQ<SearchNode> searchNodes = InitSearchNode(initial);
		Board twin = initial.twin();
		MinPQ<SearchNode> twinSearch = InitSearchNode(twin);

		while (!searchNodes.isEmpty() && !twinSearch.isEmpty()) {
			SearchNode minSearchNode = searchNodes.delMin();
			SearchNode twinMin = twinSearch.delMin();
			if (minSearchNode.board.isGoal()) {
				this.answerMoves = minSearchNode.moves;
				this.answerLastNode = minSearchNode;
				return;
			} else if (twinMin.board.isGoal()) {
				this.answerMoves = -1;
				this.answerLastNode = null;
				return;
			} else {
				ExtendSearchNode(searchNodes, minSearchNode);
				ExtendSearchNode(twinSearch, twinMin);
			}
		}
	}

	public boolean isSolvable() {
		return (answerMoves != -1);
	}

	public int moves() {
		return answerMoves;
	}

	private class SolutionIterator implements Iterator<Board> {

		private Stack<Board> solutionSequence;

		private SolutionIterator() {
			solutionSequence = new Stack<Board>();
			SearchNode curSN = answerLastNode;
			while (curSN != null) {
				solutionSequence.push(curSN.board);
				curSN = curSN.prev;
			}
		}

		public boolean hasNext() {
			return (!solutionSequence.isEmpty());
		}

		public Board next() {
			return solutionSequence.pop();
		}

		public void remove() {
		}
	}

	private class SolutionIterable implements Iterable<Board> {
		public Iterator<Board> iterator() {
			return new SolutionIterator();
		}
	}

	public Iterable<Board> solution() {
		if (!isSolvable())
			return null;
		return new SolutionIterable();
	}

	public static void main(String[] args) {
		// create initial board from file
		In in = new In(args[0]);
		int N = in.readInt();
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}
}
