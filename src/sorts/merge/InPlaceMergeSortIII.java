package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

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

final public class InPlaceMergeSortIII extends Sort {
    public InPlaceMergeSortIII(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("In-Place Merge III");
        this.setRunAllSortsName("In-Place Merge Sort III");
        this.setRunSortName("In-Place Merge Sort III");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void inPlaceMerge3(int[] array, int a, int m, int b) {
        if (Reads.compareValues(array[m-1], array[m]) <= 0) return;

        int i = a, j = m, k = m;

        while (Reads.compareValues(array[i], array[j]) <= 0) i++;
        j++;

        while (i < m && j < b) {
            Highlights.markArray(3, j);

            if (Reads.compareValues(array[i], array[j]) <= 0) {
                Writes.swap(array, k++, i++, 1, true, false);
                if (k == j) k = m;
            }
            else {
                IndexedRotations.cycleReverse(array, m, k, j++, 0.0625, true, false);
                k = m;
            }
        }
        Highlights.clearMark(3);
        IndexedRotations.cycleReverse(array, m, k, j, 0.0625, true, false);
        IndexedRotations.cycleReverse(array, i, m, j, 0.0625, true, false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int j = 1; j < length; j *= 2)
            for (int i = 0; i+j < length; i += 2*j)
                this.inPlaceMerge3(array, i, i+j, Math.min(i+2*j, length));
    }
}