package gr.aueb.gr.cs.nlp.bioasq.SnippetFinder.dataProcessing;
import java.io.*;
import java.util.*;

import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.*;

public class test {

  public static void main(String[] args){

  // option #2: By token

   PTBTokenizer ptbt = new PTBTokenizer(new StringReader("hello txt"),
          new CoreLabelTokenFactory(), "");
  for (CoreLabel label; ptbt.hasNext(); ) {
    label = (CoreLabel) ptbt.next();
    System.out.println(label.toString());
  }
    
  }

  

}