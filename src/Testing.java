
import Backend.*;

public class Testing {

	public static void main(String[] args) {
		
		Data d = new Data(); 
		try
		{
			d.load();
			//d.all.getPhotos().clear();
			System.out.println(d.all.getPhotos().size());
		}
		catch (Exception e)
		{
			System.out.println("No data found");
		}
		d.all.getPhotos().clear();
		d.all.getPhotos().forEach((Photo p)->{
			System.out.println(p.getFile());
		});
		d.save();

	}

}
