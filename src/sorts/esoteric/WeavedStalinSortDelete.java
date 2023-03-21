package sorts.esoteric;
import java.math.BigInteger;

import main.ArrayVisualizer;
import sorts.templates.Sort;

// Another cursed idea from Mememan
public class WeavedStalinSortDelete extends Sort {
    public WeavedStalinSortDelete(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Weaved Stalin (Deletion)");
        this.setRunAllSortsName("Weaved Stalin Sort (Elements Deleted)");
        this.setRunSortName("Deletion Weaved Stalin Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    // low complexity, high cost
    private int loprime(int n) {
        BigInteger p = BigInteger.ONE, j = p.add(p); // BigInteger.TWO doesn't exist to Eclipse, apparently
        int i=1;
        for (; i<n; i++, p=p.multiply(j), j=j.add(BigInteger.ONE)) {
            if ((p.add(BigInteger.ONE).remainder(j)).equals(BigInteger.ZERO)) {
                if (n%i==0) return i;
            }
        }
        return n;
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
        int g=sortLength, flen = 1;
        while (g>1) {
            g /= loprime2(g);
            for (int j=0; j<g; j++) {
                int i1=j;
                for (int i=j+g; i<sortLength; i+=g) {
                    if (Reads.compareIndices(array, i1, i, 1, true) <= 0) {
                        Writes.write(array, i1+=g, array[i], 1, true, false);
                    }
                    if (i1 < i) {
                        Writes.write(array, i, -1, 1, true, false);
                    }
                }
                flen=i1+1;
            }
        }
        arrayVisualizer.setCurrentLength(flen);
    }
}