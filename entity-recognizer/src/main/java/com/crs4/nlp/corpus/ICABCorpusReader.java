package com.crs4.nlp.corpus;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Splitter;

import eu.danieldk.nlp.jitar.corpus.CorpusReader;
import eu.danieldk.nlp.jitar.corpus.TaggedToken;

public class ICABCorpusReader implements CorpusReader {
    private static final Splitter SPACE_SPLITTER = Splitter.on(' ').trimResults().omitEmptyStrings();

    private final BufferedReader reader;

    boolean decapitalizeFirstWord;

    /**
     * Construct a CONLL corpus reader, use default start/end markers.
     *
     * @param reader                Reader over the corpus.
     * @param decapitalizeFirstWord If true, the first word of each sentence is decapitalized.
     */
    public ICABCorpusReader(BufferedReader reader, boolean decapitalizeFirstWord) {
        this.reader = reader;
        this.decapitalizeFirstWord = decapitalizeFirstWord;
    }

    private static String replaceCharAt(String str, int pos, char c) {
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, pos));
        sb.append(c);
        sb.append(str.substring(pos + 1));
        return sb.toString();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public List<TaggedToken> readSentence() throws IOException {
        List<TaggedToken> sentence = new ArrayList<>();

        String line;
       
        while ((line = reader.readLine()) != null) {
        	 if(line.startsWith("#")||line.startsWith("<")) return sentence;
            List<String> parts = SPACE_SPLITTER.splitToList(line);

            // We are done with this sentence.
            if (parts.size() == 0) {
                return sentence;
            }

            if (parts.size() < 4)
                throw new IOException(String.format("Line has fewer than four columns: %s", line));

            String word = parts.get(0);
            if (decapitalizeFirstWord && sentence.isEmpty())
                word = replaceCharAt(word, 0, Character.toLowerCase(word.charAt(0)));
            if(parts.get(3).equals("O"))
               sentence.add(new TaggedToken(word, parts.get(1)));
            else
            	   sentence.add(new TaggedToken(word, parts.get(3)));
        }

        // If the the file does not end with a blank line, we have left-overs.
        if (!sentence.isEmpty()) {
            return sentence;
        }

        return null;
    }
}
