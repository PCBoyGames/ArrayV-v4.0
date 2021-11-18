package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class LuckyNoisySort extends BogoSorting {
    public LuckyNoisySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Noisy");
        this.setRunAllSortsName("Lucky Noisy Sort");
        this.setRunSortName("Lucky Noisy Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
        this.setQuestion("Enter the luck for this sort:", 50);
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1 || answer > 100) return 50;
        return answer;
    }

    @Override
    public void runSort(int[] array, int currentLength, int luck) {
        int left = 1;
        int right = 1;
        int verifyi = 1;
        boolean verifypass = false;
        while (!verifypass) {
            right = verifyi + 1;
            while (right <= currentLength) {
                left = verifyi;
                while (left <= right && right <= currentLength) {
                    Highlights.markArray(1, left - 1);
                    Highlights.markArray(2, right - 1);
                    Delays.sleep(0.005);
                    if (Reads.compareValues(array[left - 1], array[right - 1]) > 0) {
                        if (randInt(1, 101) <= luck) Writes.swap(array, left - 1, right - 1, 0.005, true, false);
                        if (right - 1 > verifyi) right--;
                        left = verifyi;
                    } else left++;
                }
                right += 16;
            }
            verifyi = 1;
            verifypass = true;
            while (verifyi < currentLength && verifypass) {
                Highlights.markArray(1, verifyi - 1);
                Highlights.markArray(2, verifyi);
                Delays.sleep(0.005);
                if (Reads.compareValues(array[verifyi - 1], array[verifyi]) <= 0) verifyi++;
                else verifypass = false;
            }
        }
    }
}