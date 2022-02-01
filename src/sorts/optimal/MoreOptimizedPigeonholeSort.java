package sorts.optimal;

import java.math.BigInteger;
import java.util.HashMap;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.Statistics;

/**************************************
 * More Optimized Pigeonhole Sort:    *
 * Harder, better, faster, stronger   *
 * (O(n/2) worst case aux)            *
 *                                    *
 * (more than ever, hour after, our   *
 * work is never over)                *
 *                                    *
 * @author Distay                     *
 **************************************/

final public class MoreOptimizedPigeonholeSort extends Sort {
    public MoreOptimizedPigeonholeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("More Optimized Pigeonhole");
        this.setRunAllSortsName("More Optimized Pigeonhole Sort");
        this.setRunSortName("More Optimized Pigeonhole Sort");
        this.setCategory("Optimal Sorts");
        this.setComparisonBased(false);
        this.setBucketSort(false);
        this.setRadixSort(false);

        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private BigInteger bitlist;

    private boolean bitIsSet(BigInteger b, int loc) {
        return b.and(BigInteger.ZERO.setBit(loc)).signum() == 1;
    }
    private void setBit(int loc) {
        bitlist = bitlist.setBit(loc);
    }

    private int getVal(int[] array, int index) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(array[index]) : array[index];
    }

    @Override
    /*
     * VAR min: min(array) [just so that people can't add 2^20 to a 1,024 number array, and call it a worst case]
     * VAR max: max(array) [mockWrite purposes]
     */
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        Statistics.putStats("Unique", "Duplicate");
        int min = Reads.analyzeMin(array, sortLength, 0.5, true),
            max = Reads.analyzeMax(array, sortLength, 0, false);
        this.bitlist = BigInteger.ZERO; // A BigInteger to keep track of uniques
        HashMap<Integer, Integer> overflow = new HashMap<>(); // A HashMap to keep track of duplicates
        for(int i=0; i<sortLength; i++) {
            int v = getVal(array, i) - min;
            Highlights.markArray(1, i);
            Delays.sleep(1);
            if(bitIsSet(bitlist, v)) { // Duplicate number case
                Writes.mockWrite(max+1, v, 1, 0);
                overflow.put(v, overflow.getOrDefault(v, 1) + 1); // Track the dupe with the overflow map
                Statistics.addStat("Duplicate");
            } else {
                setBit(v); // Let the bit-list know that we've found a unique number
                Statistics.addStat("Unique");
            }
        }

        for(int i=0, cnt=-1, lead = 0; i<sortLength; i++) {
            if(lead == 0) // If we're tracking dupes, don't advance
                do {
                    cnt++;
                } while(!bitIsSet(bitlist, cnt));
            if(overflow.getOrDefault(cnt, 1) > 1) { // If overflow[cnt] has a dupe count greater than one,
                lead = overflow.get(cnt); // put the dupe count in lead,
                overflow.put(cnt, 1); // and reset the overflow dupe count, to avoid infinite loop.
            }
            Writes.write(array, i, cnt + min, 1, true, false);
            if(lead > 0) { // Decrement the dupe count when still operating on an overflowed bit
                lead--;
            }
        }
    }
}