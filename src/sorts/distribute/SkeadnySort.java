package sorts.distribute;

import main.ArrayVisualizer;
import sorts.exchange.SnowballSort;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class SkeadnySort extends Sort {
    public SkeadnySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Skeadny");
        this.setRunAllSortsName("Skeadny Sort");
        this.setRunSortName("Skeadny Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2048);
        this.setBogoSort(false);
    }
    
    protected int stablereturn(int a) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(a) : a;
    }
    
    protected void handleflip(int[] array, int a, int b, double times) {
        if (b - a > 2) Writes.reversal(array, a, b, 0.25 / times, true, false);
        else Writes.swap(array, a, b, 0.25 / times, true, false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 0; i < currentLength; i++) {
            boolean doflip = true;
            Highlights.clearAllMarks();
            Highlights.markArray(1, i);
            Delays.sleep(0.25);
            int cmp = Reads.compareValues(stablereturn(array[i]), i);
            double times = 1;
            while (cmp != 0 && doflip) {
                if (cmp < 0) {
                    doflip = false;
                    Highlights.clearAllMarks();
                    Highlights.markArray(1, i);
                    arrayVisualizer.setExtraHeading(" / array[i] < i! Fallback!");
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                }
                if (doflip) {
                    if (Reads.compareValues(stablereturn(array[i]), stablereturn(array[stablereturn(array[i])])) == 0) {
                        doflip = false;
                        Highlights.clearAllMarks();
                        Highlights.markArray(1, i);
                        Highlights.markArray(2, stablereturn(array[i]));
                        arrayVisualizer.setExtraHeading(" / array[i] == array[array[i]]! Fallback!");
                        try { Thread.sleep(1000); } catch (InterruptedException e) {}
                    }
                    if (doflip) {
                        handleflip(array, i, stablereturn(array[i]), times);
                        cmp = Reads.compareValues(stablereturn(array[i]), i);
                        times += 0.1;
                    }
                }
            }
            // A saving grace for non-conforming inputs!
            if (!doflip) {
                SnowballSort failsafe = new SnowballSort(arrayVisualizer);
                failsafe.runSort(array, currentLength, 0);
                break;
            }
        }
        arrayVisualizer.setExtraHeading("");
    }
}