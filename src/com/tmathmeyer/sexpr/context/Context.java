package com.tmathmeyer.sexpr.context;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.tmathmeyer.sexpr.data.DList;
import com.tmathmeyer.sexpr.data.Empty;

public class Context
{
    public static Map<String, Func> getDefaultHashMap()
    {
        Map<String, Func> m = new HashMap<String, Func>();

        for(int i = 0; i < 10; i++)
        {
            final int j = i;
            m.put(i+"", new Func(){

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

            });
        }

        m.put("+", makePlus(null));
        m.put("*", makeMult(null));
        m.put("cons", makeCons(null,null));
        m.put("empty", makeEmpty());
        m.put("let", makeLet(null, null, null));
        m.put("first", makeFirst(null));
        m.put("defun", makeDefun(null, null));
        
        m.put("program", makeProgram(new LinkedList<Func>(), null));

        return m;
    }



    public static String mult(String s, int t)
    {
        StringBuilder sb = new StringBuilder(s.length() * t);
        for(int i=0; i<t; i++) 
        {
            sb.append(s);
        }
        return sb.toString();

    }

    public static Func makePlus(final List<Integer> ints, final Integer... additional)
    {
        return new Func(){
            public List<Integer> sums = deepClone(ints, additional);

            private List<Integer> deepClone(List<Integer> ints, Integer[] additional)
            {
                List<Integer> n = new LinkedList<Integer>();
                for(Integer i : additional)
                {
                    n.add(i);
                }
                if (ints == null)
                {
                    return n;
                }
                for(Integer i : ints)
                {
                    n.add(i);
                }
                return n;
            }

            @Override
            public Object eval(Map<String, Func> map)
            {
                int sum = 0;
                for(Integer i: sums)
                {
                    sum += i;
                }
                return sum;
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {    
                return makePlus(sums, (Integer) func.eval(ctx));
            }

        };
    }
    
    public static Func makeMult(final List<Integer> ints, final Integer... additional)
    {
        return new Func(){
            public List<Integer> sums = deepClone(ints, additional);

            private List<Integer> deepClone(List<Integer> ints, Integer[] additional)
            {
                List<Integer> n = new LinkedList<Integer>();
                for(Integer i : additional)
                {
                    n.add(i);
                }
                if (ints == null)
                {
                    return n;
                }
                for(Integer i : ints)
                {
                    n.add(i);
                }
                return n;
            }

            @Override
            public Object eval(Map<String, Func> map)
            {
                int sum = 1;
                for(Integer i: sums)
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

        };
    }
    
    public static Func makeEmpty()
    {
        return new Func(){
            

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

        };
    }
    
    public static Func makeCons(final Func o, final Object a)
    {
        return new Func(){
            
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

        };
    }
    
    public static Func makeFirst(final com.tmathmeyer.sexpr.data.List o)
    {
        return new Func(){
            
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

        };
    }
    
    public static Func makeLet(final String c, final Func eve, final Func evn)
    {
        return new Func(){
            
            Func evaluation = evn;
            String name = c;
            Func evaluee = eve;
            

            @Override
            public Object eval(Map<String, Func> map) throws Exception
            {
                if (map != null)
                {
                    map.put(name, evaluee);
                }
                return evaluation.eval(map);
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {
                if (name == null)
                {
                    return makeLet((String) func.eval(ctx), null, null);
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

        };
    }

    public static Func makeDefun(final String n, final Func eval)
    {
        return new Func(){
            
            Func evaluation = eval;
            String name = n;
            

            @Override
            public Object eval(Map<String, Func> map) throws Exception
            {
                if (map != null)
                {
                    map.put(name, evaluation);
                }
                return evaluation.eval(map);
            }

            @Override
            public Func addParam(Func func, Map<String, Func> ctx) throws Exception
            {
                if (name == null)
                {
                    return makeDefun((String) func.eval(ctx), null);
                }
                if (evaluation == null)
                {
                    return makeDefun(name, func);
                }
                throw new Exception();
            }

        };
    }
    
    public static Func makeProgram(final LinkedList<Func> list, final Func next)
    {
        return new Func(){
            
            LinkedList<Func> fxns = new LinkedList<Func>();
            
            @Override
            public Object eval(Map<String, Func> map) throws Exception
            {
                Object o = null;
                for(Func f : fxns)
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
        };
    }
}
