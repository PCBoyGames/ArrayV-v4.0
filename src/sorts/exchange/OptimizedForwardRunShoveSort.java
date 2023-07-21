package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedForwardRunShoveSort extends MadhouseTools {
    public OptimizedForwardRunShoveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Forward Run Shove");
        this.setRunAllSortsName("Optimized Forward Run Shove Sort");
        this.setRunSortName("Optimized Forward Run Shovesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (patternDefeat(array, 0, currentLength, false, 0.25, true, false)) return;
        int[] expect = maxSortedW(array, 0, currentLength, 0.25, true);
        currentLength = expect[0];
        int expectval = stableReturn(array[expect[1]]);
        int[] grab = minSortedW(array, 0, currentLength, 0.25, true);
        int find = stableReturn(array[grab[1]]);
        for (int i = grab[0]; i < currentLength - 1; i = grab[0]) {
            for (int j = currentLength - 1; j > i; j--) {
                if (Reads.compareIndices(array, i, j, 0.25, true) > 0) {
                    int seg = i;
                    while (Reads.compareIndices(array, seg, seg + 1, 0.25, true) <= 0) seg++;
                    Highlights.clearAllMarks();
                    rotateIndexed(array, i, seg + 1, j + 1, 0.25, true, false);
                    if (j == currentLength - 1 && Reads.compareIndexValue(array, j, expectval, 0.25, true) == 0) {
                        expect = maxSortedW(array, i, currentLength, 0.25, true);
                        currentLength = expect[0];
                        if (expect[1] == -1) return;
                        else expectval = stableReturn(array[expect[1]]);
                        while (j > currentLength) j--;
                    }
                    if (Reads.compareIndexValue(array, i, find, 0.25, true) == 0) break;
                }
            }
            grab = minSortedW(array, i + 1, currentLength, 0.25, true);
            if (grab[1] == -1) return;
            else find = stableReturn(array[grab[1]]);
        }
    }
}