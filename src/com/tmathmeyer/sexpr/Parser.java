package com.tmathmeyer.sexpr;

import java.util.LinkedList;

import com.tmathmeyer.sexpr.context.Context;
import com.tmathmeyer.sexpr.tokens.EmptyTokenTree;
import com.tmathmeyer.sexpr.tokens.Token;
import com.tmathmeyer.sexpr.tokens.TokenTree;

public class Parser
{
    public static void main(String... args) throws Exception
    {
        String f = "(+ (+ (+ 1 2) (+ 3 4)) (+ (+ 5 6) (+ 7 8)))";
        String k = "(+ 1 2 3 4 5 6)";
        String m = "(+ 1 (* 2 3))";
        
        String L = "(L (x y) (+ x y))";
        String def = "(def name (+ 3 2))";
        
        String empty = "(cons (cons 1 empty) (cons 2 empty))";
        
        String omfg = "(let ten (* 2 5) (let hundo (* ten ten) (hundo)))";
        
        new Parser().parse(omfg);
    }
    
    
    public void parse(String s) throws Exception
    {
        LinkedList<Token> tok = tokeniser(s);
        
        TokenTree t = new EmptyTokenTree();
        for(Token tt : tok)
        {
            t = t.add(tt, tt.sdepth);
            
        }
        
        t.lock();
        //t.print(0);
        System.out.println(t.eval(Context.getDefaultHashMap()));
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