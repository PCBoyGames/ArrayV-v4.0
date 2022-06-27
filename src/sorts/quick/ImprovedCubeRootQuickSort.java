package sorts.quick;

import static java.lang.Math.cbrt;
import java.util.Random;
import main.ArrayVisualizer;
import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;

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

public class ImprovedCubeRootQuickSort extends Sort {
    public ImprovedCubeRootQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Improved Cube Root Quick");
        this.setRunAllSortsName("Improved Cube Root Quick Sort");
        this.setRunSortName("Improved Cube Root Quick Sort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private int binSearch(int[] array, int b, int val, boolean left) {
		int a = 0, cmp = left ? -1 : 0;
		
		while(a < b) {
			int m = (a+b)/2;
			
			Highlights.markArray(2, m);
			Delays.sleep(0.25);
			
			if(Reads.compareValues(array[m], val) > cmp)
				b = m;
			else
				a = m+1;
		}
		return a;
	}
	
	private void quickSort(int[] array, int[] tmp, int a, int b) {
		if(b-a < 32) {
			BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
			smallSort.customBinaryInsert(array, a, b, 0.25);
			
			return;
		}
		Highlights.clearAllMarks();
		
		Random r = new Random();
		int n = b-a, size = (int)cbrt(n), s = (n-1)/size+1, c = 0;
		
		for(int i = a+r.nextInt(s-1); i < b; i += s, c++) {
			int loc = this.binSearch(tmp, c, array[i], false);
			
			for(int j = c; j > loc; j--)
				Writes.write(tmp, j, tmp[j-1], 0.25, true, true);
			Writes.write(tmp, loc, array[i], 1, true, true);
		}
		
		if(Reads.compareValues(tmp[0], tmp[c-1]) == 0) {
			int i = a, j = b, piv = tmp[0];
			
			for(int k = i; k < j; k++) {
				if(Reads.compareValues(array[k], piv) == -1) {
					Writes.swap(array, k, i++, 1, true, false);
				}
				else if(Reads.compareValues(array[k], piv) == 1) {
					do {
						j--;
						Highlights.markArray(3, j);
						Delays.sleep(1);
					}
					while(j > k && Reads.compareValues(array[j], piv) == 1);
					
					Writes.swap(array, k, j, 1, true, false);
					Highlights.clearMark(3);
					
					if(Reads.compareValues(array[k], piv) == -1) {
						Writes.swap(array, k, i++, 1, true, false);
					}
				}
			}
			this.quickSort(array, tmp, a, i);
			this.quickSort(array, tmp, j, b);
			
			return;
		}
		
		int bCnt = c+1;
		int[] pa = new int[bCnt];
		int[] pb = new int[bCnt];
		Writes.changeAllocAmount(2*bCnt);
		
		Writes.write(pa, 0, a, 0, false, true);
		Writes.write(pb, 0, a, 0, false, true);
		
		for(int i = a; i < b; i++) {
			Highlights.markArray(1, i);
			
			int loc = this.binSearch(tmp, c, array[i], true);
			Writes.write(pb, loc, pb[loc]+1, 0, false, true);
		}
		for(int i = 1; i < bCnt; i++)
			Writes.write(pb, i, pb[i]+pb[i-1], 0, false, true);
		Writes.arraycopy(pb, 0, pa, 1, bCnt-1, 0, false, true);
		
		for(int i = 0; i < bCnt-1; i++) {
			while(pa[i] < pb[i]) {
				int t = array[pa[i]], t0 = -1;
				
				Highlights.markArray(4, pa[i]);
				int nxt = this.binSearch(tmp, c, t, true), nxt0;
				
				while(nxt != i) {
					Highlights.markArray(3, pa[nxt]);
					nxt0 = this.binSearch(tmp, c, array[pa[nxt]], true);
						
					if(pa[nxt] != pb[nxt]-1)
						while(nxt0 == nxt) {
							Writes.write(pa, nxt, pa[nxt]+1, 0, false, true);
							Highlights.markArray(3, pa[nxt]);
							nxt0 = this.binSearch(tmp, c, array[pa[nxt]], true);
						}
							
					t0 = array[pa[nxt]];
					Writes.write(array, pa[nxt], t, 0.5, true, false);
					Writes.write(pa, nxt, pa[nxt]+1, 0, false, true);
					t = t0;
					
					nxt = nxt0;
				}
				Highlights.clearAllMarks();
				
				if(t0 != -1) Writes.write(array, pa[i], t, 0.5, true, false);
				Writes.write(pa, i, pa[i]+1, 0, false, true);
			}
		}
		this.quickSort(array, tmp, a, pb[0]);
		for(int i = 1; i < bCnt; i++)
			this.quickSort(array, tmp, pb[i-1], pb[i]);
		
		Writes.changeAllocAmount(-2*bCnt);
	}

    @Override
    public void runSort(int[] array, int length, int buckets) {
		int[] tmp = Writes.createExternalArray((int)cbrt(length));
		
		this.quickSort(array, tmp, 0, length);
		
		Writes.deleteExternalArray(tmp);
    }
}