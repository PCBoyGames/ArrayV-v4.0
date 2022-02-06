package sorts.templates;

import main.ArrayVisualizer;
import sorts.insert.PDBinaryInsertionSort;
import utils.Rotations;

/*******************************
 *                             *
 * Lassosort: Just a quick jab *
 * at trying keyed block merge *
 * sorts.                      *
 *                             *
 * Still very WIP, but getting *
 * there.                      *
 *                             *
 * @author Distray             *
 * @author PCBoy               *
 *                             *
 *******************************/
public abstract class LassoSorting extends Sort {
    public LassoSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected static final int nil = 0x80000000,
                         growSpeed = 2;
    protected int BufSize, BufLoc, EctaKeyLoc;
    protected int[] KeySpace;
    protected PDBinaryInsertionSort binserter;

    protected void multiSwap(int[] array, int locA, int locB, int size) {
        for(int i=0; i<size; i++) {
            Writes.swap(array, locA+i, locB+i, 1, true, false);
        }
    }
    protected void multiSwapBW(int[] array, int locA, int locB, int size) {
        for(int i=size-1; i>=0; i--) {
            Writes.swap(array, locA+i, locB+i, 1, true, false);
        }
    }

    protected void shift(int[] array, int from, int to, double sleep, boolean aux) {
        if(from == to)
            return;
        int k = array[from];
        if(from < to) {
            Writes.arraycopy(array, from+1, array, from, to-from, sleep/2d, true, aux);
        } else {
            Writes.arraycopy(array, to, array, to+1, from-to, sleep/2d, true, aux);
        }
        Writes.write(array, to, k, sleep, true, false);
    }

    protected void rotate(int[] array, int pos, int lenA, int lenB) {
        Rotations.neon(array, pos, lenA, lenB, 1, true, false);
    }

    // Copied from Disquad Sort
    protected void NQSort(int[] array, int a, int b, int c, int d) {
        if(d-a == 2) {
            this.NTSort(array, a, a==b?c:b, d);
            return;
        } else if(d-a == 1) {
            if(Reads.compareValues(array[a], array[d]) == 1) {
                Writes.swap(array, a, d, 1, true, false);
            }
            return;
        } else if(d-a < 1) {
            return;
        }
        int t1 = nil, t2 = nil;

        if(Reads.compareValues(array[b], array[d]) == 1) {
            t1 = array[d];
            Writes.write(array, d, array[b], 1, true, false);
        }

        if(Reads.compareValues(array[a], array[c]) == 1) {
            t2 = array[c];
            Writes.write(array, c, array[a], 1, true, false);
        }

        if(Reads.compareValues(array[c], array[d]) == 1) {
            Writes.swap(array, c, d, 1, true, false);
        }

        if(t1 == nil) {
            if(t2 == nil) {
                // no temp
                if(Reads.compareValues(array[a], array[b]) == 1) {
                    Writes.swap(array, a, b, 1, true, false);
                }
            } else {
                // temp2 used
                if(Reads.compareValues(t2, array[b]) == 1) {
                    Writes.write(array, a, array[b], 0.5, true, false);
                    Writes.write(array, b, t2, 0.5, true, false);
                } else {
                    Writes.write(array, a, t2, 0.5, true, false);
                }
            }
        } else {
            if(t2 == nil) {
                // temp1 used
                if(Reads.compareValues(array[a], t1) == 1) {
                    Writes.write(array, b, array[a], 0.5, true, false);
                    Writes.write(array, a, t1, 0.5, true, false);
                } else {
                    Writes.write(array, b, t1, 0.5, true, false);
                }
            } else {
                // filled temps
                if(Reads.compareValues(t2, t1) == 1) {
                    Writes.write(array, b, t2, 0.5, true, false);
                    Writes.write(array, a, t1, 0.5, true, false);
                } else {
                    Writes.write(array, b, t1, 0.5, true, false);
                    Writes.write(array, a, t2, 0.5, true, false);
                }
            }
        }
        if(Reads.compareValues(array[b], array[c]) == 1) {
            Writes.swap(array, b, c, 1, true, false);
        }
    }

