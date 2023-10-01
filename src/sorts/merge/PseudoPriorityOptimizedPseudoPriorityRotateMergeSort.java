package sorts.merge;

import java.util.ArrayList;

import main.ArrayVisualizer;

/*

CODED FOR ARRAYV BY PCBOYGAMES
EXTENDING CODE BY AYAKO-CHAN AND APHITORITE

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class PseudoPriorityOptimizedPseudoPriorityRotateMergeSort extends PseudoPriorityOptimizedNaturalRotateMergeSort {

    ArrayList<int[]> rotates;

    public PseudoPriorityOptimizedPseudoPriorityRotateMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pseudo-Priority Optimized Pseudo-Priority Rotate Merge");
        this.setRunAllSortsName("Pseudo-Priority Optimized Pseudo-Priority Rotate Merge Sort");
        this.setRunSortName("Pseudo-Priority Optimized Pseudo-Priority Rotate Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void fromSetup(int[] array, int a, int m, int b) {
        int lenA = m - a, lenB = b - m;
        if (a == b) return;
        boolean pass = true;
        if (lenA <= insertlimit || lenB <= insertlimit) {
            if (m - a > b - m) inPlaceMergeBW(array, a, m, b);
            else inPlaceMergeFW(array, a, m, b);
            pass = false;
        }
        if (pass) {
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
                if (a < m1 - (lenB - r1) && m1 - (lenB - r1) < m1) {
                    rotates.add(new int[] {a, m1 - (lenB - r1), m1});
                    Writes.auxWrites++;
                }
                if (m1 < b - r1 && b - r1 < b) {
                    rotates.add(new int[] {m1, b - r1, b});
                    Writes.auxWrites++;
                }
            } else {
                int r1 = 0, r2 = lenA;
                while (r1 < r2) {
                    int ml = r1 + (r2 - r1) / 2;
                    if (Reads.compareValues(array[a + ml], array[m + (c - ml) - 1]) > 0) r2 = ml;
                    else r1 = ml + 1;
                }
                rotateIndexed(array, a + r1, m, m + (c - r1), 1, true, false);
                int m1 = a + c;
                if (a < a + r1 && a + r1 < m1) {
                    rotates.add(new int[] {a, a + r1, m1});
                    Writes.auxWrites++;
                }
                if (m1 < m1 + (lenA - r1) && m1 + (lenA - r1) < b) {
                    rotates.add(new int[] {m1, m1 + (lenA - r1), b});
                    Writes.auxWrites++;
                }
            }
        }
    }

    protected void toSetup(int[] array) {
        while (rotates.size() > 0) {
            int sel = 0;
            int size = rotates.get(0)[2] - rotates.get(0)[0];
            int selSize = size;
            for (int i = 1; i < rotates.size(); i++) {
                size = rotates.get(i)[2] - rotates.get(i)[0];
                Reads.addComparison();
                if (size > selSize) {
                    sel = i;
                    selSize = size;
                }
            }
            int[] a = rotates.get(sel);
            fromSetup(array, a[0], a[1], a[2]);
            while (rotates.contains(a)) {
                rotates.remove(a);
                Writes.auxWrites++;
            }
        }
    }

    protected void merge(int[] array, int a, int m, int b) {
        int extlen2 = 1;
        for (int ext = insertlimit; ext < b - a; ext += insertlimit, extlen2++);
        rotates = new ArrayList<>(extlen2);
        Writes.changeAllocAmount(extlen2);
        rotates.add(new int[] {a, m, b});
        Writes.auxWrites++;
        toSetup(array);
        Writes.changeAllocAmount(-1 * extlen2);
        rotates.clear();
    }

    protected void initPass(int[] array, int start, int end) {
        indexes.add(start);
        Writes.auxWrites++;
        int i = mergeFindRun(array, start, end);
        if (i >= end) return;
        int j = mergeFindRun(array, i, end);
        merge(array, start, i, j);
        if (j >= end) return;
        int k = j;
        while (true) {
            indexes.add(k);
            Writes.auxWrites++;
            i = mergeFindRun(array, k, end);
            if (i >= end) break;
            j = mergeFindRun(array, i, end);
            merge(array, k, i, j);
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
        merge(array, indexes.get(sel), indexes.get(sel) + selLenA, indexes.get(sel) + selLenA + selLenB);
        indexes.remove(indexes.get(sel + 1));
        Writes.auxWrites++;
    }

    public void ppOPPRM(int[] array, int start, int end) {
        int extlen = 1;
        for (int ext = 2 * seglimit; ext < end - start; ext += 2 * seglimit, extlen++);
        indexes = new ArrayList<>(extlen);
        Writes.changeAllocAmount(extlen);
        initPass(array, start, end);
        while (indexes.size() > 1) findSmall(array, end);
        indexes.clear();
        Writes.changeAllocAmount((-1 * extlen));
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        ppOPPRM(array, 0, currentLength);
    }
}