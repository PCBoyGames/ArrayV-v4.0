package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/**
 * @author Ayako-chan
 *
 */
public class PancakeBogoSort extends BogoSorting {

    public PancakeBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Pancake Bogo");
        setRunAllSortsName("Pancake Bogo Sort");
        setRunSortName("Pancake Bogosort");
        setCategory("Bogo Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(true);
        setUnreasonableLimit(512);
        setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = length - 1; i >= 0; i--)
            while (!isMaxSorted(array, 0, i + 1))
                Writes.reversal(array, 0, BogoSorting.randInt(0, i + 1), delay, true, false);

    }

}