    protected void NTSort(int[] array, int a, int b, int c) {
        int t = nil;
        if(Reads.compareValues(array[a], array[c]) == 1) {
            t = array[c];
            Writes.write(array, c, array[a], 1, true, false);
        }

        if(Reads.compareValues(array[b], array[c]) == 1) {
            Writes.swap(array, a, c, 1, true, false);
        }

        if(t == nil) {
            if(Reads.compareValues(array[a], array[b]) == 1) {
                Writes.swap(array, a, b, 1, true, false);
            }
        } else {
            if(Reads.compareValues(t, array[b]) == 1) {
                Writes.write(array, a, array[b], 1, true, false);
                Writes.write(array, b, t, 1, true, false);
            } else {
                Writes.write(array, a, t, 1, true, false);
            }
        }
    }

    protected void quadBuild(int[] array, int start, int end) {
        for(int i=start; i<end; i+=4) {
            int b = Math.min(i+1, end),
                c = Math.min(i+2, end),
                d = Math.min(i+3, end);
            this.NQSort(array, i, b, c, d);
        }
    }

    protected Range exponentialSearch(int[] array, int start, int end, int key, double sleep, boolean goRight) {
        int prev = 1;
        int length = end-start;
        if(goRight) {
            while(prev < length && Reads.compareValues(array[prev+start], key) == -1) {
                prev *= growSpeed;
                Highlights.markArray(1, prev+start);
                Delays.sleep(sleep);
            }
        } else {
            while(prev < length && Reads.compareValues(array[end-prev], key) == 1) {
                prev *= growSpeed;
                if(end-prev>=start)
                    Highlights.markArray(1, end-prev);
                Delays.sleep(sleep);
            }
        }
        if(prev > length) {
            prev = length;
        }
        if(goRight)
            return new Range(end-prev, end-(prev/growSpeed));
        else
            return new Range((prev/growSpeed)+start, prev+start);
    }

    protected int linearExpSearch(int[] array, int start, int end, int key, boolean goRight, double sleep) {
        if(goRight) {
            while(end >= start) {
                if(Reads.compareValues(array[start], key) == 1)
                    break;
                Highlights.markArray(1, start++);
                Delays.sleep(sleep);
            }
            return start;
        } else {
            while(end >= start) {
                if(Reads.compareValues(array[end], key) == -1)
                    break;
                Highlights.markArray(1, end--);
                Delays.sleep(sleep);
            }
            return end+1;
        }
    }

    protected int linearExpSearch_exclusive(int[] array, int start, int end, int key, boolean goRight, double sleep) {
        if(goRight) {
            while(end >= start) {
                int comp = Reads.compareValues(array[start], key);
                if(comp == 1)
                    break;
                else if(comp == 0)
                    return -1;
                Highlights.markArray(1, start++);
                Delays.sleep(sleep);
            }
            return start;
        } else {
            while(end >= start) {
                int comp = Reads.compareValues(array[end], key);
                if(comp == -1)
                    break;
                else if(comp == 0)
                    return -1;
                Highlights.markArray(1, end--);
                Delays.sleep(sleep);
            }
            return end+1;
        }
    }

