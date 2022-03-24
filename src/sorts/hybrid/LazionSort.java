package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BlockInsertionSortNeon;
import sorts.insert.PDBinaryInsertionSort;
import sorts.templates.GrailSorting;
import utils.Rotations;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class LazionSort extends GrailSorting {

    BlockInsertionSortNeon blocksert = new BlockInsertionSortNeon(arrayVisualizer);
    PDBinaryInsertionSort binsert = new PDBinaryInsertionSort(arrayVisualizer);

    public LazionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lazion Stable");
        this.setRunAllSortsName("Lazion Stable Sort");
        this.setRunSortName("Lazion Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the base for this sort:", 2);
    }

    protected int powlte(int value, int base) {
        int val;
        for (val = 1; val <= value; val *= base);
        return val / base;
    }

    protected void grailRotate(int[] array, int pos, int lenA, int lenB) {
        Highlights.clearAllMarks();
        Rotations.neon(array, pos, lenA, lenB, 0.5, true, false);
    }

    protected void merge(int[] array, int start, int end, int base) {
        int blockLen = (end - start) / base;
        for (int i = start; i + blockLen < end; i += blockLen) {
            grailMergeWithoutBuffer(array, start, i - start + blockLen, blockLen);
        }
    }

    protected void nonBn(int[] array, int start, int end) {
        blocksert.insertionSort(array, start, end);
    }

    protected void mergesLen(int[] array, int start, int end, int lengthstart, int base) {
        int len = lengthstart;
        int index = start;
        while (len < end - start) {
            index = start;
            while (index + len <= end) {
                merge(array, index, index + len, base);
                index += len;
            }
            if (index != end) nonBn(array, index, end);
            len *= base;
        }
        if (len == end - start) merge(array, start, end, base);
        else nonBn(array, start, end);
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        int blockLen = powlte((int) Math.sqrt(currentLength), base);
        int i;
        for (i = 0; i + blockLen <= currentLength; i += blockLen) {
            binsert.pdbinsert(array, i, i + blockLen, 0.5, false);
        }
        if (i < currentLength) binsert.pdbinsert(array, i, currentLength, 0.5, false);
        mergesLen(array, 0, currentLength, blockLen, base);
    }
}