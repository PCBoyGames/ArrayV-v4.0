package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class LuckyCombSort extends BogoSorting {
    public LuckyCombSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Comb");
        this.setRunAllSortsName("Lucky Comb Sort");
        this.setRunSortName("Lucky Combsort");
        this.setCategory("Impractical Sorts");
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
        int gap = (int) (currentLength / 1.3);
        boolean anyswaps = true;
        while (gap >= 1 && anyswaps) {
            anyswaps = false;
            for (int i = 0; i + gap < currentLength; i++) {
                Highlights.markArray(1, i);
                Highlights.markArray(2, i + gap);
                Delays.sleep(1);
                if (Reads.compareValues(array[i], array[i + gap]) > 0) {
                    anyswaps = true;
                    if (randInt(1, 101) <= luck) Writes.swap(array, i, i + gap, 1, true, false);
                }
            }
            if (!anyswaps && gap != 1) {
                gap /= 1.3;
                anyswaps = true;
            }
        }
    }
}