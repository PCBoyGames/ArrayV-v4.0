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
public class IPCountingPivotQuickSort extends MadhouseTools {
    public IPCountingPivotQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("In Place Stable Counting Pivot Quick");
        this.setRunAllSortsName("In Place Stable Counting Pivot Quick Sort");
        this.setRunSortName("In Place Stable Counting Pivot Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int getPivot(int[] array, int start, int end) {
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
        while (true) {
            cnt += times.get(pos);
            if (cnt >= (end - start) / 2) {
                // If not perfect, determine from the sides what the closest split of uniques is.
                // The highest unique could in theory take value up more than half of the items.
                // So we don't use it if it's not needed.
                // Unaffected by all one unique value (checked alongside range sortedness).
                // This resolves certain side bias, too.
                if (pos > 0 && cnt > (end - start) / 2 && Math.abs(cnt - times.get(pos) - (end - start) / 2) < Math.abs(cnt - (end - start) / 2)) pos--;
                int result = values.get(pos);
                Writes.allocAmount -= 2 * times.size();
                values.clear();
                times.clear();
                return result;
            } else pos++;
        }
    }

    protected void rotateIndexed(int[] array, int a, int b, int c) {
        rotateIndexed(array, a, b, c, 1, true, false);
    }

    protected int partition(int[] array, int a, int b, int piv, int depth) {
        Writes.recordDepth(depth);
        if (b - a < 2) {
            if (Reads.compareValues(array[a], piv) <= 0) return a + 1;
            return a;
        }
        int m = a + (b - a) / 2;
        Writes.recursion();
        int l = partition(array, a, m, piv, depth + 1);
        Writes.recursion();
        int r = partition(array, m, b, piv, depth + 1);
        int l1 = l - a, r1 = r - m;
        rotateIndexed(array, a + l1, m, m + r1);
        return a + l1 + r1;
    }

    public void countingPivotQuick(int[] array, int start, int end, int depth) {
        Writes.recordDepth(depth);
        if (end - start < 16) {
            PDBinaryInsertionSort binsert = new PDBinaryInsertionSort(arrayVisualizer);
            binsert.pdbinsertUnstable(array, start, end, 1, false);
            return;
        }
        if (isSorted(array, start, end)) return;
        int pivot = getPivot(array, start, end);
        int result = partition(array, start, end, pivot, 0);
        Highlights.clearAllMarks();
        Writes.recursion();
        countingPivotQuick(array, start, result, depth + 1);
        Highlights.clearAllMarks();
        Writes.recursion();
        countingPivotQuick(array, result, end, depth + 1);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        countingPivotQuick(array, 0, currentLength, 0);
    }
}