package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

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

final public class BadBubbleSort extends Sort {
    public BadBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bad Bubble");
        this.setRunAllSortsName("Bad Bubble Sort");
        this.setRunSortName("Bad Bubble Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int Breaks = 0;
        for(int i = length - 1; i > 0; i--) {
            boolean sorted = true;
            int maxBreaks = 0;
            for(int j = 0; j < i; j++) {
                Highlights.markArray(1, j);
                Highlights.markArray(2, j + 1);
                Delays.sleep(0.025);
                if(Reads.compareValues(array[j], array[j + 1]) == 1){
                    Writes.swap(array, j, j + 1, 0.075, true, false);
                    sorted = false;
                    Breaks = 0;
                } else if(maxBreaks == 0) {
                    maxBreaks = Breaks;
                    if(Reads.compareValues(array[j], array[i]) == -1)
                        Breaks=Breaks*2+1;
                    else if(Reads.compareValues(array[j], array[j + 2]) == 0)
                        Breaks+=2;
                    else
                        Breaks++;
                    j = -1;
                } else if(maxBreaks > 0) {
                    Breaks++;
                    maxBreaks--;
                }
            }
            if(sorted) break;
        }
    }
}