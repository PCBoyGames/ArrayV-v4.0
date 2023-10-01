package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class NReboundSort extends Sort {
    public NReboundSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bounce-N Rebound");
        this.setRunAllSortsName("Bounce-N Rebound Sort");
        this.setRunSortName("Bounce-N Rebound Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the bounce for this sort:\n0 = Automatic", 0);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 0) return 0;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int times) {
        if (times == 0) times = currentLength / 16 >= 2 ? currentLength / 16 : currentLength / 2;
        boolean setup = false;
        boolean sorted = false;
        for (int dir = 1; !sorted; dir *= -1) {
            int i = dir == 1 || !setup ? 0 : currentLength - 2;
            int bounces = 0;
            setup = true;
            sorted = true;
            for (; i >= 0 && i < currentLength - 1; i += dir) {
                if (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) {
                    Writes.swap(array, i, i + 1, 0.01, true, sorted = false);
                    bounces++;
                    if (bounces == times) {
                        dir *= -1;
                        bounces = 0;
                    }
                }
            }
        }
    }
}