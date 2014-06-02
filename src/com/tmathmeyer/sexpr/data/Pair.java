package com.tmathmeyer.sexpr.data;

public class Pair<A, B>
{
    private final A a;
    private final B b;

    public Pair(A aa, B bb)
    {
        a = aa;
        b = bb;
    }

    public A getFirst()
    {
        return a;
    }

    public B getSecond()
    {
        return b;
    }
}