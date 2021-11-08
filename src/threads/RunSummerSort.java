package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.select.CocktailPeelSort;
import sorts.select.InstinctCocktailSandpaperSort;
import sorts.select.InstinctReverseSandpaperSort;
import sorts.select.InstinctSandpaperSort;
import sorts.select.OptimizedReverseSandpaperSort;
import sorts.select.PeelSort;
import sorts.select.ReversePeelSort;
import sorts.templates.Sort;
import utils.Distributions;
import utils.Shuffles;
import utils.StopSort;

/*
 * 
MIT License

Copyright (c) 2019 ArrayV 4.0 Team

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
    boolean stability = false;
    public RunSummerSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        sortCount = numSorts * 50;
        categoryCount = sortCount;
    }

    protected synchronized void runIndividualSort(Sort sort, int bucketCount, int[] array, int defaultLength, double defaultSpeed, boolean slowSort, String shuffleName, int uniques) throws Exception {
        Delays.setSleepRatio(2.5);
        
        if(defaultLength != arrayVisualizer.getCurrentLength()) {
            arrayFrame.setLengthSlider(defaultLength);
        }
        
        if (shuffleName == "Many Similar" || shuffleName == "Stability Test") {
            arrayVisualizer.getArrayFrame().setUniqueSlider(uniques);
        } else {
            arrayVisualizer.getArrayFrame().setUniqueSlider(defaultLength);
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
        Thread.sleep(1000);
        
        sortNumber++;
        
    }


    protected synchronized void runSort(int[] array, String shuffleName) throws Exception {
        
        /*Sort InstinctSandpaper = new InstinctSandpaperSort(arrayVisualizer);
        runIndividualSort(InstinctSandpaper, 0, array, 64, 0.5, false, shuffleName, 16);
        
        Sort InstinctReverseSandpaper = new InstinctReverseSandpaperSort(arrayVisualizer);
        runIndividualSort(InstinctReverseSandpaper, 0, array, 64, 0.5, false, shuffleName, 16);
        
        Sort InstinctCocktailSandpaper = new InstinctCocktailSandpaperSort(arrayVisualizer);
        runIndividualSort(InstinctCocktailSandpaper, 0, array, 64, 0.5, false, shuffleName, 16);*/
        
        Sort OptimizedReverseSandpaper = new OptimizedReverseSandpaperSort(arrayVisualizer);
        runIndividualSort(OptimizedReverseSandpaper, 0, array, 128, 0.5, false, shuffleName, 16);
        
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        
        /*
        RSS BASIC (25)
        */
        
        arrayVisualizer.getArrayManager().setDistribution(Distributions.LINEAR); // 1
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.RANDOM);
        RunSummerSort.this.runSort(array, "Random");
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.REVERSE); // 2
        RunSummerSort.this.runSort(array, "Reversed");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.ALMOST); // 3
        RunSummerSort.this.runSort(array, "Almost Sorted");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.RANDOM); // 4
        RunSummerSort.this.runSort(array, "Many Similar");
        
        arrayVisualizer.setComparator(2);
        RunSummerSort.this.runSort(array, "Stability Test");
		
        arrayVisualizer.setComparator(0);
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SHUFFLED_TAIL_LEGACY); // 5
        RunSummerSort.this.runSort(array, "Scrambled End");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SHUFFLED_HEAD_LEGACY); // 6
        RunSummerSort.this.runSort(array, "Scrambled Start");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.FINAL_MERGE); // 7
        RunSummerSort.this.runSort(array, "Final Merge");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SAWTOOTH); // 8
        RunSummerSort.this.runSort(array, "Sawtooth");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.HALF_ROTATION); // 9
        RunSummerSort.this.runSort(array, "Half-Rotated");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.FINAL_MERGE)
                                         .addSingle(Shuffles.REVERSE); // 10
        RunSummerSort.this.runSort(array, "Reversed Final Merge");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.ORGAN); // 11
        RunSummerSort.this.runSort(array, "Pipe Organ");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.FINAL_RADIX); // 12
        RunSummerSort.this.runSort(array, "Final Radix");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.PAIRWISE); // 13
        RunSummerSort.this.runSort(array, "Final Pairwise");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.BST_TRAVERSAL); // 14
        RunSummerSort.this.runSort(array, "Binary Search Tree");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.HEAPIFIED); // 15
        RunSummerSort.this.runSort(array, "Heap");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.REVERSE)
                                         .addSingle(Shuffles.SMOOTH); // 16
        RunSummerSort.this.runSort(array, "Smooth Heap");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.REVERSE)
                                         .addSingle(Shuffles.POPLAR); // 17
        RunSummerSort.this.runSort(array, "Poplar Heap");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.PARTIAL_REVERSE); // 18
        RunSummerSort.this.runSort(array, "Half-Reversed");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.DOUBLE_LAYERED); // 19
        RunSummerSort.this.runSort(array, "Evens Reversed");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SHUFFLED_ODDS); // 20
        RunSummerSort.this.runSort(array, "Scrambled Odds");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.INTERLACED); // 21
        RunSummerSort.this.runSort(array, "Evens Up, Odds Down");

        arrayVisualizer.getArrayManager().setDistribution(Distributions.BELL_CURVE); // 22
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.ALREADY);
        RunSummerSort.this.runSort(array, "Bell Curve");

        arrayVisualizer.getArrayManager().setDistribution(Distributions.PERLIN_NOISE_CURVE); // 23
        RunSummerSort.this.runSort(array, "Perlin Noise Curve");

        arrayVisualizer.getArrayManager().setDistribution(Distributions.PERLIN_NOISE); // 24
        RunSummerSort.this.runSort(array, "Perlin Noise");

        arrayVisualizer.getArrayManager().setDistribution(Distributions.LINEAR); // 25
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.TRIANGULAR);
        RunSummerSort.this.runSort(array, "Triangular");

        /*
        RSS MADHOUSE PLUS (25)
        */
        
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SHUFFLED_HALF); // 26
        RunSummerSort.this.runSort(array, "Scrambled Back Half");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.NOISY); // 27
        RunSummerSort.this.runSort(array, "Low Disparity");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.PARTITIONED); // 28
        RunSummerSort.this.runSort(array, "Partitioned");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SAWTOOTH)
                                         .addSingle(Shuffles.REVERSE); // 29
        RunSummerSort.this.runSort(array, "Reversed Sawtooth");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.FINAL_BITONIC); // 30
        RunSummerSort.this.runSort(array, "Final Bitonic");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.REC_RADIX); // 31
        RunSummerSort.this.runSort(array, "Recursive Final Radix");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.INV_BST); // 32
        RunSummerSort.this.runSort(array, "Inverted Binary Tree");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.LOG_SLOPES); // 33
        RunSummerSort.this.runSort(array, "Logpile");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.TRI_HEAP); // 34
        RunSummerSort.this.runSort(array, "Triangle Heap");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.CIRCLE); // 35
        RunSummerSort.this.runSort(array, "Circle Pass");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.QSORT_BAD); // 36
        RunSummerSort.this.runSort(array, "Quick Killer");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.PDQ_BAD); // 37
        RunSummerSort.this.runSort(array, "Pattern Quick Killer");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.GRAIL_BAD); // 38
        RunSummerSort.this.runSort(array, "Grail Killer");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SHUF_MERGE_BAD); // 39
        RunSummerSort.this.runSort(array, "Shuffle Killer");

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.BLOCK_RANDOMLY); // 40
        RunSummerSort.this.runSort(array, "Blocks");

        arrayVisualizer.getArrayManager().setDistribution(Distributions.RANDOM); // 41
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.ALREADY);
        RunSummerSort.this.runSort(array, "Natural Random");

        arrayVisualizer.getArrayManager().setDistribution(Distributions.SINE); // 42
        RunSummerSort.this.runSort(array, "Sine Wave");

        arrayVisualizer.getArrayManager().setDistribution(Distributions.COSINE); // 43
        RunSummerSort.this.runSort(array, "Cosine Wave");

        arrayVisualizer.getArrayManager().setDistribution(Distributions.RULER); // 44
        RunSummerSort.this.runSort(array, "Ruler");

        arrayVisualizer.getArrayManager().setDistribution(Distributions.BLANCMANGE); // 45
        RunSummerSort.this.runSort(array, "Blancmange Curve");

        arrayVisualizer.getArrayManager().setDistribution(Distributions.DIVISORS); // 46
        RunSummerSort.this.runSort(array, "Sum of Divisors");

        arrayVisualizer.getArrayManager().setDistribution(Distributions.FSD); // 47
        RunSummerSort.this.runSort(array, "Fly Straight, Dammit!");

        arrayVisualizer.getArrayManager().setDistribution(Distributions.REVLOG); // 48
        RunSummerSort.this.runSort(array, "Decreasing Random");

        arrayVisualizer.getArrayManager().setDistribution(Distributions.MODULO); // 49
        RunSummerSort.this.runSort(array, "Modulo Function");

        arrayVisualizer.getArrayManager().setDistribution(Distributions.DIGITS_PROD); // 50
        RunSummerSort.this.runSort(array, "Product of Digits");
        
        /*
        */
    }
    
    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if(arrayVisualizer.isActive())
            return;

        Sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread() {
            @Override
            public void run() {
                try{
                    if(runAllActive) {
                        RunSummerSort.this.sortNumber = current;
                        RunSummerSort.this.sortCount = total;
                    }
                    else {
                        RunSummerSort.this.sortNumber = 1;
                    }

                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("PCBSAM for ArrayV");

                    RunSummerSort.this.executeSortList(array);
                    
                    if(!runAllActive) {
                        arrayVisualizer.setCategory("Running");
                        arrayVisualizer.setHeading("Done");
                    }
                    
                    arrayManager.toggleMutableLength(true);
                }
                catch (Exception e) {
                    JErrorPane.invokeErrorMessage(e);
                }
                Sounds.toggleSound(false);
                arrayVisualizer.setSortingThread(null);
            }
        });

        arrayVisualizer.runSortingThread();
    }
}