package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class LuckyCocktailSort extends BogoSorting {
    public LuckyCocktailSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Cocktail");
        this.setRunAllSortsName("Lucky Cocktail Sort");
        this.setRunSortName("Lucky Cocktailsort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(true);
        this.setQuestion("Enter the luck for this sort:", 50);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 100) return 50;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int luck) {
        boolean anyswaps = true;
        while (anyswaps) {
            anyswaps = false;
            for (int i = 0; i < currentLength - 1; i++) {
                if (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) {
                    anyswaps = true;
                    if (randInt(1, 101) <= luck) Writes.swap(array, i, i + 1, 0.01, true, false);
                }
            }
            for (int i = currentLength - 3; i > 0; i--) {
                if (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) {
                    anyswaps = true;
                    if (randInt(1, 101) <= luck) Writes.swap(array, i, i + 1, 0.01, true, false);
                }
            }
        }
    }
}
