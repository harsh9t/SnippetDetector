/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.gr.cs.nlp.bioasq.SnippetFinder.Statistics;

import gr.aueb.gr.cs.nlp.bioasq.SnippetFinder.dataProcessing.SnippetInstance;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author mgeorgio
 */
public class SnippetInstanceStats {
    static ArrayList<String> stopWords=new ArrayList<String>();
     static String[] stopwords = {"a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost",
            "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "amoungst", "amount", "an", "and",
            "another", "any", "anyhow", "anyone", "anything", "anyway", "anywhere", "are", "around", "as", "at", "back", "be", "became",
            "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides",
            "between", "beyond", "bill", "both", "bottom", "but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt",
            "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven", "else",
            "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few",
            "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from",
            "front", "full", "further", "get", "give", "go", "had", "has", "hasnt",
            "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself",
            "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into",
            "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many",
            "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must",
            "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none",
            "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto",
            "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own", "part", "per", "perhaps",
            "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she",
            "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something",
            "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their",
            "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon",
            "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru",
            "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until",
            "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever",
            "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while",
            "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet",
            "you", "your", "yours", "yourself", "yourselves","terms", "CONDITIONS", "conditions", "values", "interested.", "care", "sure","contact", "grounds", "buyers", "tried", "said,", "plan", "value", "principle.", "forces", "sent:", "is,", "was", "like",
            "discussion", "tmus", "diffrent.", "layout", "area", "thanks", "thankyou", "hello", "bye", "rise", "fell", "fall", "psqft."};
    
     public static void initStopWords(){
         for(String s:stopwords){
            stopWords.add(s);
         }
     }
     
    public static Boolean hasCommonTokens(String query, String sentence) {
        initStopWords();
        int common = 0;
        Boolean hasCommon = false;
        String[] queryTokens = query.replaceAll("\\.", "").replaceAll("\\?", "").split(" ");
        String[] sentenceTokens = sentence.replaceAll("\\.", "").replaceAll("\\?", "").split(" ");
        for (String s : queryTokens) {
            s=s.toLowerCase();
            if(stopWords.contains(s)){continue;}
            for (String s2 : sentenceTokens) {
                s2=s2.toLowerCase();
                if(stopWords.contains(s2)){continue;}
                if (s.equalsIgnoreCase(s2)) {
                    common++;
                }
            }
        }
        if (common > 0) {
            hasCommon = true;
        }
        return hasCommon;
    }

    public static void negativeSentencesReductionStats(HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> data) {
        int overallSentences = 0;
        int overallSentencesSubstracted = 0;
        int overallSnippetsLost = 0;
        for (String question : data.keySet()) {
            int lostSnippets = 0;
            int total_sentences = 0;
            int sentences_substracted = 0;
            HashMap<ArrayList<String>, SnippetInstance> textSnippets = data.get(question);
            for (ArrayList<String> text : textSnippets.keySet()) {
                SnippetInstance tempSnippets = textSnippets.get(text);
                for (String sentence : text) {
                    total_sentences++;
                    overallSentences++;
                    if (!hasCommonTokens(question, sentence)) {
                        sentences_substracted++;
                        overallSentencesSubstracted++;
                            if(tempSnippets.getSnippetSentences().contains(sentence.toLowerCase())){
                            lostSnippets++;
                            overallSnippetsLost++;
                            }

                    }
                }
            }
            /*System.out.println("===================================");
            System.out.println("Question: " + question);
            System.out.println("total_sentences: " + total_sentences);
            System.out.println("sentences substracted: " + sentences_substracted);
            System.out.println("remaining sentences for instances: " + (total_sentences - sentences_substracted));
            System.out.println("Lost Snippets: " + lostSnippets);
            System.out.println("====================================");*/
        }
        System.out.println("===================================");
        System.out.println("===================================");
        System.out.println("overall sentences: " + overallSentences);
        System.out.println("overall sentences substracted: " + overallSentencesSubstracted);
        System.out.println("overall remaining sentences for instances: " + (overallSentences - overallSentencesSubstracted));
        System.out.println("overall lost Snippets: " + overallSnippetsLost);
        System.out.println("===================================");
        System.out.println("====================================");
    }

    
    
     public static HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> negativeSentencesReduction(HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> data) {
         HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> dataReduced=new HashMap<String, HashMap<ArrayList<String>, SnippetInstance>>();
        for (String question : data.keySet()) {
            System.out.println(question);
            HashMap<ArrayList<String>, SnippetInstance> temp=new HashMap<ArrayList<String>, SnippetInstance>();
            HashMap<ArrayList<String>, SnippetInstance> textSnippets = data.get(question);
            ArrayList<String> tempDoc=new ArrayList<String>();
            for (ArrayList<String> text : textSnippets.keySet()) {
                SnippetInstance tempSnippets = textSnippets.get(text);
                for (String sentence : text) {
                    if (hasCommonTokens(question, sentence)) {
                        tempDoc.add(sentence);
                    }
                }
                temp.put(tempDoc, tempSnippets);
            }
            dataReduced.put(question, temp);
        }
        return dataReduced;
    }
    
    public static void main(String args[]) {
  
    }

}
