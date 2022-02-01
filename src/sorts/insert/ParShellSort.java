package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class ParShellSort extends Sort {
    public ParShellSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("mg-2018's Par Shell");
        this.setRunAllSortsName("mg-2018's Par(X) Shell Sort");
        this.setRunSortName("mg-2018's Par(X) Shellsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // thx to Control for sharing the code for finding Par(X) in O(n) time
    int par(int[] array, int len) {
        boolean[] max = new boolean[len];
        int maximum = array[0];
        for(int i=1; i<len; i++) {
            if(array[i] > maximum) {
                maximum = array[i];
                max[i] = true;
            }
        }

        int i = len-1;
        int p = 1;
        int j = len-1;
        while(j >= 0 && i >= p) {
            while(!max[j] && j > 0) j--;
            maximum = array[j];

            while(maximum <= array[i] && i >= p) i--;
            if(array[j] > array[i] && p < i - j) p = i - j;
            j--;
        }

        return p;
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        InsertionSort ins = new InsertionSort(this.arrayVisualizer);

        while(true) {
            int par = this.par(array, sortLength);
            Highlights.markArray(3, par);

            double exactParDiv2 = (double)par/2.0;
            if(par/2 <= 1) {
                arrayVisualizer.setExtraHeading(" / Current Par(X): " + par);
                Highlights.clearMark(2);
                ins.customInsertSort(array, 0, sortLength, 1, false);
                break;
            }

            arrayVisualizer.setExtraHeading(" / Current Par(X): " + par);
            for(int i=0; i < ((par%2==0) ? par/2 : par/2+1); i++) {
                for(int j=1; i+(int)(exactParDiv2*j) < sortLength; j++) {
                    int k = j;
                    int temp = array[i+(int)(exactParDiv2*j)];

                    while(k > 0 && Reads.compareValues(array[i+(int)(exactParDiv2*(k-1))], temp) > 0) {
                        Writes.write(array, i+(int)(exactParDiv2*k), array[i+(int)(exactParDiv2*(k-1))], 0, false, false);
                        Highlights.markArray(1, i+(int)(exactParDiv2*(k-1)));
                        Highlights.markArray(2, i+(int)(exactParDiv2*k));
                        Delays.sleep(0.5);
                        k--;
                    }

                    Writes.write(array, i+(int)(exactParDiv2*k), temp, 0, false, false);

                    int finalMarkPos = (k==0) ? i : i+(int)(exactParDiv2*(k-1));
                    Highlights.markArray(1, finalMarkPos);
                    Highlights.markArray(2, i+(int)(exactParDiv2*k));
                    Delays.sleep(0.5);
                }
            }
        }

        arrayVisualizer.setExtraHeading("");
    }
}
