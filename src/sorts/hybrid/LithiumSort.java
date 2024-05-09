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
 * This algorithm introduces some ideas, that in conjunction with a heavier usage of
 * scrolling buffers, code optimizations and other tricks (like the ones in Holy GrailSort),
 * minimizes moves and comparisons for every step of the in-place block merge sorting procedure.
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

    private static final int RUN_SIZE           = 32,
                             SMALL_SORT         = 256,
                             MAX_STRAT4_UNIQUE  = 8,
                             SMALL_MERGE        = 16;

    private int origBlockLen,
                blockLen,
                bufPos,
                bufLen,
                keyPos,
                keyLen;

    private boolean dualBuf;

    private class BitArray {
        private final int[] array;
        private final int pa, pb, w;

        public final int size, length;

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

        if(k < i)
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
                this.swapKeys(array, bits, k++, i1);

                i1 = k;
                for (int i = Math.max(k + 1, tm); i < j1; i++)
                    if (this.compareKeys(array, bits, i, i1) < 0)
                        i1 = i;
            } else {
                blockSwapFW(array, a + k * blockLen, a + j1 * blockLen, blockLen);
                this.swapKeys(array, bits, k, j1++);

                if (i1 == k++) i1 = j1 - 1;
            }
        }

        while (k < j1 - 1) {
            if (i1 > k) blockSwapFW(array, a + k * blockLen, a + i1 * blockLen, blockLen);
            this.swapKeys(array, bits, k++, i1);

            i1 = k;
            for (int i = k + 1; i < j1; i++)
                if (this.compareKeys(array, bits, i, i1) < 0)
                    i1 = i;
        }
    }

    private boolean compareMidKey(int[] array, BitArray bits, int i, int midKey) {
        if (bits == null) return Reads.compareIndexValue(array, this.keyPos + i, midKey, 1, true) < 0;
        else              return Reads.compareOriginalValues(bits.get(i), midKey) < 0;
    }

    private void mergeBlocks(int[] array, int a, int midKey, int blockQty, int blockLen, int lastLen, BitArray bits) {
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

    private void blockCycle(int[] array, int a, int b, int blockLen, BitArray bits) {
        int total = (b - a) / blockLen;
        for (int i = 0; i < total; i++) {
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

    private void kotaMerge(int[] array, int a, int m, int b, int blockLen, BitArray bits) {
        int c = 0,
            t = 2,
            i = a,
            j = m,
            k = this.bufPos,
            l = 0,
            r = 0;

        while (c++ < this.bufLen) {
            if (Reads.compareIndices(array, i, j, 0.5, true) <= 0) {
                Writes.swap(array, k++, i++, 0.5, true, false);
                l++;
            } else {
                Writes.swap(array, k++, j++, 0.5, true, false);
                r++;
            }
        }

        boolean left = l >= r;
        k = left ? i - l : j - r;
        c = 0;

        do {
            if (i < m && (j == b || Reads.compareIndices(array, i, j, 0.5, true) <= 0)) {
                Writes.swap(array, k++, i++, 0.5, true, false);
                l++;
            } else {
                Writes.swap(array, k++, j++, 0.5, true, false);
                r++;
            }

            if (++c == blockLen) {
                bits.set(t++, (k - a) / blockLen - 1);

                if (left) l -= blockLen;
                else      r -= blockLen;

                left = l >= r;
                k = left ? i - l : j - r;

                c = 0;
            }
        } while (i < m || j < b);

        int b1 = b - c;

        blockSwapFW(array, k - c, b1, c);
        r -= c;

        t = 0;
        k = this.bufPos;

        while (l > 0) {
            blockSwapFW(array, k, m - l, blockLen);
            bits.set(t++, (m - a - l) / blockLen);
            k += blockLen;
            l -= blockLen;
        }

        while (r > 0) {
            blockSwapFW(array, k, b1 - r, blockLen);
            bits.set(t++, (b1 - a - r) / blockLen);
            k += blockLen;
            r -= blockLen;
        }
    }

    private int log2(int n) {
        return 32 - Integer.numberOfLeadingZeros(n);
    }

    private void prepareKeys(BitArray bits, int q) {
        for (int i = 0; i < q; i++)
            bits.set(i, i);
    }

    private void combine(int[] array, int a, int m, int b, BitArray bits) {
        if (b - m <= this.bufLen) {
            this.mergeWithBufferBW(array, a, m, b, true);
            return;
        }

        if (this.dualBuf) {
            kotaMerge(array, a, m, b, this.blockLen, bits);
            blockCycle(array, a, b, this.blockLen, bits);
        } else {
            int leftBlocks  = (m - a) / this.blockLen,
                rightBlocks = (b - m) / this.blockLen,
                blockQty    = leftBlocks + rightBlocks,
                frag        = (b - a) - blockQty * this.blockLen;

            int midKey;
            if (bits == null) {
                insertSort(array, this.keyPos, this.keyPos + blockQty + 1);
                midKey = array[this.keyPos + leftBlocks];
            } else {
                prepareKeys(bits, blockQty);
                midKey = leftBlocks;
            }

            this.blockSelect(
                array, bits, a, leftBlocks,
                rightBlocks, this.blockLen
            );

            this.mergeBlocks(
                array, a, midKey,
                blockQty, this.blockLen,
                frag, bits
            );
        }
    }

    private void strat3BLenCalc(int twoR, int r) {
        int sqrtTwoR = 1;
        for (; sqrtTwoR * sqrtTwoR < twoR; sqrtTwoR *= 2);
        // double blockLen until number of bits needed < r
        for (; twoR / sqrtTwoR > r / (2 * (log2(twoR / sqrtTwoR) + 1)); sqrtTwoR *= 2);
        this.blockLen = sqrtTwoR;
    }

    private void noBitsBLenCalc(int twoR) {
        int kLen = this.keyLen,
            kBuf = (kLen + (kLen & 1)) / 2,
            bLen = 1, target;

        if (kBuf >= twoR / kBuf) {
            this.bufLen = kBuf;
            this.bufPos = this.keyPos + this.keyLen - kBuf;
            target = kBuf;
        } else {
            this.bufLen = 0;
            target = twoR / kLen;
        }

        for (; bLen <= target; bLen *= 2);
        this.blockLen = bLen;
    }

    private void resetBuf() {
        this.bufPos   = this.keyPos;
        this.bufLen   = this.keyLen;
        this.blockLen = this.origBlockLen;
    }

    private boolean checkValidBitArray(int[] array, int a, int b, int size) {
        return Reads.compareIndices(array, a + size, b - size, 0.1, true) < 0;
    }

    private void firstMerge(int[] array, int a, int m, int b, boolean strat3) {
        if (b - m <= this.bufLen) {
            this.mergeWithBufferBW(array, a, m, b, true);
            return;
        }

        int m1 = a + (m - a) / 2,
            m2 = this.binarySearch(array, m, b, array[m1], true),
            m3 = m1 + m2 - m;

        rotate(array, m1, m, m2);

        int twoR = b - m3;
        if (strat3) this.strat3BLenCalc(twoR, m1 - a);

        int nW   = twoR / this.blockLen,
            w    = log2(nW) + 1,
            size = nW * w;

        if (checkValidBitArray(array, a, m1, size)) {
            m3 = m2 - ((m2 - m3) / this.blockLen) * this.blockLen;

            BitArray bits = new BitArray(array, a, m1 - size, nW, w);
            this.combine(array, m3, m2, b, bits);
            bits.free();
        } else {
            this.noBitsBLenCalc(twoR);
            m3 = m2 - ((m2 - m3) / this.blockLen) * this.blockLen;

            boolean dualBuf = this.dualBuf;
            this.dualBuf = false;
            this.combine(array, m3, m2, b, null);
            this.dualBuf = dualBuf;
            this.resetBuf();
        }

        twoR = m3 - a;
        if (strat3) this.strat3BLenCalc(twoR, b - m3);

        nW   = twoR / this.blockLen;
        w    = log2(nW) + 1;
        size = nW * w;

        boolean frag;
        int m4, m5;
        if (this.checkValidBitArray(array, m3, b, size)) {
            m4 = a + ((m1 - a) / this.blockLen) * this.blockLen;
            m5 = m3 - (m1 - m4);

            frag = m4 != m1;
            if (frag) rotate(array, m4, m1, m3);

            BitArray bits = new BitArray(array, m3, b - size, nW, w);
            this.combine(array, a, m4, m5, bits);
            bits.free();
        } else {
            this.noBitsBLenCalc(twoR);
            m4 = a + ((m1 - a) / this.blockLen) * this.blockLen;
            m5 = m3 - (m1 - m4);

            frag = m4 != m1;
            if (frag) rotate(array, m4, m1, m3);

            boolean dualBuf = this.dualBuf;
            this.dualBuf = false;
            this.combine(array, a, m4, m5, null);
            this.dualBuf = dualBuf;
            this.resetBuf();
        }

        if (frag) this.mergeWithBufferBW(array, a, m3, m5, false);
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

        boolean strat3 = this.blockLen == 0;

        int twoR = r * 2;
        while (twoR < b - a) {
            int i = a + twoR;
            this.firstMerge(array, a, a + r, i, strat3);

            if (strat3) this.strat3BLenCalc(twoR, r);

            int nW   = twoR / this.blockLen,
                w    = log2(nW) + 1,
                size = nW * w;

            BitArray bits;
            boolean dualBuf = this.dualBuf;
            if (checkValidBitArray(array, a, a + twoR, size))
                bits = new BitArray(array, a, a + twoR - size, nW, w);
            else {
                bits = null;
                this.dualBuf = false;
                this.noBitsBLenCalc(twoR);
            }

            for (; i < b - twoR; i += twoR)
                this.combine(array, i, i + r, i + twoR, bits);

            if (i + r < b)
                this.combine(array, i, i + r, b, bits);

            if (bits == null) {
                this.resetBuf();
                this.dualBuf = dualBuf;
            } else bits.free();

            r = twoR;
            twoR *= 2;
        }

        this.firstMerge(array, a, a + r, b, strat3);

        this.bufLen = 0;
        insertSort(array, b, e);

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

    // strategy 4
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

    public void sort(int[] array, int a, int b, boolean doDualBuf) {
        int n = b - a;
        if (n <= SMALL_SORT) {
            inPlaceMergeSort(array, a, b);
            return;
        }

        int sqrtn = 1;
        for (; sqrtn * sqrtn < n; sqrtn *= 2);

        int ideal = doDualBuf ? 2 * sqrtn : sqrtn;
        int keysFound = findKeys(array, a, b, ideal);

        if (keysFound <= MAX_STRAT4_UNIQUE) {
            inPlaceMergeSort(array, a, b);
            return;
        }

        this.bufPos       = b - keysFound;
        this.bufLen       = keysFound;
        this.keyLen       = keysFound;
        this.keyPos       = this.bufPos;
        this.origBlockLen = sqrtn;

        if (keysFound == ideal && doDualBuf) {
            // strat 1
            this.blockLen = sqrtn;
            this.dualBuf  = true;
        } else if (keysFound >= sqrtn) {
            // strat 2
            this.blockLen = sqrtn;
            this.dualBuf  = false;
        } else {
            // strat 3
            this.blockLen = 0;
            this.dualBuf  = false;
        }

        sortRuns(array, a, b - keysFound);
        this.lithiumLoop(array, a, b);
    }

    public void sort(int[] array, int a, int b) {
        this.sort(array, a, b, true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.sort(array, 0, length);
    }
}
