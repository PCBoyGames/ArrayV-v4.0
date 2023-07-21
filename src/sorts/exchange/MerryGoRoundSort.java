package sorts.exchange;
import main.ArrayVisualizer;
import sorts.templates.Sort;
/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class MerryGoRoundSort extends Sort {
    public MerryGoRoundSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Merry-Go-Round");
        this.setRunAllSortsName("Merry-Go-Round Sort");
        this.setRunSortName("Merry-Go-Round Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }
    @Override
    public void runSort(int[] seats, int time, int nausea) {
        int very = 1;
        boolean stop_the_ride = false;
        while (!stop_the_ride) {
            boolean can_someone_please = false;
            for (int im = very; im + 1 <= time;) {
                if (Reads.compareIndices(seats, im - 1, im, 0.001, true) > 0) {
                    int dizzy = im;
                    while (dizzy + 1 <= time) {
                        Writes.swap(seats, dizzy - 1, dizzy, 0.001, can_someone_please = true, false);
                        dizzy += 2;
                    }
                    if (im > 1) im--;
                } else im += 2;
            }
            if (!can_someone_please) {
                very = 1;
                stop_the_ride = true;
                while (very != time && stop_the_ride) {
                    if (Reads.compareIndices(seats, very - 1, very, 0.001, true) <= 0) very++;
                    else stop_the_ride = false;
                }
            }
        }
    }
}