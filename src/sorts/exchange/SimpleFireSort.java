package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES
FROM A VARIANT OF FIRE SORT BY NAOAN1201

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SimpleFireSort extends Sort {
    public SimpleFireSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Simple Fire");
        this.setRunAllSortsName("Simple Fire Sort");
        this.setRunSortName("Simple Fire Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int twist = 1;
        int limit = currentLength;
        int i = 0;
        int moves = 0;
        while (i < currentLength || twist == -1) {
            if (i == 0 && twist == -1){
                twist *= -1;
                moves = 0;
            }
            for (int j = i; (twist == -1 ? j < currentLength : j > 0) && Reads.compareValues(array[j - 1], array[j]) == twist; j -= twist) {
                moves++;
                Writes.swap(array, j, j - 1, 0.005, true, false);
            }
            i += twist;
            if (moves > limit){
                limit += currentLength;
                moves = 0;
                twist *= -1;
            }
        }
    }
}