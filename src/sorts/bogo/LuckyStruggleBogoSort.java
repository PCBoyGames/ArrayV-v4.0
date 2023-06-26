package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

Probably the end of the Struggle Saga.

 */

public final class LuckyStruggleBogoSort extends BogoSorting {
    public LuckyStruggleBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Lucky Struggle Bogo");
        this.setRunAllSortsName("Lucky Struggle Bogo Sort");
        this.setRunSortName("Lucky Struggle Bogosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
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
        while (!this.isArraySorted(array, currentLength)){
            int i = 0;
            if (currentLength < 3) {
                if (Reads.compareIndices(array, i, i + 1, 0.025, true) > 0) {
                    if (randInt(1, 101) <= luck) Writes.swap(array, i, i + 1, 0.075, true, false);
                }
            } else {
                i = BogoSorting.randInt(0, currentLength-2);
                if (Reads.compareIndices(array, i, i + 1, 0.025, true) > 0) {
                    if (randInt(1, 101) <= luck) Writes.swap(array, i, i + 1, 0.075, true, false);
                }
                if (Reads.compareIndices(array, i + 1, i + 2, 0.075, true) > 0) {
                    if (randInt(1, 101) <= luck) Writes.swap(array, i + 1, i + 2, 0.075, true, false);
                }
                if (Reads.compareIndices(array, i, i + 1, 0.025, true) > 0) {
                    if (randInt(1, 101) <= luck) Writes.swap(array, i, i + 1, 0.075, true, false);
                }
            }
        }
    }
}
