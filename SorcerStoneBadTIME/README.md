Orynchuk Yelyzaveta

Main.java refactoring Time consuming optimization

RESULTS:

| Version | Time | Median | Allocated on heap | Max RSS |
|---|---|---|---|---|
| First (bad) version | ~4.5-8.5 s | 4.8 s | ~102 MB | ~157 MB |
| Bad version (previous) | ~5.0-5.6 s | 5.1 s | ~8.1 MB | ~57 MB |
| Memory optimized version | ~4.3-9.7 s | 5.3 s | ~8.1 MB | ~59 MB |
| Time optimized version | ~0.12-0.21 s | 0.15 s | ~6.6 MB | ~56 MB |


TIME OPTIMIZATION PROPOSALS: MEMORY OPTIMIZED -> TIME OPTIMIZED (and memory)

1. Count the words with a `HashMap` (word -> how many times) instead of two nested loops. Before, for every word the program looked through the whole list again, so the work grew with the square of the number of words.

2. Do not sort all the words. The `HashMap` does not need them sorted, so the slow sorting of ~80 000 words is removed.

3. Count each word right when it is read. There is no big array of all words to fill and walk through again.

4. Read the file in blocks of 8 KB instead of one byte at a time. Fewer calls to `read()` means less work.

5. Create the `HashMap` with enough room for all unique words at the start, so it does not have to grow and re-arrange its items while counting.

6. Find the top 30 with a small sorted array and insert a word only if it is better than the weakest one. It is faster than `PriorityQueue` with comparators and boxed numbers.
