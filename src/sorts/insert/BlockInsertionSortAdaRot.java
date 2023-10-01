package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.GrailSorting;
import utils.Rotations;

public class BlockInsertionSortAdaRot extends GrailSorting {
    public BlockInsertionSortAdaRot(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Block Insertion (\"Adaptive\" Rotation Rule)");
        this.setRunAllSortsName("Block Insertion Sort (\"Adaptive\" Rotation Rule)");
        this.setRunSortName("Block Insertsort (\"Adaptive\" Rotation Rule)");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void grailRotate(int[] array, int pos, int lenA, int lenB) {
        Rotations.adaptable(array, pos, lenA, lenB, 1, true, false);
    }

    private int findRun(int[] array, int a, int b) {
        int i = a + 1;
        if (i == b)
            return i;
        if (Reads.compareIndices(array, i - 1, i++, 1, true) == 1) {
            while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) == 1) i++;
            Writes.reversal(array, a, i - 1, 1, true, false);
        }
        else while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;
        Highlights.clearMark(2);
        return i;
    }

    public void insertionSort(int[] array, int a, int b) {
        int i, j, len;
        i = findRun(array, a, b);
        while (i < b) {
            j = findRun(array, i, b);
            len = j - i;
            grailMergeWithoutBuffer(array, a, i - a, len);
            i = j;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        insertionSort(array, 0, currentLength);
    }
}