package sorts.hybrid;

import sorts.templates.Sort;
import sorts.insert.BinaryInsertionSort;
import utils.IndexedRotations;
import main.ArrayVisualizer;

/*
 *
MIT License

Copyright (c) 2021 aphitorite

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

final public class InPlaceStableCycleSort extends Sort {
    public InPlaceStableCycleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        setSortListName("In-Place Stable Cycle");
        setRunAllSortsName("In-Place Stable Cycle Sort");
        setRunSortName("In-Place Stable Cyclesort");
        setCategory("Hybrid Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    //stable sorting algorithm performing a worst case of
    //O(n^2) comparisons and O(n) moves in O(1) space

    private void multiSwap(int[] array, int a, int b, int len) {
        for(int i = 0; i < len; i++)
            Writes.swap(array, a+i, b+i, 1, true, false);
    }

    private void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
    }

    private long getRank(int[] array, int a, int b, int r) {
        int c = 0, ce = 0;

        for(int i = a; i < b; i++) {
            if(i == r) continue;

            Highlights.markArray(2, i);
            Delays.sleep(0.0025);

            int cmp = Reads.compareValues(array[i], array[r]);

            c  += cmp == -1 ? 1 : 0;
            ce += cmp <=  0 ? 1 : 0;
        }
        return (long)ce << 32 | (long)c;
    }

    private int selectMedian(int[] array, int a, int b) {
        int med = (b-a)/2;
        int min = a, max = min;

        for(int i = a+1; i < b; i++) {
            if(Reads.compareIndices(array, i, min, 0.25, true) < 0)
                min = i;
            else if(Reads.compareIndices(array, i, max, 0.25, true) > 0)
                max = i;
        }
        //max or min might be the median
        long rank = getRank(array, a, b, min);
        int r = (int)rank, re = (int)(rank >> 32);

        if(med >= r && med <= re) return array[min];

        rank = getRank(array, a, b, max);
        r = (int)rank; re = (int)(rank >> 32);

        if(med >= r && med <= re) return array[max];

        for(int i = a;; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.5);

            if(Reads.compareValues(array[i], array[min]) > 0
               && Reads.compareValues(array[i], array[max]) < 0) {

                rank = getRank(array, a, b, i);
                r = (int)rank; re = (int)(rank >> 32);

                if(med >= r && med <= re)
                    return array[i];

                else if(re < med)
                    min = i;

                else max = i;
            }
        }
    }

    private void resetBits(int[] array, int pa, int pb, int bLen) {
        pa--; pb--;
        for(int i = 0; i < bLen; i++)
            if(Reads.compareIndices(array, ++pa, ++pb, 0.5, true) > 0)
                Writes.swap(array, pa, pb, 0.5, true, false);
    }

    private boolean initBitBuffer(int[] array, int a, int b, int piv, int bLen) {
        int p = b, aCnt = 0, bCnt = 0, tCnt = 0;

        //find lesser and greater elements
        for(int i = b; i > a && tCnt < 2*bLen; i--) {
            int pCmp = Reads.compareIndexValue(array, i-1, piv, 1, true);

            if(aCnt < bLen && pCmp < 0) {
                rotate(array, i, p-tCnt, p);
                p = i+tCnt++;
                aCnt++;
            }
            else if(bCnt < bLen && pCmp > 0) {
                rotate(array, i, p-tCnt, p);
                p = i+tCnt++;
                rotate(array, i-1, i, p-bCnt);
                bCnt++;
            }
        }
        rotate(array, p-tCnt, p, b);
        if(tCnt == 2*bLen) return false;

        //if not enough were found either terminate or search equal elements

        int b1 = b-tCnt;

        //redistribute
        if(aCnt < bLen && bCnt < bLen) {
            BinaryInsertionSort smallSort = new BinaryInsertionSort(arrayVisualizer);
            smallSort.customBinaryInsert(array, b1, b, 0.125);
            rotate(array, a, b1, b-bCnt);
            return true;
        }
        int eCnt = 0, eLen = tCnt-bLen;
        p = b1;

        //search equals
        for(int i = b1; eCnt < eLen; i--) {
            if(Reads.compareIndexValue(array, i-1, piv, 1, true) == 0) {
                rotate(array, i, p-eCnt, p);
                p = i+eCnt++;
                eCnt++;
            }
        }
        rotate(array, p-eLen, p, b1);
        rotate(array, b-2*bLen, b1, b-bCnt);

        return false;
    }

    //@param cmp - comp val that means bit flipped
    private int blockCyclePartitionDest(int[] array, int a, int a1, int b1, int b, int pa, int pb, int piv, int bLen, int cmp) {
        int d = a1, e = 0;
        int pCmp = Reads.compareValues(array[a1], piv);

        for(int i = a1+bLen; i < b; i += bLen) {
            Highlights.markArray(2, i);
            int vCmp = Reads.compareValues(array[i], piv);

            if(vCmp < pCmp) d += bLen;
            else if(i < b1 && Reads.compareIndices(array, pa+(i-a)/bLen, pb+(i-a)/bLen, 0.01, true) != cmp
                           && vCmp == pCmp) e++;

            Highlights.markArray(3, d);
            Delays.sleep(0.01);
        }
        while(Reads.compareIndices(array, pa+(d-a)/bLen, pb+(d-a)/bLen, 0.01, true) == cmp || e-- > 0) {
            d += bLen;

            Highlights.markArray(3, d);
            Delays.sleep(0.01);
        }

        return d;
    }
    //b-a is divisible by bLen
    private void blockCyclePartition(int[] array, int a, int b, int pa, int pb, int piv, int bLen, int cmp) {
        for(int i = a; i < b; i += bLen) {
            if(Reads.compareIndices(array, pa+(i-a)/bLen, pb+(i-a)/bLen, 1, true) != cmp) {
                Highlights.markArray(1, i);
                int j = i;

                for(;;) {
                    int k = blockCyclePartitionDest(array, a, i, j, b, pa, pb, piv, bLen, cmp);

                    Writes.swap(array, pa+(k-a)/bLen, pb+(k-a)/bLen, 0.02, true, false);
                    if(k == i) break;
                    multiSwap(array, i, k, bLen);

                    j = k;
                }
            }
        }
    }

    private int leftBinSearch(int[] array, int a, int b, int val) {
        while(a < b) {
            int m = a+(b-a)/2;

            if(Reads.compareValues(val, array[m]) <= 0)
                b = m;
            else
                a = m+1;
        }
        return a;
    }
    private int rightBinSearch(int[] array, int a, int b, int val) {
        while(a < b) {
            int m = a+(b-a)/2;

            if(Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m+1;
        }
        return a;
    }
    private void merge(int[] array, int[] cnt, int a, int m, int b, int piv) {
        int m1 = leftBinSearch(array, m, b, piv);
        int m2 = rightBinSearch(array, m1, b, piv);

        int aCnt = m1-m, mCnt = m2-m1, bCnt = b-m2;

        rotate(array, a+cnt[0], m, m1);
        cnt[0] += aCnt;

        rotate(array, a+cnt[0]+cnt[1], m1, m2);
        cnt[1] += mCnt;
        cnt[2] += bCnt;
    }
    //optimized for: [     A     ][ B ]
    private void mergeEasy(int[] array, int a, int m, int b, int piv) {
        b = rightBinSearch(array, m, b, piv);
        int m1 = leftBinSearch(array, a, m, piv);
        int m2 = rightBinSearch(array, m1, m, piv);

        rotate(array, m2, m, b);

        b = leftBinSearch(array, m2, b-(m-m2), piv);
        rotate(array, m1, m2, b);
    }

    //returns true if u dont have to sort
    private boolean partition(int[] array, int a, int b, int piv) {
        //create bit buffer

        int n = b-a, bLen = (int)Math.sqrt(n-1)+1;
        if(initBitBuffer(array, a, b, piv, bLen)) return true;

        int b1 = b-2*bLen, pa = b1, pb = b1+bLen;
        int cmp = 1;

        //partition blocks

        for(int i = a; i < b1; i += bLen, cmp = -cmp)
            blockCyclePartition(array, i, Math.min(i+bLen, b1), pa, pb, piv, 1, cmp);
        resetBits(array, pa, pb, bLen);

        //type blocks

        int p = a;
        int[] cnt = {0, 0, 0};

        for(int i = a; i < b1; i += bLen) {
            merge(array, cnt, p, i, Math.min(i+bLen, b1), piv);

            while(cnt[0] >= bLen) {
                cnt[0] -= bLen;
                p += bLen;
            }
            while(cnt[1] >= bLen) {
                rotate(array, p, p+cnt[0], p+cnt[0]+bLen);
                cnt[1] -= bLen;
                p += bLen;
            }
            while(cnt[2] >= bLen) {
                rotate(array, p, p+cnt[0]+cnt[1], p+cnt[0]+cnt[1]+bLen);
                cnt[2] -= bLen;
                p += bLen;
            }
        }
        blockCyclePartition(array, a, p, pa, pb, piv, bLen, 1);
        resetBits(array, pa, pb, bLen);

        //rotate lesser and eq items + redistribute bits

        mergeEasy(array, p, b1, b, piv);
        mergeEasy(array, a, p, b, piv);

        return false;
    }

    //@param cmp - comp val that means bit flipped
    //items equal to the median automatically count as bit flipped
    private int stableCycleDest(int[] array, int a1, int b1, int b, int p, int piv, int cmp) {
        int d = a1, e = 0;

        for(int i = a1+1; i < b; i++) {
            Highlights.markArray(2, i);

            int pCmp = Reads.compareValues(array[i], piv);
            boolean bit = pCmp == cmp || pCmp == 0;

            int val  = bit ? array[p+i] : array[i];
            int vCmp = Reads.compareValues(val, array[a1]);

            if(vCmp == -1) d++;
            else if(i < b1 && !bit && vCmp == 0) e++;

            Highlights.markArray(3, d);
            Delays.sleep(0.01);
        }
        for(;;) {
            int pCmp = Reads.compareValues(array[d], piv);
            boolean bit = pCmp == cmp || pCmp == 0;

            if(!bit && e-- == 0) break;
            d++;

            Highlights.markArray(3, d);
            Delays.sleep(0.01);
        }

        return d;
    }
    private void stableCycle(int[] array, int a, int b, int p, int piv, int cmp) {
        for(int i = a; i < b; i++) {
            int pCmp = Reads.compareValues(array[i], piv);
            boolean bit = pCmp == cmp || pCmp == 0;

            if(!bit) {
                Highlights.markArray(1, i);
                int j = i;

                for(;;) {
                    int k = stableCycleDest(array, i, j, b, p, piv, cmp);

                    if(k == i) break;

                    int t = array[i];
                    Writes.write(array, i, array[k], 0.01, true, false);
                    Writes.write(array, k, array[p+k], 0.01, true, false);
                    Writes.write(array, p+k, t, 0.01, true, false);

                    j = k;
                }
                Writes.swap(array, i, p+i, 0.02, true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        if(length <= 32) {
            BinaryInsertionSort smallSort = new BinaryInsertionSort(arrayVisualizer);
            smallSort.customBinaryInsert(array, 0, length, 0.25);
            return;
        }

        int piv = selectMedian(array, 0, length);
        if(partition(array, 0, length, piv)) return;

        //find equal elements zone to be excluded from sorting

        int n = length/2, p = (length+1)/2;
        while(n > 0 && Reads.compareIndices(array, n-1, p, 1, true) == 0) {
            n--; p++;
        }
        if(n == 0) return;

        Highlights.clearMark(2);

        //depending on these comparisons we have to manage block E of equal elements on one partition

        if(Reads.compareValues(array[p], piv) == 0) {
            //[      A      ][ = ][ E ][   B    ]
            int p1 = p, bSize = 1;
            while(++p1 < length && Reads.compareIndexValue(array, p1, piv, 1, true) == 0)
                bSize++;

            stableCycle(array, 0, n, p, piv, 1);
            Highlights.clearAllMarks();
            multiSwap(array, 0, p, bSize);
            stableCycle(array, bSize, n, p, piv, -1);
        }
        else if(Reads.compareValues(array[n-1], piv) == 0) {
            //[   A    ][ E ][ = ][      B      ]
            int n1 = n, bSize = 1;
            while(--n1 > 0 && Reads.compareIndexValue(array, n1-1, piv, 1, true) == 0)
                bSize++;

            stableCycle(array, 0, n1, p, piv, 1);
            Highlights.clearAllMarks();
            multiSwap(array, n1, length-bSize, bSize);
            stableCycle(array, 0, n, p, piv, -1);
        }
        else {
            //[      A      ][ = ][      B      ] (E does not exist)
            stableCycle(array, 0, n, p, piv, 1);
            stableCycle(array, 0, n, p, piv, -1);
        }
    }
}