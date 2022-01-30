package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public final class ExchangeBozoMergeSort extends BogoSorting {

    public ExchangeBozoMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Exchange Bozo Merge");
        this.setRunAllSortsName("Exchange Bozo Merge Sort");
        this.setRunSortName("Exchange Bozo Mergesort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(true);
    }

    protected void exchangeBozoSort(int[] array, int a, int b) {
        while(!this.isRangeSorted(array, a, b, false, true)) {
            int index1 = BogoSorting.randInt(a, b),
                index2 = BogoSorting.randInt(a, b);
            int comp = Reads.compareIndices(array, index1, index2, this.delay, true);
            if (index1 < index2 ? comp > 0 : comp < 0)
                Writes.swap(array, index1, index2, this.delay, true, false);
        }
    }
    
    protected void sort(int[] array, int a, int b) {
        if(b - a < 2)
            return;
        int m = a + (b - a) / 2;
        sort(array, a, m);
        sort(array, m, b);
        exchangeBozoSort(array, a, b);
    }
    
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
