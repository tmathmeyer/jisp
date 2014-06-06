package com.tmathmeyer.sexpr;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.tmathmeyer.sexpr.data.DList;
import com.tmathmeyer.sexpr.data.Empty;

public class ParserTest
{
    
    DList l1 = new DList(2, new Empty());
    
    
    @Test
    public void additionTest() throws Exception
    {
        Object out = new Parser("(+ 1 1)").parse();
        assertEquals(out, 2);
    }
    
    @Test
    public void booleanTest() throws Exception
    {
        String truth = "(#t)";
        Object truthR = new Parser(truth).parse();
        assertTrue(truthR.equals(true));
        
        String lie = "(#f)";
        Object lieR = new Parser(lie).parse();
        assertTrue(lieR.equals(false));
    }
    
    @Test
    public void multiplicationTest() throws Exception
    {
        String testcase = "(* 3 4)";
        Object out = new Parser(testcase).parse();
        assertEquals(out, 12);
    }
    
    @Test
    public void nestedTest() throws Exception
    {
        String testcase = "(+ (+ 1 2 3) 4)";
        Object out = new Parser(testcase).parse();
        assertEquals(out, 10);
    }
    
    @Test
    public void reverseNestedTest() throws Exception
    {
        String testcase = "(+ 4 (+ 1 2 3))";
        Object out = new Parser(testcase).parse();
        assertEquals(out, 10);
    }
    
    @Test
    public void layeredTest() throws Exception
    {
        String testcase = "(+ (+ (+ (+ (+ (+ (+ 1 2) 3) 4) 5) 6) 7) 8)";
        Object out = new Parser(testcase).parse();
        assertEquals(out, 36);
    }
    
    @Test
    public void mixedMathTest() throws Exception
    {
        String testcase = "(* (+ 1 1) (+ 2 3))";
        Object out = new Parser(testcase).parse();
        assertEquals(out, 10);
    }
    
    @Test
    public void listTest() throws Exception
    {
        String empty = "(∅)";
        String cons = "(cons 2 ∅)";
        String lots = "(cons 1 (cons 2 (cons 3 (cons 4 ∅))))";
        String ret = "(first (cons 2 ∅))";
        Object emptyR = new Parser(empty).parse();
        Object consR = new Parser(cons).parse();
        Object lotsR = new Parser(lots).parse();
        Object retR = new Parser(ret).parse();
        
        assertEquals(emptyR.toString(), "[]");
        assertEquals(consR.toString(), "[2]");
        assertEquals(lotsR.toString(), "[1,2,3,4]");
        assertEquals(retR, 2);
    }
    
    @Test
    public void beginTest() throws Exception
    {
        String begin = "(begin (+ 1 2) (+ 2 3) (+ 3 4))";
        Object beginR = new Parser(begin).parse();
        
        assertEquals(beginR, 7);
    }
    
    @Test
    public void defunTest() throws Exception
    {
        String def = "(begin (defun add +) (add 2 3) (add 3 4))";
        String redef = "(begin (defun M +) (defun M *) (* 1 2) (+ 3 4))";
        
        Object defR = new Parser(def).parse();
        Object redefR = new Parser(redef).parse();
        
        assertEquals(defR, 7);
        assertEquals(redefR, 12);
    }
    
    @Test
    public void lambdaTest() throws Exception
    {
        String lambda = "(begin (defun add (λ (x y) (+ x y))) (add 1 2))";
        Object lambdaR = new Parser(lambda).parse();
        assertEquals(lambdaR, 3);
    }

    @Test
    public void letTest() throws Exception
    {
        String let = "(let ten (+ 5 5) (* ten ten))";
        Object letR = new Parser(let).parse();
        assertEquals(letR, 100);
    }
    
    @Test
    public void emptyTest() throws Exception
    {
        String identity = "(empty ∅)";
        String nested = "(empty (rest (cons 4 ∅)))";
        String fail = "(empty 4)";
        
        Object identityR = new Parser(identity).parse();
        Object nestedR = new Parser(nested).parse();
        Object failR = new Parser(fail).parse();
        
        assertEquals(identityR, true);
        assertEquals(nestedR, true);
        assertEquals(failR, false);
        
    }

    @Test
    public void recursionTest() throws Exception
    {
        String realrange = 
            "(begin (defun range (λ (start end) (cond (= start end) (∅) (#t) (let next (+ 1 start) (cons next (range next end)))))) (range 0 (* 2 5)))";

        Object rangeResult = new Parser(realrange).parse();
        assertEquals(rangeResult.toString(), "[1,2,3,4,5,6,7,8,9,10]");
    }
    
    @Test
    public void classAct()
    {
    	String testcase = "(* 3 4)";
        Integer out = new Parser(testcase).parse(Integer.class);
        assertEquals(out, new Integer(12));
    }
    
    @Test
    public void classList()
    {
    	String testcase =
    			"(begin (defun range (λ (start end) (cond (= start end) (∅) (#t) (let next (+ 1 start) (cons next (range next end)))))) (range 0 (* 2 5)))";
        List<Integer> out = new Parser(testcase).parseAsList(Integer.class);
        int should = 1;
        for(Integer i : out)
        {
        	assertEquals(i, new Integer(should++));
        }
    }
    
    @Test
    public void sourceBuilder()
    {
    	String[] program = {"(begin",
                            "(defun range (λ (start end) (cond (= start end) (∅) (#t) (let next (+ 1 start) (cons next (range next end))))))",
                            "(defun incriment (λ (value) (+ 1 value)))",
                            "(defun square-L (λ (list) (cond (empty list) (∅)",
                                                       "(#t) (let fir (first list)",
                                                                 "(let res (rest list)",
                                                                      "(let incr (* fir fir)",
                                                                           "(cons incr (square-L res))))))))",
                            "(defun 100 (* 5 4 4))",
                            "(square-L (range 0 100)))"};
    	
    	List<String> prog = Arrays.asList(program);
    	Parser p = Parser.buildParser(prog);
    	Integer count = 1;
    	for (Integer i : p.parseAsList(Integer.class))
    	{
    		assertEquals(i, new Integer(count*count));
    		count++;
    	}
    	
    	
    }

}
