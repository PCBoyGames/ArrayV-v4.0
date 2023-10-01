package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class SomedaySort extends BogoSorting {
    public SomedaySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Someday");
        this.setRunAllSortsName("Someday Sort");
        this.setRunSortName("Someday Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (!isArraySorted(array, currentLength)) {
            for (int i = 0; i != 1; i = randInt(Integer.MIN_VALUE, Integer.MAX_VALUE)) {
                try {Thread.sleep(randInt(0, Integer.MAX_VALUE));} catch (InterruptedException e) {}
                arrayVisualizer.updateNow();
            }
            bogoSwap(array, 0, currentLength, false);
        }
    }
}