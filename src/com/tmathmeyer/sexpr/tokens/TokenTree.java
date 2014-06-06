package com.tmathmeyer.sexpr.tokens;

import java.util.Map;

import com.tmathmeyer.sexpr.context.Func;

public interface TokenTree
{
    TokenTree add(Token t, int depth);
    TokenTree lock();
    Object eval(Map<String, Func> context) throws Exception;

    void print(int i);
    String getExpr();
}
