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

        for(int i = 0; i < 9; i++)
        {
            final int j = i;
            m.put(i+"", new Func(){

                @Override
                public int getParamCount()
                {
                    return 0;
                }

                @Override
                public Object eval(Map<String, Func> map)
                {
                    return j;
                }

                @Override
                public Func addParam(Func func)
                {
                    return this;
                }

                @Override
                public String getName()
                {
                    return ""+j;
                }

            });
        }

        m.put("+", makePlus(null));
        m.put("*", makeMult(null));
        m.put("cons", makeCons(null,null));
        m.put("empty", makeEmpty());
        m.put("let", makeLet(null, null, null));


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

            @Override
            public int getParamCount()
            {
                return 0;
            }

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
            public Func addParam(Func func) throws Exception
            {    
                return makePlus(sums, (Integer) func.eval(null));
            }

            @Override
            public String getName()
            {
                return "plus";
            }

        };
    }
    
    public static Func makeMult(final List<Integer> ints, final Integer... additional)
    {
        return new Func(){
            public List<Integer> sums = deepClone(ints, additional);

            @Override
            public int getParamCount()
            {
                return 0;
            }

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
            public Func addParam(Func func) throws Exception
            {    
                return makeMult(sums, (Integer) func.eval(null));
            }

            @Override
            public String getName()
            {
                return "multiply";
            }

        };
    }
    
    public static Func makeEmpty()
    {
        return new Func(){
            
            @Override
            public int getParamCount()
            {
                return 0;
            }

            @Override
            public Object eval(Map<String, Func> map)
            {
                return new Empty();
            }

            @Override
            public Func addParam(Func func) throws Exception
            {    
                return this;
            }

            @Override
            public String getName()
            {
                return "empty";
            }

        };
    }
    
    public static Func makeCons(final Func o, final Object a)
    {
        return new Func(){
            
            Func olist = o;
            Object additional = a;
            
            @Override
            public int getParamCount()
            {
                return 0;
            }

            @Override
            public Object eval(Map<String, Func> map) throws Exception
            {
                return new DList(additional, (com.tmathmeyer.sexpr.data.List) olist.eval(map));
            }

            @Override
            public Func addParam(Func func) throws Exception
            {
                if (additional == null)
                {
                    return makeCons(null, func.eval(null));
                }
                if (olist == null)
                {
                    return makeCons(func, additional);
                }
                throw new Exception();
            }

            @Override
            public String getName()
            {
                return "empty";
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
            public int getParamCount()
            {
                return 0;
            }

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
            public Func addParam(Func func) throws Exception
            {
                if (name == null)
                {
                    return makeLet((String) func.eval(null), null, null);
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
            public String getName()
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
            public int getParamCount()
            {
                return 0;
            }

            @Override
            public Object eval(Map<String, Func> map) throws Exception
            {
                return string;
            }

            @Override
            public Func addParam(Func func) throws Exception
            {
                return this;
            }

            @Override
            public String getName()
            {
                return "String";
            }
        };
    }
}
