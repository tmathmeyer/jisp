#JISP
###~a language in the lisp family for masochists~

##Language constructs
###There are six 'types' in jisp.
####Numbers
Any real number. In theory, negative numbers are supported, however there is no negation as of yet,
so creating a number would have to be done through overflow... (this will change eventually)

####Lists
lists are a pair of any ` -> object` and a ` -> list`

####Booleans
true and false

####Strings
string literals

####Blobs
(a b c d ...) used only in lambdas (for now)

####Functions
this is where jisp differs from most languages. absolutly everything in jisp is a function:

  * `#t` is a function that returns true
  * `#f` is a function that returns false
  * `∅` is a function that returns a list (which happens to be empty)
  * `1` is a function that returns the number 1 (and so on for all digits 0-9)
  * `+` is a function that takes any number of ` -> number` and returns a number which is the sum of all the results of it's parameters
  * `*` is a function identical to `+` except that it multiplies the results of it's parameters
  * `-` NOT YET IMPLEMENTED (but it will be unary negation, not subtraction)
  * `cons` is a function that takes a `A -> B` and a `C -> list` and returns a list
  * `empty` is a function that takes a `A -> list` and returns a boolean, representing whether the result of it's parameter is an empty list
  * `first` is a function that takes a `A -> list` and returns the first element of the list
  * `rest` is a function that takes a `A -> list` and returns a list representing the parameter sans it's first element. if it's parameter returns empty so does this function
  * <sup>1</sup>`let` is a function that takes a `A -> string`, a `B -> C`, and a `D -> E`, and then sets the result of the first function as a the name of the second function in the context, then returns E
  * <sup>1</sup> `defun`is a functin that takes a `A -> string`, a `B -> C`, and sets the result of the first function as the name of a function that returns the result of the second function.
  * `begin` is a function that takes any number of functions, evaluates them, and returns the result of the last one.
  * <sup>1</sup>`λ` (lambda) is a function that takes a blob and a function, and returns a function that adds the parameters to the context and then evaluates the function provided to lambda
  * `=` is a function that takes two functions, and tests if their results are the same.
  * `cond` is a function that takes any even number of functions: the odd numbered functions are `A -> boolean` and the even numbered ones are `B -> C`. if function x (where x is odd) evaluates to true, then function x+1 is evaluated and the result is returned from cond

###1: Context 
context is global; if you define it anywhere, it can be used anywhere later in the program, even outside of the scope of it's let or lambda.

defun also has the unique property that it is a hard link. if you use the program:

`(begin (defun M +) (M 2 3))`

it's pretty obvious that the result is 5. However if you evaluate

`(begin (defun M +) (defun M *) (+ 3 2))`

the result is 6. Since after the first defun M and + are THE SAME THING, redefining M also redefines +. magical



###Notes
`A -> B` should be read as "function from A to B"


###Examples
####Recursion
`(begin (defun range (λ (start end) (cond (= start end) (∅) (#t) (let next (+ 1 start) (cons next (range next end)))))) (let 100 (* 5 5 4) (range 0 100)))`

