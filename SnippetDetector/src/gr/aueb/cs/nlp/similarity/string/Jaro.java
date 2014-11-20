package gr.aueb.cs.nlp.similarity.string;

import gr.aueb.cs.nlp.similarity.string.utils.StringManipulation;

import java.util.ArrayList;


public class Jaro
{
	public static double getSimilarity(String s1, String s2, boolean perChar, boolean useSynonyms, boolean greek)
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
		
		if(perChar)
		{
			int maxSeparatingDistance = Math.max((Math.max(s1.length(), s2.length()) / 2) - 1, 1);
			
			String m1 = StringManipulation.getCommonCharacters(s1, s2, maxSeparatingDistance);
			String m2 = StringManipulation.getCommonCharacters(s2, s1, maxSeparatingDistance);
			
			if((m1.length()==0)||(m2.length()==0))
			{
				return 0.0;
			}
			
			if(m1.length() != m2.length())
			{
				return 0.0;
			}
			
			int transposotions = 0;
			
			for(int i=0;i<m1.length();i++)
			{
				if(m1.charAt(i)!=m2.charAt(i))
				{
					transposotions++;
				}
			}
			
			transposotions /= 2;
			
			double similarity = ((double)m1.length()/(double)(3*s1.length())) 
								+ ((double)m1.length()/(double)(3*s2.length())) 
								+ ((double)(m1.length() - transposotions)/(double)(3*m1.length()));
			return similarity;
		}
		else
		{
			ArrayList<String> s1Tokens = StringManipulation.getTokensList(s1, " ");
			ArrayList<String> s2Tokens = StringManipulation.getTokensList(s2, " ");
			
			int maxSeparatingDistance = Math.max((Math.max(s1Tokens.size(), s2Tokens.size()) / 2) - 1, 1);
			
			ArrayList<String> m1 = StringManipulation.getCommonTokens(s1Tokens, s2Tokens, maxSeparatingDistance, useSynonyms);
			ArrayList<String> m2 = StringManipulation.getCommonTokens(s2Tokens, s1Tokens, maxSeparatingDistance, useSynonyms);
			
			if((m1.size()==0)||(m2.size()==0))
			{
				return 0.0;
			}
			
			if(m1.size() != m2.size())
			{
				return 0.0;
			}
			
			int transposotions = 0;
			
			for(int i=0;i<m1.size();i++)
			{
				if(!m1.get(i).equals(m2.get(i)))
				{
					transposotions++;
				}
			}
			
			transposotions /= 2;
			
			double similarity = ((double)m1.size()/(double)(3*s1Tokens.size())) 
								+ ((double)m1.size()/(double)(3*s2Tokens.size())) 
								+ ((double)(m1.size() - transposotions)/(double)(3*m1.size()));
			return similarity;
		}
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
		
		int maxSeparatingDistance = (Math.max(s1Tokens.size(), s2Tokens.size()) / 2) - 1;
		
		ArrayList<String> m1 = StringManipulation.getCommonTokens(s1Tokens, s2Tokens, maxSeparatingDistance, false);
		ArrayList<String> m2 = StringManipulation.getCommonTokens(s2Tokens, s1Tokens, maxSeparatingDistance, false);
		
		if((m1.size()==0)||(m2.size()==0))
		{
			return 0.0;
		}
		
		if(m1.size() != m2.size())
		{
			return 0.0;
		}
		
		int transposotions = 0;
		
		for(int i=0;i<m1.size();i++)
		{
			if(!m1.get(i).equals(m2.get(i)))
			{
				transposotions++;
			}
		}
		
		transposotions /= 2;
		
		double similarity = ((double)m1.size()/(double)(3*s1Tokens.size())) 
							+ ((double)m1.size()/(double)(3*s2Tokens.size())) 
							+ ((double)(m1.size() - transposotions)/(double)(3*m1.size()));
		return similarity;
	}
}
