package com.tmathmeyer.sexpr;

import java.util.List;

public class Sexpr
{
    private final Defun func;
    private final List<Sexpr> params;
    
    public Sexpr(Defun f, List<Sexpr> l)
    {
        func = f;
        params = l;
    }
    
    public void eval()
    {
        func.call(params);
    }
}
