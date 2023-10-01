package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;
import visuals.VisualStyles;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class WharSort extends MadhouseTools {
    public WharSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Whar");
        this.setRunAllSortsName("Whar Sort");
        this.setRunSortName("Wharsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        VisualStyles vis = arrayVisualizer.getVisualStyles();
        int mainlen = currentLength;
        int[] dims;
        while (currentLength > 1) {
            boolean checks = true;
            dims = getRectangleDimensions(currentLength);
            vis.resetvals();
            while (checks) {
                checks = false;
                arrayVisualizer.setExtraHeading(" / Factor Pair: (" + dims[1] + "," + dims[0] + ")");
                boolean swapshere = true;
                int len = currentLength;
                int par;
                while (swapshere) {
                    swapshere = false;
                    Highlights.clearAllMarks();
                    par = parX(array, 0, len, 0.1, true);
                    for (int i = 0; i < len; i++) for (int j = Math.min(i + par, len - 1); j > i; j--) if (isValidRectangleAction(dims[0], dims[1], i, j)) if (Reads.compareIndices(array, i, j, 0.05, true) > 0) Writes.swap(array, i, j, 0.05, swapshere = (checks = true), false);
                    len -= dims[0];
                }
                if (!checks || dims[1] == 1 || dims[0] == dims[1]) break;
                vis.swapvals();
                checks = false;
                arrayVisualizer.setExtraHeading(" / Factor Pair: (" + dims[0] + "," + dims[1] + ")");
                swapshere = true;
                len = currentLength;
                while (swapshere) {
                    swapshere = false;
                    Highlights.clearAllMarks();
                    par = parX(array, 0, len, 0.1, true);
                    for (int i = 0; i < len; i++) for (int j = Math.min(i + par, len - 1); j > i; j--) if (isValidRectangleAction(dims[1], dims[0], i, j)) if (Reads.compareIndices(array, i, j, 0.05, true) > 0) Writes.swap(array, i, j, 0.05, swapshere = (checks = true), false);
                    len -= dims[1];
                }
                vis.swapvals();
            }
            vis.resetvals();
            if (coprime(dims[0], dims[1])) break;
            currentLength = maxSorted(array, 0, currentLength, 0.1, true);
            arrayVisualizer.setCurrentLength(currentLength);
        }
        arrayVisualizer.setCurrentLength(mainlen);
        arrayVisualizer.setExtraHeading("");
    }
}