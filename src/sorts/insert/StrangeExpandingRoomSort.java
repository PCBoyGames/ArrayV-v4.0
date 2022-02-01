package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class StrangeExpandingRoomSort extends Sort {

    int first;
    int last;

    public StrangeExpandingRoomSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Strange Expanding Room");
        this.setRunAllSortsName("Strange Expanding Room Sort");
        this.setRunSortName("Strange Expanding Roomsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the base for this sort:", 2);
    }

    protected int binarySearch(int[] array, int a, int b, int value) {
        while (a < b) {
            int m = a + ((b - a) / 2);
            Highlights.markArray(1, a);
            Highlights.markArray(3, m);
            Highlights.markArray(2, b);
            Delays.sleep(0.015);
            if (Reads.compareValues(value, array[m]) < 0) b = m;
            else a = m + 1;
        }
        Highlights.clearMark(3);
        return a;
    }

    protected boolean binsert(int[] array, int start, int gap, int currentLength) {
        boolean inserts = false;
        boolean firstfound = false;
        for (int i = start + 1; i < currentLength; i++) {
            if (Reads.compareValues(array[i - 1], array[i]) > 0) {
                inserts = true;
                int item = array[i];
                int left = i - gap > start ? i - gap : start;
                if (Reads.compareValues(array[left], item) <= 0) left = binarySearch(array, left + 1, i - 1, item);
                Highlights.clearAllMarks();
                Highlights.markArray(2, left);
                for (int right = i; right > left; right--) Writes.write(array, right, array[right - 1], 0.015, true, false);
                Writes.write(array, left, item, 0.015, true, false);
                if (!firstfound) {
                    first = left;
                    firstfound = true;
                }
                Highlights.clearAllMarks();
                last = i;
            } else {
                Highlights.markArray(1, i);
                Delays.sleep(0.015);
            }
        }
        return inserts;
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 0) return 0;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        first = 0;
        last = currentLength;
        boolean inserts = true;
        int end = currentLength;
        int initgap = base;
        while (initgap < Math.sqrt(currentLength) + 1) {
            initgap *= base;
        }
        for (int g = initgap; inserts; g *= base) {
            inserts = binsert(array, first, g, end);
            end = last;
            first = first - (g * base) - 1 > 0 ? first - (g * base) - 1 : 0;
        }
    }
}