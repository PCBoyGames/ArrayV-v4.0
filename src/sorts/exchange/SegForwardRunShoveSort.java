package sorts.exchange;

import main.ArrayVisualizer;
import sorts.merge.OptimizedNaturalRotateMergeSort;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SegForwardRunShoveSort extends MadhouseTools {
    public SegForwardRunShoveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Segments of Forward Run Shove");
        this.setRunAllSortsName("Segments of Forward Run Shove Sort");
        this.setRunSortName("Segments of Forward Run Shovesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(16384);
        this.setBogoSort(false);
    }

    protected void doItr(int[] array, int start, int end) {
        for (int j = end - 1; j > start; j--) {
            if (Reads.compareIndices(array, start, j, 0.1, true) > 0) {
                int seg = start;
                while (Reads.compareIndices(array, seg, seg + 1, 0.1, true) <= 0) seg++;
                Highlights.clearAllMarks();
                rotateIndexed(array, start, seg + 1, j + 1, 0.1, true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int bound = currentLength;
        if (patternDefeat(array, 0, currentLength, false, 0.1, true, false)) return;
        int[] expect = maxSortedW(array, 0, currentLength, 0.1, true);
        currentLength = expect[0];
        int expectval = stableReturn(array[expect[1]]);
        for (int i = 0; i < currentLength - 1;) {
            doItr(array, i, currentLength);
            i++;
            while (i < currentLength - 1 && Reads.compareIndices(array, i - 1, i, 0.1, true) <= 0) i++;
            if (Reads.compareIndexValue(array, currentLength - 1, expectval, 0.1, true) == 0 || i > expect[1]) {
                expect = maxSortedW(array, i, currentLength, 0.25, true);
                currentLength = expect[0];
                if (expect[1] == -1) break;
                else expectval = stableReturn(array[expect[1]]);
            }
        }
        OptimizedNaturalRotateMergeSort merge = new OptimizedNaturalRotateMergeSort(arrayVisualizer);
        merge.runSort(array, bound, 0);
    }
}