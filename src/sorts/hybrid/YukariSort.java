package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite and Scandum

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 * @author Scandum
 *
 */
public class YukariSort extends Sort {

    public YukariSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Yukari");
        this.setRunAllSortsName("Yukari Sort");
        this.setRunSortName("Yukarisort");
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

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b)
            Writes.write(array, b, temp, 0.5, true, false);
    }

    protected void rotateNoBuf(int[] array, int a, int m, int b) {
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
            if (p3 - p0 >= 3)
                Writes.reversal(array, p0, p3, 1, true, false);
            else
                Writes.swap(array, p0, p3, 1, true, false);
            Highlights.clearMark(2);
        }
    }

    protected void rotate(int[] array, int[] buf, int a, int m, int b) {
        Highlights.clearAllMarks();
        if (a >= m || m >= b)
            return;
        if (buf == null) {
            rotateNoBuf(array, a, m, b);
            return;
        }
        int pos = a, left = m - a, right = b - m;
        int pta = pos, ptb = pos + left, ptc = pos + right, ptd = ptb + right;
        if (left < right) {
            int bridge = right - left;
            if (bridge < left) {
                int loop = left;
                if (bridge > buf.length) {
                    rotateNoBuf(array, a, m, b);
                    return;
                }
                Writes.arraycopy(array, ptb, buf, 0, bridge, 1, true, true);
                while (loop-- > 0) {
                    Writes.write(array, --ptc, array[--ptd], 0.5, true, false);
                    Writes.write(array, ptd, array[--ptb], 0.5, true, false);
                }
                Writes.arraycopy(buf, 0, array, pta, bridge, 1, true, false);
            } else {
                if (left > buf.length) {
                    rotateNoBuf(array, a, m, b);
                    return;
                }
                Writes.arraycopy(array, pta, buf, 0, left, 1, true, true);
                Writes.arraycopy(array, ptb, array, pta, right, 1, true, false);
                Writes.arraycopy(buf, 0, array, ptc, left, 1, true, false);
            }
        } else if (right < left) {
            int bridge = left - right;
            if (bridge < right) {
                if (bridge > buf.length) {
                    rotateNoBuf(array, a, m, b);
                    return;
                }
                int loop = right;
                Writes.arraycopy(array, ptc, buf, 0, bridge, 1, true, true);
                while (loop-- > 0) {
                    Writes.write(array, ptc++, array[pta], 0.5, true, false);
                    Writes.write(array, pta++, array[ptb++], 0.5, true, false);
                }
                Writes.arraycopy(buf, 0, array, ptd - bridge, bridge, 1, true, false);
            } else {
                if (right > buf.length) {
                    rotateNoBuf(array, a, m, b);
                    return;
                }
                Writes.arraycopy(array, ptb, buf, 0, right, 1, true, true);
                while (left-- > 0)
                    Writes.write(array, --ptd, array[--ptb], 1, true, false);
                Writes.arraycopy(buf, 0, array, pta, right, 1, true, false);
            }
        } else {
            while (left-- > 0)
                Writes.swap(array, pta++, ptb++, 1, true, false);
            Highlights.clearMark(2);
        }
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

    protected void merge(int[] array, int[] buf, int a, int m, int b, boolean bnd) {
        if (bnd) {
            if (a >= m || m >= b || Reads.compareValues(array[m - 1], array[m]) <= 0)
                return;
            a = leftExpSearch(array, a, m, array[m], false);
            b = rightExpSearch(array, m, b, array[m - 1], true);
            if (Reads.compareValues(array[a], array[b - 1]) > 0) {
                rotate(array, buf, a, m, b);
                return;
            }
        }
        Highlights.clearMark(2);
        if (m - a > b - m)
            mergeBWExt(array, buf, a, m, b);
        else
            mergeFWExt(array, buf, a, m, b);
    }

    public void rotateMerge(int[] array, int[] buf, int a, int m, int b) {
        while (Math.min(m - a, b - m) > buf.length) {
            if (a >= m || m >= b || Reads.compareValues(array[m - 1], array[m]) <= 0)
                return;
            a = leftExpSearch(array, a, m, array[m], false);
            b = rightExpSearch(array, m, b, array[m - 1], true);
            if (Reads.compareValues(array[a], array[b - 1]) > 0) {
                rotate(array, buf, a, m, b);
                return;
            }
            if (Math.min(m - a, b - m) <= buf.length) {
                merge(array, buf, a, m, b, false);
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
            rotate(array, buf, m1, m, m2);
            if (b - (m3 + 1) < m3 - a) {
                rotateMerge(array, buf, m3 + 1, m2, b);
                m = m1;
                b = m3;
            } else {
                rotateMerge(array, buf, a, m1, m3);
                m = m2;
                a = m3 + 1;
            }
        }
        merge(array, buf, a, m, b, true);
    }

    protected int findRun(int[] array, int a, int b, int mRun) {
        int i = a + 1;
        if (i < b)
            if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0)
                    i++;
                if (i - a < 4)
                    Writes.swap(array, a, i - 1, 1.0, true, false);
                else
                    Writes.reversal(array, a, i - 1, 1.0, true, false);
            } else
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0)
                    i++;
        Highlights.clearMark(2);
        while (i - a < mRun && i < b) {
            insertTo(array, i, rightExpSearch(array, a, i, array[i], false));
            i++;
        }
        return i;
    }

    public void mergeSort(int[] array, int a, int b) {
        int len = b - a;
        if (len <= 32) {
            // insertion sort
            findRun(array, a, b, b - a);
            return;
        }
        int mRun = 16;
        int bLen = 1;
        while (bLen * bLen < len)
            bLen *= 2;
        int[] buf = Writes.createExternalArray(bLen);
        int i, j, k;
        while (true) {
            i = findRun(array, a, b, mRun);
            if (i >= b)
                break;
            j = findRun(array, i, b, mRun);
            rotateMerge(array, buf, a, i, j);
            Highlights.clearMark(2);
            if (j >= b)
                break;
            k = j;
            while (true) {
                i = findRun(array, k, b, mRun);
                if (i >= b)
                    break;
                j = findRun(array, i, b, mRun);
                rotateMerge(array, buf, k, i, j);
                if (j >= b)
                    break;
                k = j;
            }
        }
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
