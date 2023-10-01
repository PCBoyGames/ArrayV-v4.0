package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class LuckyNoisySort extends BogoSorting {
    public LuckyNoisySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Noisy");
        this.setRunAllSortsName("Lucky Noisy Sort");
        this.setRunSortName("Lucky Noisy Sort");
        this.setCategory("Bogo Sorts");
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
        int verifyi = 1;
        boolean verifypass = false;
        while (!verifypass) {
            for (int right = verifyi + 1; right <= currentLength; right += 16) {
                int left = verifyi;
                while (left <= right && right <= currentLength) {
                    if (Reads.compareIndices(array, left - 1, right - 1, 0.005, true) > 0) {
                        if (randInt(1, 101) <= luck) Writes.swap(array, left - 1, right - 1, 0.005, true, false);
                        if (right - 1 > verifyi) right--;
                        left = verifyi;
                    } else left++;
                }
            }
            verifyi = 1;
            verifypass = true;
            while (verifyi < currentLength && verifypass) {
                if (Reads.compareIndices(array, verifyi - 1, verifyi, 0.005, true) <= 0) verifyi++;
                else verifypass = false;
            }
        }
    }
}