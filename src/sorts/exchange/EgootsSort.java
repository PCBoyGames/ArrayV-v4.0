package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
 * Note: This sort can also be called "Backwards Stooge Sort"
 */

/**
 * @author Lancewer
 * @author Flanlaina
 *
 */
public class EgootsSort extends Sort {

    public EgootsSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Egoots");
        this.setRunAllSortsName("Egoots Sort");
        this.setRunSortName("Egootssort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }
    
    protected void stoogeSort(int[] array, int a, int b) {
        if(Reads.compareIndices(array, a, b, 0.005, true) > 0)
            Writes.swap(array, a, b, 0.01, true, false);
        if(b - a + 1 >= 3) {
            int t = (b - a + 1) / 3;
            stoogeSort(array, a + t, b);
            stoogeSort(array, a, b - t);
            stoogeSort(array, a + t, b);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        stoogeSort(array, 0, sortLength - 1);

    }

}
