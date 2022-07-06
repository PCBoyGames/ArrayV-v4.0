package sorts.esoteric;
import main.ArrayVisualizer;
import sorts.templates.BogoSorting;
/* Idea Storage Sort: An accidentally stable sorting algorithm
 * made from genuine hell rocks.
 * Ported from old Sorts 2018 mod.
 */
public class IssaSort extends BogoSorting {
    public IssaSort(ArrayVisualizer aV) {
        super(aV);
        this.setSortListName("Issa");
        this.setRunAllSortsName("Idea Storage/\"Issa\" Sort");
        this.setRunSortName("Issasort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private void PartialSort(int[] array, int start, int end) {
        while (Reads.compareValues(array[start], array[start+1]) > 0 && start<end-1) {
            Writes.swap(array, start, start+1, 0.05, true, false);
            start++;
        }
    }
    private void WeirdSort(int[] array, int end) {
        int i=0, j=0, k=0;
        while (i<end && j<end) {
            if (Reads.compareValues(array[i], array[i+1]) > 0) {
                PartialSort(array, i, end);
            }
            if (Reads.compareValues(array[j], array[j+1]) > 0) {
                Writes.swap(array, j, j+1, 0.05, true, false);
                j++;
            }
            if (Reads.compareValues(array[i], array[j]) < 0) {
                i=j;
            } else {
                i++;
            }
            if (Reads.compareValues(array[k], array[k+1]) > 0) { // **SERIOUSLY, HOW THE FUCK IS IT STABLE**
                if (k<i) {
                    PartialSort(array, k, end);
                    k++;
                } else {
                    k=0;
                }
            }

            /* Remove this if you're a masochist. */
            //if (Reads.compareValues(array[i], array[i+1]) < 0 && Reads.compareValues(array[j], array[j+1]) < 0 && Reads.compareValues(array[k], array[k+1]) < 0) {
            //    i++;
            //}
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        while (!isArraySorted(array, sortLength)) {
            WeirdSort(array, sortLength);
        }
    }
}