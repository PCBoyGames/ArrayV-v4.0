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

Sort so bad, it gives you diarrhea.

 */

public class NoroSort extends BogoSorting {
    public NoroSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Noro");
        this.setRunAllSortsName("Noro Sort");
        this.setRunSortName("Norosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int k = 1;
        for (int i = 1; i <= currentLength; i++) {
            while (!isRangeSorted(array, 0, i)) {
                if (i != 1) {
                    k = randInt(1, i);
                    if (Reads.compareIndices(array, k-1, k, this.delay, true) > 0) Writes.swap(array, k-1, k, this.delay, true, false);
                }
            }
        }
    }
}
