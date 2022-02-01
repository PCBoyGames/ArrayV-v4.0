package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class LuckyCircleSort extends BogoSorting {
    public LuckyCircleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Circle");
        this.setRunAllSortsName("Lucky Circle Sort");
        this.setRunSortName("Lucky Circlesort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(true);
        this.setQuestion("Enter the luck for this sort:", 50);
    }

    protected boolean circle(int[] array, int a, int b, boolean anyswaps, int luck) {
        int left = a;
        int right = b;
        boolean swaphere = false;
        while (left < right) {
            Highlights.markArray(1, left);
            Highlights.markArray(2, right);
            Delays.sleep(0.5);
            if (Reads.compareValues(array[left], array[right]) > 0) {
                if (randInt(1, 101) <= luck) Writes.swap(array, left, right, 0.5, true, false);
                swaphere = true;
            }
            left++;
            right--;
        }
        if (anyswaps) return anyswaps;
        else return swaphere;
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 100) return 50;
        return answer;
    }


    @Override
    public void runSort(int[] array, int currentLength, int luck) {
        int offset = 0;
        int gap = 2;
        boolean anyswaps = true;
        while (anyswaps) {
            anyswaps = false;
            gap = currentLength;
            while (gap >= 1) {
                offset = 0;
                while (offset + (gap - 1) < currentLength) {
                    anyswaps = circle(array, offset, offset + (gap - 1), anyswaps, luck);
                    offset += gap;
                }
                gap /= 2;
            }
        }
    }
}
