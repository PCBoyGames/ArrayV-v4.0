package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class ExchangeBojoSort extends BogoSorting {
    public ExchangeBojoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Exchange Bojo");
        this.setRunAllSortsName("Exchange Bojo Sort");
        this.setRunSortName("Exchange Bojosort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (!this.isArraySorted(array, currentLength)) {
            int i1 = randInt(0, currentLength);
            int i2 = randInt(0, currentLength);
            int temp;
            if (i1 > i2) {
                temp = i1;
                i1 = i2;
                i2 = temp;
            }
            if (Reads.compareIndices(array, i1, i2, delay, true) > 0) {
                Writes.reversal(array, i1, i2, delay, true, false);
            }
        }
    }
}