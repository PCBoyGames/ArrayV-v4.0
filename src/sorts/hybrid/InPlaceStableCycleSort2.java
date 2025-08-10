package sorts.hybrid;

import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;
import main.ArrayVisualizer;

/*
 *
MIT License

Copyright (c) 2021-2024 aphitorite, edited by Haruki (a.k.a. Ayako-chan)

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

public class InPlaceStableCycleSort2 extends Sort {
	public InPlaceStableCycleSort2(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("In-Place Stable Cycle (IPSC + Log)");
		this.setRunAllSortsName("In-Place Stable Cycle Sort");
		this.setRunSortName("In-Place Stable Cyclesort");
		this.setCategory("Hybrid Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	//stable sorting algorithm performing a worst case of
	//O(n^2) comparisons and O(n) moves in O(1) space

	private long getRank(int[] array, int a, int b, int r) {
		int c = 0, ce = 0;

		for(int i = a; i < b; i++) {
			if(i == r) continue;

			Highlights.markArray(2, i);
			Delays.sleep(0.01);

			int cmp = Reads.compareValues(array[i], array[r]);

			c  += cmp == -1 ? 1 : 0;
			ce += cmp <=  0 ? 1 : 0;
		}
		Highlights.clearMark(2);

		return (long)ce << 32 | (long)c;
	}

	private int selectMedian(int[] array, int a, int b) {
		int med = (b-a)/2;
		int min = a, max = min;

		for(int i = a+1; i < b; i++) {
			if(Reads.compareIndices(array, i, min, 0.25, true) < 0)
				min = i;
			else if(Reads.compareIndices(array, i, max, 0.25, true) > 0)
				max = i;
		}
		//max or min might be the median
		long rank = this.getRank(array, a, b, min);
		int r = (int)rank, re = (int)(rank >> 32);

		if(med >= r && med <= re) return array[min];

		rank = this.getRank(array, a, b, max);
		r = (int)rank; re = (int)(rank >> 32);

		if(med >= r && med <= re) return array[max];

		for(int i = a;; i++) {
			Highlights.markArray(1, i);
			Delays.sleep(0.5);

			if(Reads.compareValues(array[i], array[min]) > 0
			   && Reads.compareValues(array[i], array[max]) < 0) {

				rank = this.getRank(array, a, b, i);
				r = (int)rank; re = (int)(rank >> 32);

				if(med >= r && med <= re)
					return array[i];

				else if(re < med)
					min = i;

				else max = i;
			}
		}
	}

	////////////////////
	//                //
	//  PARTITIONING  //
	//                //
	////////////////////

	private int log2(int n) {
		int r = 0;
		while((1 << r) < n) r++;
		return r;
	}
	private int productLog(int n) {
		int r = 1;
		while((r<<r)+r-1 < n) r++;
		return r;
	}

	private void blockSwap(int[] array, int a, int b, int s) {
		while(s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
	}
	private int blockRead(int[] array, int a, int piv, int wLen, int pCmp) {
		int r = 0, i = 0;

		while(wLen-- > 0)
			r |= (Reads.compareValues(array[a++], piv) < pCmp ? 1 : 0) << (i++);

		return r;
	}
	private void blockXor(int[] array, int a, int b, int v) { //special thanks to Distray
		while(v > 0) {
			if((v&1) > 0) Writes.swap(array, a, b, 1, true, false);
			v >>= 1; a++; b++;
		}
	}
	private int partition(int[] array, int[] swap, int a, int n, int bLen, int piv, int pCmp) {
		int p = a;
		int l = 0, r = 0, lb = 0, rb = 0;

		for(int i = 0; i < n; i++) {
			if(Reads.compareValues(array[a+i], piv) < pCmp)
				Writes.write(array, p+(l++), array[a+i], 0.5, true, false);
			else
				Writes.write(swap, r++, array[a+i], 0.5, true, true);

			if(l == bLen) {
				p += bLen;
				l = 0;
				lb++;
			}
			if(r == bLen) {
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

		if(min > 0) {
			int max = lb+rb-min;
			int wLen = this.log2(min);

			int j = a, k = a, v = 0;

			for(int i = min; i > 0; i--) {
				while(!(Reads.compareValues(array[j+wLen], piv) < pCmp)) j += bLen;
				while(  Reads.compareValues(array[k+wLen], piv) < pCmp ) k += bLen;
				this.blockXor(array, j, k, v++);
				j += bLen; k += bLen;
			}

			j = x ? p-bLen : a; k = j;
			int s = x ? -bLen : bLen;

			for(int i = max; i > 0; ) {
				if(x ^ (Reads.compareValues(array[k+wLen], piv) < pCmp)) {
					this.blockSwap(array, j, k, bLen);
					j += s;
					i--;
				}
				k += s;
			}

			j = 0;
			int ps = x ? a : m, pa = ps, pb = x ? m : a;
			int mask = ((x ? 1 : 0) << wLen) - (x ? 1 : 0);

			for(int i = min; i > 0; i--) {
				k = mask ^ this.blockRead(array, ps, piv, wLen, pCmp);

				while(j != k) {
					this.blockSwap(array, ps, pa + k*bLen, bLen);
					k = mask ^ this.blockRead(array, ps, piv, wLen, pCmp);
				}
				this.blockXor(array, ps, pb, j);
				j++; ps += bLen; pb += bLen;
			}
		}
		if(l > 0) {
			Highlights.clearMark(2);
			Writes.arraycopy(array, p, swap, 0, l, 0.5, true, true);
			Writes.arraycopy(array, m, array, m+l, rb*bLen, 0.5, true, false);
			Writes.arraycopy(swap, 0, array, m, l, 0.5, true, false);
		}
		return m+l;
	}

	private int[] ternaryPartition(int[] array, int a, int b, int piv) {
		int bLen = this.productLog(b-a);
		int[] swap = Writes.createExternalArray(bLen);

		int m2 = this.partition(array, swap, a, b-a, bLen, piv, 1);
		int m1 = this.partition(array, swap, a, m2-a, bLen, piv, 0);

		Writes.deleteExternalArray(swap);

		return new int[] { m1, m2 };
	}

	/////////////////////////
	//                     //
	//  STABLE CYCLE SORT  //
	//                     //
	/////////////////////////

	//@param cmp - comp val that means bit flipped
	//items equal to the median automatically count as bit flipped
	private int stableCycleDest(int[] array, int a, int a1, int b1, int b, int p, int piv, int cmp) {
		int d = a1, e = 0;

		for(int i = a1+1; i < b; i++) {
			Highlights.markArray(2, i);

			int pCmp = Reads.compareValues(array[i], piv);
			boolean bit = pCmp == cmp || pCmp == 0;

			int val  = bit ? array[p+i-a] : array[i];
			int vCmp = Reads.compareValues(val, array[a1]);

			if(vCmp == -1) d++;
			else if(i < b1 && !bit && vCmp == 0) e++;

			Highlights.markArray(3, d);
			Delays.sleep(0.01);
		}
		for(;;) {
			int pCmp = Reads.compareValues(array[d], piv);
			boolean bit = pCmp == cmp || pCmp == 0;

			if(!bit && e-- == 0) break;
			d++;

			Highlights.markArray(3, d);
			Delays.sleep(0.01);
		}

		return d;
	}
	private void stableCycle(int[] array, int a, int b, int p, int piv, int cmp) {
		for(int i = a; i < b; i++) {
			int pCmp = Reads.compareValues(array[i], piv);
			boolean bit = pCmp == cmp || pCmp == 0;

			if(!bit) {
				Highlights.markArray(1, i);
				int j = i;

				for(;;) {
					int k = this.stableCycleDest(array, a, i, j, b, p, piv, cmp);

					if(k == i) break;

					int t = array[i];
					Writes.write(array, i, array[k], 0.01, true, false);
					Writes.write(array, k, array[p+k-a], 0.01, true, false);
					Writes.write(array, p+k-a, t, 0.01, true, false);

					j = k;
				}
				Writes.swap(array, i, p+i-a, 0.02, true, false);
			}
		}
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int a = 0, b = length;

		if(length <= 32) {
			BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
			smallSort.customBinaryInsert(array, a, b, 0.25);
			return;
		}

		int piv = this.selectMedian(array, a, b);
		int[] t = this.ternaryPartition(array, a, b, piv);

		int m1 = t[0], m2 = t[1];

		int h1 = m1-a, h2 = b-m2, hMax = Math.max(h1,h2);
		int a1 = a+hMax, b1 = b-hMax;

		this.stableCycle(array, a, a+h1, b1, piv, 1);
		this.blockSwap(array, a+h1, b1+h1, hMax-h1);

		this.blockSwap(array, a, b1, hMax-h2);
		this.stableCycle(array, a1-h2, a1, b-h2, piv, -1);
	}
}