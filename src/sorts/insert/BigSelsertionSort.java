package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class BigSelsertionSort extends MadhouseTools {
    public BigSelsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Big Selsertion");
        this.setRunAllSortsName("Big Selsertion Sort");
        this.setRunSortName("Big Selsertion Sort");
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
        arrayVisualizer.setExtraHeading(" / bSize = " + blockSize);
        if (patternDefeat(array, 0, currentLength, false, 1, true, false)) return;
        SelsertionSort Selsert = new SelsertionSort(arrayVisualizer);
        for (int i = 0; i < currentLength; i += blockSize) Selsert.selsert(array, i, Math.min(i + blockSize, currentLength), false);
        for (int i = blockSize; i < currentLength; i += blockSize) Selsert.insert(array, 0, i, Math.min(i + blockSize, currentLength), false);
    }
}