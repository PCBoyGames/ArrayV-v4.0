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
        int mi = min;
        int size = max - mi + 1;
        int[] holes = Writes.createExternalArray(size);
        for(int x = start; x < start + len; x++) {
            Highlights.markArray(1, x);
            Writes.write(holes, array[x] - mi, holes[array[x] - mi] + 1, 1, false, true);
        }
        int j = start;
        for(int count = 0; count < size; count++) {
            for (int i = 0; i < holes[count]; i++) {
                Highlights.markArray(1, j);
                Delays.sleep(1);
                while (count + mi != array[j]) Writes.write(array, j, randInt(min, max + 1), 0.1, true, false);
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