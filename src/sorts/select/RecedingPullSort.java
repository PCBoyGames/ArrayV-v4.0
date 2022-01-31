package sorts.select; import main.ArrayVisualizer; import sorts.templates.Sort;
import sorts.insert.InsertionSort;
public class RecedingPullSort extends Sort {public RecedingPullSort(
    ArrayVisualizer arrayVisualizer){super(arrayVisualizer); this.setCategory(
    "Selection Sorts"); this.setSortListName("Receding Pull"); this.
    setRunAllSortsName("Receding Pull Sort"); this.setRunSortName(
    "Receding Pull Sort"); this.setRadixSort(false); this.setBogoSort(false);
    this.setComparisonBased(true); this.setBucketSort(false); this.
    setUnreasonablySlow(true); this.setUnreasonableLimit(32);} // wanna get
    // over with

    private void recedingPull(int[] array, int start, int end, int rec) {
        if(end <= start) return;
        Writes.recordDepth(rec++);
        for(int i=start; i<end-1; i++) {
            for(int j=end-1; j>i; j--) {
                Highlights.markArray(2, j);
                Delays.sleep(0.01);
                if(Reads.compareValues(array[j-1], array[j]) > 0) {
                    if(j < end-1) {
                        Writes.recursion();
                        this.recedingPull(array, start, j+1, rec);
                    } else {
                        Writes.multiSwap(array, j++, i, 1, true, false);
                        this.recedingPull(array, start+1, j, rec);
                    }
                }
            }
        }
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.recedingPull(array, 0, length, 0);
        InsertionSort i = new InsertionSort(arrayVisualizer);
        i.customInsertSort(array, 0, length, 0.125, false);
    }
}