package sorts.esoteric;
import main.ArrayVisualizer;
import sorts.templates.Sort;

public class BeatingADeadSort extends Sort {
    public BeatingADeadSort(ArrayVisualizer aV){
        super(aV);
        this.setSortListName("Beating A Dead");
        this.setRunAllSortsName("Beating A Dead Sort");
        this.setRunSortName("Beating A Dead Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }
    // Dead "Dead Gnome"
    private void DDG(int[] array, int e){
        if(e <= 0)
            return;
        this.DDG(array, e-1);
        for(int i = e; i > 0; i--){
            Highlights.markArray(1, i);
            if(Reads.compareValues(array[i], array[i-1]) == -1) {
                Writes.swap(array, i, i-1, 1, true, false);
            } else
                this.DDG(array, e-1);
            this.DDG(array, i-1);
            Delays.sleep(0.02);
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        DDG(array, length);
    }
}