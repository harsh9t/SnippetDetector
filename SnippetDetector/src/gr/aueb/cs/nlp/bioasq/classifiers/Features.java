/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.cs.nlp.bioasq.classifiers;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.util.CoreMap;
import gr.aueb.cs.nlp.similarity.string.EditDistance;
import gr.aueb.cs.nlp.similarity.string.JaccardCoefficient;
import gr.aueb.gr.cs.nlp.bioasq.SnippetFinder.dataProcessing.SnippetInstance;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeMap;

/**
 *
 * @author mgeorgio
 */
public class Features {

    static ArrayList<String> stopWords = new ArrayList<String>();
    static HashMap<String, ArrayList<Double>> deepVectors = new HashMap<String, ArrayList<Double>>();
    public static HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> data = new HashMap<String, HashMap<ArrayList<String>, SnippetInstance>>();

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
        "you", "your", "yours", "yourself", "yourselves", "terms", "CONDITIONS", "conditions", "values", "interested.", "care", "sure", "contact", "grounds", "buyers", "tried", "said,", "plan", "value", "principle.", "forces", "sent:", "is,", "was", "like",
        "discussion", "tmus", "diffrent.", "layout", "area", "thanks", "thankyou", "hello", "bye", "rise", "fell", "fall", "psqft.", "?", "*", "-", "!", "@", "#", "$"};

    public static void initStopWords() throws FileNotFoundException, IOException, ClassNotFoundException {
        for (String s : stopwords) {
            stopWords.add(s);
        }
    }

    public static Double editDistance(String question, String sentence) {
        return new Double(EditDistance.editDistance(question, sentence));
    }

    /* public static Double jaccardCoefficientNoSyns(String query, String sentence) {

     Double sim = JaccardCoefficient.getSimilarity(query, sentence, false, false);
     return sim;
     }

     public static Double jaccardCoefficient(String query, String sentence) {

     Double sim = JaccardCoefficient.getSimilarity(query, sentence, true, false);
     return sim;
     }*/
    public static Double norm(ArrayList<Double> q) {
        Double norm = 0.0;
        if(q.size()<1){
            return 1.0;
        }
        else{
        for (Double d : q) {
            norm += Math.pow(d, 2);
        }
        return norm;}

    }
    public static ArrayList<String> tokenize(String sentence){
    ArrayList<String> tokens=new ArrayList<String>();
    PTBTokenizer ptbt = new PTBTokenizer(new StringReader(sentence),
          new CoreLabelTokenFactory(), "");
  for (CoreLabel label; ptbt.hasNext(); ) {
    label = (CoreLabel) ptbt.next();
    tokens.add(label.toString());
  }
  return tokens;
}
    
    public static Double tf(ArrayList<String> text, String lemma){
        Double tf=0.0;
        for(String sentence:text){
           ArrayList<String> tokens=tokenize(sentence);
           for(String token:tokens){
               if(token.equalsIgnoreCase(lemma)){
                   tf++;
               }
           }
        }
        return tf;
    }

    public static Double cosine(String question, String sentence,ArrayList<String> dictionary,TreeMap<String, Double> idf, ArrayList<String> text) {
        Double score = 0.0;
        ArrayList<String>  q=tokenize(question);
        ArrayList<String> s=tokenize(sentence);
        ArrayList<Double> qVector=new ArrayList<Double>();
        ArrayList<Double> sVector=new ArrayList<Double>();
        for(String st:s){
            if(idf.containsKey(st)){
                Double idsScore=Math.log(10876004 /idf.get(st));
                Double tf=tf(text, st);
                sVector.add(tf*idsScore);
            }
        }
        for(String st:q){
            if(idf.containsKey(st)){
                Double idsScore=Math.log(10876004 /idf.get(st));
                Double tf=tf(text, st);
                qVector.add(tf*idsScore);
            }
        }
        Double sum=0.0;
        if(qVector.size()<sVector.size()){
            for(int i=0;i<qVector.size();i++){
               sum+=qVector.get(i)*sVector.get(i);
            }
        }
        else if(qVector.size()==0 || sVector.size()==0){
        }
        else if(qVector.size()>sVector.size()){
            for(int i=0;i<sVector.size();i++){
               sum+=qVector.get(i)*sVector.get(i);
            }
        }
        score=sum/(norm(qVector)*norm(sVector));
        return score;
    }

    
    public static ArrayList<String> lemmatize(String documentText) {
        ArrayList<String> lemmas = new ArrayList<String>();
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
        String text = documentText;
        Annotation document = pipeline.process(text);
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                lemmas.add(lemma);
            }
        }
        return lemmas;
    }
    public static ArrayList<Double> deepVectorFeatures(ArrayList<Double> queryCentroid, ArrayList<Double> sentenceCentroid) {
        ArrayList<Double> scores = new ArrayList<Double>();
        return scores;

    }

    public static ArrayList<Double> returnFeatures(String query, String sentence, SnippetInstance sp) {
        ArrayList<Double> instance = new ArrayList<Double>();
        ArrayList<String> snippets = sp.getSnippetSentences();
        instance.add(editDistance(query, sentence));

        //instance.add(jaccardCoefficient( query,  sentence));
        for (String sn : snippets) {
            if (sn.contains(sentence) || sentence.contains(sn)) {
                instance.add(1.0);
            } else {
                instance.add(0.0);
            }
        }

        return instance;
    }

}
