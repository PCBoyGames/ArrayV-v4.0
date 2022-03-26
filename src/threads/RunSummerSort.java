package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.bogo.BoomSort;
import sorts.exchange.AltQuasimiddleSort;
import sorts.exchange.GoalkeeperSort;
import sorts.exchange.HeadPullRoomSort;
import sorts.exchange.OptimizedGoalkeeperSort;
import sorts.exchange.ThirtySort;
import sorts.exchange.ZigZagSort;
import sorts.exchange.QuasimiddleSort;
import sorts.exchange.BadThirtySort;
import sorts.hybrid.KitaSort;
import sorts.templates.Sort;
import utils.Distributions;
import utils.Shuffles;
import utils.StopSort;

/*
 *
MIT License

Copyright (c) 2019-2022 ArrayV 4.0 Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

final public class RunSummerSort extends MultipleSortThread {
    int numSorts = 1;
    boolean alternate_distributions = false;
    boolean seeds = false;

    boolean stability = false;
    public RunSummerSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        sortCount = numSorts * 53;
    }

    protected synchronized void runIndividualSort(Sort sort, int bucketCount, int[] array, int defaultLength, double defaultSpeed, boolean slowSort, String shuffleName, int uniques, boolean alt) throws Exception {
        Delays.setSleepRatio(2.5);

        if(defaultLength != arrayVisualizer.getCurrentLength()) {
            arrayFrame.setLengthSlider(defaultLength);
        }

        if (shuffleName == "Many Similar" || shuffleName == "Stability Test") {
            arrayVisualizer.getArrayFrame().setUniqueSlider(uniques);
        } else {
            if (alt && alternate_distributions) arrayVisualizer.getArrayFrame().setUniqueSlider(defaultLength / 8);
            else arrayVisualizer.getArrayFrame().setUniqueSlider(defaultLength);
        }

        if (shuffleName == "Stability Test" && !stability) {
            sortNumber -= numSorts;
            stability = true;
        }

        arrayManager.refreshArray(array, arrayVisualizer.getCurrentLength(), arrayVisualizer);

        arrayVisualizer.setHeading(sort.getRunAllSortsName() + " on " + shuffleName + " (Sort " + sortNumber + " of " + sortCount + ")");

        double sortSpeed = 1.0;

        if(defaultLength < (arrayVisualizer.getCurrentLength() / 2)) {
            sortSpeed = defaultSpeed * Math.pow((arrayVisualizer.getCurrentLength() / 2048d), 2);
        }
        else {
            sortSpeed = defaultSpeed * (arrayVisualizer.getCurrentLength() / 2048d);
        }

        Delays.setSleepRatio(sortSpeed);

        Timer.enableRealTimer();

        try {
            sort.runSort(array, arrayVisualizer.getCurrentLength(), bucketCount);
        }
        catch (StopSort e) { }
        catch (Exception e) {
            JErrorPane.invokeErrorMessage(e);
        }

        arrayVisualizer.endSort();
        Thread.sleep(10);
        arrayVisualizer.updateNow();
        Thread.sleep(990);
        arrayVisualizer.updateNow();

        sortNumber++;

    }

    protected synchronized void runSort(int[] array, String shuffleName, boolean alt) throws Exception {

        //Sort Quasimiddle = new QuasimiddleSort(arrayVisualizer);
        //runIndividualSort(Quasimiddle, 0, array, 128, 1, false, shuffleName, 16, alt);
        
        //Sort AltQuasimiddle = new AltQuasimiddleSort(arrayVisualizer);
        //runIndividualSort(AltQuasimiddle, 0, array, 128, 1, false, shuffleName, 16, alt);
        
        //Sort BadThirty = new BadThirtySort(arrayVisualizer);
        //runIndividualSort(BadThirty, 0, array, 64, 1, false, shuffleName, 16, alt);
        
        //Sort Thirty = new ThirtySort(arrayVisualizer);
        //runIndividualSort(Thirty, 0, array, 128, 0.25, false, shuffleName, 16, alt);
        
        //Sort HeadPullRoom = new HeadPullRoomSort(arrayVisualizer);
        //runIndividualSort(HeadPullRoom, 0, array, 128, 1, false, shuffleName, 16, alt);
        
        //Sort Boom = new BoomSort(arrayVisualizer);
        //runIndividualSort(Boom, 0, array, 128, 1, false, shuffleName, 16, alt);
        
        //Sort Kita = new KitaSort(arrayVisualizer);
        //runIndividualSort(Kita, 0, array, 1024, 1, false, shuffleName, 16, alt);
        
        Sort ZigZag = new ZigZagSort(arrayVisualizer);
        runIndividualSort(ZigZag, 0, array, 128, 1, false, shuffleName, 16, alt);

    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {

        /*
        RSS BASIC (25)
        */

        arrayVisualizer.getArrayManager().setDistribution(Distributions.LINEAR); // 1
        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_RANDOM : Shuffles.RANDOM);
        runSort(array, "Random", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.REVERSE); // 2
        runSort(array, "Reversed", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_ALMOST : Shuffles.ALMOST); // 3
        runSort(array, "Almost Sorted", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_RANDOM : Shuffles.RANDOM); // 4
        runSort(array, "Many Similar", false);

        arrayVisualizer.setComparator(2);
        runSort(array, "Stability Test", false);

        arrayVisualizer.setComparator(0);
        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_SHUFFLED_TAIL_LEGACY : Shuffles.SHUFFLED_TAIL_LEGACY); // 5
        runSort(array, "Scrambled End", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_SHUFFLED_HEAD_LEGACY : Shuffles.SHUFFLED_HEAD_LEGACY); // 6
        runSort(array, "Scrambled Start", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.FINAL_MERGE); // 7
        runSort(array, "Final Merge", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SAWTOOTH); // 8
        runSort(array, "Sawtooth", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.HALF_ROTATION); // 9
        runSort(array, "Half-Rotated", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.FINAL_MERGE)
                                         .addSingle(Shuffles.REVERSE); // 10
        runSort(array, "Reversed Final Merge", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.ORGAN); // 11
        runSort(array, "Pipe Organ", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.FINAL_RADIX); // 12
        runSort(array, "Final Radix", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_PAIRWISE : Shuffles.PAIRWISE); // 13
        runSort(array, "Final Pairwise", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.BST_TRAVERSAL); // 14
        runSort(array, "Binary Search Tree", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.HEAPIFIED); // 15
        runSort(array, "Heap", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.REVERSE)
                                         .addSingle(Shuffles.SMOOTH); // 16
        runSort(array, "Smooth Heap", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.REVERSE)
                                         .addSingle(Shuffles.POPLAR); // 17
        runSort(array, "Poplar Heap", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.PARTIAL_REVERSE); // 18
        runSort(array, "Half-Reversed", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.DOUBLE_LAYERED); // 19
        runSort(array, "Evens Reversed", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_SHUFFLED_ODDS : Shuffles.SHUFFLED_ODDS); // 20
        runSort(array, "Scrambled Odds", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.INTERLACED); // 21
        runSort(array, "Evens Up, Odds Down", true);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.BELL_CURVE); // 22
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.ALREADY);
        runSort(array, "Bell Curve", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.PERLIN_NOISE_CURVE); // 23
        runSort(array, "Perlin Noise Curve", false);

        arrayVisualizer.getArrayManager().setDistribution(seeds ? Distributions.SEEDED_PERLIN_NOISE : Distributions.PERLIN_NOISE); // 24
        runSort(array, "Perlin Noise", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.LINEAR); // 25
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.TRIANGULAR);
        runSort(array, "Triangular", true);

        /*
        RSS MADHOUSE PLUS (28)
        */

        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_SHUFFLED_HALF : Shuffles.SHUFFLED_HALF); // 26
        runSort(array, "Scrambled Back Half", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_SHUFFLED_ENDS : Shuffles.SHUFFLED_ENDS); // 27
        runSort(array, "Both Sides Scrambled", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_NOISY : Shuffles.NOISY); // 28
        runSort(array, "Low Disparity", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_PARTITIONED :Shuffles.PARTITIONED); // 29
        runSort(array, "Partitioned", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SAWTOOTH)
                                    .addSingle(Shuffles.REVERSE); // 30
        runSort(array, "Reversed Sawtooth", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.FINAL_BITONIC); // 31
        runSort(array, "Final Bitonic", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.REC_RADIX); // 32
        runSort(array, "Recursive Final Radix", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.INV_BST); // 33
        runSort(array, "Inverted Binary Tree", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.LOG_SLOPES); // 34
        runSort(array, "Logpile", false);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.TRI_HEAP); // 35
        runSort(array, "Triangle Heap", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_CIRCLE : Shuffles.CIRCLE); // 36
        runSort(array, "Circle Pass", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.QSORT_BAD); // 37
        runSort(array, "Quick Killer", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.PDQ_BAD); // 38
        runSort(array, "Pattern Quick Killer", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_GRAIL_BAD : Shuffles.GRAIL_BAD); // 39
        runSort(array, "Grail Killer", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SHUF_MERGE_BAD); // 40
        runSort(array, "Shuffle Killer", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_BLOCK_RANDOMLY : Shuffles.BLOCK_RANDOMLY); // 41
        runSort(array, "Blocks", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.PRIME); // 42
        runSort(array, "Prime-Numbered Index", true);

        arrayVisualizer.getArrayManager().setDistribution(seeds ? Distributions.SEEDED_RANDOM : Distributions.RANDOM); // 43
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.ALREADY);
        runSort(array, "Natural Random", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.SINE); // 44
        runSort(array, "Sine Wave", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.COSINE); // 45
        runSort(array, "Cosine Wave", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.RULER); // 46
        runSort(array, "Ruler", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.BLANCMANGE); // 47
        runSort(array, "Blancmange Curve", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.DIVISORS); // 48
        runSort(array, "Sum of Divisors", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.FSD); // 49
        runSort(array, "Fly Straight, Dammit!", false);

        arrayVisualizer.getArrayManager().setDistribution(seeds ? Distributions.SEEDED_REVLOG : Distributions.REVLOG); // 50
        runSort(array, "Decreasing Random", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.MODULO); // 51
        runSort(array, "Modulo Function", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.DIGITS_PROD); // 52
        runSort(array, "Product of Digits", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.TOTIENT); // 53
        runSort(array, "Euler Totient Function", false);

        /*
        */
    }

    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if(arrayVisualizer.isActive()) return;

        Sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread() {
            @Override
            public void run() {
                try{
                    if(runAllActive) {
                        sortNumber = current;
                        sortCount = total;
                    }
                    else sortNumber = 1;

                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("Seeded Shuffles");
                    arrayVisualizer.setHeading(seeds ? "ON" : "OFF");

                    arrayVisualizer.updateNow();
                    Thread.sleep(3000);

                    arrayVisualizer.setCategory("fungamer2");
                    arrayVisualizer.setHeading("");

                    arrayVisualizer.updateNow();

                    executeSortList(array);

                    if(!runAllActive) {
                        arrayVisualizer.setCategory("Running");
                        arrayVisualizer.setHeading("Done");
                    }

                    arrayManager.toggleMutableLength(true);
                }
                catch (Exception e) { JErrorPane.invokeErrorMessage(e); }
                Sounds.toggleSound(false);
                arrayVisualizer.setSortingThread(null);
            }
        });

        arrayVisualizer.runSortingThread();
    }
}