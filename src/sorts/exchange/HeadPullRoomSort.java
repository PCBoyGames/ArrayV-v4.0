package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class HeadPullRoomSort extends Sort {
    public HeadPullRoomSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Head Pull Room");
        this.setRunAllSortsName("Head Pull Room Sort");
        this.setRunSortName("Head Pull Roomsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int min(int[] array, int start, int end) {
        int i = start;
        int min = start;
        while (i < end) {
            if (Reads.compareIndices(array, i, min, 0.05, true) < 0) min = i;
            i++;
        }
        return min;
    }

    protected boolean pull(int[] array, int start, int end) {
        Highlights.clearMark(3);
        boolean swap = false;
        int i = start + 1;
        int pull;
        int check = start;
        int highpull = min(array, start, end);
        Highlights.markArray(3, highpull);
        while (check != highpull) {
            boolean pulled = false;
            while (!pulled) {
                if (Reads.compareIndices(array, i - 1, i, 0.1, true) > 0) {
                    pull = i;
                    pulled = true;
                    if (pull > check) check = pull;
                    while (pull > start) {
                        Writes.swap(array, pull - 1, pull, 0.1, true, false);
                        swap = true;
                        pull--;
                    }
                    i = start + 1;
                } else i++;
            }
        }
        return swap;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int size = (int) Math.sqrt(currentLength) + 1;
        int start = 0;
        int last = currentLength;
        boolean continues = true;
        for (int effectivelen = currentLength; effectivelen > 0 && continues; effectivelen = last - size) {
            continues = false;
            for (int i = start - size > 0 ? start - size : 0; i < effectivelen; i++) {
                boolean sel = pull(array, i, i + size < effectivelen ? i + size + 1 : effectivelen);
                continues = sel || continues;
                if (!continues) start = i;
                if (sel) last = i + size < effectivelen ? i + size + 1 : effectivelen;
            }
        }
    }
}