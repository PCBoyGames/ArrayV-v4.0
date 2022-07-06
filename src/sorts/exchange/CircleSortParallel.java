package sorts.exchange;

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

final public class CircleSortParallel extends Sort {
    public CircleSortParallel(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Circle (Parallel)");
        this.setRunAllSortsName("Parallel Circle Sort");
        this.setRunSortName("Parallel Circlesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int[] array;
    private int end;

    private volatile boolean swapped;

    private class CircleSort extends Thread {
        private int a, b;

        public CircleSort(int a, int b) {
            this.a = a;
            this.b = b;
        }
        public void run() {
            CircleSortParallel.this.circleSort(a, b);
        }
    }

    private void circleSort(int a, int b) {
        if (a >= this.end) return;

        for (int i = a, j = b-1; i < j; i++, j--)
            if (j < this.end && Reads.compareIndices(array, i, j, 1, true) > 0) {
                Writes.swap(array, i, j, 1, true, false);
                this.swapped = true;
            }

        if (b-a < 4) return;

        int m = (a+b)/2;

        CircleSort l = new CircleSort(a, m);
        CircleSort r = new CircleSort(m, b);

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

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        this.array = array;
        this.end = sortLength;
        this.swapped = true;
        int n = 1;
        for (; n < sortLength; n*=2);

        while (swapped) {
            swapped = false;
            this.circleSort(0, n);
        }
    }
}