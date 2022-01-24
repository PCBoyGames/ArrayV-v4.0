package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.hybrid.IfBreadInFrenchIsPainThenIOwnAFuckingBakerySort;
import sorts.templates.Sort;


final public class GreenHerringSort extends Sort {
    public GreenHerringSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Green Herring");
        this.setRunAllSortsName("Green Herring Sort");
        this.setRunSortName("Green Herring Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	IfBreadInFrenchIsPainThenIOwnAFuckingBakerySort bakery =
    new IfBreadInFrenchIsPainThenIOwnAFuckingBakerySort(arrayVisualizer);
    	bakery.greenHerringPass(array, 0, currentLength);
    }
}