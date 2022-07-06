package sorts.hybrid;

import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;
import main.ArrayVisualizer;

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

final public class LogMergeSort extends Sort {
    public LogMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Log Merge");
        this.setRunAllSortsName("Log Merge Sort");
        this.setRunSortName("Log Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Set block size (default: calculates minimum block length for current length)", 1);
    }

    private int log2(int n) {
        return 31-Integer.numberOfLeadingZeros(n);
    }
    private int productLog(int n) {
        int r = 1;
        while (r*((1<<r)+1)-1 < n) r++;
        return r;
    }

    private int medianOf3(int[] array, int[] indices) {
        // small length cases

        // maybe an error would be better but w/e
        if (indices.length == 0) return -1;

        // median of 1 or 2 elements can just be the first
        if (indices.length < 3) return indices[0];

        // 3 element case (common)
        // only first 3 elements are considered if given an array of 4+ indices
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
        // anti-overflow with good rounding
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

    private int mOMHelper(int[] array, int start, int length) {
        if (length == 1) return start;

        int[] meds = new int[3];
        int third = length / 3;
        meds[0] = mOMHelper(array, start, third);
        meds[1] = mOMHelper(array, start + third, third);
        meds[2] = mOMHelper(array, start + 2 * third, third);

        return medianOf3(array, meds);
    }

    private int medianOfMedians(int[] array, int start, int length) {
        if (length == 1) return start;

        int[] meds = new int[3];

        int nearPower = (int) Math.pow(3, Math.round(Math.log(length)/Math.log(3)));
        if (nearPower == length)
            return mOMHelper(array, start, length);

        nearPower /= 3;
        // uncommon but can happen with numbers slightly smaller than 2*3^k
        // (e.g., 17 < 18 or 47 < 54)
        if (2*nearPower >= length) nearPower /= 3;

        meds[0] = mOMHelper(array, start, nearPower);
        meds[2] = mOMHelper(array, start + length - nearPower, nearPower);
        meds[1] = medianOfMedians(array, start + nearPower, length - 2 * nearPower);

        return medianOf3(array, meds);
    }

    private void blockSwap(int[] array, int a, int b, int s) {
        while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }

    //@param pCmp - 0 for < piv, 1 for <= piv
    private boolean pivCmp(int v, int piv, int pCmp) {
        return Reads.compareValues(v, piv) < pCmp;
    }

    private void pivBufSet(int[] array, int pa, int pb, int v, int wLen) {
        while (wLen-- > 0) {
            if ((v & 1) == 1) Writes.swap(array, pa+wLen, pb+wLen, 1, true, false);
            v >>= 1;
        }
    }
    //@param bit - < pivot means this bit
    private int pivBufGet(int[] array, int pa, int piv, int pCmp, int wLen, int bit) {
        int r = 0;

        while (wLen-- > 0) {
            r <<= 1;
            r |= (this.pivCmp(array[pa++], piv, pCmp) ? bit : bit^1);
        }
        return r;
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

    private void blockCycle(int[] array, int p, int n, int p1, int bLen, int wLen, int piv, int pCmp, int bit) {
        for (int i = 0; i < n; i++) {
            int dest = this.pivBufGet(array, p+i*bLen, piv, pCmp, wLen, bit);

            while (dest != i) {
                this.blockSwap(array, p+i*bLen, p+dest*bLen, bLen);
                dest = this.pivBufGet(array, p+i*bLen, piv, pCmp, wLen, bit);
            }
            this.pivBufSet(array, p+i*bLen, p1+i*bLen, i, wLen);
        }
    }

    private int blockPartition(int[] array, int[] swap, int a, int b, int bLen, int piv, int pCmp) {
        for (; a < b; a++) {
            Highlights.markArray(1, a);
            Delays.sleep(0.25);
            if (!this.pivCmp(array[a], piv, pCmp)) break;
        }
        for (; b > a; b--) {
            Highlights.markArray(1, b-1);
            Delays.sleep(0.25);
            if (this.pivCmp(array[b-1], piv, pCmp)) break;
        }
        if (b-a <= 2*bLen) return this.partitionEasy(array, swap, a, b, piv, pCmp);

        Highlights.clearMark(2);

        //sort blocks and type blocks

        int p = a;
        int l = 0, r = 0;
        int lb = 0, rb = 0;

        for (int i = a; i < b; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.25);

            if (this.pivCmp(array[i], piv, pCmp)) {
                Writes.write(swap, l++, array[i], 0.25, false, true);

                if (l == bLen) {
                    Writes.arraycopy(swap, 0, array, p, bLen, 0.5, true, false);
                    l = 0;
                    lb++;
                    p += bLen;
                }
            }
            else {
                Writes.write(swap, bLen+(r++), array[i], 0.25, false, true);

                if (r == bLen) {
                    Writes.arraycopy(swap, bLen, array, p, bLen, 0.5, true, false);
                    r = 0;
                    rb++;
                    p += bLen;
                }
            }
        }

        //sort blocks

        Highlights.clearMark(3);

        int min = Math.min(lb, rb);
        int m = a+lb*bLen;

        if (min > 0) {
            int bCnt = lb+rb;
            int wLen = this.log2(min-1)+1; //ceil(log2(min))

            for (int i = 0, j = 0, k = 0; i < min; i++) { //set bit buffers
                while (!this.pivCmp(array[a+j*bLen+wLen], piv, pCmp)) j++;
                while ( this.pivCmp(array[a+k*bLen+wLen], piv, pCmp)) k++;
                this.pivBufSet(array, a+(j++)*bLen, a+(k++)*bLen, i, wLen);
            }

            if (lb < rb) {
                for (int i = bCnt-1, j = 0; j < rb; i--) //swap right to left
                    if (!this.pivCmp(array[a+i*bLen+wLen], piv, pCmp))
                        this.blockSwap(array, a+i*bLen, a+(bCnt-(++j))*bLen, bLen);

                this.blockCycle(array, a, lb, m, bLen, wLen, piv, pCmp, 0);
            }
            else {
                for (int i = 0, j = 0; j < lb; i++) //swap left to right
                    if (this.pivCmp(array[a+i*bLen+wLen], piv, pCmp))
                        this.blockSwap(array, a+i*bLen, a+(j++)*bLen, bLen);

                this.blockCycle(array, m, rb, a, bLen, wLen, piv, pCmp, 1);
            }
        }

        //handle leftover

        Writes.arraycopy(swap, bLen, array, b-r, r, 1, true, false);

        if (l > 0) {
            Highlights.clearMark(2);
            Writes.arraycopy(array, a+lb*bLen, array, a+lb*bLen+l, rb*bLen, 1, true, false);
            Writes.arraycopy(swap, 0, array, a+lb*bLen, l, 1, true, false);
        }
        return a+lb*bLen+l;
    }

    private void selectMedian(int[] array, int[] aux, int a, int b, int bLen) {
        int med = (a+b)/2;
        boolean badPartition = false;

        while (b-a > 32) {
            int p;

            if (badPartition) {
                int n = b-a;
                n -= ~n&1; // even lengths bad
                p = this.medianOfMedians(array, a, n);
                badPartition = false;
            }
            else p = this.medianOf9(array, a, b);

            Highlights.markArray(3, p);
            int m = this.blockPartition(array, aux, a, b, bLen, array[p], 0);

            if (m == a) {
                Highlights.markArray(3, p);
                m = this.blockPartition(array, aux, a, b, bLen, array[p], 1);

                if (med >= a && med < m) return;
            }

            int left  = m-a;
            int right = b-m;
            badPartition = 8*left < right || 8*right < left;

            if (m <= med) a = m;
            else         b = m;
        }
        BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
        smallSort.customBinaryInsert(array, a, b, 0.25);
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

    private void mergeTo(int[] from, int[] to, int a, int m, int b, int p, boolean auxwrite) {
        int i = a, j = m;

        while (i < m && j < b) {
            Highlights.markArray(2, i);
            Highlights.markArray(3, j);

            if (Reads.compareValues(from[i], from[j]) <= 0)
                Writes.write(to, p++, from[i++], 1, true, auxwrite);
            else
                Writes.write(to, p++, from[j++], 1, true, auxwrite);
        }
        Highlights.clearMark(3);

        while (i < m) {
            Highlights.markArray(2, i);
            Writes.write(to, p++, from[i++], 1, true, auxwrite);
        }
        while (j < b) {
            Highlights.markArray(2, j);
            Writes.write(to, p++, from[j++], 1, true, auxwrite);
        }
        Highlights.clearMark(2);
    }
    private void smartMerge(int[] array, int[] buf, int a, int m, int b) {
        if (Reads.compareIndices(array, m-1, m, 1, true) <= 0) return;

        a = this.rightBinSearch(array, a, m, array[m]);
        b = this.leftBinSearch(array, m, b, array[m-1]);

        if (m-a <= b-m) this.mergeFWExt(array, buf, a, m, b);
        else           this.mergeBWExt(array, buf, a, m, b);
    }
    private void pingPongMerge(int[] array, int[] buf, int a, int m1, int m2, int m3, int b) {
        int p = 0, p1 = p + m2-a, pEnd = p + b-a;

        if (Reads.compareIndices(array, m1-1, m1, 1, true) > 0
        || Reads.compareIndices(array, m3-1, m3, 1, true) > 0) {
            this.mergeTo(array, buf, a, m1, m2, p, true);
            this.mergeTo(array, buf, m2, m3, b, p1, true);
            this.mergeTo(buf, array, p, p1, pEnd, a, false);
        }
        else this.smartMerge(array, buf, a, m2, b);
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

    private void blockMerge(int[] array, int[] swap, int a, int m, int b, int p, int bLen, int piv, int pCmp, int bit) {
        if (Reads.compareIndices(array, m-1, m, 1, true) <= 0) return;

        b = this.leftBinSearch(array, m, b, array[m-1]);

        if (b-m <= 2*bLen) {
            this.mergeBWExt(array, swap, a, m, b);
            return;
        }

        int a1 = this.rightBinSearch(array, a, m, array[m]);

        if (m-a1 <= 2*bLen) {
            this.mergeFWExt(array, swap, a1, m, b);
            return;
        }

        a = a1-(a1-a)%bLen;

        int bCnt = (b-a)/bLen-2, wLen = this.log2(bCnt-1)+1;

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
                this.pivBufSet(array, k-bLen, pc, t++, wLen);
                pc += bLen;

                if (left) l -= bLen;
                else     r -= bLen;

                left = l >= r;
                k = left ? i-l : j-r;

                c = 0;
            }
        }
        while (i < m);

        int b1 = b-c;

        Writes.arraycopy(array, k-c, array, b1, c, 1, true, false); //swap remainder to end (r buffer)
        r -= c;
        l = Math.min(l, m-a-l);

        //l and r buffers are divisible by bLen

        Writes.arraycopy(array, a,   array, m-l,  l, 1, true, false); //swap l buffer to front
        Writes.arraycopy(array, a+l, array, b1-r, r, 1, true, false); //swap r buffer to front
        Writes.arraycopy(swap,  0, array, a, 2*bLen, 1, true, false); //swap first merged elements to correct position in front

        this.blockCycle(array, a+2*bLen, bCnt, p, bLen, wLen, piv, pCmp, bit);
    }

    private void logMerge(int[] array, int[] aux, int a, int b, int p, int bLen, int piv, int pCmp, int bit) {
        int j = 16;

        BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);

        for (int i = a; i < b; i += j)
            smallSort.customBinaryInsert(array, i, Math.min(i+j, b), 0.25);

        for (int i; 2*j <= bLen; j *= 4) {
            for (i = a; i+2*j < b; i += 4*j)
                this.pingPongMerge(array, aux, i, i+j, i+2*j, Math.min(i+3*j, b), Math.min(i+4*j, b));
            if (i+j < b)
                this.smartMerge(array, aux, i, i+j, b);
        }

        for (; j <= 2*bLen; j *= 2)
            for (int i = a; i+j < b; i += 2*j)
                this.smartMerge(array, aux, i, i+j, Math.min(i+2*j, b));

        for (; j < b-a; j *= 2)
            for (int i = a; i+j < b; i += 2*j)
                this.blockMerge(array, aux, i, i+j, Math.min(i+2*j, b), p, bLen, piv, pCmp, bit);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int bLen = Math.max(this.productLog(length), Math.min(bucketCount, length/2));
        bLen = 1 << (this.log2(bLen-1)+1);

        int[] aux = Writes.createExternalArray(2*bLen);

        int a = 0, b = length;
        int m1 = a+length/2, m2 = m1+(length&1);

        this.selectMedian(array, aux, a, b, bLen);

        if (Reads.compareIndices(array, a, b-1, 1, true) == 0) return;

        int piv = array[m1];

        for (; Reads.compareIndices(array, m1-1, m2, 1, true) == 0; m1--, m2++);

        int pCmp = Reads.compareIndexValue(array, m1-1, piv, 1, true) == 0 ? 1 : 0; //<= piv else < piv

        this.logMerge(array, aux, a,  m1, m2, bLen, piv, pCmp, 0);
        this.logMerge(array, aux, m2, b,  a,  bLen, piv, pCmp, 1);
    }
}