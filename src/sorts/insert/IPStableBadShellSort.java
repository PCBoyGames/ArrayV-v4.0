package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class IPStableBadShellSort extends MadhouseTools {
    public IPStableBadShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("In-Place Stable \"Bad\" Shell");
        this.setRunAllSortsName("In-Place Stable \"Bad\" Shell Sort");
        this.setRunSortName("In-Place Stable \"Bad\" Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void badShellPass(int[] array, int start, int end, int gap) {
        for (int h = start; h + gap < end; h++) {
            int i = h;
            while (Reads.compareIndices(array, i, i + gap, 0.5 / gap, true) > 0) {
                stableSwap(array, i, i + gap, 0.5 / gap, true, false);
                if (i - gap >= start) i -= gap;
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        // You could probably guess what this sequence is since I'm the one writing this code.
        int[] gs = {1, 4, 10, 23, 57, 132, 301, 701, 1636, 3657, 8172, 18235, 40764, 91064, 203519, 454741, 1016156, 2270499, 5073398, 11335582, 25328324, 56518561, 126451290, 282544198, 631315018};
        for (int g = gs.length - 1; g >= 0; g--) if (gs[g] / 1.73 < currentLength) {
            arrayVisualizer.setExtraHeading(" / GAP: " + gs[g]);
            badShellPass(array, 0, currentLength, gs[g]);
        }
    }
}