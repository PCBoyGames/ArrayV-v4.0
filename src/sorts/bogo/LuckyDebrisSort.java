package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class LuckyDebrisSort extends BogoSorting {
    public LuckyDebrisSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Debris");
        this.setRunAllSortsName("Lucky Debris Sort");
        this.setRunSortName("Lucky Debris Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the luck for this sort:", 50);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 100) return 50;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int luck) {
        int start = 0;
        int end = 0;
        boolean anyrev = true;
        while (anyrev) {
            anyrev = false;
            for (int i = 0; i < currentLength - 1; i++) {
                start = i;
                while (Reads.compareIndices(array, i, i + 1, 0.025, true) > 0 && i < currentLength - 1) i++;
                end = i;
                if (start != end) {
                    if (randInt(1, 101) <= luck) {
                        if (end - start < 3) Writes.swap(array, start, end, 0.075, true, false);
                        else Writes.reversal(array, start, end, 0.075, true, false);
                    }
                    anyrev = true;
                }
            }
        }
    }
}