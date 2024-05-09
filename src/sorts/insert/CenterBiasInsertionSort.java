package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class CenterBiasInsertionSort extends MadhouseTools {
    public CenterBiasInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Center Bias Insertion");
        this.setRunAllSortsName("Center Bias Insertion Sort");
        this.setRunSortName("Center Bias Insertion Sort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 1; i < currentLength; i++) Writes.insert(array, i, centerBiasSearch(array, 0, i, array[i], false, 5, true), 1, true, false);
    }
}