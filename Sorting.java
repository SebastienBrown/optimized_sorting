import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

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
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new SortTask(data, 0, data.length - 1));
        pool.shutdown();
//        System.out.println("Final output is " + Arrays.toString(data));
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
//            System.out.println("Compute called with left: " + left + ", right: " + right);
            // Base case:
            if (right - left < BASECASE) {
                Arrays.sort(data, left, right + 1);
                return;
            }

            // Partition the array
            int pivotIndex = partition(data, left, right);

            // Create sub-tasks for the left and right partitions
            SortTask leftTask = new SortTask(data, left, pivotIndex - 1);
            SortTask rightTask = new SortTask(data, pivotIndex + 1, right);
//          System.out.println(Arrays.toString(Arrays.copyOfRange(data, left, right)));
            // Fork the sub-tasks and wait for them to complete
            leftTask.fork();
            rightTask.fork();
            leftTask.join();
            rightTask.join();
            }
        }

    private static int partition(float[] data, int left, int right) {
        // CODE FOR ALWAYS PICKING THE RIGHT-MOST VALUE AS PIVOT --> WORKS
//        float pivot = data[right];
//        int i = left - 1;
//        for (int j = left; j <= right - 1; j++) {
//            if (data[j] < pivot) {
//                i++;
//                swap(data, i, j);
//            }
//        }
//        swap(data, i+1, right);
//        return i+1;

        // Median-of-three approach
        int mid = left + (right - left) / 2;
        float pivot;
        if((data[left] <= data[mid] && data[mid] <= data[right])
        || (data[right] <= data[mid] && data[mid] <= data[left])) {
            pivot = data[mid];
            swap(data, mid, right);
        } else if ((data[left] <= data[right] && data[right] < data[mid])
        || data[mid] <= data[right] && data[right] <= data[left]) {
            pivot = data[right];
        } else {
            pivot = data[left];
            swap(data, left, right);
        }
        int i = left - 1;
        for (int j = left; j <= right - 1; j++) {
            if (data[j] < pivot) {
                i++;
                swap(data, i, j);
            }
        }
        swap(data, i+1, right);
        return i+1;
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

