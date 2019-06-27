package Backend;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Data {

	public AllSettings all;
	
	public Data()
	{
		all = new AllSettings();
	}
	
	
	public void load() throws IOException, ClassNotFoundException
	{
		try
		{
			FileInputStream ios = new FileInputStream("data.ser");
			ObjectInputStream ois = new ObjectInputStream(ios);
			all = (AllSettings)ois.readObject();
			ois.close();
			ios.close();
			
		}
		catch(FileNotFoundException e)
		{
			this.save();
		}

	}
	
	public void save()
	{
		try
		{
			FileOutputStream fos = new FileOutputStream("data.ser");
			ObjectOutputStream os = new ObjectOutputStream(fos);
			
			os.writeObject(all);
			os.close();
			fos.close();
			System.out.println("data has been saved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
