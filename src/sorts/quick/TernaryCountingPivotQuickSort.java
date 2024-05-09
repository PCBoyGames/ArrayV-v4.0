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
public class TernaryCountingPivotQuickSort extends MadhouseTools {
    public TernaryCountingPivotQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Ternary Counting Pivot Quick");
        this.setRunAllSortsName("Ternary Counting Pivot Quick Sort");
        this.setRunSortName("Ternary Counting Pivot Quicksort");
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
            if (cnt >= (end - start) / 2 || times.size() == 3) {
                // The pivot will be ternary this time, so decrementing is not exactly ideal this time.
                int result = values.get(pos);
                // However, if there are three uniques, always select the middle one to partition.
                // That immediately sorts the entire range, no matter the distribution.
                if (times.size() == 3) result = values.get(1);
                Writes.allocAmount -= 2 * times.size();
                values.clear();
                times.clear();
                return result;
            } else pos++;
        }
    }

    protected int[] partition(int[] array, int a, int b, int piv) {
        int i1 = a, i = a-1, j = b, j1 = b;
        for (;;) {
            while (++i < j) {
                int cmp = Reads.compareIndexValue(array, i, piv, 1, true);
                if (cmp == 0) swap(array, i1++, i, 1, true, false);
                else if (cmp > 0) break;
            }
            Highlights.clearMark(2);
            while (--j > i) {
                int cmp = Reads.compareIndexValue(array, j, piv, 1, true);
                if (cmp == 0) swap(array, --j1, j, 1, true, false);
                else if (cmp < 0) break;
            }
            Highlights.clearMark(2);
            if (i < j) Writes.swap(array, i, j, 1, true, false);
            else {
                if (i1 == b) return new int[] {a, b};
                else if (j < i) j++;
                while (i1 > a) swap(array, --i, --i1, 1, true, false);
                while (j1 < b) swap(array, j++, j1++, 1, true, false);
                break;
            }
        }
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
        int pivot = getPivot(array, start, end);
        int[] result = partition(array, start, end, pivot);
        Highlights.clearAllMarks();
        Writes.recursion();
        countingPivotQuick(array, start, result[0], depth + 1);
        Highlights.clearAllMarks();
        Writes.recursion();
        countingPivotQuick(array, result[1], end, depth + 1);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        countingPivotQuick(array, 0, currentLength, 0);
    }
}