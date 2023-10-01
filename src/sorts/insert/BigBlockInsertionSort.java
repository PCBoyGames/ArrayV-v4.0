package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class BigBlockInsertionSort extends Sort {
    public BigBlockInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Big Block Insertion");
        this.setRunAllSortsName("Big Block Insertion Sort");
        this.setRunSortName("Big Block Insertion Sort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the limit for this sort:\n(Default: 16)", 16);
    }

    public int validateAnswer(int answer) {
        if (answer < 1) answer = 1;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int blockSize) {
        PDBinaryInsertionSort firstrun = new PDBinaryInsertionSort(arrayVisualizer);
        int is = firstrun.pd(array, 0, currentLength, 1, false);
        if (is == currentLength - 1) return;
        BlockInsertionSortAdaRot blocksert = new BlockInsertionSortAdaRot(arrayVisualizer);
        for (int i = is - is % blockSize; i < currentLength; i += blockSize) blocksert.insertionSort(array, i, Math.min(i + blockSize, currentLength));
        blocksert.insertionSort(array, 0, currentLength);
    }
}