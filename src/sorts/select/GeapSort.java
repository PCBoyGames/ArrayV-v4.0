package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.HeapSorting;

final public class GeapSort extends HeapSorting {
    public GeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Geap");
        this.setRunAllSortsName("Geap Sort");
        this.setRunSortName("Geapsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void makeGeap(int[] array, int start, int end, double sleep) {
        for(int i=end-1; i>start; i--) {
            int j=i;
            while(j >= start) {
                int next = ((j+start-2) / 2),
                    child = ((j+start) / 2);
                if(child >= start && next >= start && Reads.compareValues(array[child], array[next]) == 1)
                    Writes.swap(array, child, next, sleep, true, false);
                if(next >= start && Reads.compareValues(array[next], array[j]) == -1)
                    Writes.swap(array, j, child, sleep, true, false);
                j = next;
            }
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        this.makeGeap(array, 0, length, 2);
        for(int i=length-1; i>=0; i--) {
            Writes.swap(array, 0, --length, 1, true, false);
            this.makeGeap(array, 0, length, 2);
        }
    }
}