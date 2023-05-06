import java.util.Arrays;
 import java.util.concurrent.ForkJoinPool;
 import java.util.concurrent.RecursiveAction;
 import java.util.concurrent.ThreadLocalRandom;

 public class Sorting {
     // replace with your 
     public static final String TEAM_NAME = "baseline";

     /**
      * Sorts an array of doubles in increasing order. This method is a
      * single-threaded baseline implementation.
      *
      * @param data the array of doubles to be sorted
      */
     public static void baselineSort(float[] data) {
         Arrays.sort(data, 0, data.length);
     }

     /**
      * Sorts an array of doubles in increasing order. This method is a
      * multi-threaded optimized sorting algorithm. For large arrays (e.g., arrays of size at least 1 million) it should be significantly faster than baselineSort.
      *
      * @param data   the array of doubles to be sorted
      */

     private static final int BASECASE = 10000; // Threshold for normal sorting

     public static void parallelSort(float[] data) {
         ForkJoinPool pool = new ForkJoinPool(32);
         pool.invoke(new SortTask(data, 0, data.length - 1));
         pool.shutdown();
         //System.out.println("Final output is " + Arrays.toString(data));
     }

     public static class SortTask extends RecursiveAction {
         private float[] data;
         private int left;
         private int right;

         public SortTask(float[] data, int left, int right) {
             this.data = data;
             this.left = left;
             this.right = right;
         }

         @Override
         protected void compute() {
             //System.out.println("Compute called with left: " + left + ", right: " + right);
             // Base case:
             if (right - left < BASECASE) {
                 Arrays.sort(data, left, right+1);
                 return;
             }


        int pivotIndex=partition(data,left,right);
        
                 // Create sub-tasks for the left and right partitions
                 SortTask leftTask = new SortTask(data, left, pivotIndex);
             SortTask rightTask = new SortTask(data, pivotIndex + 1, right);
                 //System.out.println(Arrays.toString(Arrays.copyOfRange(data, left, right)));
                 // Fork the sub-tasks and wait for them to complete
                 leftTask.fork();
                 rightTask.fork();
                 leftTask.join();
                 rightTask.join();
             }
         }

    private static int partition(float[] data, int low, int high) {
         
        float pivot = data[low];
        int i = low - 1;
        int j = high + 1;
 
        while (true)
        {
            do {
                i++;
            } while (data[i] < pivot);
 
            do {
                j--;
            } while (data[j] > pivot);
 
            if (i >= j) {
                return j;
            }
 
            swap(data, i, j);
        }
     }
     

     private static void swap(float[] data, int i, int j) {
         float temp = data[i];
         data[i] = data[j];
         data[j] = temp;
     }

     /**
      * Determines if an array of doubles is sorted in increasing order.
      *
      * @param data the array to check for sortedness
      * @return `true` if the array is sorted, and `false` otherwise
      */
     public static boolean isSorted(float[] data) {
         double prev = data[0];

         for (int i = 1; i < data.length; ++i) {
             if (data[i] < prev) {
                 return false;
             }

             prev = data[i];
         }

         return true;
     }
 }
