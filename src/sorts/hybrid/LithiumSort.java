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

/*
 * Lithium Sort
 *
 * A conceptually optimal in-place block merge sorting algorithm.
 * This algorithm introduces some ideas, that in conjunction with code optimizations and
 * other tricks (like the ones in Holy GrailSort), minimizes moves and comparisons for
 * every step of the in-place block merge sorting procedure.
 *
 * Time complexity: O(n log n) best/average/worst
 * Space complexity: O(1)
 * Stable: Yes
 *
 * Special thanks to aphitorite for creating the kota merging algorithm, which enables
 * strategy 1's block merging routine to be optimal; the dualMerge routine, which simplifies
 * the rest of the code as well as improving performance; the buffer redistribution algorithm,
 * found in Adaptive Grailsort; the smarter block selection algorithm, used in the blockSelect
 * routine, and part of the code for some of the other routines.
 */

public class LithiumSort extends Sort {
    public LithiumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Lithium");
        this.setRunAllSortsName("Lithium Sort (Block Merge Sort)");
        this.setRunSortName("Lithium Sort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private static int RUN_SIZE          = 32,
                             SMALL_SORT        = 256,
                             MAX_STRAT3_UNIQUE = 8,
                             SMALL_MERGE       = 16;

    private int origBlockLen,
                blockLen,
                bufPos,
                bufLen,
                keyPos,
                keyLen;

    private boolean strat1;

    private class BitArray {
        private int[] array;
        private int pa, pb, w;

        public int size, length;

        public BitArray(int[] array, int pa, int pb, int size, int w) {
            this.array  = array;
            this.pa     = pa;
            this.pb     = pb;
            this.size   = size;
            this.w      = w;
            this.length = size * w;
        }

        private void flipBit(int a, int b) {
            Writes.swap(array, a, b, 0.5, true, false);
        }

        private boolean getBit(int a, int b) {
            return Reads.compareIndices(array, a, b, 0.1, true) > 0;
        }

        private void setBit(int a, int b, boolean bit) {
            if (this.getBit(a, b) ^ bit)
                this.flipBit(a, b);
        }

        public void free() {
            int i1 = pa + length;
            for (int i = pa, j = pb; i < i1; i++, j++)
                this.setBit(i, j, false);
        }

        public void set(int idx, int val) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            int s = idx * w, i1 = pa + s + w;
            for (int i = pa + s, j = pb + s; i < i1; i++, j++, val >>= 1)
                this.setBit(i, j, (val & 1) == 1);

            if (val > 0) System.out.println("Warning: Word too large");
        }

        public int get(int idx) {
            assert (idx >= 0 && idx < size) : "BitArray index out of bounds";

            int r = 0, s = idx * w;
            for (int k = 0, i = pa + s, j = pb + s; k < w; k++, i++, j++)
                r |= (this.getBit(i, j) ? 1 : 0) << k;
            return r;
        }

        public void swap(int a, int b) {
            assert (a >= 0 && a < size) : "BitArray index out of bounds";
            assert (b >= 0 && b < size) : "BitArray index out of bounds";

            int tmp = this.get(a);
            this.set(a, this.get(b));
            this.set(b, tmp);
        }
    }

    //changes len sized blocks order ABC -> BCA
    private void multiTriSwap(int[] array, int a, int b, int c, int len) {
        Highlights.clearMark(2);
        for (int i = 0; i < len; i++) {
            int temp = array[a + i];
            Writes.write(array, a + i, array[b + i], 0.333, true, false);
            Writes.write(array, b + i, array[c + i], 0.333, true, false);
            Writes.write(array, c + i, temp, 0.333, true, false);
        }
    }

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
        int rl  = b - m,
            ll  = m - a,
            bl  = this.bufLen,
            min = rl != ll && Math.min(bl, Math.min(rl, ll)) > SMALL_MERGE ? bl : 1;

