package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

The more diverse the factors, the worse it gets!

*/
public class FasterbySort extends MadhouseTools {
    public FasterbySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Fasterby");
        this.setRunAllSortsName("Fasterby Sort");
        this.setRunSortName("Fasterby Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean trial(int[] array, int currentLength, int gap, int start) {
        arrayVisualizer.setExtraHeading(" / OFF: " + start + ", GAP: " + gap);
        boolean found = false;
        for (int i = start; i < currentLength; i += gap) {
            int sel = i;
            for (int j = sel + gap; j + gap - start <= currentLength; j += gap) if (Reads.compareIndices(array, sel, j, 0, true) > 0) sel = j;
            if (sel != i) for (int j = 0; j < gap - start; j++) Writes.swap(array, i + j, sel + j, 0.1, found = true, false);
        }
        return found;
    }

    protected boolean tryFasterby(int[] array, int currentLength) {
        for (int i = currentLength / 2; i >= 1; i--) for (int j = 0; j < i; j++) if (currentLength % i == 0 && trial(array, currentLength, i, j)) return true;
        return false;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (tryFasterby(array, currentLength));
    }
}