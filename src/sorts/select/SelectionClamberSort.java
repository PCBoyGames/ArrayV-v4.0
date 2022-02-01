package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;


final public class SelectionClamberSort extends Sort {
    public SelectionClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Selection Clamber");
        this.setRunAllSortsName("Selection Clamber Sort");
        this.setRunSortName("Selection Clamber Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private void selectionClamber(int[] array, int start, int end, int rec) {
        Writes.recordDepth(rec++);
        for(int i=start; i<end-1; i++) {
            int l=i;
            for(int j=i+1; j<end; j++) {
                Highlights.markArray(2, j);
                Delays.sleep(0.01);
                if(Reads.compareValues(array[j], array[l]) < 0) {
                    l = j;
                }
            }
            if(l == end-1) {
                Writes.swap(array, l, i, 1, true, false);
            } else {
                Writes.recursion();
                this.selectionClamber(array, i, l+1, rec);
            }
        }
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.selectionClamber(array, 0, length, 0);
    }
}