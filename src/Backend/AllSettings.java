package Backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class AllSettings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4348918297950926444L;
	
	ArrayList<Photo> photos; 
	
	ArrayList<Albums> albums;
	
	
	public AllSettings()
	{
		photos = new ArrayList<Photo>();
		albums = new ArrayList<Albums>();
	}
	
	public ArrayList<Photo> getPhotos()
	{
		return photos;
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
	
	public ArrayList<Albums> getAlbums()
	{
		return albums;
	}
	
	public UUID addAlbum(String r)
	{
		Albums n = new Albums(r);
		albums.add(n);
		return n.id;
	}
	
	
	public Photo getPhoto(UUID id)
	{
		Photo toFind = null;
		for(int i = 0; i< photos.size(); i++)
		{
			toFind = photos.get(i);
			if(toFind.id.equals(id))
			{
				break;
			}
		}
		return toFind;
	}

	public Albums getAlbumsByID(UUID id) {
		Albums toFind = null;
		for(int i = 0; i< albums.size(); i++)
		{
			toFind = albums.get(i);
			if(toFind.id.equals(id))
			{
				break;
			}
		}
		return toFind;
		
	}

}
