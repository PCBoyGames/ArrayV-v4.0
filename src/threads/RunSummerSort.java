package threads;

import main.ArrayManager;
import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.bogo.BoomSort;
import sorts.bogo.PapoSort;
import sorts.concurrent.VanVoorhisFourFourSortIterative;
import sorts.concurrent.VanVoorhisFourFourSortRecursive;
import sorts.distribute.ShnexSort;
import sorts.distribute.SkeadnySort;
import sorts.esoteric.DistributionNetworkSortOnce;
import sorts.esoteric.DistributionNetworkSortTwice;
import sorts.esoteric.SafeAssSort;
import sorts.esoteric.ShuffleNetworkSort;
import sorts.esoteric.ShuffleNetworkSortOnce;
import sorts.exchange.AltQuasimiddleSort;
import sorts.exchange.DigSort;
import sorts.exchange.GoalkeeperSort;
import sorts.exchange.HeadPullRoomSort;
import sorts.exchange.NaoanSort;
import sorts.exchange.NapoleonSortResolve;
import sorts.exchange.NapoleonSortResolveNTS;
import sorts.exchange.OptimizedGoalkeeperSort;
import sorts.exchange.PDSegmentedSort;
import sorts.exchange.ParFutureStrangeSort;
import sorts.exchange.ParSelectFutureStrangeSort;
import sorts.exchange.ThirtySort;
import sorts.exchange.ZigZagSort;
import sorts.exchange.QuasimiddleSort;
import sorts.exchange.SearchSort;
import sorts.exchange.SegmentedSort;
import sorts.exchange.SelectFutureStrangeSort;
import sorts.exchange.StrangeSort;
import sorts.exchange.BadThirtySort;
import sorts.exchange.CaliforniumSort;
import sorts.exchange.EvubSort;
import sorts.exchange.FutureStrangeSort;
import sorts.hybrid.CircleOptimizedWeaveMergeSort;
import sorts.hybrid.ImprovedWeaveMergeSortIII;
import sorts.hybrid.KitaSort;
import sorts.hybrid.OptimizedMystifySort;
import sorts.hybrid.OptimizedWeaveMergeSort;
import sorts.hybrid.PartitionHeapMergeSort;
import sorts.hybrid.UnnamedSort;
import sorts.merge.InPlaceMergeSortII;
import sorts.merge.InPlaceMergeSortIV;
import sorts.misc.InPlaceSmartSafeStalinSort;
import sorts.misc.PDSafeStalinSort;
import sorts.misc.SmartSafeStalinSort;
import sorts.quick.OOPSingularityQuickSort;
import sorts.select.OutOfPlaceStableSelectionSort;
import sorts.select.RectangleSelectionSort;
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
    boolean stabilityproper = true;
    boolean stability = false;

    public RunSummerSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        sortCount = numSorts * 56;
    }

    protected synchronized void runIndividualSort(Sort sort, int bucketCount, int[] array, int defaultLength, double defaultSpeed, boolean slowSort, String shuffleName, int uniques, boolean alt) throws Exception {
        Delays.setSleepRatio(2.5);
        if (defaultLength != arrayVisualizer.getCurrentLength()) arrayFrame.setLengthSlider(defaultLength);
        if (shuffleName == "Many Similar" || shuffleName == "Stability Test") arrayVisualizer.getArrayFrame().setUniqueSlider(uniques);
        else {
            if (alt && alternate_distributions) arrayVisualizer.getArrayFrame().setUniqueSlider(defaultLength / 8);
            else arrayVisualizer.getArrayFrame().setUniqueSlider(defaultLength);
        }
        if (shuffleName == "Stability Test" && !stability) {
            sortNumber -= numSorts;
            stability = true;
        }
        arrayManager.refreshArray(array, arrayVisualizer.getCurrentLength(), arrayVisualizer);
        arrayVisualizer.setHeading(sort.getRunAllSortsName() + " (" + shuffleName + ": " + (numSorts > 1 ? (int) Math.round((sortNumber + 0.5) / numSorts) : sortNumber) + "/" + sortCount / numSorts + ")");
        double sortSpeed = 1.0;
        if (defaultLength < (arrayVisualizer.getCurrentLength() / 2)) sortSpeed = defaultSpeed * Math.pow((arrayVisualizer.getCurrentLength() / 2048d), 2);
        else sortSpeed = defaultSpeed * (arrayVisualizer.getCurrentLength() / 2048d);
        Delays.setSleepRatio(sortSpeed);
        Timer.enableRealTimer();
        try {
            sort.runSort(array, arrayVisualizer.getCurrentLength(), bucketCount);
        } catch (StopSort e)  {
        } catch (Exception e) {
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

        //Sort ZigZag = new ZigZagSort(arrayVisualizer);
        //runIndividualSort(ZigZag, 0, array, 128, 1, false, shuffleName, 16, alt);

        //Sort PDSafeStalin = new PDSafeStalinSort(arrayVisualizer);
        //runIndividualSort(PDSafeStalin, 0, array, 512, 16, false, shuffleName, 16, alt);

        //Sort Search = new SearchSort(arrayVisualizer);
        //runIndividualSort(Search, 0, array, 128, 8, false, shuffleName, 16, alt);

        //Sort InPlaceMergeII = new InPlaceMergeSortII(arrayVisualizer);
        //runIndividualSort(InPlaceMergeII, 0, array, 256, 0.5, false, shuffleName, 16, alt);

        //Sort Evub = new EvubSort(arrayVisualizer);
        //runIndividualSort(Evub, 3, array, 256, 1, false, shuffleName, 16, alt);

        //Sort SmartSafeStalin = new SmartSafeStalinSort(arrayVisualizer);
        //runIndividualSort(SmartSafeStalin, 0, array, 256, 4, false, shuffleName, 16, alt);

        //Sort InPlaceSmartSafeStalin = new InPlaceSmartSafeStalinSort(arrayVisualizer);
        //runIndividualSort(InPlaceSmartSafeStalin, 0, array, 256, 4, false, shuffleName, 16, alt);

        //Sort VV44Rec = new VanVoorhisFourFourSortRecursive(arrayVisualizer);
        //runIndividualSort(VV44Rec, 0, array, 256, 1, false, shuffleName, 16, alt);

        //Sort VV44Itr = new VanVoorhisFourFourSortIterative(arrayVisualizer);
        //runIndividualSort(VV44Itr, 0, array, 256, 1, false, shuffleName, 16, alt);

        //Sort OOPSSelection = new OutOfPlaceStableSelectionSort(arrayVisualizer);
        //runIndividualSort(OOPSSelection, 0, array, 128, 4, false, shuffleName, 16, alt);

        //Sort SafeAss = new ShuffleNetworkSort(arrayVisualizer);
        //runIndividualSort(SafeAss, 21, array, 512, 1, false, shuffleName, 16, alt);

        //Sort RectangleSelection = new RectangleSelectionSort(arrayVisualizer);
        //runIndividualSort(RectangleSelection, 0, array, 128, 0.5, false, shuffleName, 8, alt);

        //Sort UniqueSelection = new UnnamedSort(arrayVisualizer);
        //runIndividualSort(UniqueSelection, 0, array, 512, 1, false, shuffleName, 16, alt);

        Sort Dig = new DigSort(arrayVisualizer);
        runIndividualSort(Dig, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort PartitionHeapMerge = new PartitionHeapMergeSort(arrayVisualizer);
        //runIndividualSort(PartitionHeapMerge, 0, array, 512, 1, false, shuffleName, 16, alt);

    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {

        // A simple shorthand.
        ArrayManager arman = arrayVisualizer.getArrayManager();

        /*/
        RSS BASIC (25)
        /*/

        arman.setDistribution(Distributions.LINEAR); // 1
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_RANDOM : Shuffles.RANDOM);
        runSort(array, "Random", true);
        arman.setShuffleSingle(Shuffles.REVERSE); // 2
        runSort(array, "Reversed", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_ALMOST : Shuffles.ALMOST); // 3
        runSort(array, "Almost Sorted", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_RANDOM : Shuffles.RANDOM); // 4
        runSort(array, "Many Similar", false);
        if (stabilityproper) {
            arrayVisualizer.setComparator(2);
            runSort(array, "Stability Test", false);
            arrayVisualizer.setComparator(0);
        } else {
            arrayVisualizer.setHeading("Stability Test inconsistent with algorithm. Skipping...");
            arrayVisualizer.updateNow();
            Thread.sleep(3000);
        }
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_SHUFFLED_TAIL_LEGACY : Shuffles.SHUFFLED_TAIL_LEGACY); // 5
        runSort(array, "Scrambled End", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_SHUFFLED_HEAD_LEGACY : Shuffles.SHUFFLED_HEAD_LEGACY); // 6
        runSort(array, "Scrambled Start", true);
        arman.setShuffleSingle(Shuffles.FINAL_MERGE); // 7
        runSort(array, "Final Merge", true);
        arman.setShuffleSingle(Shuffles.SAWTOOTH); // 8
        runSort(array, "Sawtooth", true);
        arman.setShuffleSingle(Shuffles.HALF_ROTATION); // 9
        runSort(array, "Half-Rotated", true);
        arman.setShuffleSingle(Shuffles.FINAL_MERGE).addSingle(Shuffles.REVERSE); // 10
        runSort(array, "Reversed Final Merge", true);
        arman.setShuffleSingle(Shuffles.ORGAN); // 11
        runSort(array, "Pipe Organ", true);
        arman.setShuffleSingle(Shuffles.FINAL_RADIX); // 12
        runSort(array, "Final Radix", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_PAIRWISE : Shuffles.PAIRWISE); // 13
        runSort(array, "Final Pairwise", true);
        arman.setShuffleSingle(Shuffles.BST_TRAVERSAL); // 14
        runSort(array, "Binary Search Tree", true);
        arman.setShuffleSingle(Shuffles.HEAPIFIED); // 15
        runSort(array, "Heap", true);
        arman.setShuffleSingle(Shuffles.REVERSE).addSingle(Shuffles.SMOOTH); // 16
        runSort(array, "Smooth Heap", true);
        arman.setShuffleSingle(Shuffles.REVERSE).addSingle(Shuffles.POPLAR); // 17
        runSort(array, "Poplar Heap", true);
        arman.setShuffleSingle(Shuffles.PARTIAL_REVERSE); // 18
        runSort(array, "Half-Reversed", true);
        arman.setShuffleSingle(Shuffles.DOUBLE_LAYERED); // 19
        runSort(array, "Evens Reversed", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_SHUFFLED_ODDS : Shuffles.SHUFFLED_ODDS); // 20
        runSort(array, "Scrambled Odds", true);
        arman.setShuffleSingle(Shuffles.INTERLACED); // 21
        runSort(array, "Evens Up, Odds Down", true);
        arman.setDistribution(Distributions.BELL_CURVE); // 22
        arman.setShuffleSingle(Shuffles.ALREADY);
        runSort(array, "Bell Curve", false);
        arman.setDistribution(Distributions.PERLIN_NOISE_CURVE); // 23
        runSort(array, "Perlin Noise Curve", false);
        arman.setDistribution(seeds ? Distributions.SEEDED_PERLIN_NOISE : Distributions.PERLIN_NOISE); // 24
        runSort(array, "Perlin Noise", false);
        arman.setDistribution(Distributions.LINEAR); // 25
        arman.setShuffleSingle(Shuffles.TRIANGULAR);
        runSort(array, "Triangular", true);

        /*/
        RSS MADHOUSE PLUS (29)
        /*/

        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_SHUFFLED_HALF : Shuffles.SHUFFLED_HALF); // 26
        runSort(array, "Scrambled Back Half", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_SHUFFLED_ENDS : Shuffles.SHUFFLED_ENDS); // 27
        runSort(array, "Both Sides Scrambled", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_ONLY_RUNS : Shuffles.ONLY_RUNS); // 28
        runSort(array, "Random Runs", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_NOISY : Shuffles.NOISY); // 29
        runSort(array, "Low Disparity", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_PARTITIONED :Shuffles.PARTITIONED); // 30
        runSort(array, "Partitioned", true);
        arman.setShuffleSingle(Shuffles.SAWTOOTH).addSingle(Shuffles.REVERSE); // 31
        runSort(array, "Reversed Sawtooth", true);
        arman.setShuffleSingle(Shuffles.FINAL_BITONIC); // 32
        runSort(array, "Final Bitonic", true);
        arman.setShuffleSingle(Shuffles.DOUBLE_LAYERED).addSingle(Shuffles.HALF_ROTATION); // 33
        runSort(array, "Diamond", true);
        arman.setShuffleSingle(Shuffles.REC_RADIX); // 34
        runSort(array, "Recursive Final Radix", true);
        arman.setShuffleSingle(Shuffles.INV_BST); // 35
        runSort(array, "Inverted Binary Tree", true);
        arman.setShuffleSingle(Shuffles.LOG_SLOPES); // 36
        runSort(array, "Logpile", false);
        arman.setShuffleSingle(Shuffles.TRI_HEAP); // 37
        runSort(array, "Triangle Heap", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_CIRCLE : Shuffles.CIRCLE); // 38
        runSort(array, "Circle Pass", true);
        arman.setShuffleSingle(Shuffles.QSORT_BAD); // 39
        runSort(array, "Quick Killer", true);
        arman.setShuffleSingle(Shuffles.PDQ_BAD); // 40
        runSort(array, "Pattern Quick Killer", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_GRAIL_BAD : Shuffles.GRAIL_BAD); // 41
        runSort(array, "Grail Killer", true);
        arman.setShuffleSingle(Shuffles.SHUF_MERGE_BAD); // 42
        runSort(array, "Shuffle Killer", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_BLOCK_RANDOMLY : Shuffles.BLOCK_RANDOMLY); // 43
        runSort(array, "Blocks", true);
        arman.setShuffleSingle(Shuffles.PRIME); // 44
        runSort(array, "Prime-Numbered Index", false);
        arman.setDistribution(seeds ? Distributions.SEEDED_RANDOM : Distributions.RANDOM); // 45
        arman.setShuffleSingle(Shuffles.ALREADY);
        runSort(array, "Natural Random", false);
        arman.setDistribution(Distributions.SINE); // 46
        runSort(array, "Sine Wave", false);
        arman.setDistribution(Distributions.COSINE); // 47
        runSort(array, "Cosine Wave", false);
        arman.setDistribution(Distributions.RULER); // 48
        runSort(array, "Ruler", false);
        arman.setDistribution(Distributions.BLANCMANGE); // 49
        runSort(array, "Blancmange Curve", false);
        arman.setDistribution(Distributions.DIVISORS); // 50
        runSort(array, "Sum of Divisors", false);
        arman.setDistribution(Distributions.FSD); // 51
        runSort(array, "Fly Straight, Dammit!", false);
        arman.setDistribution(seeds ? Distributions.SEEDED_REVLOG : Distributions.REVLOG); // 52
        runSort(array, "Decreasing Random", false);
        arman.setDistribution(Distributions.MODULO); // 53
        runSort(array, "Modulo Function", false);
        arman.setDistribution(Distributions.DIGITS_PROD); // 54
        runSort(array, "Product of Digits", false);
        arman.setDistribution(Distributions.TOTIENT); // 55
        runSort(array, "Euler Totient Function", false);
        arman.setDistribution(Distributions.TWPK_FOUR); // 56
        runSort(array, "TWPK's FOUR", false);

        /*/
        /*/
    }

    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if (arrayVisualizer.isActive()) return;
        Sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(
            new Thread() {
                @Override
                public void run() {
                    try {
                        if (runAllActive) {
                            sortNumber = current;
                            sortCount = total;
                        } else sortNumber = 1;
                        arrayManager.toggleMutableLength(false);
                        arrayVisualizer.setCategory("Seeded Shuffles");
                        arrayVisualizer.setHeading(seeds ? "ON" : "OFF");
                        arrayVisualizer.updateNow();
                        Thread.sleep(3000);
                        arrayVisualizer.setCategory("PCBSAM for ArrayV");
                        //arrayVisualizer.setCategory("naoan1201");
                        arrayVisualizer.setHeading("");
                        arrayVisualizer.updateNow();
                        executeSortList(array);
                        if (!runAllActive) {
                            arrayVisualizer.setCategory("Running");
                            arrayVisualizer.setHeading("Done");
                        }
                        arrayManager.toggleMutableLength(true);
                    } catch (Exception e) { JErrorPane.invokeErrorMessage(e); }
                    Sounds.toggleSound(false);
                    arrayVisualizer.setSortingThread(null);
                }
            }
        );
        arrayVisualizer.runSortingThread();
    }
}