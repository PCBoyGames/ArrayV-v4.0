package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class PingPongSort extends Sort {
    public PingPongSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Pingpong");
        this.setRunAllSortsName("Pingpong Sort");
        this.setRunSortName("Pingpong Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(32768);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int i = sortLength/2;
        int segComparison = 0; // comparison count of single segment
        boolean forward = true;
        while (true) {
            if (Reads.compareIndices(array, i-1, i, 1, true) > 0) {
                Writes.swap(array, i-1, i, 0, true, false);

                if (i == 1) forward = true;
                else if (i == sortLength-1) forward = false;
                else forward = (forward ? false : true);

                segComparison = 0;
                continue;
            }

            else {
                if (forward) i++;
                else i--;
                segComparison++;
            }

            if (segComparison >= sortLength-1) break;
            else {
                if (i == 0) {
                    forward = true;
                    segComparison = 0;
                    i++;
                }
                else if (i == sortLength) {
                    forward = false;
                    segComparison = 0;
                    i--;
                }
            }
        }
    }
}
