package sorts.hybrid;

import sorts.templates.Sort;
import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;

/*
 *
MIT License

Copyright (c) 2022 Control, implemented by aphitorite

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

final public class KitaSort extends Sort {
    public KitaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Kita");
        this.setRunAllSortsName("Kita Sort");
        this.setRunSortName("Kitasort");
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

        while(i < m && j < b) {
            Highlights.markArray(2, i);
            Highlights.markArray(3, j);

            if(Reads.compareValues(from[i], from[j]) <= 0)
                Writes.write(to, p++, from[i++], 1, true, auxwrite);
            else
                Writes.write(to, p++, from[j++], 1, true, auxwrite);
        }
        Highlights.clearAllMarks();

        while(i < m) {
            Highlights.markArray(2, i);
            Writes.write(to, p++, from[i++], 1, true, auxwrite);
        }
        while(j < b) {
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

        while(i >= 0 && j >= a) {
            Highlights.markArray(2, j);

            if(Reads.compareValues(tmp[i], array[j]) >= 0)
                Writes.write(array, --b, tmp[i--], 1, true, false);
            else
                Writes.write(array, --b, array[j--], 1, true, false);
        }
        Highlights.clearAllMarks();

        while(i >= 0) Writes.write(array, --b, tmp[i--], 1, true, false);
    }

    private void blockMerge(int[] array, int[] buf, int[] tags, int[] tTmp, int a, int m, int b, int bLen) {
        int ta = a/bLen, tm = m/bLen, tb = b/bLen,
            ti = ta, tj = tm, i = a+tags[ti]*bLen, j = m+tags[tj]*bLen,
            c = 0, ci = 0, cj = 0, bi = ti, bj = tj, l = 0, r = 0, t = 2, p;

        boolean lBuf, lLeft = true, rLeft = true;

        Highlights.markArray(2, i);
        Highlights.markArray(3, j);

        for(int k = 0; k < 2*bLen; k++) {
            if(lLeft && (!rLeft || Reads.compareValues(array[i], array[j]) <= 0)) {
                Writes.write(buf, k, array[i++], 1, true, true);
                l++;

                if(++ci == bLen) {
                    if(++ti == tm) {
                        lLeft = false;
                        Highlights.clearMark(2);
                    }
                    else {
                        i = a+tags[ti]*bLen;
                        ci = 0;
                        Highlights.markArray(2, i);
                    }
                }
                else Highlights.markArray(2, i);
            }
            else {
                Writes.write(buf, k, array[j++], 1, true, true);
                r++;

                if(++cj == bLen) {
                    if(++tj == tb) {
                        rLeft = false;
                        Highlights.clearMark(3);
                    }
                    else {
                        j = m+tags[tj]*bLen;
                        cj = 0;
                        Highlights.markArray(3, j);
                    }
                }
                else Highlights.markArray(3, j);
            }
        }

        lBuf = l >= r;
        p = lBuf ? a+tags[bi]*bLen : m+tags[bj]*bLen;

        do {
            if(lLeft && (!rLeft || Reads.compareValues(array[i], array[j]) <= 0)) {
                Writes.write(array, p++, array[i++], 1, true, false);
                l++;

                if(++ci == bLen) {
                    if(++ti == tm) {
                        lLeft = false;
                        Highlights.clearMark(2);
                    }
                    else {
                        i = a+tags[ti]*bLen;
                        ci = 0;
                        Highlights.markArray(2, i);
                    }
                }
                else Highlights.markArray(2, i);
            }
            else {
                Writes.write(array, p++, array[j++], 1, true, false);
                r++;

                if(++cj == bLen) {
                    if(++tj == tb) {
                        rLeft = false;
                        Highlights.clearMark(3);
                    }
                    else {
                        j = m+tags[tj]*bLen;
                        cj = 0;
                        Highlights.markArray(3, j);
                    }
                }
                else Highlights.markArray(3, j);
            }
            if(++c == bLen) {
                if(lBuf) {
                    l -= bLen;
                    Writes.write(tTmp, t++, tags[bi++], 0, false, true);
                }
                else {
                    r -= bLen;
                    Writes.write(tTmp, t++, tags[bj++]+tm-ta, 0, false, true);
                }

                lBuf = l >= r;
                p = lBuf ? a+tags[bi]*bLen : m+tags[bj]*bLen;
                c = 0;
            }
        }
        while(lLeft || rLeft);

        p = 0; t = 0;

        while(l > 0) {
            Writes.arraycopy(buf, p, array, a+tags[bi]*bLen, bLen, 1, true, false);
            Writes.write(tTmp, t++, tags[bi++], 0, false, true);
            p += bLen;
            l -= bLen;
        }
        while(r > 0) {
            Writes.arraycopy(buf, p, array, m+tags[bj]*bLen, bLen, 1, true, false);
            Writes.write(tTmp, t++, tags[bj++]+tm-ta, 0, false, true);
            p += bLen;
            r -= bLen;
        }
        Writes.arraycopy(tTmp, 0, tags, ta, tb-ta, 0, false, true);
    }
    private void blockCycle(int[] array, int[] buf, int[] keys, int a, int bLen, int bCnt) {
        for(int i = 0; i < bCnt; i++) {
            if(Reads.compareOriginalValues(i, keys[i]) != 0) {
                Writes.arraycopy(array, a + i*bLen, buf, 0, bLen, 1, true, true);
                int j = i, next = keys[i];

                do {
                    Writes.arraycopy(array, a + next*bLen, array, a + j*bLen, bLen, 1, true, false);
                    Writes.write(keys, j, j, 1, true, true);

                    j = next;
                    next = keys[next];
                }
                while(Reads.compareOriginalValues(next, i) != 0);

                Writes.arraycopy(buf, 0, array, a + j*bLen, bLen, 1, true, false);
                Writes.write(keys, j, j, 1, true, true);
            }
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int a = 0, b = length;
        BinaryInsertionSort smallSort = new BinaryInsertionSort(this.arrayVisualizer);

        if(length <= 32) {
            smallSort.customBinaryInsert(array, a, b, 0.5);
            return;
        }

        int sqrtLg = (32-Integer.numberOfLeadingZeros(length-1))/2,
            bLen   = 1 << sqrtLg,
            tLen   = length/bLen,
            bufLen = 2*bLen;

        int[] buf  = Writes.createExternalArray(bufLen);
        int[] tags = Writes.createExternalArray(tLen);
        int[] tTmp = Writes.createExternalArray(tLen);

        int b1 = b-length%bLen,
            j = 1;

        if(sqrtLg%2 == 0) {
            for(int i = a+1; i < b1; i += 2)
                if(Reads.compareIndices(array, i-1, i, 0.5, true) > 0)
                    Writes.swap(array, i-1, i, 0.5, false, false);
            j *= 2;
        }

        for(; j < bufLen; j *= 4)
            for(int i = a; i+j < b1; i += 4*j)
                this.pingPongMerge(array, buf, i, i+j, Math.min(i+2*j, b1), Math.min(i+3*j, b1), Math.min(i+4*j, b1));

        for(int i = 0; i < tLen; i++)
            Writes.write(tags, i, i&1, 0, false, true);

        for(; j < length; j *= 2)
            for(int i = a; i+j < b1; i += 2*j)
                this.blockMerge(array, buf, tags, tTmp, i, i+j, Math.min(i+2*j, b1), bLen);

        this.blockCycle(array, buf, tags, 0, bLen, tLen);

        if(b1 < b) {
            smallSort.customBinaryInsert(array, b1, b, 0.5);
            this.mergeBWExt(array, buf, a, b1, b);
        }

        Writes.deleteExternalArray(buf);
        Writes.deleteExternalArray(tags);
        Writes.deleteExternalArray(tTmp);
    }
}