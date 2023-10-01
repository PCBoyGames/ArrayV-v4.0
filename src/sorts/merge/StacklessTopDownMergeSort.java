package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
MIT License

Copyright (c) 2020-2021 aphitorite

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

public class StacklessTopDownMergeSort extends Sort {
    public StacklessTopDownMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stackless Top-Down Merge");
        this.setRunAllSortsName("Stackless Top-Down Merge Sort");
        this.setRunSortName("Stackless Top-Down Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void merge(int[] array, int[] tmp, int a, int m, int b) {
        int s = b-m;

        Writes.arraycopy(array, m, tmp, 0, s, 1, true, true);

        int i = s-1, j = m-1;

        while (i >= 0 && j >= a) {
            Highlights.markArray(2, j);

            if (Reads.compareValues(tmp[i], array[j]) >= 0)
                Writes.write(array, --b, tmp[i--], 1, true, false);
            else
                Writes.write(array, --b, array[j--], 1, true, false);
        }
        Highlights.clearMark(2);
        while (i >= 0) Writes.write(array, --b, tmp[i--], 1, true, false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int n = length;
        int[] tmp = Writes.createExternalArray(n/2);

        for (int c = 2, i = 1; i < n; i++, c++)
            for (int j = 1; (c/j & 1) == 0; j <<= 1)
                this.merge(array, tmp, i+1-(j<<1), i+1-j, i+1);

        int j;
        for (j = 1; (n/j & 1) == 0; j <<= 1);
        int k = j;
        for (j <<= 1; n/j > 0; j <<= 1)
            if ((n/j & 1) == 1) {
                this.merge(array, tmp, n-k-j, n-k, n);
                k += j;
            }
    }
}