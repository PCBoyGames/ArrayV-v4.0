package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class LuckyGrateSort extends BogoSorting {
    public LuckyGrateSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Grate");
        this.setRunAllSortsName("Lucky Grate Sort");
        this.setRunSortName("Lucky Gratesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(true);
        this.setQuestion("Enter the luck for this sort:", 50);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 100)
            return 50;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int luck) {
        boolean anyswaps = true;
        while (anyswaps) {
            anyswaps = false;
            for (int i = 0; i < currentLength - 1; i++) {
				for (int j = currentLength - 1; j > i; j--) {
					Highlights.markArray(1, i);
					Highlights.markArray(2, j);
					Delays.sleep(0.01);
					if (Reads.compareValues(array[i], array[j]) > 0) {
						anyswaps = true;
                        if (BogoSorting.randInt(1, 101) <= luck) {
						    Writes.swap(array, i, j, 0.01, true, false);
                        }
                        break;
					}
				}
			}
        }
    }
}
