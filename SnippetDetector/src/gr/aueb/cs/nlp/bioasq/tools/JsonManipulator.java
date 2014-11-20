package gr.aueb.cs.nlp.bioasq.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class JsonManipulator
{
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		JsonManipulator manipulator = new JsonManipulator();
		//manipulator.parseJsonForPostAgreement("duplicate_questions_post_assessment.json");
		manipulator.parseJsonForCorrelation("assessed2.json");
		System.out.println("done");
	}
	
	public HashMap<Question, ArrayList<Question>> parseJsonForCorrelation(String jsonFileName)
	{
		HashMap<Question, ArrayList<Question>> references = new HashMap<>();
		try
		{
			JsonReader jsonReader = new JsonReader(new InputStreamReader(new FileInputStream(jsonFileName)));
			JsonParser jsonParser = new JsonParser();
			JsonArray jsonArray = jsonParser.parse(jsonReader).getAsJsonArray();
			int id = 1;
			for (JsonElement element : jsonArray)
			{
				JsonObject jsonObject = element.getAsJsonObject();
				String hashId = jsonObject.get("_id").getAsString();
				String type = jsonObject.get("type").getAsString();
				String body = jsonObject.get("body").getAsString();
				String creator = jsonObject.get("creator").getAsString();
				JsonObject answer = jsonObject.get("answer").getAsJsonObject();
				ArrayList<String> idealAnswers = new ArrayList<>();
				ArrayList<ArrayList<Integer>> idealAnswerScores = new ArrayList<>();
				String goldenIdealAnswer = "";
				JsonArray idealAnswersArray = answer.get("ideal").getAsJsonArray();
				for (JsonElement idealAnswerElement : idealAnswersArray)
				{
					JsonObject idealAnswerObject = idealAnswerElement.getAsJsonObject();
					boolean golden = idealAnswerObject.get("golden").getAsBoolean();
					if (golden)
					{
						goldenIdealAnswer = idealAnswerObject.get("body").getAsString();
					}
					else
					{
						idealAnswers.add(idealAnswerObject.get("body").getAsString());
						ArrayList<Integer> answerScores = new ArrayList<>();
						if(idealAnswerObject.has("scores"))
						{
							JsonObject scoreObject = idealAnswerObject.get("scores").getAsJsonObject();
							if (scoreObject.has("recall"))
							{
								answerScores.add(scoreObject.get("recall").getAsInt());
							}
							else
							{
								answerScores.add(0);
							}
							if (scoreObject.has("precision"))
							{
								answerScores.add(scoreObject.get("precision").getAsInt());
							}
							else
							{
								answerScores.add(0);
							}
							if (scoreObject.has("repetition"))
							{
								answerScores.add(scoreObject.get("repetition").getAsInt());
							}
							else
							{
								answerScores.add(0);
							}
							if (scoreObject.has("readability"))
							{
								answerScores.add(scoreObject.get("readability").getAsInt());
							}
							else
							{
								answerScores.add(0);
							}
						}
						else
						{
							answerScores.add(0);
							answerScores.add(0);
							answerScores.add(0);
							answerScores.add(0);
						}
						idealAnswerScores.add(answerScores);
					}
				}
				ArrayList<ArrayList<String>> exactAnswer = new ArrayList<>();
				if (answer.has("exact"))
				{
					if (type.equals("list") || type.equals("factoid"))
					{
						JsonArray listAnswers = answer.get("exact").getAsJsonArray();
						for (JsonElement listAnswer : listAnswers)
						{
							ArrayList<String> answersList = new ArrayList<>();
							if(listAnswer.isJsonArray())
							{
								for (JsonElement answerItem : listAnswer.getAsJsonArray())
								{
									answersList.add(answerItem.getAsString());
								}
							}
							else
							{
								answersList.add(listAnswer.getAsString());
							}
							exactAnswer.add(answersList);
						}
					}
					else
					{
						ArrayList<String> answersList = new ArrayList<>();
						answersList.add(answer.get("exact").getAsString());
						exactAnswer.add(answersList);
					}
				}
				JsonArray annotations = answer.getAsJsonArray("annotations");
				ArrayList<String> concepts = new ArrayList<>(getSimpleAnnotations(annotations, "concept"));
				ArrayList<String> documents = new ArrayList<>(getSimpleAnnotations(annotations, "document"));
				ArrayList<String> statements = new ArrayList<>(getSimpleAnnotations(annotations, "statement"));
				ArrayList<Snippet> snippets = new ArrayList<>(getSnippetsForPostAgreement(annotations));
				
				ArrayList<Question> questions = new ArrayList<>();
				questions.add(new Question(id, hashId, type, body, goldenIdealAnswer, new ArrayList<Integer>(), creator, exactAnswer, concepts, documents, snippets, statements));
				for (int i = 0; i < idealAnswers.size(); i++)
				{
					questions.add(new Question(id, hashId, type, body, idealAnswers.get(i), idealAnswerScores.get(i), creator, exactAnswer, concepts, documents, snippets, statements));
				}
				references.put(new Question(id, hashId, type, body, goldenIdealAnswer, new ArrayList<Integer>(), creator, exactAnswer, concepts, documents, snippets, statements), questions);
				//System.out.println(questions.get(id - 1));
				id++;
			}
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return references;
	}
	
	public ArrayList<Question> parseJsonForAgreement(String jsonFileName)
	{
		
		ArrayList<Question> questions = new ArrayList<>();
		try
		{
			JsonReader jsonReader = new JsonReader(new InputStreamReader(new FileInputStream(jsonFileName)));
			JsonParser jsonParser = new JsonParser();
			JsonArray jsonArray = jsonParser.parse(jsonReader).getAsJsonArray();
			int id = 1;
			for (JsonElement element : jsonArray)
			{
				JsonObject jsonObject = element.getAsJsonObject();
				String hashId = jsonObject.get("_id").getAsString();
				String type = jsonObject.get("type").getAsString();
				String body = jsonObject.get("body").getAsString();
				String creator = jsonObject.get("creator").getAsString();
				JsonObject answer = jsonObject.get("answer").getAsJsonObject();
				String idealAnswer = answer.get("ideal").getAsString();
				ArrayList<Integer> idealAnswerScores = new ArrayList<>();
				ArrayList<ArrayList<String>> exactAnswer = new ArrayList<>();
				if (answer.has("exact"))
				{
					if (type.equals("list") || type.equals("factoid"))
					{
						JsonArray listAnswers = answer.get("exact").getAsJsonArray();
						for (JsonElement listAnswer : listAnswers)
						{
							ArrayList<String> answersList = new ArrayList<>();
							if(listAnswer.isJsonArray())
							{
								for (JsonElement answerItem : listAnswer.getAsJsonArray())
								{
									answersList.add(answerItem.getAsString());
								}
							}
							else
							{
								answersList.add(listAnswer.getAsString());
							}
							exactAnswer.add(answersList);
						}
					}
					else
					{
						ArrayList<String> answersList = new ArrayList<>();
						answersList.add(answer.get("exact").getAsString());
						exactAnswer.add(answersList);
					}
				}
				JsonArray annotations = answer.getAsJsonArray("annotations");
				ArrayList<String> concepts = new ArrayList<>(getSimpleAnnotations(annotations, "concept"));
				ArrayList<String> documents = new ArrayList<>(getSimpleAnnotations(annotations, "document"));
				ArrayList<String> statements = new ArrayList<>(getSimpleAnnotations(annotations, "statement"));
				ArrayList<Snippet> snippets = new ArrayList<>(getSnippets(annotations));
				questions.add(new Question(id, hashId, type, body, idealAnswer, idealAnswerScores, creator, exactAnswer, concepts, documents, snippets, statements));
				//System.out.println(questions.get(id - 1));
				id++;
			}
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return questions;
	}
	
	public ArrayList<Question> parseJsonForAgreement2(String jsonFileName)
	{
		
		ArrayList<Question> questions = new ArrayList<>();
		try
		{
			JsonReader jsonReader = new JsonReader(new InputStreamReader(new FileInputStream(jsonFileName)));
			JsonParser jsonParser = new JsonParser();
			JsonArray jsonArray = jsonParser.parse(jsonReader).getAsJsonArray();
			int id = 1;
			for (JsonElement element : jsonArray)
			{
				JsonObject jsonObject = element.getAsJsonObject();
				String hashId = jsonObject.get("_id").getAsString();
				String type = jsonObject.get("type").getAsString();
				String body = jsonObject.get("body").getAsString();
				String creator = jsonObject.get("creator").getAsString();
				JsonObject answer = jsonObject.get("answer").getAsJsonObject();
				String idealAnswer = answer.get("ideal").getAsString();
				ArrayList<Integer> idealAnswerScores = new ArrayList<>();
				ArrayList<ArrayList<String>> exactAnswer = new ArrayList<>();
				if (answer.has("exact"))
				{
					if (type.equals("list") || type.equals("factoid"))
					{
						JsonArray listAnswers = answer.get("exact").getAsJsonArray();
						for (JsonElement listAnswer : listAnswers)
						{
							ArrayList<String> answersList = new ArrayList<>();
							if(listAnswer.isJsonArray())
							{
								for (JsonElement answerItem : listAnswer.getAsJsonArray())
								{
									answersList.add(answerItem.getAsString());
								}
							}
							else
							{
								answersList.add(listAnswer.getAsString());
							}
							exactAnswer.add(answersList);
						}
					}
					else
					{
						ArrayList<String> answersList = new ArrayList<>();
						answersList.add(answer.get("exact").getAsString());
						exactAnswer.add(answersList);
					}
				}
				JsonArray conceptsArray = jsonObject.getAsJsonArray("concepts");
				ArrayList<String> concepts = new ArrayList<>();
				for (JsonElement conceptElement : conceptsArray)
				{
					JsonObject concept = conceptElement.getAsJsonObject();
					concepts.add(concept.get("uri").getAsString());
				}
				
				JsonArray documentsArray = jsonObject.getAsJsonArray("documents");
				ArrayList<String> documents = new ArrayList<>();
				for (JsonElement documentElement : documentsArray)
				{
					JsonObject document = documentElement.getAsJsonObject();
					documents.add(document.get("uri").getAsString());
				}
				
				JsonArray statementsArray = jsonObject.getAsJsonArray("statements");
				ArrayList<String> statements = new ArrayList<>();
				for (JsonElement statementElement : statementsArray)
				{
					JsonObject statement = statementElement.getAsJsonObject();
					statements.add(statement.get("title").getAsString());
				}
				
				ArrayList<Snippet> snippets = new ArrayList<>(getSnippets2(jsonObject.getAsJsonArray("snippets")));
				questions.add(new Question(id, hashId, type, body, idealAnswer, idealAnswerScores, creator, exactAnswer, concepts, documents, snippets, statements));
				//System.out.println(questions.get(id - 1));
				id++;
			}
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return questions;
	}
	
	public ArrayList<Question> parseJsonForPostAgreement(String jsonFileName)
	{
		
		ArrayList<Question> questions = new ArrayList<>();
		try
		{
			JsonReader jsonReader = new JsonReader(new InputStreamReader(new FileInputStream(jsonFileName)));
			JsonParser jsonParser = new JsonParser();
			JsonArray jsonArray = jsonParser.parse(jsonReader).getAsJsonArray();
			int id = 1;
			for (JsonElement element : jsonArray)
			{
				JsonObject jsonObject = element.getAsJsonObject();
				String hashId = jsonObject.get("id").getAsString();
				String type = jsonObject.get("type").getAsString();
				String body = jsonObject.get("body").getAsString();
				String creator = jsonObject.get("creator").getAsString();
				JsonObject answer = jsonObject.get("answer").getAsJsonObject();
				JsonArray idealAnswerArray = answer.get("ideal").getAsJsonArray();
				String idealAnswer = "";
				int goldIdealAnswerCount = 0;
				int idealAnswerCount = 0;
				for (JsonElement idealAnswerElement : idealAnswerArray)
				{
					idealAnswerCount++;
					JsonObject idealAnswerObject = idealAnswerElement.getAsJsonObject();
					boolean golden = idealAnswerObject.get("golden").getAsBoolean();
					if (golden)
					{
						goldIdealAnswerCount++;
						idealAnswer = idealAnswerObject.get("body").getAsString();
					}
				}
				//System.out.println(body + "\t" + hashId);
				//System.out.println("Ideal Answers: " + idealAnswerCount);
				//System.out.println("Gold Ideal Answers: " + goldIdealAnswerCount);
				ArrayList<Integer> idealAnswerScores = new ArrayList<>();
				ArrayList<ArrayList<String>> exactAnswer = new ArrayList<>();
				if (answer.has("exact"))
				{
					if (type.equals("list") || type.equals("factoid"))
					{
						JsonArray listAnswers = answer.get("exact").getAsJsonArray();
						for (JsonElement listAnswer : listAnswers)
						{
							ArrayList<String> answersList = new ArrayList<>();
							if(listAnswer.isJsonArray())
							{
								for (JsonElement answerItem : listAnswer.getAsJsonArray())
								{
									answersList.add(answerItem.getAsString());
								}
							}
							else
							{
								answersList.add(listAnswer.getAsString());
							}
							exactAnswer.add(answersList);
						}
					}
					else
					{
						ArrayList<String> answersList = new ArrayList<>();
						answersList.add(answer.get("exact").getAsString());
						exactAnswer.add(answersList);
					}
				}
				JsonArray annotations = answer.getAsJsonArray("annotations");
				ArrayList<String> concepts = new ArrayList<>(getSimpleAnnotationsForPostAgreement(annotations, "concept"));
				ArrayList<String> documents = new ArrayList<>(getSimpleAnnotationsForPostAgreement(annotations, "document"));
				ArrayList<String> statements = new ArrayList<>(getSimpleAnnotationsForPostAgreement(annotations, "statement"));
				ArrayList<Snippet> snippets = new ArrayList<>(getSnippetsForPostAgreement(annotations));
				questions.add(new Question(id, hashId, type, body, idealAnswer, idealAnswerScores, creator, exactAnswer, concepts, documents, snippets, statements));
				//System.out.println(questions.get(id - 1));
				id++;
			}
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return questions;
	}
	
	public ArrayList<Question> parseJsonForBaseline(String jsonFileName)
	{
		
		ArrayList<Question> questions = new ArrayList<>();
		try
		{
			JsonReader jsonReader = new JsonReader(new InputStreamReader(new FileInputStream(jsonFileName)));
			JsonParser jsonParser = new JsonParser();
			JsonObject mainObject = jsonParser.parse(jsonReader).getAsJsonObject();
			JsonArray jsonArray = mainObject.getAsJsonArray("questions");
			int id = 1;
			for (JsonElement element : jsonArray)
			{
				JsonObject jsonObject = element.getAsJsonObject();
				String hashId = jsonObject.get("id").getAsString();
				String type = jsonObject.get("type").getAsString();
				String body = jsonObject.get("body").getAsString();
				String creator = "unknown";
				String idealAnswer = "";
				ArrayList<Integer> idealAnswerScores = new ArrayList<>();
				ArrayList<ArrayList<String>> exactAnswer = new ArrayList<>();
				ArrayList<String> concepts = new ArrayList<>(getSimpleAnnotationsForBaseline(jsonObject.getAsJsonArray("concepts")));
				ArrayList<String> documents = new ArrayList<>(getSimpleAnnotationsForBaseline(jsonObject.getAsJsonArray("documents")));
				ArrayList<String> statements = new ArrayList<>(getTriplesForBaseline(jsonObject.getAsJsonArray("triples")));
				ArrayList<Snippet> snippets = new ArrayList<>(getSnippetsForBaseline(jsonObject.getAsJsonArray("snippets")));
				questions.add(new Question(id, hashId, type, body, idealAnswer, idealAnswerScores, creator, exactAnswer, concepts, documents, snippets, statements));
				//System.out.println(questions.get(id - 1));
				id++;
			}
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return questions;
	}
	
	public ArrayList<Question> updateJsonWithAnswers(ArrayList<Question> questions, HashMap<String, String> answers, boolean exact)
	{
		for (int i = 0; i < questions.size(); i++)
		{
			if(exact)
			{
				questions.get(i).setExactAnswer(answers.get(questions.get(i).getHashId()));
			}
			else
			{
				questions.get(i).setIdealAnswer(answers.get(questions.get(i).getHashId()));
			}			
		}
		return questions;
	}
	
	public void dumpAnswersToJson(ArrayList<Question> questions, String jsonFileName)
	{
		try
		{
			JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(new FileOutputStream(jsonFileName), "UTF-8"));
			jsonWriter.beginObject();
			jsonWriter.name("questions");
			jsonWriter.beginArray();
			for (Question question : questions)
			{
				jsonWriter.beginObject();
				jsonWriter.name("id").value(question.getHashId());
				jsonWriter.name("ideal_answer").value(question.getIdealAnswer());
				jsonWriter.name("exact_answer");
				switch (question.getType())
				{
					case "yesno":
						jsonWriter.value(question.getExactAnswer().get(0).get(0));
						break;
					/*case "factoid":
						jsonWriter.beginArray();
						for (String fact : question.getExactAnswer().get(0))
						{
							jsonWriter.value(fact);
						}
						jsonWriter.endArray();
						break;
					case "list":
						jsonWriter.beginArray();
						for (ArrayList<String> facts : question.getExactAnswer())
						{
							jsonWriter.beginArray();
							for (String fact : facts)
							{
								jsonWriter.value(fact);
							}
							jsonWriter.endArray();
						}
						jsonWriter.endArray();
						break;*/
					default:
						jsonWriter.value("");
				}
				jsonWriter.endObject();
			}
			jsonWriter.endArray();
			jsonWriter.endObject();
			jsonWriter.flush();
			jsonWriter.close();
			
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
	}
	
	public ArrayList<String> getSimpleAnnotations(JsonArray annotations, String type)
	{
		ArrayList<String> specificAnnotations = new ArrayList<>();
		for (JsonElement element : annotations)
		{
			JsonObject annotation = element.getAsJsonObject();
			if (annotation.get("type").getAsString().equals(type))
			{
				if (type.equals("statement"))
				{
					specificAnnotations.add(annotation.get("s").getAsString());
				}
				else
				{
					specificAnnotations.add(annotation.get("uri").getAsString());
				}
			}
		}
		return specificAnnotations;
	}
	
	public ArrayList<String> getSimpleAnnotationsForPostAgreement(JsonArray annotations, String type)
	{
		ArrayList<String> specificAnnotations = new ArrayList<>();
		for (JsonElement element : annotations)
		{
			JsonObject annotation = element.getAsJsonObject();
			if (annotation.get("type").getAsString().equals(type))
			{
				if(annotation.get("golden").getAsBoolean())
				{
					if (type.equals("statement"))
					{
						specificAnnotations.add(annotation.get("s").getAsString());
					}
					else
					{
						specificAnnotations.add(annotation.get("uri").getAsString());
					}
				}
			}
		}
		return specificAnnotations;
	}
	
	public ArrayList<Snippet> getSnippets(JsonArray annotations)
	{
		ArrayList<Snippet> snippets = new ArrayList<>();
		String document = null;
		String beginSection = null;
		String endSection = null;
		int startOffset = 0;
		int endOffset = 0;
		String text = null;
		for (JsonElement element : annotations)
		{
			JsonObject annotation = element.getAsJsonObject();
			if (annotation.get("type").getAsString().equals("snippet"))
			{
				document = annotation.get("document").getAsString();
				beginSection = annotation.get("beginFieldName").getAsString();
				endSection = annotation.get("endFieldName").getAsString();
				startOffset = annotation.get("beginIndex").getAsInt();
				endOffset = annotation.get("endIndex").getAsInt();
				text = annotation.get("text").getAsString();
				snippets.add(new Snippet(document, beginSection, endSection, startOffset, endOffset, text));
			}
		}
		return snippets;
	}
	
	public ArrayList<Snippet> getSnippets2(JsonArray snippetsArray)
	{
		ArrayList<Snippet> snippets = new ArrayList<>();
		String document = null;
		String beginSection = null;
		String endSection = null;
		int startOffset = 0;
		int endOffset = 0;
		String text = null;
		for (JsonElement snippetElement : snippetsArray)
		{
			JsonObject snipppet = snippetElement.getAsJsonObject();
			document = snipppet.get("document").getAsString();
			beginSection = snipppet.get("beginSection").getAsString();
			endSection = snipppet.get("endSection").getAsString();
			startOffset = snipppet.get("beginIndex").getAsInt();
			endOffset = snipppet.get("endIndex").getAsInt();
			text = snipppet.get("text").getAsString();
			snippets.add(new Snippet(document, beginSection, endSection, startOffset, endOffset, text));
		}
		return snippets;
	}
	
	public ArrayList<Snippet> getSnippetsForPostAgreement(JsonArray annotations)
	{
		ArrayList<Snippet> snippets = new ArrayList<>();
		String document = null;
		String beginSection = null;
		String endSection = null;
		int startOffset = 0;
		int endOffset = 0;
		String text = null;
		for (JsonElement element : annotations)
		{
			JsonObject annotation = element.getAsJsonObject();
			if (annotation.get("type").getAsString().equals("snippet"))
			{
				if(annotation.get("golden").getAsBoolean())
				{
					document = annotation.get("document").getAsString();
					beginSection = annotation.get("beginSection").getAsString();
					endSection = annotation.get("endSection").getAsString();
					startOffset = annotation.get("beginIndex").getAsInt();
					endOffset = annotation.get("endIndex").getAsInt();
					text = annotation.get("text").getAsString();
					snippets.add(new Snippet(document, beginSection, endSection, startOffset, endOffset, text));
				}
			}
		}
		return snippets;
	}
	
	public ArrayList<String> getSimpleAnnotationsForBaseline(JsonArray annotations)
	{
		ArrayList<String> simpleAnnottations = new ArrayList<>();
		if (annotations != null)
		{
			for (JsonElement element : annotations)
			{
				simpleAnnottations.add(element.getAsString());
			}
		}
		return simpleAnnottations;
	}
	
	public ArrayList<String> getTriplesForBaseline(JsonArray annotations)
	{
		ArrayList<String> simpleAnnottations = new ArrayList<>();
		if (annotations != null)
		{
			for (JsonElement element : annotations)
			{
				simpleAnnottations.add(element.getAsJsonObject().get("s").getAsString());
			}
		}
		return simpleAnnottations;
	}
	
	public ArrayList<Snippet> getSnippetsForBaseline(JsonArray annotations)
	{
		ArrayList<Snippet> snippets = new ArrayList<>();
		String document = null;
		String beginSection = null;
		String endSection = null;
		int startOffset = 0;
		int endOffset = 0;
		String text = null;
		if (annotations != null)
		{
			for (JsonElement element : annotations)
			{
				JsonObject annotation = element.getAsJsonObject();
				document = annotation.get("document").getAsString();
				beginSection = annotation.get("beginSection").getAsString();
				beginSection = annotation.get("endSection").getAsString();
				startOffset = annotation.get("offsetInBeginSection").getAsInt();
				endOffset = annotation.get("offsetInEndSection").getAsInt();
				text = annotation.get("text").getAsString();
				snippets.add(new Snippet(document, beginSection, endSection, startOffset, endOffset, text));
			}
		}
		return snippets;
	}
}
