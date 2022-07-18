package sorts.tests;

import java.util.LinkedList;

import main.ArrayVisualizer;
import sorts.hybrid.KitaSort;
import sorts.templates.Sort;
import utils.ImplQueue;

final public class StableReversal extends Sort {
    final double BLOCK_DIV = 6.98;

    public StableReversal(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stable Reversal");
        this.setRunAllSortsName("Stable Reversal Test");
        this.setRunSortName("Stable Reversal Test");
        this.setCategory("Tests");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void reverseStable(int[] array, int a, int b) {
        if (b - a == 1) {
            if (Reads.compareIndices(array, a, b, 1, true) != 0)
                Writes.swap(array, a, b, 0, true, false);
            return;
        }
        if (a >= b) return;
        int m = b - (b - a) / 2;
        LinkedList<ImplQueue<Integer>> headloc = new LinkedList<>()
                                     , tailloc = new LinkedList<>();
        headloc.add(new ImplQueue<>());
        headloc.getFirst().add(a);
        Writes.changeAllocAmount(1);
        Writes.changeAuxWrites(1);
        for (int i = a + 1; i <= m; i++) {
            int l = 0, r = headloc.size();
            boolean eq = false;
            z:
            while (l < r) {
                int M = l + (r - l) / 2;
                switch(Reads.compareIndices(array, headloc.get(M).peek(0), i, 0.25, true)) {
                    case 1:
                        r = M;
                        break;
                    case 0:
                        l = M;
                        eq = true;
                        break z;
                    case -1:
                        l = M + 1;
                        break;
                }
            }
            if (!eq)
                headloc.add(l, new ImplQueue<>());
            headloc.get(l).add(i);
            Writes.changeAllocAmount(1);
            Writes.changeAuxWrites(1);
        }
        z:
        for (int i = b; i > m; i--) {
            int l = 0, r = tailloc.size();
            boolean eq = false;
            y:
            while (l < r) {
                int M = l + (r - l) / 2;
                switch(Reads.compareIndices(array, tailloc.get(M).peek(0), i, 0.25, true)) {
                    case 1:
                        r = M;
                        break;
                    case 0:
                        l = M;
                        eq = true;
                        break y;
                    case -1:
                        l = M + 1;
                        break;
                }
            }

            int l2 = 0, r2 = headloc.size();
            y:
            while (l2 < r2) {
                int M = l2 + (r2 - l2) / 2;
                switch(Reads.compareIndices(array, headloc.get(M).peek(0), i, 0.25, true)) {
                    case 1:
                        r2 = M;
                        break;
                    case 0:
                        int j = eq ? tailloc.get(l).shift() : i;
                        Writes.swap(array, headloc.get(M).shift(), j, 1, true, false);
                        Writes.changeAllocAmount(eq?-2:-1);
                        if (headloc.get(M).isEmpty()) headloc.remove(M);
                        if (eq && tailloc.get(l).isEmpty()) {
                            tailloc.remove(l);
                            continue z;
                        } else if (!eq) continue z;
                        break y;
                    case -1:
                        l2 = M + 1;
                        break;
                }
            }
            if (!eq)
                tailloc.add(l, new ImplQueue<>());
            tailloc.get(l).add(i);
            Writes.changeAllocAmount(1);
            Writes.changeAuxWrites(1);
        }
        while (!headloc.isEmpty()) {
            ImplQueue<Integer> p = headloc.removeFirst();
            while (!p.isEmpty()) {
                Writes.changeAllocAmount(p.size()==1?-1:-2);
                if (p.size() > 1)
                    Writes.swap(array, p.shift(), p.pop(), 1, true, false);
                else
                    p.pop();
            }
        }
        while (!tailloc.isEmpty()) {
            ImplQueue<Integer> p = tailloc.removeFirst();
            while (!p.isEmpty()) {
                Writes.changeAllocAmount(p.size()==1?-1:-2);
                if (p.size() > 1)
                    Writes.swap(array, p.shift(), p.pop(), 1, true, false);
                else
                    p.pop();
            }
        }
        Writes.reversal(array, a, b, 1, true, false);
    }
    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        reverseStable(array, 0, length-1);
        KitaSort p = new KitaSort(arrayVisualizer);
        p.runSort(array, length, bucketCount); // test for true stability
    }
}
