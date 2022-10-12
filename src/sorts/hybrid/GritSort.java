package sorts.hybrid;

import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;
import utils.IndexedRotations;
import main.ArrayVisualizer;

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

final public class GritSort extends Sort {
    public GritSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Grit");
        this.setRunAllSortsName("Grit Sort");
        this.setRunSortName("Gritsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    /**
        historic sorting algorithm:

        sorts in-place stable O(n sqrt(n log n)) comparisons O(n) moves worst case deterministically
        was the fastest in-place stable O(n) moves sort circa 1996-2007 (before franceschini)

        borrows some ideas from https://doi.org/10.1007/BF01940644
        and improves upon a number of them such as eliminating the need for tail recursion

        implementation uses O(log n) space for simplified block merge and block partition
        but both can be achieved in O(1) space within the time bounds using more complicated techniques

        @author aphitorite
    */

    private final int MIN_SORT = 74; //at n < floor(74/2) the amount of pivots in gritPartition is < 2

    private int productLog(int n) {
        int r = 1;
        while ((r<<r)+r-1 < n) r++;
        return r;
    }

    private int log2(int n) {
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    //calculates optimal segment size (result is order of O(sqrt(n log n)))
    private int calcSLen(int n, int m) {
        int a = 0, b = n;
        int logN = this.log2(n-1)+1;

        while (a < b) {
            int ms = (a+b)/2, mq = n/(ms+1);

            int m1 = m-mq*logN;
            int logS = this.log2(ms-1)+1;

            // special thanks to Emerald Block:
            // chained integer division is the same as integer division by the product
            int qf = m1/mq/(mq+1)/logS;

            if (qf >= 1) b = ms;
            else        a = ms+1;
        }
        return a;
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

    private class BitArray2D {
        private final BitArray bits;

        public final int x, y, length;

        public BitArray2D(int[] array, int pa, int pb, int x, int y, int w) {
            this.bits = new BitArray(array, pa, pb, x*y, w);

            this.x = x;
            this.y = y;
            this.length = x*y*w;
        }

        public void free() {
            this.bits.free();
        }

        public void set(int i, int j, int uInt) {
            this.bits.set(j*this.x+i, uInt);
        }
        public int get(int i, int j) {
            return this.bits.get(j*this.x+i);
        }

        public void incr(int i, int j) {
            this.bits.incr(j*this.x+i);
        }
        public void decr(int i, int j) {
            this.bits.decr(j*this.x+i);
        }
    }

    private void blockSwap(int[] array, int a, int b, int s) {
        while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }

    private void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
    }

    ////////////////////////
    //                    //
    //  MEDIAN SELECTION  //
    //                    //
    ////////////////////////

    private long getRank(int[] array, int a, int b, int r) {
        int c = 0, ce = 0;

        for (int i = a; i < b; i++) {
            if (i == r) continue;

            Highlights.markArray(2, i);
            Delays.sleep(0.1);

            int cmp = Reads.compareValues(array[i], array[r]);

            c  += cmp == -1 ? 1 : 0;
            ce += cmp <=  0 ? 1 : 0;
        }
        Highlights.clearMark(2);

        return (long)ce << 32 | (long)c;
    }

    private boolean isCandidate(int[] array, int i, int min, int max) {
        Highlights.markArray(1, i);
        Delays.sleep(0.02);

        return Reads.compareValues(array[i], array[min]) > 0 && Reads.compareValues(array[i], array[max]) < 0;
    }
    //both return index
    private int selectBlockMedian(int[] array, int a, int b, int med, int min, int max) {
        int lMin = min, lMax = max;

        for (int i = a;; i++) {
            if (this.isCandidate(array, i, lMin, lMax)) {
                int c = 0, ce = 0;

                for (int j = a; j < b; j++) {
                    if (j == i) continue;

                    Highlights.markArray(2, j);
                    Delays.sleep(0.01);

                    if (this.isCandidate(array, j, min, max)) {
                        int cmp = Reads.compareValues(array[j], array[i]);

                        c  += cmp == -1 ? 1 : 0;
                        ce += cmp <=  0 ? 1 : 0;
                    }
                }
                if (med >= c && med <= ce)
                    return i;

                else if (ce < med)
                    lMin = i;

                else lMax = i;
            }
        }
    }
    //finds rank without making any move in O(n^1.5) comps worst case
    private int selectRank(int[] array, int a, int b, int med, int min, int max) {
        long rank = this.getRank(array, a, b, min);
        int r = (int)rank, re = (int)(rank >> 32);

        if (med >= r && med <= re) return min;
        int cand = re+1;

        rank = this.getRank(array, a, b, max);
        r = (int)rank; re = (int)(rank >> 32);

        if (med >= r && med <= re) return max;

        int n = b-a, a1 = 0, b1 = 0;
        cand = r-cand;

        while (cand > 3) {
            int sqrt = (int)Math.sqrt(cand), s = (n-1)/sqrt+1, j = a;

            //find the first block that contains sqrt candidates

            for (; j+s < b; j += s) {
                Highlights.clearMark(2);

                int i = j, fCnt = 0;
                while (!this.isCandidate(array, i, min, max) && ++fCnt <= s-sqrt) i++;

                if (fCnt > s-sqrt) continue;

                else {
                    a1 = i++;
                    Highlights.markArray(2, a1);

                    boolean found = false;

                    int cCnt = 1;
                    for (; fCnt <= s-sqrt; i++)
                        if (this.isCandidate(array, i, min, max)) {
                            if (++cCnt == sqrt) {
                                found = true;
                                break;
                            }
                        }
                        else fCnt++;

                    if (found) {
                        b1 = i+1;
                        break;
                    }
                }
            }
            if (j+s >= b) {
                Highlights.clearMark(2);

                int i = j;
                while (!this.isCandidate(array, i, min, max)) i++;

                a1 = i++;
                Highlights.markArray(2, a1);

                for (int cCnt = 1; cCnt < sqrt; i++)
                    if (this.isCandidate(array, i, min, max)) cCnt++;

                b1 = i+1;
            }

            //find the median of the sqrt candidates within the block

            int bMed = this.selectBlockMedian(array, a1, b1, sqrt/2, min, max);

            r = 0; re = 0;
            int c = 0, ce = 0;

            //find rank of the median in the entire set of elements + candidates

            for (int i = a; i < b; i++) {
                if (i == bMed) continue;

                Highlights.markArray(2, i);
                Delays.sleep(0.01);

                int cmp = Reads.compareValues(array[i], array[bMed]);

                r  += cmp == -1 ? 1 : 0;
                re += cmp <=  0 ? 1 : 0;

                if (this.isCandidate(array, i, min, max)) {
                    c  += cmp == -1 ? 1 : 0;
                    ce += cmp <=  0 ? 1 : 0;
                }
            }
            Highlights.clearMark(2);

            //if desired rank was found return
            //otherwise prune based on the rank of the median candidate among all candidates

            if (med >= r && med <= re)
                return bMed;

            else if (re < med) {
                min = bMed;
                cand -= ce+1;
            }
            else {
                max = bMed;
                cand -= cand-c;
            }
        }
        for (int i = a; i < b; i++) {
            if (this.isCandidate(array, i, min, max)) {
                if (--cand == 0) return i;

                rank = this.getRank(array, a, b, i);
                r = (int)rank; re = (int)(rank >> 32);

                if (med >= r && med <= re)
                    return i;
            }
        }
        return -1; //unreachable
    }

    ///////////////
    //           //
    //  LOGSORT  //
    //           //
    ///////////////

    //@param pCmp - 0 for < piv, 1 for <= piv
    private boolean pivCmp(int v, int piv, int pCmp) {
        return Reads.compareValues(v, piv) < pCmp;
    }

    private void pivBufXor(int[] array, int pa, int pb, int v, int wLen) {
        while (wLen-- > 0) {
            if ((v&1) == 1) Writes.swap(array, pa+wLen, pb+wLen, 1, true, false);
            v >>= 1;
        }
    }
    //@param bit - < pivot means this bit
    private int pivBufGet(int[] array, int pa, int piv, int pCmp, int wLen, int bit) {
        int r = 0;

        while (wLen-- > 0) {
            r <<= 1;
            r |= (this.pivCmp(array[pa++], piv, pCmp) ? 0 : 1) ^ bit;
        }
        return r;
    }

    private void blockCycle(int[] array, int p, int n, int p1, int bLen, int wLen, int piv, int pCmp, int bit) {
        for (int i = 0; i < n; i++) {
            int dest = this.pivBufGet(array, p+i*bLen, piv, pCmp, wLen, bit);

            while (dest != i) {
                this.blockSwap(array, p+i*bLen, p+dest*bLen, bLen);
                dest = this.pivBufGet(array, p+i*bLen, piv, pCmp, wLen, bit);
            }
            this.pivBufXor(array, p+i*bLen, p1+i*bLen, i, wLen);
        }
        Highlights.clearMark(2);
    }

    private int partitionEasy(int[] array, int[] aux, int a, int b, int piv, int pCmp) {
        int j = 0;

        for (int i = a; i < b; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.25);

            if (this.pivCmp(array[i], piv, pCmp))
                Writes.write(array, a++, array[i], 0.25, true, false);
            else
                Writes.write(aux, j++, array[i], 0.25, false, true);
        }
        Writes.arraycopy(aux, 0, array, a, j, 0.5, true, false);

        return a;
    }
    private int blockPartition(int[] array, int[] aux, int a, int b, int bLen, int piv, int pCmp) {
        if (b-a <= bLen) return this.partitionEasy(array, aux, a, b, piv, pCmp);

        //sort blocks and type blocks

        int p = a;
        int l = 0, r = 0;
        int lb = 0, rb = 0;

        for (int i = a; i < b; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.25);

            if (this.pivCmp(array[i], piv, pCmp)) {
                Writes.write(array, p + l++, array[i], 0.25, true, false);

                if (l == bLen) {
                    l = 0;
                    lb++;
                    p += bLen;
                }
            }
            else {
                Writes.write(aux, r++, array[i], 0.25, false, true);

                if (r == bLen) {
                    Writes.arraycopy(array, p, array, p+bLen, l, 0.5, true, false);
                    Writes.arraycopy(aux, 0, array, p, bLen, 0.5, true, false);

                    r = 0;
                    rb++;
                    p += bLen;
                }
            }
        }

        //sort blocks

        int min = Math.min(lb, rb);
        int m = a+lb*bLen;

        if (min > 0) {
            int bCnt = lb+rb;
            int wLen = this.log2(min-1)+1; // ceil(log2(min))

            for (int i = 0, j = 0, k = 0; i < min; i++) { // set bit buffers
                while (!this.pivCmp(array[a+j*bLen+wLen], piv, pCmp)) j++;
                while ( this.pivCmp(array[a+k*bLen+wLen], piv, pCmp)) k++;
                this.pivBufXor(array, a+(j++)*bLen, a+(k++)*bLen, i, wLen);
            }

            if (lb < rb) {
                for (int i = bCnt-1, j = 0; j < rb; i--) // swap right to left
                    if (!this.pivCmp(array[a+i*bLen+wLen], piv, pCmp))
                        this.blockSwap(array, a+i*bLen, a+(bCnt-(++j))*bLen, bLen);

                this.blockCycle(array, a, lb, m, bLen, wLen, piv, pCmp, 0);
            }
            else {
                for (int i = 0, j = 0; j < lb; i++) // swap left to right
                    if (this.pivCmp(array[a+i*bLen+wLen], piv, pCmp))
                        this.blockSwap(array, a+i*bLen, a+(j++)*bLen, bLen);

                this.blockCycle(array, m, rb, a, bLen, wLen, piv, pCmp, 1);
            }
        }

        //handle leftover

        Writes.arraycopy(aux, 0, array, b-r, r, 1, true, false);

        if (l > 0) {
            Highlights.clearMark(2);
            Writes.arraycopy(array, b-r-l, aux, 0, l, 1, false, true);
            Writes.arraycopy(array, a+lb*bLen, array, a+lb*bLen+l, rb*bLen, 1, true, false);
            Writes.arraycopy(aux, 0, array, a+lb*bLen, l, 1, true, false);
        }
        return a+lb*bLen+l;
    }

