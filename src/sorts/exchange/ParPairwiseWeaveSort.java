package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class ParPairwiseWeaveSort extends MadhouseTools {

    int par;
    boolean deter;

    public ParPairwiseWeaveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Par(X) Pairwise Weave");
        this.setRunAllSortsName("Par(X) Pairwise Weave Sort");
        this.setRunSortName("Par(X) Pairwise Weave Sort");
        this.setCategory("Exchange Sorts");
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
            while (i + g <= end && g <= par) g *= 2;
            g /= 2;
            for (; g >= gap; g /= 2) if (Reads.compareIndices(array, i, i + g, 0.25, deter = true) > 0) Writes.swap(array, i, i + g, 0.25, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        par = parX(array, 0, currentLength, 0.25, true);
        int gap = 2;
        while (gap <= currentLength / 2 && gap <= par) gap *= 2;
        gap /= 2;
        boolean finalized = false;
        while (!finalized) {
            if (gap == 1) finalized = true;
            deter = false;
            for (int i = 0; i < gap; i++) pairChecks(array, i, currentLength - 1, gap);
            gap /= 2;
            Highlights.clearAllMarks();
            if (!finalized && deter) par = parX(array, 0, currentLength, 0.25, true);
        }
    }
}