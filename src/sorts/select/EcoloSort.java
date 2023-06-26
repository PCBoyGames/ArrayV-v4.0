package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class EcoloSort extends Sort {
    public EcoloSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Ecolo");
        this.setRunAllSortsName("Ecolo Sort");
        this.setRunSortName("Ecolosort");
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
        for (int left = 1, right = currentLength, way = 1; left <= right; left++, right--, way *= -1) {
            for (int i = way == 1 ? left : right; (way == 1 && i < right) || (way == -1 && i > left); i += way) {
                if (Reads.compareIndices(array, left - 1, i - 1, 0.05, true) > 0) Writes.swap(array, left - 1, i - 1, 0.05, true, false);
                if (Reads.compareIndices(array, i - 1, right - 1, 0.05, true) > 0) Writes.swap(array, i - 1, right - 1, 0.05, true, false);
            }
        }
    }
}