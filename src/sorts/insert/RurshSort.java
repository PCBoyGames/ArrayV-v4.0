package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;
import utils.IndexedRotations;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class RurshSort extends MadhouseTools {
    public RurshSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Rursh");
        this.setRunAllSortsName("Rursh Sort");
        this.setRunSortName("Rurshsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i = 1, m = 0;
        while (!(i == currentLength)) {
            int r = m + i, l = m - 1;
            while (!(r - l == 1)) {
                if (Reads.compareIndices(array, i, (int) (Math.floor((l + r) / 2) % i), 2, true) < 0) r = (int) Math.floor((l + r) / 2);
                else l = (int) Math.floor((l + r) / 2);
            }
            m += i - r;
            IndexedRotations.adaptable(array, 0, r % i, i, 0.5, true, false);
            i++;
        }
        IndexedRotations.adaptable(array, 0, m, i, 0.5, true, false);
    }
}