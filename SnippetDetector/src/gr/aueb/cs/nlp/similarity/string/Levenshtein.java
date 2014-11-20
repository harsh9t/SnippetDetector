package gr.aueb.cs.nlp.similarity.string;

import gr.aueb.cs.nlp.similarity.string.utils.MathFunctions;
import gr.aueb.cs.nlp.similarity.string.utils.StringManipulation;

import java.util.ArrayList;


public class Levenshtein
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
			int s1Length = s1.length();
			int s2Length = s2.length();
			
			if(s1Length==0)
			{
				return 0.0;
			}
			if(s2Length==0)
			{
				return 0.0;
			}
			
			int [][] d = new int [s1Length + 1][s2Length + 1];
			
			for(int i=0;i<=s1Length;i++)
			{
				d[i][0] = i;
			}
			for(int j=0;j<=s2Length;j++)
			{
				d[0][j] = j;
			}
			
			char ch1 = ' ';
			char ch2 = ' ';
			int cost = 0;
			for(int i=1;i<=s1Length;i++)
			{
				ch1 = s1.charAt(i - 1);
				for(int j=1;j<=s2Length;j++)
				{
					ch2 = s2.charAt(j - 1);
					cost = (ch1==ch2) ? 0 : 1;
					d[i][j] = MathFunctions.min_of_3(d[i-1][j] + 1, d[i][j-1] + 1, d[i-1][j-1] + cost);
				}
			}
			
			int maxLength = (s1Length > s2Length) ? s1Length : s2Length;
			double similarity = 1.0 - ((double)d[s1Length][s2Length] / (double)maxLength);
			return similarity;
		}
		else
		{
			ArrayList<String> s1Tokens = StringManipulation.getTokensList(s1, " ");
			ArrayList<String> s2Tokens = StringManipulation.getTokensList(s2, " ");
			
			int s1Length = s1Tokens.size();
			int s2Length = s2Tokens.size();
			
			if(s1Length==0)
			{
				return 0.0;
			}
			if(s2Length==0)
			{
				return 0.0;
			}
			
			int [][] d = new int [s1Length + 1][s2Length + 1];
			
			for(int i=0;i<=s1Length;i++)
			{
				d[i][0] = i;
			}
			for(int j=0;j<=s2Length;j++)
			{
				d[0][j] = j;
			}
			
			String token1 = "";
			String token2 = "";			
			int cost = 0;
			for(int i=1;i<=s1Length;i++)
			{
				token1 = s1Tokens.get(i-1);
				for(int j=1;j<=s2Length;j++)
				{
					token2 = s2Tokens.get(j-1);
					cost = (token1.equals(token2)) ? 0 : ((useSynonyms) && (StringManipulation.wn.isSynonym(token1, token2))) ? 0 : 1;
					d[i][j] = MathFunctions.min_of_3(d[i-1][j] + 1, d[i][j-1] + 1, d[i-1][j-1] + cost);
				}
			}
			
			int maxLength = (s1Length > s2Length) ? s1Length : s2Length;
			double similarity = 1.0 - ((double)d[s1Length][s2Length] / (double)maxLength);
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
		
		int s1Length = s1Tokens.size();
		int s2Length = s2Tokens.size();
		
		if(s1Length==0)
		{
			return 0.0;
		}
		if(s2Length==0)
		{
			return 0.0;
		}
		
		int [][] d = new int [s1Length + 1][s2Length + 1];
		
		for(int i=0;i<=s1Length;i++)
		{
			d[i][0] = i;
		}
		for(int j=0;j<=s2Length;j++)
		{
			d[0][j] = j;
		}
		
		String token1 = "";
		String token2 = "";			
		int cost = 0;
		for(int i=1;i<=s1Length;i++)
		{
			token1 = s1Tokens.get(i-1);
			for(int j=1;j<=s2Length;j++)
			{
				token2 = s2Tokens.get(j-1);
				cost = (token1.equals(token2)) ? 0 : 1;
				d[i][j] = MathFunctions.min_of_3(d[i-1][j] + 1, d[i][j-1] + 1, d[i-1][j-1] + cost);
			}
		}
		
		int maxLength = (s1Length > s2Length) ? s1Length : s2Length;
		double similarity = 1.0 - ((double)d[s1Length][s2Length] / (double)maxLength);
		return similarity;
	}
}
