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
 * Bogosort randomly shuffles the array until it is sorted.
 */
public final class OmegaOmegaBogoSort extends BogoSorting {
    public OmegaOmegaBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Omega Omega Bogo");
        this.setRunAllSortsName("Omega Omega Bogo Sort");
        this.setRunSortName("\u03A9 \u03A9 Bogosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(true);
    }

    private void omegaOmegaBogo(int[] array, int start, int end) {
        if(end-start == 1) {
            Writes.swap(array, start, end, 1, true, false);
            return;
        }
        if(start < end) {
            int i, k, satisfied;
            do {
                satisfied = 0;
                for(int j = start; j < end; j++) {
                    i = randInt(j, end);
                    k = randInt(j, end);
                    if(Reads.compareValues(array[j],array[i]) == 1) {
                        this.omegaOmegaBogo(array, j, i);
                        satisfied++;
                    } else {
                        Writes.swap(array, j, k, 1, true, false);
                    }
                }
            } while(satisfied > 0);
            this.omegaOmegaBogo(array, start, end-1);
            this.omegaOmegaBogo(array, start+1, end-1);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while(!this.isArraySorted(array, length))
            omegaOmegaBogo(array, 0, length);
    }
}
