package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public class PullSelectionSortC extends BogoSorting {
    public PullSelectionSortC(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pull Selection C");
        this.setRunAllSortsName("Pull Selection Sort (Type C)");
        this.setRunSortName("C-Pull Selection Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while (!isArraySorted(array, length)) {
            for (int i=0; i < length; i++) {
                for (int j=i+1; j<length; j++) {
                    if (Reads.compareValues(array[j], array[i]) == -1) {
                        Writes.multiSwap(array, i, j, 0.1, true, false);
                        break;
                    }
                }
            }
        }
    }
}
