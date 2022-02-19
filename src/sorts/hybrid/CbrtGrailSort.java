package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.templates.GrailSorting;
import utils.IndexedRotations;

final public class CbrtGrailSort extends GrailSorting {
    public CbrtGrailSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Cube Root Grail");
        this.setRunAllSortsName("Cube Root Grail Sort");
        this.setRunSortName("Cbrt Grail");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private int keysLowLoc, keysHighLoc, bufferLoc, keysLowLen, keysHighLen, bufferLen;
    private InsertionSort small;
    private void sort4Blocks(int[] array, int start, int end) {
        for(int i=start; i<end; i+=4) {
            small.customInsertSort(array, i, Math.min(i+4, end), 0.5, false);
        }
    }
    private void pingPongMergeFW(int[] array, int start, int mid, int end) {
        int l=start, h=mid;
        while(l<mid&&h<end) {
            if(Reads.compareValues(array[l], array[h]) <= 0) {
                Writes.swap(array, bufferLoc++, l++, 1, true, false);
            } else {
                Writes.swap(array, bufferLoc++, h++, 1, true, false);
            }
        }
        while(l<mid)
            Writes.swap(array, bufferLoc++, l++, 1, true, false);
        while(h<end)
            Writes.swap(array, bufferLoc++, h++, 1, true, false);
    }
    private int fragMergeFW(int[] array, int start, int mid, int end) {
        int l=start, h=mid;
        while(l<mid&&h<end) {
            if(Reads.compareValues(array[l], array[h]) <= 0) {
                Writes.swap(array, bufferLoc++, l++, 1, true, false);
            } else {
                Writes.swap(array, bufferLoc++, h++, 1, true, false);
            }
        }
        if(l<mid) {
            int f=mid-l;
            multiSwap(array, l, end-f, f);
            return f;
        } else {
            return end-h;
        }
    }
    private void multiSwap(int[] array, int locA, int locB, int size) {
        for(int i=0; i<size; i++) Writes.swap(array, locA+i, locB+i, 1, true, false);
    }
    // **GOD NO**
    private void blockSelectFW(int[] array, int start, int mid, int end) {
        int blockLen0 = bufferLen;
        while((end-start)/blockLen0 > keysLowLen && blockLen0 <= (mid-start)/2 && blockLen0 < (end-mid)/2) {
            blockLen0*=2;
        }
        int blockLen1 = Math.max(2*blockLen0/keysHighLen, bufferLen),
            leftoverLeft = 0, leftoverRight = 0, leftoverTotal = 0;
        if((end-mid)%bufferLen > 0) {
            leftoverRight = (end-mid)%bufferLen;
            end-=leftoverRight;
            leftoverTotal+=leftoverRight;
        }
        if((mid-start)%bufferLen > 0) {
            leftoverLeft = (mid-start)%bufferLen;
            IndexedRotations.cycleReverse(array, mid-leftoverLeft, mid, end, 1, true, false);
            end-=leftoverLeft;
            mid-=leftoverLeft;
            leftoverTotal+=leftoverLeft;
        }
        int keys0 = Math.min((end-start-1)/blockLen0+1, keysLowLen);
        for(int i=start, iKey = keysLowLoc; i<=end-blockLen0; i+=blockLen0, iKey++) {
            int min=i, minKey=iKey;
            for(int j=i+blockLen0, jKey = iKey+1; j<=end-blockLen0; j+=blockLen0, jKey++) {
                int cmp=Reads.compareIndices(array, min, j, 0.125, true);
                if(cmp > 0 || (cmp == 0 && Reads.compareIndices(array, minKey, jKey, 0.125, true) > 0)) {
                    min=j;
                    minKey=jKey;
                }
            }
            multiSwap(array, i, min, blockLen0);
            Writes.swap(array, minKey, iKey, 1, true, false);
        }
        if(blockLen0 > bufferLen) {
            for(int k=start; k<=end-blockLen0; k+=blockLen0) {
                for(int i=k, iKey = keysHighLoc; i<=end-blockLen1 && i<k+2*blockLen0; i+=blockLen1) {
                    int min=i, minKey=iKey;
                    for(int j=i+blockLen1, jKey = iKey+1; j<=end-blockLen1 && j<k+2*blockLen0; j+=blockLen1, jKey++) {
                        int cmp=Reads.compareIndices(array, min, j, 0.125, true);
                        if(cmp > 0 || (cmp == 0 && Reads.compareIndices(array, minKey, jKey, 0.125, true) > 0)) {
                            min=j;
                            minKey=jKey;
                        }
                    }
                    multiSwap(array, i, min, blockLen1);
                    Writes.swap(array, minKey, iKey, 1, true, false);
                }
                small.customInsertSort(array, keysHighLoc, keysHighLoc+keysHighLen, 0.125, false);
            }
            keys0 = (end-start-1)/blockLen1+1;
            blockLen0 = blockLen1;
        }
        int frag=blockLen0 > bufferLen?end:start;
        for(int i=1; i<keys0; i++) {
            if(blockLen0 > bufferLen) {
                grailMergeWithoutBuffer(array, start+(i-1)*blockLen0, blockLen0, blockLen0);
            } else
                frag=start+((i+1)*blockLen0)-fragMergeFW(array, frag, start+i*blockLen0, Math.min(start+(i+1)*blockLen0, end));
        }
        multiSwap(array, bufferLoc, frag, end-frag);
        bufferLoc+=end-frag;
        IndexedRotations.helium(array, bufferLoc, bufferLoc+bufferLen, end, 1, true, false);
        bufferLoc=end-bufferLen;
        grailMergeWithoutBuffer(array, end, leftoverRight, leftoverLeft);
        IndexedRotations.holyGriesMills(array, bufferLoc, end, end+leftoverTotal, 1, true, false);
        grailMergeWithoutBuffer(array, start, end-start, leftoverTotal);
        bufferLoc+=leftoverTotal;
        small.customInsertSort(array, keysLowLoc, keysLowLoc+keysLowLen, 0.5, false);
    }

