package sorts.exchange;

import main.ArrayVisualizer;
import sorts.hybrid.BestForNSort;
import sorts.templates.BestForNSortingRes1;
import sorts.templates.BestForNSortingRes2;
import sorts.templates.BestForNSortingRes3;
import sorts.templates.BestForNSortingRes4;
import sorts.templates.MadhouseTools;
import utils.Statistics;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class BFNSpreadSort extends MadhouseTools {
    public BFNSpreadSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Best For N Spread");
        this.setRunAllSortsName("Best For N Spread Sort");
        this.setRunSortName("Best For N Spreadsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(8192);
        this.setBogoSort(false);
        this.setQuestion("Enter the run length for this sort:", 64);
    }

    protected void cs(int[] array, double a, double b) {
        if ((int) a != (int) b) if (Reads.compareIndices(array, (int) a, (int) b, 0.1, true) > 0) Writes.swap(array, (int) a, (int) b, 0.1, true, false);
    }

    protected void spread(int[] array, int start, int end, int over, int depth) {
        Writes.recordDepth(depth);
        int l = end - start;
        if (l < over) {
            if (l < 0) return;
            BestForNSort getNetwork = new BestForNSort(arrayVisualizer);
            getNetwork.callNetwork(array, start, start + l);
            return;
        }
        Statistics.putStat("Network Use");
        Statistics.addStat("Network Use");
        if (over < 39) for (int i = 0; i < BestForNSortingRes1.best1[over].length; i++) cs(array, start + (1.0 * BestForNSortingRes1.best1[over][i][0] / (over - 1)) * (l - 1), start + (1.0 * BestForNSortingRes1.best1[over][i][1] / (over - 1)) * (l - 1));
        else if (over < 50) for (int i = 0; i < BestForNSortingRes2.best2[over - 39].length; i++) cs(array, start + (1.0 * BestForNSortingRes2.best2[over - 39][i][0] / (over - 1)) * (l - 1), start + (1.0 * BestForNSortingRes2.best2[over - 39][i][1] / (over - 1)) * (l - 1));
        else if (over < 58) for (int i = 0; i < BestForNSortingRes3.best3[over - 50].length; i++) cs(array, start + (1.0 * BestForNSortingRes3.best3[over - 50][i][0] / (over - 1)) * (l - 1), start + (1.0 * BestForNSortingRes3.best3[over - 50][i][1] / (over - 1)) * (l - 1));
        else if (over < 65) for (int i = 0; i < BestForNSortingRes4.best4[over - 58].length; i++) cs(array, start + (1.0 * BestForNSortingRes4.best4[over - 58][i][0] / (over - 1)) * (l - 1), start + (1.0 * BestForNSortingRes4.best4[over - 58][i][1] / (over - 1)) * (l - 1));
        if (depth % 2 == 1) {
            if (end - 1 >= start) {
                Writes.recursion();
                spread(array, minSorted(array, start, end - 1, 0, true), maxSorted(array, start, end - 1, 0, true), over, depth + 1);
            }
        }
        if (start + 1 <= end) {
            Writes.recursion();
            spread(array, minSorted(array, start + 1, end, 0, true), maxSorted(array, start + 1, end, 0, true), over, depth + 1);
        }
        if (end - 1 >= start) {
            Writes.recursion();
            spread(array, minSorted(array, start, end - 1, 0, true), maxSorted(array, start, end - 1, 0, true), over, depth + 1);
        }
        if (depth % 2 == 0) {
            if (start + 1 <= end) {
                Writes.recursion();
                spread(array, minSorted(array, start + 1, end, 0, true), maxSorted(array, start + 1, end, 0, true), over, depth + 1);
            }
        }
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 2) return 2;
        if (answer > 64) return 64;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int base) {
        arrayVisualizer.setExtraHeading(" / NETWORK: " + base);
        spread(array, minSorted(array, 0, currentLength, 0, true), maxSorted(array, 0, currentLength, 0, true), Math.min(base, currentLength), 0);
    }
}