    // Still WIP (O(log n) average, O(n) worst, O(0) best)
    protected int slopeSearch(int[] array, int start, int end, int keyIndex, double sleep, boolean exclusive) {
        int key = arrayVisualizer.doingStabilityCheck() ? arrayVisualizer.getStabilityValue(array[keyIndex])
            : array[keyIndex];
        if(end-start == 0)
            return start;
        else if(end-start == 1) {
            return Reads.compareValues(array[start], key) == 1 ? start : end;
        }
        int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
        for(int i=start; i<end; i++) {
            int v = array[i];
            if(arrayVisualizer.doingStabilityCheck()) {
                v = arrayVisualizer.getStabilityValue(v);
            }
            if(max < v) {
                max = v;
            }
            if(min > v) {
                min = v;
            }
        }
        double approxRate = (max-min) / (double)(end - start);
        int approxIndex = (int) ((key - min) / approxRate) + start; // gets pretty close
        if(approxIndex < start)
            return start;
        if(approxIndex > end)
            return end;
        Highlights.markArray(1, approxIndex);
        Delays.sleep(sleep);
        //approxIndex = Math.min(Math.max(approxIndex, start), end);
        int comp = Reads.compareValues(array[approxIndex], key);
        if(exclusive ? comp>0 : comp >= 0) {
            if(exclusive) {
                return linearExpSearch_exclusive(array, start, approxIndex, key, false, sleep);
            }
            return linearExpSearch(array, start, approxIndex, key, false, sleep);
        } else if(exclusive && comp == 0){
            return -1;
        }
        if(exclusive) {
            return linearExpSearch_exclusive(array, approxIndex, end, key, true, sleep);
        }
        return linearExpSearch(array, approxIndex, end, key, true, sleep);
    }

    protected int binSearch(int[] array, int start, int end, int key, double sleep, boolean goLeft) {
        while(start < end) {
            int mid = start + ((end - start) / 2);
            if(goLeft)
                if(Reads.compareValues(array[mid], key) <= 0) {
                    start = mid+1;
                } else {
                    end = mid;
                }
            else
                if(Reads.compareValues(array[mid], key) < 0) {
                    start = mid+1;
                } else {
                    end = mid;
                }
            Highlights.markArray(1, mid);
            Delays.sleep(sleep);
        }
        return start;
    }


    // copied from Scrsort (copied from Silversort (copied from PrallieSort Mk. II (copied from Aphitorite's Rotate Merge Sort)))
    protected void rotateMerge(int[] array, int start, int mid, int end) {
        if(start==mid||mid==end)
            return;
        if(end-start < 4) {
            this.binserter.pdbinsert(array, start, end, 1, false);
            return;
        }
        int mid1, mid2, rotatedmid;
        if(mid-start >= end-mid) {
            mid1 = (mid+start) / 2;
            mid2 = this.binSearch(array, mid, end, array[mid1], 1, true);
            rotatedmid = mid1 + (mid2 - mid);
        } else {
            mid2 = (mid+end+1) / 2;
            mid1 = this.binSearch(array, start, mid, array[mid2], 1, false);
            rotatedmid = mid2++ - (mid - mid1);
        }

        this.rotate(array, mid1, mid-mid1, mid2-mid);

        this.rotateMerge(array, start, mid1, rotatedmid);
        this.rotateMerge(array, rotatedmid+1, mid2, end);
    }

    protected int grabKeys(int[] array, int len, int idealKeys) {
        int uniqueKeys = 0, end = EctaKeyLoc+BufSize, searchPosition = end-1;
        while(uniqueKeys < idealKeys && searchPosition >= 0) {
            int search = this.slopeSearch(array, end-uniqueKeys, end, searchPosition, 1, true);
            if(search >= 0 && search <= end) {
                this.shift(array, searchPosition, search-1, 1, false);
                uniqueKeys++;
            }
            searchPosition--;
        }
        return uniqueKeys;
    }

    protected int ceilSqrt(int l) {
        long k = 1;
        while(k*k < l) {
            k<<=1;
        }
        return (int) k;
    }

    protected void mergeRight(int[] array, int start, int mid, int end) {
        this.multiSwapBW(array, mid, BufLoc, end-mid);
        int left = mid-1, right = BufLoc+(end-mid)-1, to = end-1;
        while(left >= start && right >= BufLoc) {
            if(Reads.compareValues(array[left], array[right])==1) {
                Writes.swap(array,left--,to--,1,true,false);
            } else {
                Writes.swap(array,right--,to--,1,true,false);
            }
        }
        while(right >= BufLoc) {
            Writes.swap(array,right--,to--,1,true,false);
        }
    }

