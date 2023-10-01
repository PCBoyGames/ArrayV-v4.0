package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES
EXTENDING CODE BY MEME MAN AND FUNGAMER2

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class PDRollSort extends MadhouseTools {
    public PDRollSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pattern-Defeating Rolling");
        this.setRunAllSortsName("Pattern-Defeating Rolling Sort");
        this.setRunSortName("Pattern-Defeating Rollsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (patternDefeat(array, 0, currentLength, false, 0.1, true, false)) return;
        int[] min = minSortedW(array, 0, currentLength, 0.01, true);
        for (int i = min[0]; i < currentLength && min[1] != -1; i = min[0]) {
            int diff = min[1] - i;
            Highlights.clearAllMarks();
            for (int r = 0; r < diff; r++) Writes.insert(array, i, currentLength - 1, 0.01, true, false);
            min = minSortedW(array, i + 1, currentLength, 0.1, true);
        }
    }
}
