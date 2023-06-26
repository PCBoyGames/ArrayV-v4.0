package sorts.hybrid;

import java.util.Random;
import static java.lang.Math.sqrt;
import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;

import main.ArrayVisualizer;

/*
 *
MIT License

Copyright (c) 2021 aphitorite

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

final public class QuickHeapSort extends Sort {
    public QuickHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Quick Heap");
        this.setRunAllSortsName("Quick Heapsort");
        this.setRunSortName("Quick Heapsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    //implements "improved quick heap sort" from https://arxiv.org/pdf/1209.4214.pdf

    private void insertTo(int[] array, int a, int b, int t) {
        Highlights.clearMark(2);
        while (a > b) Writes.write(array, a, array[--a], 0.5, true, false);
        Writes.write(array, b, t, 0.5, true, false);
    }

    private int binSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = (a+b)/2;

            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m+1;
        }
        return a;
    }

    private int sqrtMedian(int[] array, int a, int b) {
        int n = b-a, cnt = (int)sqrt(n);
        cnt -= 1-cnt%2;
        int p = a, s = n/cnt;

        Random rand = new Random();
        int r = rand.nextInt(s);

        for (int i = a+r; cnt > 0; cnt--, p++, i += s) {
            int t = array[i];
            Writes.write(array, i, array[p], 0.5, true, false);
            this.insertTo(array, p, this.binSearch(array, a, p, t), t);
        }
        return p;
    }

    private int partition(int[] array, int a, int b) {
        int m = this.sqrtMedian(array, a, b);
        int p = (a+m)/2, i = m-1, j = b;
        Highlights.markArray(3, p);

        while (true) {
            do {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
            }
            while (i < j && Reads.compareIndices(array, i, p, 0, false) == 1);

            do {
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
            }
            while (j >= i && Reads.compareIndices(array, j, p, 0, false) == -1);

            if (i < j) Writes.swap(array, i, j, 1, true, false);
            else {
                while (a <= p) Writes.swap(array, --i, a++, 1, true, false);
                return i;
            }
        }
    }

    private void siftDown(int[] array, boolean max, int p, int n, int r) {
        int t = array[p+r];
        int cmp = max ? 1 : -1;

        while (2*r+2 < n) {
            int nxt = p+2*r+1;
            nxt = (Reads.compareValues(array[nxt+1], array[nxt]) == cmp) ? nxt+1 : nxt;

            if (Reads.compareValues(array[nxt], t) == cmp) {
                Writes.write(array, p+r, array[nxt], 0.5, true, false);
                r = nxt-p;
            }
            else {
                Writes.write(array, p+r, t, 0.5, true, false);
                return;
            }
        }
        int nxt = p+2*r+1;

        if (nxt-p < n && Reads.compareValues(array[nxt], t) == cmp) {
            Writes.write(array, p+r, array[nxt], 0.5, true, false);
            r = nxt-p;
        }
        Writes.write(array, p+r, t, 0.5, true, false);
    }

    private void siftDownEasy(int[] array, boolean max, int p, int n, int m, int t) {
        int r = 0;
        int cmp = max ? 1 : -1;

        while (2*r+1 < m) {
            int nxt = p+2*r+1;
            nxt = (Reads.compareValues(array[nxt+1], array[nxt]) == cmp) ? nxt+1 : nxt;

            Writes.write(array, p+r, array[nxt], 0.5, true, false);
            r = nxt-p;
        }
        int nxt = p+2*r+1;

        if (nxt-p < n) {
            Writes.write(array, p+r, array[nxt], 0.5, true, false);
            r = nxt-p;
            if (r+1 < n) Writes.write(array, p+(r++), array[p+r], 0.5, true, false);
        }
        Writes.write(array, p+r, t, 0.5, true, false);
    }

    private void heapSort(int[] array, boolean max, int a, int b, int p) {
        int n = b-a, m = (n/4)*2+1;
        int cmp = max ? 1 : -1;

        for (int i = (n-1)/2; i >= 0; i--)
            this.siftDown(array, max, a, n, i);

        for (int i = a+m+1; i < b; i += 2)
            if (Reads.compareIndices(array, i, i-1, 1, true) == cmp)
                Writes.swap(array, i-1, i, 1, true, false);

        if (max) {
            for (int i = 0; i < n; i++) {
                int t = array[--p];
                Highlights.markArray(2, p);
                Writes.write(array, p, array[a], 1, false, false);
                this.siftDownEasy(array, max, a, n, m, t);
            }
        }
        else {
            for (int i = 0; i < n; i++) {
                int t = array[p];
                Highlights.markArray(2, p);
                Writes.write(array, p++, array[a], 1, false, false);
                this.siftDownEasy(array, max, a, n, m, t);
            }
        }
    }

    private void quickHeapSort(int[] array, int a, int b) {
        int start = a, end = b;

        while (end-start > 32) {
            int p = this.partition(array, start, end);
            Highlights.clearAllMarks();

            int left  = p-start;
            int right = end-(p+1);

            if (left <= right) {
                this.heapSort(array, true, start, p, end);
                end -= left;
                Writes.swap(array, --end, p, 1, true, false);
            }
            else {
                this.heapSort(array, false, p+1, end, start);
                start += right;
                Writes.swap(array, start++, p, 1, true, false);
            }
        }
        BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
        smallSort.customBinaryInsert(array, start, end, 0.25);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.quickHeapSort(array, 0, length);
    }
}