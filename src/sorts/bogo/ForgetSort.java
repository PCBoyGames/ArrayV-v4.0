package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class ForgetSort extends BogoSorting {
    public ForgetSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Forget");
        this.setRunAllSortsName("Forget Sort");
        this.setRunSortName("Forgetsort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the luck for this sort:", 95);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 100) return 95;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int luck) {
        delay = 0.05;
        boolean docase = true;
        while (docase) {
            boolean changes = false;
            int dir = randInt(1, 101) <= luck ? 1 : -1;
            for (int i = randInt(0, currentLength - 1); (i >= 0 && i + 1 < currentLength) || !changes; i += dir) {
                if (Reads.compareIndices(array, i, i + 1, delay, true) == dir) Writes.swap(array, i, i + 1, delay, changes = true, false);
                else break;
            }
            if (changes) docase = !isArraySorted(array, currentLength);
        }
    }
}