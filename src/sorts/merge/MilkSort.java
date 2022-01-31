package sorts.merge;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;
public final class MilkSort extends Sort {
    public MilkSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Milk");
        this.setRunAllSortsName("Milk Sort");
        this.setRunSortName("Milksort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void t(int[] array, int a, int b, int len) {
        for (int i = 0; i < len; i++) {
            Writes.swap(array, a + i, b + i, 1, true, false);
        }
    }
    private void milk(int[] array, int s, int m, int e) {
        if((e-s <= 16 || s == m || m == e) && s < e) {
            InsertionSort i = new InsertionSort(arrayVisualizer);
            i.customInsertSort(array, s, e, 0.5, false);
            return;
        }
        int rz = e-m, r = m, l = s;
        while(rz > 0) {
            if(Reads.compareValues(array[l], array[r]) == 1) {
                t(array, l,r,rz);
            } else {
                l++;
                rz--;
            }
        }
        this.Milk(array, m, e);
    }
    private void Milk(int[] array, int s, int e) {
        int m = s+(e-s)/2;
        if(m==s)
            return;
        this.Milk(array,s,m);
        this.Milk(array,m,e);
        this.milk(array, s, m, e);
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        Milk(array, 0, length);
    }
}