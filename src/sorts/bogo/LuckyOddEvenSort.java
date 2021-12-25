package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class LuckyOddEvenSort extends BogoSorting {
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
        int i = 0;
        int offset = 0;
        boolean anyswaps = false;
        boolean reset = true;
        while (reset) {
            i = offset;
            while (i + 1 < currentLength) {
                Highlights.markArray(1, i);
                Highlights.markArray(2, i + 1);
                Delays.sleep(0.01);
                if (Reads.compareValues(array[i], array[i + 1]) > 0) {
                    anyswaps = true;
                    if (randInt(1, 101) <= luck) Writes.swap(array, i, i + 1, 0.01, true, false);
                }
                i += 2;
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