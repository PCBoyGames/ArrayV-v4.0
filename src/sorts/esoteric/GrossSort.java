package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.hybrid.IfBreadInFrenchIsPainThenIOwnAFuckingBakerySort;
import sorts.templates.Sort;


final public class GrossSort extends Sort {
    public GrossSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Gross");
        this.setRunAllSortsName("Gross Sort");
        this.setRunSortName("Gross Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        IfBreadInFrenchIsPainThenIOwnAFuckingBakerySort bakery =
    new IfBreadInFrenchIsPainThenIOwnAFuckingBakerySort(arrayVisualizer);
        bakery.grossPass(array, 0, currentLength, currentLength, true);
    }
}