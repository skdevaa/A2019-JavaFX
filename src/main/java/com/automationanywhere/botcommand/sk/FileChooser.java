/*
 * Copyright (c) 2019 Automation Anywhere.
 * All rights reserved.
 *
 * This software is the proprietary information of Automation Anywhere.
 * You shall use it only in accordance with the terms of the license agreement
 * you entered into with Automation Anywhere.
 */
/**
 * 
 */
package com.automationanywhere.botcommand.sk;

import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;


import java.io.File;
import java.net.URL;
import java.util.concurrent.Semaphore;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;


import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Window;





/**
 * @author Stefan Karsten
 *
 */

@BotCommand
@CommandPkg(label="File Chooser", name="FileChooser", description="Choose File",  icon="jfx.svg",
node_label="FileChooser", return_type=STRING, return_label="File", return_required=true)
public class FileChooser  {
	
	private static Semaphore sem;
	private FXWindow window;
	private String filename="";
	private String directory = null;
	private String message = "";
	
	@Execute
	public Value<String> action(@Idx(index = "1", type = TEXT) @Pkg(label = "Message", default_value_type = STRING) @NotEmpty String message,
					   @Idx(index = "2", type = TEXT) @Pkg(label = "Initial Directory", default_value_type = STRING)  String dir) throws Exception
	{
		
		

		  if (this.sem == null) {
		      this.sem = new Semaphore(1);
		    }
		  
		 

		  this.directory = (dir == null || dir.equals("")) ? null : dir;
		  this.message = message;
	      this.sem.acquire();
	      
	      window = new FXWindow("File Chooser",800,300);
	      window.getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
	             @Override
	             public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	            	 quit();
	             }
	         });
		  Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		        	  try {
						initFX();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            }
		       });
		        
		   this.sem.acquire();
		   this.sem.release();
		   
		   return (new StringValue(this.filename));
    
	}



  private  void initFX() throws Exception {
	  

	  javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
	  if (this.directory != null)
	  {
		  fileChooser.setInitialDirectory(new File(this.directory));
	  }
	  
	  GridPane grid = new GridPane();    	
	  grid.setHgap(10);
      grid.setVgap(12);
      grid.setAlignment(Pos.CENTER);
  
 	  Label label = new Label();
 	  label.setText(this.message);
 	  TextField textElement = new TextField ();  
 	  textElement.setPrefWidth(500);
 	  Button browse= new Button("Browse");
 	  Button ok= new Button("OK");
 	  Button cancel= new Button("Cancel");
 	  GridPane innergridbut = new GridPane();
 	  innergridbut.setAlignment(Pos.CENTER);
      HBox hbButtons = new HBox();
 	  hbButtons.getChildren().addAll(ok,cancel);
 	  hbButtons.setSpacing(10.0);
 	  innergridbut.add(hbButtons, 0,1);
 	  grid.add(label, 0,0);
 	  grid.add(textElement, 0,1);
 	  grid.add(browse, 1,1);
 	  grid.add(innergridbut, 0,2);
 	  
    	 
      Scene  scene  =  new  Scene(grid, Color.WHITE);
 	  URL url = this.getClass().getResource("/css/styles.css");
	  scene.getStylesheets().add(url.toExternalForm());
 	  window.getPanel().setScene(scene);
     

  	 browse.setOnAction((e)->{
  		 Window win =  window.getPanel().getScene().getWindow();
  		window.getFrame().setAlwaysOnTop(false);
		File file = fileChooser.showOpenDialog(win);
		window.getFrame().setAlwaysOnTop(true);
		setFileName(file.getAbsolutePath());
		textElement.setText(file.getAbsolutePath());

 	 });
      
 	 ok.setOnAction((e)->{
		 window.getFrame().setVisible(false);
	     quit();

	 });
 	 cancel.setOnAction((e)->{
 		window.getFrame().setVisible(false);
	     quit();

	 });
      




   }
  
	private  void setFileName(String filename) {
		 this.filename = filename;
	}


	private  void quit() {
		 this.sem.release();
	}
}

	


