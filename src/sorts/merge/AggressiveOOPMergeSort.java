package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class AggressiveOOPMergeSort extends Sort {

    public AggressiveOOPMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Merge (Aggressive, Out-of-Place)");
        this.setRunAllSortsName("Merge Sort (Aggressive, Out-of-Place)");
        this.setRunSortName("Merge Sort (Aggressive, Out-of-Place)");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int[] mergeSortAggOOP_(int[] arr, int l, int h, int depth) {
        Writes.recordDepth(depth);
        int m = (l + h) / 2;

        if (l >= m) {
            if (depth != 0) {
                if (h > l) {
                    int[] tmp = new int[h];
                    Writes.write(tmp, 0, arr[l], 0.5, true, true);
                    return tmp;
                } else {
                    return new int[0];
                }
            } else {
                return null;
            }
        }
        int[] arr_l = Writes.createExternalArray(m - l);
        Writes.recursion();
        arr_l = mergeSortAggOOP_(arr, l, m, depth + 1);
        int[] arr_h = Writes.createExternalArray(h - m);
        Writes.recursion();
        arr_h = mergeSortAggOOP_(arr, m, h, depth + 1);

        int[] arr_to = arr;
        if (depth != 0) {
            arr_to = new int[truncate(arr_l.length + arr_h.length)];
        }

        int[] tmp = arr_to;

        int l_ = 0;
        int h_ = 0;

        int loc = 0;
        int tmp_l = arr_l[l_];
        int tmp_h = 0;
        boolean last = false;

        while (l_ < truncate(arr_l.length) && h_ < truncate(arr_h.length)) {
            if (last) {
                tmp_l = arr_l[l_];
            } else {
                tmp_h = arr_h[h_];
            }

            last = tmp_l < tmp_h;
            if (last) {
                Writes.write(tmp, loc, tmp_l, 0.5, true, true);
                l_++;
            } else {
                Writes.write(tmp, loc, tmp_h, 0.5, true, true);
                h_++;
            }
            loc++;
        }

        while (l_ < truncate(arr_l.length)) {
            Writes.write(tmp, loc, arr_l[l_], 0.5, true, true);
            loc++;
            l_++;
        }

        while (h_ < truncate(arr_h.length)) {
            Writes.write(tmp, loc, arr_h[h_], 0.5, true, true);
            loc++;
            h_++;
        }

        Writes.deleteExternalArrays(arr_l, arr_h);
        return tmp;
    }

    private int truncate(int length) {
        int tmplen = length;
        if (tmplen > arrayVisualizer.getCurrentLength())
            tmplen = arrayVisualizer.getCurrentLength();
        return tmplen;
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        mergeSortAggOOP_(array, 0, sortLength, 0);
    }

}
