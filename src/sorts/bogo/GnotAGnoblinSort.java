package sorts.bogo;

import java.util.ArrayList;
import main.ArrayVisualizer;
import sorts.templates.BogoSorting;
import sorts.templates.Parallelize;

 /*******************************************
  *          The Epsilon Committee          *
  * --------------------------------------- *
  * We've got 99 problems, and practicality *
  * ain't one                               *
  * ======================================= *
  * Author: Distray                         *
  *******************************************/

 public final class GnotAGnoblinSort extends BogoSorting implements Parallelize {
   public GnotAGnoblinSort(ArrayVisualizer arrayVisualizer) {
     super(arrayVisualizer);

     this.setSortListName("Gnot A Gnoblin");
     this.setRunAllSortsName("Gnot A Gnoblin Sort");
     this.setRunSortName("Gnot A Gnoblin Sort");
     this.setCategory("Bogo Sorts");
     this.setComparisonBased(true);
     this.setBucketSort(false);
     this.setRadixSort(false);
     this.setUnreasonablySlow(true);
     this.setUnreasonableLimit(5);
     this.setBogoSort(true);

   }
   protected double delay = 0.1;
   public int min, max;

   private int f(int v) {
       int n = v;
       while (v-->1)n*=v;
       return n;
   }

   public void gnomePass(int[] array, int s, int a) {
       int d = 0;
       while (a>s && Reads.compareIndices(array, a, --a, 0.05, true) < 0) {
           Writes.swap(array, a, a+1, 0.05, true, true); d++;
       }
          if (d<min) min=d;
       if (d>max) max=d;
   }
   protected Void gnomePass(Object... vals) {
       assert vals.length == 3;
       run("gnomePass", vals);
       return null;
   }
   public void runSort(int[] array, int length, int bucketCount) {
       ArrayList<int[]> p = new ArrayList<>();
       int fc = f(length);
       Func[] pool = new Func[fc];
       for (int i=fc; i>0; i--) {
           bogoSwap(array, 0, length, true);
           p.add(Writes.copyOfArray(array, length));
       }
       int[][] f = p.toArray(new int[0][]);
       p.clear();
       gnoblin:
       while (!isArraySorted(array, length)) {
           for (int i=1; i<length; i++) {
               min=length; max=0;
               for (int j=0; j<fc; j++) {
                   pool[j]=new Func(f[j], 0, i).setConsumer(this::gnomePass);
                   pool[j].start();
               }
               for (int j=0; j<fc; j++) {
                    try {
                        pool[j].join();
                    } catch(InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
               }
               if (min != max) {
                   for (int j=0; j<fc; j++) {
                       bogoSwap(f[j], 0, length, true);
                   }
                   continue gnoblin;
               }
           }
           int m=0, mi=0;
           for (int i=0; i<fc; i++) {
               int a=randInt(0,length),b=randInt(0,length),c=Math.abs(b-a);
               if (c>m) {m=c; mi=i;}
               Writes.multiSwap(f[i], a, b, 0.01, true, true);
           }
           Writes.arraycopy(f[mi], 0, array, 0, length, 1, true, false);
       }
       Writes.deleteExternalArrays(f);
   }
 }