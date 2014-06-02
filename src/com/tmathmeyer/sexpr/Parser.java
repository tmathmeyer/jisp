package com.tmathmeyer.sexpr;

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
    public static void main(String... args) throws Exception
    {
        String emptytest = "(empty (first (cons 3 ∅)))";
        
        String recursion = "(begin (defun inc (λ (aaa) (+ 1 aaa))) (inc (inc 1))) ";
        
        String map = "(defun linc (λ (lis fxn) (cond (empty lis) (∅) (#t) (let fir (first lis) (let res (rest lis) (let incr (fxn fir) (cons incr (linc res))))))))";
        String inc = "(defun inc (λ (ins) (+ 1 ins)))";
        
        String range = "(defun range (λ (siz) (cond (= siz 9) (∅) (#t) (let nsiz (+ 1 siz) (cons siz (range nsiz))))))";
        
        //String fun = "(begin "+map+" ("
        
        String callRange = "(begin "+range+" (range 0))";
        String callLinc = "(begin " + map + " " + inc + " " + range + "(linc (range 0) inc))";
        
        String huh = "(cond (= 2 10) (+ 2 2) (= 2 2) (∅))";
        
        String realrange = "(begin (defun range (λ (start end) (cond (= start end) (∅) (#t) (let next (+ 1 start) (cons next (range next end)))))) (range 0 (* 4 5)))";
        
        String heh = "(begin (defun range (λ (start end) (cond (= start end) (∅) (#t) (let next (+ 1 start) (cons next (range next end)))))) (let 100 (* 5 5 4) (range 0 100)))";
        
        
        new Parser().parse(heh);
    }
    
    
    public Object parse(String s) throws Exception
    {
        LinkedList<Token> tok = tokeniser(s);
        
        TokenTree t = new EmptyTokenTree();
        for(Token tt : tok)
        {
            t = t.add(tt, tt.sdepth);
            
        }
        
        t.lock();
        //t.print(0);
        Map<String, Func> ctx = Context.getDefaultHashMap();
        try
        {
            Object a = t.eval(ctx);
            System.out.println(a);
            return a;
        }
        catch(Exception e)
        {
            for(Entry<String, Func> ff : ctx.entrySet())
            {
                System.out.print(ff.getKey() + " : ");
                System.out.println(ff.getValue().getExpr());
            }
            e.printStackTrace();
            throw e;
        }
    }
    
    public LinkedList<Token> expand(LinkedList<Token> compressed)
    {
        LinkedList<Token> tok = new LinkedList<Token>();
        
        for(Token tkn : compressed)
        {
            String[] toks = tkn.tok.split(" ");
            tok.add(new Token(tkn.sdepth, toks[0]));
            
            for(int i=1; i< toks.length; i++)
            {
                tok.add(new Token(tkn.sdepth, toks[i])); 
            }
        }
        
        System.out.println();
        return tok;
        
    }
    
    public LinkedList<Token> tokeniser(String src)
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