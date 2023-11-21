package dev.patrick.mealmaker.util;

import java.util.AbstractList;

public class ArrayList<E> extends AbstractList<E> {

    private E[] list;
    private int size;

    @SuppressWarnings("unchecked")
    public ArrayList() {

        list = (E[]) new Object[3];
        size = 0;

    }

    @Override
    public boolean add(E e) {

        if (size == 3) {
            throw new IllegalArgumentException();
        }

        list[size++] = e;

        return true;
    }

    @Override
    public E get(int index) {
        return list[index];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {

        String rtnString = "";

        for (int i = 0; i < size; i++) {

            rtnString += list[i] + ",";

        }

        return rtnString.substring(0, rtnString.length() - 1);

    }
}
