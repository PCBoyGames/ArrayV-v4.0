package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

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

public class BitonicSortAlternative extends Sort {
	public BitonicSortAlternative(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Bitonic (Alternative)");
		this.setRunAllSortsName("Alternative Bitonic Sort");
		this.setRunSortName("Alternative Bitonic Sort");
		this.setCategory("Concurrent Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	private void compSwap(int[] array, int a, int b) {
		if (Reads.compareIndices(array, a, b, 0.5, true) > 0)
			Writes.swap(array, a, b, 0.5, false, false);
	}

	private void bitonicMergeL(int[] array, int a, int a1, int b) {
		int m = b-a1;

		if (m < 1) return;

		for (int i = a1, j = b; i > a && j > a1; )
			this.compSwap(array, --i, --j);

		this.bitonicMergeL(array, a, b-m/2, b);
		this.bitonicMergeL(array, a, a1-m/2, a1);
	}
	private void bitonicMergeR(int[] array, int a, int b1, int b) {
		int m = b1-a;

		if (m < 1) return;

		for (int i = a, j = b1; i < b1 && j < b; )
			this.compSwap(array, i++, j++);

		this.bitonicMergeR(array, a, a+m/2, b);
		this.bitonicMergeR(array, b1, b1+m/2, b);
	}

	private void bitonicSort(int[] array, int a, int b) {
		if (b-a < 2) return;

		int m = (a+b)/2;

		this.bitonicSort(array, a, m);
		this.bitonicSort(array, m, b);

		for (int i = m-1, j = m; i >= a; i--, j++)
			this.compSwap(array, i, j);

		int k;

		for (k = 1; 2*k < m-a; k *= 2);
		this.bitonicMergeL(array, a, m-k, m);

		for (k = 1; 2*k < b-m; k *= 2);
		this.bitonicMergeR(array, m, m+k, b);
	}

	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) {
		this.bitonicSort(array, 0, sortLength);
	}
}