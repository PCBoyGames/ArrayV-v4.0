package sorts.hybrid;

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

public final class ProportionExtendSort extends Sort {
	public ProportionExtendSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Proportion Extend (PE)");
		this.setRunAllSortsName("Proportion Extend Sort");
		this.setRunSortName("PEsort");
		this.setCategory("Hybrid Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
		this.setQuestion("Enter pSize.  Sample size will be n/pSize:  (default: 16)", 16);
	}

	private final int MIN_INSERT = 32;

	private int rightBinSearch(int[] array, int a, int b, int val) {
		while(a < b) {
			int m = a+(b-a)/2;
			Highlights.markArray(2, m);
			Delays.sleep(0.25);

			if(Reads.compareValues(val, array[m]) < 0)
				b = m;
			else
				a = m+1;
		}
		return a;
	}
	private void binaryInsert(int[] array, int a, int m, int b) {
		for(int i = m; i < b; i++) {
			int t = array[i];
			Highlights.markArray(3, i);
			int j = this.rightBinSearch(array, a, i, t);
			Highlights.clearMark(3);
			Writes.arraycopy(array, j, array, j+1, i-j, 0.25, true, false);
			Writes.write(array, j, t, 0.5, true, false);
		}
	}

	private int partition(int[] array, int a, int b, int p) {
		int i = a-1, j = b;
		Highlights.markArray(3, p);

		do {
			do {
				i++;
				Highlights.markArray(1, i);
				Delays.sleep(0.5);
			}
			while(i < j && Reads.compareValues(array[i], array[p]) < 0);

			do {
				j--;
				Highlights.markArray(2, j);
				Delays.sleep(0.5);
			}
			while(j >= i && Reads.compareValues(array[j], array[p]) > 0);

			if(i < j) Writes.swap(array, i, j, 1, false, false);
			else {
				Highlights.clearMark(3);
				return i;
			}
		}
		while(true);
	}

	private void peSort(int[] array, int a, int b, int pSize) {
		int n = b-a;

		if(n <= this.MIN_INSERT) {
			this.binaryInsert(array, a, a+1, b);
			return;
		}

		double p = 1d/pSize;
		int m = a + Math.max(1, (int)(n*p));
		this.peSort(array, a, m, pSize);
		this.peRec(array, a, m, b, pSize);
	}

	private void peRec(int[] array, int a, int m, int b, int pSize) {
		if(b-a <= this.MIN_INSERT) {
			this.binaryInsert(array, a, Math.max(a+1, m), b);
			return;
		}
		if(m-a < 1) {
			this.peSort(array, a, b, Math.max(2, pSize/2));
			return;
		}
		if(b-m < 1) return;

		int m1 = (a+m)/2;
		int m2 = this.partition(array, m, b, m1);
		int m3 = m2;

		while(m-- > m1) Writes.swap(array, m, --m2, 1, true, false);

		this.peRec(array, a, m, m2, pSize);
		this.peRec(array, m2+1, m3, b, pSize);
	}

	@Override
	public void runSort(int[] array, int length, int pSize) {
		if(pSize < 2) pSize = 16;
		this.peSort(array, 0, length, Math.min(pSize, length));
	}
}