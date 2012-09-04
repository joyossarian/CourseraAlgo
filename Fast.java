import java.util.Arrays;
import java.util.TreeSet;

public class Fast {

	private static void drawLineSeg(Point[] segPoints) {
		String output = new String();
		int len = segPoints.length;
		for (int i = 0; i < len; ++i) {
			output += segPoints[i].toString();
			if (i != len - 1) {
				output += " -> ";
			}
			segPoints[i].draw();
		}
		StdOut.println(output);
		segPoints[0].drawTo(segPoints[len - 1]);
	}

	private static void outputSeg(Point oP, Point[] sortedPoints,
			int startIndex, int adj) {
		Point[] segPoints = new Point[adj + 1];
		for (int i = 0; i < adj; ++i) {
			segPoints[i] = sortedPoints[startIndex + i];
		}
		segPoints[adj] = oP;
		Arrays.sort(segPoints);
		if (oP.compareTo(segPoints[0]) == 0)
			drawLineSeg(segPoints);
	}

	private static void checkSeg(Point oP, Point[] sortedPoints) {
		double lastSlope = oP.slopeTo(sortedPoints[1]);
		int adj = 1;
		int startIndex = 1;
		for (int i = 2; i < sortedPoints.length; ++i) {
			double curSlope = oP.slopeTo(sortedPoints[i]);
			if (curSlope == lastSlope) {
				++adj;
			} else if (oP.compareTo(sortedPoints[i]) == 0) {
				++adj;
			} else if (adj >= 3) {
				// output seg
				outputSeg(oP, sortedPoints, startIndex, adj);
				adj = 1;
				lastSlope = curSlope;
				startIndex = i;
			} else {
				adj = 1;
				lastSlope = curSlope;
				startIndex = i;
			}
		}
		if (adj >= 3) {
			// output seg
			outputSeg(oP, sortedPoints, startIndex, adj);
		}
	}

	public static void main(String[] args) {
		if (args.length < 1)
			return;
		String filename = args[0];
		In input = new In(filename);
		int N = input.readInt();
		if (N < 4)
			return;
		Point[] points = new Point[N];
		Point[] sortedPoints = new Point[N];
		for (int i = 0; i < N; ++i) {
			int x = input.readInt();
			int y = input.readInt();
			points[i] = new Point(x, y);
			sortedPoints[i] = points[i];
		}

		// rescale coordinates and turn on animation mode
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);

		for (int i = 0; i < N; ++i) {
			Point oP = points[i];
			Arrays.sort(sortedPoints, oP.SLOPE_ORDER);
			checkSeg(oP, sortedPoints);
		}
	}
}
