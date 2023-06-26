package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class SelfNetworkSort extends Sort {

    boolean changes = false;

    public SelfNetworkSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Self-Network");
        this.setRunAllSortsName("Self-Network Sort");
        this.setRunSortName("Self-Networksort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void networksort(int[] array, int[] indexnetwork, int length) {
        for (int i = 1; i < length; i += 2) {
            Highlights.markArray(3, i - 1);
            Highlights.markArray(4, i);
            pairsort(array, indexnetwork[i - 1], indexnetwork[i]);
        }
    }

    protected void pairsort(int[] array, int i, int j) {
        if (i > j) {
            int temp = i;
            i = j;
            j = temp;
        }
        if (Reads.compareIndices(array, i, j, 0.1, true) > 0) Writes.swap(array, i, j, 1, changes = true, false);
    }

    protected int bubblepass(int[] array, int currentLength) {
        int c = 1;
        for (int i = 1; i < currentLength; i++) {
            if (Reads.compareIndices(array, i - 1, i, 0.1, true) > 0) {
                Writes.swap(array, i - 1, i, 0.1, true, false);
                c = 1;
            } else c++;
        }
        return c;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int[] indexnetwork = Writes.createExternalArray(length);
        int workinglength = length;
        while (workinglength > 0) {
            changes = false;
            Highlights.clearAllMarks();
            Writes.arraycopy(array, 0, indexnetwork, 0, workinglength, 0, true, true);
            networksort(array, indexnetwork, workinglength);
            Highlights.clearAllMarks();
            if (!changes) {
                workinglength -= bubblepass(array, workinglength);
                Writes.deleteExternalArray(indexnetwork);
                indexnetwork = Writes.createExternalArray(workinglength);
            }
        }
    }
}