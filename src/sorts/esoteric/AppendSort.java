package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class AppendSort extends BogoSorting {
    public AppendSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Append");
        this.setRunAllSortsName("Append Sort");
        this.setRunSortName("Appendsort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (currentLength < Math.pow(2, ArrayVisualizer.MAX_LENGTH_POWER)) {
            // Compare is of two values above.
            Reads.addComparison();
            currentLength++;
            arrayVisualizer.setCurrentLength(currentLength);
            Writes.write(array, currentLength - 1, randInt(0, currentLength - 1), 0.1, true, false);
        }
    }
}