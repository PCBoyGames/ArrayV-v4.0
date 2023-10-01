package sorts.esoteric;

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

Worse than WorstWorstsort!

 */

public class HopoSort extends BogoSorting {
    public HopoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Hopo");
        this.setRunAllSortsName("Hopo Sort");
        this.setRunSortName("Hoposort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    while (!isArraySorted(array, currentLength)) {
        int i = randInt(1, currentLength);
        int j = randInt(0, i);
        Reads.compareIndices(array, i, j, this.delay, true);
        }
    }
}