    private void mergeFWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = m-a;

        Writes.arraycopy(array, a, tmp, 0, s, 1, true, true);

        int i = 0, j = m;

        while (i < s && j < b) {
            Highlights.markArray(2, j);

            if (Reads.compareValues(tmp[i], array[j]) <= 0)
                Writes.write(array, a++, tmp[i++], 1, true, false);
            else
                Writes.write(array, a++, array[j++], 1, true, false);
        }
        Highlights.clearAllMarks();

        while (i < s) Writes.write(array, a++, tmp[i++], 1, true, false);
    }
    private void mergeBWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = b-m;

        Writes.arraycopy(array, m, tmp, 0, s, 1, true, true);

        int i = s-1, j = m-1;

        while (i >= 0 && j >= a) {
            Highlights.markArray(2, j);

            if (Reads.compareValues(tmp[i], array[j]) >= 0)
                Writes.write(array, --b, tmp[i--], 1, true, false);
            else
                Writes.write(array, --b, array[j--], 1, true, false);
        }
        Highlights.clearAllMarks();

        while (i >= 0) Writes.write(array, --b, tmp[i--], 1, true, false);
    }

    private void blockMergeHelper(int[] array, int[] swap, int a, int m, int b, int p, int bLen, int piv, int pCmp, int bit) {
        if (b-m <= 2*bLen) {
            this.mergeBWExt(array, swap, a, m, b);
            return;
        }
        if (m-a <= 2*bLen) {
            this.mergeFWExt(array, swap, a, m, b);
            return;
        }

        int bCnt = 0, wLen = this.log2((b-a)/bLen-3)+1;

        int i = a, j = m, k = 0;
        int l = 0, r = 0;

        int c = 0;

        while (c++ < 2*bLen) { //merge 2 blocks into buffer to create 2 buffers
            if (Reads.compareValues(array[i], array[j]) <= 0) {
                Writes.write(swap, k++, array[i++], 1, true, true);
                l++;
            }
            else {
                Writes.write(swap, k++, array[j++], 1, true, true);
                r++;
            }
        }

        int t = 0, pc = p;

        boolean left = l >= r;
        k = left ? i-l : j-r;

        c = 0;

        do {
            if (j == b || Reads.compareValues(array[i], array[j]) <= 0) {
                Writes.write(array, k++, array[i++], 1, true, false);
                l++;
            }
            else {
                Writes.write(array, k++, array[j++], 1, true, false);
                r++;
            }
            if (++c == bLen) { //change buffer after every block
                this.pivBufXor(array, k-bLen, pc, t++, wLen);
                pc += bLen;

                if (left) l -= bLen;
                else     r -= bLen;

                left = l >= r;
                k = left ? i-l : j-r;

                c = 0;
                bCnt++;
            }
        }
        while (i < m);

        int b1 = j-c;

        Writes.arraycopy(array, k-c, array, b1, c, 1, true, false); //swap remainder to end (r buffer)
        r -= c;
        l = Math.min(l, m-a-l);

        //l and r buffers are divisible by bLen

        Writes.arraycopy(array, a,   array, m-l,  l, 1, true, false); //swap l buffer to front
        Writes.arraycopy(array, a+l, array, b1-r, r, 1, true, false); //swap r buffer to front
        Writes.arraycopy(swap,  0, array, a, 2*bLen, 1, true, false); //swap first merged elements to correct position in front

        this.blockCycle(array, a+2*bLen, bCnt, p, bLen, wLen, piv, pCmp, bit);
    }
    private void blockMerge(int[] array, int[] swap, int a, int m, int b, int p, int bLen, int piv, int pCmp, int bit) {
        if (b-a < this.MIN_SORT) return;

        int a1 = a+(m-a)%bLen;

        this.blockMergeHelper(array, swap, a1, m, b, p, bLen, piv, pCmp, bit);
        this.mergeFWExt(array, swap, a, a1, b);
    }

    /////////////////
    //             //
    //  GRIT SORT  //
    //             //
    /////////////////

    //@param cmp - comp val that means the element is a sorting element
    //items equal to the median automatically count as bit flipped
    private int stableCycleDest(int[] array, int a, int a1, int b1, int b, int p, int piv, int cmp) {
        int d = a1, e = 0;

        for (int i = a1+1; i < b; i++) {
            Highlights.markArray(2, i);
            boolean bit = Reads.compareValues(array[i], piv) == cmp;

            int val  = bit ? array[i] : array[p+(i-a)];
            int vCmp = Reads.compareValues(val, array[a1]);

            if (vCmp == -1) d++;
            else if (i < b1 && bit && vCmp == 0) e++;

            Highlights.markArray(3, d);
            Delays.sleep(0.025);
        }
        while (true) {
            if (Reads.compareValues(array[d], piv) == cmp && e-- == 0) break;
            d++;

            Highlights.markArray(3, d);
            Delays.sleep(0.025);
        }
        Highlights.clearMark(3);

        return d;
    }
    private void stableCycle(int[] array, int a, int b, int p, int piv, int cmp) {
        for (int i = a; i < b; i++) {
            if (Reads.compareValues(array[i], piv) == cmp) {
                Highlights.markArray(1, i);
                int j = i;

                while (true) {
                    int k = this.stableCycleDest(array, a, i, j, b, p, piv, cmp);

                    if (k == i) break;

                    int t = array[i];
                    Writes.write(array, i, array[k], 0.1, true, false);
                    Writes.write(array, k, array[p+(k-a)], 0.1, true, false);
                    Writes.write(array, p+(k-a), t, 0.1, true, false);

                    j = k;
                }
                Writes.swap(array, i, p+(i-a), 0.2, true, false);
            }
        }
    }

    private void gritSortRuns(int[] array, int a, int b, int p, int s1, int s2, int piv, int cmp) {
        if (b-a < this.MIN_SORT) {
            this.stableCycle(array, a, b, p, piv, cmp);
            return;
        }

        int m = (a+b)/2;

        for (int i = a; i < m; i += s1)
            this.stableCycle(array, i, Math.min(i+s1, m), p+(i-a), piv, cmp);
        for (int i = m; i < b; i += s2)
            this.stableCycle(array, i, Math.min(i+s2, b), p+(i-a), piv, cmp);
    }

    private void findPivots(int[] array, BitArray qIdx, int a, int p, int q, int piv, int cmp) {
        int f = a, fKeys = 0;

        for (int i = a; fKeys < q; i++) {
            if (Reads.compareIndexValue(array, i, piv, 1, true) != cmp) {
                this.rotate(array, f, f+fKeys, i);
                Writes.swap(array, p+(i-a), i, 1, true, false);
                f = i-fKeys;
                qIdx.set(fKeys, i-a-(fKeys++));
            }
        }
        this.rotate(array, a, f, f+q);
    }
    private void sortPivots(int[] array, BitArray qIdx, int a, int b) { //selection sort on a bit buffer array :grimacing:
        for (int i = a; i < b-1; i++) {
            int min = i, iIdx = qIdx.get(i-a), mIdx = iIdx;

            for (int j = min+1; j < b; j++) {
                int cmp = Reads.compareValues(array[j], array[min]);

                if (cmp < 0) {
                    min  = j;
                    mIdx = qIdx.get(j-a);
                }
                else if (cmp == 0) {
                    int jIdx = qIdx.get(j-a);

                    if (jIdx < mIdx) {
                        min  = j;
                        mIdx = jIdx;
                    }
                }
            }
            if (min > i) {
                Writes.swap(array, i, min, 1, true, false);
                qIdx.set(i-a,   mIdx);
                qIdx.set(min-a, iIdx);
            }
        }
    }
    private boolean pivEq(int val, int vPos, int pivL, int pivR, int ql, int qr) {
        int cmpL = ql == -1 ?  1 : Reads.compareValues(val, pivL);
        int cmpR = qr == -1 ? -1 : Reads.compareValues(val, pivR);

        return (cmpL > 0 || (cmpL == 0 && vPos >= ql)) && (cmpR < 0 || (cmpR == 0 && vPos < qr));
    }
    //@param vPos is offset to zero
    private int pivBinSearch(int[] array, BitArray qIdx, int a, int b, int val, int vPos) {
        int a1 = a;

        while (a1 < b) {
            int m = a1+(b-a1)/2;

            Highlights.markArray(2, m);
            Delays.sleep(0.25);

            int cmp = Reads.compareValues(val, array[m]);

            if (cmp < 0 || (cmp == 0 && vPos < qIdx.get(m-a)))
                b = m;
            else
                a1 = m+1;
        }
        return a1;
    }
    private void gritPartitionHelper(int[] array, int a, int b, int pa, int pb, int p, int s, int piv, int cmp) {
        int n = b-a;
        int rCnt = (n-1)/s+1, c = 0, q = n/(s+1), logS = this.log2(s-1)+1;

        BitArray runs = new BitArray(array, pa+rCnt, pb+rCnt, rCnt, logS);

        //select q pivots

        while (true) {
            int min = -1, mPos = -1;

            for (int i = 0; i < rCnt; i++) {
                if (Reads.compareValues(array[pa+i], array[pb+i]) < 0) {
                    int rMin = a+i*s + runs.get(i);
                    Highlights.markArray(1, rMin);

                    if (min == -1 || Reads.compareIndices(array, rMin, mPos, 0.25, false) < 0) {
                        min  = i;
                        mPos = rMin;
                        Highlights.markArray(2, mPos);
                    }
                }
            }
            if (min == -1) break;

            if (mPos == Math.min(a+(min+1)*s, b)-1)
                Writes.swap(array, pa+min, pb+min, 1, true, false);
            else
                runs.incr(min);

            if (c++ == s) { //pick off every s+1 item
                Writes.swap(array, mPos, p+(mPos-a), 1, true, false);
                c = 0;
            }
        }
        this.blockSwap(array, pa, pb, rCnt);
        runs.free();

        BitArray qIdx = new BitArray(array, pa, pb, q, this.log2(n-1)+1);
        this.findPivots(array, qIdx, a, p, q, piv, cmp);
        int a1 = a+q;

        this.sortPivots(array, qIdx, a, a1);

        //x = segment, y = result
        BitArray2D counts = new BitArray2D(array, pa+qIdx.length, pb+qIdx.length, q, q+1, logS);

        //set up counters for each segment

        for (int i = 0; i < q; i++) {
            if (i > 0)
                for (int j = 0; j < q+1; j++)
                    counts.set(i, j, counts.get(i-1, j));

            int k = a1+i*s;

            for (int j = 0; j < s; j++) {
                Highlights.markArray(1, k+j);
                Delays.sleep(0.5);

                int loc = this.pivBinSearch(array, qIdx, a, a1, array[k+j], k+j-a1)-a;
                counts.incr(i, loc);
            }
        }

        //transport elements using a stable cycle with 2d array of counters

        for (int i = a1; i < b; i++) {
            if (Reads.compareValues(array[i], piv) == cmp) {
                Highlights.markArray(3, i);
                int j = i;

                while (true) {

                    //binary search for segment

                    int loc  = this.pivBinSearch(array, qIdx, a, a1, array[i], j-a1)-a;
                    int curS = (j-a1)/s;

                    //set filter pivots

                    int pivL = loc > 0 ? array[a+loc-1]  : -1, pivR = loc < q ? array[a+loc]  : -1;
                    int ql   = loc > 0 ? qIdx.get(loc-1) : -1, qr   = loc < q ? qIdx.get(loc) : -1;

                    //read how many elements belong to same bucket from previous segments

                    int cntA = curS > 0 ? counts.get(curS-1, loc) : 0;
                    int cntB = 0;

                    //count how many unmarked elements behind current element belong to the same bucket

                    for (int i1 = Math.max(i+1, a1+curS*s); i1 < j; i1++)
                        if (Reads.compareValues(array[i1], piv) == cmp && this.pivEq(array[i1], i1-a1, pivL, pivR, ql, qr))
                            cntB++;

                    //find destination using the information

                    int k = a1+loc*s+cntA;
                    Highlights.markArray(2, k);

                    while (true) {
                        if (Reads.compareIndexValue(array, k, piv, 0.25, true) == cmp && cntB-- == 0) break;
                        k++;
                    }
                    if (k == i) break;

                    //transport element and continue cycle

                    int t = array[i];
                    Writes.write(array, i, array[k], 0.33, true, false);
                    Writes.write(array, k, array[p+(k-a)], 0.33, true, false);
                    Writes.write(array, p+(k-a), t, 0.34, true, false);

                    j = k;
                }
                Highlights.clearAllMarks();
                Writes.swap(array, i, p+(i-a), 1, true, false);
            }
        }
        qIdx.free();
        counts.free();
        this.blockSwap(array, a, p, q);

        //redistribute pivots without comparisons

        while (q > 0) {
            this.rotate(array, p, p+q, p+s+(q--));
            p += s+1;
        }
    }
    private void gritPartition(int[] array, int a, int b, int p, int s1, int s2, int piv, int cmp) {
        if (b-a < this.MIN_SORT) {
            this.blockSwap(array, a, p, b-a);
            return;
        }

        int m = (a+b)/2, p1 = p+(m-a);

        if (cmp == -1) {
            this.gritPartitionHelper(array, a, m, m, p1, p, s1, piv, cmp);
            this.gritPartitionHelper(array, m, b, p, a, p1, s2, piv, cmp);
        }
        else {
            this.gritPartitionHelper(array, a, m, p1, m, p, s1, piv, cmp);
            this.gritPartitionHelper(array, m, b, a, p, p1, s2, piv, cmp);
        }
    }

    private void gritSortBuckets(int[] array, int a, int b, int p, int s1, int s2, int piv, int cmp) {
        if (b-a < this.MIN_SORT) {
            this.blockSwap(array, a, p, b-a);
            return;
        }

        int m = (a+b)/2;

        for (int i = a; i < m; i += s1+1) {
            this.stableCycle(array, i, Math.min(i+s1, m), p+(i-a), piv, cmp);
            if (i+s1 < m) Writes.swap(array, i+s1, p+(i+s1-a), 1, true, false);
        }
        for (int i = m; i < b; i += s2+1) {
            this.stableCycle(array, i, Math.min(i+s2, b), p+(i-a), piv, cmp);
            if (i+s1 < b) Writes.swap(array, i+s1, p+(i+s1-a), 1, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int a = 0, b = length;
        int n = b-a;

        if (n <= 32) {
            BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
            smallSort.customBinaryInsert(array, a, b, 0.25);
            return;
        }

        int min = a, max = min;

        for (int i = a+1; i < b; i++) {
            if (Reads.compareIndices(array, i, min, 0.25, true) < 0)
                min = i;
            else if (Reads.compareIndices(array, i, max, 0.25, true) > 0)
                max = i;
        }

        int m1 = (a+b)/2;
        int med = array[this.selectRank(array, a, b, m1, min, max)];

        int lgBLen = this.productLog(length);
        int[] tmp = Writes.createExternalArray(2*lgBLen);

        this.blockPartition(array, tmp, a, this.blockPartition(array, tmp, a, b, 2*lgBLen, med, 1), 2*lgBLen, med, 0);
        if (Reads.compareIndices(array, a, b-1, 1, true) == 0) return;

        //find equal elements zone to be excluded from sorting

        int m2 = m1+n%2;
        while (Reads.compareIndices(array, m1-1, m2, 1, true) == 0) {
            m1--; m2++;
        }
        Highlights.clearMark(2);

        //depending on these comparisons we have to manage block E of equal elements on one partition

        if (Reads.compareValues(array[m2], med) == 0) {
            //[      A      ][ = ][ E ][   B    ]
            //a   a1         m1   m2   m3

            int m3 = m2, bSize = 1;
            while (++m3 < b && Reads.compareIndexValue(array, m3, med, 1, true) == 0)
                bSize++;

            int a1 = a+bSize;

            //calculate segment sizes for each half (total of 4)

            int len1 = m1-a;
            int s1 = this.calcSLen(len1/2, (len1+1)/2);
            int s2 = this.calcSLen((len1+1)/2, len1/2);

            int len2 = len1-bSize;
            int s3 = this.calcSLen(len2/2, (len2+1)/2);
            int s4 = this.calcSLen((len2+1)/2, len2/2);

            //sort runs

            this.gritSortRuns(array, a, m1, m2, s1, s2, med, -1);
            this.blockSwap(array, a, m2, bSize);
            this.gritSortRuns(array, a1, m1, m3, s3, s4, med, 1);

            if (Math.max(len1, len2) >= this.MIN_SORT) {

                //multiway stable partition around pivots

                this.gritPartition(array, a, m1, m2, s1, s2, med, -1);
                this.blockSwap(array, a, m2, bSize);
                this.gritPartition(array, a1, m1, m3, s3, s4, med, 1);

                //sort partitions

                this.gritSortBuckets(array, a, m1, m2, s1, s2, med, -1);
                this.blockSwap(array, a, m2, bSize);
                this.gritSortBuckets(array, a1, m1, m3, s3, s4, med, 1);

                //merge

                this.blockMerge(array, tmp, a,  (a+m1)/2, m1, m2, lgBLen, med, 0, 0);
                this.blockMerge(array, tmp, m3, (m3+b)/2, b,  a,  lgBLen, med, 0, 1);
            }
        }
        else if (Reads.compareValues(array[m1-1], med) == 0) {
            //[   A    ][ E ][ = ][      B      ]
            //a         m3   m1   m2        b1

            int m3 = m1, bSize = 1;
            while (--m3 > a && Reads.compareIndexValue(array, m3-1, med, 1, true) == 0)
                bSize++;

            int b1 = b-bSize;

            /*this.gritSortRuns(array, a, m3, m2, med, 1);
            Highlights.clearAllMarks();
            this.blockSwap(array, m3, b-bSize, bSize);
            this.gritSortRuns(array, a, m1, m2, med, -1);*/



            //calculate segment sizes for each half (total of 4)

            int len1 = m3-a;
            int s1 = this.calcSLen(len1/2, (len1+1)/2);
            int s2 = this.calcSLen((len1+1)/2, len1/2);

            int len2 = len1+bSize;
            int s3 = this.calcSLen(len2/2, (len2+1)/2);
            int s4 = this.calcSLen((len2+1)/2, len2/2);

            //sort runs

            this.gritSortRuns(array, a, m3, m2, s1, s2, med, -1);
            this.blockSwap(array, m3, b1, bSize);
            this.gritSortRuns(array, a, m1, m2, s3, s4, med, 1);

            if (Math.max(len1, len2) >= this.MIN_SORT) {

                //multiway stable partition around pivots

                this.gritPartition(array, a, m3, m2, s1, s2, med, -1);
                this.blockSwap(array, m3, b1, bSize);
                this.gritPartition(array, a, m1, m2, s3, s4, med, 1);

                //sort partitions

                this.gritSortBuckets(array, a, m3, m2, s1, s2, med, -1);
                this.blockSwap(array, m3, b1, bSize);
                this.gritSortBuckets(array, a, m1, m2, s3, s4, med, 1);

                //merge

                this.blockMerge(array, tmp, a,  (a+m3)/2, m3, m2, lgBLen, med, 0, 0);
                this.blockMerge(array, tmp, m2, (m2+b)/2, b,  a,  lgBLen, med, 1, 1);
            }
        }
        else {
            //[      A      ][ = ][      B      ] (E does not exist)
            //a              m1   m2

            //calculate segment sizes for each half (total of 2 x2)

            int len1 = m1-a;
            int s1 = this.calcSLen(len1/2, (len1+1)/2);
            int s2 = this.calcSLen((len1+1)/2, len1/2);

            //sort runs

            this.gritSortRuns(array, a, m1, m2, s1, s2, med, -1);
            this.gritSortRuns(array, a, m1, m2, s1, s2, med, 1);

            if (len1 >= this.MIN_SORT) {

                //multiway stable partition around pivots

                this.gritPartition(array, a, m1, m2, s1, s2, med, -1);
                this.gritPartition(array, a, m1, m2, s1, s2, med, 1);

                //sort partitions

                this.gritSortBuckets(array, a, m1, m2, s1, s2, med, -1);
                this.gritSortBuckets(array, a, m1, m2, s1, s2, med, 1);

                //merge

                this.blockMerge(array, tmp, a,  (a+m1)/2, m1, m2, lgBLen, med, 0, 0);
                this.blockMerge(array, tmp, m2, (m2+b)/2, b,  a,  lgBLen, med, 0, 1);
            }
        }
        Writes.deleteExternalArray(tmp);
    }
}