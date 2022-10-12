package sorts.exchange;

import java.util.Random;
import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Kiriko-chan
 * @author yuji
 *
 */
public final class RandomCocktailCombSort extends Sort {

    public RandomCocktailCombSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Random Cocktail Comb");
        this.setRunAllSortsName("Random Cocktail Comb Sort");
        this.setRunSortName("Random Cocktail Combsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        setQuestion("Enter shrink factor (input/100):", 130);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 110) return 130;
        return answer;
    }

    public void combSort(int[] array, int a, int b, double shrink) {
        int length = b - a;
        boolean anyswap = false;
        Random rng = new Random();
        int gap = length;
        while ((gap > 1) || anyswap) {
            if (gap > 1)
                gap = (int) (gap / shrink);
            anyswap = false;
            if (rng.nextBoolean()) {
                for (int i = a; (gap + i) < b; i++) {
                    if (Reads.compareIndices(array, i, i + gap, 0.25d, true) > 0) {
                        Writes.swap(array, i, i + gap, 0.75d, true, false);
                        anyswap = true;
                    }
                }
            } else {
                for (int i = b - 1; i >= a + gap; i--) {
                    if (Reads.compareIndices(array, i - gap, i, 0.25d, true) > 0) {
                        Writes.swap(array, i - gap, i, 0.75d, true, false);
                        anyswap = true;
                    }
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        combSort(array, 0, sortLength, bucketCount / 100.0);

    }

}
