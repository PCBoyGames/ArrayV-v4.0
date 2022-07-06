package sorts.hybrid;

import sorts.templates.Sort;
import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;

/*
 *
MIT License

Copyright (c) 2022 aphitorite

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

final public class UnstableKitaSort extends Sort {
    public UnstableKitaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Unstable Kita");
        this.setRunAllSortsName("Unstable Kita Sort");
        this.setRunSortName("Unstable Kitasort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void blockSwap(int[] array, int a, int b, int s) {
        while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
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
    private void ninther(int[] array, int a, int b) {
        int s = (b-a)/8;

        int a1 = this.medianOfThree(array, a,     a+  s, a+2*s);
        int m1 = this.medianOfThree(array, a+3*s, a+4*s, a+5*s);
        int b1 = this.medianOfThree(array, a+6*s, a+7*s, b-1);

        Writes.swap(array, a, this.medianOfThree(array, a1, m1, b1), 1, true, false);
    }
    private void medianOfMedians(int[] array, int a, int b) {
        while (b-a > 2) {
            int m = a, i = a;

            for (; i+2 < b; i += 3)
                Writes.swap(array, m++, this.medianOfThree(array, i, i+1, i+2), 1, true, false);
            while (i < b)
                Writes.swap(array, m++, i++, 1, true, false);

            b = m;
        }
    }

    private int[] partition(int[] array, int a, int b, int piv) {
        //partition -> [a][E > piv][i][E == piv][j][E < piv][b]
        //returns   -> i and j ^
        int i1 = a, i = a-1, j = b, j1 = b;

        for (;;) {
            while (++i < j) {
                int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
                if (cmp == 0) Writes.swap(array, i1++, i, 1, true, false);
                else if (cmp > 0) break;
            }
            Highlights.clearMark(2);

            while (--j > i) {
                int cmp = Reads.compareIndexValue(array, j, piv, 0.5, true);
                if (cmp == 0) Writes.swap(array, --j1, j, 1, true, false);
                else if (cmp < 0) break;
            }
            Highlights.clearMark(2);

            if (i < j) {
                Writes.swap(array, i, j, 1, true, false);
                Highlights.clearMark(2);
            }
            else {
                if (i1 == b) return new int[] {a, b};
                else if (j < i) j++;

                while (i1 > a) Writes.swap(array, --i, --i1, 1, true, false);
                while (j1 < b) Writes.swap(array, j++, j1++, 1, true, false);

                break;
            }
        }
        return new int[] {i, j};
    }
    //returns -> index above p != array[p]
    private int quickSelect(int[] array, int a, int b, int p) {
        int minInsert = 16;
        boolean badPartition = false;

        while (true) {
            int piv;

            if (badPartition) {
                this.medianOfMedians(array, a, b);
                piv = array[a];
            }
            else piv = array[this.medianOfThree(array, a, (a+b)/2, b-1)];

            int[] m = this.partition(array, a, b, piv);

            int left = m[0]-a, right = b-m[1];
            badPartition = 16*Math.min(left, right) < Math.max(left, right);

            if (p < m[0]) b = m[0];
            else if (p >= m[1]) a = m[1];
            else break;

            if (b-a <= minInsert) {
                BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
                smallSort.customBinaryInsert(array, a, b, 0.25);
                break;
            }
        }
        if (Reads.compareIndices(array, p, b-1, 1, true) == 0)
            return b;

        while (Reads.compareIndices(array, p, ++p, 1, true) == 0);

        return p;
    }

    private void mergeBW(int[] array, int a, int m, int b, int p) {
        int pLen = b-m;
        this.blockSwap(array, m, p, pLen);

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

    private boolean blockLesser(int[] array, int a, int b, int bLen) {
        int cmp = Reads.compareIndices(array, a, b, 1, true);
        return cmp < 0 || (cmp == 0 && Reads.compareIndices(array, a+bLen-1, b+bLen-1, 1, true) < 0);
    }
    private int findNext(int[] array, int a, int b, int bLen, int bsv) {
        int min = -1;

        for (int i = a; i < b; i += bLen)
            if (Reads.compareIndexValue(array, i, bsv, 1, true) > 0
            && (min == -1 || this.blockLesser(array, i, min, bLen)))
                min = i;

        return min;
    }
    private int findBuffer(int[] array, int a, int bLen, int bsv) {
        while (Reads.compareIndexValue(array, a+bLen-1, bsv, 1, true) > 0) a += bLen;
        return a;
    }

    private void blockMerge(int[] array, int a, int m, int b, int p, int t, int bLen, int bsv) {
        int i = this.findNext(array, a, m, bLen, bsv),
            j = this.findNext(array, m, b, bLen, bsv);

        int c = 0, ci = 0, cj = 0, l = 0, r = 0, k = p;
        boolean lBuf, lLeft = true, rLeft = true;

        for (; c < 2*bLen; c++) {
            if (lLeft && (!rLeft || Reads.compareValues(array[i], array[j]) <= 0)) {
                Writes.swap(array, k++, i++, 1, true, false);
                l++;

                if (++ci == bLen) {
                    i = this.findNext(array, a, m, bLen, bsv);

                    if (i == -1) lLeft = false;
                    else        ci = 0;
                }
            }
            else {
                Writes.swap(array, k++, j++, 1, true, false);
                r++;

                if (++cj == bLen) {
                    j = this.findNext(array, m, b, bLen, bsv);

                    if (j == -1) rLeft = false;
                    else        cj = 0;
                }
            }
        }

        lBuf = l >= r;
        k = lBuf ? this.findBuffer(array, a, bLen, bsv) : this.findBuffer(array, m, bLen, bsv);
        c = 0;

        do {
            if (lLeft && (!rLeft || Reads.compareValues(array[i], array[j]) <= 0)) {
                Writes.swap(array, k++, i++, 1, true, false);
                l++;

                if (c == 0) Writes.swap(array, k-1, t+(k-a)/bLen, 1, true, false);

                if (++ci == bLen) {
                    i = this.findNext(array, a, m, bLen, bsv);

                    if (i == -1) lLeft = false;
                    else        ci = 0;
                }
            }
            else {
                Writes.swap(array, k++, j++, 1, true, false);
                r++;

                if (c == 0) Writes.swap(array, k-1, t+(k-a)/bLen, 1, true, false);

                if (++cj == bLen) {
                    j = this.findNext(array, m, b, bLen, bsv);

                    if (j == -1) rLeft = false;
                    else        cj = 0;
                }
            }
            if (++c == bLen) {
                if (lBuf) l -= bLen;
                else     r -= bLen;

                lBuf = l >= r;
                k = lBuf ? this.findBuffer(array, a, bLen, bsv) : this.findBuffer(array, m, bLen, bsv);
                c = 0;
            }
        }
        while (lLeft || rLeft);

        while (l > 0) {
            k = this.findBuffer(array, a, bLen, bsv);

            this.blockSwap(array, p, k, bLen);
            Writes.swap(array, k, t+(k-a)/bLen, 1, true, false);

            l -= bLen;
            p += bLen;
        }
        while (r > 0) {
            k = this.findBuffer(array, m, bLen, bsv);

            this.blockSwap(array, p, k, bLen);
            Writes.swap(array, k, t+(k-a)/bLen, 1, true, false);

            r -= bLen;
            p += bLen;
        }
        for (k = a; k < b; k += bLen, t++)
            Writes.swap(array, k, t, 1, true, false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int a = 0, b = length;
        BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);

        if (length <= 32) {
            smallSort.customBinaryInsert(array, a, b, 0.5);
            return;
        }

        int bLen   = 1 << ((32-Integer.numberOfLeadingZeros(length-1))/2),
            tLen   = length/bLen-1,
            bufLen = 2*bLen;

        int a1 = this.quickSelect(array, a, b, a+bufLen+tLen-1), b1 = b-(b-a1)%bLen;
        int p  = a, t = a+bufLen;
        length = b1-a1;

        int bsv = array[a1-1];

        int j = 16;

        //insertion

        for (int i = a1; i < b; i += j)
            smallSort.customBinaryInsert(array, i, Math.min(i+j, b), 0.25);

        //merging w/ buffer

        for (int i; 4*j <= bufLen; j *= 4) {
            for (i = a1; i+2*j < b; i += 4*j)
                this.pingPongMerge(array, i, i+j, i+2*j, Math.min(i+3*j, b), Math.min(i+4*j, b), p);
            if (i+j < b)
                this.mergeBW(array, i, i+j, b, p);
        }
        for (; j <= bufLen; j *= 2)
            for (int i = a1; i+j < b; i += 2*j)
                this.mergeBW(array, i, i+j, Math.min(i+2*j, b), p);
        for (; j < length; j *= 2)
            for (int i = a1; i+j < b1; i += 2*j)
                this.blockMerge(array, i, i+j, Math.min(i+2*j, b1), p, t, bLen, bsv);

        //block select

        for (int i = a1; i < b1-bLen; i += bLen) {
            int min = i;

            for (int k = min+bLen; k < b1; k += bLen)
                if (this.blockLesser(array, k, min, bLen)) min = k;

            if (min > i) this.blockSwap(array, i, min, bLen);
        }

        //cleaning up

        this.mergeBW(array, a1, b1, b, p);
        smallSort.customBinaryInsert(array, a, a+bufLen+tLen, 0.25);
    }
}