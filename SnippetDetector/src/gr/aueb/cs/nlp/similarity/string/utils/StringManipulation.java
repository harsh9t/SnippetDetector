package gr.aueb.cs.nlp.similarity.string.utils;

import gr.aueb.cs.nlp.rte.wn.WordNet;
import gr.aueb.cs.nlp.stemming.StemmerEN;
import gr.aueb.cs.nlp.stemming.StemmerGR;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class StringManipulation
{
	public static final StemmerEN stEn = new StemmerEN();
	public static final StemmerGR stGR = new StemmerGR();
	public static final WordNet wn = new WordNet();
	
	public static String normalizeGreek(String s1)
	{
		s1 = s1.replaceAll("À", "é");
		s1 = s1.replaceAll("à", "õ");
		s1 = s1.toUpperCase();
		String normalized = s1.replaceAll("¢", "Á")
								.replaceAll("¸", "Å")
								.replaceAll("¹", "Ç")
								.replaceAll("º", "É")
								.replaceAll("¼", "Ï")
								.replaceAll("¾", "Õ")
								.replaceAll("¿", "Ù")
								.replaceAll("Ú", "É")
								.replaceAll("Û", "Õ");
		return normalized;
	}
	
	public static ArrayList<String> getTokensList(String s1, String delimeter)
	{
		ArrayList<String> tokens = new ArrayList<String>();
		String [] s1Tokens = s1.split(delimeter);
		for (String s1Token : s1Tokens)
		{
			tokens.add(s1Token);
		}
		return tokens;
	}
	
	public static HashMap<String, Integer> getTokensMap(ArrayList<String> s1Tokens)
	{
		HashMap<String, Integer> tokens = new HashMap<String, Integer>();
		for (String s1Token : s1Tokens)
		{
			if(!tokens.containsKey(s1Token))
			{
				tokens.put(s1Token, 1);
			}
			else
			{
				tokens.put(s1Token, tokens.get(s1Token) + 1);
			}
		}
		return tokens;
	}
	
	public static ArrayList<String> getNGrams(String s1, int n)
	{
		ArrayList<String> nGrams = new ArrayList<String>();
		StringBuffer nGram = new StringBuffer();
		for(int i=0 - n + 1;i<s1.length(); i++)
		{
			nGram = new StringBuffer();
			for(int j=0;j<n;j++)
			{
				if(i+j<0)
				{
					nGram.append("#");
				}
				else if(i+j>=s1.length())
				{
					nGram.append("%");
				}
				else
				{
					nGram.append(s1.charAt(i+j));
				}
			}
			nGrams.add(nGram.toString());
		}
		/*for (String ngram : nGrams)
		{
			System.out.println(ngram);
		}*/
		return nGrams;
	}
	
	/*public static ArrayList<String> getNGrams(ArrayList<String> s1, int n)
	{
		ArrayList<String> nGrams = new ArrayList<String>();
		StringBuffer nGram = new StringBuffer();
		for(int i=0 - n + 1;i<s1.size(); i++)
		{
			nGram = new StringBuffer();
			for(int j=0;j<n;j++)
			{
				if(i+j<0)
				{
					nGram.append("#");
				}
				else if(i+j>=s1.size())
				{
					nGram.append("%");
				}
				else
				{
					nGram.append(s1.get(i+j));
				}
			}
			nGrams.add(nGram.toString());
		}
		for (String ngram : nGrams)
		{
			System.out.println(ngram);
		}
		return nGrams;
	}*/
	
	public static String getSoundex(String s1, int soundexLength, boolean greek)
	{
		if(greek)
		{
			return s1;
		}
		String tmpStr;
		String wordStr;
		char curChar;
		char lastChar;
		final int wsLen;
		final char firstLetter;
		
		//ensure soundexLen is in a valid range
		if (soundexLength > 10)
		{
			soundexLength = 10;
		}
		if (soundexLength < 4) {
			soundexLength = 4;
		}
		
		//check for empty input
		if(s1 == null)
		{
			return "";
		}
		
		if (s1.length() == 0)
		{
			return "";
		}
		
		//remove case
		s1 = s1.toUpperCase();
		
		/* Clean and tidy
		*/
		wordStr = s1;
		wordStr = wordStr.replaceAll("[^A-Z]", " "); // rpl non-chars w space
		wordStr = wordStr.replaceAll("\\s+", "");   // remove spaces
		
		//check for empty input again the previous clean and tidy could of shrunk it to zero.
		if (wordStr.length() == 0)
		{
			return ("");
		}
		
		/* The above improvements
		 * may change this first letter
		*/
		firstLetter = wordStr.charAt(0);
		
		// uses the assumption that enough valid characters are in the first 4 times the soundex required length
		if(wordStr.length() > (6*4)+1)
		{
			wordStr = "-" + wordStr.substring(1,6*4);
		}
		else
		{
			wordStr = "-" + wordStr.substring(1);
		}
		// Begin Classic SoundEx
		/*
		1) B,P,F,V
		2) C,S,K,G,J,Q,X,Z
		3) D,T
		4) L
		5) M,N
		6) R
		*/
		wordStr = wordStr.replaceAll("[AEIOUWH]", "0");
		wordStr = wordStr.replaceAll("[BPFV]", "1");
		wordStr = wordStr.replaceAll("[CSKGJQXZ]", "2");
		wordStr = wordStr.replaceAll("[DT]", "3");
		wordStr = wordStr.replaceAll("[L]", "4");
		wordStr = wordStr.replaceAll("[MN]", "5");
		wordStr = wordStr.replaceAll("[R]", "6");
		
		// Remove extra equal adjacent digits
		wsLen = wordStr.length();
		lastChar = '-';
		tmpStr = "-";     /* replacing skipped first character */
		for (int i = 1; i < wsLen; i++)
		{
			curChar = wordStr.charAt(i);
			if (curChar != lastChar)
		    {
				tmpStr += curChar;
				lastChar = curChar;
		    }
		}
		wordStr = tmpStr;
		wordStr = wordStr.substring(1);          /* Drop first letter code   */
		wordStr = wordStr.replaceAll("0", "");  /* remove zeros             */
		wordStr += "000000000000000000";              /* pad with zeros on right  */
		wordStr = firstLetter + "-" + wordStr;      /* Add first letter of word */
		wordStr = wordStr.substring(0, soundexLength); /* size to taste     */
		return (wordStr);
	}
	
	public static String getCommonCharacters(String s1, String s2, int maxSeparatingDistance)
	{
		StringBuffer commonCharactersBuffer = new StringBuffer();
		StringBuffer s2Buffer = new StringBuffer(s2);
		char ch = ' ';
		for(int i=0;i<s1.length();i++)
		{
			ch = s1.charAt(i);
			boolean found = false;
			
			for(int j=Math.max(0, i - maxSeparatingDistance);!found && j < Math.min(i + maxSeparatingDistance, s2.length()); j++)
			{
				if(s2Buffer.charAt(j)==ch)
				{
					found = true;
					commonCharactersBuffer.append(ch);
					s2Buffer.setCharAt(j, (char)0);
				}
			}
		}
		
		return commonCharactersBuffer.toString();
	}
	
	public static ArrayList<String> getCommonTokens(ArrayList<String> s1Tokens, ArrayList<String> s2Tokens, int maxSeparatingDistance, boolean useSynonyms)
	{
		ArrayList<String> commonTokens = new ArrayList<String>();
		ArrayList<String> s2TokensList = new ArrayList<String>(s2Tokens);
		String token = "";
		for(int i=0;i<s1Tokens.size();i++)
		{
			token = s1Tokens.get(i);
			boolean found = false;
			
			for(int j=Math.max(0, i - maxSeparatingDistance);j < Math.min(i + maxSeparatingDistance, s2Tokens.size()) && !found; j++)
			{
				if(s2TokensList.get(j)!=null)
				{
					if(s2TokensList.get(j).equals(token))
					{
						found = true;
						commonTokens.add(token);
						s2TokensList.set(j, null);
					}
					else if ((useSynonyms) && (wn.isSynonym(token, s2TokensList.get(j))))
					{
						found = true;
						commonTokens.add(token);
						s2TokensList.set(j, null);
					}
				}
			}
		}
		
		return commonTokens;
	}
	
	public static int getCommonPrefixLength(String s1, String s2, int minCommonPrefixLength)
	{
		int commonPrefixLength = MathFunctions.min_of_3(minCommonPrefixLength, s1.length(), s2.length());
		for(int i=0;i<commonPrefixLength;i++)
		{
			if(s1.charAt(i)!=s2.charAt(i))
			{
				return i;
			}
		}
		return commonPrefixLength;
	}
	
	public static int getCommonPrefixLength(ArrayList<String> s1Tokens, ArrayList<String> s2Tokens, int minCommonPrefixLength, boolean useSynonyms)
	{
		int commonPrefixLength = MathFunctions.min_of_3(minCommonPrefixLength, s1Tokens.size(), s2Tokens.size());
		for(int i=0;i<commonPrefixLength;i++)
		{
			if(!s1Tokens.get(i).equals(s2Tokens.get(i)))
			{
				return i;
			}
			else if((useSynonyms)&&(!wn.isSynonym(s1Tokens.get(i), s2Tokens.get(i))));
		}
		return commonPrefixLength;
	}
	
	public static boolean containsSynonym(String word, Collection<String> possibleSynonyms)
	{
		for (String posssibleSynonym : possibleSynonyms)
		{
			if(wn.isSynonym(word, posssibleSynonym))
			{
				return true;
			}
		}
		return false;
	}
	
	public static HashSet<String> getUniqueTokensAndSynonyms(Collection<String> tokens)
	{
		HashSet<String> uniqueTokens = new HashSet<String>();
		for (String token : tokens)
		{
			if(!containsSynonym(token, uniqueTokens))
			{
				uniqueTokens.add(token);
			}
		}
		return uniqueTokens;
	}
	
	public static int getNumberOfEqualsAndSynonyms(String word, Collection<String> possibleSynonyms)
	{
		int numberOfEqualsAndSynonyms = 0;
		for (String posssibleSynonym : possibleSynonyms)
		{
			if((wn.isSynonym(word, posssibleSynonym))||(word.equalsIgnoreCase(posssibleSynonym)))
			{
				numberOfEqualsAndSynonyms++;
			}
		}
		return numberOfEqualsAndSynonyms;
	}
	
	public static HashMap<String, HashSet<String>> loadWordsMapFromFile(String tableFileName)
	{
		HashMap<String, HashSet<String>> wordsMap = null; 
		try
		{
			final File wordsMapFile = new File(tableFileName);
			if(!wordsMapFile.exists())
			{
				wordsMapFile.createNewFile();
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(wordsMapFile)), 512);
			
			char [] buff = new char[512];
			StringBuffer xmlContents = new StringBuffer();
			int bytesRead = 0;
			while((bytesRead = br.read(buff))!=-1)
			{
				xmlContents.append(buff,0,bytesRead);
			}
			if(xmlContents.length()>0)
			{
				xmlContents.trimToSize();
				br.close();
				XStream xstream = new XStream(new DomDriver());
				xstream.alias("HashMap<String, HashSet<String>>", HashMap.class);
				wordsMap = (HashMap<String, HashSet<String>>)xstream.fromXML(xmlContents.toString());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return wordsMap;
	}
}