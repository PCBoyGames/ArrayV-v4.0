package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;

/*
 *
MIT License

Copyright (c) 2024 aphitorite

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

public class HalfEctaSort extends Sort {
    public HalfEctaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Half Ecta");
        this.setRunAllSortsName("Half Ectasort");
        this.setRunSortName("Half Ectasort");
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
        while (i < m) {
            Highlights.markArray(2, i);
            Writes.write(to, p++, from[i++], 1, true, auxwrite);
        }
        Highlights.clearMark(2);

        while (j < b) {
            Highlights.markArray(3, j);
            Writes.write(to, p++, from[j++], 1, true, auxwrite);
        }
        Highlights.clearMark(3);
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
            if (i != keys[i]) {
                Writes.arraycopy(array, a + i*bLen, buf, 0, bLen, 1, true, true);
                int j = i, next = keys[i];

                do {
                    Writes.arraycopy(array, a + next*bLen, array, a + j*bLen, bLen, 1, true, false);
                    Writes.write(keys, j, j, 1, true, true);

                    j = next;
                    next = keys[next];
                }
                while (next != i);

                Writes.arraycopy(buf, 0, array, a + j*bLen, bLen, 1, true, false);
                Writes.write(keys, j, j, 1, true, true);
            }
        }
    }
    //precondition: m-a is >= bLen and divisible by bLen
    private void blockMerge(int[] array, int[] buf, int[] tags, int a, int m, int b, int bLen) {
        int b1 = b-(b-m)%bLen;

        if(b1-m <= bLen)
            this.mergeBWExt(array, buf, a, m, b1);

        else {
            int i = a, j = m, l = a, r = m, tp = 1;

            for(int k = 0; k < bLen; k++) {
                int t = Reads.compareIndices(array, i, j, 0.5, true) <= 0 ?
                        array[i++] : array[j++];

                Writes.write(buf, k, t, 0.5, false, true);
            }
            while(l < m && r < b1) {
                boolean left = i-l > 0 && (i-l == bLen || Reads.compareIndices(array, l+bLen-1, r+bLen-1, 1, true) <= 0);
                int p = left ? l : r;

                for(int k = 0; k < bLen; k++) {
                    int t = j == b1 || (i < m && Reads.compareIndices(array, i, j, 0.5, true) <= 0) ?
                            array[i++] : array[j++];

                    Highlights.markArray(3, p);
                    Writes.write(array, p++, t, 0.5, false, false);
                }
                if(left) l = p;
                else     r = p;

                Highlights.clearMark(3);
                Writes.write(tags, tp++, (p-a)/bLen-1, 1, false, true);
            }
            Highlights.clearMark(2);

            int p = l < m ? l : r;

            Writes.arraycopy(buf, 0, array, p, bLen, 0.5, true, false);
            Writes.write(tags, 0, (p-a)/bLen, 1, false, true);

            while((l += bLen) < m)  Writes.write(tags, tp++, (l-a)/bLen, 1, false, true);
            while((r += bLen) < b1) Writes.write(tags, tp++, (r-a)/bLen, 1, false, true);

            this.blockCycle(array, buf, tags, a, bLen, (b-a)/bLen);
        }
        this.mergeBWExt(array, buf, a, b1, b);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int a = 0, b = length;
        BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);

        if (length <= 32) {
            smallSort.customBinaryInsert(array, a, b, 0.5);
            return;
        }

        int bLen = 1 << (32-Integer.numberOfLeadingZeros(length))/2,
            tLen = length/bLen;

        int j = 16;

        int[] buf  = Writes.createExternalArray(bLen);
        int[] tags = Writes.createExternalArray(tLen);

        //insertion

        for (int i = a; i < b; i += j)
            smallSort.customBinaryInsert(array, i, Math.min(i+j, b), 0.25);

        //merging w/ buffer

        for (int i; 4*j <= bLen; j *= 4) {
            for (i = a; i+2*j < b; i += 4*j)
                this.pingPongMerge(array, buf, i, i+j, i+2*j, Math.min(i+3*j, b), Math.min(i+4*j, b));
            if (i+j < b)
                this.mergeBWExt(array, buf, i, i+j, b);
        }

        for (; j <= bLen; j *= 2)
            for (int i = a; i+j < b; i += 2*j)
                this.mergeBWExt(array, buf, i, i+j, Math.min(i+2*j, b));

        //block merge

        for (int i; j < length; j *= 2) {
            for (i = a; i+j+bLen < b; i += 2*j)
                this.blockMerge(array, buf, tags, i, i+j, Math.min(i+2*j, b), bLen);
            if (i+j < b)
                this.mergeBWExt(array, buf, i, i+j, b);
        }

        Writes.deleteExternalArray(buf);
        Writes.deleteExternalArray(tags);
    }
}