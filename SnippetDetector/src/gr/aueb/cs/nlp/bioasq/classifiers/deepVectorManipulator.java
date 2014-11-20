/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.cs.nlp.bioasq.classifiers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author mary
 */
public class deepVectorManipulator {

    static HashMap<String, ArrayList<Double>> deepVectors=new HashMap<String, ArrayList<Double>>();
    
    

    public static void Parser(String dir, String dirVecs) {

        BufferedReader reader = null;
        BufferedReader readerVecs = null;
        File file = new File(dir);
        File fileVecs = new File(dirVecs);
        try {
            reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String line = reader.readLine();
            readerVecs = new BufferedReader(new FileReader(fileVecs.getAbsoluteFile()));
            String lineVecs = readerVecs.readLine();

            while (line != null) {
                ArrayList<Double> deepVec=new ArrayList<Double>();
                String[] vector=lineVecs.split(" ");
                for(String s:vector){
                    s=s.trim();
                    System.out.println(s);
                    deepVec.add(Double.parseDouble(s));
                }
                deepVectors.put(line, deepVec);
                line=reader.readLine();
                lineVecs=readerVecs.readLine();
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
    
    public static ArrayList<Double> findCentroid(String sentence){
        ArrayList<Double> centroid=new ArrayList<Double>();
        String[] sentence_tokens = sentence.split(" ");
        int tokens=1;
        for (String s : sentence_tokens) {
                if (deepVectors.containsKey(s)) {
                    tokens++;
                    ArrayList<Double> vec=deepVectors.get(s);
                    for(int i=0;i<200;i++){
                        centroid.set(i, centroid.get(i)+vec.get(i));
                    }
                }
        }
        for(int j=0;j<centroid.size();j++){
            centroid.set(j, centroid.get(j)/tokens);
        }
        return centroid;
    }
    
    
    public static void main(String args[]) throws FileNotFoundException, IOException{
        Parser("Data/DeepVectors/types.txt", "Data/DeepVectors/vectors.txt");
        FileOutputStream fos = new FileOutputStream("DatFiles/deepVecs.dat");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(deepVectors);
        oos.close();
    }

}
