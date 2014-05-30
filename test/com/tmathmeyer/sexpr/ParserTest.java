package com.tmathmeyer.sexpr;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.tmathmeyer.sexpr.data.DList;
import com.tmathmeyer.sexpr.data.Empty;

public class ParserTest
{
    
    Parser p;
    DList l1 = new DList(2, new Empty());
    
    @Before
    public void initialize()
    {
        p = new Parser();
    }
    
    @Test
    public void additionTest() throws Exception
    {
        String testcase = "(+ 1 1)";
        Object out = p.parse(testcase);
        assertEquals(out, 2);
    }
    
    @Test
    public void multiplicationTest() throws Exception
    {
        String testcase = "(* 3 4)";
        Object out = p.parse(testcase);
        assertEquals(out, 12);
    }
    
    @Test
    public void nestedTest() throws Exception
    {
        String testcase = "(+ (+ 1 2 3) 4)";
        Object out = p.parse(testcase);
        assertEquals(out, 10);
    }
    
    @Test
    public void reverseNestedTest() throws Exception
    {
        String testcase = "(+ 4 (+ 1 2 3))";
        Object out = p.parse(testcase);
        assertEquals(out, 10);
    }
    
    @Test
    public void layeredTest() throws Exception
    {
        String testcase = "(+ (+ (+ (+ (+ (+ (+ 1 2) 3) 4) 5) 6) 7) 8)";
        Object out = p.parse(testcase);
        assertEquals(out, 36);
    }
    
    @Test
    public void mixedMathTest() throws Exception
    {
        String testcase = "(* (+ 1 1) (+ 2 3))";
        Object out = p.parse(testcase);
        assertEquals(out, 10);
    }
    
    @Test
    public void listTest() throws Exception
    {
        String empty = "(empty)";
        String cons = "(cons 2 empty)";
        String lots = "(cons 1 (cons 2 (cons 3 (cons 4 empty))))";
        String ret = "(first (cons 2 empty))";
        Object emptyR = p.parse(empty);
        Object consR = p.parse(cons);
        Object lotsR = p.parse(lots);
        Object retR = p.parse(ret);
        
        assertEquals(emptyR.toString(), "[]");
        assertEquals(consR.toString(), "[2]");
        assertEquals(lotsR.toString(), "[1,2,3,4]");
        assertEquals(retR, 2);
    }

}
