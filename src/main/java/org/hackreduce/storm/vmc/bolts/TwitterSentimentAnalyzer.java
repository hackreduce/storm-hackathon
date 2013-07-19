package org.hackreduce.storm.vmc.bolts;

import com.github.pmerienne.trident.ml.nlp.TwitterSentimentClassifier;

/**
 * Created with IntelliJ IDEA.
 * User: Richard Gu
 * Date: 7/19/13
 * Time: 3:56 PM
 */


public class TwitterSentimentAnalyzer extends TwitterSentimentClassifier {

    public TwitterSentimentAnalyzer() {
        super();
    }

    public Boolean classify(String text) {
        return super.classify(text);
    }

}
