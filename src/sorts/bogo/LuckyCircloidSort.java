package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

Coded for ArrayV by Kiriko-chan
in collaboration with yuji and PCBoy

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Kiriko-chan
 * @author yuji
 * @author PCBoy
 *
 */
public final class LuckyCircloidSort extends BogoSorting {

    public LuckyCircloidSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Circloid");
        this.setRunAllSortsName("Lucky Circloid Sort");
        this.setRunSortName("Lucky Circloid Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(true);
        setQuestion("Enter the luck for this sort:", 50);
    }
    
    @Override
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 100) return 50;
        return answer;
    }

    protected boolean circle(int[] array, int left, int right, int luck) {
        int a = left;
        int b = right - 1;
        boolean swapped = false;
        while (a < b) {
            if (Reads.compareIndices(array, a, b, 0.5, true) > 0) {
                if (BogoSorting.randInt(1, 101) <= luck) Writes.swap(array, a, b, 0.5, true, false);
                swapped = true;
            }
            a++;
            b--;
            if (a == b) b++;
        }
        return swapped;
    }

    public boolean circlePass(int[] array, int left, int right, int luck, int depth) {
        Writes.recordDepth(depth);
        if (right - left < 2) return false;
        int mid = left + (right - left) / 2; // avoid integer overflow
        Writes.recursion();
        boolean l = circlePass(array, left, mid, luck, depth + 1);
        Writes.recursion();
        boolean r = circlePass(array, mid, right, luck, depth + 1);
        return circle(array, left, right, luck) || l || r;
    }

    @Override
    public void runSort(int[] array, int sortLength, int luck) {
        for (; circlePass(array, 0, sortLength, luck, 0); );
    }
}