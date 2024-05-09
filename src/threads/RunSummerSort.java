package threads;

import main.ArrayManager;
import main.ArrayVisualizer;
import panes.JErrorPane;
import sorts.bogo.ScrambleQuickSort;
import sorts.concurrent.VanVoorhisFourFourSortIterative;
import sorts.concurrent.VanVoorhisFourFourSortRecursive;
import sorts.exchange.AccelerateSort;
import sorts.exchange.BabblurSort;
import sorts.exchange.BasicDigSort;
import sorts.exchange.CityscapeSortNS;
import sorts.exchange.ClampSort;
import sorts.exchange.DeterministicSimpleArgoSort;
import sorts.exchange.DigSort;
import sorts.exchange.DragSort;
import sorts.exchange.EggSort;
import sorts.exchange.EvubSort;
import sorts.exchange.FiringSquadSortIterative;
import sorts.exchange.FiringSquadSortRecursive;
import sorts.exchange.ForwardRunShoveSort;
import sorts.exchange.InOrderShoveSort;
import sorts.exchange.JerenSort;
import sorts.exchange.LoopSort;
import sorts.exchange.LootSort;
import sorts.exchange.OldDigSort;
import sorts.exchange.OpiumSort;
import sorts.exchange.OptimizedBabblurSort;
import sorts.exchange.OptimizedDragSort;
import sorts.exchange.OptimizedInOrderShoveSort;
import sorts.exchange.OptimizedShobeSort;
import sorts.exchange.PDInOrderShoveSort;
import sorts.exchange.PairwisePoptimalSort;
import sorts.exchange.SearchSort;
import sorts.exchange.SegForwardRunShoveSort;
import sorts.exchange.ShobeSort;
import sorts.exchange.SpreadSort;
import sorts.exchange.SwaplessInOrderShoveSort;
import sorts.exchange.SwivelSort;
import sorts.exchange.UnnamedSort;
import sorts.exchange.ZigZagSort;
import sorts.hybrid.BismuthSort;
import sorts.hybrid.ByproductSort;
import sorts.hybrid.CavernousSort;
import sorts.hybrid.HeliumSort;
import sorts.hybrid.IzaSort;
import sorts.hybrid.KitaSort;
import sorts.hybrid.KleeSort;
import sorts.hybrid.LonginusSort;
import sorts.hybrid.OptimizedPartitionHeapMergeSort;
import sorts.hybrid.PartitionHeapMergeSort;
import sorts.hybrid.PebbleSort;
import sorts.hybrid.ShiftSort;
import sorts.hybrid.StablePebbleSort;
import sorts.insert.BigBlockInsertionSort;
import sorts.insert.BlockInsertionSortAdaRot;
import sorts.insert.CappedShipperSort;
import sorts.insert.CiuraCappedShipperSort;
import sorts.insert.FhellSort;
import sorts.insert.IPStableBadShellSort;
import sorts.insert.ParShellShellSort;
import sorts.insert.RurshSort;
import sorts.insert.ShellShellSort;
import sorts.insert.ShipperSort;
import sorts.merge.InPlaceMergeSortII;
import sorts.merge.RougeLazyStableSort;
import sorts.quick.CountingDualPivotQuickSort;
import sorts.quick.CountingPivotQuickSort;
import sorts.quick.IPCountingDualPivotQuickSort;
import sorts.quick.IPCountingPivotQuickSort;
import sorts.quick.IPTernaryCountingPivotQuickSort;
import sorts.quick.InPlaceSergioSort;
import sorts.quick.InPlaceUnstableSergioSort;
import sorts.quick.IndexSmartSingularityQuickSort;
import sorts.quick.MashuSort;
import sorts.quick.MidSingularityQuickSort;
import sorts.quick.OOPCountingDualPivotQuickSort;
import sorts.quick.OOPCountingPivotQuickSort;
import sorts.quick.OOPSingularityQuickSort;
import sorts.quick.OOPSmartSingularityQuickSort;
import sorts.quick.OOPTernaryCountingDualPivotQuickSort;
import sorts.quick.OOPTernaryCountingPivotQuickSort;
import sorts.quick.OOPTernarySingularityQuickSort;
import sorts.quick.OptimizedInPlaceSergioSort;
import sorts.quick.OptimizedMidSingularityQuickSort;
import sorts.quick.OptimizedSmartSingularityQuickSort;
import sorts.quick.SergioSort;
import sorts.quick.ShellUnstableSingularityQuickSort;
import sorts.quick.SingularityQuickSort;
import sorts.quick.SmartSingularityQuickSort;
import sorts.quick.TernaryCountingPivotQuickSort;
import sorts.quick.TernarySingularityQuickSort;
import sorts.quick.UnboundedSingularityQuickSort;
import sorts.quick.UnboundedUnstableSingularityQuickSort;
import sorts.quick.UnstableSingularityQuickSort;
import sorts.quick.UnstableSmartSingularityQuickSort;
import sorts.quick.UnstableTernarySingularityQuickSort;
import sorts.select.CrinkleSort;
import sorts.select.EhatSort;
import sorts.select.MoreOptimizedOpiumSort;
import sorts.select.OptimizedOpiumSort;
import sorts.select.OptimizedOutOfPlaceRotateSelectSort;
import sorts.select.OptimizedRollSort;
import sorts.select.OptimizedRotateSelectSort;
import sorts.select.OutOfPlaceRotateSelectSort;
import sorts.select.PDOpiumSort;
import sorts.select.PDOutOfPlaceRotateSelectSort;
import sorts.select.PDRollSort;
import sorts.select.PDRotateSelectSort;
import sorts.select.PDStableOpiumSort;
import sorts.select.PasserbySort;
import sorts.select.PeelBingoSort;
import sorts.select.QhatSort;
import sorts.select.RollSort;
import sorts.select.RotateSelectSort;
import sorts.select.SandpaperBingoSort;
import sorts.select.StableOpiumSort;
import sorts.select.SwaplessOptimizedReverseSandpaperSort;
import sorts.select.SwaplessRollSort;
import sorts.select.WharSort;
import sorts.select.WhaySort;
import sorts.select.WhstSort;
import sorts.select.WjatSort;
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

