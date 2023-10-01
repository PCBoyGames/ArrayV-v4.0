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

Stop me.

 */

public class LuckyStruggleSort extends BogoSorting {
    public LuckyStruggleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Lucky Struggle");
        this.setRunAllSortsName("Lucky Struggle Sort");
        this.setRunSortName("Lucky Strugglesort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4096);
        this.setBogoSort(true);
        this.setQuestion("Enter the luck for this sort:", 50);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 100) {
            return 50;
        }
        return answer;
    }

    private void luckyStruggle(int[] array, int a, int b, int luck) {
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            if (b - a + 1 < 3) {
                for (int i = a; i + 1 < b; i++) {
                    if (Reads.compareIndices(array, i, i + 1, 0.025, true) > 0) {
                        sorted = false;
                        if (randInt(1, 101) <= luck) Writes.swap(array, i, i + 1, 0.075, true, false);
                    }
                }
            } else {
                for (int i = a; i + 2 < b; i++) {
                    if (Reads.compareIndices(array, i, i + 1, 0.025, true) > 0) {
                        sorted = false;
                        if (randInt(1, 101) <= luck) Writes.swap(array, i, i + 1, 0.075, true, false);
                    }
                    if (Reads.compareIndices(array, i + 1, i + 2, 0.025, true) > 0) {
                        sorted = false;
                        if (randInt(1, 101) <= luck) Writes.swap(array, i + 1, i + 2, 0.075, true, false);
                    }
                    if (Reads.compareIndices(array, i, i + 1, 0.025, true) > 0) {
                        sorted = false;
                        if (randInt(1, 101) <= luck) Writes.swap(array, i, i + 1, 0.075, true, false);
                    }
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int luck) {
        luckyStruggle(array, 0, currentLength, luck);
    }
}
