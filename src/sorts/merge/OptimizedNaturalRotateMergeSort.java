package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES
EXTENDING CODE BY AYAKO-CHAN AND APHITORITE

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedNaturalRotateMergeSort extends MadhouseTools {

    int insertlimit = 8;
    int seglimit = 16;

    public OptimizedNaturalRotateMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Natural Rotate Merge");
        this.setRunAllSortsName("Optimized Natural Rotate Merge Sort");
        this.setRunSortName("Optimized Natural Rotate Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean threeResolves(int x) {
        int y = 1;
        while (true) {
            int z = (int) (Math.pow(2, y) + 1);
            if (z > x) return false;
            else if (z == x) return true;
            y++;
        }
    }

    protected int mergeFindRun(int[] array, int a, int b) {
        int i = stableFindRun(array, a, b, 0.5, true, false);
        int j;
        for (; i < a + seglimit && i < b; i = j) {
            j = stableFindRun(array, i, b, 0.5, true, false);
            grailMergeWithoutBuffer(array, a, i - a, j - i);
        }
        return i;
    }

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = minExponentialSearch(array, m, b, array[a], true, 0.5, true);
            rotateIndexed(array, a, m, i, 1, true, false);
            int t = i - m;
            m = i;
            a += t + 1;
            if (a >= m) break;
            a = minExponentialSearch(array, a, m, array[m], false, 0.5, true);
        }
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        while (b > m && m > a) {
            int i = maxExponentialSearch(array, a, m, array[b - 1], false, 0.5, true);
            rotateIndexed(array, i, m, b, 1, true, false);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a) break;
            b = maxExponentialSearch(array, m, b, array[m - 1], true, 0.5, true);
        }
    }

    protected void merge(int[] array, int a, int m, int b, int d) {
        Writes.recordDepth(d);
        int lenA = m - a, lenB = b - m;
        if (lenA <= insertlimit || lenB <= insertlimit) {
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

    protected void attemptSmall(int[] array, int[] sizes, int runs) {
        int min2 = 0;
        System.err.println(runs);
        for (int i = 0; i + 1 < runs; i++) if (sizes[i] + sizes [i + 1] < sizes[min2] + sizes[min2 + 1]) min2 = i;
        int sum = 0;
        for (int i = 0; i < min2; i++) sum += sizes[i];
        System.err.println(sum + " " + sizes[min2] + " " + sizes[min2 + 1] + " ENDS AT " + (sum + sizes[min2] + sizes[min2 + 1]));
        merge(array, sum, sum + sizes[min2], sum + sizes[min2] + sizes[min2 + 1], 0);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i, j, k;
        int extlen = 1;
        for (int ext = 2 * seglimit; ext < currentLength; ext += 2 * seglimit, extlen++);
        int[] sizes = Writes.createExternalArray(extlen);
        while (true) {
            int l = 0;
            i = mergeFindRun(array, 0, currentLength);
            if (i >= currentLength) break;
            j = mergeFindRun(array, i, currentLength);
            merge(array, 0, i, j, 0);
            Writes.write(sizes, l++, j, 0.1, true, true);
            Highlights.clearMark(2);
            if (j >= currentLength) break;
            k = j;
            while (true) {
                i = mergeFindRun(array, k, currentLength);
                if (i >= currentLength) {
                    int sum = 0;
                    for (int m = 0; m < l; m++) sum += sizes[m];
                    Writes.write(sizes, l++, currentLength - sum, 0.1, true, true);
                    break;
                }
                j = mergeFindRun(array, i, currentLength);
                merge(array, k, i, j, 0);
                int sum = 0;
                for (int m = 0; m < l; m++) sum += sizes[m];
                Writes.write(sizes, l++, j - sum, 0.1, true, true);
                if (j >= currentLength) break;
                k = j;
            }
            attemptSmall(array, sizes, l);
            if (l <= 2) break;
        }
        Writes.deleteExternalArray(sizes);
    }
}