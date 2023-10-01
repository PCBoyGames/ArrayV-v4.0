package sorts.hybrid;

import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;
import main.ArrayVisualizer;
import utils.IndexedRotations;

/*
 *
MIT License

Copyright (c) 2020-2022 aphitorite

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

public class KotaSort extends Sort {
    public KotaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Kota");
        this.setRunAllSortsName("Kota Sort");
        this.setRunSortName("Kotasort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int tLenCalc(int n, int bLen) {
        int n1 = n-2*bLen;
        int a = 0, b = 2*bLen;

        while (a < b) {
            int m = (a+b)/2;

            if (n1-m < (m+3)*bLen) b = m;
            else                  a = m+1;
        }
        return a;
    }

    private void multiSwap(int[] array, int a, int b, int s) {
        while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }
    private void rotate(int[] array, int a, int m, int b) {
        IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
    }

    private int leftBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a+(b-a)/2;
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
            int m = a+(b-a)/2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);

            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m+1;
        }
        return a;
    }

    private int findKeysBW(int[] array, int a, int b, int nKeys) {
        int f = 1, p = b-f;

        for (int i = p; i > a && f < nKeys; i--) {
            Highlights.markArray(1, i);
            Delays.sleep(0.5);

            int loc = this.leftBinSearch(array, p, p+f, array[i-1])-p;

            if (loc == f || Reads.compareValues(array[i-1], array[p+loc]) < 0) {
                this.rotate(array, i, p, p+(f++));
                p = i-1;
                this.rotate(array, i-1, i, p+loc+1);
            }
            Highlights.clearMark(2);
        }
        this.rotate(array, p, p+f, b);

        return f;
    }

    private void mergeBW(int[] array, int a, int m, int b, int p) {
        int pLen = b-m;
        this.multiSwap(array, m, p, pLen);

        int i = pLen-1, j = m-1, k = b-1;

        while (i >= 0 && j >= a) {
            if (Reads.compareValues(array[p+i], array[j]) >= 0)
                Writes.swap(array, k--, p+(i--), 1, true, false);
            else
                Writes.swap(array, k--, j--, 1, true, false);
        }
        while (i >= 0) Writes.swap(array, k--, p+(i--), 1, true, false);
    }

    private void mergeTo(int[] array, int a, int m, int b, int p) {
        int i = a, j = m;

        while (i < m && j < b) {
            if (Reads.compareValues(array[i], array[j]) <= 0)
                Writes.swap(array, p++, i++, 1, true, false);
            else
                Writes.swap(array, p++, j++, 1, true, false);
        }
        while (i < m) Writes.swap(array, p++, i++, 1, true, false);
        while (j < b) Writes.swap(array, p++, j++, 1, true, false);
    }
    private void pingPongMerge(int[] array, int a, int m1, int m, int m2, int b, int p) {
        int p1 = p+m-a, pEnd = p+b-a;

        this.mergeTo(array, a, m1, m, p);
        this.mergeTo(array, m, m2, b, p1);
        this.mergeTo(array, p, p1, pEnd, a);
    }

    private void inPlaceMergeBW(int[] array, int a, int m, int b) {
        while (b > m && m > a) {
            int i = this.rightBinSearch(array, a, m, array[b-1]);

            this.rotate(array, i, m, b);

            int t = m-i;
            m = i;
            b -= t+1;

            if (m == a) break;

            b = this.leftBinSearch(array, m, b, array[m-1]);
        }
    }

    private int selectMin(int[] array, int a, int b, int bLen) {
        int min = a;

        for (int i = min+bLen; i < b; i += bLen)
            if (Reads.compareValues(array[i], array[min]) < 0)
                min = i;

        return min;
    }
    private void blockSelect(int[] array, int a, int b, int t, int bLen) {
        while (a < b) {
            int min = this.selectMin(array, a, b, bLen);

            if (min != a) this.multiSwap(array, a, min, bLen);
            Writes.swap(array, a, t++, 10, true, false);

            a += bLen;
        }
    }
    private void blockMerge(int[] array, int a, int m, int b, int t, int p, int bLen) {
        int c = 0, tp = t;

        int i = a, j = m, k = p;
        int l = 0, r = 0;

        while (c++ < 2*bLen) { //merge 2 blocks into buffer to create 2 buffers
            if (Reads.compareValues(array[i], array[j]) <= 0) {
                Writes.swap(array, k++, i++, 1, true, false);
                l++;
            }
            else {
                Writes.swap(array, k++, j++, 1, true, false);
                r++;
            }
        }

        boolean left = l >= r;
        k = left ? i-l : j-r;

        c = 0;

        do {
            if (i < m && (j == b || Reads.compareValues(array[i], array[j]) <= 0)) {
                Writes.swap(array, k++, i++, 1, true, false);
                l++;
            }
            else {
                Writes.swap(array, k++, j++, 1, true, false);
                r++;
            }
            if (++c == bLen) { //change buffer after every block
                Writes.swap(array, k-bLen, tp++, 10, true, false);

                if (left) l -= bLen;
                else     r -= bLen;

                left = l >= r;
                k = left ? i-l : j-r;

                c = 0;
            }
        }
        while (i < m || j < b);

        int b1 = b-c;

        this.multiSwap(array, k-c, b1, c); //swap remainder to end (r buffer)
        r -= c;

        //l and r buffers are divisible by bLen
        this.multiSwap(array, m-l, a, l);    //swap l buffer to front
        this.multiSwap(array, b1-r, a+l, r); //swap r buffer to front
        this.multiSwap(array, a, p, 2*bLen); //swap first merged elements to correct position in front

        this.blockSelect(array, a+2*bLen, b1, t, bLen);
    }
    private void blockMergeNoBuf(int[] array, int a, int m, int b, int t, int bLen) { //from wiki sort
        for (int i = a+bLen, j = t; i < m; i += bLen, j++) //tag blocks
            Writes.swap(array, i, j, 10, true, false);

        int i = a+bLen, b1 = b-(b-m)%bLen;

        while (i < m && m < b1) {
            if (Reads.compareValues(array[i-1], array[m+bLen-1]) > 0) {
                this.multiSwap(array, i, m, bLen);
                this.inPlaceMergeBW(array, a, i, i+bLen);

                m += bLen;
            }
            else {
                int min = this.selectMin(array, i, m, bLen);

                if (min > i) this.multiSwap(array, i, min, bLen);
                Writes.swap(array, t++, i, 1, true, false);
            }
            i += bLen;
        }
        if (i < m) {
            do {
                int min = this.selectMin(array, i, m, bLen);

                if (min > i) this.multiSwap(array, i, min, bLen);
                Writes.swap(array, t++, i, 1, true, false);
                i += bLen;
            }
            while (i < m);
        }
        else {
            while (m < b1 && Reads.compareValues(array[m-1], array[m]) > 0) {
                this.inPlaceMergeBW(array, a, m, m+bLen);
                m += bLen;
            }
        }
        this.inPlaceMergeBW(array, a, b1, b);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int a = 0, b = length;
        BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);

        if (length <= 32) {
            smallSort.customBinaryInsert(array, a, b, 0.5);
            return;
        }

        int bLen   = 1 << ((32-Integer.numberOfLeadingZeros(length-1))/2), //pow of 2 >= sqrt n
            tLen   = this.tLenCalc(length, bLen),
            bufLen = 2*bLen;

        int j = 16;

        int keys = this.findKeysBW(array, a, b, bufLen+tLen);

        if (keys == 1) return;

        else if (keys <= 8) { //strat 3: lazy stable
            for (int i = a; i < b; i += j)
                smallSort.customBinaryInsert(array, i, Math.min(i+j, b), 0.25);

            for (; j < length; j *= 2)
                for (int i = a; i+j < b; i += 2*j)
                    this.inPlaceMergeBW(array, i, i+j, Math.min(i+2*j, b));

            return;
        }

        length -= keys;

        if (keys < bufLen+tLen) {
            while (bufLen > 2*(keys-bufLen)) bufLen /= 2;

            bLen = bufLen/2;
            tLen = keys-bufLen;
        }

        int b1 = b-keys, t = b1, p = b1+tLen;

        //insertion

        for (int i = a; i < b1; i += j)
            smallSort.customBinaryInsert(array, i, Math.min(i+j, b1), 0.25);

        //merging w/ buffer

        for (int i; 4*j <= bufLen; j *= 4) {
            for (i = a; i+2*j < b1; i += 4*j)
                this.pingPongMerge(array, i, i+j, i+2*j, Math.min(i+3*j, b1), Math.min(i+4*j, b1), p);
            if (i+j < b1)
                this.mergeBW(array, i, i+j, b1, p);
        }

        for (; j <= bufLen; j *= 2)
            for (int i = a; i+j < b1; i += 2*j)
                this.mergeBW(array, i, i+j, Math.min(i+2*j, b1), p);

        //block merge

        int limit = bLen*(tLen+3);

        for (int i; j < length && Math.min(2*j, length) < limit; j *= 2) {
            for (i = a; i+j+bufLen < b1; i += 2*j)
                this.blockMerge(array, i, i+j, Math.min(i+2*j, b1), t, p, bLen);
            if (i+j < b1)
                this.mergeBW(array, i, i+j, b1, p);
        }

        smallSort.customBinaryInsert(array, p, b, 0.25);

        //strat 2

        if (bufLen <= tLen) bufLen *= 2;
        bLen = 2*j/bufLen;

        for (int i; j < length; j *= 2, bLen *= 2) {
            for (i = a; i+j+2*bLen < b1; i += 2*j)
                this.blockMergeNoBuf(array, i, i+j, Math.min(i+2*j, b1), t, bLen);
            if (i+j < b1)
                this.inPlaceMergeBW(array, i, i+j, b1);
        }

        this.inPlaceMergeBW(array, a, b1, b);
    }
}