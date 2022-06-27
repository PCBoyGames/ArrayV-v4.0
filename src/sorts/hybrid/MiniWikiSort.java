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

final public class MiniWikiSort extends Sort {
	public MiniWikiSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
		this.setSortListName("Mini Wiki");
		this.setRunAllSortsName("Mini Wikisort");
		this.setRunSortName("Mini Wikisort");
		this.setCategory("Hybrid Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}
	
	private void multiSwap(int[] array, int a, int b, int len) {
		for(int i = 0; i < len; i++)
			Writes.swap(array, a+i, b+i, 1, true, false);
	}
    
    private void rotate(int[] array, int a, int m, int b) {
		IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
    }
	
	private void mergeFW(int[] array, int p, int a, int m, int b, boolean inPlace) {
		if(inPlace) {
			int i = a, j = m, k;
			
			while(i < j && j < b) {
				if(Reads.compareValues(array[i], array[j]) > 0) {
					k = j;
					while(++k < b && Reads.compareIndices(array, i, k, 0, false) > 0);
					
					this.rotate(array, i, j, k);
					
					i += k-j;
					j = k;
				} 
				else i++;
			}
		}
		else {
			int len2 = m-a, pEnd = p+len2;
			
			this.multiSwap(array, p, a, len2);
			
			while(p < pEnd && m < b) {
				if(Reads.compareValues(array[p], array[m]) <= 0)
					Writes.swap(array, a++, p++, 1, true, false);
				
				else Writes.swap(array, a++, m++, 1, true, false);
			}
			while(p < pEnd)
				Writes.swap(array, a++, p++, 1, true, false);
		}
	}
	private void mergeBW(int[] array, int p, int a, int m, int b, boolean inPlace) {
		if(inPlace) {
			int i = m-1, j = b-1, k;
			
			while(j > i && i >= a){
				if(Reads.compareValues(array[i], array[j]) > 0) {
					k = i;
					while(--k >= a && Reads.compareIndices(array, k, j, 0, false) > 0);
					
					this.rotate(array, k+1, i+1, j+1);
					
					j -= i-k;
					i = k;
				} 
				else j--;
			}
		}
		else {
			int len2 = b-m, pEnd = p+len2-1;
			
			this.multiSwap(array, p, m, len2);
			
			m--;
			while(pEnd >= p && m >= a) {
				if(Reads.compareValues(array[pEnd], array[m]) >= 0)
					Writes.swap(array, --b, pEnd--, 1, true, false);
				
				else Writes.swap(array, --b, m--, 1, true, false);
			}
			while(pEnd >= p)
				Writes.swap(array, --b, pEnd--, 1, true, false);
		}
	}
	
	private int selectMin(int[] array, int a, int b, int bLen) {
		int min = a;
		
		for(int i = min+bLen; i < b; i += bLen)
			if(Reads.compareValues(array[i], array[min]) < 0)
				min = i;
			
		return min;
	}
	
	private void blockMerge(int[] array, int a, int m, int b, int p, int t, int bLen, boolean inPlace) {
		if(Reads.compareValues(array[m-1], array[m]) <= 0) return;
		
		else if(Reads.compareValues(array[a], array[b-1]) > 0) {
			this.rotate(array, a, m, b);
			return;
		}
		else if(b-m <= bLen) {
			this.mergeBW(array, p, a, m, b, inPlace);
			return;
		}
		
		int b1 = b-(b-m)%bLen;
		int i  = a+(m-a-1)%bLen+1;
		
		for(int j = i, k = t; j < m; j += bLen, k++)
			Writes.swap(array, j, k, 10, true, false);
		
		while(i < m && m < b1) {
			if(Reads.compareValues(array[i-1], array[m+bLen-1]) > 0) {
				this.multiSwap(array, i, m, bLen);
				this.mergeBW(array, p, a, i, i+bLen, inPlace);
				
				m += bLen;
			}
			else {
				int min = this.selectMin(array, i, m, bLen);
						
				if(min != i) this.multiSwap(array, i, min, bLen);
				Writes.swap(array, t++, i, 1, true, false);
			}
			i += bLen;
		}
		if(i < m) {
			do {
				int min = this.selectMin(array, i, m, bLen);
						
				this.multiSwap(array, i, min, bLen);
				Writes.swap(array, t++, i, 1, true, false);
				i += bLen;
			}
			while(i < m);
			
			this.mergeBW(array, p, a, b1, b, inPlace);
		}
		else {
			while(m < b1 && Reads.compareValues(array[m-bLen], array[m]) > 0) {
				this.mergeBW(array, p, a, m, m+bLen, inPlace);
				m += bLen;
			}
			if(m == b1) this.mergeBW(array, p, a, b1, b, inPlace);
			else        this.mergeFW(array, p, m-bLen+1, m, b, inPlace);
		}
	}
	
	private int countDistinct(int[] array, int a, int b, int nKeys) {
		int r = 1;
		
		for(int i = a+1; i < b && r < nKeys; i++)
			if(Reads.compareIndices(array, i-1, i, 0.5, true) < 0)
				r++;
		
		Highlights.clearMark(2);
		return r;
	}
	private void extractKeys(int[] array, int a, int b, int nKeys) {
		int p = a, f = 1;
		
		for(int i = a+1; f < nKeys; i++)
			if(Reads.compareIndices(array, i-1, i, 0.5, true) < 0) {
				this.rotate(array, p, p+f, i);
				p = i-f;
				f++;
			}
			
		Highlights.clearMark(2);
		this.rotate(array, a, p, p+f);
	}
	
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		int a = 0, b = length;
		BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
		
		if(length <= 32) {
			smallSort.customBinaryInsert(array, a, b, 0.5);
			return;
		}
			
		int j = length;
		while(j > 16) j = (j+1)/2;
		
		//insertion
		
		for(int i = a; i < b; i += j)
			smallSort.customBinaryInsert(array, i, Math.min(i+j, b), 0.25);
		
		//block merge
		
		int minKeys = 4; //lazy stable on this many unique (+1)
		
		for(int k = j, m = (int)Math.sqrt(j-1)+1; j < length; j *= 2) { //main merge sort loop
		
			//buffer length calculation
		
			int bLen;
			
			if(j == k) {
				bLen = m;
				m *= 2;
				k *= 4;
			}
			else bLen = m - (m+3)/4; //approximate sqrt(2) using 1.5
			
			int tLen = j/bLen;
			int keys = bLen + tLen;
			
			//search for buffer
			
			boolean inPlace = false;
			
			int t    = a;
			int kLen = this.countDistinct(array, a, a+j, keys);
			
			if(kLen < keys) {
				for(int i = a+2*j; i+keys < b; i += 2*j) {
					int cnt = this.countDistinct(array, i, Math.min(i+j, b), keys);
					
					if(cnt == keys) {
						t = i;
						kLen = cnt;
						break;
					}
					else if(cnt > kLen) {
						t = i;
						kLen = cnt;
					}
				}
				if(kLen < keys) { //switch to strat 2
					inPlace = true;
					bLen = 2 * ((j-1)/kLen+1);
					kLen /= 2;
				}
			}
			
			//merging loop
			
			if(2*kLen <= minKeys) { //switch to strat 3
				for(int i = a; i+j < b; i += 2*j)
					this.mergeBW(array, 0, i, i+j, Math.min(i+2*j, b), inPlace);
			}
			else {
				this.extractKeys(array, t, Math.min(t+j, b), kLen);
				int p = t+tLen;
				
				for(int i = a; i < t; i += 2*j)
					this.blockMerge(array, i, i+j, i+2*j, p, t, bLen, inPlace);
				
				if(t+j < b) {
					this.blockMerge(array, t+kLen, t+j, Math.min(t+2*j, b), p, t, bLen, inPlace);
					
					for(int i = t+2*j; i < b; i += 2*j)
						this.blockMerge(array, i, i+j, Math.min(i+2*j, b), p, t, bLen, inPlace);
				}
				
				smallSort.customBinaryInsert(array, t, t+kLen, 0.25);
				this.mergeFW(array, 0, t, t+kLen, Math.min(t+2*j, b), true);
			}
		}
    }
}