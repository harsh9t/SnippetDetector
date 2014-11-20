/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.cs.nlp.bioasq.classifiers;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.util.CoreMap;
import static gr.aueb.cs.nlp.bioasq.classifiers.deepVectorManipulator.deepVectors;
import gr.aueb.gr.cs.nlp.bioasq.SnippetFinder.dataProcessing.SnippetInstance;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

/**
 *
 * @author mary
 */
public class Baseline {

    public static TreeMap<String, Double> idf = new TreeMap<String, Double>();
    public static HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> Data = new HashMap<String, HashMap<ArrayList<String>, SnippetInstance>>();

    public static void initIdf() throws FileNotFoundException, IOException, ClassNotFoundException {

        FileInputStream fis = new FileInputStream("DatFiles/idfNEW.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);
        idf = (TreeMap<String, Double>) ois.readObject();
        ois.close();
        fis.close();
    }

    public static void buildIds(String dir) throws FileNotFoundException, IOException {
        BufferedReader reader = null;
        File file = new File(dir);
        try {
            reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String line = reader.readLine();

            while (line != null) {
                String[] tokens = line.split(" ");
                idf.put(tokens[0], Double.parseDouble(tokens[1]));
                System.out.println(tokens[0] + "::" + tokens[1]);
                line = reader.readLine();
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
            }
            FileOutputStream fos = new FileOutputStream("DatFiles/idfNEW.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(idf);
            oos.close();

        }
    }

    public static ArrayList<String> tokenize(String sentence) {
        ArrayList<String> tokens = new ArrayList<String>();
        PTBTokenizer ptbt = new PTBTokenizer(new StringReader(sentence),
                new CoreLabelTokenFactory(), "");
        for (CoreLabel label; ptbt.hasNext();) {
            label = (CoreLabel) ptbt.next();
            tokens.add(label.toString());
        }
        return tokens;
    }

    public static ArrayList<String> buildDictionary(HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> data) {

        ArrayList<String> dictionary = new ArrayList<String>();
        for (String query : data.keySet()) {
            HashMap<ArrayList<String>, SnippetInstance> temp = data.get(query);
            for (ArrayList<String> document : temp.keySet()) {
                for (String sentence : document) {
                    ArrayList<String> sentenceTokens = tokenize(sentence);
                    for (String token : sentenceTokens) {
                        token = token.toLowerCase();
                        if (token == "?" || token == "&" || token == "#" || token == "!" || token == "$" || token == "'" || token == "%" || token == "*" || token == "," || token == "/" || token == "-" || token == ":" || token == "<" || token == ">" || token == "=" || token == "+" || token == "@" || token == "!" || dictionary.contains(token)) {
                            continue;
                        } else {
                            dictionary.add(token);
                        }
                    }
                }
            }
        }
        System.out.println("Dictionary size" + dictionary.size());
        return dictionary;
    }

    public static ArrayList<String> lemmatize(String documentText) {
        ArrayList<String> lemmas = new ArrayList<String>();
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
        String text = documentText;
        Annotation document = pipeline.process(text);
        for (CoreMap sentence : document.get(SentencesAnnotation.class)) {
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                String word = token.get(TextAnnotation.class);
                String lemma = token.get(LemmaAnnotation.class);
                lemmas.add(lemma);
            }
        }
        return lemmas;
    }

    public static HashMap<String, Double> idf(HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> data) {
        int documents = 0;
        ArrayList<String> dictionary = buildDictionary(data);
        System.out.println("Dictionary is done...");
        HashMap<String, Double> idf = new HashMap<String, Double>();
        System.out.println("Found " + dictionary.size() + " lemmas in texts...");
        for (int i = 17854; i < dictionary.size(); i++) {
            String lemma = dictionary.get(i);
            for (String query : data.keySet()) {
                HashMap<ArrayList<String>, SnippetInstance> tempDoc = data.get(query);
                for (ArrayList<String> document : tempDoc.keySet()) {
                    documents++;
                    for (String Sentence : document) {
                        ArrayList<String> temp = tokenize(Sentence);
                        if (temp.contains(lemma)) {
                            if (idf.keySet().contains(lemma)) {
                                Double freq = idf.get(lemma);
                                idf.remove(lemma);
                                idf.put(lemma, freq + 1.0);
                            } else {
                                //System.out.println(lemma);
                                idf.put(lemma, 1.0);
                            }
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("We have " + documents + " total documents..");
        return idf;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("DatFiles/TrainingData.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);
        HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> data = (HashMap<String, HashMap<ArrayList<String>, SnippetInstance>>) ois.readObject();
        ois.close();
        fis.close();
        System.err.println("Training loaded");
        /* HashMap<String, Double> idf = idf(data);
         FileOutputStream fos = new FileOutputStream("DatFiles/idf3.dat");
         ObjectOutputStream oos = new ObjectOutputStream(fos);
         oos.writeObject(idf);
         oos.close();*/

        fis = new FileInputStream("DatFiles/idfNEW.dat");
        ois = new ObjectInputStream(fis);
        TreeMap<String, Double> idf = (TreeMap<String, Double>) ois.readObject();
        ois.close();
        fis.close();
        System.err.println("idf loaded");
        ArrayList<String> dictionary = buildDictionary(data);
        int q = 0;
        int dPos = 0;
        int dneg = 0;
        int snippets = 0;
        int inSnip = 0;
        for (String question : data.keySet()) {
            q++;
            HashMap<ArrayList<String>, SnippetInstance> temp = data.get(question);
            for (ArrayList<String> doc : temp.keySet()) {
                SnippetInstance snipIn = temp.get(doc);
                for (int i=0;i<doc.size();i++) {
                    String s=doc.get(i);
                    Double cos = Features.cosine(question, s, dictionary, idf, doc);

                    if (snipIn.getSnippetSentences().contains(s.toLowerCase())) {
                        dPos++;
                        System.out.println("QUESTION: " + question);
                        System.out.println("SENTENCE:" + dPos + ":" + s);
                        System.out.println("SNIPPETS:" + snipIn.getSnippetSentences());
                        if(i!=0){
                        System.out.println(doc.get(i-1));
                        }
                        if(i!=doc.size()-1){
                        System.out.println(doc.get(i+1));
                        }
                        System.out.println(cos + ":1");
                        inSnip++;
                    } else {
                        dneg++;
                        System.out.println("QUESTION: " + question);
                        System.out.println("SENTENCE:" + dneg + ":" + s);
                        System.out.println("SNIPPETS:" + snipIn.getSnippetSentences());
                        System.out.println(cos + ":0");
                    }

                }
                snippets = snippets + snipIn.getSnippetSentences().size();
            }
        }
        System.out.println(q);
        //System.out.println(d);
        System.out.println("Snippets: " + snippets);
        System.out.println("Snippets found: " + snippets);
        //buildIds("Data/IDF.txt");
    }

}
