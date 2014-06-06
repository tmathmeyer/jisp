package com.tmathmeyer.sexpr.data;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class DList implements List
{

    Object first;
    List rest;
    
    public DList(Object o, List rest2)
    {
        this.first = o;
        this.rest = rest2;
    }

    @Override
    public List add(Object o)
    {
        return new DList(o, rest);
    }

    @Override
    public Object first() throws Exception
    {
        return first;
    }

    @Override
    public void print(boolean start, PrintStream ps)
    {
        if (start)
        {
            ps.print("[");
        }
        else
        {
            ps.print(",");
        }
        ps.print(first.toString());
        rest.print(false, ps);
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
        return rest;
    }

	@SuppressWarnings("unchecked")
	@Override
	public <T> java.util.LinkedList<T> asList()
	{
		java.util.LinkedList<T> rest = this.rest.asList();
		rest.addFirst((T)first);
		return rest;
	}
}
