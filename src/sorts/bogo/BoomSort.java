package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class BoomSort extends BogoSorting {
    public BoomSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Boom");
        this.setRunAllSortsName("Boom Sort");
        this.setRunSortName("Boomsort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2048);
        this.setBogoSort(false);
    }
    
    protected boolean bogo(int[] array, int start, int end) {
        boolean swap = false;
        while (!isMinSorted(array, start, end)) {
            swap = true;
            bogoSwap(array, start, end, false);
        }
        return swap;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        delay = 0.05;
        int size = (int) Math.sqrt(currentLength) + 1;
        int start = 0;
        int last = currentLength;
        boolean continues = true;
        for (int effectivelen = currentLength; effectivelen > 0 && continues; effectivelen = last - size) {
            continues = false;
            for (int i = start - size > 0 ? start - size : 0; i < effectivelen; i++) {
                boolean sel = bogo(array, i, i + size < effectivelen ? i + size + 1 : effectivelen);
                continues = sel || continues;
                if (!continues) start = i;
                if (sel) last = i + size < effectivelen ? i + size + 1 : effectivelen;
            }
        }
    }
}