package threads;

import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.bogo.BirthdayMergeSort;
import sorts.bogo.BirthdaySort;
import sorts.bogo.BovoSort;
import sorts.bogo.ChirSort;
import sorts.bogo.CospoSort;
import sorts.bogo.ExchangeBojoSort;
import sorts.bogo.ExchangeChirMergeSort;
import sorts.bogo.ExchangeChirSort;
import sorts.bogo.ExchangeGoroSort;
import sorts.bogo.LuckyBubbleSort;
import sorts.bogo.LuckyCircleSort;
import sorts.bogo.LuckyCircloidSort;
import sorts.bogo.LuckyCocktailSort;
import sorts.bogo.LuckyCombSort;
import sorts.bogo.LuckyDebrisSort;
import sorts.bogo.LuckyGrateSort;
import sorts.bogo.LuckyMerrySort;
import sorts.bogo.LuckyNoisySort;
import sorts.bogo.LuckyOddEvenSort;
import sorts.bogo.LuckyPopSort;
import sorts.bogo.LuckyPrimeWeaveHighSort;
import sorts.bogo.LuckyPrimeWeaveLowSort;
import sorts.bogo.LuckyRougeSort;
import sorts.bogo.OptimizedBubbleBogoMergeSort;
import sorts.bogo.OptimizedBubbleBogoSort;
import sorts.bogo.SemiDeterministicBovoSort;
import sorts.bogo.WatermelonWavesSort;
import sorts.distribute.FlightSort;
import sorts.exchange.AdaptiveBinaryClamberSort;
import sorts.exchange.AdaptiveClamberSort;
import sorts.exchange.AdaptiveExponentialClamberSort;
import sorts.exchange.AdaptiveFibonacciClamberSort;
import sorts.exchange.AdaptiveRandomClamberSort;
import sorts.exchange.AdaptiveSquareClamberSort;
import sorts.exchange.AdaptiveTriClamberSort;
import sorts.exchange.AsteraceaeSort;
import sorts.exchange.BaseNOddEvenSort;
import sorts.exchange.CircleSortInverseIterative;
import sorts.exchange.CircleSortRouge;
import sorts.exchange.ClamberSort;
import sorts.exchange.ClericSortIterative;
import sorts.exchange.ClericSortRecursive;
import sorts.exchange.CocktailPushSort;
import sorts.exchange.DebrisIterativePopSort;
import sorts.exchange.DebrisSort;
import sorts.exchange.DebrisSortEquality;
import sorts.exchange.FateSort;
import sorts.exchange.FireSort;
import sorts.exchange.ForwardRunShoveSort;
import sorts.exchange.GnomeWeaveHighSort;
import sorts.exchange.GnomeWeaveLowSort;
import sorts.exchange.HeadPullSort;
import sorts.exchange.HesvoSort;
import sorts.exchange.InOrderShoveSort;
import sorts.exchange.InstinctBubbleSort;
import sorts.exchange.InstinctClamberSort;
import sorts.exchange.IterativePopSort;
import sorts.exchange.MerryGoRoundSort;
import sorts.exchange.MoreOptimizedBubbleSort;
import sorts.exchange.NaturalHeadPullSort;
import sorts.exchange.NoisySort;
import sorts.exchange.OddEvenWeaveHighSort;
import sorts.exchange.OddEvenWeaveLowSort;
import sorts.exchange.OptimizedBaseNOddEvenSort;
import sorts.exchange.OptimizedCocktailGrateSort;
import sorts.exchange.OptimizedGrateSort;
import sorts.exchange.OptimizedOddEvenSort;
import sorts.exchange.OptimizedPushSort;
import sorts.exchange.OptimizedReverseGrateSort;
import sorts.exchange.OptimizedShoveSort;
import sorts.exchange.OptimizedSplitCenterSort;
import sorts.exchange.OptimizedStrangePushSort;
import sorts.exchange.OptimizedWhy2Sort;
import sorts.exchange.OptimizedWhySort;
import sorts.exchange.OptimizedZipperSort;
import sorts.exchange.PDBinaryClamberSort;
import sorts.exchange.PDIterativePopPopSort;
import sorts.exchange.PDIterativePopSort;
import sorts.exchange.PDIterativePopSortUnstable;
import sorts.exchange.PDPopSort;
import sorts.exchange.PlaygroundSort;
import sorts.exchange.PopPopSort;
import sorts.exchange.PushSort;
import sorts.exchange.ReflectionSort;
import sorts.exchange.ReverseClamberSort;
import sorts.exchange.ReversePushSort;
import sorts.exchange.RougeSpacePopSort;
import sorts.exchange.ShircleSortIterative;
import sorts.exchange.ShircleSortRecursive;
import sorts.exchange.SnowballIterativePopSort;
import sorts.exchange.SnowballSort;
import sorts.exchange.SplitCenterSort;
import sorts.exchange.StableForwardRunShoveSort;
import sorts.exchange.StablePlaygroundSort;
import sorts.exchange.StrangePushSort;
import sorts.exchange.StrangeSort;
import sorts.exchange.StupidFireSort;
import sorts.exchange.StupidlyDaftOnionPopStoogeSort;
import sorts.exchange.SwaplessHeadPullSort;
import sorts.exchange.SwaplessPlaygroundSort;
import sorts.exchange.SwaplessPushSort;
import sorts.exchange.TableAdaBinClamberSort;
import sorts.exchange.TableClamberSort;
import sorts.exchange.UnbelievableSort;
import sorts.exchange.UnoptimizedFateSort;
import sorts.exchange.WeirdInsertionSort;
import sorts.exchange.Why2Sort;
import sorts.exchange.WhySort;
import sorts.exchange.XSort;
import sorts.exchange.ZipperSort;
import sorts.hybrid.ClarkSort;
import sorts.hybrid.MystifySort;
import sorts.hybrid.OptimizedMystifySort;
import sorts.hybrid.RecursivePushSort;
import sorts.hybrid.ShockSort;
import sorts.hybrid.ShockSortAlt;
import sorts.hybrid.TumbleweedSort;
import sorts.insert.ExpandingRoomSort;
import sorts.insert.OptimizedGambitInsertionSort;
import sorts.insert.OptimizedRoomSort;
import sorts.insert.RandomShellSort;
import sorts.insert.ShellHighSort;
import sorts.insert.ShellLowSort;
import sorts.insert.StrangeExpandingRoomSort;
import sorts.merge.AdaptiveBinfaClurgeNewSolSort;
import sorts.merge.AdaptiveClurgeNewSolSort;
import sorts.merge.FlightMergeSort;
import sorts.merge.MobMergeSort;
import sorts.merge.SnowballMergeSort;
import sorts.merge.SplitCenterMergeSort;
import sorts.misc.CupcakeWrapperSort;
import sorts.misc.SafeStalinSort;
import sorts.quick.HeadPullQuickSort;
import sorts.quick.IgnorantQuickSort;
import sorts.quick.MagneticaQuickSort;
import sorts.quick.SingularityQuickSort;
import sorts.quick.SwaplessIgnorantQuickSort;
import sorts.quick.UnboundedSingularityQuickSort;
import sorts.quick.UnboundedUnstableSingularityQuickSort;
import sorts.quick.UnstableSingularityQuickSort;
import sorts.select.CocktailPeelSort;
import sorts.select.CocktailSandpaperSort;
import sorts.select.EcoloSort;
import sorts.select.FallSort;
import sorts.select.HeapHunterSort;
import sorts.select.InstinctCocktailSandpaperSort;
import sorts.select.InstinctReverseSandpaperSort;
import sorts.select.InstinctSandpaperSort;
import sorts.select.OptimizedReverseSandpaperSort;
import sorts.select.PeelSort;
import sorts.select.ReversePeelSort;
import sorts.select.ScrollHeapHunterSort;
import sorts.select.ShepherdSort;
import sorts.select.ShoveSandpaperSort;
import sorts.select.StableFallSort;
import sorts.templates.Sort;
import utils.Distributions;
import utils.Shuffles;
import utils.StopSort;

