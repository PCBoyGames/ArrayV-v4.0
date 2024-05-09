package sorts.merge;
/*
	Copyright (C) 2014-2022 Igor van den Hoven ivdhoven@gmail.com
*/

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*
	Permission is hereby granted, free of charge, to any person obtaining
	a copy of this software and associated documentation files (the
	"Software"), to deal in the Software without restriction, including
	without limitation the rights to use, copy, modify, merge, publish,
	distribute, sublicense, and/or sell copies of the Software, and to
	permit persons to whom the Software is furnished to do so, subject to
	the following conditions:

	The above copyright notice and this permission notice shall be
	included in all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
	EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
	MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
	IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
	CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
	TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
	SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

/*
	piposort 1.1.5.4
*/
public class PipoSort extends Sort {

    public PipoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pipo");
        this.setRunAllSortsName("Pipo Sort");
        this.setRunSortName("Pipo Sort");
        this.setCategory("Merge Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    void branchless_oddeven_sort(int[] array, int start, int nmemb) {
        int swap, pta, pte;
        int w = 1;
        boolean z = true, x, y;

        switch (nmemb) {
            default:
                pte = start + nmemb - 3;
                do {
                    z = !z;
                    pta = pte + (!z ? 1 : 0);
                    do {
                        x = Reads.compareIndices(array, pta, pta + 1, 1, true) > 0;
                        y = !x;
                        swap = array[pta + (y == true ? 1 : 0)];
                        Writes.write(array, pta, array[pta + (x == true ? 1 : 0)], 1, true, false);
                        Writes.write(array, pta + 1, swap, 1, true, false);
                        pta -= 2;
                        w |= (x == true ? 1 : 0);
                    } while (pta >= start);
                } while (w-- > 0 && --nmemb > 0);
                return;
            case 3:
                pta = start;
                x = Reads.compareIndices(array, pta, pta + 1, 1, true) > 0;
                y = !x;
                swap = array[pta + (y == true ? 1 : 0)];
                Writes.write(array, pta, array[pta + (x == true ? 1 : 0)], 1, true, false);
                Writes.write(array, pta + 1, swap, 1, true, false);
                pta++;

                x = Reads.compareIndices(array, pta, pta + 1, 1, true) > 0;
                y = !x;
                swap = array[pta + (y == true ? 1 : 0)];
                Writes.write(array, pta, array[pta + (x == true ? 1 : 0)], 1, true, false);
                Writes.write(array, pta + 1, swap, 1, true, false);
                if (x == false)
                    return;
            case 2:
                pta = start;
                x = Reads.compareIndices(array, pta, pta + 1, 1, true) > 0;
                y = !x;
                swap = array[pta + (y == true ? 1 : 0)];
                Writes.write(array, pta, array[pta + (x == true ? 1 : 0)], 1, true, false);
                Writes.write(array, pta + 1, swap, 1, true, false);
            case 1:
            case 0:
                return;
        }
    }

    // aux means that from is aux and dest is main
    void oddeven_parity_merge(int[] from, int[] dest, int start, int dest_start, int left, int right, boolean aux) {
        boolean x;
        int ptl = start, ptr = start + left, ptd = dest_start;
        int tpl = start + left - 1, tpr = start + left + right - 1, tpd = dest_start + left + right - 1;

        if (left < right)
            Writes.write(dest, ptd++, Reads.compareIndices(from, ptl, ptr, 1, !aux) <= 0 ? from[ptl++] : from[ptr++], 1,
                    aux, !aux);// shan't highlight aux

        while (--left > 0) {
            Highlights.clearAllMarks();
            x = Reads.compareIndices(from, ptl, ptr, 1, !aux) <= 0;
            Writes.write(dest, ptd, from[ptl], 0.25, aux, !aux);
            ptl += x ? 1 : 0;
            Writes.write(dest, ptd + (x ? 1 : 0), from[ptr], 0.25, aux, !aux);
            ptr += !x ? 1 : 0;
            ptd++;

            x = Reads.compareIndices(from, tpl, tpr, 1, !aux) <= 0;
            Writes.write(dest, tpd, from[tpl], 0.25, aux, !aux);
            tpl -= !x ? 1 : 0;
            tpd--;
            Writes.write(dest, tpd + (x ? 1 : 0), from[tpr], 0.25, aux, !aux);
            tpr -= x ? 1 : 0;
        }
        Writes.write(dest, tpd, Reads.compareIndices(from, tpl, tpr, 1, !aux) > 0 ? from[tpl] : from[tpr], 1, aux,
                !aux);
        Writes.write(dest, ptd, Reads.compareIndices(from, ptl, ptr, 1, !aux) <= 0 ? from[ptl] : from[ptr], 1, aux,
                !aux);
    }

    public void auxiliary_rotation(int[] array, int[] swap, int start, int left, int right) {
        Writes.arraycopy(array, start, swap, 0, left, 0.25, true, true);

        Writes.arraycopy(array, start + left, array, start, right, 0.25, true, true);

        Writes.arraycopy(swap, 0, array, start + right, left, 1, true, false);
    }

    void ping_pong_merge(int[] array, int start, int[] swap, int nmemb) {
        int quad1, quad2, quad3, quad4, half1, half2;

        if (nmemb <= 7) {
            branchless_oddeven_sort(array, start, nmemb);
            return;
        }
        half1 = nmemb / 2;
        quad1 = half1 / 2;
        quad2 = half1 - quad1;
        half2 = nmemb - half1;
        quad3 = half2 / 2;
        quad4 = half2 - quad3;

        ping_pong_merge(array, start, swap, quad1);
        ping_pong_merge(array, start + quad1, swap, quad2);
        ping_pong_merge(array, start + half1, swap, quad3);
        ping_pong_merge(array, start + half1 + quad3, swap, quad4);

        if (Reads.compareIndices(array, start + quad1 - 1, start + quad1, 1, true) <= 0
                && Reads.compareIndices(array, start + half1 - 1, start + half1, 1, true) <= 0
                && Reads.compareIndices(array, start + half1 + quad3 - 1, start + half1 + quad3, 1, true) <= 0) {
            return;
        }

        if (Reads.compareIndices(array, start, start + half1 - 1, 1, true) > 0
                && Reads.compareIndices(array, start + quad1, start + half1 + quad3 - 1, 1, true) > 0
                && Reads.compareIndices(array, start + half1, start + nmemb - 1, 1, true) > 0) {
            auxiliary_rotation(array, swap, start, quad1, quad2 + half2);
            auxiliary_rotation(array, swap, start, quad2, half2);
            auxiliary_rotation(array, swap, start, quad3, quad4);
            return;
        }

        oddeven_parity_merge(array, swap, start, 0, quad1, quad2, false);
        oddeven_parity_merge(array, swap, start + half1, half1, quad3, quad4, false);
        oddeven_parity_merge(swap, array, 0, start, half1, half2, true);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int[] swap = Writes.createExternalArray(sortLength);
        ping_pong_merge(array, 0, swap, sortLength);
        Writes.deleteExternalArray(swap);
    }

}
