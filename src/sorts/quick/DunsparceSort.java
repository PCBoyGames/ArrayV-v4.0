package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite and Californium-252

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+
 */

/**
 * @author Ayako-chan
 * @author aphitorite
 * @author Californium-252
 *
 */
public final class DunsparceSort extends Sort {

    public DunsparceSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Dunsparce");
        this.setRunAllSortsName("Dunsparce Sort");
        this.setRunSortName("Dunsparcesort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int medianOf3(int[] array, int... indices) {
        // 3 element case (only one triggered, other cases removed)
        // only first 3 elements are considered if given an array of 4+ indices
        if (Reads.compareIndices(array, indices[0], indices[1], 0.5, true) <= 0) {
            if (Reads.compareIndices(array, indices[1], indices[2], 0.5, true) <= 0)
                return indices[1];
            if (Reads.compareIndices(array, indices[0], indices[2], 0.5, true) < 0)
                return indices[2];
            return indices[0];
        }
        if (Reads.compareIndices(array, indices[1], indices[2], 0.5, true) >= 0) {
            return indices[1];
        }
        if (Reads.compareIndices(array, indices[0], indices[2], 0.5, true) <= 0) {
            return indices[0];
        }
        return indices[2];
    }

    private int medianOf9(int[] array, int start, int end) {
        // anti-overflow with good rounding
        int length = end - start;
        int half = length / 2;
        int quarter = half / 2;
        int eighth = quarter / 2;
        int med0 = medianOf3(array, start, start + eighth, start + quarter);
        int med1 = medianOf3(array, start + quarter + eighth, start + half, start + half + eighth);
        int med2 = medianOf3(array, start + half + quarter, start + half + quarter + eighth, end - 1);
        return medianOf3(array, new int[] { med0, med1, med2 });
    }

    private int mOMHelper(int[] array, int start, int length) {
        if (length == 1)
            return start;
        int[] meds = new int[3];
        int third = length / 3;
        meds[0] = mOMHelper(array, start, third);
        meds[1] = mOMHelper(array, start + third, third);
        meds[2] = mOMHelper(array, start + 2 * third, third);

        return medianOf3(array, meds);
    }

    private int medianOfMedians(int[] array, int start, int length) {
        if (length == 1)
            return start;
        int[] meds = new int[3];
        int nearPower = (int) Math.pow(3, Math.round(Math.log(length) / Math.log(3)) - 1);
        if (nearPower == length)
            return mOMHelper(array, start, length);
        // uncommon but can happen with numbers slightly smaller than 2*3^k
        // (e.g., 17 < 18 or 47 < 54)
        if (2 * nearPower >= length)
            nearPower /= 3;
        meds[0] = mOMHelper(array, start, nearPower);
        meds[2] = mOMHelper(array, start + length - nearPower, nearPower);
        meds[1] = medianOfMedians(array, start + nearPower, length - 2 * nearPower);

        return medianOf3(array, meds);
    }

    private int equ(int a, int b) {
        return ((a - b) >> 31) + ((b - a) >> 31) + 1;
    }

