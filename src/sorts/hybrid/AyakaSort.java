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
public final class AyakaSort extends Sort {

    public AyakaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Ayaka");
        this.setRunAllSortsName("Ayaka Sort");
        this.setRunSortName("Ayakasort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int binSearch(int[] array, int a, int b, int val, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0))
                b = m;
            else
                a = m+1;
        }
        return a;
    }

    protected int expSearch(int[] array, int a, int b, int val, boolean dir, boolean left) {
        int i = 1;
        int a1, b1;
        if (dir) {
            if (left)
                while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0)
                    i *= 2;
            else
                while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
                    i *= 2;
            a1 = a + i / 2;
            b1 = Math.min(b, a - 1 + i);
        } else {
            if (left)
                while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0)
                    i *= 2;
            else
                while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
                    i *= 2;
            a1 = Math.max(a, b - i + 1);
            b1 = b - i / 2;
        }
        return binSearch(array, a1, b1, val, left);
    }

    protected void multiSwap(int[] array, int a, int b, int len) {
        for (int i = 0; i < len; i++)
            Writes.swap(array, a + i, b + i, 1, true, false);
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b)
            Writes.write(array, b, temp, 0.5, true, false);
    }

    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        if (a >= m || m >= b)
            return;
        int p0 = a, p1 = m - 1, p2 = m, p3 = b - 1;
        int tmp;
        while (p0 < p1 && p2 < p3) {
            tmp = array[p1];
            Writes.write(array, p1--, array[p0], 0.5, true, false);
            Writes.write(array, p0++, array[p2], 0.5, true, false);
            Writes.write(array, p2++, array[p3], 0.5, true, false);
            Writes.write(array, p3--, tmp, 0.5, true, false);
        }
        while (p0 < p1) {
            tmp = array[p1];
            Writes.write(array, p1--, array[p0], 0.5, true, false);
            Writes.write(array, p0++, array[p3], 0.5, true, false);
            Writes.write(array, p3--, tmp, 0.5, true, false);
        }
        while (p2 < p3) {
            tmp = array[p2];
            Writes.write(array, p2++, array[p3], 0.5, true, false);
            Writes.write(array, p3--, array[p0], 0.5, true, false);
            Writes.write(array, p0++, tmp, 0.5, true, false);
        }
        if (p0 < p3) { // don't count reversals that don't do anything
            // I had to do this due to Madhouse.
            if (p3 - p0 >= 3)
                Writes.reversal(array, p0, p3, 1, true, false);
            else
                Writes.swap(array, p0, p3, 1, true, false);
            Highlights.clearMark(2);
        }
    }

    protected int buildUniqueRun(int[] array, int a, int n) {
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
            // I had to do this due to Madhouse.
            if (i - a >= 4)
                Writes.reversal(array, a, i - 1, 1, true, false);
            else
                Writes.swap(array, a, i - 1, 1, true, false);
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
            // I had to do this due to Madhouse.
            if (b - i >= 4)
                Writes.reversal(array, i, b - 1, 1, true, false);
            else
                Writes.swap(array, i, b - 1, 1, true, false);
        }
        return nKeys;
    }

    protected int findKeys(int[] array, int a, int b, int nKeys, int n) {
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
                if (i - j < 4)
                    Writes.swap(array, j, i - 1, 1.0, true, false);
                else
                    Writes.reversal(array, j, i - 1, 1.0, true, false);
            } else
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;
            if (i < b) {
                noSort = false;
                j = i - (i - j - 1) % mRun - 1;
            }
            while (i - j < mRun && i < b) {
                insertTo(array, i, expSearch(array, j, i, array[i], false, false));
                i++;
            }
            j = i++;
        }
        return noSort;
    }

    protected void insertSort(int[] array, int a, int b) {
        int i = a + 1;
        if (i >= b)
            return;
        if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) i++;
            if (i - a < 4)
                Writes.swap(array, a, i - 1, 1.0, true, false);
            else
                Writes.reversal(array, a, i - 1, 1.0, true, false);
        } else
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        Highlights.clearMark(2);
        for (; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i], false, false));
    }

    protected boolean checkReverseBounds(int[] array, int a, int m, int b) {
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return true;
        }
        return false;
    }

    protected boolean boundCheck(int[] array, int a, int m, int b) {
        if (a >= m || m >= b)
            return true;
        return Reads.compareValues(array[m - 1], array[m]) <= 0
                  || checkReverseBounds(array, a, m, b);
    }

    protected void mergeFW(int[] array, int a, int m, int b, int p) {
        int pLen = m - a;
        multiSwap(array, a, p, pLen);
        int i = 0, j = m, k = a;
        while (i < pLen && j < b)
            if (Reads.compareValues(array[p + i], array[j]) <= 0)
                Writes.swap(array, k++, p + (i++), 1, true, false);
            else
                Writes.swap(array, k++, j++, 1, true, false);
        while (i < pLen)
            Writes.swap(array, k++, p + (i++), 1, true, false);
    }

    protected void mergeBW(int[] array, int a, int m, int b, int p) {
        int pLen = b - m;
        multiSwap(array, m, p, pLen);
        int i = pLen - 1, j = m - 1, k = b - 1;
        while (i >= 0 && j >= a)
            if (Reads.compareValues(array[p + i], array[j]) >= 0)
                Writes.swap(array, k--, p + (i--), 1, true, false);
            else
                Writes.swap(array, k--, j--, 1, true, false);
        while (i >= 0)
            Writes.swap(array, k--, p + (i--), 1, true, false);
    }

    protected void mergeTo(int[] array, int a, int m, int b, int p) {
        int i = a, j = m;
        while (i < m && j < b)
            if (Reads.compareValues(array[i], array[j]) <= 0)
                Writes.swap(array, p++, i++, 1, true, false);
            else
                Writes.swap(array, p++, j++, 1, true, false);
        while (i < m)
            Writes.swap(array, p++, i++, 1, true, false);
        while (j < b)
            Writes.swap(array, p++, j++, 1, true, false);
    }

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = expSearch(array, m, b, array[a], true, true);
            rotate(array, a, m, i);
            int t = i - m;
            m = i;
            a += t + 1;
            if (a >= m)
                break;
            a = expSearch(array, a, m, array[m], true, false);
        }
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        while (b > m && m > a) {
            int i = expSearch(array, a, m, array[b - 1], false, false);
            rotate(array, i, m, b);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a)
                break;
            b = expSearch(array, m, b, array[m - 1], false, true);
        }
    }

    public void inPlaceMerge(int[] array, int a, int m, int b, boolean bnd) {
        if (bnd) {
            if (boundCheck(array, a, m, b))
                return;
            a = expSearch(array, a, m, array[m], true, false);
            b = expSearch(array, m, b, array[m - 1], false, true);
            if (checkReverseBounds(array, a, m, b))
                return;
        }
        if (b - m < m - a)
            inPlaceMergeBW(array, a, m, b);
        else
            inPlaceMergeFW(array, a, m, b);
    }

    protected void redistBuffer(int[] array, int a, int m, int b) {
        int rPos = binSearch(array, m, b, array[a], true);
        rotate(array, a, m, rPos);
        int dist = rPos-m;
        a += dist;
        m += dist;
        int a1 = a+(m-a)/2;
        rPos = binSearch(array, m, b, array[a1], true);
        rotate(array, a1, m, rPos);
        dist = rPos-m;
        a1  += dist;
        m   += dist;
        inPlaceMerge(array, a, a1-dist, a1, false);
        inPlaceMerge(array, a1, m, b, false);
    }

    protected void redistBufferBW(int[] array, int a, int m, int b) {
        int rPos = binSearch(array, a, m, array[b-1], false);
        rotate(array, rPos, m, b);
        int dist = m-rPos;
        b -= dist;
        m -= dist;
        int b1 = m+(b-m)/2;
        rPos = binSearch(array, a, m, array[b1-1], false);
        rotate(array, rPos, m, b1);
        dist = m-rPos;
        b1  -= dist;
        m   -= dist;
        inPlaceMerge(array, b1, b1+dist, b, false);
        inPlaceMerge(array, a, m, b1, false);
    }

    public void merge(int[] array, int a, int m, int b, int p, boolean bnd) {
        if (bnd) {
            if (boundCheck(array, a, m, b))
                return;
            a = expSearch(array, a, m, array[m], true, false);
            b = expSearch(array, m, b, array[m - 1], false, true);
            if (checkReverseBounds(array, a, m, b))
                return;
        }
        if (Math.min(m - a, b - m) <= 8) {
            if (m - a > b - m)
                inPlaceMergeBW(array, a, m, b);
            else
                inPlaceMergeFW(array, a, m, b);
        } else if (b - m < m - a)
            mergeBW(array, a, m, b, p);
        else
            mergeFW(array, a, m, b, p);
    }

    public void pingPongMerge(int[] array, int a, int m1, int m2, int m3, int b, int p) {
        if (Reads.compareValues(array[m1-1], array[m1]) > 0
        || (m3 < b && Reads.compareValues(array[m3-1], array[m3]) > 0)) {
            int p1 = p + m2-a, pEnd = p + b-a;
            mergeTo(array, a, m1, m2, p);
            mergeTo(array, m2, m3, b, p1);
            mergeTo(array, p, p1, pEnd, a);
        }
        else merge(array, a, m2, b, p, true);
    }

    public void rotateMerge(int[] array, int a, int m, int b, int p, int pLen) {
        while (Math.min(m - a, b - m) > pLen) {
            if (boundCheck(array, a, m, b))
                return;
            a = expSearch(array, a, m, array[m], true, false);
            b = expSearch(array, m, b, array[m - 1], false, true);
            if (checkReverseBounds(array, a, m, b))
                return;
            if (Math.min(m - a, b - m) <= pLen) {
                merge(array, a, m, b, p, false);
                return;
            }
            int m1, m2, m3;
            if (m-a >= b-m) {
                m1 = a+(m-a)/2;
                m2 = binSearch(array, m, b, array[m1], true);
                m3 = m1+(m2-m);
            }
            else {
                m2 = m+(b-m)/2;
                m1 = binSearch(array, a, m, array[m2], false);
                m3 = (m2++)-(m-m1);
            }
            rotate(array, m1, m, m2);
            if (b - (m3 + 1) < m3 - a) {
                rotateMerge(array, m3 + 1, m2, b, p, pLen);
                m = m1;
                b = m3;
            } else {
                rotateMerge(array, a, m1, m3, p, pLen);
                m = m2;
                a = m3 + 1;
            }
        }
        merge(array, a, m, b, p, true);
    }

    public void lazyStableSort(int[] array, int a, int b) {
        int j = b - a;
        for (; j >= 32; j = (j + 1) / 2);
        if (buildRuns(array, a, b, j))
            return;
        for (int i; j < b - a; j *= 2) {
            for (i = a; i + 2 * j <= b; i += 2 * j)
                inPlaceMerge(array, i, i + j, i + 2 * j, true);
            if (i + j < b)
                inPlaceMerge(array, i, i + j, b, true);
        }
    }

    public void mergeSort(int[] array, int a, int b) {
        int len = b - a;
        if (len < 64) {
            lazyStableSort(array, a, b);
            return;
        }
        int j = len;
        for (; j >= 32; j = (j + 1) / 2);
        int bLen = j;
        for (bLen = j; bLen * bLen < len; bLen *= 2);
        int ideal = bLen;
        //choose direction to find keys
        boolean bwBuf;
        int rRun = buildUniqueRunBW(array, b, ideal), lRun = 0;
        if (rRun == ideal) bwBuf = true;
        else {
            lRun = buildUniqueRun(array, a, ideal);
            if (lRun == ideal) bwBuf = false;
            else bwBuf = (rRun < 16 && lRun < 16) || rRun >= lRun;
        }
        int keys = bwBuf ? findKeysBW(array, a, b, rRun, ideal)
                         : findKeys(array, a, b, lRun, ideal);
        if (keys == 1)
            return;
        if (keys <= 4) {
            lazyStableSort(array, a, b);
            return;
        }
        if (keys < ideal) {
            while (bLen > keys)
                bLen /= 2;
            keys = bLen;
        }
        int a1, b1, p;
        if (bwBuf) {
            a1 = a;
            b1 = b - keys;
            p = b1;
        } else {
            a1 = a + keys;
            b1 = b;
            p = a;
        }
        len = b1 - a1;
        if (!buildRuns(array, a1, b1, j)) {
            int i;
            for (; j <= bLen; j *= 2)
                for (i = a1; i+j < b1; i += 2*j)
                    merge(array, i, i + j, Math.min(i + 2 * j, b1), p, true);
            for (; j < len; j *= 2) {
                for (i = a1; i+j < b1; i += 2*j)
                    rotateMerge(array, i, i + j, Math.min(i + 2 * j, b1), p, bLen);
            }
        }
        insertSort(array, p, p + bLen);
        if (bwBuf) {
            a = expSearch(array, a, b1, array[b1], true, false);
            if (keys >= ideal/2)
                this.redistBufferBW(array, a, b1, b);
            else
                inPlaceMerge(array, a, b1, b, false);
        } else {
            b = expSearch(array, a1, b, array[a1 - 1], false, true);
            if (keys >= ideal/2) this.redistBuffer(array, a, a1, b);
            else
                inPlaceMerge(array, a, a1, b, false);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
