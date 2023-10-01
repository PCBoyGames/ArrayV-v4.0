package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
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

public class ProportionExtendMergeSort extends Sort {
    public ProportionExtendMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Proportion Extend Merge");
        this.setRunAllSortsName("Proportion Extend Mergesort");
        this.setRunSortName("Proportion Extend Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

	private int MIN_INSERT = 8;
	private BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);

	private void blockSwap(int[] array, int a, int b, int s) {
		while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
	}

	private int partition(int[] array, int a, int b, int p) {
        int i = a - 1;
        int j = b;
		Highlights.markArray(3, p);

        while (true) {
			do {
				i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
			}
			while (i < j && Reads.compareIndices(array, i, p, 0, false) == -1);

			do {
				j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
			}
            while (j >= i && Reads.compareIndices(array, j, p, 0, false) == 1);

            if (i < j) Writes.swap(array, i, j, 1, true, false);
            else      return i;
        }
    }

	private void mergeFW(int[] array, int a, int m, int b, int p) {
		int pLen = m-a;
        this.blockSwap(array, a, p, pLen);

		int i = 0, j = m, k = a;

		while (i < pLen && j < b) {
			if (Reads.compareValues(array[p+i], array[j]) <= 0)
                Writes.swap(array, k++, p+(i++), 1, true, false);
            else
                Writes.swap(array, k++, j++, 1, true, false);
		}
		while (i < pLen) Writes.swap(array, k++, p+(i++), 1, true, false);
	}
	private void mergeBW(int[] array, int a, int m, int b, int p) {
        int pLen = b-m;
        this.blockSwap(array, m, p, pLen);

        int i = pLen-1, j = m-1, k = b-1;

        while (i >= 0 && j >= a) {
            if (Reads.compareValues(array[p+i], array[j]) >= 0)
                Writes.swap(array, k--, p+(i--), 1, true, false);
            else
                Writes.swap(array, k--, j--, 1, true, false);
        }
        while (i >= 0) Writes.swap(array, k--, p+(i--), 1, true, false);
    }
	private void smartMerge(int[] array, int a, int m, int b, int p) {
		if (m-a < b-m) this.mergeFW(array, a, m, b, p);
		else          this.mergeBW(array, a, m, b, p);
	}

	private void mergeTo(int[] array, int a, int m, int b, int p) {
		int i = a, j = m;

		while (i < m && j < b) {
			if (Reads.compareValues(array[i], array[j]) <= 0)
				Writes.swap(array, p++, i++, 1, true, false);
			else
				Writes.swap(array, p++, j++, 1, true, false);
		}
		while (i < m) Writes.swap(array, p++, i++, 1, true, false);
		while (j < b) Writes.swap(array, p++, j++, 1, true, false);
	}
	private void pingPongMerge(int[] array, int a, int m1, int m, int m2, int b, int p) {
		int p1 = p+m-a, pEnd = p+b-a;

		this.mergeTo(array, a, m1, m, p);
		this.mergeTo(array, m, m2, b, p1);
		this.mergeTo(array, p, p1, pEnd, a);
	}
	private void mergeSort(int[] array, int a, int b, int p) {
		int n = b-a, j = n;
		for (; (j+3)/4 >= this.MIN_INSERT; j = (j+3)/4);

		for (int i = a; i < b; i += j)
			this.smallSort.customBinaryInsert(array, i, Math.min(b, i+j), 0.25);

		for (int i; j < n; j *= 4) {
			for (i = a; i+2*j < b; i += 4*j)
				this.pingPongMerge(array, i, i+j, i+2*j, Math.min(i+3*j, b), Math.min(i+4*j, b), p);
			if (i+j < b)
				this.mergeBW(array, i, i+j, b, p);
		}
	}
	private void smartMergeSort(int[] array, int a, int b, int p, int pb) {
		if (b-a <= pb-p) {
			this.mergeSort(array, a, b, p);
			return;
		}
		int m = (a+b) >>> 1;

		this.mergeSort(array, a, m, p);
		this.mergeSort(array, m, b, p);
		this.mergeFW(array, a, m, b, p);
	}

	private void peSort(int[] array, int a, int m, int b) {
		int n = b-a;

		if (n < 4*this.MIN_INSERT) {
			this.smallSort.customBinaryInsert(array, a, b, 0.25);
			return;
		}
		if (m-a <= n/3) {
			int t = (n+2)/3;
			this.smartMergeSort(array, m, b-t, b-t, b);
			this.smartMerge(array, a, m, b-t, b-t);
			m = b-t;
		}
		int m1 = (a+m) >>> 1;
		int m2 = this.partition(array, m, b, m1);

		int i = m, j = m2;
		while (i > m1) Writes.swap(array, --i, --j, 1, true, false);

		m = m2-(m-m1);

		if (m-m1 < b-m2) {
			this.mergeSort(array, m1, m, m2);
			this.smartMerge(array, a, m1, m, m2);
			this.peSort(array, m+1, m2, b);
		}
		else {
			this.mergeSort(array, m2, b, m1);
			this.smartMerge(array, m+1, m2, b, m1);
			this.peSort(array, a, m1, m);
		}
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.peSort(array, 0, 0, length);
    }
}