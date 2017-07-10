package com.athena.library.data;


public class SortElement {

    public String name;        //条件名字
    public SortWeight rank;    //排序优先级
    public int count;          //条件个数
    public long weight;        //初始权重

    public SortElement(String name, SortWeight rank, int count) {
        this.name = name;
        this.rank = rank;
        this.count = count;
    }

    @Override
    public String toString() {
        return "name:" + name + "\t rank:" + rank + "\t count:" + count + "\t weight:" + weight;
    }
}
