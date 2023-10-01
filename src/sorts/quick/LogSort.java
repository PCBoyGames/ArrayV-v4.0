package sorts.quick;

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

public class LogSort extends Sort {
    public LogSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Log");
        this.setRunAllSortsName("Log Sort");
        this.setRunSortName("Logsort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Set block size (default: calculates minimum block length for current length)", 1);
    }

    //special thanks to Distray for revising the code / algorithm
    //median selection and sort helper code from AeosQSort by Anonymous0726

    private int productLog(int n) {
        int r = 1;
        while ((r<<r)+r-1 < n) r++;
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
                Writes.write(array, p+(l++), array[i], 0.25, true, false);

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
            int wLen = 32 - Integer.numberOfLeadingZeros(min-1); //ceil(log2(min))

            for (int i = 0, j = 0, k = 0; i < min; i++) { //set bit buffers
                while (!this.pivCmp(array[a+j*bLen+wLen], piv, pCmp)) j++;
                while ( this.pivCmp(array[a+k*bLen+wLen], piv, pCmp)) k++;
                this.pivBufSet(array, a+(j++)*bLen, a+(k++)*bLen, i, wLen);
            }

            if (lb < rb) {
                for (int i = bCnt-1, j = 0; j < rb; i--) //swap right to left
                    if (!this.pivCmp(array[a+i*bLen+wLen], piv, pCmp))
                        this.blockSwap(array, a+i*bLen, a+(bCnt-(++j))*bLen, bLen);

                for (int i = 0; i < lb; i++) { //block cycle
                    int dest = this.pivBufGet(array, a+i*bLen, piv, pCmp, wLen, 0);

                    while (dest != i) {
                        this.blockSwap(array, a+i*bLen, a+dest*bLen, bLen);
                        dest = this.pivBufGet(array, a+i*bLen, piv, pCmp, wLen, 0);
                    }
                    this.pivBufSet(array, a+i*bLen, m+i*bLen, i, wLen);
                }
            }
            else {
                for (int i = 0, j = 0; j < lb; i++) //swap left to right
                    if (this.pivCmp(array[a+i*bLen+wLen], piv, pCmp))
                        this.blockSwap(array, a+i*bLen, a+(j++)*bLen, bLen);

                for (int i = 0; i < rb; i++) { //block cycle
                    int dest = this.pivBufGet(array, m+i*bLen, piv, pCmp, wLen, 1);

                    while (dest != i) {
                        this.blockSwap(array, m+i*bLen, m+dest*bLen, bLen);
                        dest = this.pivBufGet(array, m+i*bLen, piv, pCmp, wLen, 1);
                    }
                    this.pivBufSet(array, a+i*bLen, m+i*bLen, i, wLen);
                }
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

    private void logSort(int[] array, int[] aux, int a, int b, int bLen, boolean badPartition) {
        while (b-a > 32) {
            int p;

            if (badPartition) {
                int n = b-a;
                n -= ~n&1; // even lengths bad
                p = this.medianOfMedians(array, a, n);
                badPartition = false;
            }
            else p = this.medianOf9(array, a, b);

            Highlights.markArray(2, p);
            int m = this.blockPartition(array, aux, a, b, bLen, array[p], 0);
            int left = m-a, right = b-m;

            if (m == a) { //few unique
                m = this.blockPartition(array, aux, a, b, bLen, array[p], 1);
                badPartition = left*8 < right;
                a = m;
            }
            else {
                if (right < left) {
                    badPartition = right*8 < left;
                    this.logSort(array, aux, m, b, bLen, badPartition);
                    b = m;
                }
                else {
                    badPartition = left*8 < right;
                    this.logSort(array, aux, a, m, bLen, badPartition);
                    a = m;
                }
            }
        }
        BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);
        smallSort.customBinaryInsert(array, a, b, 0.25);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int bLen = Math.max(this.productLog(length), Math.min(bucketCount, length));
        int[] aux = Writes.createExternalArray(bLen);

        this.logSort(array, aux, 0, length, bLen, false);
    }
}