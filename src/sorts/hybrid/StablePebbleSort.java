package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.merge.NaturalRotateMergeSort;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class StablePebbleSort extends Sort {
    public StablePebbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stable Pebble");
        this.setRunAllSortsName("Stable Pebble Sort");
        this.setRunSortName("Stable Pebble Sort");
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
            if (holes[stablereturn(array[x]) - min] != 1) Writes.write(holes, stablereturn(array[x]) - min, 1, 0.05, false, true);
        }
        int collected = 0;
        for (int count = 0; count < size; count++) {
            if (holes[count] == 1) {
                for (int i = collected; i < currentLength; i++) {
                    Highlights.markArray(1, i);
                    Highlights.clearMark(2);
                    Delays.sleep(0.05);
                    if (Reads.compareValues(stablereturn(array[i]), count + min) == 0) {
                        if (collected != i) Writes.insert(array, i, collected, 0.05, true, false);
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
                    if (Reads.compareIndices(array, i, j, 0.05, true) == 0) {
                        if (collected != j) Writes.insert(array, j, collected, 0.05, true, false);
                        collected++;
                        break;
                    }
                }
            }
            Highlights.clearAllMarks();
            NaturalRotateMergeSort natural = new NaturalRotateMergeSort(arrayVisualizer);
            natural.runSort(array, currentLength, 0);
        }
    }
}