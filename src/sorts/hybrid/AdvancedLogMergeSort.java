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

public class AdvancedLogMergeSort extends Sort {
	public AdvancedLogMergeSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Advanced Log Merge");
		this.setRunAllSortsName("Advanced Log Merge Sort");
		this.setRunSortName("Advanced Log Mergesort");
		this.setCategory("Hybrid Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
		this.setQuestion("Set block size (default: calculates minimum block length for current length)", 1);
	}

	private int MIN_INSERT = 16;

	private BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);

	private class BitArray {
		private int[] array;
		private int pa, pb, w;

		public int size;

		public BitArray(int[] array, int pa, int pb, int size, int w) {
			this.array = array;
			this.pa = pa;
			this.pb = pb;
			this.size = size;
			this.w  = w;
		}

		private void flipBit(int a, int b) {
			Writes.swap(array, a, b, 0.5, true, false);
		}
		private boolean getBit(int a, int b) {
			return Reads.compareIndices(array, a, b, 0, false) > 0;
		}

		public void setXor(int idx, int val) {
			assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

			int s = idx*w, i1 = pa+s+w;
			for (int i = pa+s, j = pb+s; i < i1; i++, j++, val >>= 1)
				if ((val & 1) == 1) this.flipBit(i, j);

			if (val > 0) System.out.println("Warning: Word too large");
		}
		public int get(int idx) {
			assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

			int r = 0, s = idx*w;
			for (int k = 0, i = pa+s, j = pb+s; k < w; k++, i++, j++)
				r |= (this.getBit(i, j) ? 1 : 0) << k;
			return r;
		}
	}

	private int log2(int n) {
		return 31 - Integer.numberOfLeadingZeros(n);
	}

	private int calcBLen(int n) {
		int h = n/2, r = Math.min(64, h/2), c = h/r;
		while (c*(this.log2(c-1)+1)+c <= h/2) { r--; c = h/r; }
		return r+1;
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
		if (m-a <= bLen) {
			this.mergeFWExt(array, swap, a, m, b);
			return;
		}
		Writes.arraycopy(array, m-bLen, swap, 0, bLen, 1, true, true);

		int bCnt = 0, wLen = this.log2((b-a)/bLen-2)+1;

		int i = a, j = m, k = 0, pc = p;

		while (i < m-bLen && j+bLen-1 < b) {
			if (Reads.compareIndices(array, i+bLen-1, j+bLen-1, 0.5, true) <= 0) {
				this.pivBufXor(array, i, pc, k++, wLen);
				i += bLen;
			}
			else {
				this.pivBufXor(array, j, pc, (k++ << 1) | 1, wLen+1);
				j += bLen;
			}
			pc += bLen;
			bCnt++;
		}
		while (i < m-bLen) {
			this.pivBufXor(array, i, pc, k++, wLen);
			i += bLen;
			pc += bLen;
			bCnt++;
		}
		Highlights.clearMark(2);
		Writes.arraycopy(array, a, array, m-bLen, bLen, 1, true, false);

		int a1 = a+bLen;
		this.blockCycle(array, a1, bCnt, p, bLen, wLen, piv, pCmp, bit);

		int f = a1;
		boolean left = this.pivCmp(array[a1+wLen], piv, pCmp) ^ (bit != 0);

		if (!left) Writes.swap(array, a1+wLen, p+wLen, 1, true, false);

		for (k = 1, j = a; k < bCnt; k++) {
			int nxt = a1 + k*bLen;
			boolean frag = this.pivCmp(array[nxt+wLen], piv, pCmp) ^ (bit != 0);

			if (!frag) Writes.swap(array, nxt+wLen, p+(nxt+wLen-a1), 1, true, false);

			if (left ^ frag) {
				i = f; f = nxt;

				while (i < nxt) {
					int cmp = Reads.compareValues(array[i], array[f]);
					Highlights.markArray(2, f);

					if (cmp < 0 || (left && cmp == 0))
						Writes.write(array, j++, array[i++], 1, true, false);
					else
						Writes.write(array, j++, array[f++], 1, true, false);
				}
				left = !left;
			}
		}
		if (left) {
			k = a1 + bCnt*bLen;
			i = f; f = k;

			while (i < k && f < b) {
				Highlights.markArray(2, f);

				if (Reads.compareValues(array[i], array[f]) <= 0)
					Writes.write(array, j++, array[i++], 1, true, false);
				else
					Writes.write(array, j++, array[f++], 1, true, false);
			}
			Highlights.clearMark(2);

			if (f == b) {
				while (i < k) Writes.write(array, j++, array[i++], 1, true, false);
				Writes.arraycopy(swap, 0, array, b-bLen, bLen, 1, true, false);

				return;
			}
		}
		i = 0;

		while (i < bLen && f < b) {
			Highlights.markArray(2, f);

			if (Reads.compareValues(swap[i], array[f]) <= 0)
				Writes.write(array, j++, swap[i++], 1, true, false);
			else
				Writes.write(array, j++, array[f++], 1, true, false);
		}
		Highlights.clearMark(2);

		while (i < bLen)
			Writes.write(array, j++, swap[i++], 1, true, false);
	}
	private void blockMergeEasy(int[] array, int[] swap, int a, int m, int b, int p, int bLen, int piv, int pCmp, int bit) {
		if (b-m <= bLen) {
			this.mergeBWExt(array, swap, a, m, b);
			return;
		}
		if (m-a <= bLen) {
			this.mergeFWExt(array, swap, a, m, b);
			return;
		}

		int a1 = a+(m-a)%bLen;

		this.blockMergeHelper(array, swap, a1, m, b, p, bLen, piv, pCmp, bit);
		this.mergeFWExt(array, swap, a, a1, b);
	}

	private boolean blockMerge(int[] array, int[] swap, int a, int m, int b, int bLen) {
		int l = m-a, r = b-m;
		int lCnt = (l+r+1)/2;

		int med;

		//find lower ceil((A+B)/2) elements and then find max of halves to get median
		//binary search is used for O(log n) performance

		if (r < l) {
			if (r <= bLen) {
				this.mergeBWExt(array, swap, a, m, b);
				return false;
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
				return false;
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

		//stable ternary partition around median:
		//[  < med ][ == med ][  > med ][  < med ][ == med ][  > med ]
		//                    |-- ms2 -||-- ms1 -|
		//^a        ^m1                 ^m                  ^m2       ^b

		int m1 = this.leftBinSearch(array, a, m, med);
		int m2 = this.rightBinSearch(array, m, b, med);

		int ms2 = m-this.rightBinSearch(array, m1, m, med);
		int ms1 = this.leftBinSearch(array, m, m2, med)-m;

		this.rotate(array, m-ms2, m, m2);         //ABCABC -> ABABCC
		this.rotate(array, m1, m-ms2, m+ms1-ms2); //ABABCC -> AABBCC

		this.blockMergeEasy(array, swap, a, m1, m1+ms1, a+lCnt, bLen, med, 0, 0);
		this.blockMergeEasy(array, swap, m2-ms2, m2, b, a, bLen, med, 1, 1);

		return m2-m1-(ms2+ms1) <= lCnt; //if enough elements for bit buffer (or doing full merge) rotate and merge
	}

	private void blockMergeWithBufHelper(int[] array, int[] swap, int a, int m, int b, int pa, int pb, int bLen) {
		if (m-a <= bLen) {
			this.mergeFWExt(array, swap, a, m, b);
			return;
		}
		Writes.arraycopy(array, m-bLen, swap, 0, bLen, 1, true, true);

		int bCnt = 0, maxBCnt = (b-a)/bLen-1, wLen = this.log2(maxBCnt)+1; //ceil(log2(maxBCnt+1))

		BitArray pos  = new BitArray(array, pa, pb, maxBCnt, wLen);
		BitArray bits = new BitArray(array, pb-maxBCnt, pb+pb-pa-maxBCnt, maxBCnt, 1); //negative word size for backwards bit buffer

		int a1 = a+bLen, i = a, j = m, k;

		while (i < m-bLen && j+bLen-1 < b) {
			int posV;

			if (Reads.compareIndices(array, i+bLen-1, j+bLen-1, 0.5, true) <= 0) {
				posV = i == a ? (m-a1)/bLen-1 : (i-a1)/bLen;
				bits.setXor(bCnt, 1);
				i += bLen;
			}
			else {
				posV = (j-a1)/bLen;
				j += bLen;
			}
			if (bCnt != posV) pos.setXor(bCnt, posV+1);

			bCnt++;
		}
		while (i < m-bLen) {
			int posV = i == a ? (m-a1)/bLen-1 : (i-a1)/bLen;
			if (bCnt != posV) pos.setXor(bCnt, posV+1);
			bits.setXor(bCnt, 1);

			i += bLen;
			bCnt++;
		}
		Highlights.clearMark(2);
		Writes.arraycopy(array, a, array, m-bLen, bLen, 1, true, false);

		//backwards block cycle sort

		for (i = 0; i < bCnt; i++) {
			k = pos.get(i);

			if (k > 0) {
				Writes.arraycopy(array, a1 + i*bLen, array, a, bLen, 1, true, false);
				j = i;

				do {
					Writes.arraycopy(array, a1 + (k-1)*bLen, array, a1 + j*bLen, bLen, 1, true, false);
					pos.setXor(j, k);

					j = k-1;
					k = pos.get(j);
				}
				while (k != i+1);

				Writes.arraycopy(array, a, array, a1 + j*bLen, bLen, 1, true, false);
				pos.setXor(j, k);
			}
		}

		//merge blocks

		int f = a1;
		boolean left = bits.get(0) != 0;

		if (left) bits.setXor(0, 1);

		for (k = 1, j = a; k < bCnt; k++) {
			int nxt = a1 + k*bLen;
			boolean frag = bits.get(k) != 0;

			if (frag) bits.setXor(k, 1);

			if (left ^ frag) {
				i = f; f = nxt;

				while (i < nxt) {
					int cmp = Reads.compareValues(array[i], array[f]);
					Highlights.markArray(2, f);

					if (cmp < 0 || (left && cmp == 0))
						Writes.write(array, j++, array[i++], 1, true, false);
					else
						Writes.write(array, j++, array[f++], 1, true, false);
				}
				left = !left;
			}
		}
		if (left) {
			k = a1 + bCnt*bLen;
			i = f; f = k;

			while (i < k && f < b) {
				Highlights.markArray(2, f);

				if (Reads.compareValues(array[i], array[f]) <= 0)
					Writes.write(array, j++, array[i++], 1, true, false);
				else
					Writes.write(array, j++, array[f++], 1, true, false);
			}
			Highlights.clearMark(2);

			if (f == b) {
				while (i < k) Writes.write(array, j++, array[i++], 1, true, false);
				Writes.arraycopy(swap, 0, array, b-bLen, bLen, 1, true, false);

				return;
			}
		}
		i = 0;

		while (i < bLen && f < b) {
			Highlights.markArray(2, f);

			if (Reads.compareValues(swap[i], array[f]) <= 0)
				Writes.write(array, j++, swap[i++], 1, true, false);
			else
				Writes.write(array, j++, array[f++], 1, true, false);
		}
		Highlights.clearMark(2);

		while (i < bLen)
			Writes.write(array, j++, swap[i++], 1, true, false);
	}
	//precondition: pb+pb-pa-1 is within bounds
	private void blockMergeWithBuf(int[] array, int[] swap, int a, int m, int b, int pa, int pb, int bLen) {
		if (b-m <= bLen) {
			this.mergeBWExt(array, swap, a, m, b);
			return;
		}
		if (m-a <= bLen) {
			this.mergeFWExt(array, swap, a, m, b);
			return;
		}

		int a1 = a+(m-a)%bLen;

		this.blockMergeWithBufHelper(array, swap, a1, m, b, pa, pb, bLen);
		this.mergeFWExt(array, swap, a, a1, b);
	}

	private void pureLogMergeSort(int[] array, int[] swap, int a, int b, int bLen) {
		int j;
		for (j = b-a; (j+1)/2 >= this.MIN_INSERT; j = (j+1)/2);

		for (int i = a; i < b; i += j)
			this.smallSort.customBinaryInsert(array, i, Math.min(b, i+j), 0.25);

		for (; j < b-a; j *= 2) {
			int k;

			for (k = a; k+j < b && !this.blockMerge(array, swap, k, k+j, Math.min(b, k+2*j), bLen); k += 2*j);

			for (int i = k+2*j; i+j < b; i += 2*j)
				this.blockMergeWithBuf(array, swap, i, i+j, Math.min(b, i+2*j), k, k+j, bLen);
		}
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		if (length <= this.MIN_INSERT) {
			this.smallSort.customBinaryInsert(array, 0, length, 0.25);
			return;
		}
		int bLen = Math.max(this.calcBLen(length), Math.min(bucketCount, length));
		int[] aux = Writes.createExternalArray(bLen);

		this.pureLogMergeSort(array, aux, 0, length, bLen);

		Writes.deleteExternalArray(aux);
	}
}