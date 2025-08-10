package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class ITakeHisHaySort extends Sort {
    public ITakeHisHaySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("And so I take his hay...");
        this.setRunAllSortsName("And so I take his hay... Sort");
        this.setRunSortName("And so I take his hay... Sort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    /*
        The external area is regarded as a 2N continuous band.
        The following rules apply when placing values ​​in the external area.
            If (maximum <= value), place it above the ascending order column and update the maximum.
            If (value < minimum), place it below the descending column and update the minimum.
            If (minimum <= value < maximum), place new values ​​(maximum and minimum) in ascending order column, and let the value group arranged so far be Part.
        Merge parts.
     */

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int[] desc = Writes.createExternalArray(currentLength), asc = Writes.createExternalArray(currentLength);
        int dIn = currentLength - 1, aIn = 1, min = array[0], max = array[0], j = 0;
        boolean bCon = false;
        Writes.write(asc, 0, array[0], 0, false, true);
        for (int i = 1; i < currentLength; i++) {
            Highlights.markArray(1, i);
            if (Reads.compareValues(max, array[i]) <= 0) Writes.write(asc, aIn++, max = array[i], 1, false, true);
            else if (Reads.compareValues(array[i], min) < 0) Writes.write(desc, dIn--, min = array[i], 1, false, bCon = true);
            else if (Reads.compareValues(min, array[i]) <= 0 && Reads.compareValues(array[i], max) < 0) {
                dIn++;
                while (dIn < currentLength) Writes.write(array, j++, desc[dIn++], 1, true, false);
                dIn = currentLength - 1;
                for (int k = 0; k < aIn; k++) {
                    if (bCon) Writes.write(array, j++, asc[k], 1, true, false);
                    else j++;
                }
                aIn = 1;
                Writes.write(asc, 0, array[i], 1, false, true);
                max = min = array[i];
                bCon = false;
            }
        }
        dIn++;
        while (dIn < currentLength) Writes.write(array, j++, desc[dIn++], 1, true, false);
        if (bCon) for (int k = 0; k < aIn; k++) Writes.write(array, j++, asc[k], 1, true, false);
        Writes.deleteExternalArray(desc);
        Writes.deleteExternalArray(asc);
        // And then it does some sort of merge sort.
        PDMergeSort pdm = new PDMergeSort(arrayVisualizer);
        pdm.runSort(array, currentLength, 0);
    }
}