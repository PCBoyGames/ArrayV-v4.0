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
public class CountingDualPivotQuickSort extends MadhouseTools {
    public CountingDualPivotQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Counting Dual Pivot Quick");
        this.setRunAllSortsName("Counting Dual Pivot Quick Sort");
        this.setRunSortName("Counting Dual Pivot Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void swap(int[] array, int a, int b, double pause, boolean mark, boolean auxwrite) {
        if (a != b) Writes.swap(array, a, b, pause, mark, auxwrite);
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
                // If not perfect, determine from the sides what the closest split of uniques is.
                // The highest unique could in theory take value up more than half of the items.
                // So we don't use it if it's not needed.
                // Unaffected by all one unique value (checked alongside range sortedness).
                // This resolves certain side bias, too.
                if (pos > 0 && cnt > (end - start) / 3 && Math.abs(cnt - times.get(pos) - (end - start) / 3) < Math.abs(cnt - (end - start) / 3)) pos--;
                pivot1 = values.get(pos);
            }
            if (cnt >= (end - start) / 1.5 && pivot2 == Integer.MIN_VALUE) {
                if (pos > 0 && cnt > (end - start) / 1.5 && Math.abs(cnt - times.get(pos) - (end - start) / 1.5) < Math.abs(cnt - (end - start) / 1.5)) pos--;
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

    protected int[] partition(int[] array, int a, int b, int piv1, int piv2) {
        int i = a, j = b;
        for (int k = i; k < j; k++) {
            if (Reads.compareIndexValue(array, k, piv1, 1, true) <= 0) swap(array, k, i++, 1, true, false);
            else if (Reads.compareIndexValue(array, k, piv2, 1, true) > 0) {
                do {
                    j--;
                    Highlights.markArray(3, j);
                    Delays.sleep(1);
                } while (k < j && Reads.compareIndexValue(array, j, piv2, 1, false) > 0);
                swap(array, k, j, 1, true, false);
                if (Reads.compareIndexValue(array, k, piv1, 1, true) <= 0) swap(array, k, i++, 1, true, false);
            }
        }
        Highlights.clearAllMarks();
        return new int[] {i, j};
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
            countingPivotQuick(array, result[0], result[1], depth);
            Highlights.clearAllMarks();
        }
        Writes.recursion();
        countingPivotQuick(array, result[1], end, depth + 1);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        countingPivotQuick(array, 0, currentLength, 0);
    }
}