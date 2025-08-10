package sorts.concurrent;

import sorts.templates.Sort;
import main.ArrayVisualizer;

import java.util.Random;

/*
 *
MIT License

Copyright (c) 2024 aphitorite & Control

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

public class AKSSortingNetwork extends Sort {
	public AKSSortingNetwork(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("AKS Sorting Network");
		this.setRunAllSortsName("AKS Sorting Network");
		this.setRunSortName("AKS Sorting Network");
		this.setCategory("Concurrent Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	/**
		galactic sorting algorithm:

		first ever O(n log n) size and O(log n) depth sorting network
		but the original algorithm has an extremely large constant (approx 3.38 * 10**16 N log N)
		implementation has a constant of 192 N log N instead

		implements https://doi.org/10.1145/800061.808726 using random multigraphs
		in place of the expander graph constructions mentioned in the original paper

		special thanks to control for helping me understand the algorithm
		as well as explaining and studying expander graph halvers

		@author aphitorite
	*/

	private int end;
	private int N;

	// parameters (feel free to adjust)

	private static int logA = 1; // some constant (>= 1)

	private static int NEARSORT_DEPTH = 4;   // log of epsilon of nearsort
	private static int   HALVER_DEPTH = 32;  // >= 3 (min degree of expander graphs)
	private static int       MIN_SORT = 128; // recommended: >= 2*HALVER_DEPTH  -OR-  2 ** triangular_root(HALVER_DEPTH)
	private static int   ZIG_ZAG_ITER = 3;   // number of zig-zag iterations (>= 3)

	// constants (do not change)

	private static double SLEEP = 0.5 / HALVER_DEPTH; // dynamic halver delay

	private int X(int i, int t) {
		return N >> Math.min((logA+1)*(t+1) - logA*i, 31); // undefined behavior when shifting > 31 bits
	}
	private int Y(int i, int t) {
		int r = 0;
		while(i > 0) r += X(i--, t);
		return r;
	}

	private void writeInterval(int[][] tree, int i, int a, int b, int c, int d) {
		tree[i][0] = a;
		tree[i][1] = b;
		tree[i][2] = c;
		tree[i][3] = d;
		Writes.changeAuxWrites(4);
	}

	private void compSwap(int[] array, int[] map, int a, int b) {
		if (map[b] < this.end && Reads.compareIndices(array, map[a], map[b], SLEEP, true) > 0)
			Writes.swap(array, map[a], map[b], SLEEP, false, false);
	}

	private void smallSort(int[] array, int[] map, int a, int b) { // creasesort for compactness
		if (b-a < 2) return;

		int n = (1 << 31-Integer.numberOfLeadingZeros(b-a-1)) - 1;

		for (int k = 2*n; k > 0; k /= 2) {
			for (int i = a+1; i < b; i += 2)
				this.compSwap(array, map, i-1, i);
			for (int j = n; j >= Math.max(k/2, 1); j /= 2)
				for (int i = a+1+j; i < b; i += 2)
					this.compSwap(array, map, i-j, i);
		}
	}

	private void epsHalver(int[] array, int[] map, int[] idx, Random r, int a, int m, int h, boolean odd) {
		for (int k = HALVER_DEPTH-2; k > 0; k--) {
			for (int i = 0; i < h; i++) {
				int j = r.nextInt(i+1);
				idx[i] = idx[j];
				idx[j] = i;
				Writes.changeAuxWrites(2);
			}
			for (int i = 0, s; i < h; i++)
				if ((s = idx[i]) < h-1 || !odd)
					this.compSwap(array, map, a+i, m+s);
		}
		for (int i = 0, s; i < h; i++)
			if ((s = i) < h-1 || !odd)
				this.compSwap(array, map, a+i, m+s);

		for (int i = 0, s; i < h; i++)
			if ((s = (i+1)%h) < h-1 || !odd)
				this.compSwap(array, map, a+i, m+s);
	}

	private void epsNearsort(int[] array, int[] map, int[] idx, Random r, int a, int n, int d) {
		if (n <= MIN_SORT || d >= NEARSORT_DEPTH) {
			if (n <= MIN_SORT) this.smallSort(array, map, a, a+n);
			return;
		}

		int h = n/2, m = a+n-h;

		this.epsHalver(array, map, idx, r, a, m, h + n%2, n%2 == 1);

		this.epsNearsort(array, map, idx, r, a, n-h, d+1);
		this.epsNearsort(array, map, idx, r, m,   h, d+1);
	}

	private int mapInt(int[] map, int[] interval, int i, int j) {
		int a = interval[i+i], b = interval[i+i+1];
		while(a < b) Writes.write(map, j++, a++, 0, false, true);
		return j;
	}
	private void cherry(int[] array, int[] map, int[] idx, Random rand, int[][] tree, int root, int tSize) {
		int j = this.mapInt(map, tree[root], 0, 0);

		int l = 2*root + 1, r = l+1;

		if (l < tSize) { // if left and right child nodes exist
			j = this.mapInt(map, tree[l], 0, j);
			j = this.mapInt(map, tree[l], 1, j);
			j = this.mapInt(map, tree[r], 0, j);
			j = this.mapInt(map, tree[r], 1, j);
		}
		j = this.mapInt(map, tree[root], 1, j);

		this.epsNearsort(array, map, idx, rand, 0, j, 0);
	}
	private void zig(int[] array, int[] map, int[] idx, Random rand, int[][] tree, int t) {
		int tSize = (2 << t) - 1;

		for (int i = 1; i <= tSize; i = 4*i + 3)
			for (int j = i/2; j < i; j++)
				this.cherry(array, map, idx, rand, tree, j, tSize);
	}
	private void zag(int[] array, int[] map, int[] idx, Random rand, int[][] tree, int t) {
		this.epsNearsort(array, map, idx, rand, 0, mapInt(map, tree[0], 1, mapInt(map, tree[0], 0, 0)), 0);

		int tSize = (2 << t) - 1;

		for (int i = 3; i <= tSize; i = 4*i + 3)
			for (int j = i/2; j < i; j++)
				this.cherry(array, map, idx, rand, tree, j, tSize);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		this.end = length;

		if (length <= MIN_SORT) {
			int[] map = Writes.createExternalArray(length);

			for (int i = 0; i < length; i++)
				Writes.write(map, i, i, 0, false, true);

			this.smallSort(array, map, 0, length);

			Writes.deleteExternalArray(map);

			return;
		}

		int logN = 32 - Integer.numberOfLeadingZeros(length-1);
		this.N   = 1 << logN;

		int[] idx = Writes.createExternalArray(N/2);
		int[] map = Writes.createExternalArray(N);

		int[][] tree = new int[N-1][4]; // implicit tree array of 4-tuples
		Writes.changeAllocAmount(tree.length * 4);

		Random r = new Random();

		boolean minSort = false;

		sortLoop:
		for (int t = 1; t < logN; t++) { // O(log n)
			for (int i = 0, k = 0; i <= t; i++) {
				for (int j = 0, s = N >> i; !minSort && j < (1<<i); j++) {
					int p  = j * s,
						l1 = p   + (j%2 == 1 ? 0 : Y(i,t)),
						l2 = p   + Y(i+1,t),
						r1 = p+s - Y(i+1,t),
						r2 = p+s - (j%2 == 1 ? Y(i,t) : 0);

					minSort |= (l2-l1 == 0 && s <= MIN_SORT); // detects when aks sorts elements
					                                          // with a distance <= MIN_SORT
					if (i == t) l2 = r1 = r2;

					if (minSort) {
						for (k = 0; k < s; k++) Writes.write(map, k, p+k, 0, false, true);
						this.smallSort(array, map, 0, s);
					}
					else this.writeInterval(tree, k++, l1, l2, r1, r2);
				}
				if (minSort) break sortLoop;
			}
			for (int i = 0; i < ZIG_ZAG_ITER; i++) {
				if (i%2 == 1) this.zag(array, map, idx, r, tree, t);
				else         this.zig(array, map, idx, r, tree, t);
			}
		}
		Writes.deleteExternalArray(idx);
		Writes.deleteExternalArray(map);
		Writes.changeAllocAmount(-tree.length * 4);
	}
}