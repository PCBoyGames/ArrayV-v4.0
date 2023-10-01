package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class RectangleSelectionSort extends Sort {
    public RectangleSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Rectangle Selection");
        this.setRunAllSortsName("Rectangle Selection Sort");
        this.setRunSortName("Rectangle Selection Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int width = (int) Math.sqrt(currentLength);
        while (currentLength % width != 0) width++;
        for (int i = 0; i < currentLength; i++) {
            for (int x = i % width, j = i - x; j < currentLength; j += width) {
                int start = j;
                int sel = j < i ? j + x : j;
                for (int k = sel + 1; k < j + width; k++) if (Reads.compareIndices(array, sel, k, 0.1, true) > 0) sel = k;
                if (sel % width != x) Writes.swap(array, sel, start + x, 0.1, true, false);
            }
            int sel = i;
            for (int j = i + width; j < currentLength; j += width) if (Reads.compareIndices(array, sel, j, 0.1, true) > 0) sel = j;
            if (sel != i) Writes.swap(array, sel, i, 0.1, true, false);
        }
    }
}