        while ((rl > min && ll > min) || (rl < SMALL_MERGE && rl > 1 && ll < SMALL_MERGE && ll > 1)) {
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

    public int findKeys(int[] array, int a, int b, int q) {
        int n = 1,
            p = b - 1;

        for (int i = p; i > a && n < q; i--) {
            int l = binarySearch(array, p, p + n, array[i - 1], true) - p;
            if (l == n || Reads.compareIndices(array, i - 1, p + l, 1, true) < 0) {
                rotate(array, i, p, p + n++);
                p = i - 1;
                insertToRight(array, i - 1, p + l);
            }
        }

        rotate(array, p, p + n, b);

        return n;
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

    private void mergeWithBufferBW(int[] array, int a, int m, int b, boolean left) {
        int rl = b - m;

        if (rl <= SMALL_MERGE || rl > this.bufLen) {
            mergeInPlaceBW(array, a, m, b, left);
            return;
        }

        blockSwapBW(array, m, this.bufPos, rl);

        int l = m - 1,
            r = this.bufPos + rl - 1,
            o = b - 1;

        for (; l >= a && r >= this.bufPos; o--) {
            int cmp = Reads.compareIndices(array, r, l, 0.5, true);
            if (left ? cmp >= 0 : cmp > 0)
                 Writes.swap(array, o, r--, 0.5, true, false);
            else Writes.swap(array, o, l--, 0.5, true, false);
        }

        while (r >= this.bufPos)
            Writes.swap(array, o--, r--, 0.5, true, false);
    }

    private void mergeRestWithBufferFW(int[] array, int a, int m, int b, int pLen, boolean left) {
        int l = this.bufPos,
            r = m,
            o = a,
            e = this.bufPos + pLen;

        for (; l < e && r < b; o++) {
            int cmp = Reads.compareIndices(array, l, r, 0.5, true);
            if (left ? cmp <= 0 : cmp < 0)
                 Writes.swap(array, o, l++, 0.5, true, false);
            else Writes.swap(array, o, r++, 0.5, true, false);
        }

        while (l < e)
            Writes.swap(array, o++, l++, 0.5, true, false);
    }

    private int mergeWithScrollingBufferFW(int[] array, int a, int m, int b, int p, boolean left) {
        int i = a, j = m;

        while (i < m && j < b) {
            int cmp = Reads.compareIndices(array, i, j, 0.5, true);
            if (left ? cmp <= 0 : cmp < 0)
                 Writes.swap(array, p++, i++, 0.5, true, false);
            else Writes.swap(array, p++, j++, 0.5, true, false);
        }

        if (i > p) {
            while (i < m)
                Writes.swap(array, p++, i++, 1, true, false);
        }

        return j;
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

    private void shift(int[] array, int a, int m, int b, boolean left) {
		if (left) {
			if (m == b) return;

			while (m > a)
				Writes.swap(array, --b, --m, 0.5, true, false);
		} else {
			if (m == a) return;

			while (m < b)
                Writes.swap(array, a++, m++, 0.5, true, false);
		}
	}

    private void dualMergeFW(int[] array, int a, int m, int b, int r) {
        int i = a,
            j = m,
            k = a - r;

        while (k < i && i < m) {
            if (Reads.compareIndices(array, i, j, 0.5, true) <= 0)
                 Writes.swap(array, k++, i++, 0.5, true, false);
            else Writes.swap(array, k++, j++, 0.5, true, false);
        }

        if (k < i)
            shift(array, j - r, j, b, false);
        else {
            int i2 = m - 1,
                j2 = b - 1;
            k = i2 + b - j;

            while (i2 >= i && j2 >= j) {
                if (Reads.compareIndices(array, i2, j2, 0.5, true) > 0)
                     Writes.swap(array, k--, i2--, 0.5, true, false);
                else Writes.swap(array, k--, j2--, 0.5, true, false);
            }

            while (j2 >= j)
                Writes.swap(array, k--, j2--, 0.5, true, false);
        }
    }

    private void swapKeys(int[] array, BitArray bits, int a, int b) {
        if (bits == null) Writes.swap(array, this.keyPos + a, this.keyPos + b, 1, true, false);
        else              bits.swap(a, b);
    }

    private int compareKeys(int[] array, BitArray bits, int a, int b) {
        if (bits == null) return Reads.compareIndices(array, this.keyPos + a, this.keyPos + b, 1, true);
        else              return Reads.compareOriginalValues(bits.get(a), bits.get(b));
    }

    private void blockSelect(int[] array, BitArray bits, int a, int leftBlocks, int rightBlocks, int blockLen) {
        int total = leftBlocks + rightBlocks;

        for (int j = 0, k = leftBlocks + 1; j < k - 1; j++) {
            int min = j;

            for (int i = Math.max(leftBlocks - 1, j + 1); i < k; i++) {
                int comp = Reads.compareIndices(array, a + (i + 1) * blockLen - 1, a + (min + 1) * blockLen - 1, 2, true);

                if (comp < 0 || (comp == 0 && compareKeys(array, bits, i, min) < 0))
                    min = i;
            }

            if (min != j) {
                blockSwapFW(array, a + j * blockLen, a + min * blockLen, blockLen);
                swapKeys(array, bits, j, min);

                if (k < total && min == k - 1) k++;
            }
        }
    }

    private boolean compareMidKey(int[] array, BitArray bits, int i, int midKey) {
        if (bits == null) return Reads.compareIndexValue(array, this.keyPos + i, midKey, 1, true) < 0;
        else              return Reads.compareOriginalValues(bits.get(i), midKey) < 0;
    }

    private void mergeBlocksWithBuf(int[] array, int a, int midKey, int leftBlocks, int rightBlocks, int b, int blockLen, BitArray bits) {
        int t  = leftBlocks + rightBlocks,
            a1 = a + blockLen,
            i  = a1,
            j  = a,
            k  = -1,
            l  = -1,
            r  = leftBlocks - 1;

        boolean left = true;
        while (l < leftBlocks && r < t) {
            if (left) {
                do {
                    j += blockLen;
                    l++;
                    k++;
                } while (l < leftBlocks && this.compareMidKey(array, bits, k, midKey));

                if (l == leftBlocks) {
                    i = mergeWithScrollingBufferFW(array, i, j, b, i - blockLen, true);
                    mergeRestWithBufferFW(array, i - blockLen, i, b, blockLen, true);
                } else {
                    i = mergeWithScrollingBufferFW(array, i, j, j + blockLen - 1, i - blockLen, true);
                }

                left = false;
            } else {
                do {
                    j += blockLen;
                    r++;
                    k++;
                } while (r < t && !this.compareMidKey(array, bits, k, midKey));

                if (r == t) {
                    shift(array, i - blockLen, i, b, false);
                    blockSwapFW(array, this.bufPos, b - blockLen, blockLen);
                } else {
                    i = mergeWithScrollingBufferFW(array, i, j, j + blockLen - 1, i - blockLen, false);
                }

                left = true;
            }
        }
    }

    private void mergeBlocksLazy(int[] array, int a, int midKey, int blockQty, int blockLen, int lastLen, BitArray bits) {
        int f = a;
        boolean left = this.compareMidKey(array, bits, 0, midKey);

        for (int i = 1; i < blockQty; i++) {
            if (left ^ this.compareMidKey(array, bits, i, midKey)) {
                int next    = a + i * blockLen,
                    nextEnd = binarySearch(array, next, next + blockLen, array[next - 1], left);

                this.mergeWithBufferBW(array, f, next, nextEnd, left);
                f    = nextEnd;
                left = !left;
            }
        }

        if (left && lastLen != 0) {
            int lastFrag = a + blockQty * this.blockLen;
            this.mergeWithBufferBW(array, f, lastFrag, lastFrag + lastLen, left);
        }
    }

    private void blockCycle(int[] array, int a, int blockQty, int blockLen, BitArray bits) {
        for (int i = 0; i < blockQty; i++) {
            int k = bits.get(i);

            if (k != i) {
                int j = i;

                do {
                    blockSwapFW(array, a + k * blockLen, a + j * blockLen, blockLen);
                    bits.set(j, j);

                    j = k;
                    k = bits.get(k);
                } while (k != i);

                bits.set(j, j);
            }
        }
    }

    private void kotaMerge(int[] array, int a, int m, int b1, int blockLen, BitArray bits) {
        int i = a,
            j = m,
            l = a,
            r = m,
            t = 1;

        for (int k = 0; k < blockLen; k++) {
            if (Reads.compareIndices(array, i, j, 0.5, true) <= 0)
                 Writes.swap(array, this.bufPos + k, i++, 0.5, true, false);
            else Writes.swap(array, this.bufPos + k, j++, 0.5, true, false);
        }

        for (; l < m && r < b1; t++) {
            boolean left = i - l > 0 && (i - l == blockLen || Reads.compareIndices(array, l + blockLen - 1, r + blockLen - 1, 0.5, true) <= 0);
            int p = left ? l : r;

            for (int k = 0; k < blockLen; k++, p++) {
                boolean pl = j == b1 || (i < m && Reads.compareIndices(array, i, j, 0.5, true) <= 0);

                if (pl) Writes.swap(array, p, i++, 0.5, true, false);
                else    Writes.swap(array, p, j++, 0.5, true, false);
            }

            if (left) l = p;
            else      r = p;

            bits.set(t, (p - a) / blockLen - 1);
        }

        int p = l < m ? l : r;

        blockSwapFW(array, this.bufPos, p, blockLen);
        bits.set(0, (p - a) / blockLen);

        while (true) {
            l += blockLen;
            if (l >= m) break;

            bits.set(t++, (l - a) / blockLen);
        }

        while (true) {
            r += blockLen;
            if (r >= b1) break;

            bits.set(t++, (r - a) / blockLen);
        }
    }

    private int log2(int n) {
        return 32 - Integer.numberOfLeadingZeros(n);
    }

    private void getBlocksIndicesLazy(int[] array, int a, int leftBlocks, int rightBlocks, int blockLen, BitArray indices, BitArray bits) {
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
            ) {
                bits.set(o, l);
                indices.set(o, l++);
            } else {
                bits.set(o, r);
                indices.set(o, r++);
            }
        }

        while (l < m) {
            Highlights.markArray(0, a + (l + 1) * blockLen - 1);
            bits.set(o, l);
            indices.set(o++, l++);
        }
        Highlights.clearMark(0);

        while (r < b) {
            Highlights.markArray(0, a + (r + 1) * blockLen - 1);
            bits.set(o, r);
            indices.set(o++, r++);
        }
        Highlights.clearMark(0);
    }

    private void getBlocksIndices(int[] array, int a, int leftBlocks, int rightBlocks, int blockLen, BitArray indices, BitArray bits) {
        int m = leftBlocks - 1,
            l = m,
            r = m + 1,
            b = m + 1 + rightBlocks,
            o = 0;

        if (l != -1) {
            int lb = a + (l + 1) * blockLen - 1;
            while (true) {
                if (r == b || Reads.compareIndices(
                    array, lb,
                    a + (r + 1) * blockLen - 1,
                    25, true) <= 0
                ) {
                    bits.set(o, l);
                    indices.set(o++, l);
                    break;
                }

                bits.set(o, r);
                indices.set(o++, r++);
            }

            if (l != 0) {
                l = 0;

                for (; l < m && r < b; o++) {
                    if (Reads.compareIndices(
                        array,
                        a + (l + 1) * blockLen - 1,
                        a + (r + 1) * blockLen - 1,
                        25, true) <= 0
                    ) {
                        bits.set(o, l);
                        indices.set(o, l++);
                    } else {
                        bits.set(o, r);
                        indices.set(o, r++);
                    }
                }

                while (l < m) {
                    Highlights.markArray(0, a + (l + 1) * blockLen - 1);
                    Delays.sleep(25);
                    bits.set(o, l);
                    indices.set(o++, l++);
                }
                Highlights.clearMark(0);
            }
        }

        while (r < b) {
            Highlights.markArray(0, a + (r + 1) * blockLen - 1);
            Delays.sleep(25);
            bits.set(o, r);
            indices.set(o++, r++);
        }
        Highlights.clearMark(0);
    }

    private void prepareKeysLazy(BitArray bits, int q) {
        for (int i = 0; i < q; i++)
            bits.set(i, i);
    }

    private void prepareKeys(BitArray bits, int q, int leftBlocks) {
        int i;
        for (i = 0; i < leftBlocks - 1; i++)
            bits.set(i, i + 1);

        bits.set(i, 0);

        for (i++; i < q; i++)
            bits.set(i, i);
    }

    private void combine(int[] array, int a, int m, int b, BitArray bits, BitArray indices, boolean lazy) {
        if (b - m <= this.bufLen) {
            this.mergeWithBufferBW(array, a, m, b, true);
            return;
        }

        if (this.strat1) {
            int blockQty = (b - a) / this.blockLen,
                b1       = a + blockQty * this.blockLen;

            kotaMerge(array, a, m, b1, this.blockLen, bits);
            blockCycle(array, a, blockQty, this.blockLen, bits);
            this.mergeWithBufferBW(array, a, b1, b, true);
        } else {
            int leftBlocks  = (m - a) / this.blockLen,
                rightBlocks = (b - m) / this.blockLen,
                blockQty    = leftBlocks + rightBlocks,
                frag        = (b - a) - blockQty * this.blockLen;

            int midKey;
            if (lazy) {
                if (bits == null) {
                    insertSort(array, this.keyPos, this.keyPos + blockQty + 1);
                    midKey = array[this.keyPos + leftBlocks];
                    this.blockSelect(array, bits, a, leftBlocks, rightBlocks, this.blockLen);
                } else {
                    midKey = leftBlocks;

                    if (indices == null) {
                        prepareKeysLazy(bits, blockQty);
                        this.blockSelect(array, bits, a, leftBlocks, rightBlocks, this.blockLen);
                    } else {
                        getBlocksIndicesLazy(array, a, leftBlocks, rightBlocks, this.blockLen, indices, bits);
                        blockCycle(array, a, blockQty, this.blockLen, indices);
                    }
                }

                this.mergeBlocksLazy(array, a, midKey, blockQty, this.blockLen, frag, bits);
            } else {
                multiTriSwap(array, this.bufPos, m - this.blockLen, a, this.blockLen); // TODO check if two blockswaps are faster
                leftBlocks--;
                blockQty--;

                if (bits == null) {
                    insertSort(array, this.keyPos, this.keyPos + blockQty + 1);
                    midKey = array[this.keyPos + leftBlocks];
                    insertToRight(array, this.keyPos, this.keyPos + leftBlocks - 1);
                    this.blockSelect(array, bits, a + this.blockLen, leftBlocks, rightBlocks, this.blockLen);
                } else {
                    midKey = leftBlocks;

                    if (indices == null) {
                        prepareKeys(bits, blockQty, leftBlocks);
                        this.blockSelect(array, bits, a + this.blockLen, leftBlocks, rightBlocks, this.blockLen);
                    } else {
                        getBlocksIndices(array, a + this.blockLen, leftBlocks, rightBlocks, this.blockLen, indices, bits);
                        blockCycle(array, a + this.blockLen, blockQty, this.blockLen, indices);
                    }
                }

                this.mergeBlocksWithBuf(array, a, midKey, leftBlocks, rightBlocks, b, this.blockLen, bits);
            }
        }
    }

    private void strat2BLenCalc(int twoR, int r) {
        int sqrtTwoR = 1;
        for (; sqrtTwoR * sqrtTwoR < twoR; sqrtTwoR *= 2);
        // double blockLen until number of bits needed < r
        for (; twoR / sqrtTwoR > r / (2 * (log2(twoR / sqrtTwoR) + 1)); sqrtTwoR *= 2);
        this.blockLen = sqrtTwoR;
    }

    private void noBitsBLenCalc(int twoR) {
        // get sqrt(current subarray size)
        int sqrtTwoR = 1;
        for (; sqrtTwoR * sqrtTwoR < twoR; sqrtTwoR *= 2);

        // try to use internal buffer
        int kCnt = twoR / sqrtTwoR + 1;
        if (kCnt < this.keyLen) {
            this.bufLen = this.keyLen - kCnt;
            this.bufPos = this.keyPos + kCnt;
        } else {
            // if not able to, double blocklen until keys are enough (if they aren't)
            for (; twoR / sqrtTwoR + 1 > this.keyLen; sqrtTwoR *= 2);
            this.bufLen = 0;
        }

        this.blockLen = sqrtTwoR;
    }

    private void resetBuf() {
        this.bufPos   = this.keyPos;
        this.bufLen   = this.keyLen;
        this.blockLen = this.origBlockLen;
    }

    private boolean checkValidBitArray(int[] array, int a, int b, int size) {
        return a + size < b - size && Reads.compareIndices(array, a + size, b - size, 0.1, true) < 0;
    }

    private int[] adjust(int[] array, int a, int m, int b, boolean aSub) {
        int frag = 0;

        if (aSub) {
            int mN = a + ((m - a) / this.blockLen) * this.blockLen,
                bN = b - (m - mN);

            // a [ - A0 - ] mN [frag] m [ - A1 - ] b

            frag = mN != m ? 1 : 0;
            if (frag == 1) rotate(array, mN, m, b);

            // a [ - A0 - ] mN [ - A1 - ] b [frag] bN

            m = mN;
            b = bN;
        } else {
            a = m - ((m - a) / this.blockLen) * this.blockLen;
        }

        return new int[] {a, m, b, frag};
    }

    private void firstMergePart(int[] array, int a, int m, int b, int bA, int bB, boolean strat2, boolean aSub) {
        if (b - m <= this.bufLen) {
            this.mergeWithBufferBW(array, a, m, b, true);
            return;
        }

        boolean frag = false;
        int origB = b;

        int twoR = b - a;
        if (strat2) this.strat2BLenCalc(twoR, bB - bA);

        boolean lazy = this.blockLen > this.bufLen;

        int nW   = twoR / this.blockLen - (!(lazy || this.strat1) ? 1 : 0),
            w    = log2(nW) + 1,
            size = nW * w;

        if ((!this.strat1) && checkValidBitArray(array, bA, bB, size * 2)) {
            int[] pos = this.adjust(array, a, m, b, aSub);
            a = pos[0]; m = pos[1]; b = pos[2]; frag = pos[3] == 1;

            BitArray bits    = new BitArray(array, bA, bB - size * 2, nW, w),
                     indices = new BitArray(array, bA + size, bB - size, nW, w);

            this.combine(array, a, m, b, bits, indices, lazy);

            bits.free();
            indices.free();
        } else if (checkValidBitArray(array, bA, bB, size)) {
            int[] pos = this.adjust(array, a, m, b, aSub);
            a = pos[0]; m = pos[1]; b = pos[2]; frag = pos[3] == 1;

            BitArray bits = new BitArray(array, bA, bB - size, nW, w);
            this.combine(array, a, m, b, bits, null, lazy);
            bits.free();
        } else {
            this.noBitsBLenCalc(twoR);
            int[] pos = this.adjust(array, a, m, b, aSub);
            a = pos[0]; m = pos[1]; b = pos[2]; frag = pos[3] == 1;

            boolean strat1 = this.strat1;
            this.strat1 = false;
            this.combine(array, a, m, b, null, null, this.blockLen > this.bufLen);
            this.strat1 = strat1;
            this.resetBuf();
        }

        if (frag) this.mergeWithBufferBW(array, a, origB, b, false);
    }

    private void firstMerge(int[] array, int a, int m, int b, boolean strat2) {
        if (b - m <= this.bufLen) {
            this.mergeWithBufferBW(array, a, m, b, true);
            return;
        }

        // a [ -    -   AT    -    -]  m [ -    -   BT   -    - ] b

        int m1 = a + (m - a) / 2,
            m2 = this.binarySearch(array, m, b, array[m1], true),
            m3 = m1 + m2 - m;

        rotate(array, m1, m, m2);

        int lAT = m3 - a,
            lBT = b  - m3,
            lA0 = m1 - a,
            lA1 = m3 - m1,
            lB0 = m2 - m3,
            lB1 = b  - m2;

        // a [ - A0 - ] m1 [ - A1 - ] m3 [ - B0 - ] m2 [ - B1 - ] b

        int bA, bB;
        if (lAT < lBT) {
            if (lB0 > lB1) {
                bA = m3;
                bB = m2;
            } else {
                bA = m2;
                bB = b;
            }

            this.firstMergePart(array,  a, m1, m3, bA, bB, strat2, true);
            this.firstMergePart(array, m3, m2,  b,  a, m3, strat2, false);
        } else {
            if (lA0 > lA1) {
                bA = a;
                bB = m1;
            } else {
                bA = m1;
                bB = m3;
            }

            this.firstMergePart(array, m3, m2,  b, bA, bB, strat2, false);
            this.firstMergePart(array,  a, m1, m3, m3,  b, strat2, true);
        }
    }

    private void lithiumLoop(int[] array, int a, int b) {
        int r = RUN_SIZE,
            e = b - this.keyLen;
        while (r <= this.bufLen) {
            int twoR = 2 * r, i;
            for (i = a; i < e - twoR; i += twoR);

            if (i + r < e)
                mergeWithScrollingBufferBW(array, i, i + r, e);
            else shift(array, i, e, e + r, true);

            for (i -= twoR; i >= a; i -= twoR)
                mergeWithScrollingBufferBW(array, i, i + r, i + twoR);

            int oldR = r;
            r = twoR;
            twoR *= 2;

            for (i = a + oldR; i + twoR < e + oldR; i += twoR)
                dualMergeFW(array, i, i + r, i + twoR, oldR);

            if (i + r < e + oldR)
                dualMergeFW(array, i, i + r, e + oldR, oldR);
            else shift(array, i - oldR, i, e + oldR, false);

            r = twoR;
        }

        b = e;
        e += this.keyLen;

        boolean strat2 = this.blockLen == 0;

        int twoR = r * 2;
        while (twoR < b - a) {
            int i = a + twoR;
            this.firstMerge(array, a, a + r, i, strat2);

            if (strat2) this.strat2BLenCalc(twoR, twoR);

            boolean lazy   = this.blockLen > this.bufLen,
                    strat1 = this.strat1;

            int nW   = twoR / this.blockLen - (!(lazy || strat1) ? 1 : 0),
                w    = log2(nW) + 1,
                size = nW * w;

            BitArray bits, indices;
            if ((!strat1) && checkValidBitArray(array, a, a + twoR, size * 2)) {
                bits    = new BitArray(array, a       , a + twoR - size * 2, nW, w);
                indices = new BitArray(array, a + size, a + twoR - size    , nW, w);
            } else if (checkValidBitArray(array, a, a + twoR, size)) {
                bits    = new BitArray(array, a, a + twoR - size, nW, w);
                indices = null;
            } else {
                bits    = null;
                indices = null;
                this.strat1 = false;
                this.noBitsBLenCalc(twoR);
                lazy = this.blockLen > this.bufLen;
            }

            for (; i < b - twoR; i += twoR)
                this.combine(array, i, i + r, i + twoR, bits, indices, lazy);

            if (i + r < b)
                this.combine(array, i, i + r, b, bits, indices, lazy);

            if (bits == null) {
                this.resetBuf();
                this.strat1 = strat1;
            } else bits.free();

            if (indices != null) indices.free();

            r = twoR;
            twoR *= 2;
        }

        this.firstMerge(array, a, a + r, b, strat2);

        boolean single = this.bufLen <= SMALL_MERGE;
        this.bufLen = 0;
        insertSort(array, b, e);

        if (single) mergeInPlaceBW(array, a, b, e, true);
        else {
            r = binarySearch(array, a, b, array[e - 1], false);
            rotate(array, r, b, e);

            int d = b - r;
            e -= d;
            b -= d;

            int b0 = b + (e - b) / 2;
            r = binarySearch(array, a, b, array[b0 - 1], false);
            rotate(array, r, b, b0);

            d   = b - r;
            b0 -= d;
            b  -= d;

            mergeInPlaceBW(array, b0, b0 + d, e, true);
            mergeInPlaceBW(array, a, b, b0, true);
        }
    }

    // strategy 3
    public void inPlaceMergeSort(int[] array, int a, int b) {
        sortRuns(array, a, b);

        int r = RUN_SIZE;
        while (r < b - a) {
            int twoR = r * 2, i;
            for (i = a; i < b - twoR; i += twoR)
                mergeInPlaceBW(array, i, i + r, i + twoR, true);

            if (i + r < b) mergeInPlaceBW(array, i, i + r, b, true);

            r = twoR;
        }
    }

    public void sort(int[] array, int a, int b) {
        int n = b - a;
        if (n <= SMALL_SORT) {
            inPlaceMergeSort(array, a, b);
            return;
        }

        int sqrtn = 1;
        for (; sqrtn * sqrtn < n; sqrtn *= 2);

        int keysFound = findKeys(array, a, b, sqrtn);

        if (keysFound <= MAX_STRAT3_UNIQUE) {
            inPlaceMergeSort(array, a, b);
            return;
        }

        this.bufPos       = b - keysFound;
        this.bufLen       = keysFound;
        this.keyLen       = keysFound;
        this.keyPos       = this.bufPos;
        this.origBlockLen = sqrtn;

        if (keysFound == sqrtn) {
            this.blockLen = sqrtn;
            this.strat1   = true;
        } else {
            this.blockLen = 0;
            this.strat1   = false;
        }

        sortRuns(array, a, b - keysFound);
        this.lithiumLoop(array, a, b);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.sort(array, 0, length);
    }
}
