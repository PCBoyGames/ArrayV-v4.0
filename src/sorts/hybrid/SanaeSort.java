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
public class SanaeSort extends Sort {

    public SanaeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Sanae");
        this.setRunAllSortsName("Sanae Sort");
        this.setRunSortName("Sanaesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    static int ceilCbrt(int n) {
        int r = 0;
        while (r * r * r < n)
            r++;
        return r;
    }

    protected void multiSwap(int[] array, int a, int b, int len, boolean fw) {
        if (a == b) // avoid self-swaps
            return;
        if (fw)
            for (int i = 0; i < len; i++)
                Writes.swap(array, a + i, b + i, 1, true, false);
        else
            for (int i = len - 1; i >= 0; i--)
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

    // Easy patch to avoid self-reversals and the "reversals can be done in a single
    // swap" notes.
    protected void reverse(int[] array, int a, int b) {
        if (b - a >= 3)
            Writes.reversal(array, a, b, 1, true, false);
        else
            Writes.swap(array, a, b, 1, true, false);
    }

    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        if (a >= m || m >= b)
            return;
        int l = m - a, r = b - m;
        if (l % r == 0 || r % l == 0) {
            while (l > 1 && r > 1)
                if (r < l) {
                    this.multiSwap(array, m - r, m, r, false);
                    b -= r;
                    m -= r;
                    l -= r;
                } else {
                    this.multiSwap(array, a, m, l, true);
                    a += l;
                    m += l;
                    r -= l;
                }
            if (r == 1)
                this.insertTo(array, m, a);
            else if (l == 1)
                this.insertTo(array, a, b - 1);
        } else {
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
            if (p0 < p3) { // don't count reversals that don't do anything (doubles as self-reversal
                           // avoidance)
                reverse(array, p0, p3);
                Highlights.clearMark(2);
            }
        }
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
                a = m + 1;
        }
        return a;
    }

    protected int leftExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left)
            while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0)
                i *= 2;
        else
            while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
                i *= 2;
        int a1 = a + i / 2, b1 = Math.min(b, a - 1 + i);
        return binSearch(array, a1, b1, val, left);
    }

    protected int rightExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left)
            while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0)
                i *= 2;
        else
            while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
                i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        return binSearch(array, a1, b1, val, left);
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
                reverse(array, j, i - 1);
            } else
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;
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

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = leftExpSearch(array, m, b, array[a], true);
            rotate(array, a, m, i);
            int t = i - m;
            m = i;
            a += t + 1;
            if (m >= b)
                break;
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
            if (m <= a)
                break;
            b = rightExpSearch(array, m, b, array[m - 1], true);
        }
    }

    public void inPlaceMerge(int[] array, int a, int m, int b) {
        if (a >= m || m >= b || Reads.compareValues(array[m - 1], array[m]) <= 0)
            return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        if (b - m <= m - a)
            inPlaceMergeBW(array, a, m, b);
        else
            inPlaceMergeFW(array, a, m, b);
    }

    protected void mergeFW(int[] array, int a, int m, int b, int p) {
        int pLen = m - a;
        multiSwap(array, a, p, pLen, true);
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
        multiSwap(array, m, p, pLen, true);
        int i = pLen - 1, j = m - 1, k = b - 1;
        while (i >= 0 && j >= a)
            if (Reads.compareValues(array[p + i], array[j]) >= 0)
                Writes.swap(array, k--, p + (i--), 1, true, false);
            else
                Writes.swap(array, k--, j--, 1, true, false);
        while (i >= 0)
            Writes.swap(array, k--, p + (i--), 1, true, false);
    }

    public void merge(int[] array, int a, int m, int b, int p) {
        if (a >= m || m >= b || Reads.compareValues(array[m - 1], array[m]) <= 0)
            return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        if (b - m < m - a)
            mergeBW(array, a, m, b, p);
        else
            mergeFW(array, a, m, b, p);
    }

    protected void fragmentedMergeFW(int[] array, int a, int m, int b, int s) {
        if (a >= m || m >= b || Reads.compareValues(array[m - 1], array[m]) <= 0)
            return;
        a = leftExpSearch(array, a, m, array[m], false);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        while (m - a > s && m < b) {
            int rPos;
            int dist;
            int a1 = a + s;
            rPos = binSearch(array, m, b, array[a1], true);
            rotate(array, a1, m, rPos);
            dist = rPos - m;
            a1 += dist;
            m += dist;
            inPlaceMerge(array, a, a1 - dist, a1);
            a = a1;
        }
        inPlaceMerge(array, a, m, b);
    }

    protected void fragmentedMergeBW(int[] array, int a, int m, int b, int s) {
        if (a >= m || m >= b || Reads.compareValues(array[m - 1], array[m]) <= 0)
            return;
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        while (b - m > s && m > a) {
            int rPos;
            int dist;
            int b1 = b - s;
            rPos = binSearch(array, a, m, array[b1 - 1], false);
            rotate(array, rPos, m, b1);
            dist = m - rPos;
            b1 -= dist;
            m -= dist;
            inPlaceMerge(array, b1, b1 + dist, b);
            b = b1;
        }
        inPlaceMerge(array, a, m, b);
    }

    protected void lazyBufferedMergeBW(int[] array, int a, int m, int b, int p) {
        if (a >= m || m >= b || Reads.compareValues(array[m - 1], array[m]) <= 0)
            return;
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        int pLen = b - m;
        multiSwap(array, m, p, pLen, true);
        int i = pLen - 1, j = m - 1, k = b - 1;
        while (i >= 0 && j >= a) {
            if (Reads.compareValues(array[j], array[p + i]) > 0) {
                int q = rightExpSearch(array, a, j + 1, array[p + i], false);
                while (j >= q)
                    Writes.swap(array, k--, j--, 1, true, false);
            }
            Writes.swap(array, k--, p + (i--), 1, true, false);
        }
        while (i >= 0) {
            Writes.swap(array, k--, p + (i--), 1, true, false);
        }
    }

    protected void lazyStableSort(int[] array, int a, int b) {
        int j = b - a;
        for (; j >= 32; j = (j + 1) / 2);
        if (buildRuns(array, a, b, j))
            return;
        for (int i; j < b - a; j *= 2) {
            for (i = a; i + j < b; i += 2 * j)
                inPlaceMerge(array, i, i + j, Math.min(i + 2 * j, b));
        }
    }

    protected void lazierestSort(int[] array, int a, int b) {
        int len = b - a;
        if (len < 32) {
            lazyStableSort(array, a, b);
            return;
        }
        int s = ceilCbrt(len), s1 = s*s;
        if (buildRuns(array, a, b, s))
            return;
        for (int i = a + s, j = a; i < b; i += s) {
            if (i - j == s1) {
                j += s1;
                i += s;
            }
            inPlaceMerge(array, j, i, Math.min(i + s, b));
        }
        for (int i = a + s1; i < b; i += s1) {
            fragmentedMergeBW(array, a, i, Math.min(i + s1, b), s);
        }
    }

    public void mergeSort(int[] array, int a, int b) {
        int len = b - a;
        if (len <= 64) {
            lazyStableSort(array, a, b);
            return;
        }
        int bLen, mRun;
        for (bLen = 1; (bLen*bLen*bLen)/len < len; bLen *= 2);
        for (mRun = 1; (mRun*mRun*mRun)/len < len; mRun++);
        mRun = (16*mRun) / bLen;
        bLen = (bLen*mRun) / 16;
        // choose direction to find keys
        boolean bwBuf;
        int rRun = buildUniqueRunBW(array, b, bLen), lRun = 0;
        if (rRun == bLen) bwBuf = true;
        else {
            lRun = buildUniqueRun(array, a, bLen);
            if (lRun == bLen) bwBuf = false;
            else bwBuf = (rRun < 16 && lRun < 16) || rRun >= lRun;
        }
        int keys = bwBuf ? findKeysBW(array, a, b, rRun, bLen)
                         : findKeys(array, a, b, lRun, bLen);
        if (keys < bLen) {
            if (keys == 1) return;
            else if (keys <= ceilCbrt(len)) this.lazyStableSort(array, a, b);
            else this.lazierestSort(array, a, b);
            return;
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
        if (!buildRuns(array, a1, b1, mRun)) {
            int i, j = mRun;
            for (; j < bLen; j *= 2)
                for (i = a1; i + j < b1; i += 2 * j)
                    merge(array, i, i + j, Math.min(i + 2 * j, b1), p);
            for (i = a1 + bLen; i < b1; i += bLen)
                lazyBufferedMergeBW(array, a1, i, Math.min(i + bLen, b1), p);
        }
        lazierestSort(array, p, p + bLen);
        int s = Math.max(16, ceilCbrt(b - a));
        if (bwBuf) {
            a = leftExpSearch(array, a, b1, array[b1], false);
            fragmentedMergeBW(array, a, b1, b, s);
        } else {
            b = rightExpSearch(array, a1, b, array[a1 - 1], true);
            fragmentedMergeFW(array, a, a1, b, s);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
