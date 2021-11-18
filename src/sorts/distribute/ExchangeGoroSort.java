package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class ExchangeGoroSort extends BogoSorting {
    
    public ExchangeGoroSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Exchange Goro");
        this.setRunAllSortsName("Exchange Goro Sort");
        this.setRunSortName("Exchange Gorosort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(16384);
        this.setBogoSort(true);
    }
    
    protected void bogoCompSwap(int[] array, int a, int b) {
        for (int i = a; i < b; i++) {
            int j = randInt(i, b);
            if (Reads.compareIndices(array, i, j, delay, true) > 0) Writes.swap(array, i, j, delay, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (!this.isArraySorted(array, currentLength)) {
            int i1 = randInt(0, currentLength + 1);
            int i2 = randInt(0, currentLength + 1);
            int temp;
            if (i1 > i2) {
                temp = i1;
                i1 = i2;
                i2 = temp;
            }
            bogoCompSwap(array, i1, i2);
        }
    }
}