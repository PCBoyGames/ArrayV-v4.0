package sorts.templates;

import main.ArrayVisualizer;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

All resource files (except lengths 2 - 6) are filled with perfect sorter
networks in credit to Bert Dobbelaere. Lengths 2 - 6 are in credit to Lord
Control over on the Musicombo Discord.

https://bertdobbelaere.github.io/sorting_networks_extended.html
https://cdn.discordapp.com/attachments/901280001771601930/944383076044259409/5nd515.png

*/
public abstract class BestForNSorting extends Sort {
    protected BestForNSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    public double delay = 0.5;

    public void cs(int[] array, int s, int a, int b) {
        if (Reads.compareIndices(array, s + a, s + b, delay, true) > 0) Writes.swap(array, s + a, s + b, delay, true, false);
    }

    public void initNetwork(int[] array, int s, int l) {
        if (l < 65) {
            if (l < 39) for (int i = 0; i < BestForNSortingRes1.best1[l].length; i++) cs(array, s, BestForNSortingRes1.best1[l][i][0], BestForNSortingRes1.best1[l][i][1]);
            else if (l < 50) for (int i = 0; i < BestForNSortingRes2.best2[l - 39].length; i++) cs(array, s, BestForNSortingRes2.best2[l - 39][i][0], BestForNSortingRes2.best2[l - 39][i][1]);
            else if (l < 58) for (int i = 0; i < BestForNSortingRes3.best3[l - 50].length; i++) cs(array, s, BestForNSortingRes3.best3[l - 50][i][0], BestForNSortingRes3.best3[l - 50][i][1]);
            else if (l < 65) for (int i = 0; i < BestForNSortingRes4.best4[l - 58].length; i++) cs(array, s, BestForNSortingRes4.best4[l - 58][i][0], BestForNSortingRes4.best4[l - 58][i][1]);
        }
        else {
            System.err.println("BestForN only supports lengths up to 64.");
            return;
        }
    }
}