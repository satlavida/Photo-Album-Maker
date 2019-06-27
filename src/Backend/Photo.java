package Backend;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Photo implements Serializable {

	/**
	 * 
	 */
	
	public UUID id; 
	
	private static final long serialVersionUID = -8412360604679755599L;
	
	private String name;
	
	private ArrayList<Tag> tags;
	
	private Date date; 
	
	private String caption;
	
	private File file;
	
	public Photo(String name,String caption)
	{
		this.name = name;
		tags = new ArrayList<Tag>(); 
		this.caption = caption;
		this.date = new Date();
	}
	
	public ArrayList<Tag> getTags()
	{
		return tags; 
	}
	
	public void addTag(Tag tag)
	{
		tags.add(tag);
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String n)
	{
		name = n; 
	}
	
	public void setCaption(String n)
	{
		caption = n; 
	}
	
	public String getCaption()
	{
		return caption;
	}
	
	public void setFile(String path)
	{
		file = new File(path);
	}
	
	public File getFile()
	{
		return file; 
	}
	
	
}
