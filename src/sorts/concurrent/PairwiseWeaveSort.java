package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class PairwiseWeaveSort extends Sort {
    public PairwiseWeaveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pairwise Weave");
        this.setRunAllSortsName("Pairwise Weave Sort");
        this.setRunSortName("Pairwise Weave Sort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void pairChecks(int[] array, int start, int end, int gap) {
        for (int i = start; i + gap <= end; i += gap) {
            int g = gap;
            while (i + g <= end) g *= 2;
            g /= 2;
            for (; g >= gap; g /= 2) if (Reads.compareIndices(array, i, i + g, 0.25, true) > 0) Writes.swap(array, i, i + g, 0.25, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int gap = 2;
        while (gap <= currentLength / 2) gap *= 2;
        gap /= 2;
        boolean finalized = false;
        while (!finalized) {
            if (gap == 1) finalized = true;
            for (int i = 0; i < gap; i++) pairChecks(array, i, currentLength - 1, gap);
            gap /= 2;
        }
    }
}