package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

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

final public class BinarySiftHeapSort extends Sort {
	public BinarySiftHeapSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);

		this.setSortListName("Binary Sift Heap");
		this.setRunAllSortsName("Binary Sift Heap Sort");
		this.setRunSortName("Binary Sift Heapsort");
		this.setCategory("Selection Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}
	
	private int binSearch(int[] array, int[] idx, int b, int val) {
		int a = 0;
		
		while(a < b) {
			int m = (a+b)/2;
			
			Highlights.markArray(2, idx[m]);
			Delays.sleep(2);
			
			if(Reads.compareValues(array[idx[m]], val) <= 0)
				b = m;
			else
				a = m+1;
		}
		return a;
	}
	
	private void siftDown(int[] array, int[] idx, int r, int t, int b) {
		int j = r, c = 0;
		r = 2*r+1;
		
		while(r+1 < b) {
			if(Reads.compareIndices(array, r, r+1, 0, false) < 0) r++;
			Writes.write(idx, c++, r, 0, false, true);
			r = 2*r+1;
		}
		if(r < b) Writes.write(idx, c++, r, 0, false, true);
		
		int k = this.binSearch(array, idx, c, t);
		
		for(int i = 0; i < k; i++) {
			Writes.write(array, j, array[idx[i]], 0.25, true, false);
			j = idx[i];
		}
		Writes.write(array, j, t, 0.25, true, false);
		
		Highlights.clearMark(2);
	}
	
	@Override
	public void runSort(int[] array, int length, int bucketCount) {
		int r = 0;
		while((1 << r) < length) r++;
		
		int[] idx = Writes.createExternalArray(r+1);
		
		for(int i = (length-1)/2; i >= 0; i--)
			this.siftDown(array, idx, i, array[i], length);
		
		for(int i = length-1; i > 0; i--) {
			int t = array[i];
			
			Writes.write(array, i, array[0], 0.5, true, false);
			Highlights.clearMark(1);
			this.siftDown(array, idx, 0, t, i);
		}
	}
}