    protected void stableSegmentReversal(int[] array, int start, int end) {
        if (end - start < 3)
            Writes.swap(array, start, end, 0.75, true, false);
        else
            Writes.reversal(array, start, end, 0.75, true, false);
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (i < end && Reads.compareIndices(array, i, i + 1, 0.5, true) == 0)
                i++;
            right = i;
            if (left != right) {
                if (right - left < 3)
                    Writes.swap(array, left, right, 0.75, true, false);
                else
                    Writes.reversal(array, left, right, 0.75, true, false);
            }
            i++;
        }
    }

    protected void insertTo(int[] array, int a, int b, double sleep) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], sleep, true, false);
        if (a != b)
            Writes.write(array, b, temp, sleep, true, false);
    }

    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        if (a >= m || m >= b)
            return;
        int p0 = a, p1 = m - 1, p2 = m, p3 = b - 1;
        int tmp;
        while (p0 < p1 && p2 < p3) {
            tmp = array[p1];
            Writes.write(array, p1--, array[p0], 0.25, true, false);
            Writes.write(array, p0++, array[p2], 0.25, true, false);
            Writes.write(array, p2++, array[p3], 0.25, true, false);
            Writes.write(array, p3--, tmp, 0.5, true, false);
        }
        while (p0 < p1) {
            tmp = array[p1];
            Writes.write(array, p1--, array[p0], 0.25, true, false);
            Writes.write(array, p0++, array[p3], 0.25, true, false);
            Writes.write(array, p3--, tmp, 0.5, true, false);
        }
        while (p2 < p3) {
            tmp = array[p2];
            Writes.write(array, p2++, array[p3], 0.25, true, false);
            Writes.write(array, p3--, array[p0], 0.25, true, false);
            Writes.write(array, p0++, tmp, 0.5, true, false);
        }
        if (p0 < p3) { // don't count reversals that don't do anything
            if (p3 - p0 >= 3)
                Writes.reversal(array, p0, p3, 0.5, true, false);
            else
                Writes.swap(array, p0, p3, 0.5, true, false);
            Highlights.clearMark(2);
        }
    }

    protected int rightExpSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
            i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        while (a1 < b1) {
            int m = a1 + (b1 - a1) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) < 0)
                b1 = m;
            else
                a1 = m + 1;
        }
        return a1;
    }

    protected int findRun(int[] array, int a, int b) {
        int i = a + 1;
        if (i < b) {
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
        }
        Highlights.clearMark(2);
        return i;
    }

    public void insertSort(int[] array, int a, int b) {
        for (int i = findRun(array, a, b); i < b; i++)
            insertTo(array, i, rightExpSearch(array, a, i, array[i]), 0.5);
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        if (a >= m || m >= b || Reads.compareIndices(array, m - 1, m, 0.0, false) <= 0)
            return;
        int i = m-1, j = b-1, k;

        while (j > i && i >= a) {
            if (Reads.compareValues(array[i], array[j]) > 0) {
                k = this.rightExpSearch(array, a, i, array[j]);
                this.rotate(array, k, i+1, j+1);

                j -= (i+1)-k;
                i = k-1;
            }
            else j--;
        }
    }

    public void laziestStable(int[] array, int a, int b) {
        if (b - a <= 16) {
            insertSort(array, a, b);
            return;
        }
        int s = (int) Math.sqrt(b - a - 1) + 1;
        for (int i = a; i < b; i += s) {
            int j = Math.min(i + s, b);
            insertSort(array, i, j);
            inPlaceMergeBW(array, a, i, j);
        }
    }

    protected int[] partitionEasy(int[] array, int a, int b, int piv) {
        Highlights.clearMark(2);
        int pa = a, pb = a;
        for (int i = a; i < b; i++) {
            int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
            if (cmp < 0) {
                insertTo(array, i, pa++, 0.25);
                pb++;
            } else if (cmp == 0)
                insertTo(array, i, pb++, 0.25);
        }
        Highlights.clearMark(2);
        return new int[] {pa, pb};
    }

    protected int[] partition(int[] array, int a, int b, int piv) {
        int s = (int) Math.sqrt(b - a - 1) + 1;
        int pa = a, pb = a;
        for (int i = a; i < b; i += s) {
            int j = Math.min(i + s, b);
            int[] pr = partitionEasy(array, i, j, piv);
            if (i > a) {
                rotate(array, pb, i, pr[1]);
                rotate(array, pa, pb, pb + pr[0] - i);
            }
            pa += pr[0] - i;
            pb += pr[1] - i;
        }
        return new int[] {pa, pb};
    }

    protected void sortHelper(int[] array, int a, int b, boolean bad) {
        while (b - a > 16) {
            int pIdx;
            if (bad) {
                int n = b - a;
                n -= ~n & 1; // even lengths bad
                pIdx = this.medianOfMedians(array, a, n);
                bad = false;
            } else
                pIdx = this.medianOf9(array, a, b);
            int[] pr = partition(array, a, b, array[pIdx]);
            int lLen = pr[0] - a, rLen = b - pr[1], eqLen = pr[1] - pr[0];
            if (eqLen == b - a)
                return;
            if (rLen == 0) {
                bad = eqLen < lLen / 8;
                b = pr[0];
                continue;
            }
            if (lLen == 0) {
                bad = eqLen < rLen / 8;
                a = pr[1];
                continue;
            }
            if (rLen < lLen) {
                bad = rLen < lLen / 8;
                sortHelper(array, pr[1], b, bad);
                b = pr[0];
            } else {
                bad = lLen < rLen / 8;
                sortHelper(array, a, pr[0], bad);
                a = pr[1];
            }
        }
        insertSort(array, a, b);
    }

    public void quickSort(int[] array, int a, int b) {
        int len = b - a;
        if (len <= 16) {
            insertSort(array, a, b);
            return;
        }
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
        if (balance == 0)
            return;
        if (balance + eq == len - 1) {
            if (eq > 0)
                stableSegmentReversal(array, a, b - 1);
            else if (b - a < 4) {
                Writes.swap(array, a, b - 1, 0.75, true, false);
            } else
                Writes.reversal(array, a, b - 1, 0.75, true, false);
            return;
        }
        int sixth = len / 6;
        if (streaks > len / 20 || balance <= sixth || balance + eq >= len - sixth) {
            laziestStable(array, a, b);
        } else {
            sortHelper(array, a, b, false);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
