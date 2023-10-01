package sorts.hybrid;

import sorts.templates.BlockMergeSorting;
import main.ArrayVisualizer;

/*
 *
MIT License

Copyright (c) 2021 aphitorite

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

public class SynchronousEctaSort extends BlockMergeSorting {
    public SynchronousEctaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Synchronous Ecta");
        this.setRunAllSortsName("Synchronous Ecta Sort");
        this.setRunSortName("Synchronous Ectasort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void writeTo(int[] array, int to, int from) {
        Highlights.markArray(2, from);
        Writes.write(array, to, array[from], 1, true, false);
    }

    private void mergeWithBufFWHead(int[] array, int[] tmp, int m, int b) {
        int lenB = b-m, pb = tmp.length;
        int pa = pb-lenB, p = pa-lenB;

        while (pa < pb && m < b) {
            Highlights.markArray(2, m);

            if (Reads.compareValues(tmp[pa], array[m]) <= 0)
                Writes.write(tmp, p++, tmp[pa++], 1, true, true);
            else
                Writes.write(tmp, p++, array[m++], 1, true, true);
        }
        if (p < pa)
            while (pa < pb) Writes.write(tmp, p++, tmp[pa++], 1, true, true);
        while (m < b)
            Writes.write(tmp, p++, array[m++], 1, true, true);
    }
    private void mergeWithBufBWHead(int[] array, int[] tmp, int m, int b) {
        int lenB = b-m, pb = tmp.length;
        int pa = pb-lenB, p = b+lenB;
        pb--; b--;

        while (pb >= pa && b >= m) {
            Highlights.markArray(2, b);

            if (Reads.compareValues(tmp[pb], array[b]) > 0)
                Writes.write(array, --p, tmp[pb--], 1, true, false);
            else
                Writes.write(array, --p, array[b--], 1, true, false);
        }
        if (p > b+1)
            while (b >= m) Writes.write(array, --p, array[b--], 1, true, false);
        while (pb >= pa)
            Writes.write(array, --p, tmp[pb--], 1, true, false);
    }

    private int blockMergeEasy(int[] array, int[] idx, int i, int j, int a, int m, int b, int bLen, int bCnt, int[] p) {
        int c = (j-p[1] > i-p[0]) ? 1 : 0;
        int k = 0;

        while (i < m || j < b) {
            if (i < m && j < b) {
                if (Reads.compareValues(array[i], array[j]) <= 0)
                    this.writeTo(array, p[c]++, i++);
                else
                    this.writeTo(array, p[c]++, j++);
            }
            else if (i < m)
                this.writeTo(array, p[c]++, i++);
            else
                this.writeTo(array, p[c]++, j++);

            if (++k == bLen) {
                Writes.write(idx, bCnt++, p[c]/bLen-1, 0, false, true);
                c = (j-p[1] > i-p[0]) ? 1 : 0;
                k = 0;
            }
        }
        while (k-- > 0) this.writeTo(array, --b, --p[c]);

        return bCnt;
    }
    private int blockMergeHead(int[] array, int[] tmp, int[] idx, int a, int m, int b, int bLen, int bCnt, int[] p) {
        p[0] = a; p[1] = m;
        int i = a, j = m, bufLen = 2*bLen;

        for (int k = 0; k < bufLen; k++) {
            if (i < m && j < b) {
                if (Reads.compareValues(array[i], array[j]) <= 0)
                    Writes.write(tmp, k, array[i++], 1, true, true);
                else
                    Writes.write(tmp, k, array[j++], 1, true, true);
            }
            else if (i < m)
                Writes.write(tmp, k, array[i++], 1, true, true);
            else
                Writes.write(tmp, k, array[j++], 1, true, true);
        }
        return this.blockMergeEasy(array, idx, i, j, a, m, b, bLen, bCnt, p);
    }
    private int blockMerge(int[] array, int[] idx, int a, int m, int b, int bLen, int bCnt, int[] p) {
        int i = a, j = m;

        for (int pCnt = 2-(a-p[1])/bLen; pCnt > 0; pCnt--) {
            Writes.write(idx, bCnt++, p[0]/bLen, 0, false, true);

            for (int k = 0; k < bLen; k++) {
                if (i < m && j < b) {
                    if (Reads.compareValues(array[i], array[j]) <= 0)
                        this.writeTo(array, p[0]++, i++);
                    else
                        this.writeTo(array, p[0]++, j++);
                }
                else if (i < m)
                    this.writeTo(array, p[0]++, i++);
                else
                    this.writeTo(array, p[0]++, j++);
            }
        }
        p[0] = p[1]; p[1] = m;

        return this.blockMergeEasy(array, idx, i, j, a, m, b, bLen, bCnt, p);
    }
    private void blockCycle(int[] array, int[] buf, int[] keys, int a, int bLen, int bCnt) {
        for (int i = 0; i < bCnt; i++) {
            if (Reads.compareOriginalValues(i, keys[i]) != 0) {
                Writes.arraycopy(array, a + i*bLen, buf, 0, bLen, 1, true, true);
                int j = i, next = keys[i];

                do {
                    Writes.arraycopy(array, a + next*bLen, array, a + j*bLen, bLen, 1, true, false);
                    Writes.write(keys, j, j, 1, true, true);

                    j = next;
                    next = keys[next];
                }
                while (Reads.compareOriginalValues(next, i) != 0);

                Writes.arraycopy(buf, 0, array, a + j*bLen, bLen, 1, true, false);
                Writes.write(keys, j, j, 1, true, true);
            }
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        if (length <= 16) this.binaryInsertion(array, 0, length);

        int bLen = 1;
        while (bLen*bLen < length/2) bLen *= 2;

        int bufLen = 2*bLen, tLen = length/bLen;

        int[] tmp = Writes.createExternalArray(bufLen);
        int[] idx = Writes.createExternalArray(tLen);

        int a = 0, b = length;
        int n = b-a, i = a+1, j = 1, j2 = 2*j;

        boolean cmp = Reads.compareValues(array[i-1], array[i]) > 0;
        Writes.write(tmp, bufLen-2, array[i-(cmp?0:1)], 1, true, true);
        Writes.write(tmp, bufLen-1, array[i-(cmp?1:0)], 1, true, true);

        for (i += 2; i < b; i += 2) {
            cmp = Reads.compareValues(array[i-1], array[i]) > 0;
            Writes.write(array, i-3, array[i-(cmp?0:1)], 1, true, false);
            Writes.write(array, i-2, array[i-(cmp?1:0)], 1, true, false);
        }
        if (i == b) Writes.write(array, i-3, array[i-1], 1, true, false);

        for (b -= j2, j = j2, j2 *= 2; j < bufLen; b -= j, j = j2, j2 *= 2) {
            this.mergeWithBufFWHead(array, tmp, a, a+j);

            for (i = a+j; i+j < b; i += j2)
                this.mergeWithBufFWExt(array, i, i+j, Math.min(i+j2, b), i-j);

            this.shiftFWExt(array, i-j, i, b);
        }
        int a1 = a+j;
        for (i = a1; i+j < b; i += j2);

        this.shiftBWExt(array, i, b, b+j);

        for (i -= j2; i >= a1; i -= j2) {
            int k = Math.min(i+j2, b);
            this.mergeWithBufBWExt(array, i, i+j, k, k+j);
        }
        this.mergeWithBufBWHead(array, tmp, a, a1);
        Highlights.clearMark(2);

        int[] p = new int[2];

        for (b = a+n, j = j2, j2 *= 2; j < n; j = j2, j2 *= 2) {
            int bCnt = 2;
            bCnt = this.blockMergeHead(array, tmp, idx, a, a+j, Math.min(a+j2, b), bLen, bCnt, p);

            for (i = a+j2; i+j < b; i += j2)
                bCnt = this.blockMerge(array, idx, i, i+j, Math.min(i+j2, b), bLen, bCnt, p);

            Highlights.clearMark(2);

            i -= j2;
            int c = 0, e = i+j;
            while (p[0] < e) {
                Writes.arraycopy(tmp, c*bLen, array, p[0], bLen, 1, true, false);
                Writes.write(idx, c++, p[0]/bLen, 0, false, true);
                p[0] += bLen;
            }
            e = Math.min(i+j2, b);
            while (e-p[1] >= bLen) {
                Writes.arraycopy(tmp, c*bLen, array, p[1], bLen, 1, true, false);
                Writes.write(idx, c++, p[1]/bLen, 0, false, true);
                p[1] += bLen;
            }
            this.blockCycle(array, tmp, idx, 0, bLen, bCnt);
        }
    }
}