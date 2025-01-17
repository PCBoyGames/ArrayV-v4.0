package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class FlightSort extends Sort {
    public FlightSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Flight");
        this.setRunAllSortsName("Flight Sort");
        this.setRunSortName("Flight Sort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < currentLength; i++) {
            if (array[i] < min) min = array[i];
            if (array[i] > max) max = array[i];
        }
        int size = max - min + 1;
        int[] holes = Writes.createExternalArray(size);
        for (int x = 0; x < currentLength; x++) {
            Highlights.markArray(2, x);
            Writes.write(holes, array[x] - min, holes[array[x] - min] + 1, 1, true, true);
        }
        Highlights.clearMark(2);
        for (int count = 0, j = 0; count < size; count++) {
            for (int i = 0; i < holes[count]; i++, j++) {
                Highlights.markArray(1, j);
                Delays.sleep(1);
                int diff = (count + min) - array[j];
                if (diff != 0) for (int k = 0; k < Math.abs(diff); k++) Writes.write(array, j, diff < 0 ? array[j] - 1 : array[j] + 1, 0.25, true, false);
            }
        }
        Writes.deleteExternalArray(holes);
    }
}