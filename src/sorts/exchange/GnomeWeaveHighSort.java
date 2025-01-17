package sorts.exchange;
import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;
/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

Gnome Weave by Highest Prime is a reversal of Gnome Weave by Lowest Prime.

The prime check runs to the first result in Lowest Prime, but Highest Prime
runs it to the last result.

Because the sorting algorithm divides by prime numbers over and over again,
list lengths with recurrent factor trees cause it to act accordingly. Also,
if the length is a prime number itself, it will resort to plain OptiGnome.
Try lengths like 1365, 2310, or 4199, and it will appear more diverse.

*/
public class GnomeWeaveHighSort extends MadhouseTools {
    public GnomeWeaveHighSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Gnome Weave (High Prime)");
        this.setRunAllSortsName("Gnome Weave Sort (High Prime)");
        this.setRunSortName("Gnome Weave (High Prime)");
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
        int gap = currentLength;
        int icheck = 1;
        int primetesti = 2;
        boolean finalgap = false;
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
        while (!finalgap) {
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
            }
            int i = icheck;
            int bound = icheck;
            while ((i - 1) + gap < currentLength) {
                if (Reads.compareIndices(array, i - 1, (i - 1) + gap, 0.25, true) > 0) {
                    Writes.swap(array, i - 1, (i - 1) + gap, 0.25, true, false);
                    if (i == icheck) {
                        bound += gap;
                        i = bound;
                    }
                    else if (i - gap > 0) i -= gap;
                } else {
                    bound += gap;
                    i = bound;
                }
            }
            if (gap == 1) finalgap = true;
            if (icheck + 1 > gap && !finalgap) {
                double primetestrunning = gap;
                while (primetestrunning != 1) {
                    boolean primetest = false;
                    primetesti = 2;
                    while (!primetest) {
                        if (Math.floor(primetestrunning / primetesti) == primetestrunning / primetesti) {
                            primetestrunning = primetestrunning / primetesti;
                            primetest = true;
                        } else primetesti++;
                    }
                }
                gap = gap / primetesti;
                icheck = 1;
            } else icheck++;
        }
    }
}