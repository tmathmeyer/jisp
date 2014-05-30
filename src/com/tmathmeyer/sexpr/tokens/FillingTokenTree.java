package com.tmathmeyer.sexpr.tokens;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.tmathmeyer.sexpr.context.Context;
import com.tmathmeyer.sexpr.context.Func;

public class FillingTokenTree implements TokenTree
{

    Token func;
    List<TokenTree> params = new LinkedList<TokenTree>();
    TokenTree last = null;
    
    
    public FillingTokenTree(Token t)
    {
        this.func = t;
    }
    
    @Override
    public TokenTree add(Token t, int depth)
    {
        if (depth == 1)
        {
            if (last != null)
            {
                params.add(last);
            }
            last = new EmptyTokenTree();
            last.add(t,depth-1);
        }
        else
        {
            if (last == null)
            {
                last = new EmptyTokenTree();
            }
            last = last.add(t, depth-1);
        }
        
        return this;
    }

    @Override
    public void print(int i)
    {
        System.out.println(Context.mult(" ", i) + "[" + func.tok+"]  ");
        for(TokenTree t : params)
        {
            t.print(i+2);
        }
        if (last != null)last.print(i+2);
    }

    @Override
    public void lock()
    {
        if (last != null)
        {
            params.add(last);
            last = null;
        }
        for(TokenTree tt : params)
        {
            tt.lock();
        }
    }

    @Override
    public Object eval(final Map<String, Func> context) throws Exception
    {
        String[] parts = func.tok.split(" ");
        Func f = context.get(parts[0]);
        
        if (f == null)
        {
            f = Context.stringFunc(parts[0]);
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
        
        for(TokenTree tt: params)
        {
            final TokenTree ttf = tt;
            f = f.addParam(new Func(){

                @Override
                public int getParamCount()
                {
                    return 0;
                }

                @Override
                public Object eval(Map<String, Func> map) throws Exception
                {
                    return ttf.eval(context);
                }

                @Override
                public Func addParam(Func func)
                {
                    return this;
                }

                @Override
                public String getName()
                {
                    return "I dunno";
                }
            });
        }
        
        return f.eval(context);
    }

}
