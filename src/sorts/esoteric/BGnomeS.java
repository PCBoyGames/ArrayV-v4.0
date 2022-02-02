package sorts.esoteric;
import main.ArrayVisualizer;
import sorts.templates.Sort;

public class BGnomeS extends Sort {
    public BGnomeS(ArrayVisualizer aV){
        super(aV);
        this.setSortListName("B-Gnome");
        this.setRunAllSortsName("Type-B Gnome Sort");
        this.setRunSortName("Gnome Sort (Type-B)");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }
    private void G(int[] array, int e){
        for(int i = e; i > 0; i--){
            Highlights.markArray(1, i);
            if(Reads.compareValues(array[i], array[i-1]) == -1) {
                Writes.swap(array, i, i-1, 1, true, false);
            }
            Delays.sleep(0.02);
        }
    }
    private void BG(int[] array, int ms, int length){
        int hi=ms;
        G(array, ms);
        while(hi < length){
            while(ms < length && Reads.compareValues(array[ms], array[ms+1]) == -1){
                G(array, ms);
                ms++;
                Highlights.markArray(2, hi);
                Delays.sleep(0.001);
                if(ms > hi){
                    hi=ms;
                }
            }
            Writes.swap(array, ms, ms+1, 1, true, false);
            ms=1;
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    BG(array, 1, length);
    }
}