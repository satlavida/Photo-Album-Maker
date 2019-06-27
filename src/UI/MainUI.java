package UI;

import java.io.IOException;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;


public class MainUI extends Application {

	@Override
	public void start(Stage stg) throws Exception {
		makeUI(stg);
		
	}
	
	private void makeUI(Stage stage) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("MainUI.fxml"));
		Scene sc = new Scene(root,700,500);
		stage.setTitle("Photos");
		stage.setScene(sc);
		stage.show();
		
     
    }
	
	public static void main(String args[])
	{
		launch(args);
	}
	
	@Override
	public void stop()
	{
		
	}
}



/*import Backend.*; 

public class Testing {
	//public static void main(String args[])
	//{
		//Data d = new Data();
		//d.all.getPhotos().add(new Photo("Nice photo", "Some caption"));
		//d.save();
		//d.load();
		//System.out.println(d.all.getPhotos().get(0).getCaption()); 
		
	//}
}*/

