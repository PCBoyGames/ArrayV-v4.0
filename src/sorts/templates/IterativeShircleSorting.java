package sorts.templates;

import main.ArrayVisualizer;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public abstract class IterativeShircleSorting extends Sort {
    protected IterativeShircleSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected int end;

    protected int shircleSortRoutine(int[] array, int length, double sleep) {
        int swapCount = 0;
        for (int gap = length / 2; gap > 0; gap /= 2) {
            for (int start = 0; start + gap < end; start += 2 * gap) {
                for (int high = start + 2 * gap - 1, low = start; low < high; low++, high--) {
                    while (high < end && Reads.compareIndices(array, low, high, sleep / 2, true) > 0) {
                        Highlights.markArray(2, low);
                        Highlights.markArray(3, high);
                        Writes.insert(array, low, high, sleep, true, false);
                        Highlights.clearMark(3);
                        swapCount++;
                    }
                }
            }
        }
        return swapCount;
    }
}