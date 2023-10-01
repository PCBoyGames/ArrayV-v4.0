package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class CocktailWeaveHighSort extends MadhouseTools {
    public CocktailWeaveHighSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cocktail Weave (High Prime)");
        this.setRunAllSortsName("Cocktail Weave Sort (High Prime)");
        this.setRunSortName("Cocktail Weave (High Prime)");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void ctPass(int[] array, int currentLength, int gap) {
        int a = 0;
        int b = currentLength - 1;
        while (true) {
            boolean swaps = false;
            int last = a;
            for (int i = a; i + gap <= b; i++) {
                if (Reads.compareIndices(array, i, i + gap, 0.25, true) > 0) {
                    Writes.swap(array, last = i, i + gap, 0.25, swaps = true, false);
                }
            }
            if (!swaps) break;
            swaps = false;
            b = last;
            for (int i = b; i - gap >= a; i--) {
                if (Reads.compareIndices(array, i - gap, i, 0.25, true) > 0) {
                    Writes.swap(array, i - gap, last = i, 0.25, swaps = true, false);
                }
            }
            a = last;
            if (!swaps) break;
        }
    }

    protected boolean woven(int[] array, int currentLength, int gap) {
        for (int i = 0; i + gap < currentLength; i++) if (Reads.compareIndices(array, i, i + gap, 1, true) > 0) return false;
        return true;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int gap = currentLength;
        int[] tree = factorTree(gap);
        String treeString = "[";
        for (int i = 0; i < tree.length; i++) {
            treeString += (tree[i] + ", ");
        }
        treeString = treeString.substring(0, treeString.length() - 2) + "]";
        arrayVisualizer.setExtraHeading(" / " + gap + " = " + treeString);
        for (int i = 0; i < currentLength; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.25);
        }
        while (gap > 1) {
            gap /= highPrime(gap);
            if (gap > 1) {
                tree = factorTree(gap);
                treeString = "[";
                for (int i = 0; i < tree.length; i++) {
                    treeString += (tree[i] + ", ");
                }
                treeString = treeString.substring(0, treeString.length() - 2) + "]";
                arrayVisualizer.setExtraHeading(" / " + gap + " = " + treeString);
            } else {
                arrayVisualizer.setExtraHeading(" / 1 = []");
                break;
            }
            ctPass(array, currentLength, gap);
            //if (!woven(array, currentLength, gap)) return;
        }
        OptimizedCocktailShakerSort ocs = new OptimizedCocktailShakerSort(arrayVisualizer);
        ocs.runSort(array, currentLength, 0);
    }
}
