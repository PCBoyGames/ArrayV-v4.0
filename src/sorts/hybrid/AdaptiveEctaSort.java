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
public final class AdaptiveEctaSort extends Sort {

    public AdaptiveEctaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Ecta");
        this.setRunAllSortsName("Adaptive Ecta Sort");
        this.setRunSortName("Adaptive Ectasort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // adaptive stable merge sort with O(sqrt(n)) dynamic external buffer

    public static int getMinLevel(int n) {
        while (n >= 32)
            n = (n + 1) / 2;
        return n;
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        for (int i = a; i > b; i--)
            Writes.write(array, i, array[i - 1], 0.5, true, false);
        if (a != b)
            Writes.write(array, b, temp, 0.5, true, false);
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
        while(a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if(c < 0 || (left && c == 0))
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
    
    protected void mergeTo(int[] from, int[] to, int a, int m, int b, int p, boolean aux) {
        int i = a, j = m;
        while(i < m && j < b) {
            Highlights.markArray(2, i);
            Highlights.markArray(3, j);
            if(Reads.compareValues(from[i], from[j]) <= 0)
                Writes.write(to, p++, from[i++], 1, true, aux);
            else
                Writes.write(to, p++, from[j++], 1, true, aux);
        }
        Highlights.clearMark(3);
        while(i < m) {
            Highlights.markArray(2, i);
            Writes.write(to, p++, from[i++], 1, true, aux);
        }
        while(j < b) {
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

    protected void blockCycle(int[] array, int[] buf, int[] keys, int a, int bLen, int bCnt) {
        for (int i = 0; i < bCnt; i++)
            if (Reads.compareOriginalValues(i, keys[i]) != 0) {
                Writes.arraycopy(array, a + i * bLen, buf, 0, bLen, 1, true, true);
                int j = i, next = keys[i];
                do {
                    Writes.arraycopy(array, a + next * bLen, array, a + j * bLen, bLen, 1, true, false);
                    Writes.write(keys, j, j, 1, true, true);
                    j = next;
                    next = keys[next];
                } while (Reads.compareOriginalValues(next, i) != 0);
                Writes.arraycopy(buf, 0, array, a + j * bLen, bLen, 1, true, false);
                Writes.write(keys, j, j, 1, true, true);
            }
    }

    protected void blockMerge(int[] array, int[] buf, int[] tags, int a, int m, int b, int bLen) {
        int c = 0, t = 2;
        int i = a, j = m, k = 0;
        int l = 0, r = 0;
        while (c++ < 2 * bLen) { // merge 2 blocks into buffer to create 2 buffers
            Highlights.markArray(2, i);
            Highlights.markArray(3, j);
            if (Reads.compareValues(array[i], array[j]) <= 0) {
                Writes.write(buf, k++, array[i++], 1, true, true);
                l++;
            } else {
                Writes.write(buf, k++, array[j++], 1, true, true);
                r++;
            }
        }
        boolean left = l >= r;
        k = left ? i - l : j - r;
        c = 0;
        do {
            if (i < m)
                Highlights.markArray(2, i);
            else
                Highlights.clearMark(2);
            if (j < b)
                Highlights.markArray(3, j);
            else
                Highlights.clearMark(3);
            if (i < m && (j == b || Reads.compareValues(array[i], array[j]) <= 0)) {
                Writes.write(array, k++, array[i++], 1, true, false);
                l++;
            } else {
                Writes.write(array, k++, array[j++], 1, true, false);
                r++;
            }
            if (++c == bLen) { // change buffer after every block
                Writes.write(tags, t++, (k - a) / bLen - 1, 0, false, true);
                if (left)
                    l -= bLen;
                else
                    r -= bLen;
                left = l >= r;
                k = left ? i - l : j - r;
                c = 0;
            }
        } while (i < m || j < b);
        Highlights.clearAllMarks();
        int b1 = b - c;
        Writes.arraycopy(array, k - c, array, b1, c, 1, true, false); // copy remainder to end (r buffer)
        r -= c;
        // l and r buffers are divisible by bLen
        t = 0;
        k = 0;
        while (l > 0) {
            Writes.arraycopy(buf, k, array, m - l, bLen, 1, true, false);
            Writes.write(tags, t++, (m - a - l) / bLen, 0, false, true);
            k += bLen;
            l -= bLen;
        }
        while (r > 0) {
            Writes.arraycopy(buf, k, array, b1 - r, bLen, 1, true, false);
            Writes.write(tags, t++, (b1 - a - r) / bLen, 0, false, true);
            k += bLen;
            r -= bLen;
        }
        blockCycle(array, buf, tags, a, bLen, (b - a) / bLen);
    }

    protected void smartMerge(int[] array, int[] buf, int a, int m, int b) {
        if (boundCheck(array, a, m, b))
            return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (checkReverseBounds(array, a, m, b))
            return;
        merge(array, buf, a, m, b);
    }
    
    protected void pingPongMerge(int[] array, int[] buf, int a, int m1, int m2, int m3, int b) {
        int p = 0, p1 = p + m2-a, pEnd = p + b-a;
        if(Reads.compareIndices(array, m1-1, m1, 1, true) > 0
        || (m3 < b && Reads.compareIndices(array, m3-1, m3, 1, true) > 0)) {
            mergeTo(array, buf, a, m1, m2, p, true);
            mergeTo(array, buf, m2, m3, b, p1, true);
            mergeTo(buf, array, p, p1, pEnd, a, false);
        }
        else 
            smartMerge(array, buf, a, m2, b);
    }

    protected void smartBlockMerge(int[] array, int[] buf, int[] tags, int a, int m, int b, int bLen) {
        if (boundCheck(array, a, m, b))
            return;
        int s = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (checkReverseBounds(array, s, m, b))
            return;
        if (Math.min(m - s, b - m) <= 2 * bLen)
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
        int tLen = len / bLen, bufLen = 2 * bLen;
        if (buildRuns(array, a, b, j))
            return;
        int[] buf  = Writes.createExternalArray(bufLen);
        int i;
        for (; 4 * j <= bufLen; j *= 4) {
            for(i = a; i+2*j < b; i += 4*j)
                pingPongMerge(array, buf, i, i+j, i+2*j, Math.min(i+3*j, b), Math.min(i+4*j, b));
            if (i + j < b)
                smartMerge(array, buf, i, i + j, b);
        }
        for (; j <= bufLen; j *= 2) {
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
