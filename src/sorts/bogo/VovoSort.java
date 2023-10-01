package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

"As if learning about UwUntu wasn't enough this week..."

 */

public class VovoSort extends BogoSorting {
    public VovoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Vovo");
        this.setRunAllSortsName("Vovo Sort");
        this.setRunSortName("Vovosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(11);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (!isArraySorted(array, currentLength)) {
            boolean b = randBoolean();
            if (b) {
                Writes.multiSwap(array, randInt(0, currentLength), 0, this.delay, true, false);
            } else {
                Writes.multiSwap(array, randInt(0, currentLength), currentLength - 1, this.delay, true, false);
            }
        }
    }
}
