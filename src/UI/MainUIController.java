package UI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import Backend.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainUIController {
	public ListView<HBox> navlist;
	public ListView<HBox> images;
	ObservableList<HBox> ima; //image List View
	ObservableList<HBox> albums;
	Data d;
	UUID albumTab;
	
	public void initialize()
	{
		HBox photosTab = new HBox(new Label("Photos"));
		photosTab.setUserData(null);
		photosTab.setOnMouseClicked((ac) -> {
			navigate(ac);
		});
		albums = FXCollections.observableArrayList(photosTab);
		navlist.setItems(albums);
		ima = FXCollections.observableArrayList(); 
		d = new Data();
		loadData();
		albumTab = null;
		addAllPhotos();
		if(d.all.getAlbums().size() > 0)
		{
			d.all.getAlbums().forEach((alb) -> {
				this.addAlbumItem(alb.getName(),alb.id);
			});
		}
	}
	
	
	public void loadData()
	{
		try
		{
			d.load(); 
		}
		catch(Exception e)
		{
			System.out.println("No data found");
			d.save();
		}
	}
	
	public void addFromAlbums(Albums alb)
	{
		images.getItems().clear();
		System.out.println(alb.getPhotos().size());
		for(int i = 0; i<alb.getPhotos().size(); i++ )
		{
			Photo p = alb.getPhotos().get(i);
			addPhotoToList(p);
		}
		images.setItems(ima);
	}
	
	
	
	public void addAllPhotos()
	{
		ima.clear();
		System.out.println(d.all.getPhotos().size());
		for(int i = 0; i<d.all.getPhotos().size(); i++ )
		{
			Photo p = d.all.getPhotos().get(i);
			addPhotoToList(p);
		}
		images.setItems(ima);
	}
	
	public void addItemsMenu()
	{
		if(albumTab == null)
		{
			
			FileChooser file = new FileChooser(); 
			file.setTitle("add photos"); 
			
			List<File> files = file.showOpenMultipleDialog(new Stage());
			try {
				if(files.size()>0)
				{
					files.forEach((File f)->{
						Photo p = new Photo(f.getName(), "");
						p.id = UUID.randomUUID();
						p.setFile(f.getPath());
						d.all.getPhotos().add(p);
						addPhotoToList(p);
					});
					
					d.save();
				}
			}
			catch(Exception e)
			{
				
			}
		}
		else
		{
			//Add items to album
			ArrayList<Photo> phts = d.all.getPhotos(); 
			ListView<ImageView> list =  new ListView<ImageView>();
			list.setPrefHeight(300);
			list.setPrefWidth(400);
			phts.forEach((p)-> {
				Image im = new Image("file:"+p.getFile().getPath());
				ImageView imv = new ImageView(im);
				imv.setUserData(p.id);
				//Set Size
				imv.setFitWidth(100);
				imv.setFitHeight(100);
				list.getItems().add(imv);
			});
			list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			//Create Dialog
			Dialog<String> de = new Dialog<String>();
			Button button = new Button("OK");
			button.setOnAction((e) -> {
				de.setResult("Done");
				de.close(); 
			});
			VBox v = new VBox(list, button);
			de.getDialogPane().setContent(v); 
			de.showAndWait().ifPresent((o)->{
				MultipleSelectionModel<ImageView> selectedItems = list.getSelectionModel();
				Albums currentAlbum = this.d.all.getAlbumsByID(albumTab); 
				selectedItems.getSelectedItems().forEach((p) -> {
					UUID id =  (UUID)p.getUserData();
					Photo pe = d.all.getPhoto(id);
					currentAlbum.addPhoto(pe);
					addPhotoToList(pe);
				});
			});
		}
	}
	
	public void addAlbum()
	{
		TextInputDialog albumSetter = new TextInputDialog("Enter album name");
		Optional<String> res =  albumSetter.showAndWait();  
		if (res.isPresent() && res.get() != null) {
			String name = res.get();
			UUID id = d.all.addAlbum(name); 
			addAlbumItem(name,id);
			d.save();
		}
	}
	
	public void addAlbumItem(String name,UUID id)
	{
		HBox navListItem = new HBox(new Label(name));
		albums.add(navListItem);
		navListItem.setUserData(id);
		navListItem.setOnMouseClicked((ac) -> {
			navigate(ac);
		}); 
	}
	
	public void navigate(MouseEvent ac) 
	{
		UUID id = (UUID)((HBox)ac.getSource()).getUserData();
		albumTab = id;
		if(id == null)
		{
			addAllPhotos();
		}
		else
		{	
			Albums alb = d.all.getAlbumsByID(id); 
			addFromAlbums(alb);
		}
	}
	
	private void addPhotoToList(Photo p)
	{
		//Create Image
		Image im = new Image("file:"+p.getFile().getPath());
		ImageView imv = new ImageView(im);
		//Set Size
		imv.setFitWidth(100);
		imv.setFitHeight(100);
		imv.setCache(true);
		//Caption 
		TextArea caption = new TextArea(p.getCaption());
		caption.setEditable(false);
		caption.setMaxHeight(60);
		caption.setMaxWidth(190);
		//Edit button
		Button edit = new Button("Edit"); 
		edit.setUserData(p.id);
		edit.setOnAction((actionEvent)-> {
			editPhoto(actionEvent);
		});
		//Name
		Label name = new Label(p.getName());
		//Remove photo button
		Button cancel = new Button("Remove");
		cancel.setUserData(p.id);
		cancel.setOnAction(actionEvent ->{
			removePhoto(actionEvent);
		});
		
		//Adding elements vertically
		VBox v = new VBox(name,caption,cancel,edit);
		//Adding elements Horizontally
		HBox h = new HBox(imv,v);
		imv.setOnMouseClicked(actionEvent -> {imagePress(actionEvent);});
		imv.setUserData(p.id);
		h.setSpacing(20);
		ima.add(h);
	}
	
	public void imagePress(MouseEvent mouseEvent)
	{
		
		ImageView src = ((ImageView)mouseEvent.getSource());
		ImageView imv =  new ImageView(src.getImage());
		imv.preserveRatioProperty();
		imv.setFitHeight(540);
		imv.setFitWidth(540);
		UUID photoID = (UUID)src.getUserData();
		imv.setUserData(photoID); 
		Photo p = d.all.getPhoto(photoID);
		Label caption = new Label(p.getCaption());
		Scene sc = new Scene(new VBox(imv,caption)); 
		Stage stg = new Stage();
		stg.setScene(sc);
		stg.setTitle(p.getName());
		stg.show();
	}
	
	public void removePhoto(ActionEvent e)
	{
		try {
			HBox item = (HBox)((Button)e.getSource()).getParent().getParent();
			UUID itemID = (UUID)((Button)e.getSource()).getUserData();
			Alert ask = new Alert(AlertType.CONFIRMATION,"Are you sure you want to delete the photo");
			Optional<ButtonType> res =  ask.showAndWait();  
			if (res.isPresent() && res.get() == ButtonType.OK) {
				ima.remove(item);
				if(albumTab == null)
				{	
					d.all.removePhoto(itemID);
					d.save();
				}
				else
				{
					d.all.getAlbumsByID(albumTab).removePhoto(itemID);
				}
			}
		}
		catch(Exception es)
		{
			es.printStackTrace();
		}
		
	}
	
	public void editPhoto(ActionEvent e)
	{
		Button source = ((Button)e.getSource());
		VBox item = (VBox)(source).getParent();
		UUID id = (UUID)source.getUserData();
		TextArea edit = (TextArea)item.getChildren().get(1); 
		if(source.getText().equals("Edit"))
		{
			edit.setEditable(true);
			source.setText("Done");
		}
		else
		{
			source.setText("Edit");
			edit.setEditable(false);
			Photo p  = d.all.getPhoto(id);
			p.setCaption(edit.getText());
			d.save();
		}
	}
	
	public void exportAlbum(ActionEvent e)
	{
		if(albumTab == null)
			new Alert(AlertType.INFORMATION,"No album selected. Select Album in side list",ButtonType.OK).show();
		else
		{
			Albums album = d.all.getAlbumsByID(albumTab); 
			ArrayList<Photo> photos = album.getPhotos();
			StringBuilder s = new StringBuilder(); 
			s.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n" + 
					"<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + 
					" <head>\r\n" + 
					"  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\r\n" + 
					"  <title>" + album.getName() + "</title>");
			s.append("<body style=\"margin: 0; padding: 0;\">\r\n" +
					" <table align=\"center\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">"); 
			s.append("<tr><td><h2>"+album.getName()+"</h2></td></tr>");
			photos.forEach((p) -> {
				s.append("  <tr>\r\n" + 
						"   <td>\r\n" + 
						"    <img src=\"data:image/"+getFileType(p.getFile())+";base64,"+encodeFileToBase64Binary(p.getFile()));
				s.append("\"/><br><p>Caption:  "+p.getCaption());
				s.append("	</td>"+
						"	</tr>");
			}); 
			s.append("</table></body></html>");
			
			FileChooser  f = new FileChooser(); 
			f.setTitle("Choose a folder to save html file");
			f.setInitialFileName(album.getName().concat(".html"));
			File save = f.showSaveDialog(new Stage());
			
			try {
				FileOutputStream fos = new FileOutputStream(save);
				fos.write(s.toString().getBytes());
				fos.close();
			}
		 	catch (IOException eee) {
		 		eee.printStackTrace();
		 	}
		}
	}
	
	  private static String encodeFileToBase64Binary(File file){
          String encodedfile = null;
          try {
              FileInputStream fileInputStreamReader = new FileInputStream(file);
              byte[] bytes = new byte[(int)file.length()];
              fileInputStreamReader.read(bytes);
              encodedfile = new String(Base64.getEncoder().encode(bytes));
              fileInputStreamReader.close();
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          }

          return encodedfile;
	  }
	  
	  private static String getFileType(File file) {
		  String type=null; 
		  String name = file.getName();
		  type = name.substring(name.indexOf('.')+1);
		  return type; 
	  }
	
	
}
