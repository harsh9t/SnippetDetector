package gr.aueb.cs.nlp.bioasq.tools;

import java.io.Serializable;

public class Snippet implements Serializable
{
	private String document;
	private String beginSection;
	private String endSection;
	private int startOffset;
	private int endOffset;
	private String text;
	
	public Snippet(String document, String beginSection, String endSection, int startOffset, int endOffset, String text)
	{
		setDocument(document);
		setBeginSection(beginSection);
		setEndSection(endSection);
		setStartOffset(startOffset);
		setEndOffset(endOffset);
		setText(text);
	}
	
	public Snippet(Snippet snippet)
	{
		setDocument(snippet.document);
		setBeginSection(snippet.beginSection);
		setEndSection(snippet.endSection);
		setStartOffset(snippet.startOffset);
		setEndOffset(snippet.endOffset);
		setText(snippet.text);
	}
	
	public String getDocument()
	{
		return document;
	}
	
	public String getBeginSection()
	{
		return beginSection;
	}
	
	public String getEndSection()
	{
		return endSection;
	}
	
	public int getStartOffset()
	{
		return startOffset;
	}
	
	public int getEndOffset()
	{
		return endOffset;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setDocument(String document)
	{
		this.document = document;
	}
	
	public void setBeginSection(String beginSection)
	{
		this.beginSection = beginSection;
	}
	
	public void setEndSection(String endSection)
	{
		this.endSection = endSection;
	}
	
	public void setStartOffset(int startOffset)
	{
		this.startOffset = startOffset;
	}
	
	public void setEndOffset(int endOffset)
	{
		this.endOffset = endOffset;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("Snippet:");
		sb.append(", ");
		sb.append(document);
		sb.append(", ");
		sb.append(beginSection);
		sb.append(", ");
		sb.append(endSection);
		sb.append(", ");
		sb.append(startOffset);
		sb.append(", ");
		sb.append(endOffset);
		sb.append(", ");
		sb.append(text);
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object arg0)
	{
		return this.text.equals(((Snippet)arg0).text);
	}
	
	@Override
	public int hashCode()
	{
		return this.text.hashCode();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		
	}
	
}
