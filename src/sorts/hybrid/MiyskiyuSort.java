package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;

/**
 * @author yuji
 *
 */
public final class MiyskiyuSort extends Sort {

    public MiyskiyuSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Miyskiyu");
        setRunAllSortsName("Miyskiyu Sort");
        setRunSortName("Miyskiyusort");
        setCategory("Hybrid Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    protected boolean compSwap(int[] array, int a, int b) {
        if (Reads.compareIndices(array, a, b, 0.25, true) == 1) {
            Writes.swap(array, a, b, 0.25, false, false);
            return true;
        }
        return false;
    }

    public void sort(int[] array, int a, int b) {
        int length = b - a;
        for (int i = 1; i <= length; i++) {
            int j = i, g = 1;
            while (j + g <= length) {
                if (compSwap(array, j - 1, j - 1 + g) && j > 1)
                    j--;
                j++;
                g++;
            }
        }
        InsertionSort ins = new InsertionSort(arrayVisualizer);
        ins.customInsertSort(array, a, b, 0.5, false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
