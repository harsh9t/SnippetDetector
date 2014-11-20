package gr.aueb.cs.nlp.bioasq.tools;

import gr.aueb.cs.nlp.similarity.string.Levenshtein;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class Question
{
	private int id;
	private String hashId;
	private String type;
	private String body;
	private String idealAnswer;
	private ArrayList<Integer> idealAnswerScores;
	private String creator;
	private ArrayList<ArrayList<String>> exactAnswer;
	private ArrayList<String> concepts;
	private ArrayList<String> documents;
	private ArrayList<Snippet> snippets;
	private ArrayList<String> statements;
	
	public Question(int id, String hashId, String type, String body, String idealAnswer, ArrayList<Integer> idealAnswerScores, String creator, ArrayList<ArrayList<String>> exactAnswer,
			List<String> concepts, List<String> documents, List<Snippet> snippets, List<String> statements)
	{
		setId(id);
		setHashId(hashId);
		setType(type);
		setBody(body);
		setIdealAnswer(idealAnswer);
		setIdealAnswerScores(idealAnswerScores);
		setCreator(creator);
		setExactAnswer(exactAnswer);
		setConcepts(concepts);
		setDocuments(documents);
		setSnippets(snippets);
		setStatements(statements);
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getHashId()
	{
		return hashId;
	}
	
	public String getType()
	{
		return type;
	}
	
	public String getBody()
	{
		return body;
	}
	
	public String getIdealAnswer()
	{
		return idealAnswer;
	}
	
	public ArrayList<Integer> getIdealAnswerScores()
	{
		return idealAnswerScores;
	}
	
	public String getCreator()
	{
		return creator;
	}
	
	public ArrayList<ArrayList<String>> getExactAnswer()
	{
		return exactAnswer;
	}
	
	public ArrayList<String> getConcepts()
	{
		return concepts;
	}
	
	public ArrayList<String> getDocuments()
	{
		return documents;
	}
	
	public ArrayList<Snippet> getSnippets()
	{
		return snippets;
	}
	
	public ArrayList<String> getStatements()
	{
		return statements;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setHashId(String hashId)
	{
		this.hashId = hashId;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public void setBody(String body)
	{
		this.body = body;
	}
	
	public void setIdealAnswer(String idealAnswer)
	{
		this.idealAnswer = idealAnswer;
	}
	
	public void setIdealAnswerScores(ArrayList<Integer> idealAnswerScores)
	{
		this.idealAnswerScores = new ArrayList<>(idealAnswerScores);
	}
	
	public void setCreator(String creator)
	{
		this.creator = creator;
	}
	
	public void setExactAnswer(ArrayList<ArrayList<String>> exactAnswer)
	{
		this.exactAnswer = null;
		this.exactAnswer = new ArrayList<>(exactAnswer);
	}
	
	public void setExactAnswer(String exactAnswer)
	{
		this.exactAnswer = null;
		this.exactAnswer = new ArrayList<>();
		this.exactAnswer.add(new ArrayList<String>());
		this.exactAnswer.get(0).add(exactAnswer);
	}
	
	public void setConcepts(List<String> concepts)
	{
		this.concepts = null;
		this.concepts = new ArrayList<>(concepts);
	}
	
	public void setDocuments(List<String> documents)
	{
		this.documents = null;
		this.documents = new ArrayList<>(documents);
	}
	
	public void setSnippets(List<Snippet> snippets)
	{
		this.snippets = null;
		this.snippets = new ArrayList<>(snippets);
	}
	
	public void setStatements(List<String> statements)
	{
		this.statements = null;
		this.statements = new ArrayList<>(statements);
	}
	
	public void addConcept(String concept)
	{
		concepts.add(concept);
	}
	
	public void addDocument(String document)
	{
		documents.add(document);
	}
	
	public void addSnippet(Snippet snippet)
	{
		snippets.add(snippet);
	}
	
	public void addStatement(String statement)
	{
		statements.add(statement);
	}
	
	public void updateQuestionElements(Question question)
	{
		setIdealAnswer(question.idealAnswer);
		setIdealAnswerScores(question.idealAnswerScores);
		for (String concept : question.concepts)
		{
			this.addConcept(concept);
		}
		for (String document : question.documents)
		{
			this.addDocument(document);
		}
		for (Snippet snippet : question.snippets)
		{
			this.addSnippet(snippet);
		}
		for (String statement : question.statements)
		{
			this.addStatement(statement);
		}
		if (question.exactAnswer.size() > 0)
		{
			setExactAnswer(question.exactAnswer);
		}
	}
	
	@Override
	public int hashCode()
	{
		return hashId.hashCode();
	}
	
	@Override
	public boolean equals(Object arg0)
	{
		return ((hashId.equals("52eceada98d023950500002d")) && (((Question)arg0).hashId.equals("52f65f372059c6d71c000027"))) ||
				((hashId.equals("52f65f372059c6d71c000027")) && (((Question)arg0).hashId.equals("52eceada98d023950500002d"))) ||
				((hashId.equals("52f89fba2059c6d71c00004f")) && (((Question)arg0).hashId.equals("52d8466298d0239505000006"))) ||
				((hashId.equals("52d8466298d0239505000006")) && (((Question)arg0).hashId.equals("52f89fba2059c6d71c00004f"))) ||
				((hashId.equals("52f7c4bd2059c6d71c00002d")) && (((Question)arg0).hashId.equals("52ecd56198d023950500002b"))) ||
				((hashId.equals("52ecd56198d023950500002b")) && (((Question)arg0).hashId.equals("52f7c4bd2059c6d71c00002d"))) ||
				((hashId.equals("532c147bd6d3ac6a3400001e")) && (((Question)arg0).hashId.equals("534bf050aeec6fbd07000015"))) ||
				((hashId.equals("534bf050aeec6fbd07000015")) && (((Question)arg0).hashId.equals("532c147bd6d3ac6a3400001e"))) ||
				((hashId.equals("52e9f99798d0239505000025")) && (((Question)arg0).hashId.equals("52f89ee42059c6d71c00004d"))) ||
				((hashId.equals("52f89ee42059c6d71c00004d")) && (((Question)arg0).hashId.equals("52e9f99798d0239505000025"))) ||
				((hashId.equals( "52e7bbf698d023950500001d")) && (((Question)arg0).hashId.equals("52efc05cc8da89891000001c"))) ||
				((hashId.equals("52efc05cc8da89891000001c")) && (((Question)arg0).hashId.equals("52e7bbf698d023950500001d"))) ||
				((hashId.equals("52ea605098d0239505000028")) && (((Question)arg0).hashId.equals("52f637972059c6d71c000025"))) ||
				((hashId.equals("52f637972059c6d71c000025")) && (((Question)arg0).hashId.equals("52ea605098d0239505000028"))) ||
				((hashId.equals("52ecf2dd98d023950500002e")) && (((Question)arg0).hashId.equals("52f65bf02059c6d71c000026"))) ||
				((hashId.equals("52f65bf02059c6d71c000026")) && (((Question)arg0).hashId.equals("52ecf2dd98d023950500002e"))) ||
				((hashId.equals("52f7c91f2059c6d71c00002e")) && (((Question)arg0).hashId.equals("52d783bf98d0239505000001"))) ||
				((hashId.equals("52d783bf98d0239505000001")) && (((Question)arg0).hashId.equals("52f7c91f2059c6d71c00002e"))) ||
				((hashId.equals("52e203bc98d0239505000011")) && (((Question)arg0).hashId.equals("52ffbddf2059c6d71c00007d"))) ||
				((hashId.equals("52ffbddf2059c6d71c00007d")) && (((Question)arg0).hashId.equals("52e203bc98d0239505000011"))) ||
				((hashId.equals("52f8a7eb2059c6d71c000052")) && (((Question)arg0).hashId.equals("530cefaaad0bf1360c00000f"))) ||
				((hashId.equals("530cefaaad0bf1360c00000f")) && (((Question)arg0).hashId.equals("52f8a7eb2059c6d71c000052"))) ||
				((hashId.equals("52f88a062059c6d71c000032")) && (((Question)arg0).hashId.equals("530cefaaad0bf1360c00000e"))) ||
				((hashId.equals( "530cefaaad0bf1360c00000e")) && (((Question)arg0).hashId.equals("52f88a062059c6d71c000032"))) ||
				((hashId.equals("52fa73c62059c6d71c000058")) && (((Question)arg0).hashId.equals("52ec961098d023950500002a"))) ||
				((hashId.equals("52ec961098d023950500002a")) && (((Question)arg0).hashId.equals("52fa73c62059c6d71c000058"))) ||
				((hashId.equals("52d2b75403868f1b06000035")) && (((Question)arg0).hashId.equals("52fa74252059c6d71c00005b"))) ||
				((hashId.equals("52fa74252059c6d71c00005b")) && (((Question)arg0).hashId.equals("52d2b75403868f1b06000035"))) ||
				((hashId.equals("52f77f4d2059c6d71c00002a")) && (((Question)arg0).hashId.equals("52f112bb2059c6d71c000002"))) ||
				((hashId.equals("52f112bb2059c6d71c000002")) && (((Question)arg0).hashId.equals("52f77f4d2059c6d71c00002a"))) ||
				((hashId.equals("52fa73e82059c6d71c000059")) && (((Question)arg0).hashId.equals("52d1389303868f1b06000032"))) ||
				((hashId.equals("52d1389303868f1b06000032")) && (((Question)arg0).hashId.equals("52fa73e82059c6d71c000059"))) ||
				((hashId.equals("52fe52702059c6d71c000078")) && (((Question)arg0).hashId.equals("52fbe2bf2059c6d71c00006c"))) ||
				((hashId.equals("52fbe2bf2059c6d71c00006c")) && (((Question)arg0).hashId.equals("52fe52702059c6d71c000078"))) ||
				((hashId.equals("52df887498d023950500000c")) && (((Question)arg0).hashId.equals("52efc001c8da898910000019"))) ||
				((hashId.equals("52efc001c8da898910000019")) && (((Question)arg0).hashId.equals("52df887498d023950500000c"))) ||
				((hashId.equals("52ece29f98d023950500002c")) && (((Question)arg0).hashId.equals("52f8b2902059c6d71c000053"))) ||
				((hashId.equals("52f8b2902059c6d71c000053")) && (((Question)arg0).hashId.equals("52ece29f98d023950500002c"))) ||
				((hashId.equals("52e8e93498d023950500001e")) && (((Question)arg0).hashId.equals("52bf19c503868f1b06000001"))) ||
				((hashId.equals("52bf19c503868f1b06000001")) && (((Question)arg0).hashId.equals("52e8e93498d023950500001e"))) ||
				((hashId.equals("52f896d62059c6d71c000046")) && (((Question)arg0).hashId.equals("530cefaaad0bf1360c000011"))) ||
				((hashId.equals("530cefaaad0bf1360c000011")) && (((Question)arg0).hashId.equals("52f896d62059c6d71c000046"))) ||
				((hashId.equals("52fa70142059c6d71c000056")) && (((Question)arg0).hashId.equals("52cb9b9b03868f1b0600002d"))) ||
				((hashId.equals("52cb9b9b03868f1b0600002d")) && (((Question)arg0).hashId.equals("52fa70142059c6d71c000056"))) ||
				((hashId.equals("52e8e9d298d0239505000022")) && (((Question)arg0).hashId.equals("52bf1d6003868f1b0600000e"))) ||
				((hashId.equals("52bf1d6003868f1b0600000e")) && (((Question)arg0).hashId.equals("52e8e9d298d0239505000022"))) ||
				((hashId.equals("530cefaaad0bf1360c000005")) && (((Question)arg0).hashId.equals("52fc94572059c6d71c000070"))) ||
			    ((hashId.equals("52fc94572059c6d71c000070")) && (((Question)arg0).hashId.equals("530cefaaad0bf1360c000005"))) ||
			    ((hashId.equals("530cefaaad0bf1360c00000a")) && (((Question)arg0).hashId.equals("52fc94ae2059c6d71c000073"))) ||
			    ((hashId.equals("52fc94ae2059c6d71c000073")) && (((Question)arg0).hashId.equals("530cefaaad0bf1360c00000a"))) ||
			    ((hashId.equals("530cefaaad0bf1360c000006")) && (((Question)arg0).hashId.equals("52fc94932059c6d71c000072"))) ||
			    ((hashId.equals("52fc94932059c6d71c000072")) && (((Question)arg0).hashId.equals("530cefaaad0bf1360c000006"))) ||
			    ((hashId.equals("530cefaaad0bf1360c000008")) && (((Question)arg0).hashId.equals("52fc946d2059c6d71c000071"))) ||
			    ((hashId.equals("52fc946d2059c6d71c000071")) && (((Question)arg0).hashId.equals("530cefaaad0bf1360c000008"))) ||
			    ((hashId.equals("52f7d3472059c6d71c00002f")) && (((Question)arg0).hashId.equals("52e6c92598d0239505000019"))) ||
			    ((hashId.equals("52e6c92598d0239505000019")) && (((Question)arg0).hashId.equals("52f7d3472059c6d71c00002f"))) ||
			    ((hashId.equals("532c12f2d6d3ac6a3400001d")) && (((Question)arg0).hashId.equals("534bb147aeec6fbd07000014"))) ||
			    ((hashId.equals("534bb147aeec6fbd07000014")) && (((Question)arg0).hashId.equals("532c12f2d6d3ac6a3400001d"))) ||
			    ((hashId.equals("52fe4ad52059c6d71c000077")) && (((Question)arg0).hashId.equals("52e0c9a298d0239505000010"))) ||
			    ((hashId.equals("52e0c9a298d0239505000010")) && (((Question)arg0).hashId.equals("52fe4ad52059c6d71c000077"))) ||
			    ((hashId.equals("52fe58f82059c6d71c00007a")) && (((Question)arg0).hashId.equals("52ef7786c8da898910000015"))) ||
			    ((hashId.equals("52ef7786c8da898910000015")) && (((Question)arg0).hashId.equals("52fe58f82059c6d71c00007a"))) ||
			    ((hashId.equals("52f77edb2059c6d71c000028")) && (((Question)arg0).hashId.equals("52b06a68f828ad283c000005"))) ||
			    ((hashId.equals("52b06a68f828ad283c000005")) && (((Question)arg0).hashId.equals("52f77edb2059c6d71c000028"))) ||
			    ((hashId.equals("530cf4fe960c95ad0c000003")) && (((Question)arg0).hashId.equals("52e8e96698d023950500001f"))) ||
			    ((hashId.equals("52e8e96698d023950500001f")) && (((Question)arg0).hashId.equals("530cf4fe960c95ad0c000003"))) ||
			    ((hashId.equals("52bf1aa503868f1b06000006")) && (((Question)arg0).hashId.equals("52e8e99e98d0239505000021"))) ||
			    ((hashId.equals("52e8e99e98d0239505000021")) && (((Question)arg0).hashId.equals("52bf1aa503868f1b06000006"))) ||
			    ((hashId.equals("52efc016c8da89891000001a")) && (((Question)arg0).hashId.equals("52e62bae98d0239505000015"))) ||
			    ((hashId.equals("52e62bae98d0239505000015")) && (((Question)arg0).hashId.equals("52efc016c8da89891000001a"))) ||
			    ((hashId.equals("52ef7754c8da898910000014")) && (((Question)arg0).hashId.equals("52fe54252059c6d71c000079"))) ||
			    ((hashId.equals("52fe54252059c6d71c000079")) && (((Question)arg0).hashId.equals("52ef7754c8da898910000014"))) ||
			    ((hashId.equals("52efc041c8da89891000001b")) && (((Question)arg0).hashId.equals("52e7b4a198d023950500001b"))) ||
			    ((hashId.equals("52e7b4a198d023950500001b")) && (((Question)arg0).hashId.equals("52efc041c8da89891000001b"))) ||
			    ((hashId.equals("52e8e98298d0239505000020")) && (((Question)arg0).hashId.equals("52bf1af803868f1b06000008"))) ||
			    ((hashId.equals("52bf1af803868f1b06000008")) && (((Question)arg0).hashId.equals("52e8e98298d0239505000020"))) ||
			    ((hashId.equals("52d8494698d0239505000007")) && (((Question)arg0).hashId.equals("52f89f4f2059c6d71c00004e"))) ||
			    ((hashId.equals("52f89f4f2059c6d71c00004e")) && (((Question)arg0).hashId.equals("52d8494698d0239505000007"))) ||
			    ((hashId.equals("52f89fd32059c6d71c000051")) && (((Question)arg0).hashId.equals("52e929eb98d0239505000023"))) ||
			    ((hashId.equals("52e929eb98d0239505000023")) && (((Question)arg0).hashId.equals("52f89fd32059c6d71c000051"))) ||
			    ((hashId.equals("52f77f752059c6d71c00002b")) && (((Question)arg0).hashId.equals("52f118aa2059c6d71c000003"))) ||
			    ((hashId.equals("52f118aa2059c6d71c000003")) && (((Question)arg0).hashId.equals("52f77f752059c6d71c00002b"))) ||
			    ((hashId.equals("52f89fc62059c6d71c000050")) && (((Question)arg0).hashId.equals("52d63b2803868f1b0600003a"))) ||
			    ((hashId.equals("52d63b2803868f1b0600003a")) && (((Question)arg0).hashId.equals("52f89fc62059c6d71c000050"))) ||
			    ((hashId.equals("52f89a002059c6d71c00004c")) && (((Question)arg0).hashId.equals("530cefaaad0bf1360c000012"))) ||
			    ((hashId.equals("530cefaaad0bf1360c000012")) && (((Question)arg0).hashId.equals("52f89a002059c6d71c00004c"))) ||
			    ((hashId.equals("52d2818403868f1b06000033")) && (((Question)arg0).hashId.equals("52fa74052059c6d71c00005a"))) ||
			    ((hashId.equals("52fa74052059c6d71c00005a")) && (((Question)arg0).hashId.equals("52d2818403868f1b06000033"))) ||
			    ((hashId.equals("52fc94db2059c6d71c000074")) && (((Question)arg0).hashId.equals("530cefaaad0bf1360c000007"))) ||
			    ((hashId.equals("530cefaaad0bf1360c000007")) && (((Question)arg0).hashId.equals("52fc94db2059c6d71c000074"))) ||
			    ((hashId.equals("52f11bf22059c6d71c000005")) && (((Question)arg0).hashId.equals("52f77f892059c6d71c00002c"))) ||
			    ((hashId.equals("52f77f892059c6d71c00002c")) && (((Question)arg0).hashId.equals("52f11bf22059c6d71c000005"))) ||
			    ((hashId.equals("52ffb5d12059c6d71c00007c")) && (((Question)arg0).hashId.equals("52e7870a98d023950500001a"))) ||
			    ((hashId.equals("52e7870a98d023950500001a")) && (((Question)arg0).hashId.equals("52ffb5d12059c6d71c00007c"))) ||
			    ((hashId.equals("52f88d292059c6d71c000036")) && (((Question)arg0).hashId.equals("530cefaaad0bf1360c00000d"))) ||
			    ((hashId.equals("530cefaaad0bf1360c00000d")) && (((Question)arg0).hashId.equals("52f88d292059c6d71c000036"))) ||
			    ((hashId.equals("52fc8b772059c6d71c00006e")) && (((Question)arg0).hashId.equals("530cf4d5e2bfff940c000003"))) ||
			    ((hashId.equals("530cf4d5e2bfff940c000003")) && (((Question)arg0).hashId.equals("52fc8b772059c6d71c00006e"))) ||
			    ((hashId.equals("52f896ae2059c6d71c000045")) && (((Question)arg0).hashId.equals("530cefaaad0bf1360c000010"))) ||
			    ((hashId.equals("530cefaaad0bf1360c000010")) && (((Question)arg0).hashId.equals("52f896ae2059c6d71c000045"))) ||
			    ((hashId.equals("52ebb2c698d0239505000029")) && (((Question)arg0).hashId.equals("52f77f042059c6d71c000029"))) ||
			    ((hashId.equals("52f77f042059c6d71c000029")) && (((Question)arg0).hashId.equals("52ebb2c698d0239505000029"))) ||
			    ((hashId.equals("52fa6ac72059c6d71c000055")) && (((Question)arg0).hashId.equals("52ed795098d0239505000032"))) ||
			    ((hashId.equals("52ed795098d0239505000032")) && (((Question)arg0).hashId.equals("52fa6ac72059c6d71c000055")));
			
		//return hashId.equals(((Question)arg0).hashId);
		//return Levenshtein.getSimilarity(body.toLowerCase(), ((Question)arg0).body.toLowerCase(), true, false, false) > 0.8;
		//return body.toLowerCase().indexOf(((Question)arg0).body.toLowerCase()) == 0;
	}
	
	@Override
	public String toString()
	{
		String lf = System.lineSeparator();
		StringBuffer sb = new StringBuffer("Question:");
		sb.append(lf);
		sb.append("id: ");
		sb.append(id);
		sb.append(lf);
		sb.append("hash: ");
		sb.append(hashId);
		sb.append(lf);
		sb.append("type: ");
		sb.append(type);
		sb.append(lf);
		sb.append("body: ");
		sb.append(body);
		sb.append(lf);
		sb.append("ideal: ");
		sb.append(idealAnswer);
		sb.append(lf);
		sb.append("exact: ");
		sb.append(lf);
		for (ArrayList<String> exactList : exactAnswer)
		{
			for (String exact : exactList)
			{
				sb.append("\t");
				sb.append(exact);
				sb.append(lf);
			}
		}
		sb.append("concepts: ");
		sb.append(lf);
		for (String concept : concepts)
		{
			sb.append("\t");
			sb.append(concept);
			sb.append(lf);
		}
		sb.append("documents: ");
		sb.append(lf);
		for (String document : documents)
		{
			sb.append("\t");
			sb.append(document);
			sb.append(lf);
		}
		sb.append("snippets: ");
		sb.append(lf);
		for (Snippet snippet : snippets)
		{
			sb.append("\t");
			sb.append(snippet);
			sb.append(lf);
		}
		sb.append("statements: ");
		sb.append(lf);
		for (String statement : statements)
		{
			sb.append("\t");
			sb.append(statement);
			sb.append(lf);
		}
		return sb.toString();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		
	}
	
}
