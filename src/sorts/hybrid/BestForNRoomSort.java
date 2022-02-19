package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.BestForNSorting;
import utils.Statistics;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class BestForNRoomSort extends BestForNSorting {
    public BestForNRoomSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Best For N Room");
        this.setRunAllSortsName("Best For N Room Sort");
        this.setRunSortName("Best For N Roomsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the run length for this sort:", 64);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 2) return 2;
        if (answer > 64) return 64;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        Statistics.putStat("Network Use");
        for (int i = currentLength; i >= 0; i -= base - 1) {
            for (int j = 0; j + base <= i; j++) {
                Statistics.addStat("Network Use");
                initNetwork(array, j, base);
            }
        }
        Statistics.addStat("Network Use");
        initNetwork(array, 0, base);
    }
}