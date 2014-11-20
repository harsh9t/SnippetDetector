package gr.aueb.cs.nlp.similarity.string;

import gr.aueb.cs.nlp.similarity.string.utils.StringManipulation;

import java.util.ArrayList;
import java.util.HashSet;


public class DiceCoefficient
{
	public static double getSimilarity(String s1, String s2, boolean useSynonyms, boolean greek)
	{
		if((s1.length() == 0) || (s2.length() == 0))
		{
			return 0.0;
		}
		if(greek)
		{
			s1 = StringManipulation.normalizeGreek(s1);
			s2 = StringManipulation.normalizeGreek(s2);
		}
		s1 = s1.toUpperCase().replaceAll("\\s", " ");
		s2 = s2.toUpperCase().replaceAll("\\s", " ");
		
		ArrayList<String> s1Tokens = StringManipulation.getTokensList(s1, " ");
		ArrayList<String> s2Tokens = StringManipulation.getTokensList(s2, " ");
		
		HashSet<String> s1UniqueTokens = null;
		HashSet<String> s2UniqueTokens = null;
 		
		if(useSynonyms)
		{
			s1UniqueTokens = StringManipulation.getUniqueTokensAndSynonyms(s1Tokens);
			s2UniqueTokens = StringManipulation.getUniqueTokensAndSynonyms(s2Tokens);
		}
		else
		{
			s1UniqueTokens = new HashSet<String>(s1Tokens);
			s2UniqueTokens = new HashSet<String>(s2Tokens);
		}
 		
		HashSet<String> allTokens = new HashSet<String>();
		allTokens.addAll(s1UniqueTokens);
		if(useSynonyms)
		{
			for (String word : s2UniqueTokens)
			{
				//if(!StringManipulation.containsSynonym(word, s1Tokens))
				if(!StringManipulation.containsSynonym(word, allTokens))
				{
					allTokens.add(word);
				}
			}
		}
		else
		{
			allTokens.addAll(s2Tokens);
		}
		
		int numOfS1Tokens = s1UniqueTokens.size();
		int numOfS2Tokens = s2UniqueTokens.size();
		int numOfCommonTokens = (numOfS1Tokens + numOfS2Tokens) - allTokens.size();
		
		double similarity = (double)(2 * numOfCommonTokens) / ((double)numOfS1Tokens + (double)numOfS2Tokens);
		return similarity;
	}
	
	public static double getNGramSimilarity(String s1, String s2, int n, boolean greek)
	{
		if(greek)
		{
			s1 = StringManipulation.normalizeGreek(s1);
			s2 = StringManipulation.normalizeGreek(s2);
		}
		s1 = s1.toUpperCase().replaceAll("\\s", " ");
		s2 = s2.toUpperCase().replaceAll("\\s", " ");
		
		ArrayList<String> s1Tokens = StringManipulation.getNGrams(s1, n);
		ArrayList<String> s2Tokens = StringManipulation.getNGrams(s2, n);
		
		HashSet<String> s1UniqueTokens = new HashSet<String>(s1Tokens);
		HashSet<String> s2UniqueTokens = new HashSet<String>(s2Tokens);
 		
		HashSet<String> allTokens = new HashSet<String>();
		allTokens.addAll(s1Tokens);
		allTokens.addAll(s2Tokens);
		
		int numOfS1Tokens = s1UniqueTokens.size();
		int numOfS2Tokens = s2UniqueTokens.size();
		int numOfCommonTokens = (numOfS1Tokens + numOfS2Tokens) - allTokens.size();
		
		double similarity = (double)(2 * numOfCommonTokens) / ((double)numOfS1Tokens + (double)numOfS2Tokens);
		return similarity;
	}
}
