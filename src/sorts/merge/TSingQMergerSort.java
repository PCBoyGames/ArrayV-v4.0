package sorts.merge;

import main.ArrayVisualizer;
import sorts.quick.TernarySingularityQuickSort;

public class TSingQMergerSort extends TernarySingularityQuickSort {
    public TSingQMergerSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("In-Place Yukari (TSingQ Merger)");
        this.setRunAllSortsName("In-Place Yukari Sort (TSingQ Merger)");
        this.setRunSortName("In-Place Yukarisort (TSingQ Merger)");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        mergeSort(array, 0, currentLength);
    }
}