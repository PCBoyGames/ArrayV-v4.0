package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedBaseNOddEvenSort extends Sort {
    public OptimizedBaseNOddEvenSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Base-N Odd-Even");
        this.setRunAllSortsName("Optimized Base-N Odd-Even Sort");
        this.setRunSortName("Optimized Base-N Odd-Even Sort");
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
        int i = 0;
        int run = 0;
        int count = 0;
        while (i + 1 < currentLength && (base == 1 || count < currentLength * (base - 1))) {
            count++;
            if (i > 0) i--;
            while (Reads.compareIndices(array, i, i + 1, 0.025, true) <= 0 && i + 1 < currentLength) i++;
            if (i + 1 < currentLength) Writes.swap(array, i, i + 1, 0.075, true, false);
            run = i + base;
            while (run + 1 < currentLength) {
                if (Reads.compareIndices(array, run, run + 1, 0.025, true) > 0) Writes.swap(array, run, run + 1, 0.075, true, false);
                run += base;
            }
        }
    }
}