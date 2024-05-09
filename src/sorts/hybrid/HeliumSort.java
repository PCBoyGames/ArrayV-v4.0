package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
Copyright (c) 2020 thatsOven

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

/*
 * Helium Sort
 *
 * A block merge sorting algorithm inspired by GrailSort and focused on adaptivity.
 *
 * Time complexity:
 *  - Best case: O(n)
 *  - Average case: O(n log n)
 *  - Worst case: O(n log n)
 * Space complexity is variable.
 *
 * The algorithm extends the concept of adaptivity to memory,
 * by using different strategies based on the amount
 * of memory given to it.
 *
 * Major strategies are:
 * "Uranium": merge sort, requires n / 2 memory.
 *            The code refers to it as "Strategy 1".
 *
 * "Hydrogen": block merge sort, requires "x" memory with sqrt(n) + n / sqrt(n) <= x < n / 2.
 *             To run optimally, Hydrogen mode requires exactly sqrt(n) + 2n / sqrt(n) memory.
 *             Hydrogen mode might switch to Helium mode if amount of given memory < sqrt(n) + 2n / sqrt(n),
 *             and the array contains less than n / sqrt(n) distinct values.
 *             Hydrogen mode uses two strategies, referred as "2A" and "2B".
 *
 * "Helium": block merge sort, requires "x" memory with 0 <= x < sqrt(n) + n / sqrt(n).
 *           Helium mode uses five strategies, referred to as: "3A", "3B", "3C", "4A",
 *           and "4B". Optimal amounts of memory are:
 *              - sqrt(n): will use strategy 3B or 4A;
 *              - 0: will use strategy 3C or 4B.
 *           Strategy 3A is only used when Hydrogen mode switches.
 *
 * When a very low amount of distinct values is found or the array size is less or equal than 256,
 * the sort uses an adaptive in-place merge sort referred to as "Strategy 5".
 *
 * Special thanks to the members of The Holy Grail Sort Project, for the creation of Rewritten GrailSort,
 * which has been a great reference during the development of this algorithm,
 * and thanks to aphitorite, a great sorting mind which inspired the creation of this algorithm,
 * alongside being very helpful for me to understand some of the workings of block merge sorting algorithms,
 * and for part of the code used in this algorithm itself: "smarter block selection",
 * the algorithm used in the "blockSelectInPlace" and "blockSelectOOP" routines, and the
 * code used in the "mergeBlocks" routine.
 */

public class HeliumSort extends Sort {
    public HeliumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Helium");
        this.setRunAllSortsName("Helium Sort (Block Merge Sort)");
        this.setRunSortName("Helium Sort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);

        this.setQuestion("Insert amount of memory (or 1 .. 4 for default modes)", 0);
    }

    private static final int RUN_SIZE           = 32,
                             SMALL_SORT         = 256,
                             MIN_SORTED_UNIQUE  = 8,
                             MAX_STRAT5_UNIQUE  = 8,
                             MIN_REV_RUN_SIZE   = 8,
                             SMALL_MERGE        = 16;

    private int[] buffer = null;

    private int[] indices = null,
                  keys    = null;

    private int blockLen,
                keyPos,
                keyLen,
                bufPos,
                bufLen;

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
        boolean ip = this.buffer == null;
        int rl  = b - m,
            ll  = m - a,
            bl  = ip ? this.bufLen : this.buffer.length,
            min = rl != ll && Math.min(bl, Math.min(rl, ll)) > SMALL_MERGE ? bl : 1;

