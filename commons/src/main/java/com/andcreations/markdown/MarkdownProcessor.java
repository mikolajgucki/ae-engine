package com.andcreations.markdown;

import java.io.IOException;

import org.markdown4j.Markdown4jProcessor;

/**
 * @author Mikolaj Gucki
 */
public class MarkdownProcessor {
    /** */
    public static String process(String input) throws IOException {
        Markdown4jProcessor markdown4jProcessor = new Markdown4jProcessor();
        return markdown4jProcessor.process(input);
    }
}