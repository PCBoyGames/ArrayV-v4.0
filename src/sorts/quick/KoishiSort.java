package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite, Distray and Scandum

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 * @author Distray
 * @author Scandum
 *
 */
public class KoishiSort extends Sort {

    public KoishiSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Koishi");
        this.setRunAllSortsName("Koishi Sort");
        this.setRunSortName("Koishisort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    int equ(int a, int b) {
        return ((a - b) >> 31) + ((b - a) >> 31) + 1;
    }

    protected int medOf3(int[] array, int i0, int i1, int i2) {
        int tmp;
        if (Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            tmp = i1;
            i1 = i0;
        } else tmp = i0;
        if (Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if (Reads.compareIndices(array, tmp, i2, 1, true) > 0) return tmp;
            return i2;
        }
        return i1;
    }

    public int medP3(int[] array, int a, int b, int d) {
        if (b - a == 3 || (b - a > 3 && d == 0))
            return medOf3(array, a, a + (b - a) / 2, b - 1);
        if (b - a < 3) return a + (b - a) / 2;
        int t = (b - a) / 3;
        int l = medP3(array, a, a + t, --d), c = medP3(array, a + t, b - t, d), r = medP3(array, b - t, b, d);
        // median
        return medOf3(array, l, c, r);
    }

    public int medOfMed(int[] array, int a, int b) {
        int log5 = 0, exp5 = 1, exp5_1 = 0;
        int[] indices = new int[5];
        int n = b - a;
        while (exp5 < n) {
            exp5_1 = exp5;
            log5++;
            exp5 *= 5;
        }
        if (log5 < 1) return a;
        // fill indexes, recursing if required
        if (log5 == 1) for (int i = a, j = 0; i < b; i++, j++) indices[j] = i;
        else {
            n = 0;
            for (int i = a; i < b; i += exp5_1) {
                indices[n] = medOfMed(array, i, Math.min(b, i + exp5_1));
                n++;
            }
        }
        // sort - insertion sort is good enough for 5 elements
        for (int i = 1; i < n; i++) {
            for (int j = i; j > 0; j--) {
                if (Reads.compareIndices(array, indices[j], indices[j - 1], 0.5, true) < 0) {
                    int t = indices[j];
                    indices[j] = indices[j - 1];
                    indices[j - 1] = t;
                } else break;
            }
        }
        // return median
        return indices[(n - 1) / 2];
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

    protected void insertTo(int[] array, int a, int b, boolean aux) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, aux);
        if (a != b)
            Writes.write(array, b, temp, 0.5, true, aux);
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
        while (i < s) Writes.write(array, a++, tmp[i++], 1, true, false);
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
        while (i >= 0) Writes.write(array, --b, tmp[i--], 1, true, false);
    }

    protected void merge(int[] array, int[] buf, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0) return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        Highlights.clearMark(2);
        if (m - a > b - m)
            mergeBWExt(array, buf, a, m, b);
        else
            mergeFWExt(array, buf, a, m, b);
    }

    protected int findRun(int[] array, int a, int b, int mRun) {
        int i = a + 1;
        if (i < b)
            if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) i++;
                if (i - a < 4) Writes.swap(array, a, i - 1, 1.0, true, false);
                else Writes.reversal(array, a, i - 1, 1.0, true, false);
            } else while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        Highlights.clearMark(2);
        while (i - a < mRun && i < b) {
            insertTo(array, i, rightExpSearch(array, a, i, array[i], false), false);
            i++;
        }
        return i;
    }

    public void insertSort(int[] array, int a, int b, boolean aux) {
        int i = a + 1;
        if (i < b)
            if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) i++;
                if (i - a < 4) Writes.swap(array, a, i - 1, 1.0, true, aux);
                else Writes.reversal(array, a, i - 1, 1.0, true, aux);
            } else while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        Highlights.clearMark(2);
        for (; i < b; i++) insertTo(array, i, rightExpSearch(array, a, i, array[i], false), aux);
    }

    public void mergeSort(int[] array, int[] buf, int a, int b) {
        int i, j, k;
        int mRun = 16;
        while (true) {
            i = findRun(array, a, b, mRun);
            if (i >= b) break;
            j = findRun(array, i, b, mRun);
            merge(array, buf, a, i, j);
            Highlights.clearMark(2);
            if (j >= b) break;
            k = j;
            while (true) {
                i = findRun(array, k, b, mRun);
                if (i >= b) break;
                j = findRun(array, i, b, mRun);
                merge(array, buf, k, i, j);
                if (j >= b) break;
                k = j;
            }
        }
    }

    protected void copyReverse(int[] src, int srcPos, int[] dest, int destPos, int len, boolean aux) {
        for (int i = 0; i < len; i++)
            Writes.write(dest, destPos + i, src[srcPos + len - 1 - i], 1, !aux, aux);
    }

    protected void stableSegmentReversal(int[] array, int start, int end) {
        if (end - start < 3) Writes.swap(array, start, end, 0.75, true, false);
        else Writes.reversal(array, start, end, 0.75, true, false);
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (i < end && Reads.compareIndices(array, i, i + 1, 0.5, true) == 0) i++;
            right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, 0.75, true, false);
                else Writes.reversal(array, left, right, 0.75, true, false);
            }
            i++;
        }
    }

    protected void sortHelper(int[] array, int[] tmp, int swapoff, int start, int end, boolean aux, boolean bad) {
        if (end - start <= 32) {
            insertSort(array, start, end, aux);
            return;
        }
        int pIdx;
        if (bad) pIdx = medOfMed(array, start, end);
        else pIdx = medP3(array, start, end, 1);
        int piv = array[pIdx];
        Highlights.clearMark(2);
        int p0 = start, p1 = swapoff, p2 = swapoff + (end - start);
        for (int i = start; i < end; i++) {
            int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
            if (cmp < 0) {
                Writes.write(array, p0++, array[i], 0.5, true, aux);
            } else if (cmp == 0) {
                Writes.write(tmp, --p2, array[i], 0.5, false, !aux);
            } else {
                Writes.write(tmp, p1++, array[i], 0.5, false, !aux);
            }
        }
        int eqLen = swapoff + (end - start) - p2;
        copyReverse(tmp, p2, array, p0, eqLen, aux);
        int lLen = p0 - start, rLen = p1 - swapoff;
        if (eqLen == end - start) return;
        if (lLen == 0) bad = eqLen < rLen / 8;
        else if (rLen == 0) bad = eqLen < lLen / 8;
        else bad = lLen < rLen / 8 || rLen < lLen / 8;
        sortHelper(tmp, array, p0 + eqLen, swapoff, p1, !aux, bad);
        Writes.arraycopy(tmp, swapoff, array, p0 + eqLen, p1 - swapoff, 1.0, true, aux);
        sortHelper(array, tmp, swapoff, start, p0, aux, bad);
    }

    public void quickSort(int[] array, int a, int b) {
        int len = b - a;
        int balance = 0, eq = 0, streaks = 0, dist, eqdist, loop, cnt = len, pos = a;
        while (cnt > 16) {
            for (eqdist = dist = 0, loop = 0; loop < 16; loop++) {
                int cmp = Reads.compareIndices(array, pos, pos + 1, 0.5, true);
                dist += cmp > 0 ? 1 : 0;
                eqdist += cmp == 0 ? 1 : 0;
                pos++;
            }
            streaks += equ(dist, 0) | equ(dist + eqdist, 16);
            balance += dist;
            eq += eqdist;
            cnt -= 16;
        }
        while (--cnt > 0) {
            int cmp = Reads.compareIndices(array, pos, pos + 1, 0.5, true);
            balance += cmp > 0 ? 1 : 0;
            eq += cmp == 0 ? 1 : 0;
            pos++;
        }
        if (balance == 0) return;
        if (balance + eq == len - 1) {
            if (eq > 0) stableSegmentReversal(array, a, b - 1);
            else if (b - a < 4) Writes.swap(array, a, b - 1, 0.75, true, false);
            else Writes.reversal(array, a, b - 1, 0.75, true, false);
            return;
        }
        int[] buf = Writes.createExternalArray(len);
        int sixth = len / 6;
        if (streaks > len / 20 || balance <= sixth || balance + eq >= len - sixth)
            mergeSort(array, buf, a, b);
        else
            sortHelper(array, buf, 0, a, b, false, false);
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
