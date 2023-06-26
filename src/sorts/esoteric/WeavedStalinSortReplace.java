package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.Sort;

// Another cursed idea from Mememan
public class WeavedStalinSortReplace extends Sort {
    public WeavedStalinSortReplace(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Weaved Stalin (Replacement)");
        this.setRunAllSortsName("Weaved Stalin Sort (Elements Replaced)");
        this.setRunSortName("Replacement Weaved Stalin Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // high complexity, low cost
    private int loprime2(int n) {
        int[] p = new int[n];
        pchk:
        for (int i=2, l=0; i<n; i++) {
            for (int j=0; j<l; j++) {
                if (i%p[j]>0) continue pchk;
            }
            if (n%i == 0) return i;
            p[l++] = i;
        }
        return n;
    }
    @Override
    public void runSort(int[] array, int sortLength, int bucketLength) {
        int g=sortLength;
        while (g>1) {
            g /= loprime2(g);
            for (int j=0; j<g; j++) {
                for (int i=j+g, i1=j; i<sortLength; i+=g) {
                    if (Reads.compareIndices(array, i1, i, 1, true) <= 0) {
                        i1=i;
                    } else
                        Writes.write(array, i, array[i1], 1, true, false);
                }
            }
        }
    }
}