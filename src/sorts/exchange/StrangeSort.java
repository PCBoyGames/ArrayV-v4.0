package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class StrangeSort extends Sort {
    public StrangeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Strange");
        this.setRunAllSortsName("Strange Sort");
        this.setRunSortName("Strangesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the base for this sort:", 2);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 2) return 2;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        boolean anyswaps = true;
        while (anyswaps) {
            anyswaps = false;
            for (int offset = 1; offset != currentLength; offset++) {
                int mult = 1;
                int bound = 1;
                while (offset + mult <= currentLength) {
                    if (Reads.compareIndices(array, (int) (offset + mult / bucketCount) - 1, (int) (offset + mult) - 1, 0.1, true) > 0) {
                        Writes.swap(array, (int) (offset + mult / bucketCount) - 1, (int) (offset + mult) - 1, 0.1, anyswaps = true, false);
                        if (mult == 1 / bucketCount) {
                            bound *= bucketCount;
                            mult = bound;
                        } else mult /= bucketCount;
                    } else {
                        bound *= bucketCount;
                        mult = bound;
                    }
                }
            }
        }
    }
}