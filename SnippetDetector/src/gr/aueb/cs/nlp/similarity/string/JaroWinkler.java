package gr.aueb.cs.nlp.similarity.string;

import gr.aueb.cs.nlp.similarity.string.utils.StringManipulation;

public class JaroWinkler
{
	public static double getSimilarity(String s1, String s2, boolean perChar, boolean useSynonyms, int minCommonPrefixLength, double p, boolean greek)
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
		
		double dj = Jaro.getSimilarity(s1, s2, perChar, useSynonyms, greek);
		
		int commonPrefixLength = (perChar) ? StringManipulation.getCommonPrefixLength(s1, s2, minCommonPrefixLength)
										: StringManipulation.getCommonPrefixLength(StringManipulation.getTokensList(s1, " "), StringManipulation.getTokensList(s2, " "), minCommonPrefixLength, useSynonyms);
		
		double similarity = (double)(dj + commonPrefixLength*p*(1 - dj));
		return similarity;
	}
	
	public static double getNGramSimilarity(String s1, String s2, int minCommonPrefixLength, double p, int n, boolean greek)
	{
		if(greek)
		{
			s1 = StringManipulation.normalizeGreek(s1);
			s2 = StringManipulation.normalizeGreek(s2);
		}
		s1 = s1.toUpperCase().replaceAll("\\s", " ");
		s2 = s2.toUpperCase().replaceAll("\\s", " ");
		
		double dj = Jaro.getNGramSimilarity(s1, s2, n, greek);
		
		int commonPrefixLength = StringManipulation.getCommonPrefixLength(StringManipulation.getNGrams(s1, n), StringManipulation.getNGrams(s2, n), minCommonPrefixLength, false);
		
		double similarity = (double)(dj + commonPrefixLength*p*(1 - dj));
		return similarity;
	}
}
