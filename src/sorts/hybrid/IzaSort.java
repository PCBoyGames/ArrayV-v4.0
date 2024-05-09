package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;

/*
 *
MIT License

Copyright (c) 2021-2022 aphitorite

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

final public class IzaSort extends Sort {
	public IzaSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Iza");
		this.setRunAllSortsName("Iza Sort");
		this.setRunSortName("Izasort");
		this.setCategory("Hybrid Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}

	/**
		futuristic sorting algorithm:

		unstable sort that sorts in O(n log n) comps and O(n) moves O(1) space worst case
		this sort is deterministic unlike other in-place O(n log n) comps O(n) move sorts

		for a long time a deterministic O(n log n) comps O(n) moves sort remained
		unsolved until franceschini's method in 2007

		although this sort is a simplified unstable version of aphitorite's method
		it remains a very complicated sorting algorithm

		@author aphitorite
	*/

	private final int MIN_INSERT = 32;
	private final int MIN_HEAP   = 4095;

	private BinaryInsertionSort smallSort;

	private int log2(int n) {
		return 31-Integer.numberOfLeadingZeros(n);
	}

	private void blockSwap(int[] array, int a, int b, int s) {
		while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
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
	private int rightBinSearch(int[] array, int a, int b, int val, boolean bw) {
		int cmp = bw ? 1 : -1;

		while (a < b) {
			int m = (a+b) >>> 1;
			Highlights.markArray(3, m);
			Delays.sleep(0.25);

			if (Reads.compareValues(val, array[m]) == cmp)
				b = m;
			else
				a = m+1;
		}
		Highlights.clearMark(3);
		return a;
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

	private void siftDown(int[] array, int val, int i, int p, int n) {
		while (4*i+1 < n) {
			int max = val;
			int next = i, child = 4*i+1;

			for (int j = child; j < Math.min(child+4, n); j++) {
				if (Reads.compareValues(array[p+j], max) > 0) {
					max  = array[p+j];
					next = j;
				}
			}
			if (next == i) break;

			Writes.write(array, p+i, max, 0.25, true, false);
			i = next;
		}
		Writes.write(array, p+i, val, 0.25, true, false);
	}
	private void optiHeapSort(int[] array, int a, int b) {
		int n = b-a;

		for (int i = (n-1)/4; i >= 0; i--)
			this.siftDown(array, array[a+i], i, a, n);

		for (int i = n-1; i > 0; i--) {
			Highlights.markArray(2, a+i);
			int t = array[a+i];
			Writes.write(array, a+i, array[a], 1, false, false);
			this.siftDown(array, t, 0, a, i);
		}
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

	/////////////////
	//             //
	//  GAVL TREE  //
	//             //
	/////////////////

	//@return - tree buffer space needed to contain k elements
	private int treeBufferSpace(int k) {
		int log = this.log2(k+1);
		return (1 << log) * (log + 2) - 1;
	}

	private final class GAVLTree {
		private final int[] array;
		private final int o, p, pa, pb;

		private BitArray gSize;
		private int g, size, kSize;

		//implementation of GAVL requires extra buffer space separate from the tree's buffer space for simplicity
		//its possible to perform all operations within the same buffer space as the tree

		//O(b log b) buffer space O(log^2 b) insert time and O(log b) search time

		public GAVLTree(int[] array, int o, int p, int pa, int pb) {
			this.array = array;

			this.o  = o;  //output position
			this.p  = p;  //tree position
			this.pa = pa; //bit buffer positions
			this.pb = pb;

			this.g	 = 2; //gap size
			this.size  = 0; //count of how many elements in gaps
			this.kSize = 1; //how many gapped keys (always in the form 2^k-1)
			this.gSize = new BitArray(array, pa, pb, this.kSize+1, log2(g)+1);

			Writes.swap(array, this.keyPos(0), o, 1, true, false); //swap elements to key positions
		}

		private int gapPos(int idx) {
			return this.p+idx*(this.g+1);
		}
		private int keyPos(int idx) {
			return this.gapPos(idx+1)-1;
		}

		private void exchange(int a, int b) { //same function is used to swap in and out tree elements
			int po = this.o;

			for (int i = a;; i++) {
				int s = this.gSize.get(i);
				int j = this.gapPos(i);

				while (s-- > 0) Writes.swap(array, po++, j++, 1, true, false);

				if (i == b-1) return;

				Writes.swap(array, po++, this.keyPos(i), 1, true, false);
			}
		}
		private void increaseSize() { //precondition: this.size == this.kSize+1
			this.exchange(0, this.kSize+1);
			this.gSize.free();

			this.g++;
			this.kSize += this.size;
			this.size  = 0;
			this.gSize = new BitArray(array, this.pa, this.pb, this.kSize+1, log2(this.g)+1);

			this.exchange(0, this.kSize+1);
		}
		private int keySearch(int val) { //right binary search
			int a = 0, b = this.kSize;

			while (a < b) {
				int m = a+(b-a)/2;
				Highlights.markArray(2, this.keyPos(m));
				Delays.sleep(0.25);

				if (Reads.compareValues(val, array[this.keyPos(m)]) < 0)
					b = m;
				else
					a = m+1;
			}
			return a; //index of keys[] / gSize
		}
		private int[] gapSearch(int idx, int val) { //returns: position index in gap, tail index of gap before insertion
			int gPos  = this.gapPos(idx);
			int gTail = gPos+this.gSize.get(idx);

			return new int[] {rightBinSearch(array, gPos, gTail, val, false)-gPos, gTail-gPos};
		}

		//rebalancing

		private int minGap(int a, int b) {
			int min = this.gSize.get(a);

			for (int i = a+1; i < b; i++) {
				int minI = this.gSize.get(i);
				if (minI < min) min = minI;
			}
			return min;
		}
		private void incrRange(int a, int b) {
			for (int i = a; i < b; i++) this.gSize.incr(i);
		}
		private void decrRange(int a, int b) {
			for (int i = a; i < b; i++) this.gSize.decr(i);
		}
		private void rebalance(int idx) {
			for (int lvl = 0, curPos = idx; lvl < this.g-2; lvl++) {
				int s = 1 << lvl;
				int sibPos = curPos ^ s;
				int minPos = Math.min(curPos, sibPos);

				int curMin = this.minGap(curPos, curPos+s);
				int sibMin = this.minGap(sibPos, sibPos+s);

				if (curMin >= sibMin) {
					int diff = curMin-sibMin;

					if (diff == 1) return;

					if (diff > 1) {
						this.exchange(minPos, minPos+s*2);
						this.incrRange(sibPos, sibPos+s);
						this.decrRange(curPos, curPos+s);
						this.exchange(minPos, minPos+s*2);
					}
				}
				else {
					int diff = sibMin-curMin;

					if (diff == 1) return;

					if (diff > 1) {
						this.exchange(minPos, minPos+s*2);
						this.incrRange(curPos, curPos+s);
						this.decrRange(sibPos, sibPos+s);
						this.exchange(minPos, minPos+s*2);
					}
				}
				curPos = minPos;
			}
		}

		/**
			@param idx   - index of array element
			       loc   - index of gap
				   gPos  - location among gap elements
				   gTail - total gap elements
		*/
		public void insert(int idx, int loc, int gPos, int gTail) {
			int pos = this.gapPos(loc);
			int t = array[idx];
			Writes.write(array, idx, array[pos+gTail], 1, true, false);
			Writes.arraycopy(array, pos+gPos, array, pos+gPos+1, gTail-gPos, 0.5, true, false);
			Writes.write(array, pos+gPos, t, 1, true, false);

			this.gSize.incr(loc);
			this.size++;

			this.rebalance(loc); //rebalance after insertion

			if (this.size == this.kSize+1) this.increaseSize(); //resize if size limit is reached
		}

		public void free() {
			this.exchange(0, this.kSize+1);
			this.gSize.free();
		}

		//KEY COLLECTION EXCLUSIVE

		/**
			@param  idx   - index of element in array

			@return true  - if it successfully inserted the element
			        false - if the tree already contains the element (no insertion)
		*/
		public boolean insertDistinct(int idx) { //key collection exclusive
			if (this.size == this.kSize+1) this.increaseSize();

			int val = array[idx];
			int loc = this.keySearch(val);

			if (loc > 0 && Reads.compareValues(val, array[this.keyPos(loc-1)]) == 0)
				return false;

			int[] t  = this.gapSearch(loc, val);
			int gPos = t[0], gTail = t[1];

			if (gPos > 0 && Reads.compareValues(val, array[this.gapPos(loc)+gPos-1]) == 0)
				return false;

			//we found key:

			this.insert(idx, loc, gPos, gTail);

			return true;
		}

		//GRID SORT EXCLUSIVE

		/**
			@return int[0]  - exact position of searched key
			        int[1:] - insert information for key one position higher in tree than found key
		*/
		public int[] gridSearch(int val) {
			int kp;
			int loc = this.keySearch(val);

			int gPos = this.gapPos(loc), gTail = gPos+this.gSize.get(loc);
			int gLoc = rightBinSearch(array, gPos, gTail, val, false);

			if (gLoc == gTail) {
				if (loc == this.kSize)
					kp = gLoc-1;
				else {
					kp = this.keyPos(loc);

					loc++;
					gPos  += this.g+1;
					gLoc  =  gPos;
					gTail =  gPos+this.gSize.get(loc);
				}
			}
			else {
				kp = gLoc;
				gLoc++;
			}
			return new int[] {kp-this.p, loc, gLoc-gPos, gTail-gPos};
		}

		/*public void print() {
			for (int i = 0; i < this.kSize; i++) {
				int gPos = this.gapPos(i);
				System.out.print(Arrays.toString(Arrays.copyOfRange(array, gPos, gPos+this.gSize.get(i))));
				System.out.printf("{%d}", array[this.keyPos(i)]);
			}
			int gPos = this.gapPos(this.kSize);
			System.out.println(Arrays.toString(Arrays.copyOfRange(array, gPos, gPos+this.gSize.get(this.kSize))));
		}*/
	}

	//////////////////////////////////////
	//                                  //
	//  PIVOT SELECTION + QUICK SELECT  //
	//                                  //
	//////////////////////////////////////

	private int medianOfThree(int[] array, int a, int m, int b) {
		if (Reads.compareValues(array[m], array[a]) > 0) {
			if (Reads.compareValues(array[m], array[b]) < 0)
				return m;
			if (Reads.compareValues(array[a], array[b]) > 0)
				return a;
			else
				return b;
		}
		if (Reads.compareValues(array[m], array[b]) > 0)
			return m;
		if (Reads.compareValues(array[a], array[b]) < 0)
			return a;

		return b;
	}
	private int ninther(int[] array, int a, int b) {
		int s = (b-a)/8;

		int a1 = this.medianOfThree(array, a,     a+  s, a+2*s);
		int m1 = this.medianOfThree(array, a+3*s, a+4*s, a+5*s);
		int b1 = this.medianOfThree(array, a+6*s, a+7*s, b-1);

		return this.medianOfThree(array, a1, m1, b1);
	}
	private void pivotSelect(int[] array, int a, int b) {
		if (b-a <= 256)
			Writes.swap(array, a, this.ninther(array, a, b), 1, true, false);

		else {
			int s = (b-a)/3;

			int a1 = this.ninther(array, a,     a+  s);
			int m1 = this.ninther(array, a+  s, a+2*s);
			int b1 = this.ninther(array, a+2*s, b);

			Writes.swap(array, a, this.medianOfThree(array, a1, m1, b1), 1, true, false);
		}
	}
	private void medianOfMedians(int[] array, int a, int b) {
		while (b-a > 2) {
			int m = a, i = a;

			for (; i+2 < b; i += 3)
				Writes.swap(array, m++, this.medianOfThree(array, i, i+1, i+2), 1, true, false);
			while (i < b)
				Writes.swap(array, m++, i++, 1, true, false);

			b = m;
		}
	}

	private int partition(int[] array, int a, int b) {
		int i = a, j = b;
		Highlights.markArray(3, a);

		do {
			do {
				i++;
				Highlights.markArray(1, i);
				Delays.sleep(0.5);
			}
			while (i < j && Reads.compareValues(array[i], array[a]) < 0);

			do {
				j--;
				Highlights.markArray(2, j);
				Delays.sleep(0.5);
			}
			while (j >= i && Reads.compareValues(array[j], array[a]) > 0);

			if (i < j) Writes.swap(array, i, j, 1, false, false);
			else {
				Highlights.clearMark(3);
				Writes.swap(array, a, j, 1, true, false);
				return j;
			}
		}
		while (true);
	}
	private void dualQuickSelect(int[] array, int a, int b, int r1, int r2) {
		int a1 = a, b1 = b;
		boolean badPartition = false;

		while (b-a > this.MIN_INSERT) {
			if (badPartition) {
				this.medianOfMedians(array, a, b);
				badPartition = false;
			}
			else this.pivotSelect(array, a, b);

			int m = this.partition(array, a, b);

			if (m > r2 && m < b1)        b1 = m;
			else if (m < r2 && m+1 > a1) a1 = m+1;
			else if (m == r2)            a1 = b1;

			if (m == r1) break;

			int left = m-a, right = b-m-1;
			badPartition = 16*Math.min(left, right) < Math.max(left, right);

			if (m > r1) b = m;
			else       a = m+1;
		}
		if (b-a <= this.MIN_INSERT)
			this.smallSort.customBinaryInsert(array, a, b, 0.25);

		while (b1-a1 > this.MIN_INSERT) {
			if (badPartition) {
				this.medianOfMedians(array, a1, b1);
				badPartition = false;
			}
			else this.pivotSelect(array, a1, b1);

			int m = this.partition(array, a1, b1);

			if (m == r2) return;

			int left = m-a1, right = b1-m-1;
			badPartition = 16*Math.min(left, right) < Math.max(left, right);

			if (m > r2) b1 = m;
			else       a1 = m+1;
		}
		if (b1-a1 <= this.MIN_INSERT)
			this.smallSort.customBinaryInsert(array, a1, b1, 0.25);
	}

	/////////////////
	//             //
	//  GRID SORT  //
	//             //
	/////////////////

	private void unshuffleWithBuf(int[] array, int a, int b) { //precondition: b-a is even
		int i = a, j = b;

		for (int k = a+1; k < b; k += 2) {
			Writes.swap(array, i++, k-1, 1, true, false);
			Writes.swap(array, j++, k,   1, true, false);
		}
		this.blockSwap(array, i, b, j-b);
	}
	private void reshuffleWithBuf(int[] array, int a, int m, int b) {
		int i = b, j = m;

		this.blockSwap(array, a, b, m-a);

		for (int k = a+1; k < b; k += 2) {
			Writes.swap(array, i++, k-1, 1, true, false);
			Writes.swap(array, j++, k,   1, true, false);
		}
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

	private void fancySplitBucket(int[] array, int a, int b, int p, int s) {
		int n = b-a, m = (a+b) >>> 1;

		this.optiLazyHeap(array, m, b, s);

		int i = m-1, j = b-1;
		int c = n/2;

		while (c-- > 0) {
			if (Reads.compareValues(array[i], array[j]) > 0)
				Writes.swap(array, --p, i--, 1, true, false);
			else
				Writes.swap(array, --p, j--, 1, true, false);
		}
		int m1 = m;

		while (i >= a && j >= m) {
			if (Reads.compareValues(array[i], array[j]) > 0)
				Writes.swap(array, --m1, i--, 1, true, false);
			else
				Writes.swap(array, --m1, j--, 1, true, false);
		}
		while (j >= m) Writes.swap(array, --m1, j--, 1, true, false);
	}

	private void gridSort(int[] array, int a, int b, int ia, int im, int pk, int t1, int t2, int pa1, int pb1, int pa2, int pb2, int p, int bLen, int log, int bsv, boolean bw) {
		int bLen2X = 2*bLen;

		if (b-a <= bLen2X) {
			this.optiHeapSort(array, a, b);
			return;
		}

		this.optiLazyHeap(array, a, a+bLen2X, 3*log/2); //sort the first two blocks

		this.blockSwap(array, a, p, bLen);              //swap the first two blocks to buffer
		this.blockSwap(array, a+bLen, p+bLen2X, bLen);

		//initialize trees

		Writes.swap(array, p+bLen-1, pk, 1, true, false);

		GAVLTree keyBuf = new GAVLTree(array, pk, t1, pa1, pb1);
		GAVLTree idxBuf = new GAVLTree(array, ia, t2, pa2, pb2);

		keyBuf.insert(p+bLen-1+bLen2X, 1, 0, 0);
		idxBuf.insert(ia+1, 1, 0, 0);

		int kSize = 2;

		for (int i = a+bLen2X; i < b; i++) {
			int[] tmp = keyBuf.gridSearch(array[i]); //search tree, find index, decode index
			int loc  = tmp[0];
			int idx  = this.leftBinSearch(array, im, im+kSize, array[t2+loc])-im;

			int bPos = p+idx*bLen2X+bLen;
			int gPos = this.rightBinSearch(array, bPos, bPos+bLen, bsv, bw)-bPos;

			Writes.swap(array, i, bPos+gPos, 1, true, false); //swap sorting element to bucket

			if (gPos == bLen-1) { //if after inserting element and gap is full, split

				Writes.swap(array, bPos-1, t1+loc, 1, true, false); //swap key back to bucket block

				int bPosNew = p+kSize*bLen2X+bLen;
				this.fancySplitBucket(array, bPos-bLen, bPos+bLen, bPosNew, log); //sort bucket and merge with block

				Writes.swap(array, bPos-1, t1+loc, 1, true, false); //swap key back to tree

				//insert the new block's tail into the tree along with the new index

				keyBuf.insert(bPosNew-1,    tmp[1], tmp[2], tmp[3]);
				idxBuf.insert(ia+(kSize++), tmp[1], tmp[2], tmp[3]);
			}
		}
		keyBuf.free();
		idxBuf.free();

		for (int i = 0, j = a; i < kSize; i++) { //retrieve elements in sorted order

			int idx  = this.leftBinSearch(array, im, im+kSize, array[ia+i])-im;
			int bPos = p+idx*bLen2X+bLen;
			int gPos = this.rightBinSearch(array, bPos, bPos+bLen, bsv, bw)-bPos; //very rarely bPos+bLen can go out of bounds by 1 but it will never matter

			Writes.swap(array, bPos-1, pk+i, 1, true, false);
			this.optiLazyHeap(array, bPos, bPos+gPos, log);
			this.mergeTo(array, bPos-bLen, bPos, bPos+gPos, j);
			j += bLen+gPos;
		}

		//sort index buffer

		for (int i = 0; i < kSize-1; i++) {
			int idx = this.leftBinSearch(array, im+i, im+kSize, array[ia+i])-im;

			while (idx != i) {
				Writes.swap(array, ia+i, ia+idx, 1, true, false);
				idx = this.leftBinSearch(array, im+i, im+kSize, array[ia+i])-im;
			}
		}
	}

	////////////////
	//            //
	//  IZA SORT  //
	//            //
	////////////////

	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int a = 0, b = length;
		this.smallSort = new BinaryInsertionSort(this.arrayVisualizer);

		if (length <= this.MIN_INSERT) {
			this.smallSort.customBinaryInsert(array, a, b, 0.25);
			return;
		}

		if (length <= this.MIN_HEAP) {
			this.optiHeapSort(array, a, b);
			return;
		}

		int log  = this.log2(length-1)+1;
		int bLen = log*log;
		int keys = length/bLen;

		int bufLen  = 2*keys,
			treeLen = this.treeBufferSpace(bufLen),
			bitLen  = (bufLen-1)*log;

		int a1 = a+bitLen, b1 = b-bitLen, a2 = a1+bufLen, a3 = a2+treeLen;

		this.dualQuickSelect(array, a, b, a3, b1-1);
		this.optiHeapSort(array, b1, b);

		//[ bit buffer ][ buf ][ tree buffer ][            array             ][ bit buffer ]
		//a             a1     a2             a3                              b1            b

		if (Reads.compareIndices(array, a3, b1-1, 1, true) < 0) {
			Writes.swap(array, a1, a3, 1, true, false);
			GAVLTree tree = new GAVLTree(array, a1, a2, a, b1);

			int c = 1;
			for (int i = a3+1, j = a3+1; i < b1 && c < bufLen; i++) {
				Highlights.markArray(1, i);
				Delays.sleep(0.5);

				if (tree.insertDistinct(i)) {
					Writes.swap(array, j++, i, 1, true, false);
					c++;
				}
			}
			tree.free();
			a2 = a1+c; a3 += c;

			if (c < 2) {}

			else if (c < bufLen) {

				//[ bit buffer ][ keys ][ tree buffer ][            array             ][ bit buffer ]
				//a             a1      a2             a3                              b1            b

				BitArray cnts = new BitArray(array, a, b1, c, log);

				//perform bucket sort for every unique key

				for (int i = a3; i < b1; i++) { //count elements
					Highlights.markArray(1, i);
					Delays.sleep(0.5);

					int loc = this.leftBinSearch(array, a1, a2, array[i])-a1;
					cnts.incr(loc);
				}
				for (int i = 1, sum = cnts.get(0); i < c; i++) { //prefix sum
					sum += cnts.get(i);
					cnts.set(i, sum);
				}
				for (int i = 0, j = 0; i < c-1; i++) { //transport elements
					Highlights.markArray(3, a3+j);

					int cur = cnts.get(i);
					int loc = this.leftBinSearch(array, a1+i, a2, array[a3+j])-a1;

					while (j < cur) {
						if (loc == i) {
							j++;
							loc = this.leftBinSearch(array, a1+i, a2, array[a3+j])-a1;
						}
						else {
							cnts.decr(loc);
							int dest = cnts.get(loc);

							while (true) {
								int newLoc = this.leftBinSearch(array, a1+i, a2, array[a3+dest])-a1;

								if (newLoc != loc) {
									loc = newLoc;
									break;
								}
								cnts.decr(loc);
								dest--;
							}
							Writes.swap(array, a3+j, a3+dest, 1, true, false);
						}
					}
					j = this.rightBinSearch(array, a3+j, b1, array[a1+i], false)-a3;
				}
				cnts.free();
			}
			else {
				this.unshuffleWithBuf(array, a1, a2);
				int a4 = (a+a1) >>> 1, a5 = (a1+a2) >>> 1, t1 = a2+keys, t2 = (a3+t1) >>> 1, b2 = (b1+b) >>> 1;

				//[ bit buffer 1 ][ bit buffer 2 ][ idx buf ][ idx buf ][ buf ][ tree buffer 1 ][ tree buffer 2 ][            array             ][ bit buffer 1 ][ bit buffer 2 ]
				//a               a4              a1         a5         a2     t1               t2               a3, a6                          b1, b3          b2              b

				int a6 = a3, b3 = b1;

				sortLoop:
				while (b3-a6 > 8*bLen) {

					//ternary partition

					this.pivotSelect(array, a6, b3);

					int piv = array[a6];

					int i1 = a6+1, i = a6, j = b3, j1 = b3;

					while (true) {
						while (++i < j) {
							int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
							if (cmp == 0) Writes.swap(array, i1++, i, 1, true, false);
							else if (cmp > 0) break;
						}
						Highlights.clearMark(2);

						while (--j > i) {
							int cmp = Reads.compareIndexValue(array, j, piv, 0.5, true);
							if (cmp == 0) Writes.swap(array, --j1, j, 1, true, false);
							else if (cmp < 0) break;
						}
						Highlights.clearMark(2);

						if (i < j) {
							Writes.swap(array, i, j, 1, true, false);
							Highlights.clearMark(2);
						}
						else {
							if (i1 == b3) {
								a6 = b3;
								break sortLoop;
							}
							else if (j < i) j++;

							while (i1 > a6) Writes.swap(array, --i, --i1, 1, true, false);
							while (j1 < b3) Writes.swap(array, j++, j1++, 1, true, false);

							break;
						}
					}

					//tail recurse

					int left = i-a6, right = b3-j;

					if (left <= right) { //sort the smaller partition using larger partition as space
						int ma = a6, mb = i;

						while (mb-ma > right/2) {
							this.pivotSelect(array, ma, mb);
							int m = this.partition(array, ma, mb);

							if (mb-(m+1) < m-ma) {
								this.gridSort(array, m+1, mb, a1, a5, a2, t1, t2, a, b1, a4, b2, j, bLen, log, piv, false);
								mb = m;
							}
							else {
								this.gridSort(array, ma, m, a1, a5, a2, t1, t2, a, b1, a4, b2, j, bLen, log, piv, false);
								ma = m+1;
							}
						}
						this.gridSort(array, ma, mb, a1, a5, a2, t1, t2, a, b1, a4, b2, j, bLen, log, piv, false);

						a6 = j;
					}
					else {
						int ma = j, mb = b3;

						while (mb-ma > left/2) {
							this.pivotSelect(array, ma, mb);
							int m = this.partition(array, ma, mb);

							if (mb-(m+1) < m-ma) {
								this.gridSort(array, m+1, mb, a1, a5, a2, t1, t2, a, b1, a4, b2, a6, bLen, log, piv, true);
								mb = m;
							}
							else {
								this.gridSort(array, ma, m, a1, a5, a2, t1, t2, a, b1, a4, b2, a6, bLen, log, piv, true);
								ma = m+1;
							}
						}
						this.gridSort(array, ma, mb, a1, a5, a2, t1, t2, a, b1, a4, b2, a6, bLen, log, piv, true);

						b3 = i;
					}
				}
				this.optiHeapSort(array, a6, b3);

				//reshuffle the index buffer

				this.reshuffleWithBuf(array, a1, a5, a2);
			}

			//redistribute keys

			a3 -= c;
			int i = a3, j = a3+c;

			while (a1 < a2 && j < b1) {
				if (Reads.compareValues(array[a1], array[j]) <= 0)
					Writes.swap(array, i++, a1++, 1, true, false);
				else
					Writes.swap(array, i++, j++, 1, true, false);
			}
			while (a1 < a2) Writes.swap(array, i++, a1++, 1, true, false);
		}

		//sort lower bit buffer + filler zone (heap sorts = 15n writes)

		this.optiHeapSort(array, a, a3);
	}
}