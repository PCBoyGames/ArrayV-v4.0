package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
import sorts.merge.RotateMergeSort;
import sorts.templates.Sort;
import utils.ArrayVList;

// Image Sorting: Chaos
final public class ImageSort extends Sort {
    public ImageSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Image");
        this.setRunAllSortsName("Image Sort");
        this.setRunSortName("Imagesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void multiSwap(int[] array, int a, int b, int s) {
        for(int i=0; i<s; i++) {
            Writes.swap(array,a+i,b+i,1,true,false);
        }
    }

    protected int remoteMerge(int[] array, int a, int b, int c, int buf) {
        this.multiSwap(array, a, buf, b-a);
        int i = buf, j = b, d = buf+(b-a), k = a;
        while(j < c && i < d) {
            if(Reads.compareValues(array[i], array[j]) == 1) {
                Writes.swap(array, j, k, 1d, true, false);
                j++;
            } else {
                Writes.swap(array, i, k, 1d, true, false);
                i++;
            }
            k++;
        }

        this.multiSwap(array, k, i, d-i);

        return c-k;
    }

    public boolean imageSeek(int[] array, int start, int end) {
        ArrayVList z = Writes.createArrayList();
        for(int i=start; i<end; i++) {
            int q=1, c = 1; // c
            for(int j=i+1; j<end; j++) {
                int w = Reads.compareValues(array[j], array[i]);
                if(w == c || w == 0){
                    Writes.swap(array, j, ++i, 1, true, false);
                    q++;
                }
            }
            if(c == -1)
                Writes.reversal(array, i-q+1, i, 1, true, false);
            Writes.arrayListAdd(z, q);
        }

        Writes.deleteArrayList(z);
        BinaryInsertionSort remotePass = new BinaryInsertionSort(arrayVisualizer);

        if(z.size() < 3) {
            RotateMergeSort r = new RotateMergeSort(arrayVisualizer);
            r.rotateMerge(array,start,start+z.get(0),end);
            return true;
        }

        int maxIndex = 0, maxLoc = start;
        for(int i=1; i<z.size(); i++) {
            if(z.get(maxIndex) < z.get(i)) {
                maxIndex = i;
            }
        }

        if(maxIndex == 1 && z.size() == 3) {
            RotateMergeSort r = new RotateMergeSort(arrayVisualizer);

            int a = start+z.get(0),
                b = a+z.get(1);
            r.rotateMerge(array,start,a,b);
            r.rotateMerge(array,start,b,end);
            return true;
        }
        for(int i=0; i<maxIndex; i++) {
            maxLoc+=z.get(i);
        }
        for(int i=0; i<z.size(); i+=2) {
            if(i+1 != maxIndex && i != maxIndex && i+1 < z.size()) {
                int ml = start, mm = start, mh = start;
                for(int j=0; j<i; j++) {
                    ml+=z.get(j);
                    mm+=z.get(j);
                    mh+=z.get(j);
                }
                mm+=z.get(i);
                mh+=z.get(i)+z.get(i+1);

                this.remoteMerge(array, ml, mm, mh, maxLoc);
            } else
                i--;
        }

        if(maxIndex == z.size()-1)
            remotePass.customBinaryInsert(array, maxLoc, end, 0.25);
        else
            remotePass.customBinaryInsert(array, maxLoc, maxLoc+z.get(maxIndex+1), 0.25);

        return false;
    }
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        while(!this.imageSeek(array, 0, sortLength));
    }
}