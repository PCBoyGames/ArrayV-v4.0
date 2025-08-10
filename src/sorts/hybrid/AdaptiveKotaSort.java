package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Haruki
in collaboration with aphitorite

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * An adaptive, in-place and stable block merge sort.
 * <p>
 * To use this algorithm in another, use {@code blockMergeSort()} from a reference
 * instance.
 *
 * @author Haruki (Ayako-chan)
 * @author aphitorite
 *
 */
public class AdaptiveKotaSort extends Sort {

    public AdaptiveKotaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Adaptive Kota");
        setRunAllSortsName("Adaptive Kota Sort");
        setRunSortName("Adaptive Kotasort");
        setCategory("Hybrid Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    int tLenCalc(int n, int bLen) {
        int n1 = n-2*bLen;
        int a = 0, b = 2*bLen;

        while(a < b) {
            int m = (a+b)/2;

            if(n1-m < (m+3)*bLen) b = m;
            else                  a = m+1;
        }
        return a;
    }

    void multiSwap(int[] array, int a, int b, int s) {
        while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }

    void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b) Writes.write(array, b, temp, 0.5, true, false);
    }

    void rotate(int[] array, int a, int m, int b) {
        int l = m - a, r = b - m;
        while (l > 1 && r > 1) {
            if (r < l) {
                this.multiSwap(array, m - r, m, r);
                b -= r;
                m -= r;
                l -= r;
            } else {
                this.multiSwap(array, a, m, l);
                a += l;
                m += l;
                r -= l;
            }
        }
        if (r == 1) this.insertTo(array, m, a);
        else if (l == 1) this.insertTo(array, a, b - 1);
    }

    protected int binSearch(int[] array, int a, int b, int val, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0)) b = m;
            else a = m + 1;
        }
        return a;
    }

    protected int leftExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0) i *= 2;
        else while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0) i *= 2;
        return binSearch(array, a + i / 2, Math.min(b, a - 1 + i), val, left);
    }

    protected int rightExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0) i *= 2;
        else while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        return binSearch(array, Math.max(a, b - i + 1), b - i / 2, val, left);
    }

    // Easy patch to avoid the "reversals can be done in a single swap" notes.
    protected void reverse(int[] array, int a, int b) {
        if (b - a >= 3) Writes.reversal(array, a, b, 1, true, false);
        else Writes.swap(array, a, b, 1, true, false);
    }

    protected int buildUniqueRunFW(int[] array, int a, int n) {
        int nKeys = 1, i = a + 1;
        // build run at start
        if (Reads.compareIndices(array, i - 1, i, 1, true) < 0) {
            do {
                i++;
                nKeys++;
            } while (nKeys < n && Reads.compareIndices(array, i - 1, i, 1, true) < 0);
        } else if (Reads.compareIndices(array, i - 1, i, 1, true) > 0) {
            do {
                i++;
                nKeys++;
            } while (nKeys < n && Reads.compareIndices(array, i - 1, i, 1, true) > 0);
            reverse(array, a, i - 1);
        }
        return nKeys;
    }

    protected int buildUniqueRunBW(int[] array, int b, int n) {
        int nKeys = 1, i = b - 1;
        // build run at end
        if (Reads.compareIndices(array, i - 1, i, 1, true) < 0) {
            do {
                i--;
                nKeys++;
            } while (nKeys < n && Reads.compareIndices(array, i - 1, i, 1, true) < 0);
        } else if (Reads.compareIndices(array, i - 1, i, 1, true) > 0) {
            do {
                i--;
                nKeys++;
            } while (nKeys < n && Reads.compareIndices(array, i - 1, i, 1, true) > 0);
            reverse(array, i, b - 1);
        }
        return nKeys;
    }

    protected int findKeysFW(int[] array, int a, int b, int nKeys, int n) {
        int p = a, pEnd = a + nKeys;
        Highlights.clearMark(2);
        for (int i = pEnd; i < b && nKeys < n; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(1);
            int loc = binSearch(array, p, pEnd, array[i], true);
            if (pEnd == loc || Reads.compareValues(array[i], array[loc]) != 0) {
                rotate(array, p, pEnd, i);
                int inc = i - pEnd;
                loc += inc;
                p += inc;
                pEnd += inc;
                insertTo(array, pEnd, loc);
                nKeys++;
                pEnd++;
            }
        }
        rotate(array, a, p, pEnd);
        return nKeys;
    }

    protected int findKeysBW(int[] array, int a, int b, int nKeys, int n) {
        int p = b - nKeys, pEnd = b;
        Highlights.clearMark(2);
        for (int i = p - 1; i >= a && nKeys < n; i--) {
            Highlights.markArray(1, i);
            Delays.sleep(1);
            int loc = binSearch(array, p, pEnd, array[i], true);
            if (pEnd == loc || Reads.compareValues(array[i], array[loc]) != 0) {
                rotate(array, i + 1, p, pEnd);
                int inc = p - (i + 1);
                loc -= inc;
                pEnd -= inc;
                p -= inc + 1;
                nKeys++;
                insertTo(array, i, loc - 1);
            }
        }
        rotate(array, p, pEnd, b);
        return nKeys;
    }

    protected boolean buildRuns(int[] array, int a, int b, int mRun) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 1, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) > 0) i++;
                reverse(array, j, i - 1);
            } else while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;
            if (i < b) {
                noSort = false;
                j = i - (i - j - 1) % mRun - 1;
            }
            while (i - j < mRun && i < b) {
                insertTo(array, i, rightExpSearch(array, j, i, array[i], false));
                i++;
            }
            j = i++;
        }
        return noSort;
    }

    protected void insertSort(int[] array, int a, int b) {
        buildRuns(array, a, b, b - a);
    }

    protected void mergeFW(int[] array, int a, int m, int b, int p) {
        int pLen = m - a;
        multiSwap(array, a, p, pLen);
        int i = 0, j = m, k = a;
        while (i < pLen && j < b) {
            if (Reads.compareValues(array[p + i], array[j]) <= 0)
                Writes.swap(array, k++, p + (i++), 1, true, false);
            else
                Writes.swap(array, k++, j++, 1, true, false);
        }
        while (i < pLen) Writes.swap(array, k++, p + (i++), 1, true, false);
    }

    protected void mergeBW(int[] array, int a, int m, int b, int p) {
        int pLen = b - m;
        multiSwap(array, m, p, pLen);
        int i = pLen - 1, j = m - 1, k = b - 1;
        while (i >= 0 && j >= a) {
            if (Reads.compareValues(array[p + i], array[j]) >= 0)
                Writes.swap(array, k--, p + (i--), 1, true, false);
            else
                Writes.swap(array, k--, j--, 1, true, false);
        }
        while (i >= 0) Writes.swap(array, k--, p + (i--), 1, true, false);
    }

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = leftExpSearch(array, m, b, array[a], true);
            rotate(array, a, m, i);
            int t = i - m;
            m = i;
            a += t + 1;
            if (m >= b) break;
            a = leftExpSearch(array, a, m, array[m], false);
        }
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        while (b > m && m > a) {
            int i = rightExpSearch(array, a, m, array[b - 1], false);
            rotate(array, i, m, b);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a) break;
            b = rightExpSearch(array, m, b, array[m - 1], true);
        }
    }

    public void inPlaceMerge(int[] array, int a, int m, int b) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        if (b - m < m - a) inPlaceMergeBW(array, a, m, b);
        else inPlaceMergeFW(array, a, m, b);
    }

    public void merge(int[] array, int a, int m, int b, int p) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        if (b - m < m - a) mergeBW(array, a, m, b, p);
        else mergeFW(array, a, m, b, p);
    }

    protected void fragmentedMergeFW(int[] array, int a, int m, int b, int s) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        a = leftExpSearch(array, a, m, array[m], false);
        int rPos = leftExpSearch(array, m, b, array[a], true);
        rotate(array, a, m, rPos);
        int dist = rPos-m;
        a += dist;
        m += dist;
        while (m - a > s && m < b) {
            int a1 = a + s;
            rPos = binSearch(array, m, b, array[a1], true);
            rotate(array, a1, m, rPos);
            dist = rPos - m;
            a1 += dist;
            m += dist;
            inPlaceMerge(array, a, a1 - dist, a1);
            a = a1;
        }
        if (m < b) inPlaceMerge(array, a, m, b);
    }

    protected void fragmentedMergeBW(int[] array, int a, int m, int b, int s) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        b = rightExpSearch(array, m, b, array[m - 1], true);
        int rPos = rightExpSearch(array, a, m, array[b - 1], false);
        this.rotate(array, rPos, m, b);
        int dist = m-rPos;
        b -= dist;
        m -= dist;
        while (b - m > s && m > a) {
            int b1 = b - s;
            rPos = binSearch(array, a, m, array[b1 - 1], false);
            rotate(array, rPos, m, b1);
            dist = m - rPos;
            b1 -= dist;
            m -= dist;
            inPlaceMerge(array, b1, b1 + dist, b);
            b = b1;
        }
        if (a < m) inPlaceMerge(array, a, m, b);
    }

    int selectMin(int[] array, int a, int b, int bLen) {
        int min = a;

        for (int i = min+bLen; i < b; i += bLen)
            if (Reads.compareValues(array[i], array[min]) < 0) min = i;

        return min;
    }

    void blockSelect(int[] array, int a, int b, int t, int bLen) {
        while (a < b) {
            int min = this.selectMin(array, a, b, bLen);

            if (min != a) this.multiSwap(array, a, min, bLen);
            Writes.swap(array, a, t++, 10, true, false);

            a += bLen;
        }
    }

    void blockMerge(int[] array, int a, int m, int b, int t, int p, int bLen) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if(b - m <= 2 * bLen) {
            mergeBW(array, a, m, b, p);
            return;
        }
        int a1 = leftExpSearch(array, a, m, array[m], false);
        if(m - a1 <= 2 *bLen) {
            mergeFW(array, a1, m, b, p);
            return;
        }
        a = a1 - (a1 - a) % bLen;
        int c = 0, tp = t;

        int i = a, j = m, k = p;
        int l = 0, r = 0;

        while (c++ < 2*bLen) { //merge 2 blocks into buffer to create 2 buffers
            if (Reads.compareValues(array[i], array[j]) <= 0) {
                Writes.swap(array, k++, i++, 1, true, false);
                l++;
            } else {
                Writes.swap(array, k++, j++, 1, true, false);
                r++;
            }
        }

        boolean left = l >= r;
        k = left ? i-l : j-r;

        c = 0;

        do {
            if (i < m && (j == b || Reads.compareValues(array[i], array[j]) <= 0)) {
                Writes.swap(array, k++, i++, 1, true, false);
                l++;
            } else {
                Writes.swap(array, k++, j++, 1, true, false);
                r++;
            }
            if (++c == bLen) { //change buffer after every block
                Writes.swap(array, k-bLen, tp++, 10, true, false);

                if(left) l -= bLen;
                else     r -= bLen;

                left = l >= r;
                k = left ? i-l : j-r;

                c = 0;
            }
        } while(i < m || j < b);

        int b1 = b-c;

        this.multiSwap(array, k-c, b1, c); //swap remainder to end (r buffer)
        r -= c;

        //l and r buffers are divisible by bLen
        this.multiSwap(array, m-l, a, l);    //swap l buffer to front
        this.multiSwap(array, b1-r, a+l, r); //swap r buffer to front
        this.multiSwap(array, a, p, 2*bLen); //swap first merged elements to correct position in front

        this.blockSelect(array, a+2*bLen, b1, t, bLen);
    }

    void blockMergeNoBuf(int[] array, int a, int m, int b, int t, int bLen) { //from wiki sort
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if(b - m <= 2 * bLen) {
            inPlaceMergeBW(array, a, m, b);
            return;
        }
        int a1 = leftExpSearch(array, a, m, array[m], false);
        if(m - a1 <= 2 * bLen) {
            inPlaceMergeFW(array, a1, m, b);
            return;
        }
        a = a1 - (a1 - a) % bLen;
        for(int i = a+bLen, j = t; i < m; i += bLen, j++) //tag blocks
            Writes.swap(array, i, j, 10, true, false);

        int i = a+bLen, b1 = b-(b-m)%bLen;

        while (i < m && m < b1) {
            if (Reads.compareValues(array[i-1], array[m+bLen-1]) > 0) {
                this.multiSwap(array, i, m, bLen);
                this.inPlaceMergeBW(array, a, i, i+bLen);

                m += bLen;
            } else {
                int min = this.selectMin(array, i, m, bLen);

                if (min > i) this.multiSwap(array, i, min, bLen);
                Writes.swap(array, t++, i, 1, true, false);
            }
            i += bLen;
        }
        if (i < m) {
            do {
                int min = this.selectMin(array, i, m, bLen);

                if (min > i) this.multiSwap(array, i, min, bLen);
                Writes.swap(array, t++, i, 1, true, false);
                i += bLen;
            } while(i < m);
        } else {
            while (m < b1 && Reads.compareValues(array[m-1], array[m]) > 0) {
                this.inPlaceMergeBW(array, a, m, m+bLen);
                m += bLen;
            }
        }
        this.inPlaceMergeBW(array, a, b1, b);
    }

    void mergeTo(int[] array, int a, int m, int b, int p) {
        int i = a, j = m;

        while(i < m && j < b) {
            if(Reads.compareValues(array[i], array[j]) <= 0)
                Writes.swap(array, p++, i++, 1, true, false);
            else
                Writes.swap(array, p++, j++, 1, true, false);
        }
        while(i < m) Writes.swap(array, p++, i++, 1, true, false);
        while(j < b) Writes.swap(array, p++, j++, 1, true, false);
    }

    void pingPongMerge(int[] array, int a, int m1, int m, int m2, int b, int p) {
        int p1 = p+m-a, pEnd = p+b-a;
        if (Reads.compareIndices(array, m1-1, m1, 1, true) > 0
                || (m2 < b && Reads.compareIndices(array, m2-1, m2, 1, true) > 0)) {
            this.mergeTo(array, a, m1, m, p);
            this.mergeTo(array, m, m2, b, p1);
            this.mergeTo(array, p, p1, pEnd, a);
        } else merge(array, a, m, b, p);
    }

    public void lazyStableSort(int[] array, int a, int b) {
        int j = 16;
        if (buildRuns(array, a, b, j)) return;
        for (int i; j < b - a; j *= 2) {
            for (i = a; i + j < b; i += 2 * j)
                inPlaceMerge(array, i, i + j, Math.min(i + 2 * j, b));
        }
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using a block merge sort.
     *
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     */
    public void blockMergeSort(int[] array, int a, int b) {
        int length = b - a;
        if (length <= 32) {
            insertSort(array, a, b);
            return;
        }
        int bLen   = 1 << ((32-Integer.numberOfLeadingZeros(length-1))/2), //pow of 2 >= sqrt n
            tLen   = this.tLenCalc(length, bLen),
            bufLen = 2*bLen;
        int ideal = bufLen + tLen;
        //choose direction to find keys
        boolean bwBuf;
        int rRun = this.buildUniqueRunBW(array, b, ideal), lRun = 0;

        if (rRun == ideal) bwBuf = true;
        else {
            lRun = this.buildUniqueRunFW(array, a, ideal);

            if (lRun == ideal) bwBuf = false;
            else bwBuf = (rRun < 16 && lRun < 16) || rRun >= lRun;
        }
        //find bLen + tLen unique buffer keys
        int keys = bwBuf ? this.findKeysBW(array, a, b, rRun, ideal)
                         : this.findKeysFW(array, a, b, lRun, ideal);
        if (keys == 1) return;
        if (keys <= 8) { // strategy 3: lazy stable
            lazyStableSort(array, a, b);
            return;
        }
        if(keys < ideal) {
            while(bufLen > 2*(keys-bufLen)) bufLen /= 2;

            bLen = bufLen/2;
            tLen = keys-bufLen;
        }
        int i, j = 16, t, p, a1, b1;
        length -= keys;
        if(bwBuf) {
            p = b-bufLen; a1 = a; b1 = p-tLen; t = b1;
        } else {
            p = a+tLen; a1 = p+bufLen; b1 = b; t = a;
        }
        if (!buildRuns(array, a1, b1, j)) {
            for (; 4*j <= bufLen; j *= 4) {
                for (i = a1; i+2*j < b1; i += 4*j)
                    this.pingPongMerge(array, i, i+j, i+2*j, Math.min(i+3*j, b1), Math.min(i+4*j, b1), p);
                if (i+j < b1)
                    this.merge(array, i, i+j, b1, p);
            }
            for (; j <= bufLen; j *= 2)
                for (i = a1; i+j < b1; i += 2*j)
                    this.merge(array, i, i+j, Math.min(i+2*j, b1), p);
            //block merge

            int limit = bLen*(tLen+3);
            for (; j < length && Math.min(2*j, length) < limit; j *= 2) {
                for (i = a1; i+j < b1; i += 2*j)
                    this.blockMerge(array, i, i+j, Math.min(i+2*j, b1), t, p, bLen);
            }
            insertSort(array, p, p + bufLen);
            // strategy 2
            if (bufLen <= tLen) bufLen *= 2;
            bLen = 2*j/bufLen;

            for (; j < length; j *= 2, bLen *= 2) {
                for (i = a1; i+j < b1; i += 2*j)
                    this.blockMergeNoBuf(array, i, i+j, Math.min(i+2*j, b1), t, bLen);
            }
        }
        if (bwBuf) {
            fragmentedMergeBW(array, a, b1, b, bLen);
        } else {
            fragmentedMergeFW(array, a, a1, b, bLen);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        blockMergeSort(array, 0, sortLength);

    }

}
