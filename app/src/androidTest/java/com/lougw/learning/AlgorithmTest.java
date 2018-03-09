package com.lougw.learning;

import android.support.test.runner.AndroidJUnit4;

import com.lougw.learning.algorithm.SelectionSort;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by lougw on 18-3-9.
 */
@RunWith(AndroidJUnit4.class)
public class AlgorithmTest {
    @Test
    public void testSort() {
        SelectionSort selection = new SelectionSort();
        selection.sort1();
        selection.sort2();
    }


}
