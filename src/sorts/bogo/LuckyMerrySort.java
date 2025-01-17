package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class LuckyMerrySort extends BogoSorting {
    public LuckyMerrySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Merry-Go-Round");
        this.setRunAllSortsName("Lucky Merry-Go-Round Sort");
        this.setRunSortName("Lucky Merry-Go-Round Sort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(true);
        this.setQuestion("Enter the luck for this sort:", 50);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 100) return 50;
        return answer;
    }

    @Override
    public void runSort(int[] seats, int time, int nausea) {
        int im = 1;
        int very = 1;
        int dizzy = 1;
        boolean can_someone_please = false;
        boolean stop_the_ride = false;
        while (!stop_the_ride) {
            im = very;
            can_someone_please = false;
            while (im + 1 <= time) {
                if (Reads.compareIndices(seats, im - 1, im, 0.001, true) > 0) {
                    can_someone_please = true;
                    dizzy = im;
                    while (dizzy + 1 <= time) {
                        if (randInt(1, 101) <= nausea) Writes.swap(seats, dizzy - 1, dizzy, 0.001, true, false);
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