package sorts.exchange;
import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;
/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

Gnome Weave by Lowest Prime is a mix of OptiGnome Sort and prime numbers.

The sorting begins with setting up a gap, G. Any items a multiple of G away
from a target item, T, are compared, starting with items T and T + G. After
swapping them as needed, if an item exists at T + 2G, then T + G and T + 2G
are compared. If T + 2G is less than T + G, these two items will swap. If
this new T + G is also less than T, these two swap as well. If the upper
item in either case is greater than the other, no swap is needed and the
sort will skip forward to check for T + 3G. When the next T + XG item does
not exist, the target changes by 1, and so on until T is G - 1.

Upon reaching the last unique target value, the gap value is tested for the
smallest possible prime number, P, it can evenly divide by. The next value
is then set to G/P, the target resets to the first value, and the sorting
continues. This repeats until G is 1, and after this, the sort is complete.

The way I coded the prime check in the original Scratch project as well as
in the ArrayV port is a bit crude, because composite numbers are also being
checked for in this gap division. However, I get away with it since running
possible divisions that all occur evenly would attempt the prime factors of
a composite number before the composite number itself.

Because the sorting algorithm divides by prime numbers over and over again,
list lengths with recurrent factor trees cause it to act accordingly. Also,
if the length is a prime number itself, it will resort to plain OptiGnome.
Try lengths like 1365, 2310, or 4199, and it will appear more diverse.

*/
public class GnomeWeaveLowSort extends MadhouseTools {
    public GnomeWeaveLowSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Gnome Weave (Low Prime)");
        this.setRunAllSortsName("Gnome Weave Sort (Low Prime)");
        this.setRunSortName("Gnome Weave (Low Prime)");
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
                while (primetestrunning == gap) {
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