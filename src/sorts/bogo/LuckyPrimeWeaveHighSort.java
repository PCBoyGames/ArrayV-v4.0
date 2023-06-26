package sorts.bogo;
import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class LuckyPrimeWeaveHighSort extends BogoSorting {
    public LuckyPrimeWeaveHighSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lucky Prime Weave (High Prime)");
        this.setRunAllSortsName("Lucky Prime Weave Sort (High Prime)");
        this.setRunSortName("Lucky Prime Weave (High Prime)");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
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
        int check = 1;
        int lastbound = 2;
        int lastmove = currentLength;
        int move = currentLength;
        int noswapsfor = 0;
        int gap = currentLength;
        int primetesti = 2;
        int i = 1;
        int boundi = 1;
        double primetestrunning = 1.0;
        boolean primetest = false;
        boolean anyswaps = false;
        boolean testpass = false;
        boolean visualaesthetic = true;
        while (!testpass) {
            i = check;
            anyswaps = false;
            if ((i - 1) + gap < currentLength) {
                Highlights.markArray(1, i - 1);
                Highlights.markArray(2, (i - 1) + gap);
                Delays.sleep(0.125);
            }
            while ((i - 1) + gap < currentLength) {
                if (Reads.compareIndices(array, i - 1, (i - 1) + gap, 0.125, true) > 0) {
                    if (randInt(1, 101) <= luck) Writes.swap(array, i - 1, (i - 1) + gap, 0.125, true, false);
                    anyswaps = true;
                }
                i += move;
            }
            if (!anyswaps && gap != 1) {
                noswapsfor++;
                if (noswapsfor == move) {
                    noswapsfor = 0;
                    lastmove = move;
                    primetestrunning = move;
                    if (!visualaesthetic) {
                        while (primetestrunning != 1) {
                            primetest = false;
                            primetesti = 2;
                            while (!primetest) {
                                if (Math.floor(primetestrunning / primetesti) == primetestrunning / primetesti) {
                                    primetestrunning = primetestrunning / primetesti;
                                    primetest = true;
                                } else primetesti++;
                            }
                        }
                        move = move / primetesti;
                    }
                    visualaesthetic = false;
                    if (move != 1) {
                        primetestrunning = move;
                        while (primetestrunning != 1) {
                            primetest = false;
                            primetesti = 2;
                            while (!primetest) {
                                if (Math.floor(primetestrunning / primetesti) == primetestrunning / primetesti) {
                                    primetestrunning = primetestrunning / primetesti;
                                    primetest = true;
                                } else primetesti++;
                            }
                        }
                        gap = move / primetesti;
                    } else {
                        move = lastmove;
                        gap = 1;
                    }
                    check = 0;
                }
            } else noswapsfor = 0;
            if (gap == 1) {
                if (lastbound > 1) boundi = lastbound - 1;
                else boundi = 1;
                testpass = true;
                while (boundi < currentLength && testpass) {
                    if (Reads.compareIndices(array, boundi - 1, boundi, 0.125, true) <= 0) boundi++;
                    else {
                        testpass = false;
                        lastbound = boundi;
                        check = boundi;
                    }
                }
            } else check = (check % move) + 1;
        }
    }
}