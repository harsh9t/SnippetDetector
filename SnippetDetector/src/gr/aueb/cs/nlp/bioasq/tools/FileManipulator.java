package gr.aueb.cs.nlp.bioasq.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileManipulator
{
	
	public static void restructureFolder()
	{
		File currentDirectory = new File("Docs");
		
		for (File documentsDir : currentDirectory.listFiles(new FileFilter()
		{
		
			@Override
			public boolean accept(File arg0)
			{
				return arg0.isDirectory();
			}
		}))
		{
			File [] documentDirs = documentsDir.listFiles(new FileFilter()
				{
			
					@Override
					public boolean accept(File arg0)
					{
						return arg0.isDirectory();
					}
				});
			for (int i = 0; i < documentDirs.length; i++)
			{
				try
				{
					File newFile = new File(documentDirs[i].getCanonicalPath() + ".i");
					documentDirs[i].renameTo(newFile);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		
		for (File documentsDir : currentDirectory.listFiles(new FileFilter()
			{
			
				@Override
				public boolean accept(File arg0)
				{
					return arg0.isDirectory();
				}
			}))
		{
			for (File documentDir : documentsDir.listFiles(new FileFilter()
				{
				
					@Override
					public boolean accept(File arg0)
					{
						return arg0.isDirectory();
					}
				}))
			{
				try
				{
					Path source = Paths.get(documentDir.getCanonicalPath(), documentDir.getName().substring(0, documentDir.getName().indexOf(".")));
					Path destination = Paths.get(documentsDir.getCanonicalPath(), documentDir.getName().substring(0, documentDir.getName().indexOf(".")));
					Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
					for (File file : documentDir.listFiles())
					{
						Files.delete(file.toPath());
					}
					Files.delete(documentDir.toPath());
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		restructureFolder();
	}
	
}
