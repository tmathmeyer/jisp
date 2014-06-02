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
    public void booleanTest() throws Exception
    {
        String truth = "(#t)";
        Object truthR = p.parse(truth);
        assertTrue(truthR.equals(true));
        
        String lie = "(#f)";
        Object lieR = p.parse(lie);
        assertTrue(lieR.equals(false));
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
        String empty = "(∅)";
        String cons = "(cons 2 ∅)";
        String lots = "(cons 1 (cons 2 (cons 3 (cons 4 ∅))))";
        String ret = "(first (cons 2 ∅))";
        Object emptyR = p.parse(empty);
        Object consR = p.parse(cons);
        Object lotsR = p.parse(lots);
        Object retR = p.parse(ret);
        
        assertEquals(emptyR.toString(), "[]");
        assertEquals(consR.toString(), "[2]");
        assertEquals(lotsR.toString(), "[1,2,3,4]");
        assertEquals(retR, 2);
    }
    
    @Test
    public void beginTest() throws Exception
    {
        String begin = "(begin (+ 1 2) (+ 2 3) (+ 3 4))";
        Object beginR = p.parse(begin);
        
        assertEquals(beginR, 7);
    }
    
    @Test
    public void defunTest() throws Exception
    {
        String def = "(begin (defun add +) (add 2 3) (add 3 4))";
        String redef = "(begin (defun M +) (defun M *) (* 1 2) (+ 3 4))";
        
        Object defR = p.parse(def);
        Object redefR = p.parse(redef);
        
        assertEquals(defR, 7);
        assertEquals(redefR, 12);
    }
    
    @Test
    public void lambdaTest() throws Exception
    {
        String lambda = "(begin (defun add (λ (x y) (+ x y))) (add 1 2))";
        Object lambdaR = p.parse(lambda);
        assertEquals(lambdaR, 3);
    }

    @Test
    public void letTest() throws Exception
    {
        String let = "(let ten (+ 5 5) (* ten ten))";
        Object letR = p.parse(let);
        assertEquals(letR, 100);
    }
    
    @Test
    public void emptyTest() throws Exception
    {
        String identity = "(empty ∅)";
        String nested = "(empty (rest (cons 4 ∅)))";
        String fail = "(empty 4)";
        
        Object identityR = p.parse(identity);
        Object nestedR = p.parse(nested);
        Object failR = p.parse(fail);
        
        assertEquals(identityR, true);
        assertEquals(nestedR, true);
        assertEquals(failR, false);
        
    }

    @Test
    public void recursionTest() throws Exception
    {
        String realrange = 
            "(begin (defun range (λ (start end) (cond (= start end) (∅) (#t) (let next (+ 1 start) (cons next (range next end)))))) (range 0 (* 2 5)))";

        Object rangeResult = p.parse(realrange);
        assertEquals(rangeResult.toString(), "[1,2,3,4,5,6,7,8,9,10]");
    }

}
