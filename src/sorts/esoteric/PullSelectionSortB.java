package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class PullSelectionSortB extends Sort {
    public PullSelectionSortB(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pull Selection B");
        this.setRunAllSortsName("Pull Selection Sort (Type B)");
        this.setRunSortName("B-Pull Selection Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for(int i=0; i < length; i++) {
            for(int j=i+1; j<length; j++) {
                if(Reads.compareValues(array[j], array[i]) == -1) {
                    Writes.multiSwap(array, j, i, 0.1, true, false);
                }
            }
        }
    }
}
