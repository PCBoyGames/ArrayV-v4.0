package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class SelectionHeadPullSort extends MadhouseTools {
    public SelectionHeadPullSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Selection Head Pull");
        this.setRunAllSortsName("Selection Head Pull Sort");
        this.setRunSortName("Selection Head Pull Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(32);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int[] guard = minSortedW(array, 0, currentLength, 0.01, true);
        while (guard[1] != -1) {
            Writes.multiSwap(array, guard[1], 0, 0.01, true, false);
            guard = minSortedW(array, 0, currentLength, 0.01, true);
        }
    }
}