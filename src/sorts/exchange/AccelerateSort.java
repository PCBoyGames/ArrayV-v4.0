package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class AccelerateSort extends Sort {
    public AccelerateSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Accelerate");
        this.setRunAllSortsName("Accelerate Sort");
        this.setRunSortName("Accelerate Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int j = 1;
        while (j < currentLength) {
            int i = j;
            int k = 1;
            while ((j - 1) + (int) Math.pow(2, k) <= currentLength) {
                if (Reads.compareIndices(array, i - 1, ((j - 1) + (int) Math.pow(2, k)) - 1, 0.01, true) > 0) {
                    Writes.insert(array, i - 1, currentLength - 1, 0.01, true, false);
                    k = 1;
                    j = 1;
                    i = j;
                } else {
                    i = (j - 1) + (int) Math.pow(2, k);
                    k++;
                }
            }
            j++;
        }
    }
}
