package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;
import utils.Statistics;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SegmentedSort extends Sort {
    public SegmentedSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Segmented");
        this.setRunAllSortsName("Segmented Sort");
        this.setRunSortName("Segmented Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean sortSegments(int[] array, int start, int end) {
        boolean anyswaps = false;
        for (int i = start; i < end; i++) {
            int a = i;
            while (Reads.compareIndices(array, i, i + 1, 0, true) <= 0 && i + 1 < end) i++;
            int b1 = i;
            int b2 = i + 1;
            if (i + 1 < end) {
                if (Reads.compareIndices(array, a, b2, 0, true) > 0) {
                    i++;
                    while (Reads.compareIndices(array, i, i + 1, 0, true) <= 0 && i + 1 < end) i++;
                    int c1 = i;
                    int c2 = i + 1;
                    Highlights.clearMark(2);
                    Statistics.addStat("Rotation");
                    if ((b2 - a) % (c1 - b1) == 0 || (c1 - b1) % (b2 - a) == 0) IndexedRotations.holyGriesMills(array, a, b2, c2, 0.1, true, false);
                    else IndexedRotations.cycleReverse(array, a, b2, c2, 0.1, true, false);
                    i = a - 1;
                    anyswaps = true;
                } else i = b1;
            }
        }
        return anyswaps;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        Statistics.putStat("Rotation");
        while (sortSegments(array, 0, currentLength));
        for (int start = 1; start < currentLength; start++) {
            Highlights.markArray(3, start);
            sortSegments(array, start, currentLength);
        }
    }
}