public class RunSummerSort extends MultipleSortThread {
    int inputnum = 0;
    boolean alternate_distributions = false;
    boolean seeds = false;
    boolean stabilityproper = true;
    boolean stability = false;

    public RunSummerSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }

    protected synchronized void runIndividualSort(Sort sort, int bucketCount, int[] array, int defaultLength, double defaultSpeed, boolean slowSort, String shuffleName, int uniques, boolean alt) throws Exception {
        //if (inputnum < 40) return;
        Delays.setSleepRatio(1);
        if (defaultLength != arrayVisualizer.getCurrentLength()) arrayFrame.setLengthSlider(defaultLength);
        if (shuffleName == "Many Similar" || shuffleName == "More Similar" || shuffleName == "Stability Test") arrayVisualizer.getArrayFrame().setUniqueSlider(uniques);
        else {
            if (alt && alternate_distributions) arrayVisualizer.getArrayFrame().setUniqueSlider(defaultLength / 8);
            else arrayVisualizer.getArrayFrame().setUniqueSlider(defaultLength);
        }
        arrayManager.refreshArray(array, arrayVisualizer.getCurrentLength(), arrayVisualizer);
        if (sort.getSortListName() == "Helium") {
            if (bucketCount == 0) arrayVisualizer.setHeading("Helium Sort (Strategy 3C/4B/5, No Aux Space)");
            else if (bucketCount == 1) arrayVisualizer.setHeading("Uranium Sort (Strategy 1, Unlimited Aux Space)");
            else if (bucketCount == 2) arrayVisualizer.setHeading("Hydrogen Sort (Strategy 2A, Limited Aux Space)");
        }
        arrayVisualizer.setHeading((sort.getSortListName() == "Helium" && bucketCount < 3 ? arrayVisualizer.getHeading() : sort.getRunAllSortsName()) + " (" + shuffleName + ": " + inputnum + "/75)");
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
        if (!arrayVisualizer.blaze) {
            for (int i = 0; i < 100; i++) {
                Thread.sleep(10);
                arrayVisualizer.updateNow();
            }
        }
        sortNumber++;
    }

    protected synchronized void runSort(int[] array, String shuffleName, boolean alt) throws Exception {

        inputnum++;

        //if (alt) return;

        //Sort Kita = new KitaSort(arrayVisualizer);
        //runIndividualSort(Kita, 0, array, 1024, 1, false, shuffleName, 16, alt);

        //Sort ZigZag = new ZigZagSort(arrayVisualizer);
        //runIndividualSort(ZigZag, 0, array, 128, 1, false, shuffleName, 16, alt);

        //Sort Search = new SearchSort(arrayVisualizer);
        //runIndividualSort(Search, 0, array, 128, 8, false, shuffleName, 16, alt);

        //Sort InPlaceMergeII = new InPlaceMergeSortII(arrayVisualizer);
        //runIndividualSort(InPlaceMergeII, 0, array, 256, 0.5, false, shuffleName, 16, alt);

        //Sort Evub = new EvubSort(arrayVisualizer);
        //runIndividualSort(Evub, 3, array, 256, 1, false, shuffleName, 16, alt);

        //Sort VV44Rec = new VanVoorhisFourFourSortRecursive(arrayVisualizer);
        //runIndividualSort(VV44Rec, 0, array, 256, 1, false, shuffleName, 16, alt);

        //Sort VV44Itr = new VanVoorhisFourFourSortIterative(arrayVisualizer);
        //runIndividualSort(VV44Itr, 0, array, 256, 1, false, shuffleName, 16, alt);

        //Sort Pebble = new PebbleSort(arrayVisualizer);
        //runIndividualSort(Pebble, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort StablePebble = new StablePebbleSort(arrayVisualizer);
        //runIndividualSort(StablePebble, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort PartitionHeapMerge = new PartitionHeapMergeSort(arrayVisualizer);
        //runIndividualSort(PartitionHeapMerge, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort OptimizedPartitionHeapMerge = new OptimizedPartitionHeapMergeSort(arrayVisualizer);
        //runIndividualSort(OptimizedPartitionHeapMerge, 0, array, 512, 2, false, shuffleName, 16, alt);

        //Sort SandpaperBingo = new SandpaperBingoSort(arrayVisualizer);
        //runIndividualSort(SandpaperBingo, 0, array, 256, 1, false, shuffleName, 16, alt);

        //Sort PeelBingo = new PeelBingoSort(arrayVisualizer);
        //runIndividualSort(PeelBingo, 0, array, 256, 1, false, shuffleName, 16, alt);

        //Sort Mashu = new MashuSort(arrayVisualizer);
        //runIndividualSort(Mashu, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort BasicDig = new BasicDigSort(arrayVisualizer);
        //runIndividualSort(BasicDig, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort OldDig = new OldDigSort(arrayVisualizer);
        //runIndividualSort(OldDig, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort Dig = new DigSort(arrayVisualizer);
        //runIndividualSort(Dig, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort FiringSquad = new FiringSquadSortRecursive(arrayVisualizer);
        //runIndividualSort(FiringSquad, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort FiringSquadI = new FiringSquadSortIterative(arrayVisualizer);
        //runIndividualSort(FiringSquadI, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort Clamp = new ClampSort(arrayVisualizer);
        //runIndividualSort(Clamp, 0, array, 32, 1e9, false, shuffleName, 8, alt);

        //Sort Babblur = new BabblurSort(arrayVisualizer);
        //runIndividualSort(Babblur, 0, array, 256, 2, false, shuffleName, 16, alt);

        //Sort OptimizedBabblur = new OptimizedBabblurSort(arrayVisualizer);
        //runIndividualSort(OptimizedBabblur, 0, array, 256, 2, false, shuffleName, 16, alt);

        //Sort Shobe = new ShobeSort(arrayVisualizer);
        //runIndividualSort(Shobe, 0, array, 128, 2, false, shuffleName, 16, alt);

        //Sort OptiShobe = new OptimizedShobeSort(arrayVisualizer);
        //runIndividualSort(OptiShobe, 0, array, 128, 1, false, shuffleName, 16, alt);

        //Sort BlockInsertion = new BlockInsertionSortAdaRot(arrayVisualizer);
        //runIndividualSort(BlockInsertion, 0, array, 512, 8, false, shuffleName, 16, alt);

        //Sort BBI = new BigBlockInsertionSort(arrayVisualizer);
        //runIndividualSort(BBI, 16, array, 512, 2, false, shuffleName, 16, alt);

        //Sort RLzS = new RougeLazyStableSort(arrayVisualizer);
        //runIndividualSort(RLzS, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort Drag = new DragSort(arrayVisualizer);
        //runIndividualSort(Drag, 0, array, 128, 1, false, shuffleName, 16, alt);

        //Sort OptimizedDrag = new OptimizedDragSort(arrayVisualizer);
        //runIndividualSort(OptimizedDrag, 0, array, 128, 1, false, shuffleName, 16, alt);

        //Sort Loot = new LootSort(arrayVisualizer);
        //runIndividualSort(Loot, 0, array, 256, 1, false, shuffleName, 16, alt);

        //Sort Loop = new LoopSort(arrayVisualizer);
        //runIndividualSort(Loop, 0, array, 256, 1, false, shuffleName, 16, alt);

        //Sort Iza = new IzaSort(arrayVisualizer);
        //runIndividualSort(Iza, 0, array, 4096, 1, false, shuffleName, 16, alt);

        //Sort DeterministicArgo = new DeterministicSimpleArgoSort(arrayVisualizer);
        //runIndividualSort(DeterministicArgo, 0, array, 1024, 1, false, shuffleName, 16, alt);

        //Sort Sing = new SingularityQuickSort(arrayVisualizer);
        //runIndividualSort(Sing, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort UnstSing = new UnstableSingularityQuickSort(arrayVisualizer);
        //runIndividualSort(UnstSing, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort ShllUnstSing = new ShellUnstableSingularityQuickSort(arrayVisualizer);
        //runIndividualSort(ShllUnstSing, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort UnbdSing = new UnboundedSingularityQuickSort(arrayVisualizer);
        //runIndividualSort(UnbdSing, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort UnbdUnstSing = new UnboundedUnstableSingularityQuickSort(arrayVisualizer);
        //runIndividualSort(UnbdUnstSing, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort OOPSing = new OOPSingularityQuickSort(arrayVisualizer);
        //runIndividualSort(OOPSing, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort TernSing = new TernarySingularityQuickSort(arrayVisualizer);
        //runIndividualSort(TernSing, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort UnstTernSing = new UnstableTernarySingularityQuickSort(arrayVisualizer);
        //runIndividualSort(UnstTernSing, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort OOPTernSing = new OOPTernarySingularityQuickSort(arrayVisualizer);
        //runIndividualSort(OOPTernSing, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort MidSing = new MidSingularityQuickSort(arrayVisualizer);
        //runIndividualSort(MidSing, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort OptiMidSing = new OptimizedMidSingularityQuickSort(arrayVisualizer);
        //runIndividualSort(OptiMidSing, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort SmartSing = new SmartSingularityQuickSort(arrayVisualizer);
        //runIndividualSort(SmartSing, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort OptiSmartSing = new OptimizedSmartSingularityQuickSort(arrayVisualizer);
        //runIndividualSort(OptiSmartSing, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort UnstSmartSing = new UnstableSmartSingularityQuickSort(arrayVisualizer);
        //runIndividualSort(UnstSmartSing, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort IdxSmartSing = new IndexSmartSingularityQuickSort(arrayVisualizer);
        //runIndividualSort(IdxSmartSing, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort OOPSmartSing = new OOPSmartSingularityQuickSort(arrayVisualizer);
        //runIndividualSort(OOPSmartSing, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort Cityscape = new CityscapeSortNS(arrayVisualizer);
        //runIndividualSort(Cityscape, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort Unnamed = new AccelerateSort(arrayVisualizer);
        //runIndividualSort(Unnamed, 2, array, 64, 1, false, shuffleName, 16, alt);

        //Sort ScrambleQuick = new ScrambleQuickSort(arrayVisualizer);
        //runIndividualSort(ScrambleQuick, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort Sergio = new SergioSort(arrayVisualizer);
        //runIndividualSort(Sergio, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort IPUSergio = new InPlaceUnstableSergioSort(arrayVisualizer);
        //runIndividualSort(IPUSergio, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort IPSergio = new InPlaceSergioSort(arrayVisualizer);
        //runIndividualSort(IPSergio, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort OptiIPSergio = new OptimizedInPlaceSergioSort(arrayVisualizer);
        //runIndividualSort(OptiIPSergio, 16, array, 512, 1, false, shuffleName, 16, alt);

        //Sort Egg = new EggSort(arrayVisualizer);
        //runIndividualSort(Egg, 0, array, 128, 1, false, shuffleName, 16, alt);

        //Sort SwaplessOptimizedReverseSandpaper = new SwaplessOptimizedReverseSandpaperSort(arrayVisualizer);
        //runIndividualSort(SwaplessOptimizedReverseSandpaper, 0, array, 128, 1, false, shuffleName, 16, alt);

        //Sort Klee = new KleeSort(arrayVisualizer);
        //runIndividualSort(Klee, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort InOrderShove = new InOrderShoveSort(arrayVisualizer);
        //runIndividualSort(InOrderShove, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort SwaplessIOShove = new SwaplessInOrderShoveSort(arrayVisualizer);
        //runIndividualSort(SwaplessIOShove, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort OptimizedInOrderShove = new OptimizedInOrderShoveSort(arrayVisualizer);
        //runIndividualSort(OptimizedInOrderShove, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort PDInOrderShove = new PDInOrderShoveSort(arrayVisualizer);
        //runIndividualSort(PDInOrderShove, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort Roll = new RollSort(arrayVisualizer);
        //runIndividualSort(Roll, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort SwaplessRoll = new SwaplessRollSort(arrayVisualizer);
        //runIndividualSort(SwaplessRoll, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort OptimizedRoll = new OptimizedRollSort(arrayVisualizer);
        //runIndividualSort(OptimizedRoll, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort PDRoll = new PDRollSort(arrayVisualizer);
        //runIndividualSort(PDRoll, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort RotateSelection = new RotateSelectSort(arrayVisualizer);
        //runIndividualSort(RotateSelection, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort OptimizedRotateSelect = new OptimizedRotateSelectSort(arrayVisualizer);
        //runIndividualSort(OptimizedRotateSelect, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort PDRotateSelect = new PDRotateSelectSort(arrayVisualizer);
        //runIndividualSort(PDRotateSelect, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort OutOfPlaceRotateSelection = new OutOfPlaceRotateSelectSort(arrayVisualizer);
        //runIndividualSort(OutOfPlaceRotateSelection, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort OptimizedOutOfPlaceRotateSelect = new OptimizedOutOfPlaceRotateSelectSort(arrayVisualizer);
        //runIndividualSort(OptimizedOutOfPlaceRotateSelect, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort PDOutOfPlaceRotateSelect = new PDOutOfPlaceRotateSelectSort(arrayVisualizer);
        //runIndividualSort(PDOutOfPlaceRotateSelect, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort Opium = new OpiumSort(arrayVisualizer);
        //runIndividualSort(Opium, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort OptimizedOpium = new OptimizedOpiumSort(arrayVisualizer);
        //runIndividualSort(OptimizedOpium, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort MoreOptimizedOpium = new MoreOptimizedOpiumSort(arrayVisualizer);
        //runIndividualSort(MoreOptimizedOpium, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort StableOpium = new StableOpiumSort(arrayVisualizer);
        //runIndividualSort(StableOpium, 0, array, 128, 1, false, shuffleName, 8, alt);

        //Sort PDOpium = new PDOpiumSort(arrayVisualizer);
        //runIndividualSort(PDOpium, 0, array, 128, 1, false, shuffleName, 16, false);

        //Sort PDStableOpium = new PDStableOpiumSort(arrayVisualizer);
        //runIndividualSort(PDStableOpium, 0, array, 128, 1, false, shuffleName, 8, alt);

        //Sort Unnamed2 = new ByproductSort(arrayVisualizer);
        //for (int i = 500; i <= 512; i++)
        //runIndividualSort(Unnamed2, 0, array, i, 1, false, shuffleName, 16, alt);

        //Sort cvern = new CavernousSort(arrayVisualizer);
        //runIndividualSort(cvern, 0, array, 1024*2, 1, false, shuffleName, 16, alt);*/

        //Sort Shipper = new ShipperSort(arrayVisualizer);
        //runIndividualSort(Shipper, 0, array, 512, 0.5, false, shuffleName, 16, alt);

        //Sort ShipperCap = new CappedShipperSort(arrayVisualizer);
        //runIndividualSort(ShipperCap, 0, array, 512, 0.5, false, shuffleName, 16, alt);

        //Sort ShipperCapCiura = new CiuraCappedShipperSort(arrayVisualizer);
        //runIndividualSort(ShipperCapCiura, 0, array, 512, 0.5, false, shuffleName, 16, alt);

        //Sort Wjat = new WjatSort(arrayVisualizer);
        //Sort Whst = new WhstSort(arrayVisualizer);
        //Sort Qhat = new QhatSort(arrayVisualizer);
        //Sort Whay = new WhaySort(arrayVisualizer);
        //Sort Whar = new WharSort(arrayVisualizer);
        //Sort Ehat = new EhatSort(arrayVisualizer);
        //for (int i = 512; i <= 512; i++) {
            //runIndividualSort(Wjat, 0, array, i, 1, false, shuffleName, 16, alt);
            //runIndividualSort(Whst, 0, array, i, 1, false, shuffleName, 16, alt);
            //runIndividualSort(Qhat, 0, array, i, 1, false, shuffleName, 16, alt);
            //runIndividualSort(Whay, 0, array, i, 1, false, shuffleName, 16, alt);
            //runIndividualSort(Whar, 0, array, i, 1, false, shuffleName, 16, alt);
            //runIndividualSort(Ehat, 0, array, i, 1, false, shuffleName, 16, alt);
        //}

        //Sort ForwardRunShove = new ForwardRunShoveSort(arrayVisualizer);
        //runIndividualSort(ForwardRunShove, 0, array, 256, 1, false, shuffleName, 16, alt);

        //Sort OptimizedForwardRunShoveSort = new sorts.exchange.OptimizedForwardRunShoveSort(arrayVisualizer);
        //runIndividualSort(OptimizedForwardRunShoveSort, 0, array, 256, 1, false, shuffleName, 16, alt);

        //Sort PDForwardRunShoveSort = new sorts.exchange.PDForwardRunShoveSort(arrayVisualizer);
        //runIndividualSort(PDForwardRunShoveSort, 0, array, 256, 1, false, shuffleName, 16, alt);

        //Sort SegForwardRunShove = new SegForwardRunShoveSort(arrayVisualizer);
        //runIndividualSort(SegForwardRunShove, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort ShellShell = new ShellShellSort(arrayVisualizer);
        //runIndividualSort(ShellShell, 3, array, 512, 1, false, shuffleName, 16, alt);

        //Sort ParShellShell = new ParShellShellSort(arrayVisualizer);
        //runIndividualSort(ParShellShell, 3, array, 512, 1, false, shuffleName, 16, alt);

        //Sort PairwiseWeave = new PairwisePoptimalSort(arrayVisualizer);
        //runIndividualSort(PairwiseWeave, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort Shift = new ShiftSort(arrayVisualizer);
        //runIndividualSort(Shift, 0, array, 256, 1, false, shuffleName, 16, alt);

        //Sort Helium = new HeliumSort(arrayVisualizer);
        //runIndividualSort(Helium, 0, array, 512, 1, false, shuffleName, 16, alt);
        //runIndividualSort(Helium, 1, array, 512, 1, false, shuffleName, 16, alt);
        //runIndividualSort(Helium, 2, array, 512, 1, false, shuffleName, 16, alt);

        //Sort Bismuth = new BismuthSort(arrayVisualizer);
        //Sort BismuthSortNoBuffers = new sorts.hybrid.BismuthSortNoBuffers(arrayVisualizer);

        //for (int i = 64; i <= Math.pow(2, 20); i *= 2) {
            //for (int j = 16; j <= 65536; j *= 2) {

                /*runIndividualSort(BismuthSortNoBuffers, 1, array, 1024, arrayVisualizer.blaze ? 1e9 : 1, false, shuffleName, 16, alt);
                runIndividualSort(BismuthSortNoBuffers, 2, array, 1024, arrayVisualizer.blaze ? 1e9 : 1, false, shuffleName, 16, alt);
                runIndividualSort(BismuthSortNoBuffers, 8, array, 1024, arrayVisualizer.blaze ? 1e9 : 1, false, shuffleName, 16, alt);
                runIndividualSort(BismuthSortNoBuffers, 32, array, 1024, arrayVisualizer.blaze ? 1e9 : 1, false, shuffleName, 16, alt);
                runIndividualSort(BismuthSortNoBuffers, 64, array, 1024, arrayVisualizer.blaze ? 1e9 : 1, false, shuffleName, 16, alt);*/
                //runIndividualSort(Bismuth, 32, array, 1024, arrayVisualizer.blaze ? 1e9 : 1, false, shuffleName, 16, alt);
                //runIndividualSort(Bismuth, 64, array, 1024, arrayVisualizer.blaze ? 1e9 : 1, false, shuffleName, 16, alt);
                //if (j > 10) runIndividualSort(Bismuth, j, array, 131072, 1, false, shuffleName, 16, alt);
                //runIndividualSort(BismuthSortNoBuffers, j, array, 131072, 1, false, shuffleName, 16, alt);
            //}
        //}

        //Sort Passerby = new PasserbySort(arrayVisualizer);
        //for (int i = 64; i <= 128; i++)
        //runIndividualSort(Passerby, 0, array, 256, arrayVisualizer.blaze ? 1e9 : 0.25, false, shuffleName, 16, alt);

        //Sort Crinkle = new CrinkleSort(arrayVisualizer);
        //runIndividualSort(Crinkle, 0, array, 128, 0.1, false, shuffleName, 16, alt);

        //Sort Fhell = new FhellSort(arrayVisualizer);
        //for (int i = 512; i <= 1024; i++)
        //runIndividualSort(Fhell, 0, array, i, 1, false, shuffleName, 16, alt);

        //Sort Longinus = new LonginusSort(arrayVisualizer);
        //for (int i = 16; i < 8192; i++)
        //runIndividualSort(Longinus, 0, array, 1024, 1, false, shuffleName, 16, alt);

        //Sort Rursh = new RurshSort(arrayVisualizer);
        //runIndividualSort(Rursh, 0, array, 128, 1, false, shuffleName, 16, alt);

        //Sort Swivel = new SwivelSort(arrayVisualizer);
        //runIndividualSort(Swivel, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort Jeren = new JerenSort(arrayVisualizer);
        //runIndividualSort(Jeren, 0, array, 128, 1, false, shuffleName, 16, alt);

        //Sort IPStableBadShellSort = new IPStableBadShellSort(arrayVisualizer);
        //runIndividualSort(IPStableBadShellSort, 0, array, 512, 1, false, shuffleName, 16, alt);

        //Sort BFNSpreadSort = new sorts.exchange.BFNSpreadSort(arrayVisualizer);
        //Sort SpreadSort = new SpreadSort(arrayVisualizer);
        //for (int i = 2; i <= 64; i *= 2) {
            //runIndividualSort(BFNSpreadSort, i, array, 256, 1, false, shuffleName, 16, alt);
            //runIndividualSort(SpreadSort, i, array, 256, 1, false, shuffleName, 16, alt);
        //}

        Sort Unnamed = new UnnamedSort(arrayVisualizer);
        runIndividualSort(Unnamed, 0, array, 128, 1e9, false, shuffleName, 16, alt);

    }

    @Override
    protected synchronized void executeSortList(int[] array) throws Exception {

        // A simple shorthand.
        ArrayManager arman = arrayVisualizer.getArrayManager();

        /*/
        // RSS BASIC (25)
        /*/

        arman.setDistribution(Distributions.LINEAR); // 1
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_RANDOM : Shuffles.RANDOM);
        runSort(array, "Random", true);
        arman.setShuffleSingle(Shuffles.REVERSE); // 2
        runSort(array, "Reversed", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_ALMOST : Shuffles.ALMOST); // 3
        runSort(array, "Almost Sorted", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_RANDOM : Shuffles.RANDOM); // 4
        runSort(array, alternate_distributions ? "More Similar" : "Many Similar", false);
        if (stabilityproper) {
            inputnum--;
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
        // RSS MADHOUSE PLUS (40+)
        /*/

        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_SHUFFLED_HALF : Shuffles.SHUFFLED_HALF); // 26
        runSort(array, "Scrambled Back Half", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_SHUFFLED_HALF : Shuffles.SHUFFLED_HALF).addSingle(Shuffles.HALF_ROTATION); // 27
        runSort(array, "Scrambled Front Half", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_SHUFFLED_ENDS : Shuffles.SHUFFLED_ENDS); // 28
        runSort(array, "Both Sides Scrambled", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_ONLY_RUNS : Shuffles.ONLY_RUNS); // 29
        runSort(array, "Random Runs", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_NOISY : Shuffles.NOISY); // 30
        runSort(array, "Low Disparity", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_REAL_FINAL_MERGE : Shuffles.REAL_FINAL_MERGE).addSingle(Shuffles.FINAL_RADIX); // 31
        runSort(array, "Final Weave", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_PARTITIONED : Shuffles.PARTITIONED); // 32
        runSort(array, "Partitioned", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_PERFECT_QUICK : Shuffles.PERFECT_QUICK); // 33
        runSort(array, "Multiple Partitions", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_PARTITIONED : Shuffles.PARTITIONED).addSingle(Shuffles.FINAL_RADIX); // 34
        runSort(array, "Weaved Partition", true);
        arman.setShuffleSingle(Shuffles.INC_REV); // 35
        runSort(array, "Increasing Reversals", true);
        arman.setShuffleSingle(Shuffles.REC_REV); // 36
        runSort(array, "Recursive Reversals", true);
        arman.setShuffleSingle(Shuffles.ANTI_CIRCLE); // 37
        runSort(array, "Backwards Circle", true);
        arman.setShuffleSingle(Shuffles.SAWTOOTH).addSingle(Shuffles.REVERSE); // 38
        runSort(array, "Reversed Sawtooth", true);
        arman.setShuffleSingle(Shuffles.FINAL_BITONIC); // 39
        runSort(array, "Final Bitonic", true);
        arman.setShuffleSingle(Shuffles.FINAL_MERGE).addSingle(Shuffles.REVERSE).addSingle(Shuffles.PARTIAL_REVERSE); // 40
        runSort(array, "Penultimate Bitonic", true);
        arman.setShuffleSingle(Shuffles.DOUBLE_LAYERED).addSingle(Shuffles.HALF_ROTATION); // 41
        runSort(array, "Diamond", true);
        arman.setShuffleSingle(Shuffles.REC_RADIX); // 42
        runSort(array, "Recursive Final Radix", true);
        arman.setShuffleSingle(Shuffles.INV_BST); // 43
        runSort(array, "Inverted Binary Tree", true);
        arman.setShuffleSingle(Shuffles.MODULO); // 44
        runSort(array, "Modulo", true);
        arman.setShuffleSingle(Shuffles.LOG_SLOPES); // 45
        runSort(array, "Logpile", false);
        arman.setShuffleSingle(Shuffles.REVERSE).addSingle(Shuffles.MIN_HEAPIFIED); // 46
        runSort(array, "Min Heapified", true);
        arman.setShuffleSingle(Shuffles.FLEAPIFIED); // 47
        runSort(array, "Flipped Min Heap", true);
        arman.setShuffleSingle(Shuffles.TRI_HEAP); // 48
        runSort(array, "Triangle Heap", true);
        arman.setShuffleSingle(Shuffles.REVERSE).addSingle(Shuffles.VELV_HEAP); // 49
        runSort(array, "Velvet Heap", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_CIRCLE : Shuffles.CIRCLE); // 50
        runSort(array, "Circle Pass", true);
        arman.setShuffleSingle(Shuffles.QSORT_BAD); // 51
        runSort(array, "Quick Killer", true);
        arman.setShuffleSingle(Shuffles.PDQ_BAD); // 52
        runSort(array, "Pattern Quick Killer", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_GRAIL_BAD : Shuffles.GRAIL_BAD); // 53
        runSort(array, "Grail Killer", true);
        arman.setShuffleSingle(Shuffles.SHUF_MERGE_BAD); // 54
        runSort(array, "Shuffle Killer", true);
        arman.setShuffleSingle(seeds ? Shuffles.SEEDED_BLOCK_RANDOMLY : Shuffles.BLOCK_RANDOMLY); // 55
        runSort(array, "Blocks", true);
        arman.setShuffleSingle(Shuffles.PRIME); // 56
        runSort(array, "Prime-Numbered Index", false);
        arman.setDistribution(seeds ? Distributions.SEEDED_RANDOM : Distributions.RANDOM); // 57
        arman.setShuffleSingle(Shuffles.ALREADY);
        runSort(array, "Natural Random", false);
        arman.setDistribution(Distributions.SINE); // 58
        runSort(array, "Sine Wave", false);
        arman.setDistribution(Distributions.COSINE); // 59
        runSort(array, "Cosine Wave", false);
        arman.setDistribution(Distributions.RULER); // 60
        runSort(array, "Ruler", false);
        arman.setDistribution(Distributions.BLANCMANGE); // 61
        runSort(array, "Blancmange Curve", false);
        arman.setDistribution(Distributions.DIVISORS); // 62
        runSort(array, "Sum of Divisors", false);
        arman.setDistribution(Distributions.FSD); // 63
        runSort(array, "Fly Straight, Dammit!", false);
        arman.setDistribution(seeds ? Distributions.SEEDED_REVLOG : Distributions.REVLOG); // 64
        runSort(array, "Decreasing Random", false);
        arman.setDistribution(seeds ? Distributions.SEEDED_INCREASING_RANDOM : Distributions.INCREASING_RANDOM); // 65
        runSort(array, "Increasing Random", false);
        arman.setDistribution(Distributions.NOISY_UNIQUES); // 66
        runSort(array, "Noisy Uniques", false);
        arman.setDistribution(Distributions.MODULO); // 67
        runSort(array, "Modulo Function", false);
        arman.setDistribution(Distributions.DIGITS_SUM); // 68
        runSort(array, "Sum of Digits", false);
        arman.setDistribution(Distributions.DIGITS_PROD); // 69
        runSort(array, "Product of Digits", false);
        arman.setDistribution(Distributions.RAMP); // 70
        runSort(array, "Ramps", false);
        arman.setDistribution(Distributions.TOTIENT); // 71
        runSort(array, "Euler Totient Function", false);
        arman.setDistribution(Distributions.TWPK_FOUR); // 72
        runSort(array, "TWPK's FOUR", false);
        arman.setDistribution(Distributions.COLLATZ); // 73
        runSort(array, "Collatz Conjecture", false);
        arman.setDistribution(Distributions.WEIERSTRASS); // 74
        runSort(array, "Weierstrass Function", false);
        arman.setDistribution(Distributions.SIERPINSKI); // 75
        runSort(array, "Sierpinski Triangle", false);

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
                        //arrayVisualizer.setCategory("thatsOven");
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