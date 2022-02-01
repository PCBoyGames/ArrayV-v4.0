package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

class QRange {
    public int start, end;
    public boolean usable;

    public QRange() {
        start = end = 0;
        usable = true;
    }

    public QRange(int start, int end) {
        this();
        this.start = start;
        this.end = end;
    }

    public void set(int start, int end) {
        this.start = start;
        this.end = end;
        if(start >= end)
            this.disqualify();
    }

    public int length() {
        return end-start+1;
    }

    public void disqualify() {
        this.usable = false;
    }
}
final public class TriRandomGnomeSort extends BogoSorting {
    public TriRandomGnomeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Tri-Random Gnome");
        this.setRunAllSortsName("Tri-Random Gnome Sort");
        this.setRunSortName("Tri-Random Gnomesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private static int randInt(QRange range) {
        return randInt(range.start, range.end);
    }

    private int searchBin(int[] array, int start, int end, int key, double sleep) {
        while(start < end-1) {
            int r1 = randInt(start, end),
                r2 = randInt(start, end);
            if(r2 < r1) {
                int t=r2;
                r2  = r1;
                r1  =  t;
            }
            if(Reads.compareValues(array[r1], key) == -1) {
                Highlights.markArray(4, start);
                start = r1;
            }
            if(Reads.compareValues(array[r2], key) == 1) {
                Highlights.markArray(4, end);
                end = r2;
            }
            Highlights.markArray(2, r1);
            Highlights.markArray(3, r2);
            Delays.sleep(sleep);
        }
        Highlights.clearMark(2);
        Highlights.clearMark(3);
        return Reads.compareValues(array[start], key) == 1 ? start : end;
    }

    private void triGnomeSort(int[] array, int start, int end, double compSleep, double swapSleep) {
        int randomStart = randInt(start,end);
        QRange sortedArea = new QRange(randomStart,randomStart), secA = new QRange(start,randomStart),
               secB = new QRange(randomStart,end);

        while(secA.usable || secB.usable) {
            boolean dec = randBoolean();
            QRange ref = secB;
            if(!secA.usable) {
                dec = false;
            } else if(dec || !secB.usable) {
                dec = true;
                ref = secA;
            }

            int searchKey = randInt(ref),
                searchValue = this.searchBin(array, sortedArea.start, sortedArea.end,
                                              array[searchKey], compSleep);

            Writes.multiSwap(array, searchKey, dec ? searchValue-1 : searchValue, swapSleep, true, false);

            if(dec) {
                ref.set(ref.start, ref.end-1);
                sortedArea.set(sortedArea.start-1, sortedArea.end);
            } else {
                ref.set(ref.start+1, ref.end);
                sortedArea.set(sortedArea.start, sortedArea.end+1);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.triGnomeSort(array, 0, currentLength, 1, 0.05);
    }
}