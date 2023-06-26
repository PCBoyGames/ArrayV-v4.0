package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class LuckySelectionSort extends BogoSorting {
    public LuckySelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Selection");
        this.setRunAllSortsName("Lucky Selection Sort");
        this.setRunSortName("Lucky Selectionsort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
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
        boolean anyswaps = true;
        while (anyswaps) {
            anyswaps = false;
            for (int i = 0; i < currentLength; i++) {
                Highlights.markArray(3, i);
                int lowestindex = i;
                for (int j = i + 1; j < currentLength; j++) if (Reads.compareIndices(array, j, lowestindex, 0.025, true) == -1) lowestindex = j;
                if (i != lowestindex) {
                    anyswaps = true;
                    if (randInt(1, 101) <= luck) Writes.swap(array, i, lowestindex, 0.025, true, false);
                }
            }
            Highlights.clearAllMarks();
        }
    }
}