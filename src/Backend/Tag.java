package Backend;

import java.io.Serializable;

public class Tag implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1425776983902026198L;
	String name;
	
	public String getTag()
	{
		return name; 
	}
	
	public void setTag(String tag)
	{
		name = tag;
	}
	
}
