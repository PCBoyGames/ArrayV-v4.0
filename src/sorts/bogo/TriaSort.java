package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public class TriaSort extends BogoSorting {

    public TriaSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Tria");
        this.setRunAllSortsName("Tria Sort");
        this.setRunSortName("Triasort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void optiGnomeSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++) {
            int j = i;
            while (j > a && Reads.compareIndices(array, j, j - 1, delay, true) < 0) {
                Writes.swap(array, j - 1, j, delay, true, false);
                j--;
            }
        }
    }

    public void sort(int[] array, int a, int b) {
        while (!isRangeSorted(array, a, b, false, true)) {
            int i1 = BogoSorting.randInt(a, b - 1);
            int i2 = BogoSorting.randInt(i1 + 1, b);
            optiGnomeSort(array, i1, i2 + 1);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
