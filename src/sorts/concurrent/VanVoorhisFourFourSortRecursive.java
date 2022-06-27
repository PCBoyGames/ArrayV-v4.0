package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * 
The MIT License (MIT)

Copyright (c) 2022 Control, aphitorite

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

final public class VanVoorhisFourFourSortRecursive extends Sort {
    public VanVoorhisFourFourSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("[4,4] Van Voorhis Sort (Recursive)");
        this.setRunAllSortsName("Recursive [4,4] Van Voorhis Sorting Network");
        this.setRunSortName("Recursive [4,4] Van Voorhis Sorting Network");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private void compSwap(int[] array, int a, int b) {
		if(Reads.compareIndices(array, a, b, 0.5, true) > 0)
			Writes.swap(array, a, b, 0.5, true, false);
	}
	
	private void merge(int[] array, int p, int n, int g, int depth) {
		Writes.recordDepth(depth);
		if(n == 4) {
			this.compSwap(array, p,     p+g);
			this.compSwap(array, p+2*g, p+3*g);
			this.compSwap(array, p,     p+2*g);
			this.compSwap(array, p+g,   p+3*g);
			this.compSwap(array, p+g,   p+2*g);
			
			return;
		}
		Writes.recursion();
		this.merge(array, p,     n/4, 4*g, depth + 1);
		Writes.recursion();
		this.merge(array, p+g,   n/4, 4*g, depth + 1);
		Writes.recursion();
		this.merge(array, p+2*g, n/4, 4*g, depth + 1);
		Writes.recursion();
		this.merge(array, p+3*g, n/4, 4*g, depth + 1);
		
		for(int i = 2; i+7 < n; i += 4) {
			this.compSwap(array, p+i*g,     p+(i+6)*g);
			this.compSwap(array, p+(i+1)*g, p+(i+7)*g);
		}
		for(int i = 1; i+3 < n; i += 2)
			this.compSwap(array, p+i*g,     p+(i+3)*g);
		
		for(int i = 2; i+3 < n; i += 4) {
			this.compSwap(array, p+i*g,     p+(i+2)*g);
			this.compSwap(array, p+(i+1)*g, p+(i+3)*g);
		}
		for(int i = 3; i+3 < n; i += 2)
			this.compSwap(array, p+i*g,     p+(i+1)*g);
	}
	
	private void sort(int[] array, int p, int n, int depth) {
		Writes.recordDepth(depth);
		int f = n/4;
		
		if(f > 1) {
			Writes.recursion();
			this.sort(array, p,     f, depth + 1);
			Writes.recursion();
			this.sort(array, p+f,   f, depth + 1);
			Writes.recursion();
			this.sort(array, p+2*f, f, depth + 1);
			Writes.recursion();
			this.sort(array, p+3*f, f, depth + 1);
		}
		this.merge(array, p, n, 1, 0);
	}

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
		this.sort(array, 0, sortLength, 0);
    }
}