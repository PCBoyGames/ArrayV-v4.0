package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 *
The MIT License (MIT)

Copyright (c) 2020-2021 aphitorite

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

public class RotateMergeSortEvenMoreParallel extends Sort {
    public RotateMergeSortEvenMoreParallel(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Rotate Merge (Even More Parallel)");
        this.setRunAllSortsName("Even More Parallel Rotate Merge Sort");
        this.setRunSortName("Even More Parallel Rotate Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4096);
        this.setBogoSort(false);
    }

    private int[] array;

    private class RotateMergeSort extends Thread {
        private int a, b;
        RotateMergeSort(int a, int b) {
            this.a = a;
            this.b = b;
        }
        public void run() {
            RotateMergeSortEvenMoreParallel.this.rotateMergeSort(this.a, this.b);
        }
    }

    private class RotateMerge extends Thread {
        private int a, m, b;
        RotateMerge(int a, int m, int b) {
            this.a = a;
            this.m = m;
            this.b = b;
        }
        public void run() {
            RotateMergeSortEvenMoreParallel.this.rotateMerge(a, m, b);
        }
    }

    private class ParallelReversal extends Thread {
        private int a, b;
        ParallelReversal(int a, int b) {
            this.a = a;
            this.b = b;
        }
        public void run() {
            RotateMergeSortEvenMoreParallel.this.parallelReversal(a, b);
        }
    }

    private class ParallelSwap extends Thread {
        private int a, b;
        ParallelSwap(int a, int b) {
            this.a = a;
            this.b = b;
        }
        public void run() {
            Writes.swap(array, a, b, 1, true, false);
        }
    }

    private void parallelReversal(int a, int b) {
        int n = (b-a)/2;
        ParallelSwap[] t = new ParallelSwap[n];

        for (int i = 0; i < n; i++)
            t[i] = new ParallelSwap(a++, --b);

        for (ParallelSwap s : t) s.start();
        try {
            for (ParallelSwap s : t) s.join();
        }
        catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void rotate(int a, int m, int b) {
        ParallelReversal l = new ParallelReversal(a, m);
        ParallelReversal r = new ParallelReversal(m, b);

        l.run();
        r.run();
        try {
            l.join();
            r.join();
        }
        catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        ParallelReversal last = new ParallelReversal(a, b);
        last.start();
        try {
            last.join();
        }
        catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private int binarySearch(int a, int b, int value, boolean left) {
        while (a < b) {
            int m = a+(b-a)/2;

            boolean comp = left ? Reads.compareValues(value, this.array[m]) <= 0
                                : Reads.compareValues(value, this.array[m]) < 0;

            if (comp) b = m;
            else     a = m+1;
        }

        return a;
    }

    private void rotateMerge(int a, int m, int b) {
        if (m-a < 1 || b-m < 1) return;

        int m1, m2, m3;

        if (m-a >= b-m) {
            m1 = a+(m-a)/2;
            m2 = this.binarySearch(m, b, this.array[m1], true);
            m3 = m1+(m2-m);
        }
        else {
            m2 = m+(b-m)/2;
            m1 = this.binarySearch(a, m, this.array[m2], false);
            m3 = (m2++)-(m-m1);
        }
        this.rotate(m1, m, m2);

        RotateMerge l = new RotateMerge(a, m1, m3);
        RotateMerge r = new RotateMerge(m3+1, m2, b);
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

    protected void rotateMergeSort(int a, int b) {
        if (b-a < 2) return;

        int m = (a+b)/2;
        RotateMergeSort l = new RotateMergeSort(a, m);
        RotateMergeSort r = new RotateMergeSort(m, b);
        l.start();
        r.start();
        try {
            l.join();
            r.join();
        }
        catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.rotateMerge(a, m, b);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.array = array;
        this.rotateMergeSort(0, length);
    }
}