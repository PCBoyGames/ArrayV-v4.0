package sorts.misc;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV THEN IMPROVED BY PCBOYGAMES
FROM A VARIANT OF SAFE STALIN SORT BY NAOAN1201
BASED IN PART ON CODE FROM GEEKSFORGEEKS

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SmartSafeStalinSort extends Sort {
    public SmartSafeStalinSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Smart Safe Stalin");
        this.setRunAllSortsName("Smart Safe Stalin Sort");
        this.setRunSortName("Smart Safe Stalinsort");
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
            int getlen = currentLength;
            pass = true;
            int[] collect = Writes.createExternalArray(currentLength);
            int collection = 0;
            for (int i = 0; i + 1 < getlen; ) {
                if (Reads.compareIndices(array, i, i + 1, 0.5, true) > 0) {
                    Writes.write(collect, collection, array[i + 1], 0, false, true);
                    collection++;
                    Writes.arraycopy(array, i + 2, array, i + 1, getlen - i - 1, 0, false, false);
                    array[getlen - 1] = -1;
                    Delays.sleep(1);
                    getlen--;
                    pass = false;
                } else i++;
            }
            if (!pass) {
                Highlights.clearAllMarks();
                for (int i = 0; i < collection; i++) {
                    Writes.arraycopy(array, i, array, i + 1, getlen, 0, false, false);
                    Writes.write(array, i, collect[i], 1, true, false);
                }
            }
            Writes.deleteExternalArray(collect);
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