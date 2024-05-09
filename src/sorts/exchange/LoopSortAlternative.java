package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.GrailSorting;
import utils.Rotations;
import utils.IndexedRotations;

/*

Coded for ArrayV by Harumi
extending code by Meme Man and PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Harumi
 * @author Meme Man
 * @author PCBoy
 *
 */
public class LoopSortAlternative extends GrailSorting {

    public LoopSortAlternative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Loop (Alternative)");
        this.setRunAllSortsName("Alternative Loop Sort");
        this.setRunSortName("Alternative Loop Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    protected void grailRotate(int[] array, int pos, int lenA, int lenB) {
        Rotations.adaptable(array, pos, lenA, lenB, 0.5, true, false);
    }
    
    protected int loot(int[] array, int start, int end) {
        int collect = 0;
        int i = start;
        for (; i + collect < end; i++) {
            if (i > start) {
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
        IndexedRotations.adaptable(array, start, i, end, 0.1, true, false);
        return start + collect;
    }
    
    public void sort(int[] array, int a, int b) {
        int i = a, j = a;
        while (i < b - 1) {
            j = i;
            i = loot(array, j, b);
            if (j > a) grailMergeWithoutBuffer(array, a, j - a, i - j);
        }
        if (i < b) grailMergeWithoutBuffer(array, a, i - a, b - i);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
