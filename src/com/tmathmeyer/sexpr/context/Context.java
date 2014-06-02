package com.tmathmeyer.sexpr.context;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.tmathmeyer.sexpr.data.DList;
import com.tmathmeyer.sexpr.data.Empty;
import com.tmathmeyer.sexpr.data.Pair;

public class Context
{
    public static Map<String, Func> getDefaultHashMap()
    {
        Map<String, Func> m = new HashMap<String, Func>();

        for (int i = 0; i < 10; i++)
        {
            final int j = i;
            m.put(i + "", new Func()
            {

                @Override
                public Object eval(Map<String, Func> map)
                {
                    return j;
                }

                @Override
                public Func addParam(Func func, Map<String, Func> ctx) throws Exception
                {
                    return this;
                }

                @Override
                public String getExpr()
                {
                    return j + "";
                }

            });
        }

        m.put("+", makePlus(null));
        m.put("*", makeMult(null));

        m.put("#t", makeBoolean(true));
        m.put("#f", makeBoolean(false));
        m.put("cond", makeCond(null, new LinkedList<Pair<Func, Func>>(), null));
        m.put("=", makeEqual(null, null));


        m.put("cons", makeCons(null, null));
        m.put("∅", makeEmpty());
        m.put("first", makeFirst(null));
        m.put("rest", makeRest(null));
        m.put("empty", makeEmptyTest(null));
        
        m.put("let", makeLet(null, null, null));
        m.put("defun", makeDefun(null, null));

        m.put("λ", makeLambda(null, null));
        m.put("begin", makeBegin(new LinkedList<Func>(), null));
        
        



        return m;
    }

    public static String mult(String s, int t)
    {
        StringBuilder sb = new StringBuilder(s.length() * t);
        for (int i = 0; i < t; i++)
        {
            sb.append(s);
        }
        return sb.toString();

    }

