package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils. Rotations;

// Demolition Derby Sort: A mod of Cycle Sort with optimizations for small numbers.
final public class DemoDerbySort extends Sort {
    public DemoDerbySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Demolition Derby");
        this.setRunAllSortsName("Demolition Derby Sort");
        this.setRunSortName("Demolition Derby Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    public void multiSwap(int[] array, int a, int b, int s) {
        while(s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        // A compare optimization could be made that would introduce
        // O(k) aux, but I'm pretty sure that the plan would only work
        // on linear most-similars.
        for(int i=0; i<length-1; i++) {
            int lower, same;
            do {
                lower = same = 0;
                Highlights.markArray(2, i);
                for(int j=i+1; j < length; j++) {
                    int c=Reads.compareValues(array[j], array[i]);
                    Highlights.markArray(1, j);
                    Delays.sleep(0.01);
                    if(c == -1) {
                        lower++;
                    } else if(c == 0) {
                        Writes.swap(array, i + ++same, j, 1, true, false);
                    }
                }
                if(lower == 0 && same > 0) {
                    i += same;
                    break;
                }
                if(lower > same)
                    multiSwap(array, i, i+lower, same+1);
                else
                    Rotations.holyGriesMills(array, i, same + 1, lower, 1, true, false);
            } while(lower > 0);
        }
    }
}