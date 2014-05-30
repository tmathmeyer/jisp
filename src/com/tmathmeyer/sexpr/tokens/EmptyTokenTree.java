package com.tmathmeyer.sexpr.tokens;

import java.util.Map;

import com.tmathmeyer.sexpr.context.Context;
import com.tmathmeyer.sexpr.context.Func;

public class EmptyTokenTree implements TokenTree
{

    Token base;
    
    public EmptyTokenTree()
    {
    }

    @Override
    public TokenTree add(Token t, int depth)
    {
        if (base == null)
        {
            base = t;
            return this;
        }
        else
        {
            TokenTree tt = new FillingTokenTree(base);
            tt.add(t, depth);
            return tt;
        }
    }

    @Override
    public void print(int i)
    {
        if (base != null)
        System.out.println(Context.mult(" ", i) + base.tok);
    }

    @Override
    public void lock()
    {
        
    }
    
    @Override
    public Object eval(Map<String, Func> context) throws Exception
    {
        String[] parts = base.tok.split(" ");
        Func f = context.get(parts[0]);
        
        if (f == null)
        {
            return null;
        }
        
        for(int i = 1; i < parts.length; i++)
        {
            Func c = context.get(parts[i]);
            if (c == null)
            {
                c = Context.stringFunc(parts[i]);
            }
            f = f.addParam(c);
        }
        
        return f.eval(context);
    }

}
