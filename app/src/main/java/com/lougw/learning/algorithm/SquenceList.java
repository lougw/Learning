package com.lougw.learning.algorithm;

import java.util.Arrays;

/**
 * Created by lougw on 18-3-21.
 */

public class SquenceList<T> {
    private int DEFAULT_SIZE = 10;
    private int capacity;
    private Object[] elementData;
    private int size = 0;

    public SquenceList() {
        capacity = DEFAULT_SIZE;
        elementData = new Object[capacity];
    }

    public SquenceList(T element) {
        this();
        elementData[0] = element;
        size++;
    }

    public SquenceList(T element, int initSize) {
        capacity = 1;
        while (capacity < initSize) {
            capacity <<= 1;
        }
        elementData = new Object[capacity];
        elementData[0] = element;
        size++;
    }

    public void insert(T element, int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        ensureCapacity(size + 1);
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = element;
        size++;
    }

    public void add(T element) {
        insert(element, size);
    }

    public T delete(int index) {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException();
        }
        T oldValue = (T) elementData[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        }
        elementData[--size] = null;
        return oldValue;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > capacity) {
            while (capacity < minCapacity) {
                capacity <<= 1;
            }
            elementData = Arrays.copyOf(elementData, capacity);
        }
    }
}
