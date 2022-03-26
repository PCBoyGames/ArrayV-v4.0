package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class FlightMergeSort extends Sort {

    int min;
    int max;

    public FlightMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Flight Merge");
        this.setRunAllSortsName("Flight Merge Sort");
        this.setRunSortName("Flight Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void method(int[] array, int start, int len) {
        int size = max - min + 1;
        int[] holes = Writes.createExternalArray(size);
        for(int x = start; x < start + len; x++) {
            Highlights.markArray(1, x);
            Writes.write(holes, array[x] - min, holes[array[x] - min] + 1, 1, false, true);
        }
        int j = start;
        for(int count = 0; count < size; count++) {
            for (int i = 0; i < holes[count]; i++) {
                Highlights.markArray(1, j);
                Delays.sleep(1);
                int diff = (count + min) - array[j];
                if (diff != 0) for (int k = 0; k < Math.abs(diff); k++) Writes.write(array, j, diff < 0 ? array[j] - 1 : array[j] + 1, 0.25, true, false);
                j++;
            }
        }
        Writes.deleteExternalArray(holes);
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        min = Integer.MAX_VALUE;
        max = Integer.MIN_VALUE;
        for(int i = 0; i < currentLength; i++) {
            if(array[i] < min) min = array[i];
            if(array[i] > max) max = array[i];
        }
        int len = 2;
        int index = 0;
        while (len < currentLength) {
            index = 0;
            while (index + len - 1 < currentLength) {
                method(array, index, len);
                index += len;
            }
            len *= 2;
        }
        method(array, 0, currentLength);
    }
}