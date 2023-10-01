package sorts.bogo;
import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class LuckyPrimeWeaveHighSort extends MadhouseTools {
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
        int boundi = 1;
        boolean testpass = false;
        boolean visualaesthetic = true;
        int[] tree = factorTree(gap);
        String treeString = "[";
        for (int i = 0; i < tree.length; i++) {
            treeString += (tree[i] + ", ");
        }
        treeString = treeString.substring(0, treeString.length() - 2) + "]";
        arrayVisualizer.setExtraHeading(" / " + gap + " = " + treeString);
        for (int i = 0; i < currentLength; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.25);
        }
        while (!testpass) {
            if (gap > 1) {
                tree = factorTree(gap);
                treeString = "[";
                for (int i = 0; i < tree.length; i++) {
                    treeString += (tree[i] + ", ");
                }
                treeString = treeString.substring(0, treeString.length() - 2) + "]";
                arrayVisualizer.setExtraHeading(" / " + gap + " = " + treeString);
            } else {
                arrayVisualizer.setExtraHeading(" / 1 = []");
            }
            boolean anyswaps = false;
            for (int i = check; (i - 1) + gap < currentLength; i += move) {
                if (Reads.compareIndices(array, i - 1, (i - 1) + gap, 0.125, true) > 0) {
                    if (randInt(1, 101) <= luck) Writes.swap(array, i - 1, (i - 1) + gap, 0.125, true, false);
                    anyswaps = true;
                }
            }
            if (!anyswaps && gap != 1) {
                noswapsfor++;
                if (noswapsfor == move) {
                    noswapsfor = 0;
                    lastmove = move;
                    double primetestrunning = move;
                    if (!visualaesthetic) {
                        while (primetestrunning != 1) {
                            boolean primetest = false;
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
                            boolean primetest = false;
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