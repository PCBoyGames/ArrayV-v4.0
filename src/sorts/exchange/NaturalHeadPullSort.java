package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class NaturalHeadPullSort extends Sort {
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
        int verifyi = 1;
        boolean verifypass = false;
        while (!verifypass) {
            int i = verifyi;
            boolean currentswap = true;
            while (i < currentLength && currentswap) {
                if (Reads.compareIndices(array, i - 1, i, 0.01, true) > 0) Writes.multiSwap(array, i++, 0, 0.01, true, false);
                else currentswap = false;
            }
            verifyi = 1;
            verifypass = true;
            while (verifyi < currentLength && verifypass) {
                if (Reads.compareIndices(array, verifyi - 1, verifyi, 0.01, true) <= 0) verifyi++;
                else verifypass = false;
            }
        }
    }
}