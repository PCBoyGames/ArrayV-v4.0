package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class NaturalHeadPullSort extends Sort {
    public NaturalHeadPullSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Natural Head Pull");
        this.setRunAllSortsName("Natural Head Pull Sort");
        this.setRunSortName("Natural Head Pull Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i = 1;
        int pull = 1;
        int verifyi = 1;
        boolean currentswap = true;
        boolean verifypass = false;
        while (!verifypass) {
            i = verifyi;
            currentswap = true;
            while (i < currentLength && currentswap) {
                Highlights.markArray(1, i - 1);
                Highlights.markArray(2, i);
                Delays.sleep(0.01);
                if (Reads.compareValues(array[i - 1], array[i]) > 0) {
                    pull = i;
                    while (pull >= 1) {
                        Writes.swap(array, pull - 1, pull, 0.01, true, false);
                        pull--;
                    }
                    i++;
                } else currentswap = false;
            }
            verifyi = 1;
            verifypass = true;
            while (verifyi < currentLength && verifypass) {
                Highlights.markArray(1, verifyi - 1);
                Highlights.markArray(2, verifyi);
                Delays.sleep(0.01);
                if (Reads.compareValues(array[verifyi - 1], array[verifyi]) <= 0) verifyi++;
                else verifypass = false;
            }
        }
    }
}