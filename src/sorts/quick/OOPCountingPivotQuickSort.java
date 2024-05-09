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
public class OOPCountingPivotQuickSort extends MadhouseTools {
    public OOPCountingPivotQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Out of Place Counting Pivot Quick");
        this.setRunAllSortsName("Out of Place Counting Pivot Quick Sort");
        this.setRunSortName("Out of Place Counting Pivot Quicksort");
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

    protected int partition(int[] array, int start, int end, int pivot) {
        int[] ext = Writes.createExternalArray(end - start);
        int passed = 0;
        int collected = 0;
        for (int i = start; i < end; i++) {
            if (Reads.compareValues(array[i], pivot) <= 0) {
                if (i > start + passed) Writes.write(array, start + passed, array[i], 0, false, false);
                passed++;
            } else Writes.write(ext, collected++, array[i], 0, false, true);
            Highlights.markArray(1, i);
            Highlights.markArray(2, start + passed);
            Delays.sleep(1);
        }
        Highlights.clearAllMarks();
        for (int i = 0; i < collected; i++) Writes.write(array, start + passed + i, ext[i], 1, true, false);
        Writes.deleteExternalArray(ext);
        return start + passed;
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
        int result = partition(array, start, end, pivot);
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