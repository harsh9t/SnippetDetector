package gr.aueb.cs.nlp.similarity.string;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

		String s1 = "Kubric directed the film Shine";
		String s2 = "Shine was directed by Kubric";//s2 = s1;
		boolean greek = false;
		boolean perChar = false;
		boolean useSynonyms = true;
		
		double similarity = Manhattan.getSimilarity(s1, s2, useSynonyms, greek);
		System.out.println("Manhattan similarity: " + similarity);
		
		similarity = Levenshtein.getSimilarity(s1, s2, perChar, useSynonyms, greek);
		System.out.println("Levenshtein similarity: " + similarity);
		
		similarity = Cosine.getSimilarity(s1, s2, useSynonyms, greek);
		System.out.println("Cosine similarity: " + similarity);
		
		similarity = Euclidean.getSimilarity(s1, s2, useSynonyms, greek);
		System.out.println("Euclidean similarity: " + similarity);
		
		similarity = MatchingCoefficient.getSimilarity(s1, s2, useSynonyms, greek);
		System.out.println("Matching Coefficient similarity: " + similarity);
		
		similarity = DiceCoefficient.getSimilarity(s1, s2, useSynonyms, greek);
		System.out.println("Dice Coefficient similarity: " + similarity);
		
		similarity = JaccardCoefficient.getSimilarity(s1, s2, useSynonyms, greek);
		System.out.println("Jaccard Coefficient similarity: " + similarity);
		
		similarity = Jaro.getSimilarity(s1, s2, perChar, useSynonyms, greek);
		System.out.println("Jaro similarity: " + similarity);
		
		similarity = JaroWinkler.getSimilarity(s1, s2, perChar, useSynonyms, 6, 0.1, greek);
		System.out.println("Jaro Winkler similarity: " + similarity);
	}

}
