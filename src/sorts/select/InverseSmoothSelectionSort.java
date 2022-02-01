package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class InverseSmoothSelectionSort extends Sort {
    public InverseSmoothSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Inverse Smooth Selection");
        this.setRunAllSortsName("Inverse Smooth Selection Sort");
        this.setRunSortName("Inverse Smooth Selection Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private void multiSwap(int[] array, int a, int b, int s) {
        while(s-- > 0)
            Writes.swap(array, a++, b++, 1, true, false);
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = 0; i < length - 1; i++) {
            int seekIndex = i, equalSize = 0;

            for (int j = i + 1; j < length; j++) {
                Highlights.markArray(2, j);
                Delays.sleep(0.01);
                int comp = Reads.compareValues(array[j], array[seekIndex]);
                if (comp <= 0){
                    if(j != ++seekIndex)
                        Writes.swap(array, j, seekIndex, 1, true, false);
                    if(comp == 0)
                        equalSize++;
                    else
                        equalSize=0;
                }
            }
            if(seekIndex == length - 1) {
                Writes.reversal(array, i, length-1, 1, true, false);
                break;
            }
            multiSwap(array, i, seekIndex - equalSize, equalSize + 1);
            i += equalSize;
        }
    }
}