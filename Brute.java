import java.util.Arrays;

public class Brute {
	private static boolean colinear(Point p1, Point p2, Point p3, Point p4) {
		double slope1 = p1.slopeTo(p2);
		double slope2 = p2.slopeTo(p3);
		double slope3 = p3.slopeTo(p4);
		if (slope1 == slope2 && slope2 == slope3) {
			return true;
		}
		return false;
	}

	private static void drawLine(Point p1, Point p2, Point p3, Point p4) {
		Point[] points = new Point[4];
		points[0] = p1;
		points[1] = p2;
		points[2] = p3;
		points[3] = p4;
		Arrays.sort(points);
		StdOut.print(points[0] + " -> " + points[1] + " -> " + points[2]
				+ " -> " + points[3]);
		StdOut.println();
		points[0].draw();
		points[1].draw();
		points[2].draw();
		points[3].draw();
		points[0].drawTo(points[3]);
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
		for (int i = 0; i < N; ++i) {
			int x = input.readInt();
			int y = input.readInt();
			points[i] = new Point(x, y);
		}

		// generate 4 combination of n
		int t = 4;
		int[] cArray = new int[t + 3];
		for (int j = 1; j < cArray.length; ++j) {
			cArray[j] = j - 1;
		}
		cArray[t + 1] = N;
		cArray[t + 2] = 0;

		// rescale coordinates and turn on animation mode
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		StdDraw.show(0);

		while (true) {
			Point p1 = points[cArray[1]];
			Point p2 = points[cArray[2]];
			Point p3 = points[cArray[3]];
			Point p4 = points[cArray[4]];
			if (colinear(p1, p2, p3, p4)) {
				drawLine(p1, p2, p3, p4);
			}
			int j = 1;
			while (cArray[j] + 1 == cArray[j + 1]) {
				cArray[j] = j - 1;
				j = j + 1;
			}
			if (j > t)
				break;
			cArray[j] = cArray[j] + 1;
		}
	}
}
