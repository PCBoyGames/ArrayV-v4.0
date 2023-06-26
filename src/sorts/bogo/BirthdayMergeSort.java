package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class BirthdayMergeSort extends BogoSorting {

    int min;
    int max;

    public BirthdayMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Birthday Merge");
        this.setRunAllSortsName("Birthday Merge Sort");
        this.setRunSortName("Birthday Mergesort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4096);
        this.setBogoSort(true);
    }

    protected void method(int[] array, int start, int len) {
        int size = max - min + 1;
        int[] holes = Writes.createExternalArray(size);
        for (int x = start; x < start + len; x++) {
            Highlights.markArray(1, x);
            Writes.write(holes, array[x] - min, holes[array[x] - min] + 1, 1, false, true);
        }
        for (int count = 0, j = start; count < size; count++) {
            for (int i = 0; i < holes[count]; i++, j++) {
                Highlights.markArray(1, j);
                Delays.sleep(1);
                while (count + min != array[j]) Writes.write(array, j, randInt(min, max + 1), 0.1, true, false);
            }
        }
        Writes.deleteExternalArray(holes);
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        min = Integer.MAX_VALUE;
        max = Integer.MIN_VALUE;
        for (int i = 0; i < currentLength; i++) {
            if (array[i] < min) min = array[i];
            if (array[i] > max) max = array[i];
        }
        int index = 0;
        for (int len = 2; len < currentLength; len *= 2) {
            index = 0;
            while (index + len - 1 < currentLength) {
                method(array, index, len);
                index += len;
            }
        }
        method(array, 0, currentLength);
    }
}