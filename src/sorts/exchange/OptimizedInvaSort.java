package sorts.exchange;

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

i have no idea why i hadn't thought of this before...

 */

public final class OptimizedInvaSort extends BogoSorting {
    public OptimizedInvaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Inva");
        this.setRunAllSortsName("Optimized Inva Sort");
        this.setRunSortName("Optimized Invasort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int s = 0;
        for (; s+1 < currentLength && Reads.compareIndices(array, s, s+1, 0.25, true) <= 0; s++);
        for (int i = s; i < currentLength; i++) {
            Writes.swap(array, randInt(i, currentLength), i, 0.5, true, false);
            for (int j = i-1; j >= 0 && Reads.compareIndices(array, j, j+1, 0.25, true) > 0; j--)
                Writes.swap(array, j, j+1, 0.25, true, false);
        }
    }
}
