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

public class BitonicSortIterative2 extends Sort {
	public BitonicSortIterative2(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Bitonic (Iterative II)");
		this.setRunAllSortsName("Iterative Bitonic Sort");
		this.setRunSortName("Iterative Bitonic Sort");
		this.setCategory("Concurrent Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	//precondition: n != 0
	private int trailingZeroes(int n) {
		int r = 0;
		while ((n & 1) == 0) { r++; n >>= 1; }
		return r;
	}

	private void compSwap(int[] array, int a, int b, boolean c) {
		if ((Reads.compareIndices(array, a, b, 0.5, true) > 0) ^ c)
			Writes.swap(array, a, b, 0.5, false, false);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int n = 2, m = 0;
		while (n < length) { n *= 2; m ^= 1; }

		for (int k = 2; k <= n; k *= 2, m ^= 1)
			for (int k1 = k; k1 > 1; k1 /= 2)
				for (int j = 0, c = 0, ci = 1; j < n; j += k, c += 1 - this.trailingZeroes(ci), ci++)
					for (int i = j; i < j+k; i += k1)
						for (int i1 = 0; i1 < k1/2; i1++)
							if (i+k1/2+i1 < length) this.compSwap(array, i+i1, i+k1/2+i1, ((c & 1) ^ m) == 1);
	}
}