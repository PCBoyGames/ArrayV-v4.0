package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

The more diverse the factors, the worse it gets!

*/
public class SelectionWeaveSortLow extends MadhouseTools {

    int lastGap = 0;

    public SelectionWeaveSortLow(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Selection Weave (Low Prime)");
        this.setRunAllSortsName("Selection Weave Sort (Low Prime)");
        this.setRunSortName("Selection Weavesort (Low Prime)");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void select(int[] array, int currentLength, int g, int s) {
        int rIndex = s + lastGap + 1;
        for (int i = s; i < currentLength; i += g) {
            int minIndex = i;
            for (int j = i + g; j < Math.min(rIndex, currentLength); j += g) if (Reads.compareIndices(array, j, minIndex, 0.5, true) < 0) minIndex = j;
            if (minIndex != i) Writes.swap(array, i, minIndex, 0.5, true, false);
            if (minIndex + lastGap + 1 > rIndex) rIndex = minIndex + lastGap + 1;
        }
    }

    protected void selectNoRight(int[] array, int currentLength, int g, int s) {
        for (int i = s; i < currentLength; i += g) {
            int minIndex = i;
            for (int j = i + g; j < currentLength; j += g) if (Reads.compareIndices(array, j, minIndex, 0.5, true) < 0) minIndex = j;
            if (minIndex != i) Writes.swap(array, i, minIndex, 0.5, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        lastGap = 0;
        int gap = currentLength;
        int[] tree = factorTree(gap);
        String treeString = "[";
        for (int i = 0; i < tree.length; i++) treeString += (tree[i] + ", ");
        treeString = treeString.substring(0, treeString.length() - 2) + "]";
        arrayVisualizer.setExtraHeading(" / " + gap + " = " + treeString);
        for (int i = 0; i < currentLength; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.25);
        }
        while (gap != 1) {
            int s = lowPrime(gap);
            gap /= s;
            if (gap > 1) {
                tree = factorTree(gap);
                treeString = "[";
                for (int i = 0; i < tree.length; i++) treeString += (tree[i] + ", ");
                treeString = treeString.substring(0, treeString.length() - 2) + "]";
                arrayVisualizer.setExtraHeading(" / " + gap + " = " + treeString);
            } else arrayVisualizer.setExtraHeading(" / 1 = []");
            if (lastGap == 0) for (int k = 0; k < gap; k++) selectNoRight(array, currentLength, gap, k);
            else for (int k = 0; k < gap; k++) select(array, currentLength, gap, k);
            lastGap = gap;
        }
    }
}