/*
 * 
MIT License

Copyright (c) 2019-2021 ArrayV 4.0 Team

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
    int numSorts = 2;
    boolean alternate_distributions = false;
    boolean seeds = true;
    
    boolean stability = false;
    public RunSummerSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        sortCount = numSorts * 50;
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
        
        Sort Mystify = new MystifySort(arrayVisualizer);
        runIndividualSort(Mystify, 2, array, 512, 2, false, shuffleName, 16, alt);
        
        Sort OptimizedMystify = new OptimizedMystifySort(arrayVisualizer);
        runIndividualSort(OptimizedMystify, 2, array, 512, 2, false, shuffleName, 16, alt);
        
        /*
        
        
        Sort AdaptiveBinaryClamber = new AdaptiveBinaryClamberSort(arrayVisualizer);
        runIndividualSort(AdaptiveBinaryClamber, 0, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort AdaptiveBinfaClurge = new AdaptiveBinfaClurgeNewSolSort(arrayVisualizer);
        runIndividualSort(AdaptiveBinfaClurge, 0, array, 256, 2, false, shuffleName, 16, alt);
        
        Sort AdaptiveClamber = new AdaptiveClamberSort(arrayVisualizer);
        runIndividualSort(AdaptiveClamber, 0, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort AdaptiveClurge = new AdaptiveClurgeNewSolSort(arrayVisualizer);
        runIndividualSort(AdaptiveClurge, 0, array, 256, 2, false, shuffleName, 16, alt);
        
        Sort AdaptiveExpoClamber = new AdaptiveExponentialClamberSort(arrayVisualizer);
        runIndividualSort(AdaptiveExpoClamber, 0, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort AdaptiveFibonacciClamber = new AdaptiveFibonacciClamberSort(arrayVisualizer);
        runIndividualSort(AdaptiveFibonacciClamber, 0, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort AdaptiveRandomClamber = new AdaptiveRandomClamberSort(arrayVisualizer);
        runIndividualSort(AdaptiveRandomClamber, 0, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort AdaptiveSquareClamber = new AdaptiveSquareClamberSort(arrayVisualizer);
        runIndividualSort(AdaptiveSquareClamber, 0, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort AdaptiveTriClamber = new AdaptiveTriClamberSort(arrayVisualizer);
        runIndividualSort(AdaptiveTriClamber, 0, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort Asteraceae = new AsteraceaeSort(arrayVisualizer);
        runIndividualSort(Asteraceae, 0, array, 256, 2, false, shuffleName, 16, alt);
        
        Sort BaseNOddEven = new BaseNOddEvenSort(arrayVisualizer);
        runIndividualSort(BaseNOddEven, 4, array, 512, 1, false, shuffleName, 16, alt);
        
        Sort Birthday = new BirthdaySort(arrayVisualizer);
        runIndividualSort(Birthday, 0, array, 128, 2, false, shuffleName, 16, alt);
        
        Sort BirthdayMerge = new BirthdayMergeSort(arrayVisualizer);
        runIndividualSort(BirthdayMerge, 0, array, 128, 8, false, shuffleName, 16, alt);
        
        Sort Bovo = new BovoSort(arrayVisualizer);
        runIndividualSort(Bovo, 0, array, 10, 1e9, false, shuffleName, 5, alt);
        
        Sort Chir = new ChirSort(arrayVisualizer);
        runIndividualSort(Chir, 0, array, 10, 1e9, false, shuffleName, 5, alt);
        
        Sort Clamber = new ClamberSort(arrayVisualizer);
        runIndividualSort(Clamber, 0, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort Clark = new ClarkSort(arrayVisualizer);
        runIndividualSort(Clark, 0, array, 1024, 1, false, shuffleName, 16, alt);
        
        Sort CocktailPeel = new CocktailPeelSort(arrayVisualizer);
        runIndividualSort(CocktailPeel, 0, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort CocktailPush = new CocktailPushSort(arrayVisualizer);
        runIndividualSort(CocktailPush, 0, array, 512, 0.5, false, shuffleName, 16, alt);
        
        Sort CocktailSandpaper = new CocktailSandpaperSort(arrayVisualizer);
        runIndividualSort(CocktailSandpaper, 0, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort Cospo = new CospoSort(arrayVisualizer);
        runIndividualSort(Cospo, 0, array, 128, 1, false, shuffleName, 16, alt);
        
        Sort CupcakeWrapper = new CupcakeWrapperSort(arrayVisualizer);
        runIndividualSort(CupcakeWrapper, 0, array, 128, 4, false, shuffleName, 16, alt);
        
        Sort Debris = new DebrisSort(arrayVisualizer);
        runIndividualSort(Debris, 0, array, 512, 1, false, shuffleName, 16, alt);
        
        Sort DebrisIterativePop = new DebrisIterativePopSort(arrayVisualizer);
        runIndividualSort(DebrisIterativePop, 0, array, 256, 0.5, false, shuffleName, 16, alt);
        
        Sort Ecolo = new EcoloSort(arrayVisualizer);
        runIndividualSort(Ecolo, 0, array, 128, 0.25, false, shuffleName, 16, alt);
        
        Sort EqualDebris = new DebrisSortEquality(arrayVisualizer);
        runIndividualSort(EqualDebris, 0, array, 512, 1, false, shuffleName, 16, alt);
        
        Sort ExchangeBojo = new ExchangeBojoSort(arrayVisualizer);
        runIndividualSort(ExchangeBojo, 0, array, 128, 1, false, shuffleName, 16, alt);
        
        Sort ExchangeChir = new ExchangeChirSort(arrayVisualizer);
        runIndividualSort(ExchangeChir, 0, array, 128, 1, false, shuffleName, 16, alt);
        
        Sort ExchangeChirMerge = new ExchangeChirMergeSort(arrayVisualizer);
        runIndividualSort(ExchangeChirMerge, 0, array, 256, 1, false, shuffleName, 16, alt);
        
        Sort ExchangeGoro = new ExchangeGoroSort(arrayVisualizer);
        runIndividualSort(ExchangeGoro, 0, array, 128, 1, false, shuffleName, 16, alt);
        
        Sort ExpandingRoom = new ExpandingRoomSort(arrayVisualizer);
        runIndividualSort(ExpandingRoom, 8, array, 512, 0.25, false, shuffleName, 16, alt);
        
        Sort Fall = new FallSort(arrayVisualizer);
        runIndividualSort(Fall, 0, array, 256, 1, false, shuffleName, 16, alt);
        
        Sort Fate = new FateSort(arrayVisualizer);
        runIndividualSort(Fate, 0, array, 128, 16, false, shuffleName, 16, alt);
        
        Sort Fire = new FireSort(arrayVisualizer);
        runIndividualSort(Fire, 0, array, 256, 2, false, shuffleName, 16, alt);
        
        Sort Flight = new FlightSort(arrayVisualizer);
        runIndividualSort(Flight, 0, array, 128, 1, false, shuffleName, 16, alt);
        
        Sort FlightMerge = new FlightMergeSort(arrayVisualizer);
        runIndividualSort(FlightMerge, 0, array, 128, 4, false, shuffleName, 16, alt);
        
        Sort ForwardRunShoveSort = new ForwardRunShoveSort(arrayVisualizer);
        runIndividualSort(ForwardRunShoveSort, 0, array, 128, 0.4, false, shuffleName, 16, alt);
        
        Sort GnomeWeaveHigh = new GnomeWeaveHighSort(arrayVisualizer);
        runIndividualSort(GnomeWeaveHigh, 0, array, 300, 0.5, false, shuffleName, 15, alt);
        
        Sort GnomeWeaveLow = new GnomeWeaveLowSort(arrayVisualizer);
        runIndividualSort(GnomeWeaveLow, 0, array, 300, 0.5, false, shuffleName, 15, alt);
        
        Sort HeadPull = new HeadPullSort(arrayVisualizer);
        runIndividualSort(HeadPull, 0, array, 32, 1e9, false, shuffleName, 8, alt);
        
        Sort HeadPullQuick = new HeadPullQuickSort(arrayVisualizer);
        runIndividualSort(HeadPullQuick, 0, array, 128, 1.28, false, shuffleName, 16, alt);
        
        Sort HeapHunter = new HeapHunterSort(arrayVisualizer);
        runIndividualSort(HeapHunter, 0, array, 1024, 0.5, false, shuffleName, 16, alt);
        
        Sort Hesvo = new HesvoSort(arrayVisualizer);
        runIndividualSort(Hesvo, 0, array, 128, 1.6, false, shuffleName, 16, alt);
        
        Sort IgnorantQuick = new IgnorantQuickSort(arrayVisualizer);
        runIndividualSort(IgnorantQuick, 0, array, 256, 0.66667, false, shuffleName, 16, alt);
        
        Sort InOrderShove = new InOrderShoveSort(arrayVisualizer);
        runIndividualSort(InOrderShove, 0, array, 128, 16, false, shuffleName, 16, alt);
        
        Sort InstintBubble = new InstinctBubbleSort(arrayVisualizer);
        runIndividualSort(InstintBubble, 0, array, 64, 0.2, false, shuffleName, 16, alt);
        
        Sort InstinctCocktailSandpaper = new InstinctCocktailSandpaperSort(arrayVisualizer);
        runIndividualSort(InstinctCocktailSandpaper, 0, array, 64, 0.5, false, shuffleName, 16, alt);
        
        Sort InstintClamber = new InstinctClamberSort(arrayVisualizer);
        runIndividualSort(InstintClamber, 0, array, 64, 0.5, false, shuffleName, 16, alt);
        
        Sort InstinctReverseSandpaper = new InstinctReverseSandpaperSort(arrayVisualizer);
        runIndividualSort(InstinctReverseSandpaper, 0, array, 64, 0.5, false, shuffleName, 16, alt);
        
        Sort InstinctSandpaper = new InstinctSandpaperSort(arrayVisualizer);
        runIndividualSort(InstinctSandpaper, 0, array, 64, 0.5, false, shuffleName, 16, alt);
        
        Sort InverseIterativeCircle = new CircleSortInverseIterative(arrayVisualizer);
        runIndividualSort(InverseIterativeCircle, 0, array, 1024, 2, false, shuffleName, 16, alt);
        
        Sort IterativeCleric = new ClericSortIterative(arrayVisualizer);
        runIndividualSort(IterativeCleric, 0, array, 1024, 0.5, false, shuffleName, 16, alt);
        
        Sort IterativeShircle = new ShircleSortIterative(arrayVisualizer);
        runIndividualSort(IterativeShircle, 0, array, 1024, 0.4, false, shuffleName, 16, alt);
        
        Sort LuckyBubble = new LuckyBubbleSort(arrayVisualizer);
        runIndividualSort(LuckyBubble, 50, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort LuckyCircle = new LuckyCircleSort(arrayVisualizer);
        runIndividualSort(LuckyCircle, 50, array, 128, 4, false, shuffleName, 16, alt);
        
        Sort LuckyCircloid = new LuckyCircloidSort(arrayVisualizer);
        runIndividualSort(LuckyCircloid, 50, array, 128, 4, false, shuffleName, 16, alt);
        
        Sort LuckyCocktail = new LuckyCocktailSort(arrayVisualizer);
        runIndividualSort(LuckyCocktail, 50, array, 128, 1, false, shuffleName, 16, alt);
        
        Sort LuckyComb = new LuckyCombSort(arrayVisualizer);
        runIndividualSort(LuckyComb, 50, array, 128, 8, false, shuffleName, 16, alt);
        
        Sort LuckyDebris = new LuckyDebrisSort(arrayVisualizer);
        runIndividualSort(LuckyDebris, 50, array, 128, 2, false, shuffleName, 16, alt);
        
        Sort LuckyGrate = new LuckyGrateSort(arrayVisualizer);
        runIndividualSort(LuckyGrate, 50, array, 128, 8, false, shuffleName, 16, alt);
        
        Sort LuckyMerry = new LuckyMerrySort(arrayVisualizer);
        runIndividualSort(LuckyMerry, 50, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort LuckyNoisy = new LuckyNoisySort(arrayVisualizer);
        runIndividualSort(LuckyNoisy, 50, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort LuckyOddEven = new LuckyOddEvenSort(arrayVisualizer);
        runIndividualSort(LuckyOddEven, 50, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort LuckyPop = new LuckyPopSort(arrayVisualizer);
        runIndividualSort(LuckyPop, 50, array, 128, 1, false, shuffleName, 16, alt);
        
        Sort LuckyPrimeHigh = new LuckyPrimeWeaveHighSort(arrayVisualizer);
        runIndividualSort(LuckyPrimeHigh, 50, array, 300, 1, false, shuffleName, 15, alt);
        
        Sort LuckyPrimeLow = new LuckyPrimeWeaveLowSort(arrayVisualizer);
        runIndividualSort(LuckyPrimeLow, 50, array, 300, 1, false, shuffleName, 15, alt);
        
        Sort LuckyRouge = new LuckyRougeSort(arrayVisualizer);
        runIndividualSort(LuckyRouge, 50, array, 128, 32, false, shuffleName, 16, alt);
        
        Sort Magnetica = new MagneticaQuickSort(arrayVisualizer);
        runIndividualSort(Magnetica, 4, array, 512, 1, false, shuffleName, 16, alt);
        
        Sort MerryGoRound = new MerryGoRoundSort(arrayVisualizer);
        runIndividualSort(MerryGoRound, 0, array, 512, 4, false, shuffleName, 16, alt);
        
        Sort MobMerge = new MobMergeSort(arrayVisualizer);
        runIndividualSort(MobMerge, 0, array, 512, 1, false, shuffleName, 16, alt);
        
        Sort MoreOptimizedBubbleSort = new MoreOptimizedBubbleSort(arrayVisualizer);
        runIndividualSort(MoreOptimizedBubbleSort, 0, array, 512, 1, false, shuffleName, 16, alt);
        
        Sort NaturalHeadPull = new NaturalHeadPullSort(arrayVisualizer);
        runIndividualSort(NaturalHeadPull, 0, array, 128, 1, false, shuffleName, 16, alt);
        
        Sort Noisy = new NoisySort(arrayVisualizer);
        runIndividualSort(Noisy, 16, array, 256, 2, false, shuffleName, 16, alt);
        
        Sort OddEvenHigh = new OddEvenWeaveHighSort(arrayVisualizer);
        runIndividualSort(OddEvenHigh, 0, array, 300, 1, false, shuffleName, 15, alt);
        
        Sort OddEvenLow = new OddEvenWeaveLowSort(arrayVisualizer);
        runIndividualSort(OddEvenLow, 0, array, 300, 1, false, shuffleName, 15, alt);
        
        Sort OptimizedBaseNOddEven = new OptimizedBaseNOddEvenSort(arrayVisualizer);
        runIndividualSort(OptimizedBaseNOddEven, 4, array, 512, 1, false, shuffleName, 16, alt);
        
        Sort OptimizedBubbleBogo = new OptimizedBubbleBogoSort(arrayVisualizer);
        runIndividualSort(OptimizedBubbleBogo, 0, array, 256, 2, false, shuffleName, 16, alt);
        
        Sort OptimizedBubbleBogoMerge = new OptimizedBubbleBogoMergeSort(arrayVisualizer);
        runIndividualSort(OptimizedBubbleBogoMerge, 0, array, 256, 1, false, shuffleName, 16, alt);
        
        Sort OptimizedCocktailGrate = new OptimizedCocktailGrateSort(arrayVisualizer);
        runIndividualSort(OptimizedCocktailGrate, 0, array, 256, 16, false, shuffleName, 16, alt);
        
        Sort OptimizedGambit = new OptimizedGambitInsertionSort(arrayVisualizer);
        runIndividualSort(OptimizedGambit, 0, array, 512, 2, false, shuffleName, 16, alt);
        
        Sort OptimizedGrate = new OptimizedGrateSort(arrayVisualizer);
        runIndividualSort(OptimizedGrate, 0, array, 128, 32, false, shuffleName, 16, alt);
        
        Sort OptimizedIterativePop = new IterativePopSort(arrayVisualizer);
        runIndividualSort(OptimizedIterativePop, 0, array, 256, 1, false, shuffleName, 16, alt);
        
        Sort OptimizedOddEven = new OptimizedOddEvenSort(arrayVisualizer);
        runIndividualSort(OptimizedOddEven, 0, array, 512, 1, false, shuffleName, 16, alt);
        
        Sort OptimizedPush = new OptimizedPushSort(arrayVisualizer);
        runIndividualSort(OptimizedPush, 0, array, 512, 0.5, false, shuffleName, 16, alt);
        
        Sort OptimizedReverseGrate = new OptimizedReverseGrateSort(arrayVisualizer);
        runIndividualSort(OptimizedReverseGrate, 0, array, 256, 16, false, shuffleName, 16, alt);
        
        Sort OptimizedReverseSandpaper = new OptimizedReverseSandpaperSort(arrayVisualizer);
        runIndividualSort(OptimizedReverseSandpaper, 0, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort OptimizedRoom = new OptimizedRoomSort(arrayVisualizer);
        runIndividualSort(OptimizedRoom, 0, array, 512, 0.25, false, shuffleName, 16, alt);
        
        Sort OptimizedShove = new OptimizedShoveSort(arrayVisualizer);
        runIndividualSort(OptimizedShove, 0, array, 128, 1.6667, false, shuffleName, 16, alt);
        
        Sort OptimizedSplitCenter = new OptimizedSplitCenterSort(arrayVisualizer);
        runIndividualSort(OptimizedSplitCenter, 0, array, 2048, 1, false, shuffleName, 16, alt);
        
        Sort OptimizedStrangePush = new OptimizedStrangePushSort(arrayVisualizer);
        runIndividualSort(OptimizedStrangePush, 2, array, 512, 0.5, false, shuffleName, 16, alt);
        
        Sort OptimizedWhy = new OptimizedWhySort(arrayVisualizer);
        runIndividualSort(OptimizedWhy, 0, array, 256, 1e9, false, shuffleName, 16, alt);
        
        Sort OptimizedWhy2 = new OptimizedWhy2Sort(arrayVisualizer);
        runIndividualSort(OptimizedWhy2, 0, array, 256, 1e9, false, shuffleName, 16, alt);
        
        Sort OptimizedZipper = new OptimizedZipperSort(arrayVisualizer);
        runIndividualSort(OptimizedZipper, 0, array, 256, 0.5, false, shuffleName, 16, alt);
        
        Sort PDBinaryClamber = new PDBinaryClamberSort(arrayVisualizer);
        runIndividualSort(PDBinaryClamber, 0, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort PDIterativePop = new PDIterativePopSort(arrayVisualizer);
        runIndividualSort(PDIterativePop, 0, array, 256, 0.5, false, shuffleName, 16, alt);
        
        Sort PDIterativePopPop = new PDIterativePopPopSort(arrayVisualizer);
        runIndividualSort(PDIterativePopPop, 0, array, 256, 1, false, shuffleName, 16, alt);
        
        Sort PDPop = new PDPopSort(arrayVisualizer);
        runIndividualSort(PDPop, 0, array, 256, 0.5, false, shuffleName, 16, alt);
        
        Sort Peel = new PeelSort(arrayVisualizer);
        runIndividualSort(Peel, 0, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort Playground = new PlaygroundSort(arrayVisualizer);
        runIndividualSort(Playground, 0, array, 64, 0.5, false, shuffleName, 16, alt);
        
        Sort PopPop = new PopPopSort(arrayVisualizer);
        runIndividualSort(PopPop, 0, array, 256, 1, false, shuffleName, 16, alt);
        
        Sort Push = new PushSort(arrayVisualizer);
        runIndividualSort(Push, 0, array, 512, 0.5, false, shuffleName, 16, alt);
        
        Sort RandomizedShell = new RandomShellSort(arrayVisualizer);
        runIndividualSort(RandomizedShell, 0, array, 512, 0.5, false, shuffleName, 16, alt);
        
        Sort RecursiveCleric = new ClericSortRecursive(arrayVisualizer);
        runIndividualSort(RecursiveCleric, 0, array, 1024, 0.5, false, shuffleName, 16, alt);
        
        Sort RecursivePush = new RecursivePushSort(arrayVisualizer);
        runIndividualSort(RecursivePush, 0, array, 512, 0.5, false, shuffleName, 16, alt);
        
        Sort RecursiveShircle = new ShircleSortRecursive(arrayVisualizer);
        runIndividualSort(RecursiveShircle, 0, array, 1024, 0.4, false, shuffleName, 16, alt);
        
        Sort Reflection = new ReflectionSort(arrayVisualizer);
        runIndividualSort(Reflection, 0, array, 128, 4, false, shuffleName, 16, alt);
        
        Sort ReverseClamber = new ReverseClamberSort(arrayVisualizer);
        runIndividualSort(ReverseClamber, 0, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort ReversePeel = new ReversePeelSort(arrayVisualizer);
        runIndividualSort(ReversePeel, 0, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort ReversePush = new ReversePushSort(arrayVisualizer);
        runIndividualSort(ReversePush, 0, array, 512, 0.5, false, shuffleName, 16, alt);
        
        Sort RougeCircle = new CircleSortRouge(arrayVisualizer);
        runIndividualSort(RougeCircle, 0, array, 256, 1, false, shuffleName, 16, alt);
        
        Sort RougePop = new RougeSpacePopSort(arrayVisualizer);
        runIndividualSort(RougePop, 0, array, 256, 1, false, shuffleName, 16, alt);
        
        Sort SafeStalin = new SafeStalinSort(arrayVisualizer);
        runIndividualSort(SafeStalin, 0, array, 64, 4, false, shuffleName, 16, alt);
        
        Sort ScrollHeapHunter = new ScrollHeapHunterSort(arrayVisualizer);
        runIndividualSort(ScrollHeapHunter, 0, array, 1024, 1, false, shuffleName, 16, alt);
        
        Sort SemiDetBovo = new SemiDeterministicBovoSort(arrayVisualizer);
        runIndividualSort(SemiDetBovo, 0, array, 12, 1e9, false, shuffleName, 6, alt);
        
        Sort ShellHigh = new ShellHighSort(arrayVisualizer);
        runIndividualSort(ShellHigh, 0, array, 300, 0.5, false, shuffleName, 15, alt);
        
        Sort ShellLow = new ShellLowSort(arrayVisualizer);
        runIndividualSort(ShellLow, 0, array, 300, 0.5, false, shuffleName, 15, alt);
        
        Sort Shepherd = new ShepherdSort(arrayVisualizer);
        runIndividualSort(Shepherd, 0, array, 512, 1, false, shuffleName, 16, alt);
        
        Sort Shock = new ShockSort(arrayVisualizer);
        runIndividualSort(Shock, 0, array, 512, 1, false, shuffleName, 16, alt);
        
        Sort ShockAlt = new ShockSortAlt(arrayVisualizer);
        runIndividualSort(ShockAlt, 0, array, 512, 1, false, shuffleName, 16, alt);
        
        Sort ShoveSandpaper = new ShoveSandpaperSort(arrayVisualizer);
        runIndividualSort(ShoveSandpaper, 0, array, 128, 2, false, shuffleName, 16, alt);
        
        Sort Singularity = new SingularityQuickSort(arrayVisualizer);
        runIndividualSort(Singularity, 0, array, 256, 0.5, false, shuffleName, 16, alt);
        
        Sort Snowball = new SnowballSort(arrayVisualizer);
        runIndividualSort(Snowball, 0, array, 512, 0.25, false, shuffleName, 16, alt);
        
        Sort SnowballIterativePop = new SnowballIterativePopSort(arrayVisualizer);
        runIndividualSort(SnowballIterativePop, 0, array, 256, 0.25, false, shuffleName, 16, alt);
        
        Sort SnowballMerge = new SnowballMergeSort(arrayVisualizer);
        runIndividualSort(SnowballMerge, 0, array, 512, 0.0625, false, shuffleName, 16, alt);
        
        Sort SplitCenter = new SplitCenterSort(arrayVisualizer);
        runIndividualSort(SplitCenter, 0, array, 2048, 1, false, shuffleName, 16, alt);
        
        Sort SplitCenterMerge = new SplitCenterMergeSort(arrayVisualizer);
        runIndividualSort(SplitCenterMerge, 0, array, 2048, 0.5, false, shuffleName, 16, alt);
        
        Sort StableFall = new StableFallSort(arrayVisualizer);
        runIndividualSort(StableFall, 0, array, 256, 1, false, shuffleName, 16, alt);
        
        Sort StableForwardRunShove = new StableForwardRunShoveSort(arrayVisualizer);
        runIndividualSort(StableForwardRunShove, 0, array, 128, 0.4, false, shuffleName, 16, alt);
        
        Sort StablePlayground = new StablePlaygroundSort(arrayVisualizer);
        runIndividualSort(StablePlayground, 0, array, 64, 0.5, false, shuffleName, 16, alt);
        
        Sort Strange = new StrangeSort(arrayVisualizer);
        runIndividualSort(Strange, 2, array, 512, 1, false, shuffleName, 16, alt);
        
        Sort StrangeExpandingRoom = new StrangeExpandingRoomSort(arrayVisualizer);
        runIndividualSort(StrangeExpandingRoom, 2, array, 512, 0.25, false, shuffleName, 16, alt);
        
        Sort StrangePush = new StrangePushSort(arrayVisualizer);
        runIndividualSort(StrangePush, 2, array, 512, 0.5, false, shuffleName, 16, alt);
        
        Sort StupidFire = new StupidFireSort(arrayVisualizer);
        runIndividualSort(StupidFire, 0, array, 128, 1e9, false, shuffleName, 16, alt);
        
        Sort StupidlyDaftOnionPopStooge = new StupidlyDaftOnionPopStoogeSort(arrayVisualizer);
        runIndividualSort(StupidlyDaftOnionPopStooge, 0, array, 32, 16, false, shuffleName, 16, alt);
        
        Sort SwaplessHeadPull = new SwaplessHeadPullSort(arrayVisualizer);
        runIndividualSort(SwaplessHeadPull, 0, array, 32, 1e9, false, shuffleName, 8, alt);
        
        Sort SwaplessIgnorantQuick = new SwaplessIgnorantQuickSort(arrayVisualizer);
        runIndividualSort(SwaplessIgnorantQuick, 0, array, 256, 0.66667, false, shuffleName, 16, alt);
        
        Sort SwaplessPlayground = new SwaplessPlaygroundSort(arrayVisualizer);
        runIndividualSort(SwaplessPlayground, 0, array, 64, 0.5, false, shuffleName, 16, alt);
        
        Sort SwaplessPush = new SwaplessPushSort(arrayVisualizer);
        runIndividualSort(SwaplessPush, 0, array, 512, 0.5, false, shuffleName, 16, alt);
        
        Sort TableAdaBinClamber = new TableAdaBinClamberSort(arrayVisualizer);
        runIndividualSort(TableAdaBinClamber, 0, array, 128, 1, false, shuffleName, 16, alt);
        
        Sort TableClamber = new TableClamberSort(arrayVisualizer);
        runIndividualSort(TableClamber, 0, array, 128, 1, false, shuffleName, 16, alt);
        
        Sort Tumbleweed = new TumbleweedSort(arrayVisualizer);
        runIndividualSort(Tumbleweed, 0, array, 128, 1, false, shuffleName, 16, alt);
        
        Sort Unbelievable = new UnbelievableSort(arrayVisualizer);
        runIndividualSort(Unbelievable, 0, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort UnboundedSingularity = new UnboundedSingularityQuickSort(arrayVisualizer);
        runIndividualSort(UnboundedSingularity, 0, array, 256, 0.5, false, shuffleName, 16, alt);
        
        Sort UnboundedUnstableSingularity = new UnboundedUnstableSingularityQuickSort(arrayVisualizer);
        runIndividualSort(UnboundedUnstableSingularity, 0, array, 256, 0.5, false, shuffleName, 16, alt);
        
        Sort UnoptimizedFate = new UnoptimizedFateSort(arrayVisualizer);
        runIndividualSort(UnoptimizedFate, 0, array, 128, 32, false, shuffleName, 16, alt);
        
        Sort UnstablePDIterativePop = new PDIterativePopSortUnstable(arrayVisualizer);
        runIndividualSort(UnstablePDIterativePop, 0, array, 256, 0.5, false, shuffleName, 16, alt);
        
        Sort UnstableSingularity = new UnstableSingularityQuickSort(arrayVisualizer);
        runIndividualSort(UnstableSingularity, 0, array, 256, 0.5, false, shuffleName, 16, alt);
        
        Sort WatermelonWaves = new WatermelonWavesSort(arrayVisualizer);
        runIndividualSort(WatermelonWaves, 0, array, 10, 1e9, false, shuffleName, 5, alt);
        
        Sort WeirdInsertion = new WeirdInsertionSort(arrayVisualizer);
        runIndividualSort(WeirdInsertion, 0, array, 128, 0.5, false, shuffleName, 16, alt);
        
        Sort Why = new WhySort(arrayVisualizer);
        runIndividualSort(Why, 0, array, 8, 1e9, false, shuffleName, 4, alt);
        
        Sort Why2 = new Why2Sort(arrayVisualizer);
        runIndividualSort(Why2, 0, array, 8, 1e9, false, shuffleName, 4, alt);
        
        Sort XPattern = new XSort(arrayVisualizer);
        runIndividualSort(XPattern, 0, array, 256, 1, false, shuffleName, 16, alt);
        
        Sort Zipper = new ZipperSort(arrayVisualizer);
        runIndividualSort(Zipper, 0, array, 256, 0.5, false, shuffleName, 16, alt);
        
        /*
        */
        
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
        RSS MADHOUSE PLUS (25)
        */
        
        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_SHUFFLED_HALF : Shuffles.SHUFFLED_HALF); // 26
        runSort(array, "Scrambled Back Half", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_NOISY : Shuffles.NOISY); // 27
        runSort(array, "Low Disparity", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_PARTITIONED :Shuffles.PARTITIONED); // 28
        runSort(array, "Partitioned", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SAWTOOTH)
                                    .addSingle(Shuffles.REVERSE); // 29
        runSort(array, "Reversed Sawtooth", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.FINAL_BITONIC); // 30
        runSort(array, "Final Bitonic", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.REC_RADIX); // 31
        runSort(array, "Recursive Final Radix", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.INV_BST); // 32
        runSort(array, "Inverted Binary Tree", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.LOG_SLOPES); // 33
        runSort(array, "Logpile", false);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.TRI_HEAP); // 34
        runSort(array, "Triangle Heap", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_CIRCLE : Shuffles.CIRCLE); // 35
        runSort(array, "Circle Pass", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.QSORT_BAD); // 36
        runSort(array, "Quick Killer", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.PDQ_BAD); // 37
        runSort(array, "Pattern Quick Killer", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_GRAIL_BAD : Shuffles.GRAIL_BAD); // 38
        runSort(array, "Grail Killer", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.SHUF_MERGE_BAD); // 39
        runSort(array, "Shuffle Killer", true);

        arrayVisualizer.getArrayManager().setShuffleSingle(seeds ? Shuffles.SEEDED_BLOCK_RANDOMLY : Shuffles.BLOCK_RANDOMLY); // 40
        runSort(array, "Blocks", true);

        arrayVisualizer.getArrayManager().setDistribution(seeds ? Distributions.SEEDED_RANDOM : Distributions.RANDOM); // 41
        arrayVisualizer.getArrayManager().setShuffleSingle(Shuffles.ALREADY);
        runSort(array, "Natural Random", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.SINE); // 42
        runSort(array, "Sine Wave", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.COSINE); // 43
        runSort(array, "Cosine Wave", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.RULER); // 44
        runSort(array, "Ruler", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.BLANCMANGE); // 45
        runSort(array, "Blancmange Curve", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.DIVISORS); // 46
        runSort(array, "Sum of Divisors", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.FSD); // 47
        runSort(array, "Fly Straight, Dammit!", false);

        arrayVisualizer.getArrayManager().setDistribution(seeds ? Distributions.SEEDED_REVLOG : Distributions.REVLOG); // 48
        runSort(array, "Decreasing Random", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.MODULO); // 49
        runSort(array, "Modulo Function", false);

        arrayVisualizer.getArrayManager().setDistribution(Distributions.DIGITS_PROD); // 50
        runSort(array, "Product of Digits", false);
        
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

                    arrayVisualizer.setCategory("PCBSAM for ArrayV");
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