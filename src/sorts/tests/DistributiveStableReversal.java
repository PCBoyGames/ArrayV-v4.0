package sorts.tests;

import java.util.ArrayList;
import java.util.HashSet;

import main.ArrayVisualizer;
import sorts.hybrid.KitaSort;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class DistributiveStableReversal extends Sort {
    public DistributiveStableReversal(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Distributive Stable Reversal");
        this.setRunAllSortsName("Distributive Stable Reversal");
        this.setRunSortName("Distributive Stable Reversal");
        this.setCategory("Tests");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public int stableReturn(int value) {
        return arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(value) : value;
    }

    public void stableReversal(int[] array, int start, int end) {

        // I have to keep track of this...
        int u = 0;

        // Distray's Tree Reversal did it last, but I'm going to do it first. Because I have to.
        Writes.reversal(array, start, end, 1, true, false);

        // Only one mark is needed for what's next.
        Highlights.clearMark(2);

        // Nothing bad so far, right? Just one simple thing?
        // Here's where I go directly to hell.
        // Yep, I'm collecting the uniques using a HashSet, and there is nothing you can do to stop me.
        HashSet<Integer> uSet = new HashSet<>();

        // HashSets allow for me to take all the uniques without dupes.
        // It *also* alows me to check if there are dupes at all. AT THE SAME TIME!
        boolean dupes = false;

        arrayVisualizer.setExtraHeading(" / Creating HashSet...");

        // Check each item in order.
        for (int i = start; i <= end; i++) {

            // Treat this as an aux write.
            Writes.auxWrites++;

            // Decorative marking position.
            Highlights.markArray(1, i);
            Delays.sleep(1);

            // And here's where that dupe check matters.
            if (!uSet.add(stableReturn(array[i]))) dupes = true;

            // But if it's a new item, increment the space.
            else {
                Writes.allocAmount++;
                u++;
            }
        }

        // If all items are unique, the reversal alone is already stable.
        // This is why I did the reversal first.
        if (!dupes) {

            // I don't wanna play with you anymore...
            // *tosses HashSet into a trashbin*
            uSet.clear();
            Writes.allocAmount -= u;

            // *tosses code after this into a trashbin*
            return;
        }

        // Here he comes, here he comes...
        ArrayList<ArrayList<Integer>> indexMap = new ArrayList<>();

        // Convert the HashSet to something comparable with an indexing function, and take its place.
        // Same space before and after, so no need to change the allocation or writes.
        // Just treat this solely as a conversion.
        Object[] uCon = uSet.toArray();
        ArrayList<Integer> uList = new ArrayList<>();

        // ...and begin converting.
        for (int i = 0; i < uSet.size(); i++) {

            // Treat the Object as an int.
            uList.add((int) uCon[i]);
        }

        // Guess what, HashSet? Yep, forget you! I have what I want now!
        uSet.clear();

        arrayVisualizer.setExtraHeading(" / Nesting Indexes...");

        // This is complicated. But it works, I guess.
        for (int i = start; i <= end; i++) {

            // Treat this as an aux write.
            Writes.auxWrites++;

            // Treat this as an allocation.
            Writes.allocAmount++;
            u++;

            // Decorative marking position.
            Highlights.markArray(1, i);
            Delays.sleep(1);

            // I think I need to do this.
            int pos = uList.indexOf(stableReturn(array[i]));

            // ...and set to add.
            while (indexMap.size() <= pos) indexMap.add(new ArrayList<>());
            indexMap.get(pos).add(i);
        }

        arrayVisualizer.setExtraHeading(" / Reversing Equals...");

        // Finally, the moment of truth!
        for (int i = 0; i < indexMap.size(); i++) {

            // Set up the left and right bounds for indexes within indexMap.get(i).
            for (int left = 0, right = indexMap.get(i).size() - 1; left < right; left++, right--) {

                // YES! YES! YEEEEEEEEEES!
                Writes.swap(array, indexMap.get(i).get(left), indexMap.get(i).get(right), 1, true, false);
            }
        }

        Writes.allocAmount -= u;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        // So, let's try the reversal.
        stableReversal(array, 0, currentLength - 1);
        // And then, let's create an algorithm instance of something stable and test it.
        arrayVisualizer.setExtraHeading(" / Testing Output...");
        KitaSort k = new KitaSort(arrayVisualizer);
        k.runSort(array, currentLength, 0);
        arrayVisualizer.setExtraHeading("");
    }
}