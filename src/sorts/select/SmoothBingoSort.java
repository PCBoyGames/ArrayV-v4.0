package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2021 Distray

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

final public class SmoothBingoSort extends Sort {
    public SmoothBingoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Smooth Bingo");
        this.setRunAllSortsName("Smooth Bingo Sort");
        this.setRunSortName("Smooth Bingo Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for(int i=length-1; i>0;) {
            int seekIndex = 0, equalSize = 0;
            for(int j=1; j<=i; j++) {
                Highlights.markArray(1, j);
                Delays.sleep(0.03);
                int comp = Reads.compareValues(array[seekIndex], array[j]);
                if(comp == -1) {
                    Writes.swap(array, j, ++seekIndex, 0.2, true, false);
                    equalSize = 0;
                } else if(comp == 0) {
                    Writes.swap(array, j, ++seekIndex, 0.2, true, false);
                    equalSize++;
                }
            }
            if(seekIndex == i)
                break;

            for(int k = equalSize + 1; k > 0; k--) {
                Writes.swap(array, i--, seekIndex--, 1, true, false);
            }
        }
    }
}