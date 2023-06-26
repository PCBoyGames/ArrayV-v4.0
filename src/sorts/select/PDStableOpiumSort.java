package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class PDStableOpiumSort extends MadhouseTools {
    public PDStableOpiumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating Stable Opium");
        this.setRunAllSortsName("Pattern-Defeating Stable Opium Sort");
        this.setRunSortName("Pattern-Defeating Stable Opiumsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(16384);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (patternDefeat(array, 0, currentLength, true, 0.5, true, false)) return;
        Highlights.clearAllMarks();
        StableOpiumSort opium = new StableOpiumSort(arrayVisualizer);
        boolean skip = opium.dupes(array, currentLength);
        MoreOptimizedOpiumSort opium2 = new MoreOptimizedOpiumSort(arrayVisualizer);
        opium2.opium(array, 0, currentLength, !skip, true);
    }
}