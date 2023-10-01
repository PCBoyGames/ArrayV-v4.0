package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 *
 */
public class MiniAdaptiveGrailSort extends Sort {

    public MiniAdaptiveGrailSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Mini Adaptive Grail");
        this.setRunAllSortsName("Mini Adaptive Grail Sort");
        this.setRunSortName("Mini Adaptive Grailsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    int tLenCalc(int n, int bLen) {
        int n1 = n-bLen;
        int a = 0, b = bLen;

        while (a < b) {
            int m = (a+b)/2;

            if (n1-m < (m+1)*bLen) b = m;
            else                  a = m+1;
        }
        return a;
    }

    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b) Writes.swap(array, a, b, pause, mark, aux);
    }

    void multiSwap(int[] array, int a, int b, int s) {
        if (a != b) while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
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
        while (l > 0 && r > 0) {
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
            i++;
            nKeys++;
            while (nKeys < n && Reads.compareIndices(array, i - 1, i, 1, true) < 0) {
                i++;
                nKeys++;
            }
        } else if (Reads.compareIndices(array, i - 1, i, 1, true) > 0) {
            i++;
            nKeys++;
            while (nKeys < n && Reads.compareIndices(array, i - 1, i, 1, true) > 0) {
                i++;
                nKeys++;
            }
            reverse(array, a, i - 1);
        }
        return nKeys;
    }

    protected int buildUniqueRunBW(int[] array, int b, int n) {
        int nKeys = 1, i = b - 1;
        // build run at end
        if (Reads.compareIndices(array, i - 1, i, 1, true) < 0) {
            i--;
            nKeys++;
            while (nKeys < n && Reads.compareIndices(array, i - 1, i, 1, true) < 0) {
                i--;
                nKeys++;
            }
        } else if (Reads.compareIndices(array, i - 1, i, 1, true) > 0) {
            i--;
            nKeys++;
            while (nKeys < n && Reads.compareIndices(array, i - 1, i, 1, true) > 0) {
                i--;
                nKeys++;
            }
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

    protected void mergeBW(int[] array, int a, int m, int b, int p, boolean left) {
        int pLen = b - m;
        multiSwap(array, m, p, pLen);
        int i = pLen - 1, j = m - 1, k = b - 1;
        while (i >= 0 && j >= a) {
            int c = Reads.compareValues(array[p + i], array[j]);
            if (c > 0 || (left && c == 0))
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

    protected void inPlaceMergeBW(int[] array, int a, int m, int b, boolean left) {
        while (b > m && m > a) {
            int i = rightExpSearch(array, a, m, array[b - 1], !left);
            rotate(array, i, m, b);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a) break;
            b = rightExpSearch(array, m, b, array[m - 1], left);
        }
    }

    void mergeBlocks(int[] array, int a, int m, int b, int p, boolean left, boolean hasBuf) {
        if (hasBuf) mergeBW(array, a, m, b, p, left);
        else        inPlaceMergeBW(array, a, m, b, left);
    }

    public void inPlaceMerge(int[] array, int a, int m, int b) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        if (b - m < m - a) inPlaceMergeBW(array, a, m, b, true);
        else inPlaceMergeFW(array, a, m, b);
    }

    void merge(int[] array, int a, int m, int b, int p) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        if (b - m < m - a) mergeBW(array, a, m, b, p, true);
        else mergeFW(array, a, m, b, p);
    }

    private void mergeTo(int[] array, int a, int m, int b, int p) {
        int i = a, j = m;

        while (i < m && j < b) {
            if (Reads.compareValues(array[i], array[j]) <= 0)
                Writes.swap(array, p++, i++, 1, true, false);
            else
                Writes.swap(array, p++, j++, 1, true, false);
        }
        while (i < m) Writes.swap(array, p++, i++, 1, true, false);
        while (j < b) Writes.swap(array, p++, j++, 1, true, false);
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

    void blockSelect(int[] array, int a, int ta, int tm, int tb, int bLen) {
        int i1 = ta, j1 = tm, k = ta;

        while (k < j1 && j1 < tb) {
            if (Reads.compareValues(array[a+(i1-ta+1)*bLen-1], array[a+(j1-ta+1)*bLen-1]) <= 0) {
                if (i1 > k) this.multiSwap(array, a+(k-ta)*bLen, a+(i1-ta)*bLen, bLen);
                swap(array, k++, i1, 1, true, false);

                i1 = k;
                for (int i = Math.max(k+1, tm); i < j1; i++)
                    if (Reads.compareValues(array[i], array[i1]) < 0) i1 = i;
            } else {
                this.multiSwap(array, a+(k-ta)*bLen, a+(j1-ta)*bLen, bLen);
                Writes.swap(array, k, j1++, 1, true, false);

                if (i1 == k++) i1 = j1-1;
            }
        }
        while (k < j1-1) {
            if (i1 > k) this.multiSwap(array, a+(k-ta)*bLen, a+(i1-ta)*bLen, bLen);
            swap(array, k++, i1, 1, true, false);

            i1 = k;
            for (int i = k+1; i < j1; i++)
                if (Reads.compareValues(array[i], array[i1]) < 0) i1 = i;
        }
    }

    void blockMerge(int[] array, int a, int m, int b, int t, int p, int bLen, boolean hasBuf) {
        if (Reads.compareValues(array[m - 1], array[m]) <= 0) return;
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (b - m <= bLen) {
            if (hasBuf) mergeBW(array, a, m, b, p, true);
            else inPlaceMergeBW(array, a, m, b, true);
            return;
        }
        int a1 = leftExpSearch(array, a, m, array[m], false);
        if (m - a1 <= bLen) {
            if (hasBuf) mergeFW(array, a1, m, b, p);
            else inPlaceMergeFW(array, a1, m, b);
            return;
        }
        a = a1 - (a1 - a) % bLen;
        int tLen1 = (m-a)/bLen, bCnt = (b-a)/bLen;
        int tm = t+tLen1, tb = t+bCnt, b1 = b-(b-m)%bLen;

        int mKey = array[tm];
        this.blockSelect(array, a, t, tm, tb, bLen);

        int f = a;
        boolean left = Reads.compareIndexValue(array, t, mKey, 1, true) < 0;

        for (int i = 1; i < bCnt; i++) {
            if (left ^ (Reads.compareIndexValue(array, t+i, mKey, 1, true) < 0)) {
                int nxt  = a + i*bLen;
                int nxtE = this.binSearch(array, nxt, nxt+bLen, array[nxt-1], left);

                this.mergeBlocks(array, f, nxt, nxtE, p, left, hasBuf);
                f = nxtE;
                left = !left;
            }
        }
        if (left) this.mergeBlocks(array, f, b1, b, p, left, hasBuf);
        // TODO keys sort
        insertSort(array, t, tb);
    }

    public void lazyStableSort(int[] array, int a, int b) {
        int j = 16;
        if (buildRuns(array, a, b, j)) return;
        for (int i; j < b - a; j *= 2) {
            for (i = a; i + j < b; i += 2 * j)
                inPlaceMerge(array, i, i + j, Math.min(i + 2 * j, b));
        }
    }

    public void blockMergeSort(int[] array, int a, int b) {
        int length = b - a;
        if (length <= 32) {
            insertSort(array, a, b);
            return;
        }
        int bLen = 1 << ((30-Integer.numberOfLeadingZeros(length))/2+1),
            tLen = this.tLenCalc(length, bLen);

        int ideal = bLen + tLen;
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
        if (keys <= 4) { // strategy 3: lazy stable
            lazyStableSort(array, a, b);
            return;
        }
        if (keys < ideal) {
            keys = 1 << (31-Integer.numberOfLeadingZeros(keys));
            bLen = keys / 2;
            tLen = keys / 2;
        }
        int i, j = 16, t, p, a1, b1;
        length -= keys;
        if (bwBuf) {
            p = b-bLen; a1 = a; b1 = p-tLen; t = b1;
        } else {
            p = a+tLen; a1 = p+bLen; b1 = b; t = a;
        }
        if (!buildRuns(array, a1, b1, j)) {
            for (; 4*j <= bLen; j *= 4) {
                for (i = a1; i+2*j < b1; i += 4*j)
                    this.pingPongMerge(array, i, i+j, i+2*j, Math.min(i+3*j, b1), Math.min(i+4*j, b1), p);
                if (i+j < b1)
                    this.merge(array, i, i+j, b1, p);
            }
            for (; j <= bLen; j *= 2)
                for (i = a1; i+j < b1; i += 2*j)
                    this.merge(array, i, i+j, Math.min(i+2*j, b1), p);
            // block merge
            int limit = bLen*(tLen+1);

            for (; j < length && Math.min(2*j, length) < limit; j *= 2) {
                for (i = a1; i+j < b1; i += 2*j)
                    this.blockMerge(array, i, i+j, Math.min(i+2*j, b1), t, p, bLen, true);
            }
            insertSort(array, p, p + bLen);
            // strategy 2
            bLen = 2*j/keys;

            for (; j < length; j *= 2, bLen *= 2) {
                for (i = a1; i+j < b1; i += 2*j)
                    this.blockMerge(array, i, i+j, Math.min(i+2*j, b1), t, p, bLen, false);
            }
        }
        if (bwBuf) {
            inPlaceMergeBW(array, a, b1, b, true);
        } else {
            inPlaceMergeFW(array, a, a1, b);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        blockMergeSort(array, 0, sortLength);

    }

}
