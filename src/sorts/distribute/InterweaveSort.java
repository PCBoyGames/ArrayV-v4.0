package sorts.distribute;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.Sort;
import utils.ArrayVList;

public class InterweaveSort extends Sort {

    public InterweaveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Interweave");
        this.setRunAllSortsName("Interweave Sort");
        this.setRunSortName("Interweave Sort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    int average1, average2;
    int ofs;

    void avgList(int[] list, ArrayVList temp, int len) {
        average1 = 0;
        int i = 0;
        for (int k = 0; k < Math.ceil(len / 2.0); k++) {
            average1 += list[ofs + i];
            Highlights.markArray(0, ofs + i);
            Delays.sleep(1);
            i++;
        }
        average1 /= Math.ceil(len / 2.0);
        average2 = 0;
        for (int k = 0; k < Math.floor(len / 2.0); k++) {
            average2 += list[ofs + i];
            Highlights.markArray(0, ofs + i);
            Delays.sleep(1);
            i++;
        }
        average2 /= Math.floor(len / 2.0);
        if (Reads.compareValues(average1, average2) > 0) {
            temp.clear();
            i = 0;
            for (int k = 0; k < Math.floor(len / 2.0); k++) {
                temp.add(list[ofs + i]);
                Writes.write(list, ofs + i, list[ofs + i + (int) (Math.floor(len / 2.0))], 0.5, true, false);
                i++;
            }
            for (int k = 0; k < Math.floor(len / 2.0); k++) {
                Writes.write(list, ofs + i, temp.get(i - (int) (Math.floor(len / 2.0))), 1, true, false);
                i++;
            }
        }
    }

    void avgLeft(int[] list, ArrayVList temp, int len) {
        int i = 0;
        average1 = 0;
        for (int k = 0; k < Math.ceil(len / 4.0); k++) {
            average1 += list[ofs + i];
            Highlights.markArray(0, ofs + i);
            Delays.sleep(1);
            i++;
        }
        average1 /= Math.ceil(len / 4.0);
        average2 = 0;
        for (int k = 0; k < Math.floor(len / 4.0); k++) {
            average2 += list[ofs + i];
            Highlights.markArray(0, ofs + i);
            Delays.sleep(1);
            i++;
        }
        average2 /= Math.floor(len / 4.0);
        if (Reads.compareValues(average1, average2) > 0) {
            temp.clear();
            i = 0;
            for (int k = 0; k < Math.floor(len / 4.0); k++) {
                temp.add(list[ofs + i]);
                Writes.write(list, ofs + i, list[ofs + i + (int) (Math.floor(len / 4.0))], 0.5, true, false);
                i++;
            }
            for (int k = 0; k < Math.floor(len / 4.0); k++) {
                Writes.write(list, ofs + i, temp.get(i - (int) (Math.floor(len / 4.0))), 1, true, false);
                i++;
            }
        }
    }

    void avgRight(int[] list, ArrayVList temp, int len) {
        int i = (int) Math.floor(len / 2.0);
        average1 = 0;
        for (int k = 0; k < Math.ceil(len / 4.0); k++) {
            average1 += list[ofs + i];
            Highlights.markArray(0, ofs + i);
            Delays.sleep(1);
            i++;
        }
        average1 /= Math.ceil(len / 4.0);
        average2 = 0;
        for (int k = 0; k < Math.floor(len / 4.0); k++) {
            average2 += list[ofs + i];
            Highlights.markArray(0, ofs + i);
            Delays.sleep(1);
            i++;
        }
        average2 /= Math.floor(len / 4.0);
        if (Reads.compareValues(average1, average2) > 0) {
            temp.clear();
            i = (int) Math.floor(len / 2.0);
            for (int k = 0; k < Math.floor(len / 4.0); k++) {
                temp.add(list[ofs + i]);
                Writes.write(list, ofs + i, list[ofs + i + (int) (Math.floor(len / 4.0))], 0.5, true, false);
                i++;
            }
            for (int k = 0; k < Math.floor(len / 4.0); k++) {
                Writes.write(list, ofs + i, temp.get(i - (int) (3 * Math.floor(len / 4.0))), 1, true, false);
                i++;
            }
        }
    }

    void weave(int[] list, ArrayVList temp, ArrayVList temp2, int len) {
        temp.clear();
        temp2.clear();
        int i = 0;
        for (int k = 0; k < Math.ceil(len / 2.0); k++) {
            temp.add(list[ofs + i]);
            i++;
        }
        for (int k = 0; k < Math.floor(len / 2.0); k++) {
            temp2.add(list[ofs + i]);
            i++;
        }
        i = 1;
        while (i <= len) {
            Writes.write(list, ofs + i - 1, temp.get((int) Math.ceil(i / 2.0) - 1), 0.5, true, false);
            if (i < len)
                Writes.write(list, ofs + i, temp2.get((int) Math.ceil(i / 2.0) - 1), 0.5, true, false);
            i += 2;
        }
    }

    void getMedianPivot(int[] list, ArrayVList temp, ArrayVList temp2, int len) {
        temp.clear();
        int pivot = (list[ofs] + list[ofs + len - 1]) / 2;
        int i = 0;
        int j = 0;
        for (int k = 0; k < len; k++) {
            Highlights.markArray(0, ofs + i);
            Highlights.markArray(1, pivot);
            Delays.sleep(1);
            if (Reads.compareValues(list[ofs + i], pivot) > 0) {
                Writes.write(list, ofs + j, list[ofs + i], 1, true, false);
                j++;
            } else {
                temp.add(list[ofs + i]);
            }
            i++;
            Highlights.clearAllMarks();
        }
        i = 0;
        for (int k = 0; k < temp.size(); k++) {
            Writes.write(list, ofs + j, temp.get(i), 1, true, false);
            i++;
            j++;
        }
    }
    
    public void sort(int[] array, int a, int b) {
        int sortLength = b - a;
        this.ofs = a;
        ArrayVList temp = Writes.createArrayList();
        ArrayVList temp2 = Writes.createArrayList();
        for (int i = 0; i < 2; i++) {
            avgList(array, temp, sortLength);
            avgLeft(array, temp, sortLength);
            avgRight(array, temp, sortLength);
            weave(array, temp, temp2, sortLength);
            weave(array, temp, temp2, sortLength);
            getMedianPivot(array, temp, temp2, sortLength);
        } 
        InsertionSort inserter = new InsertionSort(arrayVisualizer);
        inserter.customInsertSort(array, a, b, 0.25, false);
        Writes.deleteArrayList(temp);
        Writes.deleteArrayList(temp2);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
