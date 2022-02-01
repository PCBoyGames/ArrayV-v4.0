package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;


final public class OutOfPlacePopSort extends Sort {
    public OutOfPlacePopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("OOP Pop");
        this.setRunAllSortsName("Out-Of-Place Pop Sort");
        this.setRunSortName("OOP Popsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void bitonicMerge(int[] array, int[] to, int start, int mid, int end, int dir) {
        dir *= -1; // what
        int a = start, b = end-1, c = dir == 1 ? start : end - 1;
        while(a < mid && b >= mid) {
            if(Reads.compareValues(array[a], array[b]) == 1) {
                Writes.write(to, c, array[a++], 2, true, false);
            } else {
                Writes.write(to, c, array[b--], 2, true, false);
            }
            c += dir;
        }
        while(a < mid) {
            Writes.write(to, c, array[a++], 2, true, false);
            c += dir;
        }
        while(b >= mid) {
            Writes.write(to, c, array[b--], 2, true, false);
            c += dir;
        }
    }

    private void oopSelection(int[] array, int[] tmp, int start, int end, int arrayDir) {
        if(arrayDir == 0) {
            for(int i=start; i<end-1; i++) {
                int min=i;
                for(int j=i+1; j<end; j++) {
                    if(Reads.compareValues(array[j], array[min]) == -1) {
                        min = j;
                    }
                }
                if(min != i)
                    Writes.swap(array, i, min, 0.1, true, false);
            }
        } else {
            int nop = Integer.MAX_VALUE, d = arrayDir / Math.abs(arrayDir),
                pt = d == 1 ? start : end-1;
            for(int i=start; i<end; i++) {
                if(array[i] < nop) {
                    nop = array[i];
                }
            }
            nop--;
            int w = start;
            for(int j=0; j<end-start; j++) {
                while(w < end && array[w] == nop)
                    w++;
                if(w == end)
                    break;
                int min = w;
                for(int i=w+1; i<end; i++) {
                    if(array[i] == nop)
                        continue;
                    if(Reads.compareIndices(array, i, min, 0.05, true) < 0) {
                        min = i;
                    }
                }
                int minVal = array[min];
                Writes.write(array, min, nop, 1, true, false);
                Writes.write(tmp, pt, minVal, 1, true, true);
                pt += d;
            }
        }
    }

    private void bubblePop(int[] array, int start, int end, int comp) {
        int swap = end;
        while(swap > start) {
            int lastSwap = start;
            for(int i=start; i<swap-1; i++) {
                if(Reads.compareValues(array[i], array[i+1]) == comp) {
                    Writes.swap(array, i, i+1, 0.025, true, false);
                    lastSwap = i+1;
                } else if(lastSwap > start)
                    break;
            }
            swap = lastSwap;
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int quart = (length + 1) / 4,
            half = (length + 1) / 2;
        int[] tmp = Writes.createExternalArray(length);
        oopSelection(array, tmp, 0, quart, -1);
        oopSelection(array, tmp, quart, half, 1);
        oopSelection(array, tmp, half, length-quart, -1);
        oopSelection(array, tmp, length-quart, length, 1);
        bitonicMerge(tmp, array, 0, quart, half, -1);
        bitonicMerge(tmp, array, half, length-quart, length, 1);
        Writes.deleteExternalArray(tmp);
        bubblePop(array, 0, length, 1);
    }
}