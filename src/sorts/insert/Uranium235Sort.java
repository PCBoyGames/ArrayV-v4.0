package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class Uranium235Sort extends Sort {

    public Uranium235Sort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Uranium-235");
        this.setRunAllSortsName("Uranium-235 Sort");
        this.setRunSortName("Uranium-235~sort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void omegaShell(int[] array, int start, int end, int gap, int d) {
        Writes.recordDepth(d++);
        if(end-start <= gap || gap < 1)
            return;
        for(int i=start+gap; i<end; i++) {
            int t = array[i], j=i-gap;
            Highlights.markArray(1, j);
            Delays.sleep(0.001);
            while(j >= start) {
                if(Reads.compareValues(array[j], t) < 0) {
                    break;
                }
                Writes.write(array, j+gap, array[j], 0.001, true, false);
                j -= gap;
            }
            if(j < i - gap) {
                Writes.write(array, j+gap, t, 0.001, true, false);
            }
            Writes.recursion(2);
            this.omegaShell(array, start, end-1, gap, d);
            this.omegaShell(array, start, end, gap-1, d);
        }
        if(gap == 1)
            return;
    }
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.omegaShell(array, 0, currentLength, currentLength-1, 0);
    }
}