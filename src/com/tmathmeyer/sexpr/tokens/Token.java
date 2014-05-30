package com.tmathmeyer.sexpr.tokens;

public class Token
{
    public int sdepth;
    public String tok;
    
    public Token(int s, String t)
    {
        tok = t.trim();
        if (tok.length() > 0)
        {
            char c = tok.charAt(0);
            if ((c<='9' && c>='0') || c=='"' || c=='\'')
            {
                sdepth = s+1;
            }
            else
            {
                sdepth = s;
            }
        }
    }
    
    public boolean isEmpty()
    {
        return tok.length() == 0;
    }
}
