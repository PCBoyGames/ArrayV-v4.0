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

public class RubidiumSort extends Sort {
    public RubidiumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Rubidium");
        this.setRunAllSortsName("Rubidium Sort (Block Merge Sort)");
        this.setRunSortName("Rubidium Sort");
        this.setCategory("Hybrid Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // partitioning variant of Lithium Sort

    private static int RUN_SIZE           = 32,
                             SMALL_SORT         = 256,
                             MAX_STRAT3_UNIQUE  = 8,
                             SMALL_MERGE        = 16;

    private int blockLen,
                bufPos,
                bufLen;

    private boolean dualBuf;

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
                bits.swap(k++, i1);

                i1 = k;
                for (int i = Math.max(k + 1, tm); i < j1; i++)
                    if (Reads.compareOriginalValues(bits.get(i), bits.get(i1)) < 0)
                        i1 = i;
            } else {
                blockSwapFW(array, a + k * blockLen, a + j1 * blockLen, blockLen);
                bits.swap(k, j1++);

                if (i1 == k++) i1 = j1 - 1;
            }
        }

        while (k < j1 - 1) {
            if (i1 > k) blockSwapFW(array, a + k * blockLen, a + i1 * blockLen, blockLen);
            bits.swap(k++, i1);

            i1 = k;
            for (int i = k + 1; i < j1; i++)
                if (Reads.compareOriginalValues(bits.get(i), bits.get(i1)) < 0)
                    i1 = i;
        }
    }

    private void mergeBlocks(int[] array, int a, int midKey, int blockQty, int blockLen, int lastLen, BitArray bits) {
        int f = a;
        boolean left = Reads.compareOriginalValues(bits.get(0), midKey) < 0;

        for (int i = 1; i < blockQty; i++) {
            if (left ^ (Reads.compareOriginalValues(bits.get(i), midKey) < 0)) {
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

            prepareKeys(bits, blockQty);

            this.blockSelect(
                array, bits, a, leftBlocks,
                rightBlocks, this.blockLen
            );

            this.mergeBlocks(
                array, a, leftBlocks,
                blockQty, this.blockLen,
                frag, bits
            );
        }
    }

    private void strat2BLenCalc(int twoR, int r) {
        int sqrtTwoR = 1;
        for (; sqrtTwoR * sqrtTwoR < twoR; sqrtTwoR *= 2);
        // double blockLen until number of bits needed < r
        for (; twoR / sqrtTwoR > r / (2 * (log2(twoR / sqrtTwoR) + 1)); sqrtTwoR *= 2);
        this.blockLen = sqrtTwoR;
    }

    private int medianOf3(int[] array, int[] indices) {
        if (indices.length == 0) return -1;
        if (indices.length <  3) return indices[0];

        if (Reads.compareIndices(array, indices[0], indices[1], 0.5, true) <= 0) {
            if (Reads.compareIndices(array, indices[1], indices[2], 0.5, true) <= 0)
                return indices[1];
            if (Reads.compareIndices(array, indices[0], indices[2], 0.5, true) < 0)
                return indices[2];
            return indices[0];
        }
        if (Reads.compareIndices(array, indices[1], indices[2], 0.5, true) >= 0) {
            return indices[1];
        }
        if (Reads.compareIndices(array, indices[0], indices[2], 0.5, true) <= 0) {
            return indices[0];
        }
        return indices[2];
    }

    private int medianOf9(int[] array, int start, int end) {
        int  length = end - start;
        int    half =  length / 2;
        int quarter =    half / 2;
        int  eighth = quarter / 2;

        int[] elements0 = {start, start + eighth, start + quarter};
        int med0 = medianOf3(array, elements0);

        int[] elements1 = {start + quarter + eighth, start + half, start + half + eighth};
        int med1 = medianOf3(array, elements1);

        int[] elements2 = {start + half + quarter, start + half + quarter + eighth, end - 1};
        int med2 = medianOf3(array, elements2);

        return medianOf3(array, new int[] {med0, med1, med2});
    }

    private boolean pivCmp(int v, int piv, int pCmp) {
        return Reads.compareValues(v, piv) < pCmp;
    }

    private void pivBufSet(int[] array, int pa, int pb, int v, int wLen) {
        while (wLen-- > 0) {
            if ((v & 1) == 1) Writes.swap(array, pa+wLen, pb+wLen, 1, true, false);
            v >>= 1;
        }
    }

    private int pivBufGet(int[] array, int pa, int piv, int pCmp, int wLen, int bit) {
        int r = 0;

        while (wLen-- > 0) {
            r <<= 1;
            r |= (this.pivCmp(array[pa++], piv, pCmp) ? bit : bit^1);
        }
        return r;
    }

    private int partitionEasy(int[] array, int a, int b, int piv, int pCmp) {
        int j = this.bufPos;

        for (int i = a; i < b; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.25);

            if (this.pivCmp(array[i], piv, pCmp))
                Writes.swap(array, a++, i, 0.25, true, false);
            else
                Writes.swap(array, j++, i, 0.25, true, false);
        }

        blockSwapFW(array, this.bufPos, a, j - this.bufPos);

        return a;
    }

    private int blockPartition(int[] array, int a, int b, int bLen, int piv, int pCmp) {
        if (b - a <= bLen) return this.partitionEasy(array, a, b, piv, pCmp);

        int p = a;
        int l = 0, r = 0;
        int lb = 0, rb = 0;

        for (int i = a; i < b; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.25);

            if (pivCmp(array[i], piv, pCmp)) {
                Writes.swap(array, p+(l++), i, 0.25, true, false);

                if (l == bLen) {
                    l = 0;
                    lb++;
                    p += bLen;
                }
            }
            else {
                Writes.swap(array, this.bufPos + r++, i, 0.25, true, false);

                if (r == bLen) {
                    blockSwapFW(array, p, p + bLen, l);
                    blockSwapFW(array, this.bufPos, p, bLen);
                    r = 0;
                    rb++;
                    p += bLen;
                }
            }
        }

        int min = Math.min(lb, rb);
        int m = a + lb * bLen;

        if (min > 0) {
            int bCnt = lb + rb;
            int wLen = log2(min - 1);

            for (int i = 0, j = 0, k = 0; i < min; i++) {
                while (!pivCmp(array[a + j * bLen + wLen], piv, pCmp)) j++;
                while ( pivCmp(array[a + k * bLen + wLen], piv, pCmp)) k++;
                pivBufSet(array, a + (j++) * bLen, a + (k++) * bLen, i, wLen);
            }

            if (lb < rb) {
                for (int i = bCnt - 1, j = 0; j < rb; i--)
                    if (!pivCmp(array[a + i * bLen + wLen], piv, pCmp))
                        blockSwapFW(array, a + i * bLen, a + (bCnt - (++j)) * bLen, bLen);

                for (int i = 0; i < lb; i++) {
                    int dest = pivBufGet(array, a + i * bLen, piv, pCmp, wLen, 0);

                    while (dest != i) {
                        blockSwapFW(array, a + i * bLen, a + dest * bLen, bLen);
                        dest = pivBufGet(array, a + i * bLen, piv, pCmp, wLen, 0);
                    }

                    pivBufSet(array, a + i * bLen, m + i * bLen, i, wLen);
                }
            }
            else {
                for (int i = 0, j = 0; j < lb; i++)
                    if (pivCmp(array[a + i * bLen + wLen], piv, pCmp))
                        blockSwapFW(array, a + i * bLen, a + (j++) * bLen, bLen);

                for (int i = 0; i < rb; i++) {
                    int dest = pivBufGet(array, m + i * bLen, piv, pCmp, wLen, 1);

                    while (dest != i) {
                        blockSwapFW(array, m + i * bLen, m + dest * bLen, bLen);
                        dest = pivBufGet(array, m + i * bLen, piv, pCmp, wLen, 1);
                    }
                    pivBufSet(array, a + i * bLen, m + i * bLen, i, wLen);
                }
            }
        }

        blockSwapBW(array, this.bufPos, b - r, r);

        if (l > 0) {
            Highlights.clearMark(2);
            blockSwapBW(array, b - r - l, this.bufPos, l);
            blockSwapBW(array, a + lb * bLen, a + lb * bLen + l, rb * bLen);
            blockSwapBW(array, this.bufPos, a + lb * bLen, l);
        }
        return a + lb * bLen + l;
    }

    private int mergePartitions(int[] array, int a, int m, int b, int piv, int pCmp) {
        boolean left = pCmp == 0;
        int p0 = binarySearch(array, a, m, piv, left);
        int p1 = binarySearch(array, m, b, piv, left);
        rotate(array, p0, m, p1);
        return p1 - m + p0;
    }

    private int rotatePartition(int[] array, int a, int b, int piv, int pCmp, int r) {
        int i;
        for (i = a; i < b - r; i += r)
            this.blockPartition(array, i, i + r, this.bufLen, piv, pCmp);

        if (i < b)
            this.blockPartition(array, i, b, this.bufLen, piv, pCmp);

        int p = a;
        while (r < b - a) {
            int twoR = 2 * r;
            for (i = a; i < b - twoR; i += twoR)
                p = mergePartitions(array, i, i + r, i + twoR, piv, pCmp);

            if (i + r < b)
                p = mergePartitions(array, i, i + r, b, piv, pCmp);

            r = twoR;
        }

        return p;
    }

    private int partition(int[] array, int a, int b, int bLen, int piv, int pCmp, boolean strat2) {
        if (strat2) {
            long r = ((long)bLen << bLen) + 2 * bLen - 1;
            if (b - a > r)
                 return this.rotatePartition(array, a, b, piv, pCmp, (int)r);
            else return this.blockPartition(array, a, b, bLen, piv, pCmp);
        }

        return this.blockPartition(array, a, b, bLen, piv, pCmp);
    }

    private void quickSelect(int[] array, int a, int b, int bLen, int k, boolean strat2) {
        while (b - a > RUN_SIZE) {
            int p = this.medianOf9(array, a, b);
            int m = this.partition(array, a, b, bLen, array[p], 0, strat2);

            if (m == k) return;
            if (m == a)
                a = this.partition(array, a, b, bLen, array[p], 1, strat2);
            else if (k > m) a = m;
            else            b = m;
        }

        insertSort(array, a, b);
    }

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

    private int find(int[] array, int a, int b, int cmp) {
        int f = array[a];
        for (a++; a < b; a++)
            if (Reads.compareIndexValue(array, a, f, 0.25, true) == cmp)
                f = array[a];
        return f;
    }

    private boolean verifyBitArray(int[] array, int a, int b, int size) {
        return Reads.compareValues(find(array, a, a + size, 1), find(array, b - size, b, -1)) == 0;
    }

    private void rubidiumLoop(int[] array, int s, int e) {
        boolean strat2 = this.blockLen == 0;

        int a  = s,
            b  = e,
            p  = -1,
            bA = -1,
            bB = -1;

        outer:
        while (b - a > SMALL_SORT) {
            if (p == -1)
                p = this.partition(array, s, e, this.bufLen, array[this.medianOf9(array, s, e)], 0, strat2);

            if (p - s > e - p) {
                a = p; b = e; bA = s; bB = p;
            } else {
                a = s; b = p; bA = p; bB = e;
            }

            int bM = bA + (bB - bA) / 2;
            this.quickSelect(array, bA, bB, this.bufLen, bM, strat2);

            BitArray bits = null;
            if (!strat2) {
                int nW   = (b - a) / this.blockLen,
                    w    = log2(nW) + 1,
                    size = nW * w;

                bits = new BitArray(array, bA, bB - size, nW, w);

                if (verifyBitArray(array, bA, bB, size)) {
                    inPlaceMergeSort(array, bA, bB);
                    s = a; e = b; p = -1;
                    continue;
                }
            }

            sortRuns(array, a, b);

            int r = RUN_SIZE;
            while (r <= this.bufLen) {
                int twoR = 2 * r, i;
                for (i = a; i < b - twoR; i += twoR)
                    this.mergeWithBufferBW(array, i, i + r, i + twoR, true);

                if (i + r < b) this.mergeWithBufferBW(array, i, i + r, b, true);

                r = twoR;
            }

            while (r < b - a) {
                int twoR = r * 2, i;

                if (strat2) {
                    this.strat2BLenCalc(twoR, bM - bA);

                    int nW   = twoR / this.blockLen,
                        w    = log2(nW) + 1,
                        size = nW * w;

                    bits = new BitArray(array, bA, bB - size, nW, w);

                    if (verifyBitArray(array, bA, bB, size)) {
                        inPlaceMergeSort(array, bA, bB);
                        s = a; e = b; p = -1;
                        continue outer;
                    }
                }

                for (i = a; i < b - twoR; i += twoR)
                    this.combine(array, i, i + r, i + twoR, bits);

                if (i + r < b) this.combine(array, i, i + r, b, bits);

                if (strat2) bits.free();

                r = twoR;
            }

            if (!strat2) bits.free();
            s = bA; e = bB; p = bM;
        }

        inPlaceMergeSort(array, a, b);
        if (bA != -1)
            inPlaceMergeSort(array, bA, bB);
    }

    public void sort(int[] array, int a, int b) {
        int n = b - a;
        if (n <= SMALL_SORT) {
            inPlaceMergeSort(array, a, b);
            return;
        }

        int bLen = 1,
            tg   = log2(n) + 1;
        for (; bLen <= tg; bLen *= 2);
        this.blockLen = bLen;

        int sqrtn = 1;
        for (; sqrtn * sqrtn < n; sqrtn *= 2);

        int ideal = 2 * bLen;
        int keysFound = findKeys(array, a, b, ideal);

        if (keysFound <= MAX_STRAT3_UNIQUE) {
            inPlaceMergeSort(array, a, b);
            return;
        }

        int e = b - keysFound;
        this.bufPos = e;
        this.bufLen = keysFound;

        if (keysFound == ideal) {
            // strat 1
            this.blockLen = bLen;
            this.dualBuf  = true;
        } else {
            // strat 2
            this.blockLen = 0;
            this.dualBuf  = false;
        }

        this.rubidiumLoop(array, a, e);

        insertSort(array, e, b);
        mergeInPlaceBW(array, a, e, b, true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.sort(array, 0, length);
    }
}
