package sorts.hybrid;

import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;
import sorts.insert.InsertionSort;

import main.ArrayVisualizer;

/*
 * 
MIT License

Copyright (c) 2020 yuji, implemented by aphitorite

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

final public class MedianMergeSort2 extends Sort {
    public MedianMergeSort2(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Median Merge II");
        this.setRunAllSortsName("Median Mergesort II");
        this.setRunSortName("Median Mergesort II");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private BinaryInsertionSort binInsSort;
	
	private void medianOfThree(int[] array, int a, int b) {
		int m = a+(b-1-a)/2;
		
		if(Reads.compareIndices(array, a, m, 0.5, true) > 0)
			Writes.swap(array, a, m, 0.5, false, false);
		
		if(Reads.compareIndices(array, m, b-1, 0.5, true) > 0) {
			Writes.swap(array, m, b-1, 0.5, false, false);
			
			if(Reads.compareIndices(array, a, m, 0.5, true) > 0)
				return;
		}
	}
	
	private void medianOfFive(int[] array, int a) {
		this.medianOfThree(array, a, a+5);
		this.medianOfThree(array, a+1, a+4);
	}
	
	//lite version
	private void medianOfMedians(int[] array, int a, int b) {
		int end = b, start = a, i, j;
		boolean ad = true;
		
		while(end - start > 1) {
			j = start;
			Highlights.markArray(2, j);
			for(i = start; i+10 <= end; i+=5) {
				this.medianOfFive(array, i);
				Writes.swap(array, j++, i+2, 1, false, false);
				Highlights.markArray(2, j);
			}
			if(i < end) {
				this.binInsSort.customBinaryInsert(array, i, end, 0.25);
				Writes.swap(array, j++, i+(end-(ad ? 1 : 0)-i)/2, 1, false, false);
				Highlights.markArray(2, j);
				if((end-i)%2 == 0) ad = !ad;
			}
			end = j;
		}
	}
	
	public int partition(int[] array, int a, int b, int p) {
        int i = a - 1;
        int j = b;
		Highlights.markArray(3, p);
		
        while(true) {
			do {
				i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
			}
			while(i < j && Reads.compareIndices(array, i, p, 0, false) == -1);
			
			do {
				j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
			}
            while(j >= i && Reads.compareIndices(array, j, p, 0, false) == 1);
				
            if(i < j) Writes.swap(array, i, j, 1, true, false);
            else      return j;
        }
    }
	
	private void merge(int[] array, int a, int m, int b, int p) {
		int i = a, j = m;
		
		while(i < m && j < b) {
			if(Reads.compareIndices(array, i, j, 0, false) <= 0)
				Writes.swap(array, p++, i++, 1, true, false);
			else 
				Writes.swap(array, p++, j++, 1, true, false);
		}
		
		while(i < m) Writes.swap(array, p++, i++, 1, true, false);
		while(j < b) Writes.swap(array, p++, j++, 1, true, false);
	}
	
	public static int getMinLevel(int n) {
		while(n >= 32) n = (n+3)/4;
		return n;
	}
	
	private void mergeSort(int[] array, int a, int b, int p) {
		int length = b-a;
		if(length < 2) return;
		
		int i, pos, j = getMinLevel(length);
		
		for(i = a; i+j <= b; i+=j)
			this.binInsSort.customBinaryInsert(array, i, i+j, 0.25);
		this.binInsSort.customBinaryInsert(array, i, b, 0.25);
		
		while(j < length) {
			pos = p;
			for(i = a; i+2*j <= b; i+=2*j, pos+=2*j)
				this.merge(array, i, i+j, i+2*j, pos);
			if(i + j < b)
				this.merge(array, i, i+j, b, pos);
			else
				while(i < b) Writes.swap(array, i++, pos++, 1, true, false);
			
			j *= 2;
			
			pos = a;
			for(i = p; i+2*j <= p+length; i+=2*j, pos+=2*j)
				this.merge(array, i, i+j, i+2*j, pos);
			if(i + j < p+length)
				this.merge(array, i, i+j, p+length, pos);
			else
				while(i < p+length) Writes.swap(array, i++, pos++, 1, true, false);
			
			j *= 2;
		}
	}
	private void mergeSortHalf(int[] array, int a, int b, int p) {
		int m = a+(b-a)/2, m1 = a+(m-a)/2;
		
		this.mergeSort(array, a, m1, p);
		this.mergeSort(array, m1, m, p);
		this.mergeSort(array, m, b, p);
		this.merge(array, a, m1, m, p);
		
		int i = p, j = m, k = a;
		
		while(i < p+m-a && j < b) {
			if(Reads.compareIndices(array, i, j, 0, false) <= 0)
				Writes.swap(array, k++, i++, 1, true, false);
			else 
				Writes.swap(array, k++, j++, 1, true, false);
		}
		while(i < p+m-a)
			Writes.swap(array, k++, i++, 1, true, false);
	}
	
	private void medianMergeSort(int[] array, int a, int b) {
		int start = a, end = b;
		boolean badPartition = false, mom = false;
		
		while(end - start > 16) {
			if(badPartition) {
				this.medianOfMedians(array, start, end);
				mom = true;
			}
			else {
				this.medianOfThree(array, start, end);
				Writes.swap(array, start, start+(end-start-1)/2, 1, true, false);
			}
			
			int p = this.partition(array, start+1, end, start);
			Writes.swap(array, start, p, 1, true, false);
			
			int left  = p-start;
			int right = end-(p+1);
			badPartition = !mom && ((left == 0 || right == 0) || (left/right >= 16 || right/left >= 16));
			
			if(left <= right) {
				if(2*left >= right) {
					this.mergeSortHalf(array, p+1, end, start);
					end = p;
				}
				else {
					this.mergeSort(array, start, p, p+1);
					start = p+1;
				}
			}
			else {
				if(2*right >= left) {
					this.mergeSortHalf(array, start, p, p+1);
					start = p+1;
				}
				else {
					this.mergeSort(array, p+1, end, start);
					end = p;
				}
			}
		}
		this.binInsSort.customBinaryInsert(array, start, end, 0.25);
	}
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.binInsSort = new BinaryInsertionSort(this.arrayVisualizer);
		this.medianMergeSort(array, 0, length);
    }
}