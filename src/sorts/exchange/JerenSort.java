package sorts.exchange;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

Or, Blubble but it's actually Bubble.

*/
public class JerenSort extends Sort {
    public JerenSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Jeren");
        this.setRunAllSortsName("Jeren Sort");
        this.setRunSortName("Jerensort");
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
        ArrayList<int[]> insertList = new ArrayList<>();
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
                    insertList.add(new int[] {left, i});
                    Writes.allocAmount += 2;
                    Writes.auxWrites += 2;
                    if (!any) first = left;
                    any = true;
                    con = 1;
                } else con++;
            }
            Highlights.clearMark(2);
            for (int i = 0; i < insertList.size(); i++) {
                Writes.insert(array, insertList.get(i)[0], insertList.get(i)[1], 0.25, true, false);
                Writes.allocAmount -= 2;
            }
            insertList.clear();
        }
    }
}