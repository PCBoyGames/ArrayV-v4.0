package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
Copyright (c) 2023 thatsOven

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
*/

public class BufMerge2 extends Sort {
    public BufMerge2(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Buf Merge 2");
        this.setRunAllSortsName("thatsOven's In-Place Buffered Merge Sort II");
        this.setRunSortName("thatsOven's In-Place Buffered Merge Sort II");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private static final int RUN_SIZE = 32;

    private int sqrtn;

    private void blockSwapFW(int[] array, int a, int b, int len) {
        for(int i = 0; i < len; i++)
            Writes.swap(array, a + i, b + i, 0.5, true, false);
    }

    private void blockSwapBW(int[] array, int a, int b, int len) {
        for(int i = len - 1; i >= 0; i--)
            Writes.swap(array, a + i, b + i, 0.5, true, false);
    }

    private void insertToLeft(int[] array, int from, int to) {
        Highlights.clearAllMarks();

        int tmp = array[from];
        for (int i = from - 1; i >= to; i--)
            Writes.write(array, i + 1, array[i], 0.25, true, false);
        Writes.write(array, to, tmp, 0.25, true, false);
    }

    private void insertToRight(int[] array, int from, int to) {
        Highlights.clearAllMarks();

        int tmp = array[from];
        for (int i = from; i < to; i++)
            Writes.write(array, i, array[i + 1], 0.25, true, false);
        Writes.write(array, to, tmp, 0.25, true, false);
    }

    public void rotate(int[] array, int a, int m, int b) {
        int rl = b - m,
            ll = m - a;

        while (rl > 1 && ll > 1) {
            if (rl < ll) {
                blockSwapFW(array, a, m, rl);
                a  += rl;
                ll -= rl;
            } else {
                b  -= ll;
                rl -= ll;
                blockSwapBW(array, a, b, ll);
            }
        }

        if      (rl == 1) insertToLeft( array, m, a);
        else if (ll == 1) insertToRight(array, a, b - 1);
    }

    private int binarySearch(int[] array, int a, int b, int value, boolean left) {
        Highlights.clearAllMarks();

        while (a < b) {
            int m = a + (b - a) / 2;

            int cmp = Reads.compareIndexValue(array, m, value, 0.25, true);
            if (left ? cmp >= 0 : cmp > 0)
                 b = m;
            else a = m + 1;
        }

        return a;
    }

    private void insertSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++)
            if (Reads.compareIndices(array, i, i - 1, 0, false) < 0)
                insertToLeft(array, i, binarySearch(array, a, i, array[i], false));
    }

    private void sortRuns(int[] array, int a, int b) {
        int i;
        for (i = a; i < b - RUN_SIZE; i += RUN_SIZE)
            insertSort(array, i, i + RUN_SIZE);

        if (i < b) insertSort(array, i, b);
    }

    private void mergeInPlaceBW(int[] array, int a, int m, int b) {
        int s = b - 1,
            l = m - 1;

        while (s > l && l >= a) {
            if (Reads.compareIndices(array, l, s, 0, false) > 0) {
                int p = this.binarySearch(array, a, l, array[s], false);
                rotate(array, p, l + 1, s + 1);
                s -= l + 1 - p;
                l = p - 1;
            } else s--;
        }
    }

    private void mergeWithStatBufferBW(int[] array, int a0, int b0, int a1, int b1, int buf) {
        int l = b0  - 1,
            r = b1  - 1,
            o = buf - 1;

        while (l >= a0 && r >= a1) {
            if (Reads.compareIndices(array, r, l, 0.5, true) >= 0)
                 Writes.swap(array, o--, r--, 0.5, true, false);
            else Writes.swap(array, o--, l--, 0.5, true, false);
        }

        while (r >= a1)
            Writes.swap(array, o--, r--, 0.5, true, false);

        while (l >= a0)
            Writes.swap(array, o--, l--, 0.5, true, false);
    }

    private void gallopMerge(int[] array, int a0, int b0, int a1, int b1, int buf) {
        int l = b0  - 1,
            r = b1  - 1,
            o = buf - 1;

        while (l >= a0 && r >= a1) {
            if (Reads.compareIndices(array, l, r, 1, true) > 0) {
                int k = binarySearch(array, a0, l, array[r], false);
                while (l >= k)
                    Writes.swap(array, l--, o--, 1, true, false);
            }

            Writes.swap(array, r--, o--, 1, true, false);
        }

        while (r >= a1)
            Writes.swap(array, o--, r--, 0.5, true, false);

        while (l >= a0)
            Writes.swap(array, o--, l--, 0.5, true, false);
    }

    private void mergeWithScrollingBufferFW(int[] array, int a, int m, int b) {
        int o = a - (m - a),
            l = a,
            r = m;

        while (l < m && r < b) {
            if (Reads.compareIndices(array, l, r, 0.5, true) <= 0)
                 Writes.swap(array, o++, l++, 0.5, true, false);
            else Writes.swap(array, o++, r++, 0.5, true, false);
        }

        while (l < m)
            Writes.swap(array, o++, l++, 0.5, true, false);

        while (r < b)
            Writes.swap(array, o++, r++, 0.5, true, false);
    }

    private void mergeWithScrollingBufferBW(int[] array, int a, int m, int b) {
        int l = m - 1,
            r = b - 1,
            o = r + m - a;

        while (r >= m && l >= a) {
            if (Reads.compareIndices(array, r, l, 0.5, true) >= 0)
                 Writes.swap(array, o--, r--, 0.5, true, false);
            else Writes.swap(array, o--, l--, 0.5, true, false);
        }

        while (r >= m)
            Writes.swap(array, o--, r--, 0.5, true, false);

        while (l >= a)
            Writes.swap(array, o--, l--, 0.5, true, false);
    }

    private void buildFW(int[] array, int a, int b) {
        int s = a,
            e = b,
            r = RUN_SIZE;

        while (r < b - a) {
            int twoR = 2 * r, i;
            for (i = s; i < e - twoR; i += twoR)
                mergeWithScrollingBufferFW(array, i, i + r, i + twoR);

            if (i + r < e)
                mergeWithScrollingBufferFW(array, i, i + r, e);

            s -= r;
            e -= r;
            r = twoR;
        }
    }

    private void buildBW(int[] array, int a, int b) {
        int s = a,
            e = b,
            r = RUN_SIZE;

        while (r < b - a) {
            int twoR = 2 * r, i;
            for (i = e; i >= s + twoR; i -= twoR)
                mergeWithScrollingBufferBW(array, i - twoR, i - r, i);

            if (i - r >= s)
                mergeWithScrollingBufferBW(array, s, i - r, i);

            s += r;
            e += r;
            r = twoR;
        }
    }

    private int sortBuf(int[] array, int a, int b) {
        int n = b - a;

        if (n <= this.sqrtn) {
            insertSort(array, a, b);
            return -1;
        }

        int h = n / 2 - (n & 1);
        a += RUN_SIZE;

        sortRuns(array, a, a + h);
        buildBW(array, a, a + h);

        return a + h - RUN_SIZE;
    }

    private void sort(int[] array, int a, int b) {
        int n = b - a, h;

        if (n <= RUN_SIZE) {
            insertSort(array, a, b);
            return;
        }

        int sqrtn = 1;
        for (; sqrtn * sqrtn < n; sqrtn *= 2);
        this.sqrtn = sqrtn;

        int gallop = n / (32 - Integer.numberOfLeadingZeros(n));

        h = n / 2 + (n & 1) - RUN_SIZE;
        b -= RUN_SIZE;

        sortRuns(array, a + h, b);
        buildFW(array, a + h, b);

        b += RUN_SIZE;

        int s = a + h + RUN_SIZE;
        while (true) {
            int p = this.sortBuf(array, s, b);
            if (p == -1) {
                mergeInPlaceBW(array, a, s, b);
                return;
            }

            if (b - p > gallop)
                 mergeWithStatBufferBW(array, a, s, p, b, p);
            else gallopMerge(array, a, s, p, b, p);
            s = p;
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.sort(array, 0, length);
    }
}