    protected int mergeCenter(int[] array, int start, int mid, int end, boolean goLeft, boolean fragMerging) {
        if(start == mid || mid == end)
            return 0;
        if(goLeft) {
            int left = mid-1, right = end-1, offset = Math.min(end-mid, BufSize);
            while(left>=start&&right>=mid) {
                if(Reads.compareValues(array[left], array[right])==1) {
                    Writes.swap(array,left--,offset+--BufLoc,1,true,false);
                } else {
                    Writes.swap(array,right--,offset+--BufLoc,1,true,false);
                }
            }
            if(fragMerging) {
                if(left >= start) {
                    return left-start+1;
                } else {
                    int f = right-mid+1;
                    this.multiSwapBW(array, start, mid, f);
                    return f;
                }
            }

            while(left>=start) {
                Writes.swap(array,left--,offset+--BufLoc,1,true,false);
            }
            while(right>=mid) {
                Writes.swap(array,right--,offset+--BufLoc,1,true,false);
            }

            this.rotate(array, BufLoc+offset, end-start, BufSize-offset);
            BufLoc = start;
            return 0;
        } else {
            int left = start, right = mid;
            while(left<mid&&right<end) {
                if(Reads.compareValues(array[left], array[right])<=0) {
                    Writes.swap(array,left++,BufLoc++,1,true,false);
                } else {
                    Writes.swap(array,right++,BufLoc++,1,true,false);
                }
            }
            if(fragMerging) {
                if(left < mid) {
                    int f = mid-left;
                    this.multiSwap(array, left, end-f, f);
                    return f;
                } else {
                    return end-right;
                }
            }
            while(left<mid) {
                Writes.swap(array,left++,BufLoc++,1,true,false);
            }
            while(right<end) {
                Writes.swap(array,right++,BufLoc++,1,true,false);
            }
            return 0;
        }
    }

    protected boolean mergeIndex(int[] array, int start, int mid, int end) {
        if(end-start <= 2*BufSize) {
            return false;
        }
        if(mid-start == 0 || end-mid == 0) {
            return false;
        }
        if(mid-start < BufSize || end-mid < BufSize) {
            this.mergeRight(array, start, mid, end);
            return false;
        }
        int leftover = (end - start) % BufSize;
        end -= leftover;
        int left = start, right = mid, q = EctaKeyLoc, z = 0, r = 0,
            midK = (mid-start-1) / BufSize + 1, p = q + midK, s = midK,
            x = KeySpace[midK];
        while(left <= mid-BufSize && right <= end-BufSize) {
            Highlights.markArray(1, left);
            Highlights.markArray(2, right);
            Delays.sleep(5);
            if(Reads.compareValues(array[left], array[right]) <= 0) {
                r++;
                Writes.write(array, q++, z++, 1, true, false);
                left+=BufSize;
            } else {
                this.shift(KeySpace, s++, r++, 1, true);
                Writes.write(array, p++, z++, 1, true, false);
                right+=BufSize;
            }
        }
        while(left <= mid-BufSize) {
            r++;
            Writes.write(array, q++, z++, 1, true, false);
            left+=BufSize;
        }
        while(right <= end-BufSize) {
            this.shift(KeySpace, s++, r++, 0, true);
            Writes.write(array, p++, z++, 1, true, false);
            right+=BufSize;
        }
        for(int i=0; i<r; i++) {
            int c = 0;
            while(array[i+EctaKeyLoc] == i && i < r)
                i++;
            if(i >= r)
                break;
            while(Reads.compareOriginalValues(array[i+EctaKeyLoc], i) != 0 && c < r) {
                int index = array[i+EctaKeyLoc];
                Writes.swap(array, i+EctaKeyLoc, index+EctaKeyLoc, 1, true, false);
                this.multiSwap(array, start+BufSize*i, start+BufSize*index, BufSize);
            }
            if(c >= r-1)
                break;
        }
        int f = BufSize, g = 0;
        boolean lastWasHi = Reads.compareValues(KeySpace[0], x) >= 0;
        if(!lastWasHi)
            g++;
        for(int i=1; i<r; i++) {
            int n = start+BufSize*i;
            boolean hi = Reads.compareValues(KeySpace[i], x) >= 0;
            if(!hi) {
                this.shift(KeySpace, i, g++, 0, true);
            }
            if(lastWasHi != hi || f < BufSize || Reads.compareValues(array[n-1], array[n]) == 1)
                f = this.mergeCenter(array, n-f, n, n+BufSize, false, true);
            else {
                this.multiSwap(array, BufLoc+BufSize, BufLoc, BufSize);
                BufLoc += BufSize;
            }
            lastWasHi = hi;
        }
        this.multiSwap(array, end-f, BufLoc, f);
        BufLoc = end-BufSize;
        if(leftover > 0) {
            System.out.println(leftover + " " + BufLoc);
            this.rotate(array, BufLoc, BufSize, leftover);
            BufLoc += leftover;
            this.mergeRight(array, start, end-BufSize-leftover, end-BufSize);
        }
        //this.binserter.customBinaryInsert(KeySpace, 0, r, 1);
        return true;
    }

