package sorts.select;

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

public class SelectionSort extends Sort {
    public SelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Selection");
        this.setRunAllSortsName("Selection Sort");
        this.setRunSortName("Selection Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void selection(int[] array, int start, int end, double delay, boolean aux) {
        for (int i = start; i < end - 1; i++) {
            int lowestindex = i;

            for (int j = i + 1; j < end; j++) {
                Highlights.markArray(2, j);
                Delays.sleep(delay);

                if (Reads.compareValues(array[j], array[lowestindex]) == -1) {
                    lowestindex = j;
                    Highlights.markArray(1, lowestindex);
                    Delays.sleep(delay);
                }
            }
            if (i != lowestindex) Writes.swap(array, i, lowestindex, delay * 4, true, aux);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        selection(array, 0, length, 0.025, false);
    }
}