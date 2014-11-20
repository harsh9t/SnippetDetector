/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.gr.cs.nlp.bioasq.SnippetFinder.dataProcessing;

import gr.aueb.cs.nlp.bioasq.tools.Snippet;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author mgeorgio
 */
public class SnippetInstance implements Serializable {

    private ArrayList<String> snippetSentences;
    private ArrayList<Snippet> preSnippets;
    private String docID;
    private String qid;
    private String query;

    public SnippetInstance() {
        snippetSentences = new ArrayList<String>();
        preSnippets = new ArrayList<Snippet>();
        qid = "";
        docID="";
    }

    public ArrayList<Snippet> getPreSnippets() {
        return preSnippets;
    }

    public void setPreSnippets(ArrayList<Snippet> preSnippets) {
        this.preSnippets = preSnippets;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ArrayList<String> getSnippetSentences() {
        return snippetSentences;
    }

    public void setSnippetSentences(ArrayList<String> snippetSentences) {
        this.snippetSentences = snippetSentences;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }
    
    

    //rounds the snippets to senteces
    public void fillSnippets(ArrayList<String> text) {
        for (Snippet s : preSnippets) {
            ArrayList<String> splittedSnippets = SnippetManipulator.sentenceSplitter(s.getText());
            for (String splitSnip : splittedSnippets) {
                for (String sDoc : text) {
                    sDoc = sDoc.toLowerCase();
                    if (sDoc.contains(splitSnip.toLowerCase())) {
                        this.snippetSentences.add(sDoc);
                    }
                }
            }
        }
    }

}
