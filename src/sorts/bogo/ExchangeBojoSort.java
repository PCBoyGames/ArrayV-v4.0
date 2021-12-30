package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

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
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        delay = 0.001;
        while (!this.isArraySorted(array, currentLength)) {
            boolean noswap = true;
            while (noswap) {
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
                    noswap = false;
                }
            }
        }
    }
}