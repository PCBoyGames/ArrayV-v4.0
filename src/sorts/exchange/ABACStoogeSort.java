package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class ABACStoogeSort extends Sort {
    public ABACStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("ABAC Stooge");
        this.setRunAllSortsName("ABAC Stooge Sort");
        this.setRunSortName("ABAC Stoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(384);
        this.setBogoSort(false);
    }

    public int getDigit(int a, int power, int radix) { // please implement a raw digit getter
        return (int) (a / Math.pow(radix, power)) % radix;
    }

    private void stoogeSort(int[] A, int i, int j, boolean base) {
        int cube = (j-i);
        cube=cube*cube*cube;
        for(int k=0; k<cube; k++) {
            int a = i, b = j;
            for(int l = (b-a+1)/3, o = 0; ; l=(b-a+1)/3) {
                switch(getDigit(k, o++, 3)) {
                    case 0: case 2:
                        b -= l; break;
                    case 1:
                        a += l; break;
                }
                if((b - a == 1 && l == 1) || l < 1)
                    break;
            }
            if(Reads.compareIndices(A, a, b, 0.5, true)>0)
                Writes.swap(A, a, b, 2.5, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.stoogeSort(array, 0, currentLength - 1, false);
    }
}