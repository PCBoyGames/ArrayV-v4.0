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
public class OptimizedPseudoPriorityRotateMergeSort extends OptimizedNaturalRotateMergeSort {

    protected int insertlimit = 8;
    int seglimit = 16;

    protected ArrayList<int[]> rotates;

    public OptimizedPseudoPriorityRotateMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Pseudo-Priority Rotate Merge");
        this.setRunAllSortsName("Optimized Pseudo-Priority Rotate Merge Sort");
        this.setRunSortName("Optimized Pseudo-Priority Rotate Mergesort");
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
        if (a >= m || m >= b) return;
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
            if (selSize > 0) fromSetup(array, a[0], a[1], a[2]);
            while (rotates.contains(a)) {
                rotates.remove(a);
                Writes.auxWrites++;
            }
        }
    }

    protected void merge(int[] array, int a, int m, int b) {
        int extlen2 = 1;
        if (insertlimit <= 0) extlen2 = b - a;
        else for (int ext = insertlimit; ext < b - a; ext += insertlimit, extlen2++);
        rotates = new ArrayList<>(extlen2);
        Writes.changeAllocAmount(extlen2);
        rotates.add(new int[] {a, m, b});
        Writes.auxWrites++;
        toSetup(array);
        Writes.changeAllocAmount(-1 * extlen2);
        rotates.clear();
    }

    protected void attemptSmall(int[] array, int[] sizes, int runs) {
        int min2 = 0;
        for (int i = 0; i + 1 < runs; i++) if (sizes[i] + sizes [i + 1] < sizes[min2] + sizes[min2 + 1]) min2 = i;
        int sum = 0;
        for (int i = 0; i < min2; i++) sum += sizes[i];
        merge(array, sum, sum + sizes[min2], sum + sizes[min2] + sizes[min2 + 1]);
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
            merge(array, 0, i, j);
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
                merge(array, k, i, j);
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