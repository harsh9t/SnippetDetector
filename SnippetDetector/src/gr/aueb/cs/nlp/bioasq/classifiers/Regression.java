/*
 *Logistic regression gia to Subtask1 me 4 features
 */
package gr.aueb.cs.nlp.bioasq.classifiers;

import de.bwaldvogel.liblinear.*;
import gr.aueb.gr.cs.nlp.bioasq.SnippetFinder.Statistics.SnippetInstanceStats;
import gr.aueb.gr.cs.nlp.bioasq.SnippetFinder.dataProcessing.SnippetInstance;
import static gr.aueb.gr.cs.nlp.bioasq.SnippetFinder.dataProcessing.SnippetManipulator.TrainingData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Regression {

    public static HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> data = new HashMap<String, HashMap<ArrayList<String>, SnippetInstance>>();
    public static HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> training = new HashMap<String, HashMap<ArrayList<String>, SnippetInstance>>();
    public static HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> testing = new HashMap<String, HashMap<ArrayList<String>, SnippetInstance>>();
    public static HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> developement = new HashMap<String, HashMap<ArrayList<String>, SnippetInstance>>();
    static ArrayList<ArrayList<Double>> trainInstances = new ArrayList<>();
    static ArrayList<ArrayList<Double>> testInstances = new ArrayList<>();

    public static void loadDatFiles(String pathTrain, String pathDev, String pathTest) throws FileNotFoundException, IOException, ClassNotFoundException {
        System.out.println("Loading Dat files");
        FileInputStream fis = new FileInputStream(pathTrain);
        ObjectInputStream ois = new ObjectInputStream(fis);
        training = (HashMap<String, HashMap<ArrayList<String>, SnippetInstance>>) ois.readObject();
        ois.close();
        fis.close();
        System.out.println("Training loaded");
        fis = new FileInputStream(pathDev);
        ois = new ObjectInputStream(fis);
        developement = (HashMap<String, HashMap<ArrayList<String>, SnippetInstance>>) ois.readObject();
        ois.close();
        fis.close();
        System.out.println("Developement loaded");
        fis = new FileInputStream(pathTest);
        ois = new ObjectInputStream(fis);
        testing = (HashMap<String, HashMap<ArrayList<String>, SnippetInstance>>) ois.readObject();
        ois.close();
        fis.close();
        System.out.println("Test loaded");
    }

    public static ArrayList<ArrayList<Double>> Instances(HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> snippets) throws IOException, FileNotFoundException, ClassNotFoundException {
        ArrayList<ArrayList<Double>> instances = new ArrayList<ArrayList<Double>>();
        for (String query : snippets.keySet()) {
            HashMap<ArrayList<String>, SnippetInstance> tempSnips = snippets.get(query);
            for (ArrayList<String> document : tempSnips.keySet()) {
                for (String s : document) {
                    ArrayList<Double> features = Features.returnFeatures(query, s, tempSnips.get(document));
                    instances.add(features);
                }
            }
        }
        return instances;
    }

    public static FeatureNode[][] createNodes(ArrayList<ArrayList<Double>> instances, int numOfFeatures) {
        FeatureNode[][] DataSet = new FeatureNode[instances.size()][11];
        for (int k = 0; k < instances.size(); k++) {
            ArrayList<Double> instance = instances.get(k);
            for (int i = 0; i < numOfFeatures - 1; i++) {
                DataSet[k][i] = new FeatureNode(i + 1, instance.get(i));//numOfSenses  
            }
        }
        return DataSet;
    }

    public static double[] targetValue(ArrayList<ArrayList<Double>> instances, int numOfFeatures) {
        double[] tv = null;
        tv = new double[instances.size()];
        for (int i = 0; i < instances.size(); i++) {
            tv[i] = instances.get(i).get(numOfFeatures - 1);
        }
        return tv;
    }

    public static ArrayList<Feature[]> createNodesTest(ArrayList<ArrayList<Double>> instances, int features) {
        ArrayList<Feature[]> DataSet = new ArrayList<Feature[]>();
        for (int k = 0; k < instances.size(); k++) {
            ArrayList<Double> ar = instances.get(k);
            Feature[] temp = new Feature[features];
            for (int i = 0; i < features - 1; i++) {
                temp[i] = new FeatureNode(i + 1, ar.get(i));
            }
            DataSet.add(temp);
        }
        return DataSet;
    }

    public static double[] Testing(Model model, ArrayList<ArrayList<Double>> instances, int features) {
        ArrayList<Feature[]> testData = createNodesTest(instances, features);
        double[] testResults = new double[instances.size()];
        for (int i = 0; i < testData.size(); i++) {
            Feature[] instance = testData.get(i);
            double prediction = Linear.predict(model, instance);
            //System.out.println("Prediction is: "+prediction);
            testResults[i] = prediction;
        }
        return testResults;
    }

    public static double accuracy(double target[], double predicted[], int testSize) {
        double accuracy = 0.0;
        double sum = 0.0;
        for (int i = 0; i < predicted.length; i++) {
            if (target[i] == predicted[i]) {
                sum++;
            }
        }
        accuracy = sum / testSize;
        return accuracy;
    }

    public static double precision(double target[], double predicted[]) {
        double precision = 0.0;
        double tp = 0.0;
        double fn = 0.0;
        double tn = 0.0;
        double fp = 0.0;
        for (int i = 0; i < predicted.length; i++) {
            if (target[i] == 0.0 && predicted[i] == 0.0) {
                tn++;
            }
            if (target[i] == 1.0 && predicted[i] == 0.0) {
                fn++;
            }
        }
        if (tn == 0 && fn == 0) {
            precision = 0;
        } else {
            precision = tn / (tn + fn);
        }

        return precision;
    }

    public static double recall(double target[], double predicted[]) {
        double recall = 0.0;
        double tp = 0.0;
        double fn = 0.0;
        double tn = 0.0;
        double fp = 0.0;
        for (int i = 0; i < predicted.length; i++) {
            if (target[i] == 1.0 && predicted[i] == 1.0) {
                tp++;
            }
            if (target[i] == 0.0 && predicted[i] == 1.0) {
                fp++;
            }
            if (target[i] == 0.0 && predicted[i] == 0.0) {
                tn++;
            }

        }
        if (tn == 0 && fp == 0) {
            recall = 0;
        } else {
            recall = tn / (tn + fp);
        }
        return recall;
    }

    public static double Fmeasure(double recall, double precision) {
        if (recall == 0.0 || precision == 0) {
            return 0.0;
        } else {
            return 2 * ((recall * precision) / (recall + precision));
        }
    }

    public static double error(double target[], double predicted[], int testSize) {

        double sum = 0.0;
        for (int i = 0; i < predicted.length; i++) {
            if (target[i] != predicted[i]) {
                sum++;
            }
        }
        return sum / testSize;
    }

    public static void RegressionCurves(double c, ArrayList<ArrayList<Double>> train, ArrayList<ArrayList<Double>> test, int features) throws IOException {
        FeatureNode[][] trainingData = createNodes(train, features);
        System.out.println("Training DataNodes:" + trainingData.length);
        double[] targetValueTrain = targetValue(train, features);
        double[] targetValueTest = targetValue(test, features);
        int quantity = (int) Math.floor(new Double(targetValueTrain.length) / 10);
        int sum = quantity;
        for (int i = 0; i < 10; i++) {
            if (i == 9) {
                sum = targetValueTrain.length;
            }
            int tp = 0;
            int fp = 0;
            int tn = 0;
            int fn = 0;

            ArrayList<ArrayList<Double>> trainToTest = new ArrayList<ArrayList<Double>>();
            for (int k = 0; k < sum; k++) {
                ArrayList<Double> tempA = train.get(k);
                trainToTest.add(tempA);
            }
            FeatureNode[][] training = new FeatureNode[sum][11];
            double[] tvP = new double[sum];
            for (int j = 0; j < sum; j++) {
                for (int k = 0; k < 11; k++) {
                    training[j][k] = trainingData[j][k];
                }
                tvP[j] = targetValueTrain[j];
            }
            System.out.println("Training at %:" + training.length);
            System.out.println("Training values at %:" + tvP.length);
            Problem problem = new Problem();
            problem.l = tvP.length;
            problem.n = 11;
            problem.x = training;
            problem.y = tvP;
            SolverType solver = SolverType.L2R_LR; // -s 0  reguralized logistic regression
            double C = c;    // cost of constraints violation
            double eps = 0.001; // stopping criteria
            Parameter parameter = new Parameter(solver, C, eps);
            Model model = Linear.train(problem, parameter);
            File modelFile = new File("model.m");
            model.save(modelFile);
            model = Model.load(modelFile);

            double[] trainResults = Testing(model, trainToTest, features);
            double[] results = Testing(model, test, features);

            double recall = recall(tvP, trainResults);
            double precision = precision(tvP, trainResults);
            double accuracy1 = accuracy(tvP, trainResults, sum);
            double error = error(tvP, trainResults, sum);

            double recallTest = recall(targetValueTest, results);
            double precisionTest = precision(targetValueTest, results);
            double accuracy = accuracy(targetValueTest, results, test.size());
            double errorTest = error(targetValueTest, results, test.size());
            double fmeasure = Fmeasure(recall, precision);
            //double fmeasureTest = Fmeasure(recallTest, precisionTest);
            // System.out.println("Error " + error);
            // System.out.println("ErrorTest " + errorTest);
            //System.out.println("Fmeasure " + fmeasure);
            //System.out.println("FmeasureTest " + fmeasureTest);
            //System.out.println("Recall: " + recall1);
            // System.out.println("precision: " + precision1);
            // System.out.println("accuracy: " + accuracy1);

            // System.out.println("Recall TEST: " + recall);
            // System.out.println("precision TEST: " + precision);
            // System.out.println("accuracy TEST: " + accuracy);
            // System.out.println("Training Data: " + sum);
            //  System.out.println("Test  Data:" + test.size());
            for (int r = 0; r < results.length; r++) {
                if (targetValueTest[r] == 1.0 && results[r] == 1.0) {
                    tp++;
                }
                if (targetValueTest[r] == 0.0 && results[r] == 1.0) {
                    fp++;
                }
                if (targetValueTest[r] == 0.0 && results[r] == 0.0) {
                    tn++;
                }
                if (targetValueTest[r] == 1.0 && results[r] == 0.0) {
                    fn++;
                }

            }

           // System.out.println("C=" + c);
            // System.out.println("Confusion Matrix");
            // System.out.println(tp + " || " + fp);
            // System.out.println("------------");
            // System.out.println(tn + " || " + fn);
            sum = sum + quantity;

        }
        int pos = 0;
        int neg = 0;
        for (int y = 0; y < targetValueTrain.length; y++) {
            if (targetValueTrain[y] == 1.0) {
                pos++;
            } else {
                neg++;
            }

        }
       // System.out.println("In training positive:"+pos);
        // System.out.println("In training negative:"+neg);

    }

    /* public static void Regression(double c, ArrayList<ArrayList<Double>> train, ArrayList<ArrayList<Double>> test) throws IOException {
     int tp = 0;
     int fp = 0;
     int tn = 0;
     int fn = 0;
     FeatureNode[][] trainingData = createNodes(train);
     System.out.println(trainingData.length);
     double[] targetValueTrain = targetValue(train);
     double[] targetValueTest = targetValue(test);
     Problem problem = new Problem();
     problem.l = targetValueTrain.length;
     problem.n = 11;
     problem.x = trainingData;
     problem.y = targetValueTrain;
     SolverType solver = SolverType.L2R_LR; // -s 0  reguralized logistic regression
     double C = c;    // cost of constraints violation
     double eps = 0.001; // stopping criteria
     Parameter parameter = new Parameter(solver, C, eps);
     Model model = Linear.train(problem, parameter);

     File modelFile = new File("model.m");
     model.save(modelFile);
     model = Model.load(modelFile);

     double[] results = Testing(model, test);
     double accuracy = accuracy(targetValueTest, results, test.size());
     double recall = recall(targetValueTest, results);
     double precision = precision(targetValueTest, results);
     System.out.println("");
     System.out.println("Training Data:" + targetValueTrain.length);
     System.out.println("Test  Data:" + test.size());
     System.out.println("C=" + c);
     System.out.println("Precision: " + precision);
     System.out.println("Recall: " + recall + '\n');
     for (int r = 0; r < results.length; r++) {
     if (targetValueTest[r] == 1.0 && results[r] == 1.0) {
     tp++;
     }
     if (targetValueTest[r] == 0.0 && results[r] == 1.0) {
     fp++;
     }
     if (targetValueTest[r] == 0.0 && results[r] == 0.0) {
     tn++;
     }
     if (targetValueTest[r] == 1.0 && results[r] == 0.0) {
     fn++;
     }

     }

     System.out.println("C=" + c);
     System.out.println("Confusion Matrix");
     System.out.println(tp + " || " + fp);
     System.out.println("------------");
     System.out.println(tn + " || " + fn);

     }*/
    public static void main(String args[]) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("DatFiles/reducedTraining.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);
        training = (HashMap<String, HashMap<ArrayList<String>, SnippetInstance>>) ois.readObject();
        ois.close();
        fis.close();
        System.err.println("Training loaded");
        fis = new FileInputStream("DatFiles/DevelopmentData.dat");
        ois = new ObjectInputStream(fis);
        developement = (HashMap<String, HashMap<ArrayList<String>, SnippetInstance>>) ois.readObject();
        System.out.println("Training:" + developement.size());
        ois.close();
        fis.close();
        System.err.println("Instances loaded");
        
        /*for(String query:training.keySet()){
            System.out.println(query);
            HashMap<ArrayList<String>,SnippetInstance> temp=training.get(query);
            for(ArrayList<String> doc:temp.keySet()){
                System.out.println("Document "+doc.size());
                SnippetInstance sp=temp.get(doc);
                System.out.println(sp.getSnippetSentences());
            }
            
        }*/   
        
         ArrayList<ArrayList<Double>> instances= Instances(training);
        //for(ArrayList<Double> pleaseBeCorrect:instances){
          // System.out.println("Instance: "+pleaseBeCorrect);
      // }
         HashMap<String, HashMap<ArrayList<String>, SnippetInstance>> reduced= SnippetInstanceStats.negativeSentencesReduction(training); 
          ArrayList<ArrayList<Double>> instances2= Instances(reduced);
          FileOutputStream fos = new FileOutputStream("DatFiles/reducedTraining.dat");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(reduced);
        oos.close();
        for(ArrayList<Double> pleaseBeCorrect:instances2){
           System.out.println("Instance: "+pleaseBeCorrect);
       }
    }

}
