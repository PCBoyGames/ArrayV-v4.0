package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

Uh oh.

 */

public final class PowSetSortNetwork extends Sort {

    int mincomp;

    public PowSetSortNetwork(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Power Set Sorting Network");
        this.setRunAllSortsName("Power Set Sorting Network");
        this.setRunSortName("Power Set Sorting Network");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(22);
        this.setBogoSort(false);
    }

    protected int stablereturn(int a) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) : a;
    }

    private void compSwap(int[] array, int a, int b, double delay) {
        if (a != b) {
            if (a > b) {
                compSwap(array, b, a, delay);
                return;
            }
            if (mincomp > a) mincomp = a;
            if (Reads.compareIndices(array, a, b, delay, true) > 0) Writes.swap(array, a, b, delay, true, false);
        }
        Highlights.clearMark(2);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (currentLength < 4) {
            MoreOptimizedBubbleSort mob = new MoreOptimizedBubbleSort(arrayVisualizer);
            mob.runSort(array, currentLength, 0);
            return;
        }
        int[] aux = Writes.createExternalArray(currentLength);
        for (int i = 0; i < currentLength; i++) Writes.write(aux, i, 0, 0.1, true, true);
        mincomp = currentLength - 2;
        boolean finalized = mincomp == 1;
        while (!finalized) {
            boolean loop = true;
            while (loop) {
                boolean goback = false;
                for (int i = currentLength - 1; i > 0; i--) {
                    if (Reads.compareValues(aux[i], currentLength - 1) >= 0) Writes.write(aux, i, 0, 0.1, goback = true, true);
                    else {
                        Writes.write(aux, i, aux[i] + 1, 0.1, true, true);
                        compSwap(array, i - 1, i, 0.05);
                        break;
                    }
                }
                if (goback) {
                    for (int i = 0; i + 1 < currentLength; i++) {
                        if (Reads.compareValues(aux[i], aux[i + 1]) > 0) {
                            while (i + 1 < currentLength) {
                                if (aux[i] + 1 <= currentLength - 1) {
                                    Writes.write(aux, i + 1, aux[i++] + 1, 0.1, true, true);
                                    compSwap(array, i, i++ - 1, 0.05);
                                } else {
                                    Writes.write(aux, i + 1, currentLength, 0.1, true, true);
                                    while (aux[i + 1] - aux[i] == 1) i--;
                                    Writes.write(aux, i, aux[i] + 1, 0.1, true, true);
                                    while (i + 1 < currentLength) {
                                        Writes.write(aux, i + 1, aux[i++] + 1, 0.1, true, true);
                                        compSwap(array, i - 1, i, 0.05);
                                    }
                                }
                            }
                        }
                    }
                }
                if (Reads.compareValues(aux[currentLength - 1], currentLength - 1) <= 0) loop = false;
            }
            finalized = mincomp == 1;
        }
        for (int i = 0; i + 1 < currentLength; i++) compSwap(array, i, i + 1, 0.05);
    }
}
