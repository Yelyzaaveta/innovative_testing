import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/*
  @author   yelyza
  @project   Default (Template) Project
  @class  ${NAME}
  @version  1.0.0
  @since 10/09/2026
*/

// path to the book
static final String BOOK_FILE_PATH = "src/txt/harry.txt";

// how many most frequent words to print at the end
static final int TOP_WORDS_COUNT = 30;

void main() throws IOException {

    LocalDateTime start = LocalDateTime.now();

    Map<String, int[]> wordFrequencies = countWords(BOOK_FILE_PATH);
    String[] topWords = findTopWords(wordFrequencies, TOP_WORDS_COUNT);

    printTopWords(topWords);

    LocalDateTime finish = LocalDateTime.now();
    System.out.println("Elapsed time (ms):");
    System.out.println(ChronoUnit.MILLIS.between(start, finish));
}

// Time optimization 1: count words with a HashMap (word -> count) instead of the nested loops
//                      "find unique words" + "count every unique word over all words" (O(n^2) -> O(n)).
// Time optimization 2: no sorting of all ~80 000 words, the HashMap does not need sorted input.
// Time optimization 3: words are counted right when they are read, so there is no array of all words
//                      to fill, copy and walk through again.
// Time optimization 4: the file is read in blocks of 8 KB instead of one byte per read() call.
// Time optimization 5: the map is created with enough room for the unique words, so it never has to rehash.
Map<String, int[]> countWords(String filePath) throws IOException {
    Map<String, int[]> frequencies = new HashMap<>(16384);
    StringBuilder word = new StringBuilder();

    try (InputStream in = Files.newInputStream(Paths.get(filePath))) {
        byte[] block = new byte[8192];
        int read;
        while ((read = in.read(block)) != -1) {
            for (int i = 0; i < read; i++) {
                int b = block[i];
                if (b >= 'A' && b <= 'Z') {
                    word.append((char) (b + ('a' - 'A')));
                } else if (b >= 'a' && b <= 'z') {
                    word.append((char) b);
                } else if (word.length() > 0) {
                    frequencies.computeIfAbsent(word.toString(), w -> new int[1])[0]++;
                    word.setLength(0);
                }
            }
        }
    }
    if (word.length() > 0) {
        frequencies.computeIfAbsent(word.toString(), w -> new int[1])[0]++;
    }

    return frequencies;
}

// Time optimization 6: the top N is kept in a small sorted array and a new word is inserted only if it beats
//                      the weakest one. No PriorityQueue, no comparators, no boxed Integers.
// Order: higher frequency first, equal frequency -> alphabetically later word first.
String[] findTopWords(Map<String, int[]> frequencies, int topCount) {
    String[] topWords = new String[topCount];
    int[] topCounts = new int[topCount];
    int size = 0;

    for (Map.Entry<String, int[]> entry : frequencies.entrySet()) {
        int count = entry.getValue()[0];
        String word = entry.getKey();

        if (size == topCount) {
            int last = size - 1;
            if (count < topCounts[last] || (count == topCounts[last] && word.compareTo(topWords[last]) < 0)) {
                continue;
            }
            size--;
        }

        int pos = size;
        while (pos > 0 && (topCounts[pos - 1] < count
                || (topCounts[pos - 1] == count && topWords[pos - 1].compareTo(word) < 0))) {
            topWords[pos] = topWords[pos - 1];
            topCounts[pos] = topCounts[pos - 1];
            pos--;
        }
        topWords[pos] = word;
        topCounts[pos] = count;
        size++;
    }

    String[] top = new String[size];
    for (int i = 0; i < size; i++) {
        top[i] = topWords[i] + " " + topCounts[i];
    }
    return top;
}

void printTopWords(String[] topWords) {
    for (String line : topWords) {
        System.out.println(line);
    }
}
