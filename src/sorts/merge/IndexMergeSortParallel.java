package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
The MIT License (MIT)

Copyright (c) 2021 aphitorite
Copyright (c) 2021 Yuri-chan

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

/**
 * @author aphitorite
 * @author Yuri-chan
 *
 */
public class IndexMergeSortParallel extends Sort {

    public IndexMergeSortParallel(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Index Merge (Parallel)");
        this.setRunAllSortsName("Parallel Index Merge Sort");
        this.setRunSortName("Parallel Index Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int[] array;
    private int[] idx;

    private class MergeSort extends Thread {
        private int a, b;
        MergeSort(int a, int b) {
            this.a = a;
            this.b = b;
        }
        public void run() {
            IndexMergeSortParallel.this.mergeSort(a, b);
        }
    }

    private void indexSort(int a, int b) {
        while (a < b) {
            Highlights.markArray(2, a);

            if (Reads.compareOriginalValues(a, idx[a]) != 0) {
                int t = array[a];
                int i = a, nxt = idx[a];

                do {
                    Writes.write(array, i, array[nxt], 0, true, false);
                    Writes.write(idx, i, i, 0.5, false, true);

                    i = nxt;
                    nxt = idx[nxt];
                }
                while (Reads.compareOriginalValues(nxt, a) != 0);

                Writes.write(array, i, t, 0, true, false);
                Writes.write(idx, i, i, 0.5, false, true);
            }
            a++;
        }
    }

    private void merge(int a, int m, int b) {
        int i = a, j = m, c = a;
        Highlights.clearAllMarks();

        while (i < m && j < b) {
            if (Reads.compareValues(array[i], array[j]) <= 0) {
                Highlights.markArray(1, i);
                Writes.write(idx, c++, i++, 0.5, false, true);
            }
            else {
                Highlights.markArray(2, j);
                Writes.write(idx, c++, j++, 0.5, false, true);
            }
        }

        while (i < m) {
            Highlights.markArray(1, i);
            Writes.write(idx, c++, i++, 0.5, false, true);
        }
        while (j < b) {
            Highlights.markArray(2, j);
            Writes.write(idx, c++, j++, 0.5, false, true);
        }

        this.indexSort(a, b);
    }

    private void mergeSort(int a, int b) {
        if (b - a < 2) return;
        int m = a + (b - a) / 2;
        MergeSort left  = new MergeSort(a, m);
        MergeSort right = new MergeSort(m, b);
        left.start();
        right.start();

        try {
            left.join();
            right.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        merge(a, m, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        this.array = array;
        this.idx = Writes.createExternalArray(sortLength);
        mergeSort(0, sortLength);
        Writes.deleteExternalArray(this.idx);

    }

}
