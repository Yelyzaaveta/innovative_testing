# Lab 2 - Good Bad Code

Orynchuk Yelyzaveta

Main.java refactoring (bad ver)

1. File starts with an empty line.

2. File has no `import` statements - doesn't compile.

3. `void main()` has no class and no method name other than `main` - everything is in one giant unnamed block.

4. File path `"src/txt/harry.txt"` is hardcoded directly in the middle of the logic instead of a named constant.

5. `int size = content.length();` unused 

6. `words.length` is not what the comment says - `// 400 000` is magic-number comment ???

7. `distinctString.contains(words[i])` checks if the word is a *substring* of the accumulated string, not if it equals one of the already-seen words (I make it because it doesnt workkk)

8. `// 5 000` magic-number comment

9. `distincts[i] += " " + freq[i];` glues a word and its count into a single strin

10. Magic number `30`

11. Variable names give no idea what they hold: `distincts`, `freq`, `str`, `words[j]`

12. No blank-line/indentation consistency !!!

13. `System.out.println("------");` not cool we can use gaps with explain
