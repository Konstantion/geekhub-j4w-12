package edu.geekhub.utils.datastructures;

public interface SimpleList {
    String get(int index);
    void add(String value);
    void set(int index, String value);
    void delete(int index);
}
