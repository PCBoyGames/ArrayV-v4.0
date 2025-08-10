package sorts.tests;

import frames.ArrayFrame;
import main.ArrayManager;
import main.ArrayVisualizer;
import sorts.templates.Sort;

import java.util.Arrays;

// REPLACE THIS IMPORT WITH THE ALGORITHM TO TEST
import sorts.hybrid.BismuthSort;

public class ForceLengths extends Sort {

    // REPLACE THIS SET WITH THE ALGORITHM TO TEST
    BismuthSort sort = new BismuthSort(arrayVisualizer);

    public ForceLengths(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Force Lengths");
        this.setRunAllSortsName("Force Lengths");
        this.setRunSortName("Force Lengths");
        this.setCategory("Tests");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        ArrayManager arman = arrayVisualizer.getArrayManager();
        ArrayFrame arfram = arrayVisualizer.getArrayFrame();
        for (int i = 2; i <= currentLength; i++) {
            arfram.setLengthSlider(i);
            arman.shuffleArray(array, arrayVisualizer.getCurrentLength(), arrayVisualizer);
            int[] sorted = new int[i];
            for (int j = 0; j < i; j++) sorted[j] = array[j];
            Arrays.sort(sorted);
            sort.runSort(array, i, 0);
            for (int j = 0; j < i; j++) if (array[j] != sorted[j]) return;
        }
    }
}