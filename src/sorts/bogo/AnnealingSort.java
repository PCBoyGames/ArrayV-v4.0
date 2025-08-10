package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;


/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- IMAGINE MAKING C++ IN JAVA -
------------------------------

*/
public class AnnealingSort extends MadhouseTools {
    public AnnealingSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Annealing");
        this.setRunAllSortsName("Annealing Sort");
        this.setRunSortName("Annealing Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(4096);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (!isSorted(array, 0, currentLength)) {
            for (int i = 0; i < currentLength - 1; i++) {
                for (int k = 0; k < 5; k++) {
                    int r = randInt(i + 1, Math.min(currentLength, i + 11));
                    if (Reads.compareIndices(array, i, r, 0.1, true) > 0) Writes.swap(array, i, r, 0.1, true, false);
                }
            }
            for (int i = currentLength - 1; i > 0; i--) {
                for (int k = 0; k < 5; k++) {
                    int r = randInt(Math.max(0, i - 10), i);
                    if (Reads.compareIndices(array, i, r, 0.1, true) < 0) Writes.swap(array, i, r, 0.1, true, false);
                }
            }
        }
    }
}