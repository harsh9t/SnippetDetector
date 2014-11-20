package gr.aueb.gr.cs.nlp.bioasq.SnippetFinder.dataProcessing;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;
import gr.aueb.cs.nlp.bioasq.tools.JsonManipulator;
import gr.aueb.cs.nlp.bioasq.tools.Question;
import gr.aueb.cs.nlp.bioasq.tools.Snippet;
import gr.aueb.gr.cs.nlp.bioasq.SnippetFinder.Statistics.SnippetInstanceStats;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class SnippetManipulator {

    public static HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> TrainingData = new HashMap<String, HashMap<ArrayList<String>, SnippetInstance>>();
    public static HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> DevelopmentData = new HashMap<String, HashMap<ArrayList<String>, SnippetInstance>>();
    public static HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> TestData = new HashMap<String, HashMap<ArrayList<String>, SnippetInstance>>();
//contains questions-<document,snippets>

    public static void createData(String FileDir, String FileDirDocs) throws FileNotFoundException, IOException {
        JsonManipulator jsonManipulator = new JsonManipulator();
        File folder = new File(FileDir);
        File[] listOfFiles = folder.listFiles();
        int q = 0;
        for (File file : listOfFiles) {
            ArrayList<Question> questions = new ArrayList<Question>();
            questions = jsonManipulator.parseJsonForBaseline(FileDir + "/" + file.getName());
            if (!file.isFile()) {
                throw new FileNotFoundException(file.getName());
            }
            for (Question question : questions) {
                String qId = question.getHashId();
                String fileTempName = file.getName().substring(0, file.getName().length() - 4);
                File folderDocs = new File(FileDirDocs + "/" + fileTempName + "Docs/" + qId);
                File[] listOfDocs = folderDocs.listFiles();
                HashMap<ArrayList<String>, SnippetInstance> temp = new HashMap<ArrayList<String>, SnippetInstance>();

                for (File DocFile : listOfDocs) {
                    q++;
                    ArrayList<String> sentences = splitDOCToSenteces(FileDirDocs + "/" + fileTempName + "Docs/" + qId + "/" + DocFile.getName());
                    SnippetInstance sn = new SnippetInstance();
                    ArrayList<Snippet> snippets = question.getSnippets();
                    ArrayList<Snippet> docSnippets = new ArrayList<Snippet>();

                    for (Snippet sni : snippets) {
                        String doc = sni.getDocument();
                        String[] tokensDoc = doc.split("/");
                        String d = tokensDoc[tokensDoc.length - 1];
                        if (d.equals(DocFile.getName())) {
                            docSnippets.add(sni);
                        }
                    }

                    sn.setQid(question.getHashId());
                    sn.setQuery(question.getBody());
                    sn.setPreSnippets(docSnippets);
                    sn.setDocID(DocFile.getName());
                    sn.fillSnippets(sentences);
                    temp.put(sentences, sn);
                }
                if (file.getName().contains("task1bPhaseB-testset1") || file.getName().contains("task1bPhaseB-testset2") || file.getName().contains("task1bPhaseB-testset3")) {
                    TrainingData.put(question.getBody(), temp);
                }
                if (file.getName().contains("task2bPhaseB-testset1") || file.getName().contains("task2bPhaseB-testset2") || file.getName().contains(("task2bPhaseB-testset3"))) {
                    DevelopmentData.put(question.getBody(), temp);
                } else {
                    TestData.put(question.getBody(), temp);
                }
            }
        }
        FileOutputStream fos = new FileOutputStream("DatFiles/TrainingData.dat");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(TrainingData);
        oos.close();

        fos = new FileOutputStream("DatFiles/TestData.dat");
        oos = new ObjectOutputStream(fos);
        oos.writeObject(TestData);
        oos.close();

        fos = new FileOutputStream("DatFiles/DevelopmentData.dat");
        oos = new ObjectOutputStream(fos);
        oos.writeObject(DevelopmentData);
        oos.close();
        System.out.println("DatFile Created");
    }

    public static ArrayList<String> splitDOCToSenteces(String dir) {
        ArrayList<String> sentences = new ArrayList<String>();
        BufferedReader reader = null;
        File file = new File(dir);
        try {
            reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String line = reader.readLine();
            while (line != null) {
                if (line.startsWith("<title>")) {
                    line = reader.readLine();
                    line=line.replace("-LRB-","");
                    line=line.replace("-LSB-","");
                    line=line.replace("-RRB-","");
                    sentences.add(line);
                    line = reader.readLine();
                } else if (line.startsWith("<abstract>")) {
                    line = reader.readLine();
                    line=line.replace("-LRB-","");
                    line=line.replace("-LSB-","");
                    line=line.replace("-RRB-","");
                    ArrayList<String> temp = sentenceSplitter(line);
                    sentences.addAll(temp);
                    line = reader.readLine();
                } else if (line.startsWith("<sections.")) {
                    line = reader.readLine();
                    line = reader.readLine();
                    line=line.replace("-LRB-","");
                    line=line.replace("-LSB-","");
                    line=line.replace("-RRB-","");
                    ArrayList<String> temp = sentenceSplitter(line);
                    sentences.addAll(temp);
                    line = reader.readLine();
                } else {
                    line = reader.readLine();
                }
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
        return sentences;
    }

    public static void findDepedency(String text) {

    }

    public static void printSentences(String dir) {
        BufferedReader reader = null;
        File file = new File(dir);
        try {
            reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
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
    
    public static void printSetStats(HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> data){
        int q=0;
        int sentences=0;
        int docs=0;
        int snippets=0;
        for(String question:data.keySet()){
            q++;
            HashMap<ArrayList<String>,SnippetInstance> temp=data.get(question);
            for(ArrayList<String> doc:temp.keySet()){
                docs++;
                SnippetInstance snipIn=temp.get(doc);
                for(String s:doc){
                    sentences++;   
                }
                snippets=snippets+snipIn.getSnippetSentences().size();
            }
        }
        System.out.println("Questions "+q);
        System.out.println("Documents "+docs);
        System.out.println("Sentences "+sentences);
        System.out.println("Snippets "+snippets);
    }

    public static ArrayList<String> sentenceSplitter(String paragraph) {
        ArrayList<String> sentences = new ArrayList<String>();
        Reader reader = new StringReader(paragraph);
        DocumentPreprocessor dp = new DocumentPreprocessor(reader);
        Iterator<List<HasWord>> it = dp.iterator();
        while (it.hasNext()) {
            StringBuilder sentenceSb = new StringBuilder();
            List<HasWord> sentence = it.next();
            for (HasWord token : sentence) {
                if (sentenceSb.length() > 1) {
                    sentenceSb.append(" ");
                }
                sentenceSb.append(token);
            }
            sentences.add(sentenceSb.toString());
        }
        return sentences;
    }

    public static void main(String args[]) throws FileNotFoundException, IOException {
        createData("Data/Queries", "Data/Docs");
        printSetStats(TrainingData);
        printSetStats(DevelopmentData);
        printSetStats(TestData);
       // SnippetInstanceStats.negativeSentencesReductionStats(TrainingData);
        /* System.out.println("Data are created!");
         System.out.println("DATA STATISTICS: ");
         for (String q : data.keySet()) {
         System.out.println(q);
         HashMap<ArrayList<String>, SnippetInstance> textSnippet = data.get(q);
         System.out.println("Total number of documents:" + textSnippet.keySet().size());
         for (ArrayList<String> t : textSnippet.keySet()) {
         System.out.println("#Sentences: " + t.size());
         System.out.println("#Snippets:" + textSnippet.get(t).getSnippetSentences().size());
         System.out.println("# Pre Snippets:" + textSnippet.get(t).getPreSnippets().size());
         System.out.println("=============TEXT====================");
         System.out.println(textSnippet.get(t).getQid());
         for (String s : t) {
         System.out.println(s);
         }
         System.out.println("---------------SNIPPETS-------------");
         for (Snippet s : textSnippet.get(t).getPreSnippets()) {
         System.out.println(s.getText());
         }
         System.out.println("---------------SNIPPETS in sentences-------------");
         for (String s : textSnippet.get(t).getSnippetSentences()) {
         System.out.println(s);
         }
         System.out.println("===========END===========================");
         System.out.println("###########################");
         }
         }
         System.out.println("%%%%%%%%%%%%%%%%%%%%%%OTHER STATISTICS%%%%%%%%%%%%%%%%%%%%%%%");
         System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");*/
        // SnippetInstanceStats.negativeSentencesReduction(data);
    }
}
