package com.tmathmeyer.sexpr.data;

import java.io.PrintStream;

public interface List
{
    public List add(Object o);
    public List rest();
    public Object first() throws Exception;
    public void print(boolean start, PrintStream ps);
    public <T> java.util.LinkedList<T> asList();
}
