package sorts.hybrid;

import java.util.Random;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;

/*
 *
MIT License

Copyright (c) 2021-2022 aphitorite

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

public final class PacheSort extends Sort {
    public PacheSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pache");
        this.setRunAllSortsName("Pache Sort");
        this.setRunSortName("Pachesort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    //simple average case O(n log n) comps O(n) moves sort

    private final int MIN_INSERT = 32;
    private final int MIN_HEAP   = 255;

    private BinaryInsertionSort smallSort;

    private Random rng;

    private int log2(int n) {
        return 31-Integer.numberOfLeadingZeros(n);
    }

    private int leftBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = (a+b) >>> 1;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);

            if (Reads.compareValues(val, array[m]) <= 0)
                b = m;
            else
                a = m+1;
        }
        return a;
    }
    private int rightBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = (a+b) >>> 1;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);

            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m+1;
        }
        return a;
    }

    private void blockSwap(int[] array, int a, int b, int s) {
        while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }

    private void mergeFW(int[] array, int a, int m, int b, int p) {
        int pLen = m-a, pEnd = p+pLen;
        this.blockSwap(array, a, p, pLen);

        while (p < pEnd && m < b) {
            if (Reads.compareValues(array[p], array[m]) <= 0)
                Writes.swap(array, a++, p++, 1, true, false);

            else Writes.swap(array, a++, m++, 1, true, false);
        }
        while (p < pEnd)
            Writes.swap(array, a++, p++, 1, true, false);
    }

    private class BitArray {
        private final int[] array;
        private final int pa, pb, w;

        public final int size, length;

        public BitArray(int[] array, int pa, int pb, int size, int w) {
            this.array = array;
            this.pa = pa;
            this.pb = pb;
            this.size = size;
            this.w  = w;
            this.length = size*w;
        }

        private void flipBit(int a, int b) {
            Writes.swap(array, a, b, 0.5, true, false);
        }
        private boolean getBit(int a, int b) {
            return Reads.compareIndices(array, a, b, 0, false) > 0;
        }
        private void setBit(int a, int b, boolean bit) {
            if (this.getBit(a, b) ^ bit)
                this.flipBit(a, b);
        }

        public void free() {
            int i1 = pa+length;
            for (int i = pa, j = pb; i < i1; i++, j++)
                this.setBit(i, j, false);
        }

        public void set(int idx, int uInt) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            int s = idx*w, i1 = pa+s+w;
            for (int i = pa+s, j = pb+s; i < i1; i++, j++, uInt >>= 1)
                this.setBit(i, j, (uInt & 1) == 1);

            if (uInt > 0) System.out.println("Warning: Word too large");
        }
        public int get(int idx) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            int r = 0, s = idx*w;
            for (int k = 0, i = pa+s, j = pb+s; k < w; k++, i++, j++)
                r |= (this.getBit(i, j) ? 1 : 0) << k;
            return r;
        }

        public void incr(int idx) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            int s = idx*w, i1 = pa+s+w;
            for (int i = pa+s, j = pb+s; i < i1; i++, j++) {
                this.flipBit(i, j);
                if (this.getBit(i, j)) return;
            }
            System.out.println("Warning: Integer overflow");
        }
        public void decr(int idx) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            int s = idx*w, i1 = pa+s+w;
            for (int i = pa+s, j = pb+s; i < i1; i++, j++) {
                this.flipBit(i, j);
                if (!this.getBit(i, j)) return;
            }
            System.out.println("Warning: Integer underflow");
        }
    }

    private void siftDown(int[] array, int val, int i, int p, int n) {
        while (4*i+1 < n) {
            int max = val;
            int next = i, child = 4*i+1;

            for (int j = child; j < Math.min(child+4, n); j++) {
                if (Reads.compareValues(array[p+j], max) > 0) {
                    max  = array[p+j];
                    next = j;
                }
            }
            if (next == i) break;

            Writes.write(array, p+i, max, 1, true, false);
            i = next;
        }
        Writes.write(array, p+i, val, 1, true, false);
    }
    private void optiHeapSort(int[] array, int a, int b) {
        int n = b-a;

        for (int i = (n-1)/4; i >= 0; i--)
            this.siftDown(array, array[a+i], i, a, n);

        for (int i = n-1; i > 0; i--) {
            Highlights.markArray(2, a+i);
            int t = array[a+i];
            Writes.write(array, a+i, array[a], 1, false, false);
            this.siftDown(array, t, 0, a, i);
        }
    }

    private int medianOfThree(int[] array, int a, int m, int b) {
        if (Reads.compareValues(array[m], array[a]) > 0) {
            if (Reads.compareValues(array[m], array[b]) < 0)
                return m;
            if (Reads.compareValues(array[a], array[b]) > 0)
                return a;
            else
                return b;
        }
        if (Reads.compareValues(array[m], array[b]) > 0)
            return m;
        if (Reads.compareValues(array[a], array[b]) < 0)
            return a;

        return b;
    }
    private int ninther(int[] array, int a) {
        int a1 = this.medianOfThree(array, a,   a+1, a+2);
        int m1 = this.medianOfThree(array, a+3, a+4, a+5);
        int b1 = this.medianOfThree(array, a+6, a+7, a+8);

        return this.medianOfThree(array, a1, m1, b1);
    }
    private void pivotSelect(int[] array, int a, int b) {
        if (b-a <= 256) {
            for (int i = a; i < a+9; i++)
                Writes.swap(array, i, i+this.rng.nextInt(b-i), 1, true, false);

            Writes.swap(array, a, this.ninther(array, a), 1, true, false);
        }
        else {
            for (int i = a; i < a+27; i++)
                Writes.swap(array, i, i+this.rng.nextInt(b-i), 1, true, false);

            int a1 = this.ninther(array, a);
            int m1 = this.ninther(array, a+9);
            int b1 = this.ninther(array, a+18);

            Writes.swap(array, a, this.medianOfThree(array, a1, m1, b1), 1, true, false);
        }
    }

    private int partition(int[] array, int a, int b) {
        int i = a, j = b;
        Highlights.markArray(3, a);

        do {
            do {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
            }
            while (i < j && Reads.compareValues(array[i], array[a]) < 0);

            do {
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
            }
            while (j >= i && Reads.compareValues(array[j], array[a]) > 0);

            if (i < j) Writes.swap(array, i, j, 1, false, false);
            else {
                Writes.swap(array, a, j, 1, true, false);
                return j;
            }
        }
        while (true);
    }
    private void dualQuickSelect(int[] array, int a, int b, int r1, int r2) {
        int a1 = a, b1 = b;

        while (b-a > this.MIN_INSERT) {
            this.pivotSelect(array, a, b);

            int m = this.partition(array, a, b);

            if (m > r2 && m < b1)        b1 = m;
            else if (m < r2 && m+1 > a1) a1 = m+1;
            else if (m == r2)            a1 = b1;

            if (m == r1) break;

            if (m > r1) b = m;
            else       a = m+1;
        }
        if (b-a <= this.MIN_INSERT)
            this.smallSort.customBinaryInsert(array, a, b, 0.25);

        while (b1-a1 > this.MIN_INSERT) {
            this.pivotSelect(array, a1, b1);

            int m = this.partition(array, a1, b1);

            if (m == r2) return;

            if (m > r2) b1 = m;
            else       a1 = m+1;
        }
        if (b1-a1 <= this.MIN_INSERT)
            this.smallSort.customBinaryInsert(array, a1, b1, 0.25);
    }

    private void optiLazyHeap(int[] array, int a, int b, int s) {
        for (int j = a; j < b; j += s) {
            int max = j;

            for (int i = max+1; i < Math.min(j+s, b); i++)
                if (Reads.compareIndices(array, i, max, 0.125, true) > 0)
                    max = i;

            Writes.swap(array, j, max, 1, true, false);
        }
        for (int j = b; j > a; ) {
            int k = a;

            for (int i = k+s; i < j; i += s)
                if (Reads.compareIndices(array, i, k, 0.125, true) > 0)
                    k = i;

            int k1 = --j;

            for (int i = k+1; i < Math.min(k+s, j); i++)
                if (Reads.compareIndices(array, i, k1, 0.125, true) > 0)
                    k1 = i;

            Highlights.markArray(3, j);

            if (k1 == j) {
                Writes.swap(array, k, j, 1, true, false);
            }
            else {
                Highlights.clearMark(2);

                int t = array[j];
                Writes.write(array, j, array[k], 0.5, true, false);
                Writes.write(array, k, array[k1], 0.5, true, false);
                Writes.write(array, k1, t, 0.5, true, false);
            }
        }
        Highlights.clearMark(3);
    }

    private void sortBucket(int[] array, int a, int b, int s, int val) {
        for (int i = b-1; i >= a; i--)
            if (Reads.compareIndexValue(array, i, val, 0.5, true) == 0)
                Writes.swap(array, i, --b, 0.5, true, false);

        this.optiLazyHeap(array, a, b, s);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int a = 0, b = length;

        if (length <= this.MIN_HEAP) {
            this.optiHeapSort(array, a, b);
            return;
        }

        this.smallSort = new BinaryInsertionSort(this.arrayVisualizer);
        this.rng = new Random();

        int log    = this.log2(length-1)+1;
        int pCnt   = length/(log*log);
        int bitLen = (pCnt+1)*log;

        int a1 = a+bitLen, b1 = b-bitLen;

        this.dualQuickSelect(array, a, b, a1, b1-1);
        this.optiHeapSort(array, b1, b);

        if (Reads.compareIndices(array, a1, b1-1, 1, true) < 0) {
            int a2 = a1;

            for (int i = 0; i < pCnt; i++)
                Writes.swap(array, a2, a2+this.rng.nextInt(b1-(a2++)), 1, true, false);

            this.optiHeapSort(array, a1, a2);

            BitArray cnts = new BitArray(array, a, b1, pCnt+1, log);

            for (int i = a2; i < b1; i++) {
                Highlights.markArray(3, i);
                cnts.incr(this.leftBinSearch(array, a1, a2, array[i])-a1);
            }
            Highlights.clearMark(3);

            for (int i = 1, sum = cnts.get(0); i < pCnt+1; i++) {
                sum += cnts.get(i);
                cnts.set(i, sum);
            }
            for (int i = 0, j = 0; i < pCnt; i++) {
                Highlights.markArray(3, a1+i);
                int cur = cnts.get(i);

                while (j < cur) {
                    int loc = this.leftBinSearch(array, a1+i, a2, array[a2+j])-a1;

                    if (loc == i) Writes.swap(array, a2+j, a2+(--cur), 1, true, false);

                    else {
                        cnts.decr(loc);
                        Writes.swap(array, a2+j, a2+cnts.get(loc), 1, true, false);
                    }
                }
                j = this.rightBinSearch(array, a2+j, b1, array[a1+i])-a2;
            }
            cnts.free();
            Highlights.clearMark(3);

            int j = a2;

            for (int i = 0; i < pCnt; i++) {
                Highlights.markArray(3, a1+i);
                int j1 = this.rightBinSearch(array, j, b1, array[a1+i]);
                this.sortBucket(array, j, j1, log, array[a1+i]);
                j = j1;
            }
            this.optiLazyHeap(array, j, b1, log);
            this.mergeFW(array, a1, a2, b1, a);
        }
        this.optiHeapSort(array, a, a1);
    }
}