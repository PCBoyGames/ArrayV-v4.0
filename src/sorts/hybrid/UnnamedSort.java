package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class UnnamedSort extends BogoSorting {
    public UnnamedSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Unnamed");
        this.setRunAllSortsName("Unnamed Sort");
        this.setRunSortName("Unnamed Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int stablereturn(int a) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) : a;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (isArraySorted(array, currentLength)) return;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < currentLength; i++) {
            if (stablereturn(array[i]) < min) min = stablereturn(array[i]);
            if (stablereturn(array[i]) > max) max = stablereturn(array[i]);
        }
        int size = max - min + 1;
        int[] holes = Writes.createExternalArray(size);
        for (int x = 0; x < currentLength; x++) {
            Highlights.markArray(1, x);
            if (holes[stablereturn(array[x]) - min] != 1) {
                Writes.write(holes, stablereturn(array[x]) - min, 1, 0.1, false, true);
            }
        }
        int collected = 0;
        for (int count = 0; count < size; count++) {
            if (holes[count] == 1) {
                for (int i = collected; i < currentLength; i++) {
                    Highlights.markArray(1, i);
                    Highlights.clearMark(2);
                    Delays.sleep(0.1);
                    if (Reads.compareValues(array[i], count + min) == 0) {
                        if (collected != i) Writes.swap(array, collected, i, 0.1, true, false);
                        collected++;
                        break;
                    }
                }
            }
        }
        Writes.deleteExternalArray(holes);
        if (collected < currentLength) {
            for (int i = 0; collected < currentLength; i++) {
                for (int j = collected; j < currentLength; j++) {
                    if (Reads.compareIndices(array, i, j, 0.1, true) == 0) {
                        if (collected != j) Writes.swap(array, collected, j, 0.1, true, false);
                        collected++;
                        break;
                    }
                }
            }
            Highlights.clearAllMarks();
            OptimizedPDMergeSort natural = new OptimizedPDMergeSort(arrayVisualizer);
            natural.runSort(array, currentLength, 0);
        }
    }
}