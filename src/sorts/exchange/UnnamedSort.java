package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class UnnamedSort extends MadhouseTools {
    public UnnamedSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Unnamed");
        this.setRunAllSortsName("Unnamed Sort");
        this.setRunSortName("Unnamedsort");
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
        boolean bool = false;
        int p1 = 1;
        int p2 = 1;
        int t2 = currentLength;
        while (!bool) {
            int t1 = p2;
            while (!(p2 == t2 || (p1 < p2 && compareIndicesI1(array, p2, p2 - 1, 0.25, true) < 0))) p2++;
            if (p2 == t2) {
                while (!(p2 == currentLength || compareIndicesI1(array, p2, p2 + 1, 0.25, true) <= 0)) p2++;
                t2 = p2;
            }
            if (compareIndicesI1(array, p2, p2 - 1, 0.25, true) < 0) {
                while (!(p1 == p2 - 1 || compareIndicesI1(array, p1, p2, 0.25, true) > 0)) p1++;
                if (t2 < currentLength && compareIndicesI1(array, p1, t2, 0.25, true) > 0) while (compareIndicesI1(array, p1, t2 + 1, 0.25, true) > 0) t2++;
                swapI1(array, p1, p2, 0.25, true, false);
            }
            if (p2 == t2) {
                if (compareIndicesI1(array, t2, t2 - 1, 0.25, true) >= 0) {
                    t2 = t1;
                    if (p1 == 1) bool = true;
                    else {
                        p1 = 1;
                        p2 = 1;
                    }
                }
            } else {
                p1++;
                if (compareIndicesI1(array, p1, p1 - 1, 0.25, true) >= 0) p2++;
            }
        }
    }
}