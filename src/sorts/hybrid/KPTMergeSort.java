package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * Practical In-Place Mergesort
 * J. Katajainen, T. Pasanen, J. Teuhola
 * 1996
 *
 * Implemented and optimized by thatsOven
 */

public class KPTMergeSort extends Sort {
    public KPTMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("KPT Merge");
        this.setRunAllSortsName("KPT MergeSort");
        this.setRunSortName("KPT MergeSort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int MIN_SMALL_BLOCK = 32,
                             RUN_SIZE = 32,
                                    K = 4;

    private int[] heap, ptrs;
    private int   smallBlock, sqrtn;

    private void blockSwapFW(int[] array, int a, int b, int len) {
        for (int i = 0; i < len; i++)
            Writes.swap(array, a + i, b + i, 0.5, true, false);
    }

    private void blockSwapBW(int[] array, int a, int b, int len) {
        for (int i = len - 1; i >= 0; i--)
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

    // the authors didn't like in-place merging, but i'm sorry, it's just better in some cases
    private void mergeInPlace(int[] array, int a, int m, int b) {
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

    // they call it binary merge, but it's confusing for me
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

    private void simpleMerge(int[] array, int a, int m, int b, int buf) {
        int rl = b - m;
        blockSwapBW(array, m, buf, rl);

        int l = m - 1,
            r = buf + rl - 1,
            o = b - 1;

        for (; l >= a && r >= buf; o--) {
            if (Reads.compareIndices(array, r, l, 0.5, true) >= 0)
                 Writes.swap(array, o, r--, 0.5, true, false);
            else Writes.swap(array, o, l--, 0.5, true, false);
        }

        while (r >= buf)
            Writes.swap(array, o--, r--, 0.5, true, false);
    }

    // credits to aphitorite for multiway merging heap implementation
    private boolean keyLessThan(int[] array, int a, int b) {
        int cmp = Reads.compareIndices(array, this.ptrs[a], this.ptrs[b], 0.25, true);
        return cmp < 0 || (cmp == 0 && Reads.compareOriginalValues(a, b) < 0);
    }

    private void siftDown(int[] array, int t, int r, int size) {
        while (2 * r + 2 < size) {
            int nxt = 2 * r + 1,
                min = nxt + (this.keyLessThan(array, this.heap[nxt], this.heap[nxt + 1]) ? 0 : 1);

            if (this.keyLessThan(array, this.heap[min], t)) {
                Writes.write(this.heap, r, this.heap[min], 0.25, true, true);
                r = min;
            } else break;
        }

        int min = 2 * r + 1;
        if (min < size && this.keyLessThan(array, this.heap[min], t)) {
            Writes.write(this.heap, r, this.heap[min], 0.25, true, true);
            r = min;
        }

        Writes.write(this.heap, r, t, 0.25, true, true);
    }

    private void merge(int[] array, int size, int buf, int r, int b) {
        int a = this.ptrs[0];

        for (int i = 0; i < size; i++)
			Writes.write(this.heap, i, i, 0, false, true);

        for (int i = (size - 1) / 2; i >= 0; i--)
            this.siftDown(array, this.heap[i], i, size);

        int o = buf;
        while (size > 0) {
            int min = this.heap[0];

            Writes.swap(array, o++, this.ptrs[min], 1, true, false);
            Writes.write(this.ptrs, min, this.ptrs[min] + 1, 0, false, true);

            if (this.ptrs[min] == Math.min(a + (min + 1) * r, b))
                this.siftDown(array, this.heap[--size], 0, size);
            else
                this.siftDown(array, this.heap[0], 0, size);
        }

        blockSwapFW(array, buf, a, o - buf);
    }

    private void bigMerges(int[] array, int s, int b, int a) {
        int r = RUN_SIZE;
        while (r < b - s) {
            int kR = r * K, i;
            for (i = s; i + kR < b; i += kR) {
                for (int j = 0; j < K; j++)
                    Writes.write(this.ptrs, j, i + j * r, 0, false, true);

                this.merge(array, K, a, r, i + kR);
            }

            int f    = i,
                size = 0;
            while (f < b) {
                Writes.write(this.ptrs, size, f, 0, false, true);
                f += r;
                size++;
            }

            if (size > 1) {
                if (size == 2)
                    simpleMerge(array, this.ptrs[0], this.ptrs[1], b, a);
                else this.merge(array, size, a, r, b);
            }

            r = kR;
        }
    }

    private int sortBuf(int[] array, int a, int b) {
        int n = b - a;
        if (n <= this.sqrtn) {
            insertSort(array, a, b);
            return -1;
        }

        int h = n / 2 + (n & 1),
            s = a + h;

        sortRuns(array, s, b);

        if (b - s > this.smallBlock)
            this.bigMerges(array, s, b, a);
        else {
            int r = RUN_SIZE;
            while (r < b - s) {
                int twoR = r * 2, i;
                for (i = s; i + twoR < b; i += twoR)
                    simpleMerge(array, i, i + r, i + twoR, a);

                if (i + r < b)
                    simpleMerge(array, i, i + r, b, a);

                r = twoR;
            }
        }

        return s;
    }

    private void sort(int[] array, int a, int b) {
        int n = b - a;
        if (n <= RUN_SIZE) {
            insertSort(array, a, b);
            return;
        }

        int sqrtn = 1;
        for (; sqrtn * sqrtn < n; sqrtn *= 2);
        this.sqrtn = sqrtn;

        this.smallBlock = Math.max(n / (32 - Integer.numberOfLeadingZeros(n)), MIN_SMALL_BLOCK);
        // the original algorithm uses a tree instead of a heap
        this.heap = Writes.createExternalArray(K + 1);
        this.ptrs = Writes.createExternalArray(K + 1);

        // the original algorithm starts by sorting the upper half.
        // i implemented it so it sorts the lower half first so fewer
        // routines would be necessary to make the sort work
        int h = n / 2 - (n & 1),
            e = a + h;

        sortRuns(array, a, e);
        this.bigMerges(array, a, e, e);

        while (true) {
            int p = this.sortBuf(array, e, b);
            if (p == -1) {
                mergeInPlace(array, a, e, b);
                Writes.deleteExternalArrays(heap, ptrs);
                return;
            }

            gallopMerge(array, a, e, p, b, p);
            e = p;
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.sort(array, 0, length);
    }
}
