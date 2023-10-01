package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class LuckyGrateSort extends BogoSorting {
    public LuckyGrateSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Grate");
        this.setRunAllSortsName("Lucky Grate Sort");
        this.setRunSortName("Lucky Gratesort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
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
            for (int i = 0; i < currentLength - 1; i++) {
                for (int j = currentLength - 1; j > i; j--) {
                    if (Reads.compareIndices(array, i, j, 0.01, true) > 0) {
                        anyswaps = true;
                        if (randInt(1, 101) <= luck) Writes.swap(array, i, j, 0.01, true, false);
                        break;
                    }
                }
            }
        }
    }
}
