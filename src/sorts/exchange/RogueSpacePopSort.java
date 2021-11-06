package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class RogueSpacePopSort extends Sort {
    public RogueSpacePopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Rogue-Space Pop");
        this.setRunAllSortsName("Rogue-Space Pop Sort");
        this.setRunSortName("Rogue-Space Pop Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    protected void bubble(int[] array, int start, int end, int dir) {
        int c = 1;
        int s;
        int f = 1;
        boolean a = false;
        for (int j = end - 1; j > 0; j -= c) {
            if (f - 1 < start) {
                s = start;
            } else {
                s = f - 1;
            }
            a = false;
            c = 1;
            for (int i = s; i < j; i++) {
                if (Reads.compareIndices(array, i, i + 1, 0.005, true) == dir) {
                    Writes.swap(array, i, i + 1, 0.015, true, false);
                    if (!a) {
                        f = i;
                    }
                    a = true;
                    c = 1;
                } else {
                    c++;
                }
            }
        }
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int len = 2;
        int index = 0;
        int dir = -1;
        while (len <= currentLength / 2) {
            index = 0;
            dir = -1;
            while (index + len <= currentLength) {
                bubble(array, index, index + len, dir);
                index += len;
                dir *= -1;
            }
            len++;
        }
        bubble(array, 0, currentLength, 1);
    }
}