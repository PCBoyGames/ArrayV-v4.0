package sorts.templates;

import main.ArrayVisualizer;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public abstract class IterativeClericSorting extends Sort {
    protected IterativeClericSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }
    protected int end;
    protected int clericSortRoutine(int[] array, int length, double sleep) {
        int swapCount = 0;
        for (int gap = length / 2; gap > 0; gap /= 2) {
            for (int start = 0; start + gap < this.end; start += 2 * gap) {
                int high = start + 2 * gap - 1;
                int low = start;
                while (low < high) {
                    if (high < end && Reads.compareIndices(array, low, high, sleep / 2, true) > 0) {
                        Highlights.markArray(3, low);
                        Highlights.markArray(4, high);
                        Writes.reversal(array, low, high, sleep, true, false);
                        Highlights.clearAllMarks();
                        swapCount++;
                    }
                    low++;
                    high--;
                }
            }
        }
        return swapCount;
    }
}