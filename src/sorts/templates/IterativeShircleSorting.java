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
            for (int start = 0; start + gap < this.end; start += 2 * gap) {
                int high = start + 2 * gap - 1;
                int low = start;
                int pull, item;
                while (low < high) {
					while (high < this.end && Reads.compareIndices(array, low, high, sleep / 2, true) > 0) {
						pull = low;
                        item = array[pull];
                        Highlights.markArray(2, low);
                        Highlights.markArray(3, high);
                        while (pull < high) {
                            Writes.write(array, pull, array[pull + 1], sleep, true, false);
                            pull++;
                        }
                        Highlights.clearMark(3);
                        Writes.write(array, pull, item, sleep, true, false);
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