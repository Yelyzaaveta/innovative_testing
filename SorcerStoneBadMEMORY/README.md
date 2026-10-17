Orynchuk Yelyzaveta

Main.java refactoring Memory Optimization

RESULTS:

Memory used by the bad version: ~102 MB allocated on the heap, ~157 MB max RSS
Execution time of the bad version: ~4.5-8.5 s (median 4.8 s)

Memory used by the memory optimized version: ~8.1 MB allocated on the heap, ~57 MB max RSS
Execution time of the memory optimized version: ~5.0-5.6 s (median 5.1 s)

MEMORY OPTIMIZATION PROPOSALS: BAD -> MEMORY OPTIMIZED

1. Read the book piece by piece instead of loading the whole text into memory at once.

2. Clean the text and split it into words in a single pass while reading. Before, every step (remove extra characters, make lowercase, split into words) created another full copy of the text.

3. Do not create the array for unique words with the size of ALL words. There are far fewer unique words than words, so start with a small array and make it bigger only when needed.

4. Do not glue the word and the number into one string ("the 3630") and then pull the number back out of the string. Keep the number as a number, so thousands of extra temporary strings are not created.

5. For the top 30, keep only the 30 best words instead of sorting all of them. The "word number" text is built only for these 30.
