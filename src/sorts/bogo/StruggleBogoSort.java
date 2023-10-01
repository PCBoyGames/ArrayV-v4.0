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

Back with another sort- HEEEEEEEEEEEELP!

HEEEELP MEEEEEEEEEEEEEEE!

 */

public class StruggleBogoSort extends BogoSorting {
    public StruggleBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Struggle Bogo");
        this.setRunAllSortsName("Struggle Bogo Sort");
        this.setRunSortName("Struggle Bogosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (!this.isArraySorted(array, currentLength)) {
            int i = 0;
            if (currentLength < 3) {
                if (Reads.compareIndices(array, i, i + 1, 0.025, true) > 0)
                Writes.swap(array, i, i + 1, 0.075, true, false);
            } else {
                i = BogoSorting.randInt(0, currentLength-2);
                if (Reads.compareIndices(array, i, i + 1, 0.025, true) > 0)
                Writes.swap(array, i, i + 1, 0.075, true, false);
                if (Reads.compareIndices(array, i + 1, i + 2, 0.075, true) > 0)
                Writes.swap(array, i + 1, i + 2, 0.075, true, false);
                if (Reads.compareIndices(array, i, i + 1, 0.025, true) > 0)
                Writes.swap(array, i, i + 1, 0.075, true, false);
            }
        }
    }
}
