package com.alg;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *  CS4050 - Algorithms and Algorithm Analysis
 *
 *  QuicksortMedian3.java
 *
 *  Imports a provided file containing consecutive integers of random order into an array to
 *  use the "Median of 3" Quicksort algorithm and calculate the time cost of sorting
 *  the provided data.
 **/

public class QuicksortMedian3 {

    private long[] data;
    private int len;

    // create int array for importing data
    public QuicksortMedian3(int max) {
        data = new long[max];
        len = 0;
    }

    // init (helper function)
    public void quickSort() {
        recursiveQuicksort(0, len - 1);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * *
     *  Recursive quicksort algorithm implementing
     *  median of thee approach to determine the
     *  pivot.
     *
     *  simpleSort added to improve small data sets
     *  with partitions containing 3 or less numbers.
     * * * * * * * * * * * * * * * * * * * * * * * * */
    public void recursiveQuicksort(int left, int right) {
        int size = right - left + 1;
        // if range is larger than three use quicksort
        if (size >= 3) {
            long median = medianOf3(left, right);    // determine partition
            int partition = partitionIterator(left, right, median);  // create partition
            recursiveQuicksort(left, partition - 1);    // sort left partition
            recursiveQuicksort(partition + 1, right);   // sort right partition
        }
        else {
            simpleSort(left, right);
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * *
      *  medianOf3 used to determine the location of
      *  the pivot used for recursiveQuicksort.
      * * * * * * * * * * * * * * * * * * * * * * * * */
    public long medianOf3(int left, int right) {
        int center = (left + right) / 2;
        if (data[left] > data[center])
            swap(left, center);
        if (data[left] > data[right])
            swap(left, right);
        if (data[center] > data[right])
            swap(center, right);
        swap(center, right - 1);    // move the pivot to right
        return data[right - 1];     // return pointer to location of median
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * *
     *  partitionIterator controls partition flow
     *  using Hoare's partitioning algorithm
     * * * * * * * * * * * * * * * * * * * * * * * * */
    public int partitionIterator(int left, int right, long pivot) {
        int leftIndex = left; // right index of first element
        int rightIndex = right - 1; // left of pivot index

        while (true) {
            while (data[++leftIndex] < pivot) // scan for larger int
                ;
            while (data[--rightIndex] > pivot)  // scan for smaller int
                ;
            if (leftIndex >= rightIndex) // pointers crossed: break
                break;
            else
                swap(leftIndex, rightIndex); // pointers never crossed: swap elements
        }
        swap(leftIndex, right - 1); // restore pivot
        return leftIndex; // return pivot location
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * *
     *  simpleSort is to improve the algorithm's
     *  efficiency when partitions are three or
     *  less elements in size.
     * * * * * * * * * * * * * * * * * * * * * * * * */
    public void simpleSort(int left, int right) {
        int size = right - left + 1;
        if (size <= 1)
            return; // don't bother
        if (size == 2) {
            if (data[left] > data[right])
                swap(left, right);
            return;
        } else {
            if (data[left] > data[right - 1])
                swap(left, right - 1);
            if (data[left] > data[right])
                swap(left, right);
            if (data[right - 1] > data[right])
                swap(right - 1, right);
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * *
     *  swap indexes [ints] of two variables/arrays
     * * * * * * * * * * * * * * * * * * * * * * * * */
    public void swap(int index1, int index2) {
        long temp = data[index1];
        data[index1] = data[index2];
        data[index2] = temp;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * *
     *  Array builder for raw data
     * * * * * * * * * * * * * * * * * * * * * * * * */
    public void insert(long value) {
        data[len] = value;
        len++;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * *
     *  Display the current contents of 'data' array
     *  primarily for debugging purposes. [unused]
     * * * * * * * * * * * * * * * * * * * * * * * * */
    public void display() {
        System.out.print("Data: ");
        for (int j = 0; j < len; j++)
            System.out.print(data[j] + " ");
        System.out.println("");
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * *
     *  readArrayFromFile reads a list of integers
     *  from a provided file and adds each of them
     *  to an Array of appropriate size.
     * * * * * * * * * * * * * * * * * * * * * * * * */
    public static int[] readArrayFromFile(String file) {
        int [] numArray;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file))))
        {
            // use amount of numbers contained in file to determine the length of the Array
            String[] arr = reader.lines().toArray(size->new String[size]);
            numArray = new int [arr.length];
            for (int i = 0; i < arr.length; i++)   // convert Array contents from String to Integer
            {
                numArray[i] = Integer.valueOf(arr[i]);
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return numArray;
    }

    public static void main(String[] args) {
        int[] Array;
        Array = readArrayFromFile(System.getProperty("user.dir")+"/src/com/alg/Randomized_Numbers_5M.txt"); // filepath

        if (Array != null) // is it even worth it?
        {
            long first_ns, last_ns, time_ns;    // variables to keep track of calculation time
            float time_ms;

            QuicksortMedian3 arr = new QuicksortMedian3(Array.length); // init array

            int n;
            for (int i = 0; i < Array.length; i++)  // convert and fill array
            {
                n = Array[i];
                arr.insert(n);
            }
            System.out.println("Running QuicksortMedian3.java . . .");
            //arr.display();  // display unsorted numbers (original array)

            /** START TIMER **/
            first_ns = System.nanoTime();

            /** QUICKSORT EXECUTE **/
            arr.quickSort();

            /** STOP TIMER **/
            last_ns = System.nanoTime();

            //arr.display();  // display sorted numbers (final sorted array)
            time_ns = last_ns - first_ns;
            time_ms = ((float) time_ns / 1000000);
            System.out.println("\nUsing a quicksort 'median of 3' algorithm on an array of " + Array.length +
                               " unordered \nsequential numbers required the following amount of time: \n");
            System.out.println(time_ns + " nanoseconds");
            System.out.println(time_ms + " milliseconds");

        }
    }
}
