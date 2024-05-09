package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Harumi
 * @author McDude_73
 * @author EilrahcF
 *
 */
public class SlopeSort extends Sort {

    public SlopeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Slope");
        setRunAllSortsName("Slope Sort");
        setRunSortName("Slopesort");
        setCategory("Exchange Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }
    
    public void sort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++)
            for (int j = i; j > a; j--)
                if (Reads.compareIndices(array, j, j - 1, 0.04, true) < 0)
                    Writes.swap(array, j - 1, j, 0.02, true, false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        /*
        for (int i = 1, j = 1; i < length; i++, j++) {
            for (int k = i - 1; k >= 0; k--, i--) {
                if (this.Reads.compareIndices(array, i, k, 0.04D, true) < 0) {
                    this.Writes.swap(array, i, k, 0.02D, true, false);
                }
            }
            i = j;
        }
        */
        sort(array, 0, length);

    }

}