    public void common(int[] array, int start, int end) {
        small = new InsertionSort(arrayVisualizer);
        int length=end-start;
        int keysNeeded = 0;
        while(1<<(3*++keysNeeded)<length);
        keysNeeded=1<<keysNeeded;
        bufferLoc = start;
        int keys2 = grailFindKeys(array, keysLowLoc, length-bufferLoc, keysNeeded);
        keysHighLoc = bufferLoc+keys2;
        int keys1 = grailFindKeys(array, keysHighLoc, length-keysHighLoc, keysNeeded);
        keysLowLoc = keysHighLoc+keys1;
        int keys0 = grailFindKeys(array, keysLowLoc, length-keysLowLoc, keysNeeded);
        keysHighLen = keys1;
        keysLowLen = keys0;
        bufferLen = keys2;
        if(keysHighLen < keysNeeded || keysLowLen < keysNeeded) {
            grailLazyStableSort(array, start, end-start);
        }
        IndexedRotations.helium(array, bufferLoc, keysHighLoc, keysLowLoc+keysLowLen, 0.5, true, false);
        keysLowLoc = bufferLoc+keys1;
        keysHighLoc = bufferLoc;
        bufferLoc += keysHighLen+keysLowLen;
        int bufferStart = bufferLoc;
        sort4Blocks(array, bufferLoc+bufferLen, end);
        int tempEnd = end, lastBlock = bufferLoc+bufferLen;
        end-=length%bufferLen;
        for(int i=4; i<=bufferLen; i*=2) {
            for(int j=bufferLoc+bufferLen;j<end;j+=2*i) {
                int k=Math.min(j+2*i, end);
                pingPongMergeFW(array,j,j+i,k);
            }
            IndexedRotations.holyGriesMills(array, bufferStart, bufferLoc, bufferLoc+bufferLen, 1, true, false);
            bufferLoc=bufferStart;
            lastBlock=bufferLoc+bufferLen+2*i;
            //high=!high;
            //yes, I planned on making it more like updated Vega's blockbuild,
            //but it failed miserably, and I scrapped it
        }
        for(int i=2*bufferLen; i<length; i*=2) {
            while(bufferLoc+bufferLen<end) {
                int k=Math.min(lastBlock+i, end);
                blockSelectFW(array,bufferLoc+bufferLen,lastBlock,k);
                lastBlock=bufferLoc+bufferLen+i;
            }
            IndexedRotations.holyGriesMills(array, bufferStart, bufferLoc, bufferLoc+bufferLen, 1, true, false);
            bufferLoc=bufferStart;
            lastBlock=bufferLoc+bufferLen+2*i;
        }
        if(end<tempEnd) { // ***AAAAAAAAA***
            small.customInsertSort(array, end, tempEnd, 0.125, false);
            pingPongMergeFW(array, bufferLoc+bufferLen, end, tempEnd);
            grailMergeWithoutBuffer(array, keysHighLoc, keysHighLen, keysLowLen);
            grailMergeWithoutBuffer(array, keysHighLoc, keysHighLen +keysLowLen, end-bufferLoc);
            IndexedRotations.holyGriesMills(array, start, tempEnd-bufferLen, tempEnd, 1, true, false);
            small.customInsertSort(array, start, start+bufferLen, 0.125, false);
            grailMergeWithoutBuffer(array, start, bufferLen, length-bufferLen);
        } else {
            grailMergeWithoutBuffer(array, keysHighLoc, keysHighLen, keysLowLen);
            IndexedRotations.holyGriesMills(array, keysHighLoc, bufferLoc, bufferLoc+bufferLen, 1, true, false);
            grailMergeWithoutBuffer(array, keysHighLoc+bufferLen, keysHighLen +keysLowLen, end-bufferStart);
            small.customInsertSort(array, start, start+bufferLen, 0.125, false);
            grailMergeWithoutBuffer(array, start, bufferLen, length-bufferLen);
        }
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        common(array, 0, length);
    }
}