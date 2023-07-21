package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class StrangePushSort extends Sort {
    public StrangePushSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Strange Push");
        this.setRunAllSortsName("Strange Push Sort");
        this.setRunSortName("Strange Pushsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the base for this sort:", 2);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 2) return 2;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        boolean anyswaps = true;
        while (anyswaps) {
            anyswaps = false;
            int i = 1;
            int gap = 1;
            while (i + gap <= currentLength) {
                if (Reads.compareIndices(array, i - 1, (i - 1) + gap, 0.01, true) > 0) {
                    for (int j = 1; j <= gap; j++) Writes.swap(array, i - 1, (i - 1) + j, 0.01, anyswaps = true, false);
                    gap *= base;
                } else i++;
            }
        }
    }
}