package sorts.quick;

import sorts.templates.Sort;
import sorts.insert.InsertionSort;
import main.ArrayVisualizer;

/*
 *
MIT License

Copyright (c) 2022 aphitorite

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

final public class LogSort2 extends Sort {
	public LogSort2(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Log II");
		this.setRunAllSortsName("Log Sort");
		this.setRunSortName("Logsort");
		this.setCategory("Quick Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
		//this.setQuestion("Set block size (default: calculates minimum block length for current length)", 1);
	}

	//calculations

	private int productLog(int n) { //for arrayv only !!!
		int r = 1;
		while ((r<<r)+r-1 < n) r++;
		return r;
	}

	private int log2(int n) {
		int r = 0;
		while ((1 << r) < n) r++;
		return r;
	}

	//pivot selection

	private void insertSort(int[] array, int a, int n, boolean auxWrite) {
		Highlights.clearMark(2);
		InsertionSort smallSort = new InsertionSort(this.arrayVisualizer);
		smallSort.customInsertSort(array, a, a+n, 0.25, auxWrite);
	}
	private void quickSelect(int[] array, int a, int n, int p) {
		while (n > 16) {
			int a1 = a+n/2, a2 = a+n-1;

			if (Reads.compareIndices(array, a1, a, 0.5, true) > 0)
				Writes.swap(array, a1, a, 0.5, false, true);

			if (Reads.compareIndices(array, a, a2, 0.5, true) > 0)
				Writes.swap(array, a, a2, 0.5, false, true);

			if (Reads.compareIndices(array, a1, a, 0.5, true) > 0)
				Writes.swap(array, a1, a, 0.5, false, true);

			int i = a, j = a+n;

			while (true) {
				while (++i <  j && Reads.compareIndices(array, i, a, 0.5, true) < 0);
				while (--j >= i && Reads.compareIndices(array, j, a, 0.5, true) > 0);

				if (i < j) Writes.swap(array, i, j, 1, true, true);
				else	{ Writes.swap(array, a, j, 1, true, true); break; }
			}
			int m = j-a;

			if (p < m) n = m;
			else if (p > m) { n -= m+1; p -= m+1; a = j+1; }
			else return;
		}
		this.insertSort(array, a, n, true);
	}

	private int medianOfNine(int[] array, int[] swap, int a, int n) {
		int s = (n-1)/8;

		for (int i = 0, j = a; i < 9; i++, j += s) {
			Highlights.markArray(1, j);
			Writes.write(swap, i, array[j], 1, false, true);
		}
		this.insertSort(swap, 0, 9, true);

		return swap[4];
	}
	private int smartMedian(int[] array, int[] swap, int a, int n, int bLen) {
		int cbrt;
		for (cbrt = 32; cbrt*cbrt*cbrt < n && cbrt < 1024; cbrt *= 2);

		int d = Math.min(bLen, cbrt); d -= d%2;
		int s = n/d;

		for (int i = 0, j = a + (int)(Math.random() * s); i < d; i++, j += s) {
			Highlights.markArray(1, j);
			Writes.write(swap, i, array[j], 1, false, true);
		}
		this.quickSelect(swap, 0, d, d/2);

		return swap[d/2];
	}

	//logsort

	private void blockSwap(int[] array, int a, int b, int s) { //for arrayv only (normally uses 3 memcpy's)
		while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
	}
	private int blockRead(int[] array, int a, int piv, int wLen, int pCmp) {
		int r = 0, i = 0;

		while (wLen-- > 0)
			r |= (Reads.compareValues(array[a++], piv) < pCmp ? 1 : 0) << (i++);

		return r;
	}
	private void blockXor(int[] array, int a, int b, int v) { //special thanks to Distray
		while (v > 0) {
			if ((v&1) > 0) Writes.swap(array, a, b, 1, true, false);
			v >>= 1; a++; b++;
		}
	}
	private int partitionEasy(int[] array, int[] swap, int a, int n, int piv, int pCmp) {
		int p = a, ps = 0;

		for (int i = n; i > 0; i--, p++) {
			Highlights.markArray(1, p);
			Delays.sleep(0.25);

			if (Reads.compareValues(array[p], piv) < pCmp)
				Writes.write(array, a++, array[p], 0.25, true, false);
			else
				Writes.write(swap, ps++, array[p], 0.25, false, true);
		}
		Writes.arraycopy(swap, 0, array, a, ps, 0.5, true, false);

		return a;
	}
	private int partition(int[] array, int[] swap, int a, int n, int bLen, int piv, int pCmp) {
		if (n <= bLen) return this.partitionEasy(array, swap, a, n, piv, pCmp);

		int p = a;
		int l = 0, r = 0, lb = 0, rb = 0;

		for (int i = 0; i < n; i++) {
			if (Reads.compareValues(array[a+i], piv) < pCmp)
				Writes.write(array, p+(l++), array[a+i], 0.5, true, false);
			else
				Writes.write(swap, r++, array[a+i], 0.5, true, true);

			if (l == bLen) {
				p += bLen;
				l = 0;
				lb++;
			}
			if (r == bLen) {
				Writes.arraycopy(array, p, array, p+bLen, l, 0.5, true, false);
				Writes.arraycopy(swap, 0, array, p, bLen, 0.5, true, false);
				p += bLen;
				r = 0;
				rb++;
			}
		}
		Writes.arraycopy(swap, 0, array, p+l, r, 1, true, false);

		boolean x = lb < rb;
		int min = x ? lb : rb;
		int m = a+lb*bLen;

		if (min > 0) {
			int max = lb+rb-min;
			int wLen = this.log2(min);

			int j = a, k = a, v = 0;

			for (int i = min; i > 0; i--) {
				while (!(Reads.compareValues(array[j+wLen], piv) < pCmp)) j += bLen;
				while (  Reads.compareValues(array[k+wLen], piv) < pCmp ) k += bLen;
				this.blockXor(array, j, k, v++);
				j += bLen; k += bLen;
			}

			j = x ? p-bLen : a; k = j;
			int s = x ? -bLen : bLen;

			for (int i = max; i > 0; ) {
				if (x ^ (Reads.compareValues(array[k+wLen], piv) < pCmp)) {
					this.blockSwap(array, j, k, bLen);
					j += s;
					i--;
				}
				k += s;
			}

			j = 0;
			int ps = x ? a : m, pa = ps, pb = x ? m : a;
			int mask = ((x ? 1 : 0) << wLen) - (x ? 1 : 0);

			for (int i = min; i > 0; i--) {
				k = mask ^ this.blockRead(array, ps, piv, wLen, pCmp);

				while (j != k) {
					this.blockSwap(array, ps, pa + k*bLen, bLen);
					k = mask ^ this.blockRead(array, ps, piv, wLen, pCmp);
				}
				this.blockXor(array, ps, pb, j);
				j++; ps += bLen; pb += bLen;
			}
		}
		if (l > 0) {
			Highlights.clearMark(2);
			Writes.arraycopy(array, p, swap, 0, l, 0.5, true, true);
			Writes.arraycopy(array, m, array, m+l, rb*bLen, 0.5, true, false);
			Writes.arraycopy(swap, 0, array, m, l, 0.5, true, false);
		}
		return m+l;
	}

	private void logSortMain(int[] array, int[] swap, int a, int n, int bLen) {
		while (n > 24) {
			int piv = n < 2048 ? this.medianOfNine(array, swap, a, n)
			                   : this.smartMedian(array, swap, a, n, bLen);

			Highlights.clearMark(2);
			int p = this.partition(array, swap, a, n, bLen, piv, 1);
			int m = p-a;

			if (m == n) {
				p = this.partition(array, swap, a, n, bLen, piv, 0);
				n = p-a;

				continue;
			}
			this.logSortMain(array, swap, p, n-m, bLen);
			n = m;
		}
		this.insertSort(array, a, n, false);
	}
	private void logSort(int[] array, int a, int n, int bLen) {
		bLen = Math.max(9, Math.min(n, bLen));
		int[] swap = Writes.createExternalArray(bLen);
		this.logSortMain(array, swap, a, n, bLen);
		Writes.deleteExternalArray(swap);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		bucketCount = this.productLog(length);
		this.logSort(array, 0, length, bucketCount);
	}
}