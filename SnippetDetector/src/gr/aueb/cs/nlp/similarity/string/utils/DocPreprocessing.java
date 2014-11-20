/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.cs.nlp.similarity.string.utils;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import gr.aueb.cs.nlp.bioasq.tools.JsonManipulator;
import gr.aueb.cs.nlp.bioasq.tools.Question;
import gr.aueb.cs.nlp.bioasq.tools.Snippet;
import static gr.aueb.gr.cs.nlp.bioasq.SnippetFinder.dataProcessing.SnippetManipulator.sentenceSplitter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author mgeorgio
 */
public class DocPreprocessing {

    public static LinkedHashMap<String,Integer> findStopWords(String FileDir, String FileDirDocs) throws FileNotFoundException {
        HashMap<String, Integer> stopWords = new HashMap<String, Integer>();
        JsonManipulator jsonManipulator = new JsonManipulator();
        File folder = new File(FileDir);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            ArrayList<Question> questions = new ArrayList<Question>();
            questions = jsonManipulator.parseJsonForBaseline(FileDir + "\\" + file.getName());
            if (!file.isFile()) {
                throw new FileNotFoundException(file.getName());
            }

            for (Question question : questions) {
                ArrayList<String> qTokens = tokenizer(question.getBody());
                for (String s : qTokens) {
                    //System.out.println(s);
                    if (!stopWords.containsKey(s)) {
                        stopWords.put(s, 1);
                    } else {
                        int freq = stopWords.get(s);
                        stopWords.remove(s);
                        stopWords.put(s, freq + 1);
                    }
                }
                String qId = question.getHashId();
                String fileTempName = file.getName().substring(0, file.getName().length() - 4);
                File folderDocs = new File(FileDirDocs + "\\" + fileTempName + "Docs\\" + qId);
                File[] listOfDocs = folderDocs.listFiles();

                for (File DocFile : listOfDocs) {

                    BufferedReader reader = null;
                    File file2 = new File(FileDirDocs + "\\" + fileTempName + "Docs\\" + qId + "\\" + DocFile.getName());
                    try {
                        reader = new BufferedReader(new FileReader(file2.getAbsoluteFile()));
                        String line = reader.readLine();
                        while (line != null) {
                           // System.out.println(line);
                            line=line.replaceAll("<text>|</text>|<abstract>|</abstract>|</title>|<title>|<sections\\..?>|</sections\\..?>","");
                           // System.out.println("AFTER: "+line);
                            ArrayList<String> tokens=tokenizer(line);
                            for (String s : tokens) {
                              //  System.out.println(s);
                                if (!stopWords.containsKey(s)) {
                                    stopWords.put(s, 1);
                                } else {
                                    int freq = stopWords.get(s);
                                    stopWords.remove(s);
                                    stopWords.put(s, freq + 1);
                                }
                            }
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
                    }
                }
            }
        }
        LinkedHashMap sorted= sortHashMapByValuesD(stopWords);
        return sorted;
    }

    public static ArrayList<String> tokenizer(String text) {
        ArrayList<String> tokens = new ArrayList<String>();
        PTBTokenizer ptbt = new PTBTokenizer(new StringReader(text), new CoreLabelTokenFactory(), "");
        for (CoreLabel label; ptbt.hasNext();) {
            label = (CoreLabel) ptbt.next();
            tokens.add(label.word());
        }
        return tokens;
    }

    
    public static LinkedHashMap<String,Integer> sortHashMapByValuesD(HashMap passedMap) {
   List mapKeys = new ArrayList(passedMap.keySet());
   List mapValues = new ArrayList(passedMap.values());
   Collections.sort(mapValues);
   Collections.sort(mapKeys);

   LinkedHashMap sortedMap = new LinkedHashMap();

   Iterator valueIt = mapValues.iterator();
   while (valueIt.hasNext()) {
       Object val = valueIt.next();
       Iterator keyIt = mapKeys.iterator();

       while (keyIt.hasNext()) {
           Object key = keyIt.next();
           String comp1 = passedMap.get(key).toString();
           String comp2 = val.toString();

           if (comp1.equals(comp2)){
               passedMap.remove(key);
               mapKeys.remove(key);
               sortedMap.put((String)key, (Integer)val);
               break;
           }

       }

   }
   return sortedMap;
}
    
    public static void main(String args[]) throws FileNotFoundException {
        LinkedHashMap stopwords=findStopWords("\\\\cern.ch\\dfs\\Users\\m\\mgeorgio\\Documents\\NetBeansProjects\\SnippetDetector\\Data\\Queries", "\\\\cern.ch\\dfs\\Users\\m\\mgeorgio\\Documents\\NetBeansProjects\\SnippetDetector\\Data\\Docs");
        for(Object s:stopwords.keySet()){
            System.out.println((String)s+":"+(Integer)stopwords.get(s));
        }
    }

}
