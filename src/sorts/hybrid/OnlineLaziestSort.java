package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * 
MIT License

Copyright (c) 2020 aphitorite

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

final public class OnlineLaziestSort extends Sort {
    public OnlineLaziestSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Online Laziest Stable");
        this.setRunAllSortsName("Online Laziest Stable Sort");
        this.setRunSortName("Online Laziest Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private void insertTo(int[] array, int a, int b) {
		Highlights.clearMark(2);
		int temp = array[a];
		while(a > b) Writes.write(array, a, array[--a], 0.5, true, false);
		Writes.write(array, b, temp, 0.5, true, false);
	}
    
	private void multiSwap(int[] array, int a, int b, int len) {
		for(int i = 0; i < len; i++)
			Writes.swap(array, a+i, b+i, 1, true, false);
	}
	
    private void rotate(int[] array, int a, int m, int b) {
        int l = m-a, r = b-m;
		
        while(l > 0 && r > 0) {
			if(r < l) {
				this.multiSwap(array, m-r, m, r);
				b -= r;
				m -= r;
				l -= r;
            }
            else {
				this.multiSwap(array, a, m, l);
				a += l;
				m += l;
				r -= l;
            }
        }
    }
	
	private int rightBinSearch(int[] array, int a, int b, int val) {
		while(a < b) {
			int m = a+(b-a)/2;
			
			if(Reads.compareValues(val, array[m]) < 0) 
				b = m;
			else     
				a = m+1;
		}
		
		return a;
	}
    
    private void binaryInsertion(int[] array, int a, int b) {
    	for(int i = a+1; i < b; i++)
			this.insertTo(array, i, this.rightBinSearch(array, a, i, array[i]));
    }
	
    private void inPlaceMergeBW(int[] array, int a, int m, int b) {
        int i = m-1, j = b-1, k;
        
        while(j > i && i >= a){
            if(Reads.compareValues(array[i], array[j]) > 0) {
                k = this.rightBinSearch(array, a, i, array[j]);
                this.rotate(array, k, i+1, j+1);
                
                j -= (i+1)-k;
                i = k-1;
            } 
            else j--;
        }
    }
    
    public void laziestStableSort(int[] array, int a, int b) {
		int s = 16, j = 512;
		
		for(int i = a; i < b; i += s) {
			if(i-a == j) {
				j *= 4;
				s *= 2;
			}
			int e = Math.min(i+s, b);
			this.binaryInsertion(array, i, e);
			this.inPlaceMergeBW(array, a, i, e);
		}
	}
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	this.laziestStableSort(array, 0, currentLength);
    }
}