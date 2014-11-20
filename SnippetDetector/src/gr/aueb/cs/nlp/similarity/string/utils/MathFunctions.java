package gr.aueb.cs.nlp.similarity.string.utils;

public class MathFunctions
{
	public static int min_of_3(int a, int b, int c)
	{
		int min = a < b ? a : b;
		min = (c < min) ? c : min;
		return min;
	}
}
