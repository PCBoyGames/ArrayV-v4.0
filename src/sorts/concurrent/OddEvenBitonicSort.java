package sorts.concurrent;

import sorts.templates.Sort;
import main.ArrayVisualizer;

/*
 *
MIT License
Copyright (c) 2020 w0rthy
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

public class OddEvenBitonicSort extends Sort {

    public OddEvenBitonicSort(ArrayVisualizer arrayVisualizer) {
    	super(arrayVisualizer);

    	this.setSortListName("Odd-Even Bitonic");
    	this.setRunAllSortsName("Odd-Even Bitonic Sort (By groszak1 & McDude_73)");
    	this.setRunSortName("Odd-Even Bitonic Sort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void oddEvenBitonic(int[] array, int start, int end, int gap, boolean reverse) {
    	if(start == end - gap) return;
    	if(!reverse) {
    		oddEvenBitonic(array, start, start + ((end - start) / 2), gap, true);
    		oddEvenBitonic(array, start + ((end - start) / 2), end, gap, false);
    		oddEvenMerge(array, start, end, gap, false);
    	} else {
    		oddEvenBitonic(array, start, start + ((end - start) / 2), gap, false);
    		oddEvenBitonic(array, start + ((end - start) / 2), end, gap, true);
    		oddEvenMerge(array, start, end, gap, true);
    	}

    }

    private void oddEvenMerge(int[] array, int start, int end, int gap, boolean reverse) {
    	if(start >= end - gap) return;
    	if(!reverse) {
    		if(((end - start) / gap) % 2 == 0) {
    			oddEvenMerge(array, start, end, gap * 2, false);
    			oddEvenMerge(array, start + gap, end + gap, gap * 2, false);
    		} else {
    			oddEvenMerge(array, start, end + gap, gap * 2, false);
    			oddEvenMerge(array, start + gap, end, gap * 2, false);
    		}
    		for(int i = start;i < end - gap;i += 2 * gap) {
    			Delays.sleep(1);
    			Highlights.markArray(1, i);
    			Highlights.markArray(2, i + gap);
    			if(Reads.compareValues(array[i], array[i + gap]) == 1) {
    				Writes.swap(array, i, i + gap, 1, true, false);
    			}
    		}
    	} else {
    		if(((end - start) / gap) % 2 == 0) {
    			oddEvenMerge(array, start, end, gap * 2, true);
    			oddEvenMerge(array, start + gap, end + gap, gap * 2, true);
    		} else {
    			oddEvenMerge(array, start, end + gap, gap * 2, true);
    			oddEvenMerge(array, start + gap, end, gap * 2, true);
    		}
    		for(int i = start;i < end - gap;i += 2 * gap) {
    			Delays.sleep(1);
    			Highlights.markArray(1, i);
    			Highlights.markArray(2, i + gap);
    			if(Reads.compareValues(array[i], array[i + gap]) == -1) {
    				Writes.swap(array, i, i + gap, 1, true, false);
    			}
    		}
    	}
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	oddEvenBitonic(array, 0, length, 1, false);
    }
}