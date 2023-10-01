package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.quick.*;
import sorts.templates.Sort;
import utils.Shuffles;

/*
 *
MIT License

Copyright (c) 2021 ArrayV 4.0 Team

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

public class RunQuickSorts extends MultipleSortThread {
    private Sort CubeRootQuickSort;
    private Sort LLQuickSort;
    private Sort LLQuickSortMiddlePivot;
    private Sort LRQuickSort;
    private Sort DualPivotQuickSort;
    private Sort StableQuickSort;
    private Sort StableQuickSortMiddlePivot;
    private Sort ForcedStableQuickSort;
    private Sort LazyStableQuickSort;
    private Sort TableSort;
    private Sort ooPQuickSort;
    private Sort IndexQuickSort;
    private Sort LRQuickSortParallel;
    private Sort StableQuickSortParallel;
    private Sort StacklessQuickSort;
    private Sort IterativeQuickSort;
    private Sort IntroSort;
    private Sort OptimizedDualPivotQuickSort;
    private Sort StupidQuickSort;
    private Sort LAQuickSort;
    private Sort MedianOfSixteenAdaptiveQuickSort;
    private Sort StacklessHybridQuickSort;
    private Sort PDQBranchedSort;
    private Sort PDQBranchlessSort;
    private Sort BubblescanQuickSort;
    private Sort StacklessDualPivotQuickSort;

    public RunQuickSorts(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.sortCount = 26;
        this.categoryCount = this.sortCount;

        LLQuickSort                      = new                      LLQuickSort(this.arrayVisualizer);
        LLQuickSortMiddlePivot           = new           LLQuickSortMiddlePivot(this.arrayVisualizer);
        LRQuickSort                      = new                      LRQuickSort(this.arrayVisualizer);
        DualPivotQuickSort               = new               DualPivotQuickSort(this.arrayVisualizer);
        StableQuickSort                  = new                  StableQuickSort(this.arrayVisualizer);
        StableQuickSortMiddlePivot       = new       StableQuickSortMiddlePivot(this.arrayVisualizer);
        ooPQuickSort                     = new                     ooPQuicksort(this.arrayVisualizer);
        ForcedStableQuickSort            = new            ForcedStableQuickSort(this.arrayVisualizer);
        LazyStableQuickSort              = new              LazyStableQuickSort(this.arrayVisualizer);
        TableSort                        = new                        TableSort(this.arrayVisualizer);
        IndexQuickSort                   = new                   IndexQuickSort(this.arrayVisualizer);
        LRQuickSortParallel              = new              LRQuickSortParallel(this.arrayVisualizer);
        StableQuickSortParallel          = new          StableQuickSortParallel(this.arrayVisualizer);
        StacklessQuickSort               = new               StacklessQuickSort(this.arrayVisualizer);
        IterativeQuickSort               = new               IterativeQuickSort(this.arrayVisualizer);
        CubeRootQuickSort                = new                CubeRootQuickSort(this.arrayVisualizer);
        IntroSort                        = new                        IntroSort(this.arrayVisualizer);
        OptimizedDualPivotQuickSort      = new      OptimizedDualPivotQuickSort(this.arrayVisualizer);
        StupidQuickSort                  = new                  StupidQuickSort(this.arrayVisualizer);
        LAQuickSort                      = new                      LAQuickSort(this.arrayVisualizer);
        MedianOfSixteenAdaptiveQuickSort = new MedianOfSixteenAdaptiveQuickSort(this.arrayVisualizer);
        StacklessHybridQuickSort         = new         StacklessHybridQuickSort(this.arrayVisualizer);
        PDQBranchedSort                  = new                  PDQBranchedSort(this.arrayVisualizer);
        PDQBranchlessSort                = new                PDQBranchlessSort(this.arrayVisualizer);
        BubblescanQuickSort              = new              BubblescanQuickSort(this.arrayVisualizer);
        StacklessDualPivotQuickSort      = new      StacklessDualPivotQuickSort(this.arrayVisualizer);
    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {
        RunQuickSorts.this.runIndividualSort(LLQuickSort,                   0,   array, 2048, arrayManager.containsShuffle(Shuffles.RANDOM) ? 1.5 : 5, false);
        RunQuickSorts.this.runIndividualSort(LLQuickSortMiddlePivot,        0,   array, 2048, 1.5,   false);
        RunQuickSorts.this.runIndividualSort(LRQuickSort,                   0,   array, 2048, 1,     false);
        RunQuickSorts.this.runIndividualSort(LRQuickSortParallel,           0,   array, 2048, 1,     false);
        RunQuickSorts.this.runIndividualSort(DualPivotQuickSort,            0,   array, 2048, 1,     false);
        RunQuickSorts.this.runIndividualSort(StacklessQuickSort,            0,   array, 2048, 1,     false);
        RunQuickSorts.this.runIndividualSort(IterativeQuickSort,            0,   array, 2048, 1,     false);
        RunQuickSorts.this.runIndividualSort(StableQuickSort,               0,   array, 2048, arrayManager.containsShuffle(Shuffles.RANDOM) ? 1 : 6.5, false);
        RunQuickSorts.this.runIndividualSort(StableQuickSortMiddlePivot,    0,   array, 2048, 1,     false);
        RunQuickSorts.this.runIndividualSort(ooPQuickSort,                  0,   array, 2048, 1,     false);
        RunQuickSorts.this.runIndividualSort(StableQuickSortParallel,       0,   array, 2048, 1,     false);
        RunQuickSorts.this.runIndividualSort(ForcedStableQuickSort,         0,   array, 2048, 1,     false);
        RunQuickSorts.this.runIndividualSort(LazyStableQuickSort,           0,   array, 256,  0.5,   false);
        RunQuickSorts.this.runIndividualSort(TableSort,                     0,   array, 1024, 0.75,  false);
        RunQuickSorts.this.runIndividualSort(IndexQuickSort,                0,   array, 1024, 0.75,  false);
        RunQuickSorts.this.runIndividualSort(CubeRootQuickSort,             0,   array, 2048, arrayManager.containsShuffle(Shuffles.RANDOM) ? 1 : 6.5, false);
        RunQuickSorts.this.runIndividualSort(BubblescanQuickSort,              0, array, 2048, 1,    false);
        RunQuickSorts.this.runIndividualSort(IntroSort,                        0, array, 2048, 1,    false);
        RunQuickSorts.this.runIndividualSort(OptimizedDualPivotQuickSort,      0, array, 2048, 0.75, false);
        RunQuickSorts.this.runIndividualSort(StupidQuickSort,                  0, array, 1024, 1,    false);
        RunQuickSorts.this.runIndividualSort(LAQuickSort,                      0, array, 2048, 0.75, false);
        RunQuickSorts.this.runIndividualSort(MedianOfSixteenAdaptiveQuickSort, 0, array, 1024, 0.25, false);
        RunQuickSorts.this.runIndividualSort(StacklessHybridQuickSort,         0, array, 2048, 0.75, false);
        RunQuickSorts.this.runIndividualSort(StacklessDualPivotQuickSort,      0, array, 2048, 0.75, false);
        RunQuickSorts.this.runIndividualSort(PDQBranchedSort,                  0, array, 2048, 0.75, false);
        RunQuickSorts.this.runIndividualSort(PDQBranchlessSort,                0, array, 2048, 0.75, false);
    }

    @Override
    protected synchronized void runThread(int[] array, int current, int total, boolean runAllActive) throws Exception {
        if (arrayVisualizer.isActive())
            return;

        Sounds.toggleSound(true);
        arrayVisualizer.setSortingThread(new Thread("QuickSorts") {
            @Override
            public void run() {
                try{
                    if (runAllActive) {
                        RunQuickSorts.this.sortNumber = current;
                        RunQuickSorts.this.sortCount = total;
                    }
                    else {
                        RunQuickSorts.this.sortNumber = 1;
                    }

                    arrayManager.toggleMutableLength(false);

                    arrayVisualizer.setCategory("Quick Sorts");

                    RunQuickSorts.this.executeSortList(array);

                    if (!runAllActive) {
                        arrayVisualizer.setCategory("Run Quick Sorts");
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
