package sorts.hybrid;

import java.util.Random;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2021-2024 aphitorite

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

public final class PacheSort2 extends Sort {
	public PacheSort2(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Pache II");
		this.setRunAllSortsName("Pache Sort");
		this.setRunSortName("Pachesort");
		this.setCategory("Hybrid Sorts");
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	//simple average case O(n log n) comps O(n) moves sort

	private final int MIN_HEAP = 255;

	private Random rng;

	private int log2(int n) {
		return 31-Integer.numberOfLeadingZeros(n);
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

	private void blockSwap(int[] array, int a, int b, int s) {
		while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
	}

	private void mergeFW(int[] array, int a, int m, int b, int p) {
		int pLen = m-a, pEnd = p+pLen;
		this.blockSwap(array, a, p, pLen);

		while (p < pEnd && m < b) {
			if (Reads.compareValues(array[p], array[m]) <= 0)
				Writes.swap(array, a++, p++, 1, true, false);

			else Writes.swap(array, a++, m++, 1, true, false);
		}
		while (p < pEnd)
			Writes.swap(array, a++, p++, 1, true, false);
	}

	private class BitArray {
		private final int[] array;
		private final int pa, pb, w;

		public final int size, length;

		public BitArray(int[] array, int pa, int pb, int size, int w) {
			this.array = array;
			this.pa = pa;
			this.pb = pb;
			this.size = size;
			this.w  = w;
			this.length = size*w;
		}

		private void flipBit(int a, int b) {
			Writes.swap(array, a, b, 0.5, true, false);
		}
		private boolean getBit(int a, int b) {
			return Reads.compareIndices(array, a, b, 0, false) > 0;
		}
		private void setBit(int a, int b, boolean bit) {
			if (this.getBit(a, b) ^ bit)
				this.flipBit(a, b);
		}

		public void free() {
			int i1 = pa+length;
			for (int i = pa, j = pb; i < i1; i++, j++)
				this.setBit(i, j, false);
		}

		public void set(int idx, int uInt) {
			assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

			int s = idx*w, i1 = pa+s+w;
			for (int i = pa+s, j = pb+s; i < i1; i++, j++, uInt >>= 1)
				this.setBit(i, j, (uInt & 1) == 1);

			if (uInt > 0) System.out.println("Warning: Word too large");
		}
		public int get(int idx) {
			assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

			int r = 0, s = idx*w;
			for (int k = 0, i = pa+s, j = pb+s; k < w; k++, i++, j++)
				r |= (this.getBit(i, j) ? 1 : 0) << k;
			return r;
		}

		public void incr(int idx) {
			assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

			int s = idx*w, i1 = pa+s+w;
			for (int i = pa+s, j = pb+s; i < i1; i++, j++) {
				this.flipBit(i, j);
				if (this.getBit(i, j)) return;
			}
			System.out.println("Warning: Integer overflow");
		}
		public void decr(int idx) {
			assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

			int s = idx*w, i1 = pa+s+w;
			for (int i = pa+s, j = pb+s; i < i1; i++, j++) {
				this.flipBit(i, j);
				if (!this.getBit(i, j)) return;
			}
			System.out.println("Warning: Integer underflow");
		}
	}

	private void siftDown(int[] array, int pos, int len, int root, int t) {
		int curr = root;
		int cmp  = Integer.numberOfLeadingZeros(root+1) % 2 == 1 ? 1 : -1;

		int left = 2*curr + 1;

		while (left < len) {
			int next = left;
			int gChild = 2*left + 1;

			for (int node : new int[] {left+1, gChild, gChild+1, gChild+2, gChild+3} ) {
				if (node >= len) break;

				if (Reads.compareIndices(array, pos+node, pos+next, 0.25, true) == cmp)
					next = node;
			}
			Highlights.clearMark(2);

			if (next >= gChild) {
				if (Reads.compareIndexValue(array, pos+next, t, 0.25, true) == cmp) {
					Writes.write(array, pos+curr, array[pos+next], 0.75, true, false);

					curr = next;
					left = 2*curr + 1;

					int parent = (next-1) / 2;

					if (Reads.compareIndexValue(array, pos+parent, t, 0.25, true) == cmp) {
						Writes.write(array, pos+curr, t, 0.75, true, false);
						t = array[pos+parent];
						Writes.write(array, pos+parent, array[pos+curr], 0.75, true, false);
					}
				}
				else break;
			}
			else {
				if (Reads.compareIndexValue(array, pos+next, t, 0.25, true) == cmp) {
					Writes.write(array, pos+curr, array[pos+next], 0.75, true, false);
					curr = next;
				}
				break;
			}
		}
		Writes.write(array, pos+curr, t, 0.75, true, false);
	}
	private void heapify(int[] array, int pos, int len) {
		for (int i = (len-1)/2; i >= 0; i--)
			this.siftDown(array, pos, len, i, array[pos+i]);
	}

	private void minMaxHeap(int[] array, int a, int b) {
		int pos = a, len = b-a;

		this.heapify(array, pos, len);

		for (int i = len; i > 1; ) {
			int t = array[pos+(--i)];
			Highlights.markArray(3, pos+i);
			Writes.write(array, pos+i, array[pos], 1, true, false);
			this.siftDown(array, pos, i, 0, t);
		}
		Highlights.clearMark(3);
	}
	private void selectMinMax(int[] array, int a, int b, int s) {
		this.heapify(array, a, b-a);

		for (int i = 0; i < s; i++) {
			int t = array[--b];
			Highlights.markArray(3, b);
			Writes.write(array, b, array[a], 1, true, false);
			this.siftDown(array, a, b-a, 0, t);
		}
		Highlights.clearMark(3);

		for (int i = 0; i < s; i++) {
			int t = array[--b];
			int c = 1;

			if (Reads.compareIndices(array, a+c+1, a+c, 0.5, true) < 0) c++;
			Highlights.clearMark(2);
			Highlights.markArray(3, b);

			Writes.write(array, b, array[a+c], 1, true, false);
			this.siftDown(array, a, b-a, c, t);
		}
		Highlights.clearMark(3);
		int a1 = a+s;

		while (a1 > a) Writes.swap(array, --a1, b++, 1, true, false);
	}

	private void optiLazyHeap(int[] array, int a, int b, int s) {
		for (int j = a; j < b; j += s) {
			int max = j;

			for (int i = max+1; i < Math.min(j+s, b); i++)
				if (Reads.compareIndices(array, i, max, 0.125, true) > 0)
					max = i;

			Writes.swap(array, j, max, 1, true, false);
		}
		for (int j = b; j > a; ) {
			int k = a;

			for (int i = k+s; i < j; i += s)
				if (Reads.compareIndices(array, i, k, 0.125, true) > 0)
					k = i;

			int k1 = --j;

			for (int i = k+1; i < Math.min(k+s, j); i++)
				if (Reads.compareIndices(array, i, k1, 0.125, true) > 0)
					k1 = i;

			Highlights.markArray(3, j);

			if (k1 == j) {
				Writes.swap(array, k, j, 1, true, false);
			}
			else {
				Highlights.clearMark(2);

				int t = array[j];
				Writes.write(array, j, array[k], 0.5, true, false);
				Writes.write(array, k, array[k1], 0.5, true, false);
				Writes.write(array, k1, t, 0.5, true, false);
			}
		}
		Highlights.clearMark(3);
	}

	private void sortBucket(int[] array, int a, int b, int s, int val) {
		for (int i = b-1; i >= a; i--)
			if (Reads.compareIndexValue(array, i, val, 0.5, true) == 0)
				Writes.swap(array, i, --b, 0.5, true, false);

		this.optiLazyHeap(array, a, b, s);
	}

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int a = 0, b = length;

		if (length <= this.MIN_HEAP) {
			this.minMaxHeap(array, a, b);
			return;
		}

		this.rng = new Random();

		int log	= this.log2(length-1)+1;
		int pCnt   = length/(log*log);
		int bitLen = (pCnt+1)*log;

		int a1 = a+bitLen, b1 = b-bitLen;

		this.selectMinMax(array, a, b, bitLen);

		if (Reads.compareIndices(array, a1, b1-1, 1, true) < 0) {
			int a2 = a1;

			for (int i = 0; i < pCnt; i++)
				Writes.swap(array, a2, a2+this.rng.nextInt(b1-(a2++)), 1, true, false);

			this.minMaxHeap(array, a1, a2);

			BitArray cnts = new BitArray(array, a, b1, pCnt+1, log);

			for (int i = a2; i < b1; i++) {
				Highlights.markArray(3, i);
				cnts.incr(this.leftBinSearch(array, a1, a2, array[i])-a1);
			}
			Highlights.clearMark(3);

			for (int i = 1, sum = cnts.get(0); i < pCnt+1; i++) {
				sum += cnts.get(i);
				cnts.set(i, sum);
			}
			for (int i = 0, j = 0; i < pCnt; i++) {
				Highlights.markArray(3, a1+i);
				int cur = cnts.get(i);
				int loc = this.leftBinSearch(array, a1+i, a2, array[a2+j])-a1;

				while (j < cur) {
					if (loc == i) {
						j++;
						loc = this.leftBinSearch(array, a1+i, a2, array[a2+j])-a1;
					}
					else {
						cnts.decr(loc);
						int dest = cnts.get(loc);

						while (true) {
							int newLoc = this.leftBinSearch(array, a1+i, a2, array[a2+dest])-a1;

							if (newLoc != loc) {
								loc = newLoc;
								break;
							}
							cnts.decr(loc);
							dest--;
						}
						Writes.swap(array, a2+j, a2+dest, 1, true, false);
					}
				}
				j = this.rightBinSearch(array, a2+j, b1, array[a1+i])-a2;
			}
			cnts.free();
			Highlights.clearMark(3);

			int j = a2;

			for (int i = 0; i < pCnt; i++) {
				Highlights.markArray(3, a1+i);
				int j1 = this.rightBinSearch(array, j, b1, array[a1+i]);
				this.sortBucket(array, j, j1, log, array[a1+i]);
				j = j1;
			}
			this.optiLazyHeap(array, j, b1, log);
			this.mergeFW(array, a1, a2, b1, a);
			this.minMaxHeap(array, a, a+pCnt);
		}
	}
}