package sorts.exchange;

import main.ArrayVisualizer;
import sorts.insert.PDBinaryInsertionSort;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class LootSort extends Sort {
    public LootSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Loot");
        this.setRunAllSortsName("Loot Sort");
        this.setRunSortName("Loot Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int collect = 0;
        int i = 0;
        for (; i + collect + 1 < currentLength; i++) {
            if (i > 0) {
                if (Reads.compareIndices(array, i - 1, i, 0.1, true) < 0) {
                    i--;
                    collect++;
                }
            }
            if (Reads.compareIndices(array, i + collect, i + collect + 1, 0.1, true) <= 0) {
                i--;
                collect++;
            }
            else Writes.insert(array, i + collect + 1, i, 0.1, true, false);
        }
        IndexedRotations.adaptable(array, 0, i, currentLength, 0.1, true, false);
        PDBinaryInsertionSort binsert = new PDBinaryInsertionSort(arrayVisualizer);
        binsert.pdbinsert(array, 0, currentLength, 0.5, false);
    }
}