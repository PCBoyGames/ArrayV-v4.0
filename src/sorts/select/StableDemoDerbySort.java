package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils. Rotations;


final public class StableDemoDerbySort extends Sort {
    public StableDemoDerbySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stable Demolition Derby");
        this.setRunAllSortsName("Stable Demolition Derby Sort");
        this.setRunSortName("Stable Demolition Derby Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
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
                        Writes.multiSwap(array, j, i + ++same, 0.0625, true, false);
                        lower--;
                    }
                }
                if(lower <= 0 && same > 0) {
                    i += same;
                    break;
                }
                Rotations.holyGriesMills(array, i, same + 1, lower, 0.5, true, false);
            } while(lower > 0);
        }
    }
}