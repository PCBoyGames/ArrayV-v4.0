package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class DeadGnomeSort extends Sort {
    public DeadGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Dead Gnome");
        this.setRunAllSortsName("Dead Gnome (Sort)");
        this.setRunSortName("Dead Gnome(sort)");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void smartGnomeSort(int[] array, int lowerBound, int upperBound, double sleep) {
        int pos = upperBound;
        while (pos > lowerBound && this.Reads.compareValues(array[pos - 1], array[pos]) == 1) {
            this.Writes.swap(array, pos - 1, pos, sleep, true, false);
            pos--;
            smartGnomeSort(array, lowerBound, pos, sleep);
        }
        while (pos > lowerBound) {
            pos--;
            smartGnomeSort(array, lowerBound, pos, sleep);
        }
    }

    public void customSort(int[] array, int low, int high, double sleep) {
        for (int i = low + 1; i < high; i++) smartGnomeSort(array, low, i, sleep);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = 1; i < length; i++) smartGnomeSort(array, 0, i, 0.05D);
    }
}
