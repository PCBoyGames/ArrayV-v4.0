package sorts.distribute;


import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
MagicSort 2020 Copyright (C) thatsOven
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

public class MagicSort extends Sort {
    public MagicSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Magic");
        this.setRunAllSortsName("thatsOven's Magic Sort");
        this.setRunSortName("thatsOven's Magic Sort");
        this.setCategory("Distribution Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public boolean checkEqual(int[] st_arr, int[] nd_arr, int length) {
        for (int i = 0; i < length; i++) {
            if (Reads.compareValues(st_arr[i], nd_arr[i]) != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int[] aux = new int[currentLength];
        for (int i = 0; i < currentLength; i++) {
            Writes.write(aux, i, array[i], 1, true, true);
        }
        for (int i = 0; i < currentLength - 1; i++) {
            int lowestindex = i;
            for (int j = i + 1; j < currentLength; j++) {
                Highlights.markArray(2, j);
                Delays.sleep(0.01);

                if (Reads.compareValues(aux[j], aux[lowestindex]) == -1) {
                    lowestindex = j;
                    Highlights.markArray(1, lowestindex);
                    Delays.sleep(0.01);
                }
            }
            Writes.swap(aux, i, lowestindex, 0.02, true, true);
        }
        Highlights.clearAllMarks();
        while (!this.checkEqual(array, aux, currentLength)) {
            for (int i = 0; i < currentLength; i++) {
                if (Reads.compareValues(array[i], aux[i]) > 0) {
                    array[i] -= 1;
                    Writes.changeWrites(1);
                } else {
                    if (Reads.compareValues(array[i], aux[i]) < 0) {
                        array[i] += 1;
                        Writes.changeWrites(1);
                    }
                }
                Highlights.markArray(0, i);
            }
        }
    }
}