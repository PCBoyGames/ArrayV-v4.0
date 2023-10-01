package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class PairwisePoptimalSort extends Sort {
    public PairwisePoptimalSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pairwise Poptimal");
        this.setRunAllSortsName("Pairwise Poptimal Sort");
        this.setRunSortName("Pairwise Poptimal Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void pairChecks(int[] array, int start, int end, int gap, int dir) {
        for (int i = start; i + gap <= end; i += gap) {
            int g = gap;
            while (i + g <= end) g *= 2;
            g /= 2;
            for (; g >= gap; g /= 2) if (Reads.compareIndices(array, i, i + g, 0.25, true) == dir) Writes.swap(array, i, i + g, 0.25, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int gap = 2;
        while (gap <= currentLength / 2) gap *= 2;
        gap /= 2;
        int dir = -1;
        boolean finalized = false;
        while (!finalized) {
            if (gap > 1) dir = -1;
            else finalized = true;
            for (int i = 0; i < gap; i++, dir *= -1) pairChecks(array, i, currentLength - 1, gap, dir);
            gap /= 2;
            if (gap == 1) for (int i = 0, j = currentLength - 2 - (currentLength) % 2; i < j; i += 2, j -= 2) Writes.swap(array, i, j, dir = 1, true, false);
        }
    }
}