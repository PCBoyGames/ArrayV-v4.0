package sorts.hybrid;

import sorts.templates.Sort;
import sorts.select.MaxHeapSort;
import sorts.insert.BinaryInsertionSort;
import utils.IndexedRotations;
import main.ArrayVisualizer;
import java.util.Arrays;
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

final public class MukuSort extends Sort {
    public MukuSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Muku");
        this.setRunAllSortsName("Muku Sort");
        this.setRunSortName("Mukusort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
	
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
			
			if(uInt > 0) System.out.println("Warning: Word too large");
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
			System.out.println("Warning: Integer overflow");
		}
		public void decr(int idx) {
			assert (idx >= 0 && idx < size) : "BitArray index out of bounds";
			
			int s = idx*w, i1 = pa+s+w;
			for(int i = pa+s, j = pb+s; i < i1; i++, j++) {
				this.flipBit(i, j);
				if(!this.getBit(i, j)) return;
			}
			System.out.println("Warning: Integer underflow");
		}
	}
	
	private class BitArray2D {
		private final BitArray bits;
		
		public final int x, y, length;
		
		public BitArray2D(int[] array, int pa, int pb, int x, int y, int w) {
			this.bits = new BitArray(array, pa, pb, x*y, w);
			
			this.x = x;
			this.y = y;
			this.length = x*y*w;
		}
		
		public void free() {
			this.bits.free();
		}
		
		public void set(int i, int j, int uInt) {
			this.bits.set(j*this.x+i, uInt);
		}
		public int get(int i, int j) {
			return this.bits.get(j*this.x+i);
		}
		
		public void incr(int i, int j) {
			this.bits.incr(j*this.x+i);
		}
		public void decr(int i, int j) {
			this.bits.decr(j*this.x+i);
		}
	}
	
	/*
	   ____                           _   _____                 _   _                 
	  / ___| ___ _ __   ___ _ __ __ _| | |  ___|   _ _ __   ___| |_(_) ___  _ __  ___ 
	 | |  _ / _ \ '_ \ / _ \ '__/ _` | | | |_ | | | | '_ \ / __| __| |/ _ \| '_ \/ __|
	 | |_| |  __/ | | |  __/ | | (_| | | |  _|| |_| | | | | (__| |_| | (_) | | | \__ \
	  \____|\___|_| |_|\___|_|  \__,_|_| |_|   \__,_|_| |_|\___|\__|_|\___/|_| |_|___/
	  
	*/
	
	private void resetBits(int[] array, int pa, int pb, int bLen) {
		for(int i = 0; i < bLen; i++, pa++, pb++)
			if(Reads.compareIndices(array, pa, pb, 0.5, true) > 0)
				Writes.swap(array, pa, pb, 0.5, true, false);
	}
	
	private void multiSwap(int[] array, int a, int b, int len) {
		while(len-- > 0) Writes.swap(array, a++, b++, 1, true, false);
	}
	
	private void rotate(int[] array, int a, int m, int b) {
		Highlights.clearAllMarks();
		IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
	}
	
	private void insertTo(int[] array, int tmp, int a, int b) {
		Highlights.clearMark(2);
		while(a > b) Writes.write(array, a, array[--a], 0.5, true, false);
		Writes.write(array, b, tmp, 0.5, true, false);
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
	private int rightBinSearch(int[] array, int a, int b, int val) {
		while(a < b) {
			int m = a+(b-a)/2;
			
			Highlights.markArray(2, m);
			Delays.sleep(0.125);
			
			if(Reads.compareValues(val, array[m]) < 0) 
				b = m;
			else	 
				a = m+1;
		}
		return a;
	}
	
	private int selectMin(int[] array, int a, int b, int bLen) {
		int min = a;
		
		for(a += bLen; a < b; a += bLen)
			if(Reads.compareValues(array[a], array[min]) < 0)
				min = a;
			
		return min;
	}
	
	//TODO: add find sqrt n bit buffer
	
	private void bitReversal(int[] array, int a, int b) {
		int len = b-a, m = 0;
		int d1 = len>>1, d2 = d1+(d1>>1);
					
		for(int i = 1; i < len-1; i++) {
			int j = d1;
			
			for(
				int k = i, n = d2; 
				(k&1) == 0; 
				j -= n, k >>= 1, n >>= 1
			);
			m += j;
			if(m > i) Writes.swap(array, a+i, a+m, 1, true, false);
		}
	}
	private void unshuffle(int[] array, int a, int b) { //b-a is even
		int len = (b-a)>>1, c = 0;
		
		for(int n = 2; len > 0; len >>= 1, n *= 2) {
			if((len&1) == 1) {
				int a1 = a+c;
				
				this.bitReversal(array, a1,     a1+n);
				this.bitReversal(array, a1,     a1+n/2);
				this.bitReversal(array, a1+n/2, a1+n);
				this.rotate(array, a+c/2, a1, a1+n/2);
				
				c += n;
			}
		}
	}
	
	private void keySort(int[] array, int t, int tIdx, int tLen) {
		for(int i = 0; i < tLen-1; i++) {
			if(Reads.compareValues(array[t+i], array[tIdx+i]) > 0 || 
			   (i > 0 && Reads.compareValues(array[t+i], array[tIdx+i-1]) < 0)) {
				   
				int next = this.leftBinSearch(array, tIdx, tIdx+tLen, array[t+i])-tIdx;
				
				do {
					Writes.swap(array, t+i, t+next, 1, true, false);
					next = this.leftBinSearch(array, tIdx, tIdx+tLen, array[t+i])-tIdx;
				}
				while(next != i);
			}
		}
	}
	
	//TODO: make a simple kota sort with strat 1 2 3
	
	/*
	  ___             ____  _                  __  __                _             
	 |_ _|_ __       |  _ \| | __ _  ___ ___  |  \/  | ___ _ __ __ _(_)_ __   __ _ 
	  | || '_ \ _____| |_) | |/ _` |/ __/ _ \ | |\/| |/ _ \ '__/ _` | | '_ \ / _` |
	  | || | | |_____|  __/| | (_| | (_|  __/ | |  | |  __/ | | (_| | | | | | (_| |
	 |___|_| |_|     |_|   |_|\__,_|\___\___| |_|  |_|\___|_|  \__, |_|_| |_|\__, |
															   |___/         |___/ 
	*/
	
	private void inPlaceMergeFW(int[] array, int a, int m, int b) {
		while(a < m && m < b) {
			a = this.rightBinSearch(array, a, m, array[m]);
			
			if(a == m) return;
			
			int i = this.leftBinSearch(array, m, b, array[a]);
						
			this.rotate(array, a, m, i);
			
			int t = i-m;
			m = i;
			a += t+1;
		}
	}
	private void inPlaceMergeBW(int[] array, int a, int m, int b) {
		while(b > m && m > a) {
			int i = this.rightBinSearch(array, a, m, array[b-1]);
			
			this.rotate(array, i, m, b);
			
			int t = m-i;
			m = i;
			b -= t+1;
			
			if(m == a) break;
			
			b = this.leftBinSearch(array, m, b, array[m-1]);
		}
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
	private void mergeBW(int[] array, int a, int m, int b, int p) {
        int pLen = b-m;
        this.multiSwap(array, p, m, pLen);
        
        int i = pLen-1, j = m-1, k = b-1;
        
        while(i >= 0 && j >= a) {
            if(Reads.compareValues(array[p+i], array[j]) >= 0) 
                Writes.swap(array, k--, p+(i--), 1, true, false);
            else
                Writes.swap(array, k--, j--, 1, true, false);
        }
        while(i >= 0) Writes.swap(array, k--, p+(i--), 1, true, false);
	}
	
	//TODO: replace all block merges with one block merge
	
	private void cascadeMergeBW(int[] array, int a, int m, int b1, int b) {
		int i = m+b-b1;
		
		while(m > a && b > b1) {
			if(Reads.compareValues(array[b-1], array[m-1]) >= 0)
				Writes.swap(array, --i, --b, 1, true, false);
			else
				Writes.swap(array, --i, --m, 1, true, false);
		}
		while(b > b1) Writes.swap(array, --i, --b, 1, true, false);
	}
	private void bufferRedist(int[] array, int a, int m, int b, int bLen) {
		int a1 = a+(m-a-1)%bLen+1, b1 = b-(b-m)%bLen, i = a1+bLen, j = m;
		
		this.multiSwap(array, a1, m-bLen, bLen);
		
		while(i < j && j < b1) {
			if(Reads.compareValues(array[i-bLen-1], array[j+bLen-1]) > 0) {
				this.multiSwap(array, i, j, bLen);
				this.cascadeMergeBW(array, a, i-bLen, i, i+bLen);
				j += bLen;
			}
			else {
				int min = this.selectMin(array, i, j, bLen);
				if(min > i) this.multiSwap(array, i, min, bLen);
				this.multiSwap(array, i-bLen, i, bLen);
			}
			i += bLen;
		}
		while(j < b1 && Reads.compareValues(array[j-bLen-1], array[j+bLen-1]) > 0) {
			this.cascadeMergeBW(array, a, j-bLen, j, j+bLen);
			j += bLen;
		}
		if(j < b1) {
			i = this.leftBinSearch(array, j, j+bLen, array[j-bLen-1]);
			this.cascadeMergeBW(array, a, j-bLen, j, i);
			this.smallSort.customBinaryInsert(array, i-bLen, i, 0.25);
			this.inPlaceMergeFW(array, i-bLen, i, b);
		}
		else {
			while(i-bLen < b1) {
				int min = this.selectMin(array, i-bLen, b1, bLen);
				if(min > i-bLen) this.multiSwap(array, i-bLen, min, bLen);
				i += bLen;
			}
			this.smallSort.customBinaryInsert(array, b1-bLen, b1, 0.25);
			this.inPlaceMergeBW(array, a, b1, b);
		}
	}
	
	private void blockSelect(int[] array, int a, int b, int bLen) {
		for(; a < b; a += bLen) {
			int min = this.selectMin(array, a, b, bLen);
			if(min > a) this.multiSwap(array, a, min, bLen);
		}
	}
	
	private void unstableKotaMerge(int[] array, int a, int m, int b, int bLen) {
		if(b-m <= bLen) {
			this.inPlaceMergeBW(array, a, m, b);
			return;
		}
		int bufLen = 2*bLen, r1 = 0, r2 = Math.min(b-m, bufLen);
		
		while(r1 < r2) {
			int ml = (r1+r2)/2;
			
			if(Reads.compareValues(array[m+ml], array[a+(bufLen-ml)-1]) > 0)
				r2 = ml;
			else
				r1 = ml+1;
		}
		int l = bufLen-r1, r = r1, i = a+l, j = m+r;
		
		boolean left = l >= r;
		int k = left ? i-l : j-r;
		
		int c = bLen;
				
		while(i < m && j < b) {
			if(Reads.compareValues(array[i], array[j]) <= 0) {
				Writes.swap(array, k++, i++, 1, true, false);
				l++;
			}
			else {
				Writes.swap(array, k++, j++, 1, true, false);
				r++;
			}
			if(--c == 0) {
				if(left) l -= bLen;
				else     r -= bLen;
				
				left = l >= r;
				k = left ? i-l : j-r;
				
				c = bLen;
			}
		}
		while(i < m) {
			Writes.swap(array, k++, i++, 1, true, false);
			l++;
			
			if(--c == 0) {
				if(left) l -= bLen;
				else     r -= bLen;
				
				left = l >= r;
				k = left ? i-l : j-r;
				
				c = bLen;
			}
		}
		while(j < b) {
			Writes.swap(array, k++, j++, 1, true, false);
			r++;
			
			if(--c == 0) {
				if(left) l -= bLen;
				else     r -= bLen;
				
				c = bLen;
				break;
			}
		}
		int rem = bLen-c, b1 = Math.min(j, b-rem);
		
		this.multiSwap(array, k-rem, b1, rem); //swap remainder to end (r buffer)
		r -= rem;
		
		//l and r buffers are divisible by bLen
		this.multiSwap(array, m-l, a, l);    //swap l buffer to front
		this.multiSwap(array, b1-r, a+l, r); //swap r buffer to front
		this.smallSort.customBinaryInsert(array, a, a+bufLen, 0.25); //sort buffer
		
		this.blockSelect(array, a+bufLen, b1, bLen);
	}
	
	/*
	  ___             ____  _                  ____            _   _ _   _             _             
	 |_ _|_ __       |  _ \| | __ _  ___ ___  |  _ \ __ _ _ __| |_(_) |_(_) ___  _ __ (_)_ __   __ _ 
	  | || '_ \ _____| |_) | |/ _` |/ __/ _ \ | |_) / _` | '__| __| | __| |/ _ \| '_ \| | '_ \ / _` |
	  | || | | |_____|  __/| | (_| | (_|  __/ |  __/ (_| | |  | |_| | |_| | (_) | | | | | | | | (_| |
	 |___|_| |_|     |_|   |_|\__,_|\___\___| |_|   \__,_|_|   \__|_|\__|_|\___/|_| |_|_|_| |_|\__, |
																							   |___/ 
	*/	
	
	//TODO: replace all of these with a single pivot version
	
	private long sqrtMedian(int[] array, int a, int b) {
		int len = b-a, s = (int)Math.sqrt(len);
		s = s-s%3+2;
		
		int step = len/s;
		a = b-(s*step);
		int t1 = s/3, t2 = s-s/3;
		
		boolean lFound = false, rFound = false;
		long res = 0;
		
		for(int j = a;; j += step) {
			int c = 0, ce = 0;
			
			for(int i = a; i < b; i += step) {
				if(i == j) continue;
				
				int cmp = Reads.compareIndices(array, i, j, 1, true);
				
				c  += cmp <  0 ? 1 : 0;
				ce += cmp <= 0 ? 1 : 0;
			}
			
			if(t1 >= c && t1 <= ce) {
				lFound = true;
				res |= (long)j;
				if(rFound) break;
			}
			if(t2 >= c && t2 <= ce) {
				rFound = true;
				res |= (long)j << 32;
				if(lFound) break;
			}
		}
		return res;
	}
	
	private int pivCmp(int[] array, int i, int piv1, int piv2, int p1Cmp, int p2Cmp) {
		if(Reads.compareValues(array[i], piv1) == p1Cmp)
			return 0;
		else if(Reads.compareValues(array[i], piv2) == p2Cmp)
			return 2;
		else
			return 1;
	}
	
	private int pivSearch(int[] array, int a, int b, int vCmp, int piv1, int piv2, int p1Cmp, int p2Cmp) {
		while(a < b) {
			int m = a+(b-a)/2;
			
			if(vCmp <= this.pivCmp(array, m, piv1, piv2, p1Cmp, p2Cmp)) 
				b = m;
			else	 
				a = m+1;
		}
		return a;
	}
	
	private void dualPivQuickSelect(int[] array, int a, int b, int r, int bp, int bLen) {
		int minInsert = 24, p1Cmp = -1, p2Cmp = 1;
		int piv1 = 0, piv2 = 0;
		boolean pickPiv = true;
		
		while(b-a > minInsert) {
			int n = b-a;
			
			if(pickPiv) {
				long res = this.sqrtMedian(array, a, b);
				piv1 = array[(int)res];
				piv2 = array[(int)(res >> 32)];
			}
			
			//partition
			
			int[] cnt = {0, 0, 0};
			int p = a;
			
			for(int i = a; i < b; i++) {
				int c = this.pivCmp(array, i, piv1, piv2, p1Cmp, p2Cmp);
				int bPos = bp+c*bLen;
				
				Writes.swap(array, bPos+cnt[c]++, i, 1, true, false);
				
				if(cnt[c] == bLen) {
					this.multiSwap(array, p, bPos, bLen);
					p += bLen;
					cnt[c] = 0;
				}
			}
			for(int i = 2, b1 = b; i >= 0; i--) {
				int bPos = bp+i*bLen;
				
				while(cnt[i]-- > 0) 
					Writes.swap(array, --b1, bPos+cnt[i], 1, true, false);
			}
			this.smallSort.customBinaryInsert(array, bp, bp+(p-a)/bLen, 0.25);
			
			for(int j = a; j < p-bLen; j += bLen) { //stable selection sort
				int min = j, mCmp = this.pivCmp(array, min, piv1, piv2, p1Cmp, p2Cmp);
				
				for(int i = min+bLen; i < p; i += bLen) {
					int vCmp = this.pivCmp(array, i, piv1, piv2, p1Cmp, p2Cmp);
					
					if(vCmp < mCmp || (vCmp == mCmp && Reads.compareIndices(array, bp+(i-a)/bLen, bp+(min-a)/bLen, 0.5, true) < 0)) {
						min = i; 
						mCmp = vCmp;
					}
				}
				if(min > j) {
					this.multiSwap(array, j, min, bLen);
					Writes.swap(array, bp+(j-a)/bLen, bp+(min-a)/bLen, 0.5, true, false);
				}
			}
			
			//rotate leftovers
			
			int m2 = this.pivSearch(array, a, p, 2, piv1, piv2, p1Cmp, p2Cmp);
			int m1 = this.pivSearch(array, p, b, 2, piv1, piv2, p1Cmp, p2Cmp);
			
			this.rotate(array, m2, p, m1);
			
			int m3 = p;
			p = m2;
			m2 += m1-m3;
			
			m1 = this.pivSearch(array, a, p, 1, piv1, piv2, p1Cmp, p2Cmp);
			m3 = this.pivSearch(array, p, m2, 1, piv1, piv2, p1Cmp, p2Cmp);
			
			this.rotate(array, m1, p, m3);
			m1 += m3-p;
			
			//prune
			
			if(r < m1) {
				if(!pickPiv) return;
				b = m1;
			}
			else if(r < m2) {
				if(Reads.compareValues(piv1, piv2) == 0) return;
				a = m1; b = m2;
				
				if(!pickPiv) {
					p1Cmp = -1; p2Cmp = 1;
					pickPiv = true;
				}
				else if(b-a == n) {
					p1Cmp = 0; p2Cmp = 0;
					pickPiv = false;
				}
			}
			else {
				if(!pickPiv) return;
				a = m2;
			}
		}
		this.smallSort.customBinaryInsert(array, a, b, 0.25);
	}
	
	/*
	TODO: add
	- in place stable block cycle 
		- destination
		+ make a ternary version
	- partition
	- partitionNoBuf
		- block sorting
		- block typing
	- partition3Way
	- partition3WayNoBuf
		- block sorting (ternary)
		- block typing (ternary)
		+ n log n no log n buffer option
	*/
	
	/*
	  _  __             ____      _ _           _   _             
	 | |/ /___ _   _   / ___|___ | | | ___  ___| |_(_) ___  _ __  
	 | ' // _ \ | | | | |   / _ \| | |/ _ \/ __| __| |/ _ \| '_ \ 
	 | . \  __/ |_| | | |__| (_) | | |  __/ (__| |_| | (_) | | | |
	 |_|\_\___|\__, |  \____\___/|_|_|\___|\___|\__|_|\___/|_| |_|
			   |___/
	*/
	
	//standard grail sort findKeys()
	private int findKeysSm(int[] array, int a, int b, int n) {
		int r = 1, p = a;
		
		for(int i = a+r; r < n && i < b; i++) {
			Highlights.markArray(1, i);
			Delays.sleep(1);
			
			int loc = this.leftBinSearch(array, p, p+r, array[i]);
			
			if(loc == p+r || Reads.compareValues(array[i], array[loc]) < 0) {
				this.rotate(array, p, p+r, i);
				this.rotate(array, i-r+loc-p, i, i+1);
				p = i-(r++);
			}
		}
		this.rotate(array, a, p, p+r);
		
		return r;
	}
	
	private long findKeysLg(int[] array, int a, int m, int a1, int a2, int b) {
		int minInsert = 16; //for simplicity this is a pow of 2
		int minSqrt   = 4;  //sqrt of minInsert
		
		int p = m-(m-a)%minInsert, c = (p-a)/minInsert, lastIdx = a2;
		
		for(int i = a2; m < a1 && i < b; i++) {
			Highlights.markArray(1, i);
			Delays.sleep(1);
			
			boolean found = false;
			
			int loc = this.leftBinSearch(array, p, m, array[i]);
			found |= loc < m && Reads.compareValues(array[i], array[loc]) == 0;
			
			for(int j = 1, k = p; !found && j <= c; j <<= 1) {
				if(((c/j)&1) == 1) {
					int loc1 = this.leftBinSearch(array, k-j*minInsert, k, array[i]);
					found |= loc1 < k && Reads.compareValues(array[i], array[loc1]) == 0;
					
					k -= j*minInsert;
				}
			}
			if(!found) {
				lastIdx = i;
				
				Writes.swap(array, m, i, 1, true, false);
				this.rotate(array, loc, m, ++m);
				
				if(m-p == minInsert) {
					p = m;
					c++;
					
					for(int j = 1, k = 2, s = minInsert, bSize = minSqrt; ((c/j)&1) == 0; j <<= 1) {
						if(j == k) {
							k *= 4;
							bSize *= 2;
						}
						this.unstableKotaMerge(array, m-s-j*minInsert, m-s, m, bSize);
						s += j*minInsert;
					}
				}
			}
		}
		for(int j = 1, k = 2, s = m-p, bSize = minSqrt; j <= c; j <<= 1) {
			if(j == k) {
				k *= 4;
				bSize *= 2;
			}
			if(((c/j)&1) == 1) {
				this.unstableKotaMerge(array, m-s-j*minInsert, m-s, m, bSize);
				s += j*minInsert;
			}
		}
		return (long)lastIdx << 32 | (long)(m-a);
	}
	
	//TODO: remove (replaced with partition(NoBuf))
	private void collectFillers(int[] array, int a, int b, int piv, int bp, int bLen) {
		//partition
		
		int[] cnt = {0, 0};
		int p = a;
		
		for(int i = a; i < b; i++) {
			int c = (Reads.compareValues(array[i], piv)+1)/2;
			int bPos = bp+c*bLen;
			
			Writes.swap(array, bPos+cnt[c]++, i, 1, true, false);
			
			if(cnt[c] == bLen) {
				this.multiSwap(array, p, bPos, bLen);
				p += bLen;
				cnt[c] = 0;
			}
		}
		for(int i = 1, b1 = b; i >= 0; i--) {
			int bPos = bp+i*bLen;
			
			while(cnt[i]-- > 0) 
				Writes.swap(array, --b1, bPos+cnt[i], 1, true, false);
		}
		this.smallSort.customBinaryInsert(array, bp, bp+(p-a)/bLen, 0.25);
		
		for(int j = a; j < p-bLen; j += bLen) { //stable selection sort
			int min = j, mCmp = (Reads.compareValues(array[min], piv)+1)/2;
			
			for(int i = min+bLen; i < p; i += bLen) {
				int vCmp = (Reads.compareValues(array[i], piv)+1)/2;
				
				if(vCmp < mCmp || (vCmp == mCmp && Reads.compareIndices(array, bp+(i-a)/bLen, bp+(min-a)/bLen, 0.5, true) < 0)) {
					min = i; 
					mCmp = vCmp;
				}
			}
			if(min > j) {
				this.multiSwap(array, j, min, bLen);
				Writes.swap(array, bp+(j-a)/bLen, bp+(min-a)/bLen, 0.5, true, false);
			}
		}
		
		//rotate leftovers
		
		this.rotate(array, this.rightBinSearch(array, a, p, piv), p, this.rightBinSearch(array, p, b, piv));
		this.smallSort.customBinaryInsert(array, bp, bp+2*bLen, 0.25);
	}
	
	/*
	  ____  _        _     _        _____ _               ____             _   
	 / ___|| |_ __ _| |__ | | ___  |  ___| | __ _ _ __   / ___|  ___  _ __| |_ 
	 \___ \| __/ _` | '_ \| |/ _ \ | |_  | |/ _` | '_ \  \___ \ / _ \| '__| __|
	  ___) | || (_| | |_) | |  __/ |  _| | | (_| | | | |  ___) | (_) | |  | |_ 
	 |____/ \__\__,_|_.__/|_|\___| |_|   |_|\__,_|_| |_| |____/ \___/|_|   \__|
	 
	*/
	
	private final int G = 14;
	private final int R = 4;
	
	private Random rng;
	
	private int stableCompare(int[] array, int a, int b, int posA, int pos) {
		int cmp = Reads.compareValues(array[a], array[b]);
		return cmp == 0 ? Reads.compareValues(array[pos+a-posA], array[pos+b-posA]) : cmp;
	}
	
	private void stableSwap(int[] array, int a, int b, int posA, int pos) {
		Writes.swap(array, a, b, 0, true, false);
		Writes.swap(array, pos+a-posA, pos+b-posA, 0.5, false, false);
	}
	
	private void stableWrite(int[] array, int a, int b, int posA, int pos) {
		Writes.write(array, a, array[b], 0, true, false);
		Writes.write(array, pos+a-posA, array[pos+b-posA], 0.5, false, false);
	}
	
	private void shiftBW(int[] array, int a, int m, int b, int posA, int pos) {
		while(m > a) this.stableSwap(array, --b, --m, posA, pos);
	}
	
	private int medianOfThree(int[] array, int a, int m, int b, int posA, int pos) {
		if(this.stableCompare(array, m, a, posA, pos) > 0) {
			if(this.stableCompare(array, m, b, posA, pos) < 0)
				return m;
			if(this.stableCompare(array, a, b, posA, pos) > 0)
				return a;
			else
				return b;
		}
		else {
			if(this.stableCompare(array, m, b, posA, pos) > 0)
				return m;
			if(this.stableCompare(array, a, b, posA, pos) < 0)
				return a;
			else
				return b;
		}
	}
	private int ninther(int[] array, int a, int posA, int pos) { //goes out of bounds n < 9
		int m1 = this.medianOfThree(array, a,   a+1, a+2, posA, pos);
		int m2 = this.medianOfThree(array, a+3, a+4, a+5, posA, pos);
		int m3 = this.medianOfThree(array, a+6, a+7, a+8, posA, pos);
		
		return this.medianOfThree(array, m1, m2, m3, posA, pos);
	}
	private int medianOfThreeNinthers(int[] array, int a, int posA, int pos) { //goes out of bounds n < 27
		int m1 = this.ninther(array, a,    posA, pos);
		int m2 = this.ninther(array, a+9,  posA, pos);
		int m3 = this.ninther(array, a+18, posA, pos);
		
		return this.medianOfThree(array, m1, m2, m3, posA, pos);
	}
	
	private int blockSearch(int[] array, int a, int b, int idx, int posA, int pos) {
		int s = G+1;
		
		while(a < b) {
			int m = a+(((b-a)/s)/2)*s;
			
			Highlights.markArray(3, m);
			Delays.sleep(0.125);
			
			if(this.stableCompare(array, idx, m, posA, pos) < 0) 
				b = m;
			else     
				a = m+s;
		}
		
		Highlights.clearMark(3);
		return a;
	}
	private int stableBinSearch(int[] array, int a, int b, int idx, boolean bw, int posA, int pos) {
		int cmp = bw ? 1 : -1;
		
		while(a < b) {
			int m = a+(b-a)/2;
			
			Highlights.markArray(3, m);
			Delays.sleep(0.125);
			
			if(this.stableCompare(array, idx, m, posA, pos) == cmp) 
				b = m;
			else
				a = m+1;
		}
		
		Highlights.clearMark(3);
		return a;
	}
	
	private void stableInsertTo(int[] array, int a, int b, int posA, int pos) {
		Highlights.clearMark(2);
		
		int tmpA = array[a], tmp = array[pos+a-posA];
		
		while(a > b) this.stableWrite(array, a, --a, posA, pos);
		
		Writes.write(array, b, tmpA, 0, true, false);
		Writes.write(array, pos+b-posA, tmp, 0.5, false, false);
	}
	
	private void stableBinInsert(int[] array, int a, int b, int posA, int pos) {
    	for(int i = a+1; i < b; i++)
			this.stableInsertTo(array, i, this.stableBinSearch(array, a, i, i, false, posA, pos), posA, pos);
    }
	
	private void siftDown(int[] array, int[] heap, int[] pa, int r, int size, int posA, int pos) {
		while(2*r+2 < size) {
			int nxt = 2*r+1;
			int min = nxt + (this.stableCompare(array, pa[heap[nxt]], pa[heap[nxt+1]], posA, pos) < 0 ? 0 : 1);

			if(this.stableCompare(array, pa[heap[min]], pa[heap[r]], posA, pos) < 0) {
				Writes.swap(heap, r, min, 0, false, true);
				r = min;
			}
			else break;
		}
		int min = 2*r+1;

		if(min < size && this.stableCompare(array, pa[heap[min]], pa[heap[r]], posA, pos) < 0)
			Writes.swap(heap, r, min, 0, false, true);
	}
	
	private void kWayMerge(int[] array, int[] heap, int[] pa, int s, int b, int p, int size, int posA, int pos) {
		if(size < 2) {
			if(size == 1) while(pa[0] < b) this.stableSwap(array, p++, pa[0]++, posA, pos);
			return;
		}
		int a = pa[0];
		
		for(int i = 0; i < size; i++)
			Writes.write(heap, i, i, 0, false, true);

		for(int i = (size-1)/2; i >= 0; i--)
			this.siftDown(array, heap, pa, i, size, posA, pos);
			
		while(size > 0) {
			int min = heap[0];
			
			this.stableSwap(array, p++, pa[min]++, posA, pos);

			if(pa[min] == Math.min(a+(min+1)*s, b))
				Writes.swap(heap, 0, --size, 0, false, true);
			
			this.siftDown(array, heap, pa, 0, size, posA, pos);
		}
	}
	
	private void retrieve(int[] array, int i, int p, int pEnd, int bsIdx, boolean bw, int posA, int pos) {
		for(int k = pEnd-(G+1); k > p+G;) {
			int m = this.stableBinSearch(array, k-G, k, bsIdx, bw, posA, pos);
			k -= G+1;
			
			while(m > k) this.stableSwap(array, --i, --m, posA, pos);
		}
		int m = this.stableBinSearch(array, p, p+G, bsIdx, bw, posA, pos);
		while(m > p) this.stableSwap(array, --i, --m, posA, pos);
	}
	
	//buffer length is at least sortLength*(G+1)-1
	private void librarySort(int[] array, int a, int b, int p, int bsIdx, boolean bw, int posA, int pos) {
		int len = b-a;
		
		if(len <= 32) {
			this.stableBinInsert(array, a, b, posA, pos);
			return;
		}
		
		int s = len;
		while(s >= 32) s = (s-1)/R + 1;
		
		int i = a+s, j = a+R*s, pEnd = p + (s+1)*(G+1)+G;
		
		this.stableBinInsert(array, a, i, posA, pos);
		
		for(int k = 0; k < s; k++) //scatter elements to make G sized gaps b/w them
			this.stableSwap(array, a+k, p + k*(G+1)+G, posA, pos);
		
		while(i < b) {
			if(i == j) { //rebalancing (retrieve from buffer & rescatter)
				this.retrieve(array, i, p, pEnd, bsIdx, bw, posA, pos);
				
				s = i-a;
				pEnd = p + (s+1)*(G+1)+G;
				j = a+(j-a)*R;
				
				for(int k = 0; k < s; k++) 
					this.stableSwap(array, a+k, p + k*(G+1)+G, posA, pos);
			}
			
			int bLoc = this.blockSearch(array, p+G, pEnd-(G+1), i, posA, pos);          //search gap location
			int loc  = this.stableBinSearch(array, bLoc-G, bLoc, bsIdx, bw, posA, pos); //search next empty space in gap
			
			if(loc == bLoc) { //if there is no empty space filled elements in gap are split
				do bLoc += G+1; 
				while(bLoc < pEnd && this.stableBinSearch(array, bLoc-G, bLoc, bsIdx, bw, posA, pos) == bLoc);
				
				if(bLoc == pEnd) { //rebalancing 
					this.retrieve(array, i, p, pEnd, bsIdx, bw, posA, pos);
					
					s = i-a;
					pEnd = p + (s+1)*(G+1)+G;
					j = a+(j-a)*R;
					
					for(int k = 0; k < s; k++) 
						this.stableSwap(array, a+k, p + k*(G+1)+G, posA, pos);
				}
				else { //if a gap is full find next non full gap to the right & shift the space down
					int rotP = this.stableBinSearch(array, bLoc-G, bLoc, bsIdx, bw, posA, pos);
					int rotS = bLoc - Math.max(rotP, bLoc - G/2);
					this.shiftBW(array, loc-rotS, bLoc-rotS, bLoc, posA, pos); 
				}
			}
			else {
				this.stableSwap(array, i++, loc, posA, pos);
				this.stableInsertTo(array, loc, this.stableBinSearch(array, bLoc-G, loc, loc, false, posA, pos), posA, pos);
			}
		}
		this.retrieve(array, b, p, pEnd, bsIdx, bw, posA, pos);
	}
	
	private void stableFlanSort(int[] array, int a, int b, int pos) {
		int[] pa   = new int[G+2]; 
		int[] heap = new int[G+2];
		
		int alloc = pa.length + heap.length;
		Writes.changeAllocAmount(alloc);
		
		int posA = a;
		
		for(int i = a+1; i < b; i++)
			this.stableSwap(array, i, a+rng.nextInt(i+1-a), posA, pos);
		
		while(b-a >= 32) {
			this.stableSwap(array, a, this.medianOfThreeNinthers(array, a, posA, pos), posA, pos);
			
			int i = a, j = b;
			Highlights.markArray(3, a);
			
			for(;;) {
				do {
					i++;
					Highlights.markArray(1, i);
					Delays.sleep(0.5);
				}
				while(i < j && this.stableCompare(array, i, a, posA, pos) > 0);
				
				do {
					j--;
					Highlights.markArray(2, j);
					Delays.sleep(0.5);
				}
				while(j >= i && this.stableCompare(array, j, a, posA, pos) < 0);
					
				if(i < j) this.stableSwap(array, i, j, posA, pos);
				else {
					this.stableSwap(array, a, j, posA, pos);
					Highlights.clearMark(3);
					break;
				}
			}
			int left = j-a, right = b-j-1, m, kCnt = 0;
			
			if(left <= right) {
				m = b-left;
				left = Math.max((right+1)/(G+1), 16);
				
				for(int k = a; k < j; k += left) {
					this.librarySort(array, k, Math.min(k+left, j), j+1, j, true, posA, pos);
					pa[kCnt++] = k;
				}
				this.kWayMerge(array, heap, pa, left, j, m, kCnt, posA, pos);
				
				b = m--;
			}
			else {
				m = a+right;
				right = Math.max((left+1)/(G+1), 16);
				
				for(int k = j+1; k < b; k += right) {
					this.librarySort(array, k, Math.min(k+right, b), a, j, false, posA, pos);
					pa[kCnt++] = k;
				}
				this.kWayMerge(array, heap, pa, right, b, a, kCnt, posA, pos);
				
				a = m+1;
			}
			this.stableSwap(array, m, j, posA, pos);
		}
		this.stableBinInsert(array, a, b, posA, pos);
		Writes.changeAllocAmount(-alloc);
	}
	
	/*
	  ___             ____  _                  ____                _   ____             _   
	 |_ _|_ __       |  _ \| | __ _  ___ ___  |  _ \ ___ _ __ ___ (_) / ___|  ___  _ __| |_ 
	  | || '_ \ _____| |_) | |/ _` |/ __/ _ \ | |_) / _ \ '_ ` _ \| | \___ \ / _ \| '__| __|
	  | || | | |_____|  __/| | (_| | (_|  __/ |  _ <  __/ | | | | | |  ___) | (_) | |  | |_ 
	 |___|_| |_|     |_|   |_|\__,_|\___\___| |_| \_\___|_| |_| |_|_| |____/ \___/|_|   \__|
	 
	*/
	
	private int selectMinIdx(int[] array, int h, int rCnt, int bits) {
		int min = -1;
		
		for(int i = 0; i < rCnt; i++)
			if(((bits >> i) & 1) == 0 && (min == -1 || Reads.compareValues(array[h+i], array[h+min]) < 0))
				min = i;
		
		return min;
	}
	private void kWayBlockMerge(int[] array, int bp, int t, int h, int a, int b, BitArray p, BitArray pSize, int bLen, int rCnt, int rLen) {
		int tp = t, bits = 0;
		
		for(int i = 0; i < rCnt; i++)
			Writes.swap(array, h+i, a+i*rLen, 1, true, false);
		
		for(int i = 0; i < rLen; i++) {
			int min  = this.selectMinIdx(array, h, rCnt, bits);
			int mIdx = p.get(min);
			
			p.incr(min);
			pSize.incr(min);

			if(mIdx+1 == Math.min(rLen, b-a-min*rLen)) {
				bits |= 1 << min;
				Writes.swap(array, bp+i, h+min, 1, true, false);
			}
			else {
				int tmp = array[bp+i];
				Writes.write(array, bp+i, array[h+min], 0.5, true, false);
				Writes.write(array, h+min, array[a+min*rLen+mIdx+1], 0.5, true, false);
				Writes.write(array, a+min*rLen+mIdx+1, tmp, 0.5, true, false);
			}
		}
		for(int i = 0; i < rLen; i += bLen)
			Writes.swap(array, bp+i, tp++, 1, true, false);
		
		int min, c = 0, cnt = 0;
		while(pSize.get(c) < bLen) c++;
		int pos = a+c*rLen+p.get(c)-pSize.get(c);
		min = this.selectMinIdx(array, h, rCnt, bits);
		
		do {
			int mIdx = p.get(min);
			
			p.incr(min);
			pSize.incr(min);

			if(mIdx+1 == Math.min(rLen, b-a-min*rLen)) {
				bits |= 1 << min;
				Writes.swap(array, pos++, h+min, 1, true, false);
			}
			else {
				int tmp = array[pos];
				Writes.write(array, pos++, array[h+min], 0.5, true, false);
				Writes.write(array, h+min, array[a+min*rLen+mIdx+1], 0.5, true, false);
				Writes.write(array, a+min*rLen+mIdx+1, tmp, 0.5, true, false);
			}
			
			if(++cnt == bLen) {
				cnt = 0;
				
				Writes.swap(array, pos-bLen, tp++, 1, true, false);
				pSize.set(c, pSize.get(c)-bLen);
				
				c = 0;
				while(pSize.get(c) < bLen) c++;
				pos = a+c*rLen+p.get(c)-pSize.get(c);
			}
			
			min = this.selectMinIdx(array, h, rCnt, bits);
		}
		while(min != -1);
		
		pSize.set(rCnt-1, pSize.get(rCnt-1)-cnt);
		while(cnt-- > 0) Writes.swap(array, --b, --pos, 1, true, false);
		
		for(int i = 0, j = bp; i < rCnt; i++) {
			int ps = pSize.get(i);
			
			while(ps > 0) {
				this.multiSwap(array, j, Math.min(a+(i+1)*rLen, b)-ps, bLen);
				ps -= bLen;
				j += bLen;
			}
		}
		for(int i = a; i < b; i += bLen) {
			min = this.selectMin(array, i, b, bLen);
			
			if(min > i) this.multiSwap(array, i, min, bLen);
			Writes.swap(array, t++, i, 1, true, false);
		}
		
		p.free();
		pSize.free();
	}
	
	/*
		 _                    ____                        _        ____             _   
		/ \   ___  ___  ___  / ___|  __ _ _ __ ___  _ __ | | ___  / ___|  ___  _ __| |_ 
	   / _ \ / _ \/ _ \/ __| \___ \ / _` | '_ ` _ \| '_ \| |/ _ \ \___ \ / _ \| '__| __|
	  / ___ \  __/ (_) \__ \  ___) | (_| | | | | | | |_) | |  __/  ___) | (_) | |  | |_ 
	 /_/   \_\___|\___/|___/ |____/ \__,_|_| |_| |_| .__/|_|\___| |____/ \___/|_|   \__|
												   |_|                                               
	*/
	
	private int selectRank(int[] array, int a, int a1, int a2, int b, int r) {
		while(a < a1) {
			int m = (a+a1)/2;
			
			Highlights.markArray(2, m);
			
			int c = a2, ce = a2;
			
			for(int i = a2; i < b; i++) {
				int cmp = Reads.compareIndexValue(array, i, array[m], 0.25, true);
				
				c  += cmp <  0 ? 1 : 0;
				ce += cmp <= 0 ? 1 : 0;
			}
			
			if(r >= c && r < ce) return m;
			
			else if(r < c) a1 = m;
			else           a  = m+1;
		}
		return a;
	}
	
	private void sort2(int[] array, int a, int a1, int a2, int b, int log2, int piv) {
		boolean hasBuf = piv != -1;
		
		int ak, keys, bits, a3;
		
		//TODO: find that extra set of keys
		
		/*if(!hasBuf) {
			ak = this.selectRank(array, a, a1, a1, b, a2)+1;
			keys = a1-ak; bits = log2*keys;
			
			a2 = a1+bits;
			int piv1 = array[ak-1];
			
			//find bit buffer + partition
		
			Arrays.sort(array, a1, a2); //sort filler zone using a block merge
		}
		else {
			ak = this.rightBinSearch(array, a, a1, piv);
			keys = a1-ak; bits = log2*keys;
		}
		
		//add items equal to pivot to zone 
		
		while(++a2 < b && Reads.compareIndices(array, a2-1, a2, 1, true) == 0);
		if(a2 == b) { //edge case
			this.inPlaceMergeFW(array, a, a1, b);
			return;
		}
		
		a3 = a2+bits;
		
		if(a3 >= b) { //edge case
			Arrays.sort(array, a2, b);
			this.inPlaceMergeFW(array, a, a1, b);
			return;
		}*/
		
		//BitArray cnts = new BitArray(array, a2-bits, a2, keys, log2);
		
	}
	
	/*
	   ___                  _   _     _                          ____      _ _     ____             _   
	  / _ \ _   _  __ _ ___(_) | |   (_)_ __   ___  __ _ _ __   / ___|_ __(_) |_  / ___|  ___  _ __| |_ 
	 | | | | | | |/ _` / __| | | |   | | '_ \ / _ \/ _` | '__| | |  _| '__| | __| \___ \ / _ \| '__| __|
	 | |_| | |_| | (_| \__ \ | | |___| | | | |  __/ (_| | |    | |_| | |  | | |_   ___) | (_) | |  | |_ 
	  \__\_\\__,_|\__,_|___/_| |_____|_|_| |_|\___|\__,_|_|     \____|_|  |_|\__| |____/ \___/|_|   \__|
	 
	*/
	
	/*
	  __  __       _            ____             _   
	 |  \/  |_   _| | ___   _  / ___|  ___  _ __| |_ 
	 | |\/| | | | | |/ / | | | \___ \ / _ \| '__| __|
	 | |  | | |_| |   <| |_| |  ___) | (_) | |  | |_ 
	 |_|  |_|\__,_|_|\_\\__,_| |____/ \___/|_|   \__|
	 
	*/
	
	private void sort3(int[] array) {
	}
	public void mainSort(int[] array, int a, int b) {
		int n = b-a;
		
		if(n < 4096) return; //block merge sort
		
		this.smallSort = new BinaryInsertionSort(this.arrayVisualizer);
		this.rng = new Random();
		
		/*
		* CREATE SQRT N BUFFER
		*/
		
		int sqrtBLen = (int)Math.sqrt((n+2)/3-1)+1;
		int sqrtBufLen = 3*sqrtBLen;
		
		int keys = this.findKeysSm(array, a, b, sqrtBufLen);
		int a1 = a+keys;
		
		int log2 = 31 - Integer.numberOfLeadingZeros(n);
		int logSq = log2*log2;
		
		if(keys < sqrtBufLen) { //determine strat 2 or 3
			//if(keys >= logSq) this.sort2(array, a, a1, a1, b, -1);
			//else              this.sort3();
			return;
		}
		
		/*
		* CREATE N / LOG N BUFFER + FILLER ZONE
		*/
		
		int logBLen = (n-1)/logSq+1;
		logBLen -= logBLen%2; //force rLen to be an even number
		
		int rLen = logBLen*log2, tLen = n/logBLen;
		int logBufLen = rLen + tLen + log2;
		
		int a2 = a+logBufLen, a3 = a2+sqrtBufLen;
		
		//stable quick select
		
		this.dualPivQuickSelect(array, a1, b, a3, 0, sqrtBLen);
		int piv = array[a3];
		
		this.smallSort.customBinaryInsert(array, a, a1, 0.25);
		Arrays.sort(array, a1, a3); //sort filler zone using a block merge
		
		//add items equal to pivot to zone 
		
		while(++a3 < b && Reads.compareIndices(array, a3-1, a3, 1, true) == 0);
		if(a3 == b) { //edge case
			this.inPlaceMergeFW(array, a, a1, b);
			return;
		}
		
		//search and extract the keys
		
		long res = this.findKeysLg(array, a, a1, a2, a3, b);
		keys = (int)res; a2 = a+keys;
		int m1 = a3+keys-sqrtBufLen, m2 = (int)(res >> 32)+1;
		
		//collect + rotate filler elements
		
		if(m2 > m1) this.collectFillers(array, a3, m2, piv, 0, sqrtBufLen/2);
		this.rotate(array, a2, a3, m1);
		
		if(keys < logBufLen) { //determine strat 2 or 3
			//if(keys >= logSq) this.sort2(array, a, a2, m1, b, piv);
			//else              this.sort3();
			return;
		}
		
		/*
		* SORTING WITH MANY DISTINCT KEYS (STRATEGY 1)
		*/
		
		this.unshuffle(array, a, a+rLen);
		a1 = a+rLen/2;
		
		m2 = m1+2*logSq; //2 log n words of bit buffer space for merge indices and buffer sizes
		
		if(m2 >= b) { //edge case
			this.smallSort.customBinaryInsert(array, m1, b, 0.25);
			this.bufferRedist(array, a, a2, b, sqrtBLen);
			return;
		}
		this.smallSort.customBinaryInsert(array, m1, m2, 0.25); //we can create a bit buffer out of the filler zone
		
		int rCnt = 0;
		for(int i = m2;;) {
			this.stableFlanSort(array, i, Math.min(i+rLen/2, b), a);
			i += rLen/2; rCnt++;
			
			if(i < b) this.keySort(array, a, a+rLen/2, rLen/2);
			else      break;
		}
		rCnt = (rCnt+1)/2;
		
		BitArray p     = new BitArray(array, m1-2*logSq, m1, rCnt, log2);
		BitArray pSize = new BitArray(array, m1-logSq, m1+logSq, rCnt, log2);
		
		for(int i = m2; i+rLen/2 < b; i += rLen)
			this.mergeBW(array, i, i+rLen/2, Math.min(i+rLen, b), a);
		
		this.kWayBlockMerge(array, a, a+rLen, a+rLen+tLen, m2, b, p, pSize, logBLen, rCnt, rLen);
		
		this.mergeFW(array, m1, m2, b, a);
		MaxHeapSort heapSort = new MaxHeapSort(this.arrayVisualizer);
    	heapSort.customHeapSort(array, a, a2, 0.5);
		this.bufferRedist(array, a, a2, b, sqrtBLen);
	}
	
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.mainSort(array, 0, length);
    }
}