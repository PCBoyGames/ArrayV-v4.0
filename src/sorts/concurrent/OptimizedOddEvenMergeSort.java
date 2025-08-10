package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2020-2025 aphitorite

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

public class OptimizedOddEvenMergeSort extends Sort {
	public OptimizedOddEvenMergeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Optimized Odd-Even Merge");
		this.setRunAllSortsName("Optimized Odd-Even Merge Sort");
		this.setRunSortName("Optimized Odd-Even Mergesort");
		this.setCategory("Concurrent Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	private void ce(int[] array, int a, int b) {
		if (Reads.compareIndices(array, a, b, 0.5, true) > 0)
			Writes.swap(array, a, b, 0.5, true, false);
	}

	private void mergePass(int[] array, int a, int b) {
		int h = (b-a)/2;

		for (int i = 0; i < h; i++)
			this.ce(array, a+i, b-h+i);
	}

	//precondition: diff(m-a, b-m) <= 1
	private void mergePassLevel(int[] array, int a, int m, int b, int k) {
		if (m-a < b-m) a--;
		if (b-m < m-a) b++;

		int n = b-a, p = m;

		for (int j = a+k; j < m; j += 2*k) {
			for (int i = j; i < Math.min(m, j+k); i++) {
				if (i+k < m) {
					int mi = a+(n-1)-(i-a);
					this.ce(array, i, i+k);
					this.ce(array, mi-k, mi);
					if (j + 2*k > m) p++;
				}
				else this.ce(array, i, p++);
			}
		}
	}

	@Override
	public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
		int a = 0, b = sortLength;
		int n = b-a;

		for (int d = 1 << 32-Integer.numberOfLeadingZeros(n-1), k = 1; d > 1; d /= 2, k *= 2) {
			for (int j = k; j > 0; j /= 2) {
				for (int i = a, dec = 0; i < b; ) {
					int im = i + (dec += n)/d;
					dec %= d;

					int ib = im + (dec += n)/d;
					dec %= d;

					if (j == k) this.mergePass(array, i, ib);
					else this.mergePassLevel(array, i, im, ib, j);

					i = ib;
				}
			}
		}
	}
}