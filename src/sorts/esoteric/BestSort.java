package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

O(1) time!!!

 */

public class BestSort extends Sort {
    public BestSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Best");
        this.setRunAllSortsName("Best Sort");
        this.setRunSortName("Bestsort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void overrideLength(int n) {
        double sleepRatio = Delays.getSleepRatio();
        arrayVisualizer.setCurrentLength(n);
        Delays.setSleepRatio(sleepRatio);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        overrideLength(0);
    }
}
