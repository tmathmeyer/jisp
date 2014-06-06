package com.tmathmeyer.sexpr;

import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.Map.Entry;

import com.tmathmeyer.sexpr.context.Context;
import com.tmathmeyer.sexpr.context.Func;
import com.tmathmeyer.sexpr.tokens.EmptyTokenTree;
import com.tmathmeyer.sexpr.tokens.Token;
import com.tmathmeyer.sexpr.tokens.TokenTree;

public class Parser
{
	private final String program;
	private final LinkedList<Token> tokens;
	private final TokenTree tokTree;
	private final boolean debug;
	
	public Parser(String string)
	{
		this(string, true);
	}
	
    public Parser(String string, boolean d)
    {
		program = removeAllDoubleSpaces(string);
		tokens = tokenise(program);
		TokenTree build = new EmptyTokenTree();
		for(Token t : tokens)
		{
			build = build.add(t, t.sdepth);
		}
		tokTree = build.lock();
		debug = d;
	}
    
    public Object parse()
    {
    	return parse(Context.getDefaultContext());
    }
    
    public Object parse(Map<String, Func> context)
    {
    	try
    	{
			return tokTree.eval(context);
		}
    	catch (Exception e)
    	{
    		if (debug)
    		{
    			for(Entry<String, Func> ff : context.entrySet())
                {
                    System.out.print(ff.getKey() + " : ");
                    System.out.println(ff.getValue().getExpr());
                }
    		}
            e.printStackTrace();
		}
    	return null;
    }
    
    public <T> T parse(Class<T> castType)
    {
    	return castType.cast(parse());
    }
    
    public <T> T parse(Class<T> castType, Map<String, Func> context)
    {
    	return castType.cast(parse(context));
    }
    
    public <T> List<T> parseAsList(Class<T> castType)
    {
    	return parse(com.tmathmeyer.sexpr.data.List.class).asList();
    }
    
    public <T> List<T> parseAsList(Class<T> castType, Map<String, Func> context)
    {
    	return parse(com.tmathmeyer.sexpr.data.List.class, context).asList();
    }
    
    public static Parser buildParser(List<String> loc)
    {
    	StringBuilder program = new StringBuilder();
    	for(String line : loc)
    	{
    		program.append(line);
    	}
    	return new Parser(program.toString());
    }
    
    
    private String removeAllDoubleSpaces(String str)
    {
    	return str.replaceAll("\\s+"," ");
    }
    
    private LinkedList<Token> tokenise(String src)
    {
        LinkedList<Token> tok = new LinkedList<Token>();
        StringBuilder cnt = new StringBuilder();;
        char[] rawsrc = src.toCharArray();
        int sdepth = -1;
        for(int i = 0; i < rawsrc.length; i++)
        {
            if (rawsrc[i] == '(')
            {
                Token t = new Token(sdepth, cnt.toString());
                if (!t.isEmpty())
                {
                    tok.add(t);
                }
                sdepth++;
                
                cnt = new StringBuilder();
                
            }
            else if (rawsrc[i] == ')')
            {
                Token t = new Token(sdepth, cnt.toString());
                if (!t.isEmpty())
                {
                    tok.add(t);
                }
                cnt = new StringBuilder();
                sdepth--;
            }
            else
            {
                cnt.append(rawsrc[i]);
            }
        }
        return tok;
    }   
}