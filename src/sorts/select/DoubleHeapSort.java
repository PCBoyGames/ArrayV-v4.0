package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

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

final public class DoubleHeapSort extends Sort {
    public DoubleHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Double Heap");
        this.setRunAllSortsName("Double Heap Sort");
        this.setRunSortName("Double Heapsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int[] array;
    private int e;

    private class MaxSiftDown extends Thread {
        private int i, n;

        MaxSiftDown(int i, int n) {
            this.i = i;
            this.n = n;
        }
        public void run() {
            DoubleHeapSort.this.maxSiftDown(i, n);
        }
    }
    private class MinSiftDown extends Thread {
        private int i, n;

        MinSiftDown(int i, int n) {
            this.i = i;
            this.n = n;
        }
        public void run() {
            DoubleHeapSort.this.minSiftDown(i, n);
        }
    }

    private class MaxHeapify extends Thread {
        private int n;

        MaxHeapify(int n) {
            this.n = n;
        }
        public void run() {
            DoubleHeapSort.this.maxHeapify(n);
        }
    }
    private class MinHeapify extends Thread {
        private int n;

        MinHeapify(int n) {
            this.n = n;
        }
        public void run() {
            DoubleHeapSort.this.minHeapify(n);
        }
    }

    private class MaxHeapSort extends Thread {
        private int n;

        MaxHeapSort(int n) {
            this.n = n;
        }
        public void run() {
            DoubleHeapSort.this.maxHeapSort(n);
        }
    }
    private class MinHeapSort extends Thread {
        private int n;

        MinHeapSort(int n) {
            this.n = n;
        }
        public void run() {
            DoubleHeapSort.this.minHeapSort(n);
        }
    }

    private void run(Thread l, Thread r) {
        l.start();
        r.start();

        try {
            l.join();
            r.join();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void maxSiftDown(int i, int n) {
        while(2*i+1 < n) {
            int l = 2*i+1, r = l+1;
            int max = r == n ? l : (Reads.compareValues(array[r], array[l]) > 0 ? r : l);

            if(Reads.compareValues(array[max], array[i]) > 0) {
                Writes.swap(array, max, i, 1, true, false);
                i = max;
            }
            else return;
        }
    }
    private void minSiftDown(int i, int n) {
        while(2*i+1 < n) {
            int l = 2*i+1, r = l+1;
            int min = r == n ? l : (Reads.compareValues(array[(e-1)-r], array[(e-1)-l]) < 0 ? r : l);

            if(Reads.compareValues(array[(e-1)-min], array[(e-1)-i]) < 0) {
                Writes.swap(array, (e-1)-min, (e-1)-i, 1, true, false);
                i = min;
            }
            else return;
        }
    }

    private void maxHeapify(int n) {
        for(int i = (n-1)/2; i >= 0; i--)
            this.maxSiftDown(i, n);
    }
    private void minHeapify(int n) {
        for(int i = (n-1)/2; i >= 0; i--)
            this.minSiftDown(i, n);
    }

    private void maxHeapSort(int n) {
        while(--n > 0) {
            Writes.swap(array, 0, n, 1, true, false);
            this.maxSiftDown(0, n);
        }
    }
    private void minHeapSort(int n) {
        while(--n > 0) {
            Writes.swap(array, e-1, e-1-n, 1, true, false);
            this.minSiftDown(0, n);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.array = array;
        this.e = length;

        int m = (length+1)/2;
        this.run(new MaxHeapify(m), new MinHeapify(length-m));

        while(Reads.compareValues(array[0], array[length-1]) > 0) {
            Writes.swap(array, 0, length-1, 1, true, false);
            this.run(new MaxSiftDown(0, m), new MinSiftDown(0, length-m));
        }
        this.run(new MaxHeapSort(m), new MinHeapSort(length-m));
    }
}