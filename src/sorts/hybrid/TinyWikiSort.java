package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;

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

final public class TinyWikiSort extends Sort {
    public TinyWikiSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Tiny Wiki");
        this.setRunAllSortsName("Tiny Wiki Sort");
        this.setRunSortName("Tiny Wikisort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

	private final double SQRTH = Math.sqrt(0.5);

	private void blockSwap(int[] array, int a, int b, int s) {
		while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
	}
	private void rotate(int[] array, int a, int m, int b) {
		Writes.reversal(array, a, m-1, 1, true, false);
		Writes.reversal(array, m, b-1, 1, true, false);
		Writes.reversal(array, a, b-1, 1, true, false);
    }

	private void mergeFWInP(int[] array, int a, int m, int b) {
		int i = a, j = m, k;

		while (i < j && j < b) {
			if (Reads.compareValues(array[i], array[j]) > 0) {
				k = j;
				while (++k < b && Reads.compareIndices(array, i, k, 0, false) > 0);

				this.rotate(array, i, j, k);

				i += k-j;
				j = k;
			}
			else i++;
		}
	}

	private void mergeFW(int[] array, int p, int a, int m, int b) {
		int len2 = m-a, pEnd = p+len2;

		this.blockSwap(array, p, a, len2);

		while (p < pEnd && m < b) {
			if (Reads.compareValues(array[p], array[m]) <= 0)
				Writes.swap(array, a++, p++, 1, true, false);

			else Writes.swap(array, a++, m++, 1, true, false);
		}
		while (p < pEnd)
			Writes.swap(array, a++, p++, 1, true, false);
	}

	private void mergeFWExt(int[] array, int[] swap, int a, int m, int b) {
		int len2 = m-a, p = 0, pEnd = len2;

		Highlights.clearMark(2);
		Writes.arraycopy(array, a, swap, 0, len2, 0.5, true, true);

		while (p < pEnd && m < b) {
			Highlights.markArray(2, m);

			if (Reads.compareValues(swap[p], array[m]) <= 0)
				Writes.write(array, a++, swap[p++], 0.5, true, true);

			else Writes.write(array, a++, array[m++], 0.5, true, true);
		}
		Highlights.clearMark(2);

		while (p < pEnd)
			Writes.write(array, a++, swap[p++], 0.5, false, true);
	}

	private void blockMerge(int[] array, int[] swap, int a, int m, int b, int p, int bLen, int merge) {
		int lb = (m-a)/bLen, rb = (b-m)/bLen, bCnt = lb+rb;
		int a1 = m-lb*bLen, b1 = m+rb*bLen;

		for (int i = m; i < b1; i += bLen)
			Writes.swap(array, i, i+bLen-1, 1, true, false);

		int mKey = array[p+lb];

		for (int j = 0; j < bCnt; j++) {
			int min = j;

			for (int i = j+1; i < bCnt; i++) {
				int cmp = Reads.compareIndices(array, a1+i*bLen, a1+min*bLen, 0.5, true);

				if (cmp < 0 || (cmp == 0 && Reads.compareIndices(array, p+i, p+min, 0.5, true) < 0))
					min = i;
			}
			if (min > j) {
				this.blockSwap(array, a1+j*bLen, a1+min*bLen, bLen);
				Writes.swap(array, p+j, p+min, 1, true, false);
			}
			if (Reads.compareIndexValue(array, p+j, mKey, 0.5, true) >= 0)
				Writes.swap(array, a1+j*bLen, a1+j*bLen+bLen-1, 1, true, false);
		}
		for (int i = b1; i > a1; i -= bLen) {
			if (Reads.compareIndices(array, i-1, i, 0.5, true) > 0) {
				if (merge == 2)      this.mergeFWExt(array, swap, i-bLen, i, b);
				else if (merge == 1) this.mergeFW(array, p, i-bLen, i, b);
				else                this.mergeFWInP(array, i-bLen, i, b);
			}
		}
		if (merge == 2)      this.mergeFWExt(array, swap, a, a1, b);
		else if (merge == 1) this.mergeFW(array, p, a, a1, b);
		else                this.mergeFWInP(array, a, a1, b);
	}

	private void blockMergeSort(int[] array, int[] swap, int a, int b, int bLen) {
		int n = b-a;
		BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);

		if (n <= 32) {
			smallSort.customBinaryInsert(array, a, b, 0.25);
			return;
		}
		int bLenH = (int)(bLen*SQRTH + 0.5d);
		int m = (a+b)/2;

		this.blockMergeSort(array, swap, a, m, bLenH);
		this.blockMergeSort(array, swap, m, b, bLenH);

		if (Reads.compareValues(array[m-1], array[m]) <= 0) return;

		if (Reads.compareValues(array[a], array[b-1]) > 0) {
			this.rotate(array, a, m, b);
			return;
		}
		if (m-a <= swap.length) {
			this.mergeFWExt(array, swap, a, m, b);
			return;
		}

		if (swap.length > bLen) bLen = swap.length;
		int tLen = (b-a)/bLen-1, keys = tLen;
		int merge = swap.length < bLen ? 1 : 2;
		if (merge != 2 && bLen > tLen) keys = bLen;

		int i, i1 = a, fk = 1;

		for (i = a+1; i < m && fk < keys; i++)
			if (Reads.compareIndices(array, i-1, i, 0.5, true) < 0) { fk++; i1 = i; }

		for (int j = i1-1, k = 1; j > a; j--) {
			if (Reads.compareIndices(array, j-1, j, 0.5, true) < 0) k++;
			else Writes.swap(array, j, j+k, 1, true, false);
		}
		int a1 = a+fk;
		smallSort.customBinaryInsert(array, a, a1, 0.25);

		if (fk < keys && fk <= 4) this.mergeFWInP(array, a1, m, b);
		else {
			merge = fk < keys ? 0 : merge;
			if (merge == 0) bLen = n/fk;
			this.blockMerge(array, swap, a1, m, b, a, bLen, merge);
		}
		smallSort.customBinaryInsert(array, a, a1, 0.25);
		if (fk <= swap.length) this.mergeFWExt(array, swap, a, a1, b);
		else this.mergeFWInP(array, a, a1, b);
	}

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		bucketCount = 16;
		int[] swap = Writes.createExternalArray(bucketCount);
		this.blockMergeSort(array, swap, 0, length, (int)(Math.sqrt(length)+0.5d));
		Writes.deleteExternalArray(swap);
    }
}