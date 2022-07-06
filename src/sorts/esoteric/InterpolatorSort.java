package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;
public final class InterpolatorSort extends BogoSorting {
    public InterpolatorSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Interpolator");
        this.setRunAllSortsName("Interpolator Sort");
        this.setRunSortName("Interpsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(true);
    }

    public int avg(int a, int b) {
        return (a+b)/2;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while (!this.isArraySorted(array, length)) {
            for (int i = 1; i < length - 1; i+=2) {
                Writes.write(array, i, avg(array[i-1], array[i+1]), 1, false, true);
            }
            for (int i = 2; i < length - 1; i+=2) {
                Writes.write(array, i, avg(array[i-1], array[i+1]), 1, false, true);
            }
        }
    }
}