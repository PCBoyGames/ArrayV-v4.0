package sorts.tests;

import java.util.Comparator;
import java.util.Random;
import java.util.TreeSet;

import main.ArrayVisualizer;
import sorts.hybrid.EctaSort;
import sorts.templates.Sort;

/*

Coded for ArrayV by Haruki
extending code by PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

O(n + n log u) time average, O(n + u) space average.

 */

/**
 * @author Haruki
 * @author PCBoy
 * 
 */
public class UBSSHaruki extends Sort {

    public UBSSHaruki(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Haruki's Unique-Based Stable Shuffle");
        this.setRunAllSortsName("Haruki's Unique-Based Stable Shuffle");
        this.setRunSortName("Haruki's Unique-Based Stable Shuffle");
        this.setCategory("Tests");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    Random rng = new Random();
    
    int leftBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) <= 0) b = m;
            else a = m + 1;
        }
        return a;
    }
    
    public void shuffleEasy(int[] array, int a, int b) {
        for(int j = a + 1; j < b; j++)
            for(int i = j, rIdx = a + rng.nextInt(j - a + 1); i > rIdx; i--)
                if(Reads.compareIndices(array, i-1, i, 0.5, true) != 0)
                    Writes.swap(array, i-1, i, 0.5, false, false);
        Highlights.clearMark(2);
    }
    
    /**
     * Shuffles the range {@code [start, end)} of {@code array} without changing the
     * order of equal elements.
     * 
     * @param array the array
     * @param start the start of the range, inclusive
     * @param end   the end of the range, exclusive
     */
    public void stableShuffle(int[] array, int start, int end) {
        int len = end - start;
        
        // If the length is small, an O(n^2) version can be used instead.
        if (len <= 16) {
            shuffleEasy(array, start, end);
            return;
        }
        
        // TreeSets allow for me to take all the uniques without dupes.
        // It *also* allows me to check if there are dupes at all. AT THE SAME TIME!
        TreeSet<Integer> uSet = new TreeSet<>(new Comparator<Integer>(){
            @Override
            public int compare(Integer lhs, Integer rhs) {
                return Reads.compareValues(lhs, rhs);
            }
        });
        
        // u: the number of unique items found
        int u = 0;
        arrayVisualizer.setExtraHeading(" / Collecting uniques...");
        
        for (int i = start; i < end; i++) {
            
            // Treat this as an aux write.
            Writes.changeAuxWrites(1);
            
            // Decorative marking position.
            Highlights.markArray(1, i);
            Delays.sleep(1);
            
            // And here's where that dupe check matters. If it's a new item, increment the
            // space.
            if (uSet.add(array[i])) {
                Writes.changeAllocAmount(1);
                u++;
            }
        }
        
        // Convert the TreeSet to something comparable with a binary search, and take
        // its place.
        // Same space before and after, so no need to change the allocation or writes.
        // Just treat this solely as a conversion.
        int[] uniques = Writes.createExternalArray(u);
        int idx = 0;
        for (int val : uSet)
            Writes.write(uniques, idx++, val, 0, false, true);
        
        // Guess what, TreeSet? Yep, forget you! I have what I want now!
        uSet.clear();
        
        int[] buf = Writes.createExternalArray(len);
        int[] locs = Writes.createExternalArray(len);
        int[] ptrs = new int[u + 1];
        Writes.changeAllocAmount(u + 1);
        
        arrayVisualizer.setExtraHeading(" / Grouping elements...");
        for (int i = start; i < end; i++) {
            
            // Decorative marking position.
            Highlights.markArray(1, i);
            
            // Search where the item corresponds to a unique element the the uniques array
            // and save it.
            idx = leftBinSearch(uniques, 0, u, array[i]);
            Writes.write(ptrs, idx, ptrs[idx] + 1, 0, false, true);
            Writes.write(locs, i - start, idx, 1, false, true);
        }
        Highlights.clearMark(2);
        
        for (int i = 1; i <= u; i++) // Do a prefix sum to find locations
            Writes.write(ptrs, i, ptrs[i] + ptrs[i - 1], 0, false, true);
        
        for (int i = len - 1; i >= 0; i--) {
            Highlights.markArray(1, start + i);
            Writes.write(ptrs, locs[i], ptrs[locs[i]] - 1, 0, false, true);
            Writes.write(buf, ptrs[locs[i]], array[start + i], 1, false, true);
        }
        
        arrayVisualizer.setExtraHeading(" / Selecting elements...");
        /*
         * Note: The loops for shuffling and the loop for transporting elements back to
         * the main array are conjoined together to reduce the number of passes.
         */
        for (int i = 0; i < len; i++) {
            // I think I need to do this.
            int j = rng.nextInt(len - i) + i;
            if (i != j) Writes.swap(locs, i, j, 0, false, true);
            
            // We know what item it picked. Use it.
            Writes.write(array, start + i, buf[ptrs[locs[i]]], 0, true, false);
            
            // And now, increment the index of the duplicate we want to use.
            Writes.write(ptrs, locs[i], ptrs[locs[i]] + 1, 1, false, true);
        }
        Writes.changeAllocAmount(-(2 * u + 1));
        Writes.deleteExternalArrays(buf, locs, uniques);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        // So, let's try the shuffle.
        stableShuffle(array, 0, sortLength);
        // And then, let's create an algorithm instance of something stable and test it.
        arrayVisualizer.setExtraHeading(" / Testing Output...");
        EctaSort e = new EctaSort(arrayVisualizer);
        e.runSort(array, sortLength, bucketCount);
        arrayVisualizer.setExtraHeading("");

    }

}
