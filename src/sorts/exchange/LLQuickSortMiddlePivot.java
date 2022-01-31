package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class LLQuickSortMiddlePivot extends Sort {
    public LLQuickSortMiddlePivot(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Left/Left Quick (Middle Pivot)");
        this.setRunAllSortsName("Quick Sort, Left/Left Pointers (Middle Pivot)");
        this.setRunSortName("Left/Left Quicksort (Middle Pivot)");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private int partition(int[] array, int a, int b) {
        int i = a, j = i, m = (a+b)/2;
        
        Highlights.markArray(3, m);
        while(j < m) {
            Highlights.clearMark(1);
            Highlights.markArray(2, j);
            Delays.sleep(0.5);
            
            if(Reads.compareValues(array[j], array[m]) <= 0)
                Writes.swap(array, i++, j, 1, true, false);
            
            j++;
        }
        
        Writes.swap(array, i, m, 1, true, false);
        j = m+1;
        m = i++;
        
        Highlights.markArray(3, m);
        while(j < b) {
            Highlights.clearMark(1);
            Highlights.markArray(2, j);
            Delays.sleep(0.5);
            
            if(Reads.compareValues(array[j], array[m]) < 0)
                Writes.swap(array, i++, j, 1, true, false);
            
            j++;
        }
        
        Writes.swap(array, --i, m, 1, true, false);
        return i;
    }
    
    private void quickSort(int[] array, int a, int b, int d) {
        Writes.recordDepth(d);
        if(b-a > 1) {
            int p = this.partition(array, a, b);
            Writes.recursion();
            this.quickSort(array, a, p, d + 1);
            Writes.recursion();
            this.quickSort(array, p+1, b, d + 1);
        }
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.quickSort(array, 0, currentLength, 0);
    }
}