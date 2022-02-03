package sorts.golf;

import main.ArrayVisualizer;
import sorts.insert.BlockInsertionSortNeon;
import sorts.templates.GrailSorting;
import utils.IndexedRotations;

public class NilSort extends GrailSorting {
    public NilSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Nil");
        this.setRunAllSortsName("Nil Sort");
        this.setRunSortName("Nilsort");
        this.setCategory("Golf Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private int buf, bufsz;
    private static final int minblinsert = 8;
    private BlockInsertionSortNeon blinserter;

    protected void grailRotate(int[] array, int pos, int lena, int lenb) {
        IndexedRotations.neon(array, pos, pos+lena, pos+lena+lenb, 1, true, false);
    }
    protected void multiSwap(int[] array, int a, int b, int len) {
        while(len-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }
    private void zeromerge(int[] array, int start, int mid, int end) {
        int l = start, r = mid;
        int lBuf = buf, rBuf = mid, lSz = bufsz, rSz = 0;
        while(l < mid || r < end) {
            while(l < mid && r < end && lSz > 0) {
                if(Reads.compareValues(array[l], array[r]) <= 0) {
                    Writes.swap(array, lBuf++, l++, 1.25, true, false);
                } else {
                    Writes.swap(array, lBuf++, r++, 1.25, true, false);
                    lSz--;
                    rSz++;
                }
            }
            if(lSz > 0) {
                while(l < mid) {
                    Writes.swap(array, lBuf++, l++, 1.25, true, false);
                }
                while(r < end && lSz > 0) {
                    Writes.swap(array, lBuf++, r++, 1.25, true, false);
                    lSz--;
                    rSz++;
                }
            }
            while(l < mid && r < end && rSz > 0) {
                if(Reads.compareValues(array[l], array[r]) <= 0) {
                    Writes.swap(array, rBuf++, l++, 1.25, true, false);
                    rSz--;
                    lSz++;
                } else {
                    Writes.swap(array, rBuf++, r++, 1.25, true, false);
                }
            }
            if(rSz > 0) {
                while(l < mid && rSz > 0) {
                    Writes.swap(array, rBuf++, l++, 1.25, true, false);
                    rSz--;
                    lSz++;
                }
                while(r < end) {
                    Writes.swap(array, rBuf++, r++, 1.25, true, false);
                }
            }
            IndexedRotations.neon(array, lBuf, mid, rBuf+rSz, 2.5, true, false);
            l += rBuf-mid+rSz;
            lBuf += rBuf-mid+rSz;
            mid = r = rBuf+rSz;
            rSz=0;
        }
        buf = end-bufsz;
    }
    
    private void pingpong(int[] array, int start, int end) {
        if(end-start <= minblinsert) {
            blinserter.insertionSort(array, start, end);
            return;
        }
        for(int i=start; i<end; i+=minblinsert)
            blinserter.insertionSort(array, i, Math.min(i+minblinsert, end));
        for(int j=minblinsert; j<end-start; j*=2) {
            for(int i=start; i<end; i+=2*j) {
                if(i+j >= end) break;
                int k=Math.min(i+2*j, end);
                if(j < bufsz) {
                    multiSwap(array, i, buf, j);
                    int l=buf, r=i+j, t=i;
                    while(l<buf+j && r<k) {
                        if(Reads.compareValues(array[l], array[r]) <= 0) {
                            Writes.swap(array, l++, t++, 1, true, false);
                        } else {
                            Writes.swap(array, r++, t++, 1, true, false);
                        }
                    }
                    while(l<buf+j) {
                        Writes.swap(array, l++, t++, 1, true, false);
                    }
                } else {
                    grailMergeWithoutBuffer(array, i, j, k-i-j);
                }
            }
        }
    }
    
    private void ppmergeruns(int[] array, int start, int end) {
        int m=Math.max(minblinsert, bufsz);
        for(int i=start; i<end; i+=m) {
            pingpong(array, i, Math.min(i+m, end));
        }
    }
    
    public void runZero(int[] array, int start, int end) {
        buf = start;
        bufsz = (int) Math.pow(end-start, 0.5d);
        bufsz = grailFindKeys(array, start, end-start, bufsz);
        blinserter = new BlockInsertionSortNeon(arrayVisualizer);
        if(bufsz < 4) {
            blinserter.insertionSort(array, start, end);
            return;
        }
        ppmergeruns(array, buf+bufsz, end);
        for(int j=Math.max(bufsz, minblinsert); j<end-start; j*=2) {
            for(int i=buf+bufsz; i<end; i+=2*j) {
                if(i+j > end)
                    break;
                if(i+2*j > end) {
                    zeromerge(array, i, i+j, end);
                } else {
                    zeromerge(array, i, i+j, i+2*j);
                }
            }
            IndexedRotations.neon(array, start, buf, buf+bufsz, 1, true, false);
            buf=start;
        }
        blinserter.insertionSort(array, buf, buf+bufsz);
        grailMergeWithoutBuffer(array, buf, bufsz, end-(buf+bufsz));
    }
    
    @Override
    public void runSort(int[] array, int len, int buck) {
        runZero(array, 0, len);
    }
}