package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class SelectionRoomSort extends MadhouseTools {
    public SelectionRoomSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Selection Room");
        this.setRunAllSortsName("Selection Room Sort");
        this.setRunSortName("Selection Roomsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean select(int[] array, int start, int end) {
        int sel = start;
        for (int i = start + 1; i < end; i++) if (Reads.compareIndices(array, i, sel, 0.05, true) < 0) sel = i;
        if (sel != start) Writes.swap(array, start, sel, 0.1, true, false);
        return sel != start;
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
                boolean sel = select(array, i, i + size < effectivelen ? i + size + 1 : effectivelen);
                continues = sel || continues;
                if (!continues) start = i;
                if (sel) last = i + size < effectivelen ? i + size + 1 : effectivelen;
            }
        }
    }
}