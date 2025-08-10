package sorts.distribute;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class CocktailShnexSort extends MadhouseTools {
    public CocktailShnexSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cocktail Shnex");
        this.setRunAllSortsName("Cocktail Shnex Sort");
        this.setRunSortName("Cocktail Shnex Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(32);
        this.setBogoSort(false);
    }

    protected int stablereturn(int a) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) : a;
    }

    // Entirely cheating, like the par function, but this does what it needs to do in O(n) time.
    protected boolean noDupes(int[] array, int len) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < len; i++) {
            if (stablereturn(array[i]) < min) min = stablereturn(array[i]);
            if (stablereturn(array[i]) > max) max = stablereturn(array[i]);
        }
        if (min != 0 || max != len - 1) return false;
        int size = max - min + 1;
        int[] holes = new int[size];
        for (int x = 0; x < len; x++) {
            if (holes[stablereturn(array[x]) - min] == 1) return false;
            else holes[stablereturn(array[x]) - min] = 1;
        }
        return true;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (noDupes(array, currentLength)) {
            int a = 0;
            int b = currentLength - 1;
            double times = 1;
            while (a < b) {
                Highlights.markArray(3, a);
                Highlights.markArray(4, b);
                boolean sent = false;
                while (!sent && a < b) {
                    if (Reads.compareValues(stablereturn(array[a]), a) != 0) {
                        Writes.multiSwap(array, a, stablereturn(array[a]), 1 / times, sent = true, false);
                        times += 0.1;
                    } else {
                        times = 1;
                        a++;
                    }
                    Highlights.markArray(3, a);
                }
                sent = false;
                while (!sent && a < b) {
                    if (Reads.compareValues(stablereturn(array[b]), b) != 0) {
                        Writes.multiSwap(array, b, stablereturn(array[b]), 1 / times, sent = true, false);
                        times += 0.1;
                    } else {
                        times = 1;
                        b--;
                    }
                    Highlights.markArray(4, b);
                }
            }
        } else {
            arrayVisualizer.setExtraHeading(" / Data does not fit! Fallback!");
            double times = 10;
            int a = currentLength / 2 - 1;
            int b = currentLength / 2 + 1;
            while (a >= 0 && b < currentLength) {
                if (b < currentLength) {
                    b++;
                    for (int i = a; i + 1 < b;) {
                        if (Reads.compareIndices(array, i, i + 1, 1 / times, true) > 0) {
                            Writes.multiSwap(array, i + 1, i = a, 1 / times, true, false);
                            times += 0.1;
                        } else i++;
                    }
                }
                if (a >= 0) {
                    a--;
                    for (int i = b - 1; i > a;) {
                        if (Reads.compareIndices(array, i - 1, i, 1 / times, true) > 0) {
                            Writes.multiSwap(array, i - 1, i = b - 1, 1 / times, true, false);
                            times += 0.1;
                        } else i--;
                    }
                }
            }
            arrayVisualizer.setExtraHeading("");
        }
    }
}