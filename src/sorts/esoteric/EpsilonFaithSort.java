package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public final class EpsilonFaithSort extends BogoSorting {
    public EpsilonFaithSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Epsilon Faith");
        this.setRunAllSortsName("Epsilon Faith Sort");
        this.setRunSortName("\u03B5 Faithsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(true);
    }
    
    private void epsiFaith(int[] array) {
        Delays.sleep(1);
        for(int i=0; i<array.length; i++) // yep
            epsiFaith(array); //lol stackoverflow DX: Director's Cut
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        // one upgrade does wonders
        do
            epsiFaith(array);
        while(!isArraySorted(array, length));
    }
}
