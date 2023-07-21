package sorts.merge;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES
EXTENDING CODE BY AYAKO-CHAN AND APHITORITE

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class PseudoPriorityOptimizedNaturalRotateMergeSort extends MadhouseTools {

    int insertlimit = 8;
    int seglimit = 16;
    ArrayList<Integer> indexes;

    public PseudoPriorityOptimizedNaturalRotateMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pseudo-Priority Optimized Natural Rotate Merge");
        this.setRunAllSortsName("Pseudo-Priority Optimized Natural Rotate Merge Sort");
        this.setRunSortName("Pseudo-Priority Optimized Natural Rotate Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
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

    protected void initPass(int[] array, int start, int end) {
        indexes.add(start);
        Writes.auxWrites++;
        int i = mergeFindRun(array, start, end);
        if (i >= end) return;
        int j = mergeFindRun(array, i, end);
        merge(array, start, i, j, 0);
        if (j >= end) return;
        int k = j;
        while (true) {
            indexes.add(k);
            Writes.auxWrites++;
            i = mergeFindRun(array, k, end);
            if (i >= end) break;
            j = mergeFindRun(array, i, end);
            merge(array, k, i, j, 0);
            if (j >= end) break;
            k = j;
        }
    }

    protected void findSmall(int[] array, int end) {
        int sel = 0;
        int size1 = indexes.get(1) - indexes.get(0);
        int size2;
        if (indexes.size() == 2) size2 = end - indexes.get(1);
        else size2 = indexes.get(2) - indexes.get(1);
        int selLenA = size1;
        int selLenB = size2;
        for (int i = 1; i + 1 < indexes.size(); i++) {
            size1 = indexes.get(i + 1) - indexes.get(i);
            if (indexes.size() == i + 2) size2 = end - indexes.get(i + 1);
            else size2 = indexes.get(i + 2) - indexes.get(i + 1);
            Reads.addComparison();
            if (size1 + size2 < selLenA + selLenB) {
                sel = i;
                selLenA = size1;
                selLenB = size2;
            }
        }
        merge(array, indexes.get(sel), indexes.get(sel) + selLenA, indexes.get(sel) + selLenA + selLenB, 0);
        indexes.remove(indexes.get(sel + 1));
        Writes.auxWrites++;
    }

    public void ppONRM(int[] array, int start, int end) {
        int extlen = 1;
        for (int ext = 2 * seglimit; ext < end - start; ext += 2 * seglimit, extlen++);
        indexes = new ArrayList<>(extlen);
        Writes.changeAllocAmount(extlen);
        initPass(array, start, end);
        System.err.println(indexes);
        while (indexes.size() > 1) findSmall(array, end);
        indexes.clear();
        Writes.changeAllocAmount(-1 * extlen);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        ppONRM(array, 0, currentLength);
    }
}