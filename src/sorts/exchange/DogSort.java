package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class DogSort extends Sort {

    public DogSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Dog");
        this.setRunAllSortsName("Dog Sort");
        this.setRunSortName("Dogsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(26);
        this.setBogoSort(false);
    }

    protected void sort(int[] array, int a, int b) {
        if(Reads.compareIndices(array, a, b, 0.01, true) > 0)
            Writes.swap(array, a, b, 0.01, true, false);
        if(a < b) {
            sort(array, a, a + (b - a) / 2);
            sort(array, a + (b - a) / 2 + 1, b);
            sort(array, a, b - 1);
            sort(array, a + 1, b);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength - 1);

    }

}
