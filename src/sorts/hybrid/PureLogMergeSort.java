package sorts.hybrid;

import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;
import utils.IndexedRotations;
import main.ArrayVisualizer;

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

final public class PureLogMergeSort extends Sort {
	public PureLogMergeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Pure Log Merge");
		this.setRunAllSortsName("Pure Log Merge Sort");
		this.setRunSortName("Pure Log Mergesort");
		this.setCategory("Hybrid Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
		this.setQuestion("Set block size (default: calculates minimum block length for current length)", 1);
	}

	private final int MIN_INSERT = 16;

	private BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);

	private int productLog(int n) {
		int r = 1;
		while ((r<<r)+r-1 < n) r++;
		return r;
	}

	private int log2(int n) {
		return 31 - Integer.numberOfLeadingZeros(n);
	}

	private int leftBinSearch(int[] array, int a, int b, int val) {
		while (a < b) {
			int m = (a+b) >>> 1;
			Highlights.markArray(2, m);
			Delays.sleep(0.25);

			if (Reads.compareValues(val, array[m]) <= 0)
				b = m;
			else
				a = m+1;
		}
		return a;
	}
	private int rightBinSearch(int[] array, int a, int b, int val) {
		while (a < b) {
			int m = (a+b) >>> 1;
			Highlights.markArray(2, m);
			Delays.sleep(0.25);

			if (Reads.compareValues(val, array[m]) < 0)
				b = m;
			else
				a = m+1;
		}
		return a;
	}

	//@param pCmp - 0 for < piv, 1 for <= piv
	private boolean pivCmp(int v, int piv, int pCmp) {
		return Reads.compareValues(v, piv) < pCmp;
	}

	private void pivBufXor(int[] array, int pa, int pb, int v, int wLen) {
		while (wLen-- > 0) {
			if ((v&1) == 1) Writes.swap(array, pa+wLen, pb+wLen, 1, true, false);
			v >>= 1;
		}
	}
	//@param bit - < pivot means this bit
	private int pivBufGet(int[] array, int pa, int piv, int pCmp, int wLen, int bit) {
		int r = 0;

		while (wLen-- > 0) {
			r <<= 1;
			r |= (this.pivCmp(array[pa++], piv, pCmp) ? 0 : 1) ^ bit;
		}
		return r;
	}

	private void blockCycle(int[] array, int p, int n, int p1, int bLen, int wLen, int piv, int pCmp, int bit) {
		for (int i = 0; i < n; i++) {
			int dest = this.pivBufGet(array, p+i*bLen, piv, pCmp, wLen, bit);

			while (dest != i) {
				this.blockSwap(array, p+i*bLen, p+dest*bLen, bLen);
				dest = this.pivBufGet(array, p+i*bLen, piv, pCmp, wLen, bit);
			}
			this.pivBufXor(array, p+i*bLen, p1+i*bLen, i, wLen);
		}
		Highlights.clearMark(2);
	}

	private void blockSwap(int[] array, int a, int b, int s) {
		while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
	}

	private void rotate(int[] array, int a, int m, int b) {
		Highlights.clearAllMarks();
		IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
	}

	private void mergeFWExt(int[] array, int[] tmp, int a, int m, int b) {
		int s = m-a;

		Writes.arraycopy(array, a, tmp, 0, s, 1, true, true);

		int i = 0, j = m;

		while (i < s && j < b) {
			Highlights.markArray(2, j);

			if (Reads.compareValues(tmp[i], array[j]) <= 0)
				Writes.write(array, a++, tmp[i++], 1, true, false);
			else
				Writes.write(array, a++, array[j++], 1, true, false);
		}
		Highlights.clearAllMarks();

		while (i < s) Writes.write(array, a++, tmp[i++], 1, true, false);
	}
	private void mergeBWExt(int[] array, int[] tmp, int a, int m, int b) {
		int s = b-m;

		Writes.arraycopy(array, m, tmp, 0, s, 1, true, true);

		int i = s-1, j = m-1;

		while (i >= 0 && j >= a) {
			Highlights.markArray(2, j);

			if (Reads.compareValues(tmp[i], array[j]) >= 0)
				Writes.write(array, --b, tmp[i--], 1, true, false);
			else
				Writes.write(array, --b, array[j--], 1, true, false);
		}
		Highlights.clearAllMarks();

		while (i >= 0) Writes.write(array, --b, tmp[i--], 1, true, false);
	}

	private void blockMergeHelper(int[] array, int[] swap, int a, int m, int b, int p, int bLen, int piv, int pCmp, int bit) {
		if (m-a <= 2*bLen) {
			this.mergeFWExt(array, swap, a, m, b);
			return;
		}

		int bCnt = 0, wLen = this.log2((b-a)/bLen-3)+1;

		int i = a, j = m, k = 0;
		int l = 0, r = 0;

		int c = 0;

		while (c++ < 2*bLen) { //merge 2 blocks into buffer to create 2 buffers
			if (Reads.compareValues(array[i], array[j]) <= 0) {
				Writes.write(swap, k++, array[i++], 1, true, true);
				l++;
			}
			else {
				Writes.write(swap, k++, array[j++], 1, true, true);
				r++;
			}
		}

		int t = 0, pc = p;

		boolean left = l >= r;
		k = left ? i-l : j-r;

		c = 0;

		do {
			if (j == b || Reads.compareValues(array[i], array[j]) <= 0) {
				Writes.write(array, k++, array[i++], 1, true, false);
				l++;
			}
			else {
				Writes.write(array, k++, array[j++], 1, true, false);
				r++;
			}
			if (++c == bLen) { //change buffer after every block
				this.pivBufXor(array, k-bLen, pc, t++, wLen);
				pc += bLen;

				if (left) l -= bLen;
				else     r -= bLen;

				left = l >= r;
				k = left ? i-l : j-r;

				c = 0;
				bCnt++;
			}
		}
		while (i < m);

		int b1 = j-c;

		Writes.arraycopy(array, k-c, array, b1, c, 1, true, false); //swap remainder to end (r buffer)
		r -= c;
		l = Math.min(l, m-a-l);

		//l and r buffers are divisible by bLen

		Writes.arraycopy(array, a,   array, m-l,  l, 1, true, false); //swap l buffer to front
		Writes.arraycopy(array, a+l, array, b1-r, r, 1, true, false); //swap r buffer to front
		Writes.arraycopy(swap,  0, array, a, 2*bLen, 1, true, false); //swap first merged elements to correct position in front

		this.blockCycle(array, a+2*bLen, bCnt, p, bLen, wLen, piv, pCmp, bit);
	}
	private void blockMergeEasy(int[] array, int[] swap, int a, int m, int b, int p, int bLen, int piv, int pCmp, int bit) {
		if (b-m <= 2*bLen) {
			this.mergeBWExt(array, swap, a, m, b);
			return;
		}
		if (m-a <= 2*bLen) {
			this.mergeFWExt(array, swap, a, m, b);
			return;
		}

		int a1 = a+(m-a)%bLen;

		this.blockMergeHelper(array, swap, a1, m, b, p, bLen, piv, pCmp, bit);
		this.mergeFWExt(array, swap, a, a1, b);
	}

	private void blockMerge(int[] array, int[] swap, int a, int m, int b, int bLen) {
		int l = m-a, r = b-m;
		int lCnt = (l+r+1)/2;

		int med;

		//find lower ceil((A+B)/2) elements and then find max of halves to get median
		//binary search is used for O(log n) performance

		if (r < l) {
			if (r <= bLen) {
				this.mergeBWExt(array, swap, a, m, b);
				return;
			}
			int la = 0, lb = r;

			while (la < lb) {
				int lm = (la+lb) >>> 1;

				if (Reads.compareIndices(array, m+lm, a+(lCnt-lm)-1, 0.25, true) <= 0)
					la = lm+1;
				else
					lb = lm;
			}
			if (la == 0)
				med = array[a+lCnt-1];
			else
				med = Reads.compareIndices(array, m+la-1, a+(lCnt-la)-1, 0.25, true) > 0 ? array[m+la-1] : array[a+(lCnt-la)-1];
		}
		else {
			if (l <= bLen) {
				this.mergeFWExt(array, swap, a, m, b);
				return;
			}
			int la = 0, lb = l;

			while (la < lb) {
				int lm = (la+lb) >>> 1;

				if (Reads.compareIndices(array, a+lm, m+(lCnt-lm)-1, 0.25, true) < 0)
					la = lm+1;
				else
					lb = lm;
			}
			if (l == r && la == l)
				med = array[m-1];
			else if (la == 0)
				med = array[m+lCnt-1];
			else
				med = Reads.compareIndices(array, a+la-1, m+(lCnt-la)-1, 0.25, true) >= 0 ? array[a+la-1] : array[m+(lCnt-la)-1];
		}
		Highlights.clearMark(2);

		//stable ternary partition around median: [ < ][ = ][ > ]

		int m1 = this.leftBinSearch(array, a, m, med);
		int m2 = this.rightBinSearch(array, m, b, med);

		int ms2 = m-this.rightBinSearch(array, m1, m, med);
		int ms1 = this.leftBinSearch(array, m, m2, med)-m;

		this.rotate(array, m-ms2, m, m2);         //ABCABC -> ABABCC
		this.rotate(array, m1, m-ms2, m+ms1-ms2); //ABABCC -> AABBCC

		this.blockMergeEasy(array, swap, a, m1, m1+ms1, a+lCnt, bLen, med, 0, 0);
		this.blockMergeEasy(array, swap, m2-ms2, m2, b, a, bLen, med, 1, 1);
	}

	private void pureLogMergeSort(int[] array, int[] swap, int a, int b, int bLen) {
		int j = this.MIN_INSERT;

		for (int i = a; i < b; i += j)
			this.smallSort.customBinaryInsert(array, i, Math.min(b, i+j), 0.25);

		for (; j < b-a; j *= 2)
			for (int i = a; i+j < b; i += 2*j)
				this.blockMerge(array, swap, i, i+j, Math.min(b, i+2*j), bLen);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int bLen = Math.max(this.productLog(length), Math.min(bucketCount, length));
		int[] aux = Writes.createExternalArray(2*bLen);

		this.pureLogMergeSort(array, aux, 0, length, bLen);

		Writes.deleteExternalArray(aux);
	}
}