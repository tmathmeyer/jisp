package com.tmathmeyer.sexpr.context;

import java.util.Map;

public interface Func
{
    Object eval(Map<String, Func> map) throws Exception;
    public Func addParam(Func func, Map<String, Func> ctx) throws Exception;
}
