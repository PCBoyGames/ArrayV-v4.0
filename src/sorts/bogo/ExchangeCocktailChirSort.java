package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Ayako-chan
in collaboration with PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author PCBoy
 *
 */
public class ExchangeCocktailChirSort extends BogoSorting {

    public ExchangeCocktailChirSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Exchange Cocktail Chir");
        this.setRunAllSortsName("Exchange Cocktail Chir Sort");
        this.setRunSortName("Exchange Cocktail Chirsort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(false);
    }

    protected void bogoCompSwap(int[] array, int a, int b) {
        if (BogoSorting.randBoolean()) {
            for (int i = a; i < b; i++) {
                int j = BogoSorting.randInt(i, b);
                if (Reads.compareIndices(array, i, j, this.delay, true) > 0) {
                    Writes.swap(array, i, j, this.delay, true, false);
                }
            }
        } else {
            for (int i = b - 1; i > a; i--) {
                int j = BogoSorting.randInt(a, i);
                if (Reads.compareIndices(array, i, j, this.delay, true) < 0) {
                    Writes.swap(array, i, j, this.delay, true, false);
                }
            }
        }
    }

    protected void circle(int[] array, int left, int right) {
        int a = left;
        int b = right;
        while (a < b) {
            if (Reads.compareIndices(array, a, b, this.delay, true) > 0)
                Writes.swap(array, a, b, this.delay, true, false);
            a++;
            b--;
        }
    }

    public void pull(int[] array, int a, int b) {
        if (a < b) {
            for (int i = a; i < b; i++) {
                if (Reads.compareIndices(array, i, i + 1, this.delay, true) > 0) {
                    Writes.swap(array, i, i + 1, delay, true, false);
                }
            }
        } else {
            for (int i = a; i > b; i--) {
                if (Reads.compareIndices(array, i, i - 1, this.delay, true) < 0) {
                    Writes.swap(array, i, i - 1, delay, true, false);
                }
            }
        }
    }

    // Easy patch to avoid self-reversals and the "reversals can be done in a single
    // swap" notes.
    public void reversal(int[] array, int a, int b, double sleep, boolean mark, boolean aux) {
        if (b <= a) return;
        if (b - a >= 3)
            Writes.reversal(array, a, b, sleep, mark, aux);
        else
            Writes.swap(array, a, b, sleep, mark, aux);
    }

    public void sort(int[] array, int a, int b) {
        int min = a, max = b;
        while (min < max-1) {
            if (this.isRangeSorted(array, min, max, false, true))
                break;
            if (this.isMinSorted(array, min, max)) {
                Highlights.markArray(3, min);
                ++min;
                continue;
            }
            if (this.isMaxSorted(array, min, max)) {
                Highlights.markArray(4, max-1);
                --max;
                continue;
            }
            int choice = BogoSorting.randInt(0, 10);
            // Exchange Bogo
            if (choice == 0)
                bogoCompSwap(array, min, max);
            // Exchange Bozo
            if (choice == 1) {
                int i1 = BogoSorting.randInt(min, max), i2 = BogoSorting.randInt(min, max);
                int comp = Reads.compareIndices(array, i1, i2, this.delay, true);
                if (i1 < i2 ? comp > 0 : comp < 0)
                    Writes.swap(array, i1, i2, this.delay, true, false);
            }
            // Bubble Bogo
            if (choice == 2) {
                int i = BogoSorting.randInt(min, max - 1);
                if (Reads.compareIndices(array, i, i + 1, this.delay, true) > 0)
                    Writes.swap(array, i, i + 1, this.delay, true, false);
            }
            // Exchange Bojo
            if (choice == 3) {
                int i1 = BogoSorting.randInt(min, max), i2 = BogoSorting.randInt(min, max);
                if (i1 > i2) {
                    int temp = i1;
                    i1 = i2;
                    i2 = temp;
                }
                if (Reads.compareIndices(array, i1, i2, this.delay, true) > 0)
                    reversal(array, i1, i2, this.delay, true, false);
            }
            // Exchange Bovo
            if (choice == 4)
                pull(array, BogoSorting.randInt(min, max), min);
            // Exchange Vogo
            if (choice == 5)
                pull(array, BogoSorting.randInt(min, max), max - 1);
            // Exchange Bomo
            if (choice == 6)
                pull(array, BogoSorting.randInt(min, max), BogoSorting.randInt(min, max));
            // Exchange Slice Bogo
            if (choice == 7) {
                int i1 = BogoSorting.randInt(min, max), i2 = BogoSorting.randInt(min, max);
                if (i1 > i2) {
                    int temp = i1;
                    i1 = i2;
                    i2 = temp;
                }
                bogoCompSwap(array, i1, i2 + 1);
            }
            // Circle Bogo
            if (choice == 8) {
                int i1 = BogoSorting.randInt(min, max), i2 = BogoSorting.randInt(min, max);
                if (i1 > i2) {
                    int temp = i1;
                    i1 = i2;
                    i2 = temp;
                }
                circle(array, i1, i2);
            }
            // Comb Bogo
            if (choice == 9) {
                int gap = BogoSorting.randInt(1, b - a);
                if (BogoSorting.randBoolean()) {
                    for (int i = a; (gap + i) < b; i++) {
                        if (Reads.compareIndices(array, i, i + gap, this.delay, true) > 0)
                            Writes.swap(array, i, i + gap, this.delay, true, false);
                    }
                } else {
                    for (int i = b - 1; i >= a + gap; i--) {
                        if (Reads.compareIndices(array, i - gap, i, this.delay, true) > 0)
                            Writes.swap(array, i - gap, i, this.delay, true, false);
                    }
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
