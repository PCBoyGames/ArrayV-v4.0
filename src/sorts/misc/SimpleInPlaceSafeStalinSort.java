package sorts.misc;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class SimpleInPlaceSafeStalinSort extends Sort {
    public SimpleInPlaceSafeStalinSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Simple In-Place Safe Stalin");
        this.setRunAllSortsName("Simple In-Place Safe Stalin Sort");
        this.setRunSortName("Simple In-Place Safe Stalinsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public int maxSorted(int[] array, int start, int end, double delay, boolean mark) {
        int a = end - 1;
        int b = end - 1;
        boolean segment = true;
        while (segment) {
            if (b - 1 < start) return start;
            if (Reads.compareIndices(array, b - 1, b, delay, mark) > 0) segment = false;
            else b--;
        }
        int sel = b - 1;
        for (int s = b - 2; s >= start; s--) if (Reads.compareIndices(array, sel, s, delay, mark) < 0) sel = s;
        while (Reads.compareIndices(array, sel, a, delay, mark) <= 0) {
            a--;
            if (a < start) break;
        }
        return a + 1;
    }

    public void sort(int[] array, int a, int b) {
        b = maxSorted(array, a, b, 1, true);
        while (b - a > 1) {
            int moved = 0;
            for (int i = a; i < b - 1; i++)
                if (Reads.compareIndices(array, i, i + 1, 1, true) > 0) 
                    Writes.insert(array, i + 1, a + moved++, 0.5, true, false);
            if (moved == 0) break;
            b = maxSorted(array, a, b, 1, true);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);
    }
}
