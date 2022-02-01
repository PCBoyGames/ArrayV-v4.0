package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class BlueFlameSort extends Sort {
    public BlueFlameSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Blue Flame");
        this.setRunAllSortsName("Blue Flame Sort");
        this.setRunSortName("Blue Flamesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void ignite(int[] array, int i, int j) {
        if(i >= j)
            return;

        if (Reads.compareIndices(array, i, j, 0.05, true) < 0) {
            Writes.swap(array, i, j, 1, true, false);
            ignite(array, i+1, j);
        } else {
            ignite(array, i+1, j-1);
        }
        Writes.reversal(array, i, j, 0, false, false);

        ignite(array, i, j-1);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for(int i=0; i<currentLength; i++)
            this.ignite(array, i, currentLength-1);
    }
}
