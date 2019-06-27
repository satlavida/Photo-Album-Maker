package Backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Albums implements Serializable{
	
	private static final long serialVersionUID = 6804874605520889909L;
	
	private ArrayList<Photo> photos;
	
	public UUID id; 
	
	private String name; 
	
	public ArrayList<Photo> getPhotos()
	{
		return photos;
	}
	
	public String getName()
	{
		return name; 
	}
	
	public Albums(String n)
	{
		this.name = n;
		this.photos = new ArrayList<Photo>();
		id = UUID.randomUUID();
	}

	public void addPhoto(Photo photo) {
		photos.add(photo);
	}
	
	public void removePhoto(UUID id)
	{
		Photo toRemove = null;
		for(int i = 0; i< photos.size(); i++)
		{
			if(photos.get(i).id.equals(id))
			{
				toRemove = photos.get(i);
				break;
			}
		}
		if(toRemove != null)
			photos.remove(toRemove);
	}

}
