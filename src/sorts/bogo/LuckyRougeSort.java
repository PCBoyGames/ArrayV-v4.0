package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class LuckyRougeSort extends BogoSorting {
    public LuckyRougeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Rouge");
        this.setRunAllSortsName("Lucky Rouge Sort");
        this.setRunSortName("Lucky Rougesort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4096);
        this.setBogoSort(true);
        this.setQuestion("Enter the luck for this sort:", 50);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 100) return 50;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int luck) {
        int gap = currentLength - 1;
        boolean anyswaps = true;
        while (gap >= 1 && anyswaps) {
            anyswaps = false;
            for (int i = 0; i + gap < currentLength; i++) {
                if (Reads.compareIndices(array, i, i + gap, 1, true) > 0) {
                    anyswaps = true;
                    if (randInt(1, 101) <= luck) Writes.swap(array, i, i + gap, 1, true, false);
                }
            }
            if (!anyswaps && gap != 1) {
                gap--;
                anyswaps = true;
            }
        }
    }
}