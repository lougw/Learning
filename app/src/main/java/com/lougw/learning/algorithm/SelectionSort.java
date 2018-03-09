package com.lougw.learning.algorithm;

import android.util.Log;

/**
 * Created by lougw on 18-3-9.
 */

public class SelectionSort {
    public void sort1() {
        int[] array = new int[]{2, 5, 1, 7, 3, 9, 2, 7};

        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i; j < array.length; j++) {
                if (array[i] > array[j]) {
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }

        for (int i = 0; i < array.length; i++) {
            Log.d("SelectionSort",Integer.toString(array[i]));
        }
    }
    public void sort2() {
        Log.d("SelectionSort","---------------------------------------------------");
        int[] array = new int[]{2, 5, 1, 7, 3, 9, 2, 7};

        for (int i = 0; i < array.length - 1; i++) {
            int index=i;
            for (int j = i; j < array.length; j++) {
                if (array[i] > array[j]) {
                    index=j;
                }
            }
            if(index!=i){
                int temp = array[i];
                array[i] = array[index];
                array[index] = temp;
            }
        }

        for (int i = 0; i < array.length; i++) {
            Log.d("SelectionSort",Integer.toString(array[i]));
        }
    }
}
