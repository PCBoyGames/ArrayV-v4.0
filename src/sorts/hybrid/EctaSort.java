package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;

/*
 *
MIT License

Copyright (c) 2020-2022 aphitorite

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

public class EctaSort extends Sort {
    public EctaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Ecta");
        this.setRunAllSortsName("Ecta Sort");
        this.setRunSortName("Ectasort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
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
        Highlights.clearAllMarks();

        while (i < m) {
            Highlights.markArray(2, i);
            Writes.write(to, p++, from[i++], 1, true, auxwrite);
        }
        while (j < b) {
            Highlights.markArray(3, j);
            Writes.write(to, p++, from[j++], 1, true, auxwrite);
        }
    }
    private void pingPongMerge(int[] array, int[] buf, int a, int m1, int m2, int m3, int b) {
        int p = 0, p1 = p + m2-a, pEnd = p + b-a;

        this.mergeTo(array, buf, a, m1, m2, p, true);
        this.mergeTo(array, buf, m2, m3, b, p1, true);
        this.mergeTo(buf, array, p, p1, pEnd, a, false);
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
    private void blockMerge(int[] array, int[] buf, int[] tags, int a, int m, int b, int bLen) {
        int c = 0, t = 2;

        int i = a, j = m, k = 0;
        int l = 0, r = 0;

        while (c++ < 2*bLen) { //merge 2 blocks into buffer to create 2 buffers
            Highlights.markArray(2, i);
            Highlights.markArray(3, j);

            if (Reads.compareValues(array[i], array[j]) <= 0) {
                Writes.write(buf, k++, array[i++], 1, true, true);
                l++;
            }
            else {
                Writes.write(buf, k++, array[j++], 1, true, true);
                r++;
            }
        }

        boolean left = l >= r;
        k = left ? i-l : j-r;

        c = 0;

        do {
            if (i < m) Highlights.markArray(2, i);
            else      Highlights.clearMark(2);
            if (j < b) Highlights.markArray(3, j);
            else      Highlights.clearMark(3);

            if (i < m && (j == b || Reads.compareValues(array[i], array[j]) <= 0)) {
                Writes.write(array, k++, array[i++], 1, true, false);
                l++;
            }
            else {
                Writes.write(array, k++, array[j++], 1, true, false);
                r++;
            }
            if (++c == bLen) { //change buffer after every block
                Writes.write(tags, t++, (k-a)/bLen-1, 0, false, true);

                if (left) l -= bLen;
                else     r -= bLen;

                left = l >= r;
                k = left ? i-l : j-r;

                c = 0;
            }
        }
        while (i < m || j < b);

        Highlights.clearAllMarks();

        int b1 = b-c;

        Writes.arraycopy(array, k-c, array, b1, c, 1, true, false); //copy remainder to end (r buffer)
        r -= c;

        //l and r buffers are divisible by bLen
        t = 0; k = 0;

        while (l > 0) {
            Writes.arraycopy(buf, k, array, m-l, bLen, 1, true, false);
            Writes.write(tags, t++, (m-a-l)/bLen, 0, false, true);
            k += bLen;
            l -= bLen;
        }
        while (r > 0) {
            Writes.arraycopy(buf, k, array, b1-r, bLen, 1, true, false);
            Writes.write(tags, t++, (b1-a-r)/bLen, 0, false, true);
            k += bLen;
            r -= bLen;
        }
        this.blockCycle(array, buf, tags, a, bLen, (b-a)/bLen);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int a = 0, b = length;
        BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);

        if (length <= 32) {
            smallSort.customBinaryInsert(array, a, b, 0.5);
            return;
        }

        int bLen   = 1 << (32-Integer.numberOfLeadingZeros(length-1))/2,
            tLen   = length/bLen,
            bufLen = 2*bLen;

        int j = 16;

        int[] buf  = Writes.createExternalArray(bufLen);
        int[] tags = Writes.createExternalArray(tLen);

        //insertion

        for (int i = a; i < b; i += j)
            smallSort.customBinaryInsert(array, i, Math.min(i+j, b), 0.25);

        //merging w/ buffer

        for (int i; 4*j <= bufLen; j *= 4) {
            for (i = a; i+2*j < b; i += 4*j)
                this.pingPongMerge(array, buf, i, i+j, i+2*j, Math.min(i+3*j, b), Math.min(i+4*j, b));
            if (i+j < b)
                this.mergeBWExt(array, buf, i, i+j, b);
        }

        for (; j <= bufLen; j *= 2)
            for (int i = a; i+j < b; i += 2*j)
                this.mergeBWExt(array, buf, i, i+j, Math.min(i+2*j, b));

        //block merge

        for (int i; j < length; j *= 2) {
            for (i = a; i+j+bufLen < b; i += 2*j)
                this.blockMerge(array, buf, tags, i, i+j, Math.min(i+2*j, b), bLen);
            if (i+j < b)
                this.mergeBWExt(array, buf, i, i+j, b);
        }

        Writes.deleteExternalArray(buf);
        Writes.deleteExternalArray(tags);
    }
}