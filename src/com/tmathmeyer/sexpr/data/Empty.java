package com.tmathmeyer.sexpr.data;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Empty implements List
{

    @Override
    public List add(Object o)
    {
        return new DList(o, this);
    }

    @Override
    public Object first() throws Exception
    {
        throw new Exception();
    }

    @Override
    public void print(boolean start, PrintStream ps)
    {
        if (start)
        {
            ps.print("[");
        }
        ps.print("]");
    }

    public String toString()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        print(true, ps);
        String content;
        try
        {
            content = baos.toString("UTF8");
        }
        catch (UnsupportedEncodingException e)
        {
            return "";
        }
        return content;
    }

    @Override
    public List rest()
    {
        return this;
    }
}
