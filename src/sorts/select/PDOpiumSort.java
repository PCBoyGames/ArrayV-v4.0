package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class PDOpiumSort extends MadhouseTools {
    public PDOpiumSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating Opium");
        this.setRunAllSortsName("Pattern-Defeating Opium Sort");
        this.setRunSortName("Pattern-Defeating Opiumsort");
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
        if (patternDefeat(array, 0, currentLength, false, 0.5, true, false)) return;
        MoreOptimizedOpiumSort opium = new MoreOptimizedOpiumSort(arrayVisualizer);
        opium.opium(array, 0, currentLength, true, true);
    }
}