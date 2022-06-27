package sorts.hybrid;

import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;

import main.ArrayVisualizer;

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

final public class QuadPivotQuickSort extends Sort {
    public QuadPivotQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Quad-Pivot Quick");
        this.setRunAllSortsName("Quad-Pivot Quick Sort");
        this.setRunSortName("Quad-Pivot Quicksort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private void compSwap(int[] array, int a, int b) {
		if(Reads.compareIndices(array, a, b, 0.5, true) > 0)
			Writes.swap(array, a, b, 0.5, false, false);
	}
	
	private void quadPivotQuick(int[] array, int a, int b) {
		int n = b-a;
		
		if(n <= 32) {
			BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
			smallSort.customBinaryInsert(array, a, b, 0.25);
			return;
		}
		
		int s = n/5;
		
		Writes.swap(array, a,   a+s,   1, true, false);
		Writes.swap(array, a+1, a+2*s, 1, true, false);
		Writes.swap(array, a+2, a+3*s, 1, true, false);
		Writes.swap(array, a+3, a+4*s, 1, true, false);
		
		this.compSwap(array, a,   a+1);
		this.compSwap(array, a+2, a+3);
		this.compSwap(array, a,   a+2);
		this.compSwap(array, a+1, a+3);
		this.compSwap(array, a+1, a+2);
		Highlights.clearMark(2);
		
		int piv1 = array[a], piv2 = array[a+1], piv3 = array[a+2], piv4 = array[a+3];
		int i1 = a, i = a, j = b, j1 = b;
		
		for(int k = i; k < j; k++) {
			if(Reads.compareIndexValue(array, k, piv2, 0.5, true) <= 0) {
				int t = array[k];
				
				Writes.write(array, k, array[i], 0.25, true, false);
				if(Reads.compareValues(t, piv1) < 0) {
					Writes.write(array, i, array[i1], 0.25, true, false);
					Writes.write(array, i1++, t, 0.25, true, false);
				}
				else Writes.write(array, i, t, 0.25, true, false);
				i++;
			}
			else if(Reads.compareIndexValue(array, k, piv3, 0.5, true) >= 0) {
				while(--j > k) {
					if(Reads.compareIndexValue(array, j, piv3, 0.5, true) <= 0) 
						break;
					if(Reads.compareIndexValue(array, j, piv4, 0, false) > 0) 
						Writes.swap(array, --j1, j, 0.5, true, false);
				}
				Highlights.clearMark(2);
				
				int t = array[k];
				
				Writes.write(array, k, array[j], 0.25, true, false);
				if(Reads.compareValues(t, piv4) > 0) {
					Writes.write(array, j, array[--j1], 0.25, true, false);
					Writes.write(array, j1, t, 0.25, true, false);
				}
				else Writes.write(array, j, t, 0.25, true, false);
				
				if(Reads.compareIndexValue(array, k, piv2, 0.5, true) <= 0) {
					t = array[k];
					
					Writes.write(array, k, array[i], 0.25, true, false);
					if(Reads.compareValues(t, piv1) < 0) {
						Writes.write(array, i, array[i1], 0.25, true, false);
						Writes.write(array, i1++, t, 0.25, true, false);
					}
					else Writes.write(array, i, t, 0.25, true, false);
					i++;
				}
			}
		}
		this.quadPivotQuick(array, a, i1);
		if(Reads.compareValues(piv1, piv2) < 0) this.quadPivotQuick(array, i1, i);
		if(Reads.compareValues(piv2, piv3) < 0) this.quadPivotQuick(array, i, j);
		if(Reads.compareValues(piv3, piv4) < 0) this.quadPivotQuick(array, j, j1);
		this.quadPivotQuick(array, j1, b);
	}
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.quadPivotQuick(array, 0, length);
    }
}