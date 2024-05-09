package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

Or, Blubble but it's actually Bubble but it's actually In Place and the code is shorter this way.

*/
public class InPlaceJerenSort extends Sort {
    public InPlaceJerenSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("In Place Jeren");
        this.setRunAllSortsName("In Place Jeren Sort");
        this.setRunSortName("In Place Jerensort");
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
        int con = 1;
        int start;
        int first = 1;
        for (int end = currentLength; end > 0; end -= con) {
            if (first - 1 < 0) start = 0;
            else start = first - 1;
            boolean any = false;
            con = 1;
            for (int i = start; i + 1 < end; i++) {
                if (Reads.compareIndices(array, i, i + 1, 0.25, true) > 0) {
                    int left = i++;
                    for (; i + 1 < end; i++) if (Reads.compareIndices(array, left, i + 1, 0.25, true) <= 0) break;
                    Highlights.clearMark(2);
                    Writes.insert(array, left, i, 0.25, true, false);
                    if (!any) first = left;
                    any = true;
                    con = 1;
                } else con++;
            }
            Highlights.clearMark(2);
        }
    }
}