package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class LuckyPopSort extends BogoSorting {
    public LuckyPopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Pop");
        this.setRunAllSortsName("Lucky Pop Sort");
        this.setRunSortName("Lucky Pop Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the luck for this sort:", 50);
    }
    
    protected void bubble(int[] array, int start, int end, int dir, int luck) {
        boolean anyswaps = true;
        while (anyswaps) {
            anyswaps = false;
            for (int i = start; i < end; i++) {
                Highlights.markArray(1, i - 1);
                Highlights.markArray(2, i);
                Delays.sleep(0.05);
                if (Reads.compareValues(array[i - 1], array[i]) == dir) {
                    anyswaps = true;
                    if (randInt(1, 101) <= luck) Writes.swap(array, i - 1, i, 0.05, true, false);
                }
            }
        }
    }
    
    
    @Override
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 100) return 50;
        return answer;
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int luck) {
        bubble(array, 1, (int) Math.floor((currentLength + 1) / 4), -1, luck);
        bubble(array, (int) Math.floor((currentLength + 1) / 4) + 1, (int) Math.floor((currentLength + 1) / 2), 1, luck);
        bubble(array, (int) Math.floor((currentLength + 1) / 2) + 1, (int) Math.floor (((currentLength + 1) * 3) / 4), -1, luck);
        bubble(array, (int) Math.floor(((currentLength + 1) * 3) / 4) + 1, currentLength, 1, luck);
        bubble(array, 1, (int) Math.floor((currentLength + 1) / 2), -1, luck);
        bubble(array, (int) Math.floor((currentLength + 1) / 2) + 1, currentLength, 1, luck);
        bubble(array, 1, currentLength, 1, luck);
    }
}