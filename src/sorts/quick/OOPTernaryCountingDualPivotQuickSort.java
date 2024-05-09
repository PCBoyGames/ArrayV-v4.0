package sorts.quick;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.insert.PDBinaryInsertionSort;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

Finally, an accurate way to get a near-perfect halving pivot!

*/
public class OOPTernaryCountingDualPivotQuickSort extends MadhouseTools {
    public OOPTernaryCountingDualPivotQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Out of Place Ternary Counting Dual Pivot Quick");
        this.setRunAllSortsName("Out of Place Ternary Counting Dual Pivot Quick Sort");
        this.setRunSortName("Out of Place Ternary Counting Dual Pivot Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int[] getPivots(int[] array, int start, int end) {
        // Counting
        ArrayList<Integer> values = new ArrayList<>();
        ArrayList<Integer> times = new ArrayList<>();
        for (int i = start; i < end; i++) {
            int idx = values.indexOf(stableReturn(array[i]));
            if (idx != -1) {
                int get = times.get(idx);
                times.set(idx, get + 1);
                Writes.auxWrites++;
            } else {
                values.add(stableReturn(array[i]));
                Writes.allocAmount++;
                Writes.auxWrites++;
                times.add(1);
                Writes.allocAmount++;
                Writes.auxWrites++;
            }
            Highlights.markArray(1, i);
            Delays.sleep(1);
        }

        // Shell
        int[] gs = {1, 4, 10, 23, 57, 132, 301, 701, 1636, 3657, 8172, 18235, 40764, 91064, 203519, 454741, 1016156, 2270499, 5073398, 11335582, 25328324, 56518561, 126451290, 282544198, 631315018};
        for (int g = gs.length - 1; g >= 0; g--) {
            while (1.68 * gs[g] >= times.size()) g--;
            int gap = gs[g];
            for (int i = gap; i < times.size(); i++) {
                int j = i;
                int v = values.get(j);
                int t = times.get(j);
                boolean w = false;
                while (j >= gap && Reads.compareValues(v, values.get(j - gap)) < 0) {
                    Highlights.markArray(1, start + j);
                    Highlights.markArray(2, start + j - gap);
                    values.set(j, values.get(j - gap));
                    Writes.auxWrites++;
                    times.set(j, times.get(j - gap));
                    Writes.auxWrites++;
                    j -= gap;
                    w = true;
                    Delays.sleep(1);
                }
                if (w) {
                    Highlights.clearMark(2);
                    Highlights.markArray(1, start + j);
                    values.set(j, v);
                    Writes.auxWrites++;
                    times.set(j, t);
                    Writes.auxWrites++;
                    Delays.sleep(1);
                }
            }
        }

        // Pivot Selection
        int cnt = 0;
        int pos = 0;
        int pivot1 = Integer.MIN_VALUE;
        int pivot2 = Integer.MIN_VALUE;
        while (true) {
            cnt += times.get(pos);
            if (cnt >= (end - start) / 3 && pivot1 == Integer.MIN_VALUE) {
                // The pivots will be ternary this time, so decrementing is not exactly ideal this time.
                pivot1 = values.get(pos);
            }
            if (cnt >= (end - start) / 1.5 && pivot2 == Integer.MIN_VALUE) {
                int result = values.get(pos);
                Writes.allocAmount -= 2 * times.size();
                values.clear();
                times.clear();
                pivot2 = result;
                return new int[] {pivot1, pivot2};
            }
            pos++;
        }
    }

    protected int[] partition(int[] array, int start, int end, int piv1, int piv2) {
        ArrayList<Integer> e1 = new ArrayList<>();
        ArrayList<Integer> g1l2 = new ArrayList<>();
        ArrayList<Integer> e2 = new ArrayList<>();
        ArrayList<Integer> g2 = new ArrayList<>();
        int passed = 0;
        for (int i = start; i < end; i++) {
            int cmp1 = Reads.compareValues(array[i], piv1);
            if (cmp1 < 0) {
                if (i > start + passed) Writes.write(array, start + passed, array[i], 0, false, false);
                passed++;
            } else if (cmp1 == 0) {
                e1.add(array[i]);
                Writes.auxWrites++;
                Writes.allocAmount++;
            } else {
                int cmp2 = Reads.compareValues(array[i], piv2);
                if (cmp2 < 0) {
                    g1l2.add(array[i]);
                    Writes.auxWrites++;
                    Writes.allocAmount++;
                } else if (cmp2 == 0) {
                    e2.add(array[i]);
                    Writes.auxWrites++;
                    Writes.allocAmount++;
                } else {
                    g2.add(array[i]);
                    Writes.auxWrites++;
                    Writes.allocAmount++;
                }
            }
            Highlights.markArray(1, i);
            Highlights.markArray(2, start + passed);
            Delays.sleep(1);
        }
        Highlights.clearAllMarks();
        int[] sized = {e1.size(), e1.size() + g1l2.size(), e1.size() + g1l2.size() + e2.size()};
        for (int i = 0; i < 3; i++) sized[i] += start + passed;
        int point = start + passed;
        for (int i = 0; i < e1.size(); i++) Writes.write(array, point++, e1.get(i), 1, true, false);
        Writes.allocAmount -= e1.size();
        for (int i = 0; i < g1l2.size(); i++) Writes.write(array, point++, g1l2.get(i), 1, true, false);
        Writes.allocAmount -= g1l2.size();
        for (int i = 0; i < e2.size(); i++)  Writes.write(array, point++, e2.get(i), 1, true, false);
        Writes.allocAmount -=  e2.size();
        for (int i = 0; i < g2.size(); i++) Writes.write(array, point++, g2.get(i), 1, true, false);
        Writes.allocAmount -= g2.size();
        e1.clear();
        g1l2.clear();
        e2.clear();
        g2.clear();
        return new int[] {start + passed, sized[0], sized[1], sized[2]};
    }

    public void countingPivotQuick(int[] array, int start, int end, int depth) {
        Writes.recordDepth(depth);
        if (end - start < 16) {
            PDBinaryInsertionSort binsert = new PDBinaryInsertionSort(arrayVisualizer);
            binsert.pdbinsertUnstable(array, start, end, 1, false);
            return;
        }
        if (isSorted(array, start, end)) return;
        int[] pivots = getPivots(array, start, end);
        int[] result = partition(array, start, end, pivots[0], pivots[1]);
        Highlights.clearAllMarks();
        Writes.recursion();
        countingPivotQuick(array, start, result[0], depth + 1);
        Highlights.clearAllMarks();
        if (pivots[1] > pivots[0]) {
            Writes.recursion();
            countingPivotQuick(array, result[1], result[2], depth);
            Highlights.clearAllMarks();
        }
        Writes.recursion();
        countingPivotQuick(array, result[3], end, depth + 1);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        countingPivotQuick(array, 0, currentLength, 0);
    }
}