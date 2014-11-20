package gr.aueb.cs.nlp.bioasq.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class DataPostprocessor
{
	
	public static void processAnswers(String inputFileName, String answersFolder, String lexiconFileName, String outputFileName)
	{
		try
		{
			JsonManipulator jsonManipulator = new JsonManipulator();
			ArrayList<Question> questions = jsonManipulator.parseJsonForBaseline(inputFileName);
			
			File answersDirectory = new File(answersFolder);
			HashMap<String, String> idealAnswers = new HashMap<>();
			for (File answerFile : answersDirectory.listFiles())
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(answerFile)));
				StringBuffer idealBuffer = new StringBuffer();
				String line = null;
				while ((line = br.readLine()) != null)
				{
					idealBuffer.append(line);
				}
				br.close();
				int beginIndex = answerFile.getName().indexOf("-") + 1;
				int endIndex = answerFile.getName().lastIndexOf("-");
				idealAnswers.put(answerFile.getName().substring(beginIndex, endIndex), idealBuffer.toString());
			}
			ArrayList<Question> updatedWithIdealQuestions = jsonManipulator.updateJsonWithAnswers(questions, idealAnswers, false);
			
			HashMap<String, String> exactAnswers = getExactYesNoAnswers(updatedWithIdealQuestions, lexiconFileName);
			ArrayList<Question> updatedWithExactQuestions = jsonManipulator.updateJsonWithAnswers(updatedWithIdealQuestions, exactAnswers, true);
			jsonManipulator.dumpAnswersToJson(updatedWithExactQuestions, outputFileName);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void processAnswersForStats(String inputFileName)
	{
		JsonManipulator jsonManipulator = new JsonManipulator();
		ArrayList<Question> questions = jsonManipulator.parseJsonForBaseline(inputFileName);
		
		int questionsCount = questions.size();
		int conceptsCount = 0;
		int documentsCount = 0;
		int snippetsCount = 0;
		int statementsCount = 0;
		int questionsWithNoConcepts = 0;
		int questionsWithNoTriples = 0;
		for (Question question : questions)
		{
			conceptsCount += question.getConcepts().size();
			if(question.getConcepts().size() == 0)
			{
				questionsWithNoConcepts++;
			}
			documentsCount += question.getDocuments().size();
			snippetsCount += question.getSnippets().size();
			statementsCount += question.getStatements().size();
			if(question.getStatements().size() == 0)
			{
				questionsWithNoTriples++;
			}
		}
		System.out.println("Questions:\t" + questionsCount);
		System.out.println("Concepts:\t" + conceptsCount);
		System.out.println("Documents:\t" + documentsCount);
		System.out.println("Snippets:\t" + snippetsCount);
		System.out.println("Triples:\t" + statementsCount);
		System.out.println("Qs with No Cs:\t" + questionsWithNoConcepts);
		System.out.println("Qs with No Ss:\t" + questionsWithNoTriples);
	}
	
	public static HashMap<String, String> getExactYesNoAnswers(ArrayList<Question> questions, String lexiconFileName)
	{
		HashMap<String, String> exactAnswers = new HashMap<>();
		ArrayList<HashSet<String>> polaritydWords = getPolarityWords(lexiconFileName);
		for (Question question : questions)
		{
			System.err.println(question.getHashId());
			if (question.getType().equals("yesno"))
			{
				if (getPolarity(question.getIdealAnswer(), polaritydWords.get(0), polaritydWords.get(1)))
				{
					exactAnswers.put(question.getHashId(), "Yes");
				}
				else
				{
					exactAnswers.put(question.getHashId(), "No");
				}
			}
		}
		return exactAnswers;
	}
	
	public static boolean getPolarity(String idealAnswer, HashSet<String> positiveWords, HashSet<String> negativeWords)
	{
		Random R = new Random(0);
		int positives = 0;
		int negatives = 0;
		String [] words = idealAnswer.split("\\s");
		for (String word : words)
		{
			if (word.indexOf(".") > 0)
			{
				word = word.substring(0, word.indexOf("."));
			}
			else if (word.indexOf(",") > 0)
			{
				word = word.substring(0, word.indexOf(","));
			}
			else if (word.indexOf("!") > 0)
			{
				word = word.substring(0, word.indexOf("!"));
			}
			else if (word.indexOf(";") > 0)
			{
				word = word.substring(0, word.indexOf(";"));
			}
			
			if (positiveWords.contains(word.toLowerCase()))
			{
				positives++;
			}
			if (negativeWords.contains(word.toLowerCase()))
			{
				negatives++;
			}
		}
		
		if (positives - negatives != 0)
		{
			return positives > negatives;
		}
		return R.nextBoolean();
	}
	
	public static ArrayList<HashSet<String>> getPolarityWords(String lexiconFileName)
	{
		ArrayList<HashSet<String>> polarityWords = new ArrayList<>();
		try
		{
			HashSet<String> positiveWords = new HashSet<>();
			HashSet<String> negativeWords = new HashSet<>();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(lexiconFileName)));
			String line = null;
			while ((line = br.readLine()) != null)
			{
				if (!line.equals(""))
				{
					String [] items = line.split("\\s");
					if (items[5].substring(items[5].indexOf("=") + 1).equals("positive"))
					{
						positiveWords.add(items[2].substring(items[2].indexOf("=") + 1));
					}
					else if (items[5].substring(items[5].indexOf("=") + 1).equals("negative"))
					{
						negativeWords.add(items[2].substring(items[2].indexOf("=") + 1));
					}
				}
			}
			polarityWords.add(positiveWords);
			polarityWords.add(negativeWords);
			br.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return polarityWords;
	}
	
        

        
        
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		/*try
		{
			System.setOut(new PrintStream("out.txt"));
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*String jsonFileName = "BioASQ-task2bPhaseB-testset5.sdx";
		String lexiconFileName = "subjclueslen1-HLTEMNLP05.tff";
		processAnswers(jsonFileName, "ideal_answers/Year 2/B5/TheGreedy", lexiconFileName, "AUEB.base-greedy.year2.batc5.json");
		processAnswers(jsonFileName, "ideal_answers/Year 2/B5/ILP", lexiconFileName, "AUEB.base-ilp.year2.batch5.json");
		*/
		
		System.out.println("done");
	}
	
}
