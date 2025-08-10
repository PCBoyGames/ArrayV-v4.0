package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class HeadPullQuickSort extends MadhouseTools {
    public HeadPullQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Head Pull Quick");
        this.setRunAllSortsName("Head Pull Quick Sort");
        this.setRunSortName("Head Pull Quicksort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 0;
        while (left < currentLength) {
            for (int right = left; right < currentLength; right++) if (Reads.compareIndices(array, left, right, 0.001, true) > 0) {
                Writes.multiSwap(array, right, 0, 0.001, true, false);
                left++;
            }
            left = minSorted(array, 0, currentLength, 0.001, true);
        }
    }
}