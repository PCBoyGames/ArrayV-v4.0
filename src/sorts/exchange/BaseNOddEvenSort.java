package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class BaseNOddEvenSort extends Sort {
    public BaseNOddEvenSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Base-N Odd-Even");
        this.setRunAllSortsName("Base-N Odd-Even Sort");
        this.setRunSortName("Base-N Odd-Even Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the base for this sort:", 4);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1) return 1;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        int offset = 0;
        boolean anyswaps = false;
        boolean reset = true;
        while (reset) {
            for (int i = offset; i + 1 < currentLength; i += base) if (Reads.compareIndices(array, i, i + 1, 0.025, true) > 0) Writes.swap(array, i, i + 1, 0.075, anyswaps = true, false);
            offset++;
            if (offset > base - 1) {
                offset = 0;
                if (!anyswaps) reset = false;
                anyswaps = false;
            }
        }
    }
}