package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class LuckyOddEvenSort extends BogoSorting {
    public LuckyOddEvenSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Odd-Even");
        this.setRunAllSortsName("Lucky Odd-Even Sort");
        this.setRunSortName("Lucky Odd-Even Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
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
        int offset = 0;
        boolean anyswaps = false;
        boolean reset = true;
        while (reset) {
            for (int i = offset; i + 1 < currentLength; i += 2) {
                if (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) {
                    anyswaps = true;
                    if (randInt(1, 101) <= luck) Writes.swap(array, i, i + 1, 0.01, true, false);
                }
            }
            offset++;
            if (offset > 1) {
                offset = 0;
                if (!anyswaps) reset = false;
                anyswaps = false;
            }
        }
    }
}