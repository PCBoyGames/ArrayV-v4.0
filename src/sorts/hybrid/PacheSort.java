package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;
import sorts.select.MaxHeapSort;
import java.util.Random;

/*
 * 
MIT License

Copyright (c) 2021 aphitorite

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

final public class PacheSort extends Sort {
	public PacheSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
		this.setSortListName("Pache");
		this.setRunAllSortsName("Pache Sort");
		this.setRunSortName("Pachesort");
		this.setCategory("Hybrid Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}
	
	private final int MIN_INSERT = 16;
	
	private Random rng;
	private BinaryInsertionSort smallSort;
	
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
			Writes.swap(array, a, b, 0.25, true, false);
		}
		private boolean getBit(int a, int b) {
			return Reads.compareIndices(array, a, b, 0, false) > 0;
		}
		private void setBit(int a, int b, boolean bit) {
			if(this.getBit(a, b) ^ bit)
				this.flipBit(a, b);
		}
		
		public void free() {
			int i1 = pa+length;
			for(int i = pa, j = pb; i < i1; i++, j++)
				this.setBit(i, j, false);
		}
		
	    public void set(int idx, int uInt) {
			assert (idx >= 0 && idx < size) : "BitArray index out of bounds";
			
			int s = idx*w, i1 = pa+s+w;
			for(int i = pa+s, j = pb+s; i < i1; i++, j++, uInt >>= 1)
				this.setBit(i, j, (uInt & 1) == 1);
			
			if(uInt > 0) System.out.printf("Warning: Word too large at index %d\n", idx);
		}
		public int get(int idx) {
			assert (idx >= 0 && idx < size) : "BitArray index out of bounds";
			
			int r = 0, s = idx*w;
			for(int k = 0, i = pa+s, j = pb+s; k < w; k++, i++, j++)
				r |= (this.getBit(i, j) ? 1 : 0) << k;
			return r;
		}
		
		public void incr(int idx) {
			assert (idx >= 0 && idx < size) : "BitArray index out of bounds";
			
			int s = idx*w, i1 = pa+s+w;
			for(int i = pa+s, j = pb+s; i < i1; i++, j++) {
				this.flipBit(i, j);
				if(this.getBit(i, j)) return;
			}
			System.out.printf("Warning: Integer overflow at index %d\n", idx);
		}
		public void decr(int idx) {
			assert (idx >= 0 && idx < size) : "BitArray index out of bounds";
			
			int s = idx*w, i1 = pa+s+w;
			for(int i = pa+s, j = pb+s; i < i1; i++, j++) {
				this.flipBit(i, j);
				if(!this.getBit(i, j)) return;
			}
			System.out.printf("Warning: Integer underflow at index %d\n", idx);
		}
	}
	
	//bit buffer creation
	
	private void randomSqrtMedian(int[] array, int a, int b) { //swap random sqrt n sample and find its median
		int len = b-a, s = (int)Math.sqrt(len);
		s -= 1-s%2;
		
		for(int i = 0; i < s; i++) {
			int rand = rng.nextInt(len-i);
			Writes.swap(array, a+i, a+i+rand, 1, true, false);
		}
		for(int i = 0;; i++) {
			int c = 0, ce = 0;
			
			for(int j = 0; j < s; j++) {
				if(j == i) continue;
				
				int cmp = Reads.compareIndices(array, a+j, a+i, 0.25, true);
				
				c  += cmp == -1 ? 1 : 0;
				ce += cmp <=  0 ? 1 : 0;
			}
			if(s/2 >= c && s/2 <= ce) {
				Writes.swap(array, a, a+i, 1, true, false);
				return;
			}
		}
	}
	private int partition(int[] array, int a, int b) {
		int i = a, j = b;
		
		this.randomSqrtMedian(array, a, b);
		Highlights.markArray(3, a);
		
		do {
			do {
				i++;
				Highlights.markArray(1, i);
				Delays.sleep(0.5);
			}
			while(i < j && Reads.compareIndices(array, i, a, 0, false) < 0);
			
			do {
				j--;
				Highlights.markArray(2, j);
				Delays.sleep(0.5);
			}
			while(j >= i && Reads.compareIndices(array, j, a, 0, false) > 0);
				
			if(i < j) Writes.swap(array, i, j, 1, true, false);
			
			else {
				Writes.swap(array, a, j, 1, true, false);
				Highlights.clearMark(3);
				return j;
			}
		}
		while(true);
	}
	private void dualQuickSelect(int[] array, int a, int b, int r1, int r2) {
		int a1 = a, b1 = b;
		
		while(b-a > this.MIN_INSERT) {
			int m = this.partition(array, a, b);
			
			if(m > r2 && m < b1)        b1 = m;
			else if(m < r2 && m+1 > a1) a1 = m+1;
			else if(m == r2)            a1 = b1;
			
			if(m > r1)      b = m;
			else if(m < r1) a = m+1;
			else            break;
		}
		if(b-a <= this.MIN_INSERT) 
			this.smallSort.customBinaryInsert(array, a, b, 0.25);
		
		while(b1-a1 > this.MIN_INSERT) {
			int m = this.partition(array, a1, b1);
			
			if(m == r2) return;
			
			else if(m > r2) b1 = m;
			else if(m < r2) a1 = m+1;
			else            break;
		}
		if(b1-a1 <= this.MIN_INSERT) 
			this.smallSort.customBinaryInsert(array, a1, b1, 0.25);
	}
	
	private int leftBinSearch(int[] array, int a, int b, int val) {
		while(a < b) {
			int m = a+(b-a)/2;
			
			Highlights.markArray(2, m);
			Delays.sleep(0.125);
			
			if(Reads.compareValues(val, array[m]) <= 0) 
				b = m;
			else	 
				a = m+1;
		}
		return a;
	}
	
	private void multiSwap(int[] array, int a, int b, int len) {
		while(len-- > 0) Writes.swap(array, a++, b++, 1, true, false);
	}
	private void mergeFW(int[] array, int a, int m, int b, int p) {
        int pLen = m-a;
        this.multiSwap(array, p, a, pLen);
        
        int i = 0, j = m, k = a;
        
        while(i < pLen && j < b) {
            if(Reads.compareValues(array[p+i], array[j]) <= 0) 
                Writes.swap(array, k++, p+(i++), 1, true, false);
            else
                Writes.swap(array, k++, j++, 1, true, false);
        }
        while(i < pLen) Writes.swap(array, k++, p+(i++), 1, true, false);
	}
	
	//sqrt n way heap
	
	private void maxToFront(int[] array, int a, int b) {
		int max = a;
		
		for(int i = a+1; i < b; i++)
			if(Reads.compareIndices(array, i, max, 0.05, true) > 0)
				max = i;
		
		Writes.swap(array, max, a, 0.5, true, false);
	}
	private void lazyHeapSort(int[] array, int a, int b) {
		int s = (int)Math.sqrt(b-a-1)+1;
		
		for(int i = a; i < b; i += s)
			this.maxToFront(array, i, Math.min(i+s, b));
		
		for(int j = b; j > a;) {
			int max = a;
			
			for(int i = max+s; i < j; i += s)
				if(Reads.compareIndices(array, i, max, 0.05, true) >= 0)
					max = i;
				
			Writes.swap(array, max, --j, 0.5, true, false);
			this.maxToFront(array, max, Math.min(max+s, j));
		}
	}
	
	private void sortBucket(int[] array, int a, int b) {
		if(b-a <= this.MIN_INSERT) {
			this.smallSort.customBinaryInsert(array, a, b, 0.25);
			return;
		}
		
		//bucket may be oversized so we partition equal elements to the right
		//the max element in a bucket is == pivot
		
		int max = a;
		for(int i = a+1; i < b; i++)
			if(Reads.compareIndices(array, i, max, 0.5, true) > 0)
				max = i;
		
		int piv = array[max];
		int i = a-1, j = b;
		
		do {
			do {
				i++;
				Highlights.markArray(1, i);
				Delays.sleep(0.25);
			}
			while(i < j && Reads.compareIndexValue(array, i, piv, 0, false) < 0);
			
			do {
				j--;
				Highlights.markArray(2, j);
				Delays.sleep(0.25);
			}
			while(j >= i && Reads.compareIndexValue(array, j, piv, 0, false) == 0);
				
			if(i < j) Writes.swap(array, i, j, 1, true, false);
			else break;
		}
		while(true);
		
		this.lazyHeapSort(array, a, i);
	}
	
	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		this.rng = new Random();
		this.smallSort = new BinaryInsertionSort(this.arrayVisualizer);
		
		int a = 0, b = length;
		
		int log = 32-Integer.numberOfLeadingZeros(length-1), logSq = log*log;
		int s = length/logSq;
		int bSize = 2*(s+1)*log;
		
		int a1 = a+bSize, b1 = b-bSize;
		
		//create bit buffer
		
		this.dualQuickSelect(array, a, b, a1, b1-1);
		
		BitArray cnt = new BitArray(array, a, b1, s+1, log);
		BitArray pos = new BitArray(array, a+bSize/2, b1+bSize/2, s+1, log);
		
		MaxHeapSort heapSort = new MaxHeapSort(this.arrayVisualizer);
		
		//main sort
		
		if(Reads.compareIndices(array, a1, b1-1, 1, true) < 0) { //if equal we dont have to sort
			int len1 = b1-a1, a2 = a1+s;
			
			for(int i = 0; i < s; i++) { //swap random n/log^2 n elements + sort
				int rand = rng.nextInt(len1-i);
				Writes.swap(array, a1+i, a1+i+rand, 1, true, false);
			}
			heapSort.customHeapSort(array, a1, a2, 0.5);
			
			//partition into buckets
			
			for(int i = a2; i < b1; i++) { //count elements
				Highlights.markArray(1, i);
				Delays.sleep(0.5);
				
				int loc = this.leftBinSearch(array, a1, a2, array[i])-a1;
				cnt.incr(loc);
			}
			for(int i = 0, sum = 0; i < s+1; i++) { //prefix sum
				sum += cnt.get(i);
				pos.set(i, sum);
			}
			for(int i = 0, j = 0; i < s; i++) { //transport elements
				int cur = pos.get(i);
				
				while(j < cur) {
					int loc = this.leftBinSearch(array, a1+i, a2, array[a2+j])-a1;
					
					if(loc == i) Writes.swap(array, a2+j, a2+(--cur), 1, true, false); //access the bit buffer as little as possible
					
					else {
						pos.decr(loc);
						Writes.swap(array, a2+j, a2+pos.get(loc), 1, true, false);
					}
				}
				j += cnt.get(i);
			}
			pos.free();
			
			//sort buckets using sqrt n way heap
			
			for(int i = 0, j = a2; i < s+1; i++) {
				int s1 = cnt.get(i);
				this.sortBucket(array, j, j+s1);
				j += s1;
			}
			cnt.free();
			
			this.mergeFW(array, a1, a2, b1, a); //redistribute pivots
		}
			
		//finishing up
		
    	heapSort.customHeapSort(array, a, a1, 0.25);
    	heapSort.customHeapSort(array, b1, b, 0.25);
	}
}