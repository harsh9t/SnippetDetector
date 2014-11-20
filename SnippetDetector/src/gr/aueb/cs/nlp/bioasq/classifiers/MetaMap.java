/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.cs.nlp.bioasq.classifiers;

import gov.nih.nlm.nls.metamap.AcronymsAbbrevs;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.Result;
import java.util.List;

/**
 *
 * @author mary
 */
public class MetaMap {

    public static void main(String args[]) throws Exception {
        MetaMapApi api = new MetaMapApiImpl();
        api.setOptions("-y");
        List<Result> resultList = api.processCitationsFromString("He had a hearattack");

        Result result = resultList.get(0);
        List<AcronymsAbbrevs> aaList = result.getAcronymsAbbrevs();
        if (aaList.size() > 0) {
            System.out.println("Acronyms and Abbreviations:");
            for (AcronymsAbbrevs e : aaList) {
                System.out.println("Acronym: " + e.getAcronym());
                System.out.println("Expansion: " + e.getExpansion());
                System.out.println("Count list: " + e.getCountList());
                System.out.println("CUI list: " + e.getCUIList());
            }
        } else {
            System.out.println(" None.");
        }

    }

}