    public static Func makePlus(final List<Func> ints, final Func... additional)
    {
        return new Func()
        {
            public List<Func> sums = deepClone(ints, additional);

            private List<Func> deepClone(List<Func> ints, Func[] additional)
            {
                List<Func> n = new LinkedList<>();
                for (Func i : additional)
                {
                    n.add(i);
                }
                if (ints == null)
                {
                    return n;
                }
                for (Func i : ints)
                {
                    n.add(i);
                }
                return n;
            }

            @Override
            public Object eval(Map<String, Func> map) throws Exception
            {
                int sum = 0;
                for (Func i : sums)
                {
                    Object o = i.eval(map);
                    if (o instanceof Integer)
                    {
                        sum += (Integer)o;
                    }
                    else
                    {
                        throw new Exception();
                    }
                }
                sums = new LinkedList<Func>();
                return sum;
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {
                return makePlus(sums, func);
            }

            @Override
            public String getExpr()
            {
                return "+";
            }

        };
    }

    public static Func makeMult(final List<Integer> ints, final Integer... additional)
    {
        return new Func()
        {
            public List<Integer> sums = deepClone(ints, additional);

            private List<Integer> deepClone(List<Integer> ints, Integer[] additional)
            {
                List<Integer> n = new LinkedList<Integer>();
                for (Integer i : additional)
                {
                    n.add(i);
                }
                if (ints == null)
                {
                    return n;
                }
                for (Integer i : ints)
                {
                    n.add(i);
                }
                return n;
            }

            @Override
            public Object eval(Map<String, Func> map)
            {
                int sum = 1;
                for (Integer i : sums)
                {
                    sum *= i;
                }
                return sum;
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {
                return makeMult(sums, (Integer) func.eval(ctx));
            }

            @Override
            public String getExpr()
            {
                return "*";
            }

        };
    }

    public static Func makeBoolean(final boolean r)
    {
        return new Func()
        {

            @Override
            public Object eval(Map<String, Func> map)
            {
                return r;
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {
                return this;
            }

            @Override
            public String getExpr()
            {
                return r?"#t":"#f";
            }

        };
    }

    public static Func makeEmpty()
    {
        return new Func()
        {

            @Override
            public Object eval(Map<String, Func> map)
            {
                return new Empty();
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {
                return this;
            }

            @Override
            public String getExpr()
            {
                return "∅";
            }

        };
    }

    public static Func makeCons(final Func o, final Object a)
    {
        return new Func()
        {

            Func olist = o;
            Object additional = a;

            @Override
            public Object eval(Map<String, Func> map) throws Exception
            {
                return new DList(additional, (com.tmathmeyer.sexpr.data.List) olist.eval(map));
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {
                if (additional == null)
                {
                    return makeCons(null, func.eval(ctx));
                }
                if (olist == null)
                {
                    return makeCons(func, additional);
                }
                throw new Exception();
            }

            @Override
            public String getExpr()
            {
                return "cons";
            }

        };
    }

    public static Func makeFirst(final com.tmathmeyer.sexpr.data.List o)
    {
        return new Func()
        {

            com.tmathmeyer.sexpr.data.List list = o;

            @Override
            public Object eval(Map<String, Func> map) throws Exception
            {
                return list.first();
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {
                if (list == null)
                {
                    return makeFirst((com.tmathmeyer.sexpr.data.List) func.eval(ctx));
                }
                throw new Exception();
            }

            @Override
            public String getExpr()
            {
                return "first";
            }

        };
    }


    public static Func makeRest(final com.tmathmeyer.sexpr.data.List o)
    {
        return new Func()
        {

            com.tmathmeyer.sexpr.data.List list = o;

            @Override
            public Object eval(Map<String, Func> map) throws Exception
            {
                return list.rest();
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {
                if (list == null)
                {
                    return makeRest((com.tmathmeyer.sexpr.data.List) func.eval(ctx));
                }
                throw new Exception();
            }

            @Override
            public String getExpr()
            {
                return "rest";
            }

        };
    }



    public static Func makeLet(final String c, final Func eve, final Func evn)
    {
        return new Func()
        {

            Func evaluation = evn;
            String name = c;
            Func evaluee = eve;

            @Override
            public Object eval(Map<String, Func> map) throws Exception
            {
                final Object evaled = evaluee.eval(map);
                map.put(name, new Func(){

                    @Override
                    public Object eval(Map<String, Func> map) throws Exception
                    {
                        return evaled;
                    }

                    @Override
                    public Func addParam(Func func, Map<String, Func> ctx) throws Exception
                    {
                        return this;
                    }

                    @Override
                    public String getExpr()
                    {
                        return "contextbreakλ"+name;
                    }
                    
                });
                return evaluation.eval(map);
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {
                if (name == null)
                {
                    String[] descr = func.getExpr().split("λ");
                    if ("contextbreak".equals(descr[0]))
                    {
                        return makeLet(descr[1], null, null);
                    }
                    else
                    {
                        Object evd = func.eval(ctx);
                        return makeLet((String) func.eval(ctx), null, null);
                    }
                }
                if (evaluee == null)
                {
                    return makeLet(name, func, null);
                }
                if (evaluation == null)
                {
                    return makeLet(name, evaluee, func);
                }
                throw new Exception();
            }

            @Override
            public String getExpr()
            {
                return "let";
            }

        };
    }

    public static Func makeDefun(final String n, final Func eval)
    {
        return new Func()
        {

            Func evaluation = eval;
            String name = n;

            @Override
            public Object eval(Map<String, Func> map) throws Exception
            {
                if (map != null)
                {
                    if ("λ".equals(evaluation.getExpr()))
                    {
                        evaluation = (Func) evaluation.eval(map);
                        map.put(name, evaluation);
                        return evaluation;
                    }
                    map.put(name, evaluation);
                }
                return evaluation.eval(map);
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {
                if (name == null)
                {
                    if ("string".equals(func.getExpr()))
                    {
                        return makeDefun((String) func.eval(ctx), null);
                    }
                    else
                    {
                        return makeDefun(func.getExpr(), null);
                    }
                }
                if (evaluation == null)
                {
                    return makeDefun(name, func);
                }
                throw new Exception();
            }

            @Override
            public String getExpr()
            {
                return "defun";
            }

        };
    }

    public static Func makeBegin(final LinkedList<Func> list, final Func next)
    {
        return new Func()
        {

            LinkedList<Func> fxns = new LinkedList<Func>();

            @Override
            public Object eval(Map<String, Func> map) throws Exception
            {
                Object o = null;
                for (Func f : fxns)
                {
                    o = f.eval(map);
                }
                return o;
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {
                fxns.add(func);
                return this;
            }

            @Override
            public String getExpr()
            {
                return "begin";
            }

        };
    }

    public static Func makeLambda(final Func p, final Func b)
    {

        return new Func()
        {
            Func params = p;
            Func body = b;

            @Override
            public Object eval(Map<String, Func> map) throws Exception
            {
                return new Func()
                {
                    String[] functions = params.getExpr().split(" ");
                    int fxnid = 0;

                    @Override
                    public Object eval(Map<String, Func> map) throws Exception
                    {
                        fxnid = 0;
                        return body.eval(map);
                    }

                    @Override
                    public Func addParam(Func func, Map<String, Func> ctx) throws Exception
                    {
                        if (fxnid < functions.length)
                        {
                            ctx.put(functions[fxnid++], func);
                        }
                        else
                        {
                            throw new Exception();
                        }
                        return this;
                    }

                    @Override
                    public String getExpr()
                    {
                        return body.getExpr().split(" ")[0];
                    }
                };
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {
                if (params == null)
                {
                    return makeLambda(func, body);
                }
                if (body == null)
                {
                    return makeLambda(params, func);
                }
                return makeLambda(params, body);
                //throw new Exception();
            }

            @Override
            public String getExpr()
            {
                return "lambda";
            }
        };
    }


    public static Func makeCond(final Func test, final List<Pair<Func, Func>> conds, final Pair<Func, Func> newCond)
    {
        return new Func()
        {
            List<Pair<Func, Func>> conditions = deepClone(conds, newCond);
            Func ev = test;

            @Override
            public Object eval(Map<String, Func> map) throws Exception
            {
                for(Pair<Func, Func> branch : conditions)
                {
                    Object testEv = branch.getFirst().eval(map);
                    if (testEv instanceof Boolean)
                    {
                        if (testEv.equals(true))
                        {
                            return branch.getSecond().eval(map);
                        }
                    }
                    else
                    {
                        throw new Exception();
                    }
                }
                throw new Exception();
            }

            private List<Pair<Func, Func>> deepClone(List<Pair<Func, Func>> conds, Pair<Func, Func> newCond)
            {
                List<Pair<Func, Func>> newList = new LinkedList<Pair<Func, Func>>();
                for(Pair<Func, Func> pair : conds)
                {
                    newList.add(pair);
                }
                if (newCond != null)
                {
                    newList.add(newCond);
                }
                return newList;
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {
                if (ev == null)
                {
                    return makeCond(func, conditions, null);
                }
                return makeCond(null, conditions, new Pair<Func, Func>(ev, func));
            }

            @Override
            public String getExpr()
            {
                return "cond";
            }
        };
    }


    public static Func makeEqual(final Func a, final Func b)
    {
        return new Func()
        {

            Func aa = a;
            Func bb = b;

            @Override
            public Object eval(Map<String, Func> map) throws Exception
            {
                Object aaa = a.eval(map);
                Object bbb = b.eval(map);

                return aaa.getClass().equals(bbb.getClass()) && aaa.equals(bbb);
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {
                if (aa == null)
                {
                    return makeEqual(func, null);
                }
                if (bb == null)
                {
                    return makeEqual(aa, func);
                }
                throw new Exception();
            }

            @Override
            public String getExpr()
            {
                return "=";
            }

        };
    }

    public static Func makeEmptyTest(final Func lev)
    {
        return new Func()
        {
            Func listeval = lev;

            @Override
            public Object eval(Map<String, Func> map) throws Exception
            {
                Object lis = listeval.eval(map);

                return lis instanceof Empty;
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {
                if (listeval == null)
                {
                    return makeEmptyTest(func);
                }
                throw new Exception();
            }

            @Override
            public String getExpr()
            {
                return "empty";
            }

        };
    }



    public static Func stringFunc(final String string)
    {
        return new Func()
        {

            @Override
            public Object eval(Map<String, Func> map) throws Exception
            {
                return string;
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {
                return this;
            }

            @Override
            public String getExpr()
            {
                return "string";
            }
        };
    }

}
