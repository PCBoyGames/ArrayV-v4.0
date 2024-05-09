package sorts.quick;

import sorts.insert.BinaryInsertionSort;
import sorts.templates.Sort;
import main.ArrayVisualizer;

/*
 *
MIT License

Copyright (c) 2020-2021 aphitorite

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

final public class StacklessHybridQuickSort2 extends Sort {
	public StacklessHybridQuickSort2(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Stackless Hybrid Quick II");
		this.setRunAllSortsName("Stackless Hybrid Quicksort");
		this.setRunSortName("Stackless Hybrid Quicksort");
		this.setCategory("Quick Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	private final int MIN_INSERT = 16;

	private void medianOfThree(int[] array, int a, int b) {
		int m = a+(b-1-a)/2;

		if (Reads.compareIndices(array, a, m, 1, true) > 0)
			Writes.swap(array, a, m, 1, true, false);

		if (Reads.compareIndices(array, m, b-1, 1, true) > 0) {
			Writes.swap(array, m, b-1, 1, true, false);

			if (Reads.compareIndices(array, a, m, 1, true) > 0)
				return;
		}

		Writes.swap(array, a, m, 1, true, false);
	}

	private int partition(int[] array, int a, int b) {
		int i = a, j = b;

		this.medianOfThree(array, a, b);
		Highlights.markArray(3, a);

		do {
			do {
				i++;
				Highlights.markArray(1, i);
				Delays.sleep(0.5);
			}
			while (i < j && Reads.compareIndices(array, i, a, 0, false) < 0);

			do {
				j--;
				Highlights.markArray(2, j);
				Delays.sleep(0.5);
			}
			while (j >= i && Reads.compareIndices(array, j, a, 0, false) >= 0);

			if (i < j) Writes.swap(array, i, j, 1, true, false);
			else {
				Writes.swap(array, a, j, 1, true, false);
				Highlights.clearMark(3);
				return j;
			}
		}
		while (true);
	}
	private int partitionEq(int[] array, int a, int b) {
		int i = a, j = b;

		this.medianOfThree(array, a, b);
		Highlights.markArray(3, a);

		do {
			do {
				i++;
				Highlights.markArray(1, i);
				Delays.sleep(0.5);
			}
			while (i < j && Reads.compareIndices(array, i, a, 0, false) <= 0);

			do {
				j--;
				Highlights.markArray(2, j);
				Delays.sleep(0.5);
			}
			while (j >= i && Reads.compareIndices(array, j, a, 0, false) > 0);

			if (i < j) Writes.swap(array, i, j, 1, true, false);
			else      return i;
		}
		while (true);
	}

	private int leftExpSearch(int[] array, int a, int b, int p) {
		int i = 32;
		while (a-1+i < b && Reads.compareIndices(array, p, a-1+i, 1, true) > 0) i *= 2;

		b = Math.min(b, a-1+i);
		if (i > 32) a += i/2;

		while (a < b) {
			int m = a+(b-a)/2;

			if (Reads.compareIndices(array, p, m, 1, true) <= 0)
				b = m;
			else
				a = m+1;
		}
		return a;
	}

	private void quickSort(int[] array, int a, int b) {
		int b1 = b;
		BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);

		do {
			while (b1-a > MIN_INSERT) {
				this.medianOfThree(array, a, b1);
				int p = this.partition(array, a, b1);

				if (p == a) a = this.partitionEq(array, a, b1);
				else       { Writes.swap(array, p, b, 1, true, false); b1 = p; }
			}
			smallSort.customBinaryInsert(array, a, b1, 0.25);

			a = b1+1;
			if (a >= b) {
				if (a-1 < b) Writes.swap(array, a-1, b, 1, true, false);
				return;
			}

			b1 = this.leftExpSearch(array, a, b, a-1);
			Writes.swap(array, a-1, b, 1, true, false);
		}
		while (true);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int a = 0, b = length;
		BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);

		while (b-a > MIN_INSERT) {
			this.medianOfThree(array, a, b);
			int p = this.partition(array, a, b);

			if (p == a) a = this.partitionEq(array, a, b);
			else       { this.quickSort(array, a, p); a = p+1; }
		}
		smallSort.customBinaryInsert(array, a, b, 0.25);
	}
}