    protected void mergeOOP(int[] array, int start, int end, int ptr) {
        int l = end-1, h = BufSize-1;
        while(h >= 0) {
            if(l < start)
                Writes.write(array, ptr--, KeySpace[h--], 1, true, false);
            else if(Reads.compareValues(array[l], KeySpace[h]) == 1) {
                Writes.write(array, ptr--, array[l--], 1, true, false);
            } else {
                Writes.write(array, ptr--, KeySpace[h--], 1, true, false);
            }
        }
    }

    public void common(int[] array, int start, int end) {
        this.binserter = new PDBinaryInsertionSort(arrayVisualizer);
        int len = end-start;
        if(len <= 4) {
            this.NQSort(array, start, Math.min(start+1, end), Math.min(start+2, end),
                                      Math.min(start+3, end));
            return;
        } else if(len <= 32) {
            this.binserter.pdbinsert(array, start, end, 0.5, false);
            return;
        }
        BufSize = this.ceilSqrt(len);
        BufLoc = end-2*BufSize;
        EctaKeyLoc = end-BufSize;
        this.KeySpace = Writes.createExternalArray(BufSize);
        for(int i=0; i<BufSize; i++) {
            Writes.write(KeySpace, i, array[EctaKeyLoc + i], 1, true, true);
            Writes.write(array, i+EctaKeyLoc, i, 1, true, false);
        }
        this.quadBuild(array, start, BufLoc);
        for(int j=8; j<=BufSize; j*=2) {
            for(int i=start; i<BufLoc; i+=j) {
                if(i+j>BufLoc) {
                    this.mergeRight(array, i, i+j/2, BufLoc);
                } else
                    this.mergeRight(array, i, i+j/2, i+j);
            }
        }
        while(BufLoc>start) {
            this.mergeCenter(array, BufLoc-2*BufSize, BufLoc-BufSize, BufLoc, true, false);
        }
        for(int j=2*BufSize; j<len; j*=2) {
            boolean f = true;
            while(BufLoc+BufSize < EctaKeyLoc && f) {
                int z = Math.min(BufLoc+BufSize+2*j, EctaKeyLoc),
                    y = BufLoc+BufSize+j,
                    x = BufLoc+BufSize;

                f = this.mergeIndex(array, x, y, z);
            }
            if(j < len/2) {
                this.rotate(array, start, BufLoc-start, BufSize);
                BufLoc = start;
            }
        }
        this.binserter.pdbinsert(array, BufLoc, BufLoc+BufSize, 0.5, false);
        this.rotateMerge(array, start, BufLoc, BufLoc+BufSize);
        this.binserter.pdbinsert(KeySpace, 0, BufSize, 0.5, true);
        this.mergeOOP(array, start, BufLoc+BufSize, end-1);
        Writes.deleteExternalArray(KeySpace);
    }
}