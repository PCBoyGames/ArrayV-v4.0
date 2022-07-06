package sorts.misc;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV THEN IMPROVED BY PCBOYGAMES
BASED IN PART ON CODE FROM GEEKSFORGEEKS

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class InPlaceSmartSafeStalinSort extends Sort {
    public InPlaceSmartSafeStalinSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("In-Place Smart Safe Stalin");
        this.setRunAllSortsName("In-Place Smart Safe Stalin Sort");
        this.setRunSortName("In-Place Smart Safe Stalinsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    protected int stepDown(int[] array, int end) {
        for (int i = end - 1; i > 0; i--) for (int j = i - 1; j >= 0; j--) if (Reads.compareIndices(array, j, i, 0.125, true) > 0) return i + 1;
        return 0;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int total = currentLength;
        boolean pass = false;
        currentLength = stepDown(array, currentLength);
        while (!pass) {
            pass = true;
            int collection = 0;
            for (int i = 0; i + 1 < currentLength; i++) {
                if (Reads.compareIndices(array, i, i + 1, 0.5, true) > 0) {
                    Writes.insert(array, i + 1, collection, 0, true, false);
                    collection++;
                    Delays.sleep(1);
                    pass = false;
                }
            }
            currentLength--;
            if (currentLength - 2 > 0) {
                int cmp = Reads.compareIndices(array, currentLength - 2, currentLength - 1, 1, true);
                while (cmp == 0 && currentLength - 2 >= 0) {
                    currentLength--;
                    if (currentLength - 2 >= 0) cmp = Reads.compareIndices(array, currentLength - 2, currentLength - 1, 4, true);
                }
            }
            currentLength = stepDown(array, currentLength);
        }
        InsertionSort clean = new InsertionSort(arrayVisualizer);
        clean.customInsertSort(array, 0, total, 10, false);
    }
}