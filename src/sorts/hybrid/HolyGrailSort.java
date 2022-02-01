package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.HolyGrailSorting;

/*
 * MIT License
 *
 * Copyright (c) 2013 Andrey Astrelin
 * Copyright (c) 2020-2021 The Holy Grail Sort Project
 * Copyright (c) 2021 ArrayV 4.0 Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/*
 * The Holy Grail Sort Project
 * Project Manager:      Summer Dragonfly
 * Project Contributors: 666666t
 *                       Anonymous0726
 *                       aphitorite
 *                       Control
 *                       dani_dlg
 *                       DeveloperSort
 *                       EilrahcF
 *                       Enver
 *                       Gaming32
 *                       lovebuny
 *                       Morwenn
 *                       MP
 *                       phoenixbound
 *                       Spex_guy
 *                       thatsOven
 *                       _fluffyy
 *
 * Special thanks to "The Studio" Discord community!
 */

final public class HolyGrailSort extends HolyGrailSorting {
    public HolyGrailSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Holy Grail");
        this.setRunAllSortsName("Holy Grail Sort");
        this.setRunSortName("Holy Grailsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        // switch(bucketCount) {
        // case 1:
        //     int[] ExtBuf = Writes.createExternalArray(this.getStaticBuffer());
        //     this.grailCommonSort(array, 0, length, ExtBuf, 0, this.getStaticBuffer());
        //     Writes.deleteExternalArray(ExtBuf);
        //     break;

        // case 2:
        //     int tempLen = 1;
        //     while(tempLen * tempLen < length) tempLen *= 2;
        //     int[] DynExtBuf = Writes.createExternalArray(tempLen);
        //     this.grailCommonSort(array, 0, length, DynExtBuf, 0, tempLen);
        //     Writes.deleteExternalArray(DynExtBuf);
        //     break;

        // default:
        //     this.grailCommonSort(array, 0, length, null, 0, 0);
        // }
        this.commonSort(array, 0, length, null, 0);
    }
}