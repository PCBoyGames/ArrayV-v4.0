package sorts.concurrent;

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

final public class CircleHalverSort extends Sort {
    public CircleHalverSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Circle Halver");
        this.setRunAllSortsName("Circle Halver Sort");
        this.setRunSortName("Circle Halver Sorting Network");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private int end;
	
	private boolean compSwap(int[] array, int a, int b) {
		if(a < this.end && b < this.end && 
		   Reads.compareIndices(array, a, b, 0.25, true) > 0) {
			   
			Writes.swap(array, a, b, 0.25, true, false);
			return true;
		}
		return false;
	}
	
	private boolean bwdComp(int[] array, int a, int b) {
		boolean did = false;
		
		for(int i = a, j = b-1; i < j; i++, j--)
			did = this.compSwap(array, i, j) ? true : did;
		
		return did;
	}
	
	private void fwdComp(int[] array, int a, int m, int l) {
		for(int i = 0; i < l; i++)
			this.compSwap(array, a+i, m+i);
	}
	
	private void halver(int[] array, int a, int b) {
		int n = b-a; int c = -1;
		boolean did = true;
		
		while(did) {
			did = false;
			
			for(int j = 1; j < n/2; j *= 2)
				for(int i = a; i < Math.min(this.end, b); i += 2*j)
					this.fwdComp(array, i, i+j, j);
				
			did = this.bwdComp(array, a, b) ? true : did;
			c++;
		}
		if(c > (int)(Math.log(b-a)/Math.log(2))-1)
			System.out.printf("[%d: %d] ", (int)(Math.log(b-a)/Math.log(2))-1, c);
	}
	
    @Override
    public void runSort(int[] array, int length, int bucketCount) throws Exception {
		this.end = length;
		length = 1 << (int)(Math.log(length-1)/Math.log(2))+1;
		
		this.halver(array, 0, length);
		
		for(int j = length/2; j > 4; j /= 2)
			for(int i = 0; i+j/2 < this.end; i += j)
				if(this.bwdComp(array, i, i+j))
					this.halver(array, i, i+j);
			
		for(int i = 2; i < this.end; i += 4)
			this.compSwap(array, i-1, i);
    }
}