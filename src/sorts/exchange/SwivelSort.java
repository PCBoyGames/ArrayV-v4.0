package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class SwivelSort extends MadhouseTools {
    public SwivelSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Swivel");
        this.setRunAllSortsName("Swivel Sort");
        this.setRunSortName("Swivelsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int start = 0, end = currentLength;
        boolean diff = true;
        while (diff) {
            diff = false;
            for (int i = start, j = (start + end) / 2; i < end && j < end && i < j; i += 2, j++) if (Reads.compareIndices(array, i, j, 0.1, true) > 0) Writes.swap(array, i, j, 0.1, diff = true, false);
            for (int i = (start + end) / 2, j = end - 1; i >= start && j >= start && i < j; i--, j -= 2) if (Reads.compareIndices(array, i, j, 0.1, true) > 0) Writes.swap(array, i, j, 0.1, diff = true, false);
        }
        while (start < end) {
            int laststart = start, lastend = end;
            start = minSorted(array, start, end, 0.1, true);
            end = start != end ? maxSorted(array, start, end, 0.1, true) : end;
            for (; start < end; start = minSorted(array, start, end, 0.1, true), end = start != end ? maxSorted(array, start, end, 0.1, true) : end) {
                if (start == laststart && end == lastend) break;
                laststart = start;
                lastend = end;
                diff = true;
                while (diff) {
                    diff = false;
                    for (int i = start, j = (start + end) / 2; i < end && j < end && i < j; i += 2, j++) if (Reads.compareIndices(array, i, j, 0.1, true) > 0) Writes.swap(array, i, j, 0.1, diff = true, false);
                    for (int i = (start + end) / 2, j = end - 1; i >= start && j >= start && i < j; i--, j -= 2) if (Reads.compareIndices(array, i, j, 0.1, true) > 0) Writes.swap(array, i, j, 0.1, diff = true, false);
                }
            }
            if (start < end) {
                for (int i = start; i + 1 < end; i++) if (Reads.compareIndices(array, i, i + 1, 0.1, true) > 0) Writes.swap(array, i, i + 1, 0.1, true, false);
                for (int i = end - 2; i >= start; i--) if (Reads.compareIndices(array, i, i + 1, 0.1, true) > 0) Writes.swap(array, i, i + 1, 0.1, true, false);
            }
            else return;
        }
    }
}