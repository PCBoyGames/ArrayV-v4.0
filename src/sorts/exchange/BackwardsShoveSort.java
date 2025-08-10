package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

+---------------------------+
| SORTING ALGORITHM SCARLET |
+---------------------------+
|    A sorting algorithm    |
|    studio by Flanlaina    |
|    (a.k.a Ayako-chan)     |
+---------------------------+

 */

/**
 * @author frankblob
 * @author Flanlaina
 *
 */
public class BackwardsShoveSort extends Sort {
    public BackwardsShoveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        setSortListName("Backwards Shove");
        setRunAllSortsName("Backwards Shove Sort");
        setRunSortName("Backwards Shove Sort");
        setCategory("Impractical Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(true);
        setUnreasonableLimit(512);
        setBogoSort(false);
    }

    private void shovesort(int[] array, int start, int end, double sleep) {
        int i = end - 1;
        while (i > start) {
            if (Reads.compareIndices(array, i, i - 1, sleep, true) < 0) {
                Writes.multiSwap(array, i, start, sleep, true, false);
                if (i < end - 1) i++;
            } else i--;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        shovesort(array, 0, sortLength, 0.125D);
    }
}
