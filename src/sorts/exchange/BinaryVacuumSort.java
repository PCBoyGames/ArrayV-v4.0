package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;
final public class BinaryVacuumSort extends BogoSorting {
    public BinaryVacuumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Binary Vacuum");
        this.setRunAllSortsName("Binary Vacuum Sort");
        this.setRunSortName("Binary Vacuum Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private void pull(int[] array, int a, int b, double sleep, boolean rev) {
        int t = a;
        --b;
        while(a < b) {
            int m = a + (b - a + 1) / 2,
                c = Reads.compareValues(array[m], array[t]);
            if(c > 0) {
                Writes.multiSwap(array, t, m, 1, true, false);
                return;
            } else {
                a = m + 1;
            }
        }
        Writes.multiSwap(array, t, b, 1, true, false);
    }
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        while(sortLength > 0) {
            while(!isMaxSorted(array, 0, sortLength))
                pull(array, 0, sortLength, 0, false);
            sortLength--;
        }
    }
}