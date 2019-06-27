package Helper;
import Backend.Photo;
import java.util.Comparator;

public class PhotoIDComparator implements Comparator<Photo> {

	public int compare(Photo a1, Photo a2) {
		return a1.id.compareTo(a2.id);
	}

}
