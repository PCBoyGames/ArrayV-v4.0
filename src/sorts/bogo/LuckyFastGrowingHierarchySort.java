package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/



 */

public class LuckyFastGrowingHierarchySort extends BogoSorting {
    public LuckyFastGrowingHierarchySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Lucky Fast-growing Hierarchy");
        this.setRunAllSortsName("Lucky Fast-growing Hierarchy Sort");
        this.setRunSortName("Lucky Fast-growing Hierarchy Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2);
        this.setBogoSort(true);
        this.setQuestion("Enter the luck for this sort:", 50);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 100) return 50;
        return answer;
    }

    private long f(int a, long n, int[] array, int d, int l) {
        Writes.recordDepth(d++);
        if (a == 0) {
            return n+1;
        } else {
            long b = n;
            for (long i = 0; i < n; i++) {
                Writes.recursion();
                b = f(a-1, b, array, d, l);
                if (Reads.compareValues(array[(a-2 < 0) ? a : a-2], array[(a-1 < 1) ? a+1 : a-1]) > 0) {
                    if (randInt(1, 101) <= l) Writes.swap(array, (a-2 < 0) ? a : a-2, (a-1 < 1) ? a+1 : a-1, 0.01, true, false);
                }
            }
            System.out.println(a + " " + b);
            return b;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int luck) {
        do {
            f(currentLength, currentLength, array, 0, luck);
        } while (!isArraySorted(array, currentLength));
    }
}
