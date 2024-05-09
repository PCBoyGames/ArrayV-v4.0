package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class PickySort extends Sort {

    public PickySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Picky");
        this.setRunAllSortsName("Picky Sort");
        this.setRunSortName("Picky Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    // Easy patch to avoid self-reversals and the "reversals can be done in a single
    // swap" notes.
    protected void reverse(int[] array, int a, int b, double sleep, boolean mark, boolean aux) {
        if (b <= a) return;
        if (b - a >= 3) Writes.reversal(array, a, b, sleep, mark, aux);
        else Writes.swap(array, a, b, sleep, mark, aux);
    }
    
    public void customSort(int[] array, int a, int b) {
        for (int i = a; i < b - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < b; j++)
                if (Reads.compareIndices(array, minIdx, j, 0.01, true) > 0) {
                    minIdx = j;
                    Highlights.markArray(3, minIdx);
                }
            reverse(array, i, minIdx, 0.25, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        customSort(array, 0, sortLength);

    }

}
