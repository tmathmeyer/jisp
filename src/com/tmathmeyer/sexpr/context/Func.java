package com.tmathmeyer.sexpr.context;

import java.util.Map;

public interface Func
{
    int getParamCount();
    Object eval(Map<String, Func> map) throws Exception;
    Func addParam(Func func) throws Exception;
    String getName();
}
