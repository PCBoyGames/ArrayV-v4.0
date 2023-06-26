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
public final class AyatoSort extends Sort {

    public AyatoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Ayato");
        this.setRunAllSortsName("Ayato Sort");
        this.setRunSortName("Ayatosort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
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
        if (a != b) Writes.write(array, b, temp, 0.5, true, false);
    }

    // Easy patch to avoid the "reversals can be done in a single swap" notes.
    protected void reverse(int[] array, int a, int b) {
        if (b - a >= 3) Writes.reversal(array, a, b, 1, true, false);
        else Writes.swap(array, a, b, 1, true, false);
    }

    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        if (a >= m || m >= b) return;
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
            if (r == 1) this.insertTo(array, m, a);
            else if (l == 1) this.insertTo(array, a, b - 1);
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
            if (c < 0 || (left && c == 0)) b = m;
            else a = m + 1;
        }
        return a;
    }

    protected int leftExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left)
            while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0) i *= 2;
        else
            while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0) i *= 2;
        int a1 = a + i / 2, b1 = Math.min(b, a - 1 + i);
        return binSearch(array, a1, b1, val, left);
    }

    protected int rightExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left)
            while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0) i *= 2;
        else
            while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
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

    protected int findRun(int[] array, int a, int b, int mRun) {
        int i = a + 1;
        if (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) i++;
                reverse(array, a, i - 1);
            } else while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        }
        Highlights.clearMark(2);
        while (i - a < mRun && i < b) {
            insertTo(array, i, rightExpSearch(array, a, i, array[i], false));
            i++;
        }
        return i;
    }

    public void insertSort(int[] array, int a, int b) {
        findRun(array, a, b, b - a);
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

    public void inPlaceMerge(int[] array, int a, int m, int b, boolean bnd) {
        if (bnd) {
            if (a >= m || m >= b || Reads.compareValues(array[m - 1], array[m]) <= 0) return;
            a = leftExpSearch(array, a, m, array[m], false);
            b = rightExpSearch(array, m, b, array[m - 1], true);
            if (Reads.compareValues(array[a], array[b - 1]) > 0) {
                rotate(array, a, m, b);
                return;
            }
        }
        if (b - m <= m - a) inPlaceMergeBW(array, a, m, b);
        else inPlaceMergeFW(array, a, m, b);
    }

    protected void mergeFW(int[] array, int a, int m, int b, int p) {
        int pLen = m - a;
        multiSwap(array, a, p, pLen, true);
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
        multiSwap(array, m, p, pLen, true);
        int i = pLen - 1, j = m - 1, k = b - 1;
        while (i >= 0 && j >= a) {
            if (Reads.compareValues(array[p + i], array[j]) >= 0)
                Writes.swap(array, k--, p + (i--), 1, true, false);
            else
                Writes.swap(array, k--, j--, 1, true, false);
        }
        while (i >= 0) Writes.swap(array, k--, p + (i--), 1, true, false);
    }

    public void merge(int[] array, int a, int m, int b, int p, boolean bnd) {
        if (bnd) {
            if (a >= m || m >= b || Reads.compareValues(array[m - 1], array[m]) <= 0) return;
            a = leftExpSearch(array, a, m, array[m], false);
            b = rightExpSearch(array, m, b, array[m - 1], true);
            if (Reads.compareValues(array[a], array[b - 1]) > 0) {
                rotate(array, a, m, b);
                return;
            }
        }
        if (Math.min(m - a, b - m) <= 8) {
            if (m - a > b - m) inPlaceMergeBW(array, a, m, b);
            else inPlaceMergeFW(array, a, m, b);
        } else if (b - m < m - a) mergeBW(array, a, m, b, p);
        else mergeFW(array, a, m, b, p);
    }

    protected void redistBufferFW(int[] array, int a, int m, int b) {
        int rPos = leftExpSearch(array, m, b, array[a], true);
        rotate(array, a, m, rPos);
        int dist = rPos - m;
        a += dist;
        m += dist;
        int a1 = a + (m - a) / 2;
        rPos = leftExpSearch(array, m, b, array[a1], true);
        rotate(array, a1, m, rPos);
        dist = rPos - m;
        a1 += dist;
        m += dist;
        inPlaceMerge(array, a, a1 - dist, a1, false);
        inPlaceMerge(array, a1, m, b, false);
    }

    protected void redistBufferBW(int[] array, int a, int m, int b) {
        int rPos = rightExpSearch(array, a, m, array[b - 1], false);
        rotate(array, rPos, m, b);
        int dist = m - rPos;
        b -= dist;
        m -= dist;
        int b1 = m + (b - m) / 2;
        rPos = rightExpSearch(array, a, m, array[b1 - 1], false);
        rotate(array, rPos, m, b1);
        dist = m - rPos;
        b1 -= dist;
        m -= dist;
        inPlaceMerge(array, b1, b1 + dist, b, false);
        inPlaceMerge(array, a, m, b1, false);
    }

    public void rotateMerge(int[] array, int a, int m, int b, int p, int pLen) {
        while (Math.min(m - a, b - m) > pLen) {
            if (a >= m || m >= b || Reads.compareValues(array[m - 1], array[m]) <= 0) return;
            a = leftExpSearch(array, a, m, array[m], false);
            b = rightExpSearch(array, m, b, array[m - 1], true);
            if (Reads.compareValues(array[a], array[b - 1]) > 0) {
                rotate(array, a, m, b);
                return;
            }
            if (Math.min(m - a, b - m) <= pLen) {
                merge(array, a, m, b, p, false);
                return;
            }
            int m1, m2, m3;
            if (m - a >= b - m) {
                m1 = a + (m - a) / 2;
                m2 = binSearch(array, m, b, array[m1], true);
                m3 = m1 + (m2 - m);
            } else {
                m2 = m + (b - m) / 2;
                m1 = binSearch(array, a, m, array[m2], false);
                m3 = (m2++) - (m - m1);
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
        int mRun = b - a;
        while (mRun >= 32) mRun = (mRun + 1) / 2;
        int i, j, k;
        while (true) {
            i = findRun(array, a, b, mRun);
            if (i >= b) break;
            j = findRun(array, i, b, mRun);
            inPlaceMerge(array, a, i, j, true);
            if (j >= b) break;
            k = j;
            while (true) {
                i = findRun(array, k, b, mRun);
                if (i >= b) break;
                j = findRun(array, i, b, mRun);
                inPlaceMerge(array, k, i, j, true);
                if (j >= b) break;
                k = j;
            }
        }
    }

    void innerSort(int[] array, int a, int b, int p, int pLen, int mRun) {
        int i, j, k;
        while (true) {
            i = findRun(array, a, b, mRun);
            if (i >= b) break;
            j = findRun(array, i, b, mRun);
            rotateMerge(array, a, i, j, p, pLen);
            if (j >= b) break;
            k = j;
            while (true) {
                i = findRun(array, k, b, mRun);
                if (i >= b) break;
                j = findRun(array, i, b, mRun);
                rotateMerge(array, k, i, j, p, pLen);
                if (j >= b) break;
                k = j;
            }
        }
    }

    public void mergeSort(int[] array, int a, int b) {
        int len = b - a;
        if (len < 64) {
            lazyStableSort(array, a, b);
            return;
        }
        int mRun = len;
        while (mRun >= 32) mRun = (mRun + 1) / 2;
        int bLen = mRun;
        for (bLen = mRun; bLen * bLen < len; bLen *= 2);
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
        int keys;
        if (bwBuf) keys = findKeysBW(array, a, b, rRun, ideal);
        else keys = findKeys(array, a, b, lRun, ideal);
        if (keys == 1) return;
        if (keys <= 4) {
            lazyStableSort(array, a, b);
            return;
        }
        if (keys < ideal) {
            while (bLen > keys) bLen /= 2;
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
        innerSort(array, a1, b1, p, bLen, mRun);
        insertSort(array, p, p + bLen);
        if (bwBuf) {
            a = leftExpSearch(array, a, b1, array[b1], false);
            if (keys >= ideal/2) redistBufferBW(array, a, b1, b);
            else inPlaceMerge(array, a, b1, b, false);
        } else {
            b = rightExpSearch(array, a1, b, array[a1 - 1], true);
            if (keys >= ideal/2) redistBufferFW(array, a, a1, b);
            else inPlaceMerge(array, a, a1, b, false);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
