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

and it's also online

good, great

 */

public class ClamberBogoSort extends BogoSorting {
    public ClamberBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Clamber Bogo");
        this.setRunAllSortsName("Clamber Bogo Sort");
        this.setRunSortName("Clamber Bogosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 1; i <= currentLength; i++) {
            while (!isRangeSorted(array, 0, i, true, false)) {
                for (int j = i-1; j > 0; j--) {
                    int r = randInt(0, j);
                    if (Reads.compareIndices(array, r, j, 0.075, true) > 0) Writes.swap(array, r, j, 0.025, true, false);
                }
            }
        }
    }
}
