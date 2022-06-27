package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;
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

final public class WeaveQuickSort extends Sort {
	public WeaveQuickSort(ArrayVisualizer arrayVisualizer) {
		super(arrayVisualizer);
		
		this.setSortListName("Weave Quick");
		this.setRunAllSortsName("Weave Quick Sort");
		this.setRunSortName("Weave Quicksort");
		this.setCategory("Hybrid Sorts");
		this.setComparisonBased(true);
		this.setBucketSort(false);
		this.setRadixSort(false);
		this.setUnreasonablySlow(false);
		this.setUnreasonableLimit(0);
		this.setBogoSort(false);
	}
	
	private int medianOf3(int[] array, int[] indices) {
		// small length cases
		
		// maybe an error would be better but w/e
		if(indices.length == 0) return -1;
		
		// median of 1 or 2 elements can just be the first
		if(indices.length < 3) return indices[0];
		
		// 3 element case (common)
		// only first 3 elements are considered if given an array of 4+ indices
		if(Reads.compareIndices(array, indices[0], indices[1], 0.5, true) <= 0) {
			if(Reads.compareIndices(array, indices[1], indices[2], 0.5, true) <= 0)
				return indices[1];
			if(Reads.compareIndices(array, indices[0], indices[2], 0.5, true) < 0)
				return indices[2];
			return indices[0];
		}
		if(Reads.compareIndices(array, indices[1], indices[2], 0.5, true) >= 0) {
			return indices[1];
		}
		if(Reads.compareIndices(array, indices[0], indices[2], 0.5, true) <= 0) {
			return indices[0];
		}
		return indices[2];
	}
	private int mOMHelper(int[] array, int start, int length) {
		if(length == 1) return start;
		
		int[] meds = new int[3];
		int third = length / 3;
		meds[0] = mOMHelper(array, start, third);
		meds[1] = mOMHelper(array, start + third, third);
		meds[2] = mOMHelper(array, start + 2 * third, third);
				
		return medianOf3(array, meds);
	}
	private int medianOfMedians(int[] array, int start, int length) {
		if(length == 1) return start;
		
		int[] meds = new int[3];
		
		int nearPower = (int) Math.pow(3, Math.round(Math.log(length)/Math.log(3)));
		if(nearPower == length)
			return mOMHelper(array, start, length);
		
		nearPower /= 3;
		// uncommon but can happen with numbers slightly smaller than 2*3^k
		// (e.g., 17 < 18 or 47 < 54)
		if(2*nearPower >= length) nearPower /= 3;
		
		meds[0] = mOMHelper(array, start, nearPower);
		meds[2] = mOMHelper(array, start + length - nearPower, nearPower);
		meds[1] = medianOfMedians(array, start + nearPower, length - 2 * nearPower);
		
		return medianOf3(array, meds);
	}
	
	private void insertTo(int[] array, int a, int b) {
		int temp = array[a];
		while(a > b) Writes.write(array, a, array[--a], 0.25, true, false);
		Writes.write(array, a, temp, 0.25, true, false);
	}
	private void rotate(int[] array, int a, int m, int b) {
		IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
	}
	
	//pow of 2 only (O(n))
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
	private void shuffle(int[] array, int a, int b) {//precondition: b-a is even
		int n = b-a;
		
		for(int j = a, m = 0, k = 2; n/k > 0; k *= 2) {
			if(((n/k)&1) == 1) {
				this.bitReversal(array, j, j+k);
				this.bitReversal(array, j, j+k/2);
				this.bitReversal(array, j+k/2, j+k);
				
				Highlights.clearMark(2);
				this.rotate(array, j-m, j, j+k/2);
				
				m += k/2;
				j += k;
			}
		}
	}
	
	private int weavePartition(int[] array, int a, int b, int piv, int cmp) {
		int c = 0;
		
		for(int i = a; i < b; i++) {
			Highlights.markArray(2, i);
			
			if(Reads.compareValues(array[i], piv) < cmp)
				this.insertTo(array, i, i-(c--));
			else
				this.insertTo(array, i, i+(++c));
		}
		int b1 = b-Math.abs(c);
		this.shuffle(array, a, b1);
		
		int p = (a+b1)/2;
		
		if(c < 0) {
			this.rotate(array, p, b1, b);
			p -= c;
		}
		return p;
	}
	private void weaveQuick(int[] array, int a, int b) {
		while(b-a > 32) {
			int n = b-a;
				n -= ~n&1;
			int p = this.medianOfMedians(array, a, n);
			
			int m = this.weavePartition(array, a, b, array[p], 0);
			int left = m-a, right = b-m;
			
			if(m == a) { //few unique
				m = this.weavePartition(array, a, b, array[p], 1);
				a = m;
			}
			else {
				if(right < left) {
					this.weaveQuick(array, m, b);
					b = m;
				}
				else {
					this.weaveQuick(array, a, m);
					a = m;
				}
			}
		}
		BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
		smallSort.customBinaryInsert(array, a, b, 0.25);
	}

	@Override
	public void runSort(int[] array, int currentLength, int bucketCount) {
		this.weaveQuick(array, 0, currentLength);
	}
}