package sorts.quick;

import main.ArrayVisualizer;
import sorts.select.SmoothSort;
import sorts.templates.Sort;

final public class OrpicSort extends Sort {
    public OrpicSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Orpic");
        this.setRunAllSortsName("Orpic Sort");
        this.setRunSortName("Orpicsort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setQuestion("Set the magnitude for this sort (input/1000):", 500);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private double threshold = 0.57D;

    private SmoothSort small;

    private void sort3(int[] array, int a, int b, int c) {
        if(Reads.compareIndices(array, a, b, 0.5, true)>0)
            Writes.swap(array, a, b, 2.5, true, false);
        if(Reads.compareIndices(array, b, c, 0.5, true)>0) {
            Writes.swap(array, b, c, 2.5, true, false);
            if(Reads.compareIndices(array, a, b, 0.5, true)>0)
                Writes.swap(array, a, b, 2.5, true, false);
        }
    }

    // Medians of 3 until there's nothing to median
    private void partition(int[] a, int p, int r, int power) {
        int medianGap, medianSize = 1, pow = power,
            maxToler = (int) Math.pow(r-p+1, threshold);
        while(pow-- > 0) {
            medianSize *= 3;
        }
        medianGap = (r-p+1) / medianSize;
        if(medianGap < 1) {
            this.small.smoothSort(a, p, r, true);
            return;
        }
        if(medianGap > 1)
            for(int i=p+medianGap, j=p+1; i<=r; i+=medianGap) {
                Writes.swap(a, i, j++, 2.5, true, false);
            }
        for(int j=0; j<power; j++) {
            for(int i=p, k=p; i<p+medianSize-2; i+=3, k++) {
                sort3(a, i, i+1, i+2);
                Writes.swap(a, i+1, k, 2.5, true, false);
            }
            medianSize = (medianSize + 2) / 3;
        }

        int piv = a[p], i = p, j = r,
            iters = 0, itersnone = 0;

        while(i <= j) {
            while(iters <= maxToler && Reads.compareValues(a[i], piv) < 0) {
                Highlights.markArray(2, i);
                i++;
                iters++;
                itersnone = 0;
            }
            if(iters + itersnone > maxToler && i <= j) {
                this.partition(a, p, r, power+1);
                return;
            } else {
                iters = 0;
                itersnone++;
            }
            while(iters <= maxToler && Reads.compareValues(a[j], piv) > 0) {
                Highlights.markArray(3, j);
                j--;
                iters++;
                itersnone = 0;
            }
            if(iters + itersnone > maxToler && i <= j) {
                this.partition(a, p, r, power+1);
                return;
            } else {
                iters = 0;
                itersnone++;
            }
            if(i <= j) {
                Writes.swap(a, i++, j--, 1, true, false);
            }
        }
        int m = Math.max(power-1, 1);
        this.partition(a, p, j, m);
        this.partition(a, i, r, m);
    }

    @Override
    public int validateAnswer(int value) {
        if(value < 1)
            return 1;
        if(value > 999)
            return 1000;
        return value;
    }

    @Override
    public void runSort(int[] array, int currentLength, int t) {
        this.small = new SmoothSort(arrayVisualizer);
        this.threshold = t / 1000d;
        arrayVisualizer.setExtraHeading(String.format(", Magnitude %,d", t));
        this.partition(array, 0, currentLength - 1, 1);
        arrayVisualizer.setExtraHeading("");
    }
}