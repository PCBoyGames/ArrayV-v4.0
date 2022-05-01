package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;
import java.util.Arrays;

/*
 * 
The MIT License (MIT)

Copyright (c) 2020-2022 aphitorite

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

final public class InPlaceMergeSortII extends Sort {
    public InPlaceMergeSortII(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("In-Place Merge II");
        this.setRunAllSortsName("In-Place Merge Sort II");
        this.setRunSortName("In-Place Merge Sort II");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	
	private void inPlaceMerge2(int[] array, int a, int m, int b) {
		int i = a, j = m, k;
		
		while(j < b) {
			m = j; k = m;
			if(Reads.compareValues(array[j-1], array[j]) <= 0) return;
			
			while(Reads.compareValues(array[i], array[j]) <= 0) i++;
			Writes.swap(array, i++, j++, 1, true, false);
			
			while(i < m && j < b) {
				if(Reads.compareValues(array[k], array[j]) <= 0) {
					Writes.swap(array, i++, k++, 1, true, false);
					if(k == j) k = m;
				}
				else {
					IndexedRotations.cycleReverse(array, m, k, j, 0.25, true, false);
					Writes.swap(array, i++, j++, 1, true, false);
					k = m;
				}
			}
			IndexedRotations.cycleReverse(array, m, k, j, 0.25, true, false);
		}
		IndexedRotations.cycleReverse(array, i, m, b, 0.25, true, false);
	}
    
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
		for(int j = 1; j < length; j *= 2) 
			for(int i = 0; i+j < length; i += 2*j)
				this.inPlaceMerge2(array, i, i+j, Math.min(i+2*j, length));
    }
}