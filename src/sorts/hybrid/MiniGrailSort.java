package sorts.hybrid;

import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;
import main.ArrayVisualizer;
import utils.IndexedRotations;

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

final public class MiniGrailSort extends Sort {
	public MiniGrailSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
		this.setSortListName("Mini Grail");
		this.setRunAllSortsName("Mini Grail Sort");
		this.setRunSortName("Mini Grailsort");
		this.setCategory("Hybrid Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}
	
	private int tLenCalc(int n, int bLen) {
		int n1 = n-bLen;
		int a = 0, b = bLen;
		
		while(a < b) {
			int m = (a+b)/2;
			
			if(n1-m < (m+1)*bLen) b = m;
			else                  a = m+1;
		}
		return a;
	}
	
	private void multiSwap(int[] array, int a, int b, int s) {
        while(s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }
	private void rotate(int[] array, int a, int m, int b) {
		IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
	}
	
	private int binSearch(int[] array, int a, int b, int val, boolean left) {
		int cmp = left ? 1 : 0;
		
		while(a < b) {
			int m = a+(b-a)/2;
			Highlights.markArray(2, m);
			Delays.sleep(0.25);
			
			if(Reads.compareValues(val, array[m]) < cmp) 
				b = m;
			else     
				a = m+1;
		}
		return a;
	}
	
	private int findKeysBW(int[] array, int a, int b, int nKeys) {
		int f = 1, p = b-f;
		
		for(int i = p; i > a && f < nKeys; i--) {
			Highlights.markArray(1, i);
			Delays.sleep(0.5);
			
			int loc = this.binSearch(array, p, p+f, array[i-1], true)-p;
				
			if(loc == f || Reads.compareValues(array[i-1], array[p+loc]) != 0) {
				this.rotate(array, i, p, p+(f++));
				p = i-1;
				this.rotate(array, i-1, i, p+loc+1);
			}
			Highlights.clearMark(2);
		}
		this.rotate(array, p, p+f, b);
		
		return f;
	}
	
	private void mergeBW(int[] array, int a, int m, int b, int p, boolean left) {
		int cmp = left ? -1 : 0;
		
        int pLen = b-m;
        this.multiSwap(array, m, p, pLen);
        
        int i = pLen-1, j = m-1, k = b-1;
        
        while(i >= 0 && j >= a) {
            if(Reads.compareValues(array[p+i], array[j]) > cmp) 
                Writes.swap(array, k--, p+(i--), 1, true, false);
            else
                Writes.swap(array, k--, j--, 1, true, false);
        }
        while(i >= 0) Writes.swap(array, k--, p+(i--), 1, true, false);
    }
	private void inPlaceMergeBW(int[] array, int a, int m, int b, boolean left) {
		while(b > m && m > a) {
			int i = this.binSearch(array, a, m, array[b-1], !left);
			
			this.rotate(array, i, m, b);
			
			int t = m-i;
			m = i;
			b -= t+1;
			
			if(m == a) break;
			
			b = this.binSearch(array, m, b, array[m-1], left);
		}
	}
	private void mergeBlocks(int[] array, int a, int m, int b, int p, boolean left, boolean hasBuf) {
		if(hasBuf) this.mergeBW(array, a, m, b, p, left);
		else       this.inPlaceMergeBW(array, a, m, b, left);
	}
	
	private void mergeTo(int[] array, int a, int m, int b, int p) {
		int i = a, j = m;
		
		while(i < m && j < b) {
			if(Reads.compareValues(array[i], array[j]) <= 0)
				Writes.swap(array, p++, i++, 1, true, false);
			else 
				Writes.swap(array, p++, j++, 1, true, false);
		}
		while(i < m) Writes.swap(array, p++, i++, 1, true, false);
		while(j < b) Writes.swap(array, p++, j++, 1, true, false);
	}
	private void pingPongMerge(int[] array, int a, int m1, int m, int m2, int b, int p) {
		int p1 = p+m-a, pEnd = p+b-a;
			
		this.mergeTo(array, a, m1, m, p);
		this.mergeTo(array, m, m2, b, p1);
		this.mergeTo(array, p, p1, pEnd, a);
	}
	
	private void blockSelect(int[] array, int a, int ta, int tm, int tb, int bLen) {
		int i1 = ta, j1 = tm, k = ta;

		while(k < j1 && j1 < tb) {
			if(Reads.compareValues(array[a+(i1-ta+1)*bLen-1], array[a+(j1-ta+1)*bLen-1]) <= 0) {
				if(i1 > k) this.multiSwap(array, a+(k-ta)*bLen, a+(i1-ta)*bLen, bLen);
				Writes.swap(array, k++, i1, 1, true, false);
				
				i1 = k;
				for(int i = Math.max(k+1, tm); i < j1; i++)
					if(Reads.compareValues(array[i], array[i1]) < 0)
						i1 = i;
			}
			else {
				this.multiSwap(array, a+(k-ta)*bLen, a+(j1-ta)*bLen, bLen);
				Writes.swap(array, k, j1++, 1, true, false);
				
				if(i1 == k++) i1 = j1-1;
			}
		}
		while(k < j1-1) {
			if(i1 > k) this.multiSwap(array, a+(k-ta)*bLen, a+(i1-ta)*bLen, bLen);
			Writes.swap(array, k++, i1, 1, true, false);
			
			i1 = k;
			for(int i = k+1; i < j1; i++)
				if(Reads.compareValues(array[i], array[i1]) < 0)
					i1 = i;
		}
	}
	private void blockMerge(int[] array, int a, int m, int b, int t, int p, int bLen, boolean hasBuf) {
		int tLen1 = (m-a)/bLen, bCnt = (b-a)/bLen;
		int tm = t+tLen1, tb = t+bCnt, b1 = b-(b-m)%bLen;
		
		int mKey = array[tm];
		this.blockSelect(array, a, t, tm, tb, bLen);
		
		int f = a;
		boolean left = Reads.compareIndexValue(array, t, mKey, 1, true) < 0;
		
		for(int i = 1; i < bCnt; i++) {
			if(left ^ (Reads.compareIndexValue(array, t+i, mKey, 1, true) < 0)) {
				int nxt  = a + i*bLen;
				int nxtE = this.binSearch(array, nxt, nxt+bLen, array[nxt-1], left);
				
				this.mergeBlocks(array, f, nxt, nxtE, p, left, hasBuf);
				f = nxtE;
				left = !left;
			}
		}
		if(left) this.mergeBlocks(array, f, b1, b, p, left, hasBuf);
		
		BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
		smallSort.customBinaryInsert(array, t, tb, 0.25);
	}
	
	@Override
	public void runSort(int[] array, int length, int bucketCount) { //TODO: keys sort
		int a = 0, b = length;
		BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
		
		if(length <= 32) {
			smallSort.customBinaryInsert(array, a, b, 0.5);
			return;
		}
		
		int bLen = 1 << ((30-Integer.numberOfLeadingZeros(length))/2+1),
			tLen = this.tLenCalc(length, bLen);
			
		int j = 16;
		
		int keys = this.findKeysBW(array, a, b, bLen+tLen);
		
		if(keys == 1) return;
		
		else if(keys <= 4) { //strat 3: lazy stable
			for(int i = a; i < b; i += j)
				smallSort.customBinaryInsert(array, i, Math.min(i+j, b), 0.25);
			
			for(; j < length; j *= 2)
				for(int i = a; i+j < b; i += 2*j)
					this.inPlaceMergeBW(array, i, i+j, Math.min(i+2*j, b), true);
				
			return;
		}
		
		if(keys < bLen+tLen) {
			keys = 1 << (31-Integer.numberOfLeadingZeros(keys));
			bLen = keys/2;
			tLen = keys/2;
		}
		
		length -= keys;
		int b1 = b-keys, t = b1, p = b1+tLen;
		
		//insertion
		
		for(int i = a; i < b1; i += j)
			smallSort.customBinaryInsert(array, i, Math.min(i+j, b1), 0.25);
		
		//merging w/ buffer
		
		for(int i; 4*j <= bLen; j *= 4) {
			for(i = a; i+2*j < b1; i += 4*j)
				this.pingPongMerge(array, i, i+j, i+2*j, Math.min(i+3*j, b1), Math.min(i+4*j, b1), p);
			if(i+j < b1)
				this.mergeBW(array, i, i+j, b1, p, true);
		}
		
		for(; j <= bLen; j *= 2)
			for(int i = a; i+j < b1; i += 2*j)
				this.mergeBW(array, i, i+j, Math.min(i+2*j, b1), p, true);
		
		//block merge
		
		int limit = bLen*(tLen+1);
		
		for(int i; j < length && Math.min(2*j, length) < limit; j *= 2) {
			for(i = a; i+j+bLen < b1; i += 2*j)
				this.blockMerge(array, i, i+j, Math.min(i+2*j, b1), t, p, bLen, true);
			if(i+j < b1)
				this.mergeBW(array, i, i+j, b1, p, true);
		}
		
		smallSort.customBinaryInsert(array, p, b, 0.25);
		
		//strat 2
		
		bLen = 2*j/keys;
			
		for(int i; j < length; j *= 2, bLen *= 2) {
			for(i = a; i+j+bLen < b1; i += 2*j)
				this.blockMerge(array, i, i+j, Math.min(i+2*j, b1), t, p, bLen, false);
			if(i+j < b1)
				this.inPlaceMergeBW(array, i, i+j, b1, true);
		}
		
		this.inPlaceMergeBW(array, a, b1, b, true);
	}
}