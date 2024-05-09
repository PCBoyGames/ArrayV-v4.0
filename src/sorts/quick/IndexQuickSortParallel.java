package sorts.quick;

import java.util.Random;
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
public class IndexQuickSortParallel extends Sort {

    public IndexQuickSortParallel(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Index Quick (Parallel)");
        this.setRunAllSortsName("Parallel Index Quick Sort");
        this.setRunSortName("Parallel Index Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int[] array;
    private int[] idx;

    private class QuickSort extends Thread {
        private int a,b;
        QuickSort(int p, int r) {
            this.a = p;
            this.b = r;
        }

        public void run() {
            IndexQuickSortParallel.this.sort(a, b);
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

    private int partition(int a, int b) {
        int c0 = a, c1 = c0, i;

        Random r = new Random();
        int m = a + r.nextInt(b-a);

        Highlights.markArray(2, m);
        for (i = a; i < m; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.25);

            if (Reads.compareValues(array[i], array[m]) <= 0)
                c1++;
        }
        i++;
        c1++;
        for (; i < b; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.25);

            if (Reads.compareValues(array[i], array[m]) < 0)
                c1++;
        }

        int p = c1-1;

        for (i = a; i < m; i++) {
            Highlights.markArray(1, i);

            if (Reads.compareValues(array[i], array[m]) <= 0)
                Writes.write(idx, c0++, i, 0.25, false, true);

            else Writes.write(idx, c1++, i, 0.25, false, true);
        }
        Highlights.markArray(1, i);
        Writes.write(idx, p, i++, 0.25, false, true);

        for (; i < b; i++) {
            Highlights.markArray(1, i);

            if (Reads.compareValues(array[i], array[m]) < 0)
                Writes.write(idx, c0++, i, 0.25, false, true);

            else Writes.write(idx, c1++, i, 0.25, false, true);
        }

        this.indexSort(a, b);
        return p;
    }

    private void sort(int a, int b) {
        if (b-a < 2) return;
        int p = partition(a, b);
        QuickSort left = new QuickSort(a, p);
        QuickSort right = new QuickSort(p + 1, b);
        left.start();
        right.start();
        try {
            left.join();
            right.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.array = array;
        this.idx = Writes.createExternalArray(sortLength);
        sort(0, sortLength);
        Writes.deleteExternalArray(this.idx);

    }

}