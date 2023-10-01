package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import sorts.insert.InsertionSort;

/*
 *
MIT License

Copyright (c) 2023 aphitorite

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

public class TopDownPoplarHeapSort extends Sort {

	public TopDownPoplarHeapSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		this.setSortListName("Top-Down Poplar Heap");
		this.setRunAllSortsName("Top-Down Poplar Heap Sort");
		this.setRunSortName("Top-Down Poplar Heapsort");
		this.setCategory("Selection Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	private int MIN_INSERT = 15;

	private void siftDownRec(int[] array, int a, int b) {
		if (b-a < 2) return;

		if (b-a == 2) {
			if (Reads.compareIndices(array, a, a+1, 0.25, true) > 0)
				Writes.swap(array, a, a+1, 0.25, true, false);
			return;
		}
		int m = (a+b)/2, a1, b1;

		if (Reads.compareIndices(array, m-1, b-2, 0.25, true) > 0) //find largest child
			{ a1 = a; b1 = m; }
		else
			{ a1 = m; b1 = b-1; }

		if (Reads.compareIndices(array, b1-1, b-1, 0.25, true) > 0) { //sift down root node
			Writes.swap(array, b1-1, b-1, 0.25, true, false);
			this.siftDownRec(array, a1, b1); //can be implemented in a loop instead of tail recursion
		}
	}

	//recursive heapify (harder to make iterative)
	private void makeHeapRec(int[] array, int a, int b) {
		if (b-a <= this.MIN_INSERT) {
			Highlights.clearMark(2);
			InsertionSort smallSort = new InsertionSort(this.arrayVisualizer);
			smallSort.customInsertSort(array, a, b, 0.25, false);
			return;
		}

		int m = (a+b)/2;

		this.makeHeapRec(array, a, m);
		this.makeHeapRec(array, m, b-1);
		this.siftDownRec(array, a, b);
	}

	private int[] findMaxRec(int[] array, int a, int b, int i) {
		if (i == b-1) return new int[] {a, b};

		int m = (a+b)/2;

		if (i < m) return this.findMaxRec(array, a, m, i);

		int[] r = this.findMaxRec(array, m, b-1, i); //find max to left of sub tree

		if (Reads.compareIndices(array, m-1, r[1]-1, 0.25, true) > 0) //compare and update current max
			return new int[] {a, m};
		else
			return r;
	}

	private void heapSort(int[] array, int a, int b) {
		this.makeHeapRec(array, a, b); //heapify

		int[] p;

		for (int i = b-2; i > a; i--) {
			p = this.findMaxRec(array, a, b, i); //find max root among poplar heaps
			                                     //store the start and end of max heap in p
			if (p[1]-1 < i) { //if current node already max, continue
				Writes.swap(array, p[1]-1, i, 0.25, true, false);
				this.siftDownRec(array, p[0], p[1]);
			}
		}
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		this.heapSort(array, 0, length);
	}
}