package sorts.merge;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
import sorts.templates.Sort;

/*
 * 
The MIT License (MIT)

Copyright (c) 2021 aphitorite

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

final public class ImprovedBufferedStoogeSort2 extends Sort {
    public ImprovedBufferedStoogeSort2(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Improved Buffered Stooge II");
        this.setRunAllSortsName("Improved Buffered Stooge Sort II");
        this.setRunSortName("Improved Buffered Stoogesort II");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private final int MIN_INSERT = 32;
	private BinaryInsertionSort binInsSort;
	
	private void medianOfThree(int[] array, int a, int b) {
		int m = a+(b-1-a)/2;
		
		if(Reads.compareIndices(array, a, m, 1, true) == 1)
			Writes.swap(array, a, m, 1, true, false);
		
		if(Reads.compareIndices(array, m, b-1, 1, true) == 1) {
			Writes.swap(array, m, b-1, 1, true, false);
			
			if(Reads.compareIndices(array, a, m, 1, true) == 1)
				return;
		}
		
		Writes.swap(array, a, m, 1, true, false);
	}
	
	private int partition(int[] array, int a, int b) {
		int i = a, j = b;
		
		this.medianOfThree(array, a, b);
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
	private void quickSelect(int[] array, int a, int b, int r) {
		while(b-a > this.MIN_INSERT) {
			int m = this.partition(array, a, b);
			
			if(m == r) return;
			
			else if(m > r) b = m;
			else           a = m+1;
		}
		this.binInsSort.customBinaryInsert(array, a, b, 0.25);
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
	
	private int getMinLevel(int n) {
		while(n >= this.MIN_INSERT) n = (n+3)/4;
		return n;
	}
	
	private void mergeSort(int[] array, int a, int b, int p) {
		int length = b-a;
		if(length < 2) return;
		
		int i, pos, j = this.getMinLevel(length);
		
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
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		this.binInsSort = new BinaryInsertionSort(this.arrayVisualizer);
		
		int a = 0, b = length, s = (b-a+2)/3, m = b-s;
		this.quickSelect(array, a, b, m);
		this.mergeSortHalf(array, a, m, m);
		
		boolean start = false;
		for(; s > this.MIN_INSERT; s = (s+1)/2, start = !start) {
			if(start) this.mergeSortHalf(array, a, a+s, b-(s+1)/2);
			else      this.mergeSortHalf(array, b-s, b, a);
		}
		if(start) this.binInsSort.customBinaryInsert(array, a, a+s, 0.25);
		else      this.binInsSort.customBinaryInsert(array, b-s, b, 0.25);
    }
}