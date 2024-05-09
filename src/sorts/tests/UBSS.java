package sorts.tests;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.hybrid.KitaSort;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

O(n + n * u) time average, O(u) space average.

*/
public class UBSS extends MadhouseTools {
    public UBSS(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Unique-Based Stable Shuffle");
        this.setRunAllSortsName("Unique-Based Stable Shuffle");
        this.setRunSortName("Unique-Based Stable Shuffle");
        this.setCategory("Tests");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // Stability Check needs this.
    // It returns the first value that matches the input's stableReturn.
    protected int stableReturnFirst(int val) {
        for (int i = 0; ; i++) if (stableReturn(val) == stableReturn(i)) return i;
    }

    public void stableShuffle(int[] array, int start, int end) {

        // I have to keep track of this...
        int u = 0;

        // Create the tag sets, one for the values, another for the counts.
        ArrayList<Integer> tagV = new ArrayList<>();
        ArrayList<Integer> tagC = new ArrayList<>();

        // ArrayLists allow for me to take all the uniques without dupes.
        // It *also* alows me to check if there are dupes at all. AT THE SAME TIME!
        boolean dupes = false;

        arrayVisualizer.setExtraHeading(" / Creating ArrayLists...");

        // Check each item in order.
        for (int i = start; i <= end; i++) {

            // Treat this as an aux write.
            Writes.auxWrites++;

            // Decorative marking position.
            Highlights.markArray(1, i);
            Delays.sleep(1);

            // And here's where that dupe check matters.
            if (tagV.contains(stableReturnFirst(array[i]))) {

                // Overcomplicated, but whatever.
                dupes = true;
                int idx = tagV.indexOf(stableReturnFirst(array[i]));
                int val = tagC.get(idx) + 1;
                tagC.set(idx, val);

            // Add a new item in.
            } else {
                Writes.allocAmount += 2;
                u++;
                Writes.auxWrites++;
                tagV.add(array[i]);
                tagC.add(1);
            }
        }

        // If all items are unique, the basic O(n) shuffle alone is already stable.
        if (!dupes) {

            // I don't wanna play with you anymore...
            // *tosses ArrayLists into a trashbin*
            tagV.clear();
            tagC.clear();
            Writes.allocAmount -= 2 * u;

            // *tosses deck of cards in the air anyway*
            shuffleArray(array, start, end + 1, 1, true, false);

            // *tosses code after this into a trashbin*
            return;
        }

        arrayVisualizer.setExtraHeading(" / Selecting items...");

        // Now that we have what we want, time to shuffle!
        for (int i = start, sum = end - start; i <= end; i++, sum--) {

            // I'm very hungry!
            // Give me the randomizer.
            int rand = sum > 1 ? randInt(1, sum + 1) : 1;

            // This running sum introduces a value-based bias to the randomizer.
            // Oddly enough, this biasing makes the shuffle *unbiased*.
            int runSum = tagC.get(0);
            int runIdx = 0;
            while (runSum < rand) {
                runIdx++;
                runSum += tagC.get(runIdx);
            }

            // We know what item it picked. Use it.
            Writes.write(array, i, tagV.get(runIdx), 1, true, false);

            // And now, decrement the value in the count tags.
            Writes.auxWrites++;
            tagC.set(runIdx, tagC.get(runIdx) - 1);

            // But ArrayV, at least in Stability Check mode, has a problem.
            // The value would need to change with the instance.
            // This should account for that.
            // I'm not counting this as an aux write, though it technically is.
            if (arrayVisualizer.doingStabilityCheck()) {
                tagV.set(runIdx, tagV.get(runIdx) + 1);
            }
        }

        // Forgot to clear this out a moment ago. Ouch.
        tagV.clear();
        tagC.clear();
        Writes.allocAmount -= 2 * u;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        // So, let's try the shuffle.
        stableShuffle(array, 0, currentLength - 1);
        // And then, let's create an algorithm instance of something stable and test it.
        arrayVisualizer.setExtraHeading(" / Testing Output...");
        KitaSort k = new KitaSort(arrayVisualizer);
        k.runSort(array, currentLength, 0);
        arrayVisualizer.setExtraHeading("");
    }
}