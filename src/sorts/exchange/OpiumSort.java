package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class OpiumSort extends Sort {
    public OpiumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Opium");
        this.setRunAllSortsName("Opium Sort");
        this.setRunSortName("Opium Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    protected boolean isLeast(int[] array, int[] ext, int index, int extcollect, int length) {
        for (int i = index + 1; i < length - extcollect; i++) if (Reads.compareIndices(array, index, i, 0.01, true) > 0) return false;
        Highlights.clearAllMarks();
        for (int i = 0; i < extcollect; i++) {
            Highlights.markArray(2, i);
            if (Reads.compareIndexValue(array, index, ext[i], 0.01, true) > 0) return false;
        }
        return true;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i = 0;
        int[] collect = Writes.createExternalArray(currentLength);
        for (int j = 0; j < currentLength; j++) collect[j] = -1;
        int collected = 0;
        while (i < currentLength) {
            if (isLeast(array, collect, i, collected, currentLength)) {
                Highlights.clearAllMarks();
                i++;
                if (collected + i >= currentLength) {
                    for (int j = 0; j < collected; j++) {
                        Writes.write(array, i + j, collect[j], 0.01, true, false);
                        collect[j] = -1;
                    }
                    collected = 0;
                }
            } else {
                Highlights.clearAllMarks();
                if (collected + i >= currentLength) {
                    for (int j = 0; j < collected; j++) {
                        Writes.write(array, i + j, collect[j], 0.01, true, false);
                        collect[j] = -1;
                    }
                    collected = 0;
                } else {
                    Writes.write(collect, collected, array[i], 0.01, false, true);
                    Writes.arraycopy(array, i + 1, array, i, currentLength - collected - i - 1, 0.01, true, false);
                    collected++;
                }
            }
        }
        Writes.deleteExternalArray(collect);
    }
}
