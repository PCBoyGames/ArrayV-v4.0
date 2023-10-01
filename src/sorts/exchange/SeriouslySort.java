package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class SeriouslySort extends Sort {
    public SeriouslySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Seriously");
        this.setRunAllSortsName("Seriously Sort");
        this.setRunSortName("Seriously Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(16);
        this.setBogoSort(false);
    }

    protected void seriously(int[] array, int start, int end, int depth) {
        Writes.recordDepth(depth);
        if (end - start < 1) return;
        if (end - start == 1) {
            if (Reads.compareIndices(array, start, end, 0, true) > 0) Writes.swap(array, start, end, 0, true, false);
            return;
        }
        for (int i = start, j = (end - start + 1) / 2; i + j <= end; i++) if (Reads.compareIndices(array, i, i + j, 0, true) == (2 * (depth % 2)) - 1) Writes.swap(array, i, i + j, 0, true, false);
        Writes.recursion();
        seriously(array, start, end - 1, depth + 1);
        Writes.recursion();
        seriously(array, start + 1, end, depth + 1);
        Writes.recursion();
        seriously(array, start, end - 1, depth + 1);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        seriously(array, 0, currentLength - 1, 0);
    }
}
