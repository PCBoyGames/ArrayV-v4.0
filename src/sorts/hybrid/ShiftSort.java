package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class ShiftSort extends MadhouseTools {
    public ShiftSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Shift");
        this.setRunAllSortsName("Shift Sort");
        this.setRunSortName("Shiftsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void shellPass(int[] array, int start, int end, int gap) {
        for (int h = gap, i = h + start; i < end; i++) {
            int v = array[i];
            int j = i;
            boolean w = false;
            Highlights.markArray(1, j);
            Highlights.markArray(2, j - h);
            Delays.sleep(0.1);
            for (; j >= h && j - h >= start && Reads.compareValues(array[j - h], v) == 1; j -= h) {
                Highlights.markArray(2, j - h);
                Writes.write(array, j, array[j - h], 0.1, w = true, false);
            }
            if (w) Writes.write(array, j, v, 0.1, true, false);
        }
        Highlights.clearAllMarks();
    }

    protected int mergeFindRun(int[] array, int a, int b) {
        int i = findRun(array, a, b, 0.1, true, false);
        int j;
        for (; i < a + 16 && i < b; i = j) {
            j = findRun(array, i, b, 0.1, true, false);
            if (i < b) grailMergeWithoutBuffer(array, a, i - a, j - i);
        }
        return i;
    }

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = minExponentialSearch(array, m, b, array[a], true, 0.1, true);
            rotateIndexed(array, a, m, i, 1, true, false);
            int t = i - m;
            m = i;
            a += t + 1;
            if (a >= m) break;
            a = minExponentialSearch(array, a, m, array[m], false, 0.1, true);
        }
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        while (b > m && m > a) {
            int i = maxExponentialSearch(array, a, m, array[b - 1], false, 0.1, true);
            rotateIndexed(array, i, m, b, 1, true, false);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a) break;
            b = maxExponentialSearch(array, m, b, array[m - 1], true, 0.1, true);
        }
    }

    protected void merge(int[] array, int a, int m, int b, int d) {
        Writes.recordDepth(d);
        int lenA = m - a, lenB = b - m;
        if (lenA <= 8 || lenB <= 8) {
            if (m - a > b - m) inPlaceMergeBW(array, a, m, b);
            else inPlaceMergeFW(array, a, m, b);
            return;
        }
        int c = lenA + (lenB - lenA) / 2;
        if (lenB < lenA) {
            int r1 = 0, r2 = lenB;
            while (r1 < r2) {
                int ml = r1 + (r2 - r1) / 2;
                if (Reads.compareValues(array[m - (c - ml)], array[b - ml - 1]) > 0) r2 = ml;
                else r1 = ml + 1;
            }
            rotateIndexed(array, m - (c - r1), m, b - r1, 1, true, false);
            int m1 = b - c;
            Writes.recursion();
            merge(array, m1, b - r1, b, d + 1);
            Writes.recursion();
            merge(array, a, m1 - (lenB - r1), m1, d + 1);
        } else {
            int r1 = 0, r2 = lenA;
            while (r1 < r2) {
                int ml = r1 + (r2 - r1) / 2;
                if (Reads.compareValues(array[a + ml], array[m + (c - ml) - 1]) > 0) r2 = ml;
                else r1 = ml + 1;
            }
            rotateIndexed(array, a + r1, m, m + (c - r1), 1, true, false);
            int m1 = a + c;
            Writes.recursion();
            merge(array, a, a + r1, m1, d + 1);
            Writes.recursion();
            merge(array, m1, m1 + (lenA - r1), b, d + 1);
        }
    }

    protected void block(int[] array, int a, int b) {
        int i, j, k;
        while (true) {
            int l = a;
            int s = 0;
            i = mergeFindRun(array, a, b);
            s++;
            if (i >= b) break;
            j = mergeFindRun(array, i, b);
            s++;
            merge(array, a, i, j, 0);
            Highlights.clearMark(2);
            if (j >= b) break;
            k = j;
            while (true) {
                i = mergeFindRun(array, k, b);
                s++;
                if (i >= b) {
                    merge(array, l, k, i, 0);
                    break;
                }
                j = mergeFindRun(array, i, b);
                s++;
                merge(array, k, i, j, 0);
                if (j >= b) break;
                l = k;
                k = j;
            }
            if (s == 3) break;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int sqrt = (int) Math.sqrt(currentLength);
        int g = sqrt;
        for (; g <= currentLength; g *= 3);
        g /= 3;
        for (; g >= sqrt; g /= 3) shellPass(array, 0, currentLength, g);
        int s = 0;
        for (; s + sqrt < currentLength; s += sqrt) block(array, s, s + sqrt);
        block(array, s, currentLength);
        for (s = minSorted(array, 1, currentLength, 0.01, true), currentLength = maxSorted(array, s, currentLength, 0.01, true); s < currentLength; s = minSorted(array, s, currentLength, 0.01, true), currentLength = maxSorted(array, s, currentLength - 1, 0.01, true)) {
            for (g = s; g + sqrt < currentLength; g += sqrt) block(array, g, g + sqrt);
            block(array, g, currentLength);
            s = minSorted(array, s + 1, currentLength, 0.01, true);
            if (s >= currentLength) break;
            currentLength = maxSorted(array, s, currentLength, 0.01, true);
            for (g = currentLength; g - sqrt >= s; g -= sqrt) block(array, g - sqrt, g);
            block(array, s, g);
        }
    }
}