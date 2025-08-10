package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

Please be advised that this sorting algorithm bears no responsibility or
liability for any potential damages, disruptions, inaccuracies, or
unintended consequences that may arise in connection with, or as a result
of, its viewpoints related to online wikis or similar digital platforms.

*/
public class SulioSort extends MadhouseTools {
    public SulioSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Sulio");
        this.setRunAllSortsName("Sulio Sort");
        this.setRunSortName("Sulio Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    public void sulio(int[] array, int start, int end, int depth) {
        Writes.recordDepth(depth);
        while (true) {
            if (patternDefeat(array, start, end, false, 0.1, true, false)) return;
            int half = (end - start) / 2;
            int adaptStart = start;
            while (adaptStart < start + half) {
                int min = start + half;
                for (int i = start + half + 1; i < end; i++) if (Reads.compareIndices(array, i, min, 0.05, true) < 0) min = i;
                int m = array[min];
                Highlights.clearMark(2);
                boolean swap = false;
                int last = start + half;
                int take = end - 1;
                while (min >= start + half) {
                    last = start + half - 1;
                    //take = end - 1;
                    for (int i = start + half - 1; i >= adaptStart; i--) if (Reads.compareValues(array[i], m) > 0) {
                        Writes.insert(array, last = i, take--, 0.001, swap = true, false);
                        min--;
                    }
                    if (!swap) break;
                }
                adaptStart = last;
                if (!swap) break;
            }
            if (end - start > 2) {
                Writes.recursion();
                sulio(array, start, start + half, depth + 1);
                start = start + half;
            } else break;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        sulio(array, 0, currentLength, 0);
    }
}