package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite and Distray

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 * @author Distray
 *
 */
public final class SimplifiedBlockTimSort extends Sort {

    public SimplifiedBlockTimSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Simplified Block Tim");
        setRunAllSortsName("Simplified Block Tim Sort");
        setRunSortName("Simplified Block Timsort");
        setCategory("Hybrid Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    // adaptive stable merge sort with O(sqrt(n)) dynamic external buffer

    public static int getMinLevel(int n) {
        while (n >= 32)
            n = (n + 1) / 2;
        return n;
    }

    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b)
            Writes.swap(array, a, b, pause, mark, aux);
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        for (int i = a; i > b; i--)
            Writes.write(array, i, array[i - 1], 0.5, true, false);
        if (a != b)
            Writes.write(array, b, temp, 0.5, true, false);
    }

    protected void multiSwap(int[] array, int a, int b, int len) {
        if (a != b)
            for (int i = 0; i < len; i++)
                Writes.swap(array, a + i, b + i, 1, true, false);
    }

    // Conjoined Gries-Mills rotations (by Distray)
    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        int lenA = m - a, lenB = b - m, pos = a;
        int end = pos + lenA + lenB;
        while (lenA > 0 && lenB > 0)
            if (lenA < lenB) {
                for (int i = 0; i < lenA; i++) {
                    int t = array[pos + i], j = pos + i + lenA;
                    for (; j < end; j += lenA)
                        Writes.write(array, j - lenA, array[j], 1, true, false);
                    Writes.write(array, j - lenA, t, 1, true, false);
                }
                pos += lenB;
                lenB %= lenA;
                lenA -= lenB;
            } else {
                for (int i = 0; i < lenB; i++) {
                    int t = array[pos + i + lenA], j = pos + i + lenA - lenB;
                    for (; j >= pos; j -= lenB)
                        Writes.write(array, j + lenB, array[j], 1, true, false);
                    Writes.write(array, j + lenB, t, 1, true, false);
                }
                end = pos + lenB;
                lenA %= lenB;
                lenB -= lenA;
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
                a = m+1;
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
                insertTo(array, i, rightExpSearch(array, j, i, array[i], false));
                i++;
            }
            j = i++;
        }
        return noSort;
    }

    protected void mergeTo(int[] from, int[] to, int a, int m, int b, int p, boolean aux) {
        int i = a, j = m;
        while (i < m && j < b) {
            Highlights.markArray(2, i);
            Highlights.markArray(3, j);
            if (Reads.compareValues(from[i], from[j]) <= 0)
                Writes.write(to, p++, from[i++], 1, true, aux);
            else
                Writes.write(to, p++, from[j++], 1, true, aux);
        }
        Highlights.clearMark(3);
        while (i < m) {
            Highlights.markArray(2, i);
            Writes.write(to, p++, from[i++], 1, true, aux);
        }
        while (j < b) {
            Highlights.markArray(2, j);
            Writes.write(to, p++, from[j++], 1, true, aux);
        }
        Highlights.clearMark(2);
    }

    protected void mergeFWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = m - a;
        Writes.arraycopy(array, a, tmp, 0, s, 1, true, true);
        int i = 0, j = m;
        while (i < s && j < b)
            if (Reads.compareValues(tmp[i], array[j]) <= 0)
                Writes.write(array, a++, tmp[i++], 1, true, false);
            else
                Writes.write(array, a++, array[j++], 1, true, false);
        while (i < s)
            Writes.write(array, a++, tmp[i++], 1, true, false);
    }

    protected void mergeBWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = b - m;
        Writes.arraycopy(array, m, tmp, 0, s, 1, true, true);
        int i = s - 1, j = m - 1;
        while (i >= 0 && j >= a)
            if (Reads.compareValues(tmp[i], array[j]) >= 0)
                Writes.write(array, --b, tmp[i--], 1, true, false);
            else
                Writes.write(array, --b, array[j--], 1, true, false);
        while (i >= 0)
            Writes.write(array, --b, tmp[i--], 1, true, false);
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

    protected void merge(int[] array, int[] buf, int a, int m, int b) {
        Highlights.clearMark(2);
        if (Math.min(m - a, b - m) <= 8) {
            if (m - a > b - m)
                inPlaceMergeBW(array, a, m, b);
            else
                inPlaceMergeFW(array, a, m, b);
        } else if (m - a > b - m)
            mergeBWExt(array, buf, a, m, b);
        else
            mergeFWExt(array, buf, a, m, b);
    }

    void mergeBlocks(int[] array, int[] buf, int a, int m, int b, boolean left) {
        int s = b - m;
        Writes.arraycopy(array, m, buf, 0, s, 1, true, true);
        int i = s - 1, j = m - 1;
        while (i >= 0 && j >= a) {
            int cmp = Reads.compareValues(buf[i], array[j]);
            if (cmp > 0 || (left && cmp == 0))
                Writes.write(array, --b, buf[i--], 1, true, false);
            else
                Writes.write(array, --b, array[j--], 1, true, false);
        }
        while (i >= 0)
            Writes.write(array, --b, buf[i--], 1, true, false);
    }

    void blockSelect(int[] array, int[] tags, int a, int tm, int tb, int bLen) {
        int i1 = 0, j1 = tm, k = 0;

        while (k < j1 && j1 < tb) {
            if (Reads.compareValues(array[a+(i1+1)*bLen-1], array[a+(j1+1)*bLen-1]) <= 0) {
                if (i1 > k) this.multiSwap(array, a+k*bLen, a+i1*bLen, bLen);
                swap(tags, k++, i1, 1, true, true);

                i1 = k;
                for (int i = Math.max(k+1, tm); i < j1; i++)
                    if (Reads.compareOriginalValues(tags[i], tags[i1]) < 0)
                        i1 = i;
            }
            else {
                this.multiSwap(array, a+k*bLen, a+j1*bLen, bLen);
                swap(tags, k, j1++, 1, true, true);

                if (i1 == k++) i1 = j1-1;
            }
        }
        while (k < j1-1) {
            if (i1 > k) this.multiSwap(array, a+k*bLen, a+i1*bLen, bLen);
            swap(tags, k++, i1, 1, true, true);

            i1 = k;
            for (int i = k+1; i < j1; i++)
                if (Reads.compareOriginalValues(tags[i], tags[i1]) < 0)
                    i1 = i;
        }
    }

    void blockMerge(int[] array, int[] buf, int[] tags, int a, int m, int b, int bLen) {
        int tLen1 = (m-a)/bLen, bCnt = (b-a)/bLen;
        int tm = tLen1, tb = bCnt, b1 = b-(b-m)%bLen;
        for (int k = 0; k < bCnt; k++)
            Writes.write(tags, k, k, 0, true, true);
        int mKey = tags[tm];
        this.blockSelect(array, tags, a, tm, tb, bLen);

        int f = a;
        boolean left = Reads.compareOriginalValues(tags[0], mKey) < 0;

        for (int i = 1; i < bCnt; i++) {
            if (left ^ (Reads.compareOriginalValues(tags[i], mKey)) < 0) {
                int nxt  = a + i*bLen;
                int nxtE = this.binSearch(array, nxt, nxt+bLen, array[nxt-1], left);

                this.mergeBlocks(array, buf, f, nxt, nxtE, left);
                f = nxtE;
                left = !left;
            }
        }
        if (left) this.mergeBlocks(array, buf, f, b1, b, left);
    }

    protected void smartMerge(int[] array, int[] buf, int a, int m, int b) {
        if (a >= m || m >= b)
            return;
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0)
            return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        merge(array, buf, a, m, b);
    }

    protected void pingPongMerge(int[] array, int[] buf, int a, int m1, int m2, int m3, int b) {
        int p = 0, p1 = p + m2-a, pEnd = p + b-a;
        if (Reads.compareIndices(array, m1-1, m1, 1, true) > 0
        || (m3 < b && Reads.compareIndices(array, m3-1, m3, 1, true) > 0)) {
            mergeTo(array, buf, a, m1, m2, p, true);
            mergeTo(array, buf, m2, m3, b, p1, true);
            mergeTo(buf, array, p, p1, pEnd, a, false);
        }
        else
            smartMerge(array, buf, a, m2, b);
    }

    protected void smartBlockMerge(int[] array, int[] buf, int[] tags, int a, int m, int b, int bLen) {
        if (a >= m || m >= b)
            return;
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0)
            return;
        int s = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareValues(array[a], array[b - 1]) > 0) {
            rotate(array, a, m, b);
            return;
        }
        if (Math.min(m - s, b - m) <= bLen)
            merge(array, buf, s, m, b);
        else {
            s -= (s - a) % bLen;
            blockMerge(array, buf, tags, s, m, b, bLen);
        }
    }

    public void blockMergeSort(int[] array, int a, int b) {
        int len = b - a;
        if (len < 128) { // adaptive bottom-up merge sort
            int j = getMinLevel(len);
            if (buildRuns(array, a, b, j))
                return;
            int[] tmp = Writes.createExternalArray(len / 2);
            int i;
            for (; j < len; j *= 2) {
                for (i = a; i + 2 * j <= b; i += 2 * j)
                    smartMerge(array, tmp, i, i + j, i + 2 * j);
                if (i + j < b)
                    smartMerge(array, tmp, i, i + j, b);
            }
            Writes.deleteExternalArray(tmp);
            return;
        }
        int j = getMinLevel(len);
        int bLen;
        for (bLen = j; bLen * bLen < len; bLen *= 2);
        int tLen = len / bLen + 2;
        if (buildRuns(array, a, b, j))
            return;
        int[] buf = Writes.createExternalArray(bLen);
        int i;
        for (; 4 * j <= bLen; j *= 4) {
            for (i = a; i+2*j < b; i += 4*j)
                pingPongMerge(array, buf, i, i+j, i+2*j, Math.min(i+3*j, b), Math.min(i+4*j, b));
            if (i + j < b)
                smartMerge(array, buf, i, i + j, b);
        }
        for (; j <= bLen; j *= 2) {
            for (i = a; i + 2 * j <= b; i += 2 * j)
                smartMerge(array, buf, i, i + j, i + 2 * j);
            if (i + j < b)
                smartMerge(array, buf, i, i + j, b);
        }
        int[] tags = Writes.createExternalArray(tLen);
        for (; j < len; j *= 2) {
            for (i = a; i + 2 * j <= b; i += 2 * j)
                smartBlockMerge(array, buf, tags, i, i + j, i + 2 * j, bLen);
            if (i + j < b)
                smartBlockMerge(array, buf, tags, i, i + j, b, bLen);
        }
        Writes.deleteExternalArray(tags);
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        blockMergeSort(array, 0, sortLength);

    }

}