        while (rl > min && ll > min) {
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
        if (min == 1 || rl <= 1 || ll <= 1) return;

        if (ip) {
            if (rl < ll) {
                blockSwapBW(array, m, this.bufPos, rl);

                for (int i = m + rl - 1; i >= a + rl; i--)
                    Writes.swap(array, i, i - rl, 0.5, true, false);

                blockSwapBW(array, this.bufPos, a, rl);
            } else {
                blockSwapFW(array, a, this.bufPos, ll);

                for (int i = a; i < b - ll; i++)
                    Writes.swap(array, i, i + ll, 0.5, true, false);

                blockSwapFW(array, this.bufPos, b - ll, ll);
            }
        } else {
            if (rl < ll) {
                Writes.arraycopy(array, m, this.buffer, 0, rl, 0.5, false, true);
                Writes.arraycopy(array, a, array, b - ll, ll, 0.5, true, false);
                Writes.arraycopy(this.buffer, 0, array, a, rl, 0.5, true, false);
            } else {
                Writes.arraycopy(array, a, this.buffer, 0, ll, 0.5, false, true);
                Writes.arraycopy(array, m, array, a, rl, 0.5, true, false);
                Writes.arraycopy(this.buffer, 0, array, b - ll, ll, 0.5, true, false);
            }
        }
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

    private void reverseRuns(int[] array, int a, int b) {
        int l = a;
        while (l < b) {
            int i = l;
            for (; i < b - 1; i++)
                if (Reads.compareIndices(array, i, i + 1, 0.5, true) <= 0)
                    break;

            if (i - l >= MIN_REV_RUN_SIZE)
                Writes.reversal(array, l, i, 0.5, true, false);

            l = i + 1;
        }
    }

    private int checkSortedIdx(int[] array, int a, int b) {
        reverseRuns(array, a, b);

        for (b--; b > a; b--)
            if (Reads.compareIndices(array, b, b - 1, 0.5, true) < 0)
                return b;

        return a;
    }

    public int findKeysUnsorted(int[] array, int a, int p, int b, int q, int to) {
        int n = b - p;

        for (int i = p; i > a && n < q; i--) {
            int l = binarySearch(array, p, p + n, array[i - 1], true) - p;
            if (l == n || Reads.compareIndices(array, i - 1, p + l, 1, true) < 0) {
                rotate(array, i, p, p + n++);
                p = i - 1;
                insertToRight(array, i - 1, p + l);
            }
        }

        rotate(array, p, p + n, to);
        return n;
    }

    private int findKeysSorted(int[] array, int a, int b, int q) {
        int n = 1,
            p = b - 1;

        for (int i = p; i > a && n < q; i--) {
            if (Reads.compareIndices(array, i - 1, i, 0.5, true) != 0) {
                rotate(array, i, p, p + n++);
                p = i - 1;
            }
        }

        if (n == q) rotate(array, p, p + n,     b);
        else        rotate(array, a,     p, p + n);

        return n;
    }

    public int[] findKeys(int[] array, int a, int b, int q, int minSorted) {
        int p = checkSortedIdx(array, a, b);
        if (p == a) return null;

        if (b - p < minSorted)
            return new int[] { findKeysUnsorted(array, a, b - 1, b, q, b), b };
        else {
            int n = findKeysSorted(array, p, b, q);
            if (n == q) return new int[] {n, p};

            return new int[] { findKeysUnsorted(array, a, p, p + n, q, b), p };
        }
    }

    private int[] findKeys(int[] array, int a, int b, int q) {
        return findKeys(array, a, b, q, MIN_SORTED_UNIQUE);
    }

    private void insertSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++)
            if (Reads.compareIndices(array, i, i - 1, 0, false) < 0)
                insertToLeft(array, i, binarySearch(array, a, i, array[i], false));
    }

    private void sortRuns(int[] array, int a, int b, int p) {
        if (p != b) b = Math.min(a + ((p - a) / RUN_SIZE + 1) * RUN_SIZE, b);

        int i;
        for (i = a; i < b - RUN_SIZE; i += RUN_SIZE)
            insertSort(array, i, i + RUN_SIZE);

        if (i < b) insertSort(array, i, b);
    }

    private boolean checkMergeBounds(int[] array, int a, int m, int b) {
        if      (Reads.compareIndices(array, m - 1, m, 0.5, true) <= 0) return true;
        else if (Reads.compareIndices(array, a, b - 1, 0.5, true) >  0) {
            rotate(array, a, m, b);
            return true;
        }

        return false;
    }

    private int[] reduceMergeBounds(int[] array, int a, int m, int b) {
        return new int[] {
            binarySearch(array, a, m - 1, array[m    ], false),
            binarySearch(array, m, b    , array[m - 1], true),
        };
    }

    private void mergeInPlaceFW(int[] array, int a, int m, int b, boolean left) {
        int s = a,
            l = m;

        while (s < l && l < b) {
            int cmp = Reads.compareIndices(array, s, l, 0, false);
            if (left ? cmp > 0 : cmp >= 0) {
                int p = binarySearch(array, l, b, array[s], left);
                rotate(array, s, l, p);
                s += p - l;
                l = p;
            } else s++;
        }
    }

    private void mergeInPlaceBW(int[] array, int a, int m, int b, boolean left) {
        int s = b - 1,
            l = m - 1;

        while (s > l && l >= a) {
            int cmp = Reads.compareIndices(array, l, s, 0, false);
            if (left ? cmp > 0 : cmp >= 0) {
                int p = this.binarySearch(array, a, l, array[s], !left);
                rotate(array, p, l + 1, s + 1);
                s -= l + 1 - p;
                l = p - 1;
            } else s--;
        }
    }

    private void mergeInPlace(int[] array, int a, int m, int b, boolean left, boolean check) {
        if (check) {
            if (checkMergeBounds(array, a, m, b)) return;
            int[] bounds = reduceMergeBounds(array, a, m, b);
            a = bounds[0];
            b = bounds[1];
        }

        if (m - a > b - m) mergeInPlaceBW(array, a, m, b, left);
        else               mergeInPlaceFW(array, a, m, b, left);
    }

    private void mergeInPlace(int[] array, int a, int m, int b) {
        mergeInPlace(array, a, m, b, true, true);
    }

    private void mergeWithBufferFW(int[] array, int a, int m, int b, int buf, boolean left) {
        int ll = m - a;
        blockSwapFW(array, a, buf, ll);

        int l = buf,
            r = m,
            o = a,
            e = buf + ll;

        for (; l < e && r < b; o++) {
            int cmp = Reads.compareIndices(array, l, r, 0.5, true);
            if (left ? cmp <= 0 : cmp < 0)
                 Writes.swap(array, o, l++, 0.5, true, false);
            else Writes.swap(array, o, r++, 0.5, true, false);
        }

        while (l < e)
            Writes.swap(array, o++, l++, 0.5, true, false);
    }

    private void mergeWithBufferBW(int[] array, int a, int m, int b, int buf, boolean left) {
        int rl = b - m;
        blockSwapBW(array, m, buf, rl);

        int l = m - 1,
            r = buf + rl - 1,
            o = b - 1;

        for (; l >= a && r >= buf; o--) {
            int cmp = Reads.compareIndices(array, r, l, 0.5, true);
            if (left ? cmp >= 0 : cmp > 0)
                 Writes.swap(array, o, r--, 0.5, true, false);
            else Writes.swap(array, o, l--, 0.5, true, false);
        }

        while (r >= buf)
            Writes.swap(array, o--, r--, 0.5, true, false);
    }

    public void mergeWithBuffer(int[] array, int a, int m, int b, int buf, boolean left) {
        if (checkMergeBounds(array, a, m, b)) return;
        int[] bounds = reduceMergeBounds(array, a, m, b);
        a = bounds[0];
        b = bounds[1];

        int ll = m - a,
            rl = b - m;

        if (ll > rl) {
            if (rl <= SMALL_MERGE) mergeInPlaceBW(array, a, m, b, left);
            else                   mergeWithBufferBW(array, a, m, b, buf, left);
        } else {
            if (ll <= SMALL_MERGE) mergeInPlaceFW(array, a, m, b, left);
            else                   mergeWithBufferFW(array, a, m, b, buf, left);
        }
    }

    public void mergeWithBuffer(int[] array, int a, int m, int b, int buf) {
        mergeWithBuffer(array, a, m, b, buf, true);
    }

    private void mergeOOPFW(int[] array, int a, int m, int b, boolean left) {
        Highlights.clearAllMarks();

        int ll = m - a;
        Writes.arraycopy(array, a, this.buffer, 0, ll, 0.25, true, true);

        int l = 0,
            r = m,
            o = a,
            e = ll;

        for (; l < e && r < b; o++) {
            int cmp = Reads.compareValueIndex(array, this.buffer[l], r, 0.5, true);
            if (left ? cmp <= 0 : cmp < 0)
                 Writes.write(array, o, this.buffer[l++], 0.5, true, false);
            else Writes.write(array, o,       array[r++], 0.5, true, false);
        }

        while (l < e)
            Writes.write(array, o++, this.buffer[l++], 0.5, true, false);
    }

    private void mergeOOPBW(int[] array, int a, int m, int b, boolean left) {
        Highlights.clearAllMarks();

        int rl = b - m;
        Writes.arraycopy(array, m, this.buffer, 0, rl, 0.25, true, true);

        int l = m  - 1,
            r = rl - 1,
            o = b  - 1;

        for (; l >= a && r >= 0; o--) {
            int cmp = Reads.compareValueIndex(array, this.buffer[r], l, 0.5, true);
            if (left ? cmp >= 0 : cmp > 0)
                 Writes.write(array, o, this.buffer[r--], 0.5, true, false);
            else Writes.write(array, o,       array[l--], 0.5, true, false);
        }

        while (r >= 0)
            Writes.write(array, o--, this.buffer[r--], 0.5, true, false);
    }

    public void mergeOOP(int[] array, int a, int m, int b, boolean left) {
        if (checkMergeBounds(array, a, m, b)) return;
        int[] bounds = reduceMergeBounds(array, a, m, b);
        a = bounds[0];
        b = bounds[1];

        int ll = m - a,
            rl = b - m;

        if (ll > rl) {
            if (rl <= SMALL_MERGE) mergeInPlaceBW(array, a, m, b, left);
            else                   mergeOOPBW(array, a, m, b, left);
        } else {
            if (ll <= SMALL_MERGE) mergeInPlaceFW(array, a, m, b, left);
            else                   mergeOOPFW(array, a, m, b, left);
        }

        Highlights.clearAllMarks();
    }

    public void mergeOOP(int[] array, int a, int m, int b) {
        this.mergeOOP(array, a, m, b, true);
    }

    private boolean optiSmartMerge(int[] array, int a, int m, int b, int buf, boolean left) {
        int ll = m - a,
            rl = b - m;

        if (ll > rl) {
            if (rl <= SMALL_MERGE) {
                mergeInPlaceBW(array, a, m, b, left);
                return true;
            }

            if (this.buffer != null && rl <= this.buffer.length)
                mergeOOPBW(array, a, m, b, left);
            else if (buf != -1 && rl <= this.bufLen)
                mergeWithBufferBW(array, a, m, b, buf, left);
            else return false;
        } else {
            if (ll <= SMALL_MERGE) {
                mergeInPlaceFW(array, a, m, b, left);
                return true;
            }

            if (this.buffer != null && ll <= this.buffer.length)
                mergeOOPFW(array, a, m, b, left);
            else if (buf != -1 && ll <= this.bufLen)
                mergeWithBufferFW(array, a, m, b, buf, left);
            else return false;
        }

        return true;
    }

    private boolean optiMerge(int[] array, int a, int m, int b, int buf) {
        int[] bounds = reduceMergeBounds(array, a, m, b);
        a = bounds[0];
        b = bounds[1];

        Highlights.clearAllMarks();

        return optiSmartMerge(array, a, m, b, buf, true);
    }

    private void getBlocksIndices(int[] array, int a, int leftBlocks, int rightBlocks, int blockLen) {
        int l = 0,
            m = leftBlocks,
            r = m,
            b = m + rightBlocks,
            o = 0;

        for (; l < m && r < b; o++) {
            if (Reads.compareIndices(
                array,
                a + (l + 1) * blockLen - 1,
                a + (r + 1) * blockLen - 1,
                25, true) <= 0
            )    Writes.write(this.indices, o, l++, 0, false, true);
            else Writes.write(this.indices, o, r++, 0, false, true);
        }
        Highlights.clearMark(2);

        while (l < m) {
            Highlights.markArray(0, a + (l + 1) * blockLen - 1);
            Writes.write(this.indices, o++, l++, 0, false, true);
        }
        Highlights.clearMark(0);

        while (r < b) {
            Highlights.markArray(0, a + (r + 1) * blockLen - 1);
            Writes.write(this.indices, o++, r++, 0, false, true);
        }
        Highlights.clearMark(0);
    }

    private void blockCycleInPlace(int[] array, int stKey, int a, int leftBlocks, int rightBlocks, int blockLen) {
        int total = leftBlocks + rightBlocks;
        for (int i = 0; i < total; i++) {
            if (Reads.compareOriginalValues(i, this.indices[i]) != 0) {
                Writes.arraycopy(array, a + i * blockLen, this.buffer, 0, blockLen, 1, true, true);
                int j = i,
                    next = this.indices[i],
                    key  = array[stKey + i];

                do {
                    Writes.arraycopy(array, a + next * blockLen, array, a + j * blockLen, blockLen, 1, true, false);
                    Writes.write(array, stKey + j, array[stKey + next], 1, true, false);
                    Writes.write(this.indices, j, j, 1, true, true);

                    j = next;
                    next = this.indices[next];
                } while (Reads.compareOriginalValues(next, i) != 0);

                Writes.arraycopy(this.buffer, 0, array, a + j * blockLen, blockLen, 1, true, false);
                Writes.write(array, stKey + j, key, 1, true, false);
                Writes.write(this.indices, j, j, 1, true, true);
            }
        }
    }

    private void blockCycleOOP(int[] array, int a, int leftBlocks, int rightBlocks, int blockLen) {
        int total = leftBlocks + rightBlocks;
        for (int i = 0; i < total; i++) {
            if (Reads.compareOriginalValues(i, this.indices[i]) != 0) {
                Writes.arraycopy(array, a + i * blockLen, this.buffer, 0, blockLen, 1, true, true);
                int j = i,
                    next = this.indices[i],
                    key  = this.keys[i];

                do {
                    Writes.arraycopy(array, a + next * blockLen, array, a + j * blockLen, blockLen, 1, true, false);
                    Writes.write(this.keys, j, this.keys[next], 1, true, true);
                    Writes.write(this.indices, j, j, 1, true, true);

                    j = next;
                    next = this.indices[next];
                } while (Reads.compareOriginalValues(next, i) != 0);

                Writes.arraycopy(this.buffer, 0, array, a + j * blockLen, blockLen, 1, true, false);
                Writes.write(this.keys, j, key, 1, true, true);
                Writes.write(this.indices, j, j, 1, true, true);
            }
        }
    }

    private void blockSelectInPlace(int[] array, int stKey, int a, int leftBlocks, int rightBlocks, int blockLen) {
        int i1 = stKey,
            tm = stKey + leftBlocks,
            j1 = tm,
            k  = stKey,
            tb = tm + rightBlocks;

        while (k < j1 && j1 < tb) {
            if (Reads.compareIndices(
                array,
                a + (i1 - stKey + 1) * blockLen - 1,
                a + (j1 - stKey + 1) * blockLen - 1,
                5, true
            ) <= 0) {
                if (i1 > k) blockSwapFW(array, a + (k - stKey) * blockLen, a + (i1 - stKey) * blockLen, blockLen);
                Writes.swap(array, k++, i1, 1, true, false);

                i1 = k;
                for (int i = Math.max(k + 1, tm); i < j1; i++)
                    if (Reads.compareIndices(array, i, i1, 5, true) < 0)
                        i1 = i;
            } else {
                blockSwapFW(array, a + (k - stKey) * blockLen, a + (j1 - stKey) * blockLen, blockLen);
                Writes.swap(array, k, j1++, 1, true, false);

                if (i1 == k++) i1 = j1 - 1;
            }
        }

        while (k < j1 - 1) {
            if (i1 > k) blockSwapFW(array, a + (k - stKey) * blockLen, a + (i1 - stKey) * blockLen, blockLen);
            Writes.swap(array, k++, i1, 1, true, false);

            i1 = k;
            for (int i = k + 1; i < j1; i++)
                if (Reads.compareIndices(array, i, i1, 5, true) < 0)
                    i1 = i;
        }
    }

    private void blockSelectOOP(int[] array, int a, int leftBlocks, int rightBlocks, int blockLen) {
        int i1 = 0,
            tm = leftBlocks,
            j1 = tm,
            k  = 0,
            tb = tm + rightBlocks;

        while (k < j1 && j1 < tb) {
            if (Reads.compareIndices(
                array,
                a + (i1 + 1) * blockLen - 1,
                a + (j1 + 1) * blockLen - 1,
                5, true
            ) <= 0) {
                if (i1 > k) blockSwapFW(array, a + k * blockLen, a + i1 * blockLen, blockLen);
                Writes.swap(this.keys, k++, i1, 0, false, true);

                i1 = k;
                for (int i = Math.max(k + 1, tm); i < j1; i++)
                    if (Reads.compareOriginalIndices(this.keys, i, i1, 0, false) < 0)
                        i1 = i;
            } else {
                blockSwapFW(array, a + k * blockLen, a + j1 * blockLen, blockLen);
                Writes.swap(this.keys, k, j1++, 0, false, true);

                if (i1 == k++) i1 = j1 - 1;
            }
        }

        while (k < j1 - 1) {
            if (i1 > k) blockSwapFW(array, a + k * blockLen, a + i1 * blockLen, blockLen);
            Writes.swap(this.keys, k++, i1, 0, false, true);

            i1 = k;
            for (int i = k + 1; i < j1; i++)
                if (Reads.compareOriginalIndices(this.keys, i, i1, 0, false) < 0)
                    i1 = i;
        }
    }

    private void smartMerge(int[] array, int a, int m, int b, boolean left) {
        if (optiSmartMerge(array, a, m, b, this.bufPos, left)) return;
        mergeInPlace(array, a, m, b, left, false);
    }

    private boolean compareKeys(int[] array, int[] keys, int key, int midKey) {
        if (array == keys) return Reads.compareIndexValue(keys, key, midKey, 1, true) < 0;
        else               return Reads.compareOriginalValues(keys[key], midKey) < 0;
    }

    private void mergeBlocks(int[] array, int a, int midKey, int blockQty, int blockLen, int lastLen, int stKey, int[] keys) {
        int f = a;
        boolean left = compareKeys(array, keys, stKey, midKey);

        for (int i = 1; i < blockQty; i++) {
            if (left ^ compareKeys(array, keys, stKey + i, midKey)) {
                int next    = a + i * blockLen,
                    nextEnd = binarySearch(array, next, next + blockLen, array[next - 1], left);

                this.smartMerge(array, f, next, nextEnd, left);
                f    = nextEnd;
                left = !left;
            }
        }

        if (left && lastLen != 0) {
            int lastFrag = a + blockQty * this.blockLen;
            this.smartMerge(array, f, lastFrag, lastFrag + lastLen, left);
        }
    }

    private void prepareOOPKeys(int blockQty) {
        for (int i = 0; i < blockQty; i++)
            Writes.write(this.keys, i, i, 0, false, true);
    }

    private void hydrogenCombine(int[] array, int a, int m, int b) {
        if (checkMergeBounds(array, a, m, b)) return;
        if (optiMerge(array, a, m, b, this.bufPos)) return;

        int leftBlocks  = (m - a) / this.blockLen,
            rightBlocks = (b - m) / this.blockLen,
            blockQty    = leftBlocks + rightBlocks,
            frag        = (b - a) - blockQty * this.blockLen;

        this.getBlocksIndices(array, a, leftBlocks, rightBlocks, this.blockLen);

        if (this.keys == null) {
            insertSort(array, this.keyPos, this.keyPos + blockQty + 1);

            int midKey = array[this.keyPos + leftBlocks];

            this.blockCycleInPlace(
                array, this.keyPos, a,
                leftBlocks, rightBlocks, this.blockLen
            );

            this.mergeBlocks(array, a, midKey, blockQty, this.blockLen, frag, this.keyPos, array);
        } else {
            this.prepareOOPKeys(blockQty);

            this.blockCycleOOP(
                array, a, leftBlocks,
                rightBlocks, this.blockLen
            );

            this.mergeBlocks(array, a, leftBlocks, blockQty, this.blockLen, frag, 0, this.keys);
        }
    }

    private void heliumCombine(int[] array, int a, int m, int b) {
        if (checkMergeBounds(array, a, m, b)) return;
        if (optiMerge(array, a, m, b, this.bufPos)) return;

        int leftBlocks  = (m - a) / this.blockLen,
            rightBlocks = (b - m) / this.blockLen,
            blockQty    = leftBlocks + rightBlocks,
            frag        = (b - a) - blockQty * this.blockLen;

        if (this.keys == null) {
            insertSort(array, this.keyPos, this.keyPos + blockQty + 1);

            int midKey = array[this.keyPos + leftBlocks];

            this.blockSelectInPlace(
                array, this.keyPos, a, leftBlocks,
                rightBlocks, this.blockLen
            );

            this.mergeBlocks(array, a, midKey, blockQty, this.blockLen, frag, this.keyPos, array);
        } else {
            this.prepareOOPKeys(blockQty);

            this.blockSelectOOP(
                array, a, leftBlocks,
                rightBlocks, this.blockLen
            );

            this.mergeBlocks(array, a, leftBlocks, blockQty, this.blockLen, frag, 0, this.keys);
        }
    }

    private void uraniumLoop(int[] array, int a, int b) {
        int r = RUN_SIZE;
        while (r < b - a) {
            int twoR = r * 2, i;
            for (i = a; i < b - twoR; i += twoR)
                this.mergeOOP(array, i, i + r, i + twoR);

            if (i + r < b) this.mergeOOP(array, i, i + r, b);

            r = twoR;
        }
    }

    private void hydrogenLoop(int[] array, int a, int b) {
        int r = RUN_SIZE;
        while (r <= this.buffer.length) {
            int twoR = 2 * r, i;
            for (i = a; i < b - twoR; i += twoR)
                this.mergeOOP(array, i, i + r, i + twoR);

            if (i + r < b) this.mergeOOP(array, i, i + r, b);

            r = twoR;
        }

        while (r < b - a) {
            int twoR = r * 2, i;
            for (i = a; i < b - twoR; i += twoR)
                this.hydrogenCombine(array, i, i + r, i + twoR);

            if (i + r < b) this.hydrogenCombine(array, i, i + r, b);

            r = twoR;
        }

        if (this.keyLen != 0) {
            int s = this.keyPos,
                e = s + this.keyLen;

            insertSort(array, s, e);
            mergeOOP(array, a, s, e);
        }
    }

    private void heliumLoop(int[] array, int a, int b) {
        int r = RUN_SIZE;
        if (this.buffer != null) {
            while (r <= this.buffer.length) {
                int twoR = 2 * r, i;
                for (i = a; i < b - twoR; i += twoR)
                    this.mergeOOP(array, i, i + r, i + twoR);

                if (i + r < b) this.mergeOOP(array, i, i + r, b);

                r = twoR;
            }
        }

        while (r <= this.bufLen) {
            int twoR = 2 * r, i;
            for (i = a; i < b - twoR; i += twoR)
                mergeWithBuffer(array, i, i + r, i + twoR, this.bufPos);

            if (i + r < b) mergeWithBuffer(array, i, i + r, b, this.bufPos);

            r = twoR;
        }

        boolean strat4       = this.blockLen == 0,
                internalKeys = this.keys == null;

        while (r < b - a) {
            int twoR = r * 2, i;

            if (strat4) {
                int kLen = this.keyLen == 0 ? this.bufLen : this.keyLen,
                    kBuf = (kLen + (kLen & 1)) / 2,
                    bLen = 1, target;

                if (kBuf >= twoR / kBuf) {
                    if (internalKeys) {
                        this.bufLen = kBuf;
                        this.bufPos = this.keyPos + this.keyLen - kBuf;
                    }

                    target = kBuf;
                } else {
                    if (internalKeys) this.bufLen = 0;

                    target = twoR / kLen;
                }

                for (; bLen <= target; bLen *= 2);
                this.blockLen = bLen;
            }

            for (i = a; i < b - twoR; i += twoR)
                this.heliumCombine(array, i, i + r, i + twoR);

            if (i + r < b) {
                if (strat4 && b - i - r <= this.keyLen) {
                    this.bufPos = this.keyPos;
                    this.bufLen = this.keyLen;
                }

                this.heliumCombine(array, i, i + r, b);
            }

            r = twoR;
        }

        if (this.keyLen != 0 || this.bufLen != 0) {
            int s = this.keyPos == -1 ? this.bufPos : this.keyPos,
                e = s + this.keyLen + this.bufLen;

            this.bufLen = 0;

            insertSort(array, s, e);

            int[] bounds = reduceMergeBounds(array, a, s, e);
            a = bounds[0];
            e = bounds[1];

            if (!optiSmartMerge(array, a, s, e, -1, true))
                mergeInPlace(array, a, s, e, true, false);
        }
    }

    // strategy 5
    private void inPlaceMergeSort(int[] array, int a, int b, int check) {
        if (check == -1) {
            int p = checkSortedIdx(array, a, b);
            if (p == a) return;

            sortRuns(array, a, b, p);
        } else sortRuns(array, a, b, check);

        int r = RUN_SIZE;
        while (r < b - a) {
            int twoR = r * 2, i;
            for (i = a; i < b - twoR; i += twoR)
                mergeInPlace(array, i, i + r, i + twoR);

            if (i + r < b) mergeInPlace(array, i, i + r, b);

            r = twoR;
        }
    }

    public void inPlaceMergeSort(int[] array, int a, int b) {
        inPlaceMergeSort(array, a, b, -1);
    }

    public void sort(int[] array, int a, int b, int mem) {
        int n = b - a;
        if (n <= SMALL_SORT) {
            inPlaceMergeSort(array, a, b, -1);
            return;
        }

        if (mem >= n / 2 || mem == 1) {
            if (mem == 1) mem = n / 2;

            int p = checkSortedIdx(array, a, b);
            if (p == a) return;
            sortRuns(array, a, b, p);

            this.buffer = Writes.createExternalArray(mem);

            this.uraniumLoop(array, a, b);

            Writes.deleteExternalArray(this.buffer);
            return;
        }

        int sqrtn = 1;
        for (; sqrtn * sqrtn < n; sqrtn *= 2);
        int keySize = n / sqrtn;

        if (mem >= sqrtn + 2 * keySize || mem == 2) {
            if (mem == 2) mem = sqrtn + 2 * keySize;
            else if (mem != sqrtn + 2 * keySize) {
                for (; sqrtn + 2 * n / sqrtn <= mem; sqrtn *= 2);
                sqrtn /= 2;
                keySize = n / sqrtn;
            }

            int p = checkSortedIdx(array, a, b);
            if (p == a) return;
            sortRuns(array, a, b, p);

            this.indices = Writes.createExternalArray(keySize);
            this.keys    = Writes.createExternalArray(keySize);
            this.buffer  = Writes.createExternalArray(mem - 2 * keySize);

            this.blockLen = sqrtn;
            this.bufLen   = 0;
            this.bufPos   = -1;
            this.keyLen   = 0;
            this.keyPos   = -1;

            // strat 2a
            this.hydrogenLoop(array, a, b);

            Writes.deleteExternalArrays(this.indices, this.keys, this.buffer);
            return;
        }

        if (mem >= sqrtn + keySize || mem == 3) {
            if (mem == 3) mem = sqrtn + keySize;
            else if (mem != sqrtn + keySize) {
                for (; sqrtn + n / sqrtn <= mem; sqrtn *= 2);
                sqrtn /= 2;
                keySize = n / sqrtn;
            }

            int[] res = findKeys(array, a, b, keySize);
            if (res == null) return;
            int keysFound = res[0],
                        p = res[1];

            this.blockLen = sqrtn;

            if (keysFound == keySize) {
                sortRuns(array, a, b - keysFound, p);

                this.indices = Writes.createExternalArray(keySize);
                this.buffer  = Writes.createExternalArray(mem - keySize);

                this.keyLen = keysFound;
                this.keyPos = b - keysFound;

                // strat 2b
                this.hydrogenLoop(array, a, b - keysFound);

                Writes.deleteExternalArrays(this.indices, this.buffer);
            } else {
                sortRuns(array, a, b, p);

                this.keys   = Writes.createExternalArray(keySize);
                this.buffer = Writes.createExternalArray(mem - keySize);

                this.bufLen = 0;
                this.bufPos = -1;
                this.keyLen = 0;
                this.keyPos = -1;

                // strat 3a
                this.heliumLoop(array, a, b);

                Writes.deleteExternalArrays(this.keys, this.buffer);
            }

            return;
        }

        if (mem >= sqrtn || mem == 4) {
            if (mem == 4) mem = sqrtn;
            else if (mem != sqrtn) {
                for (; sqrtn <= mem; sqrtn *= 2);
                sqrtn /= 2;
                keySize = n / sqrtn;
            }

            int[] res = findKeys(array, a, b, keySize);
            if (res == null) return;
            int keysFound = res[0],
                        p = res[1];

            if (keysFound != keySize && keysFound <= MAX_STRAT5_UNIQUE) {
                inPlaceMergeSort(array, a, b, p);
                return;
            }

            sortRuns(array, a, b - keysFound, p);

            this.buffer = Writes.createExternalArray(mem);

            this.bufLen = 0;
            this.bufPos = -1;
            this.keyLen = keysFound;
            this.keyPos = b - keysFound;

            if (keysFound == keySize) this.blockLen = sqrtn; // strat 3b
            else                      this.blockLen = 0;     // strat 4a

            this.heliumLoop(array, a, b - keysFound);

            Writes.deleteExternalArray(this.buffer);
            return;
        }

        int ideal = sqrtn + keySize;
        int[] res = findKeys(array, a, b, ideal);
        if (res == null) return;
        int keysFound = res[0],
                    p = res[1];

        if (keysFound <= MAX_STRAT5_UNIQUE) {
            inPlaceMergeSort(array, a, b, p);
            return;
        }

        sortRuns(array, a, b - keysFound, p);

        boolean hasMem = mem > 0;
        if (hasMem) this.buffer = Writes.createExternalArray(mem);

        if (keysFound == ideal) {
            // strat 3c
            this.blockLen = sqrtn;
            this.bufLen   = sqrtn;
            this.bufPos   = b - sqrtn;
            this.keyLen   = keySize;
            this.keyPos   = this.bufPos - keySize;
        } else {
            // strat 4b
            this.blockLen = 0;
            this.bufLen   = keysFound;
            this.bufPos   = b - keysFound;
            this.keyLen   = keysFound;
            this.keyPos   = b - keysFound;
        }

        this.heliumLoop(array, a, b - keysFound);

        if (hasMem) Writes.deleteExternalArray(this.buffer);
    }

    public void uraniumSort(int[] array, int a, int b) {
        this.sort(array, a, b, 1);
    }

    public void hydrogenSort(int[] array, int a, int b) {
        this.sort(array, a, b, 2);
    }

    public void heliumSort(int[] array, int a, int b) {
        this.sort(array, a, b, 0);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.sort(array, 0, length, bucketCount);
    }
}
