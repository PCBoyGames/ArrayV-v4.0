package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*
 *
MIT License

Copyright (c) 2019 w0rthy

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

/**
 * Boxosort is a bidirectional, buffed version of Bouosort.
 */
public class BoxoSort extends BogoSorting {
    public BoxoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Boxo");
        this.setRunAllSortsName("Boxo Sort");
        this.setRunSortName("Boxosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(100);
        this.setBogoSort(true);
    }

    private int selectiveSwap(int[] array, int length, boolean k) {
    	if (length > 1 && ((!k)^this.isArraySorted(array, length)))
    		return length+1;
    	int rand = randInt(0, length),
    		rand2 = randInt(rand, length);
    	while(k == (Reads.compareValues(array[rand], array[rand2]) == 1)) {
    		Writes.swap(array, rand, rand2, 1, true, false);
    		rand = randInt(0, length);
    	    rand2 = randInt(rand, length);
    	}
    	Writes.reversal(array, 0, rand2, 1, true, false);
    	return rand2;
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	boolean d = true;
    	int k = length;
        while(!this.isArraySorted(array, length)) {
        	k = this.selectiveSwap(array, k, d);
        	d=!d;
        	if (k <= 1 || k > length)
        		k = length;
        }
    }
}
