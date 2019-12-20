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
import java.util.concurrent.Semaphore;
import javax.swing.JFrame;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;


import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
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
@CommandPkg(label="Directory Chooser", name="DirChooser", description="Choose File", icon="",
node_label="DirChooser", return_type=STRING, return_label="Directory", return_required=true)
public class DirChooser  {
	
	private static Semaphore sem;
	private JFrame frame = null;
	private String dirname="";
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

		  initAndShowGUI();
		   this.sem.acquire();
		   this.sem.release();
		   
		   return (new StringValue(this.dirname));
    
	}


    private  void initAndShowGUI() throws Exception  {
        // This method is invoked on the JavaFX thread
        // This method is invoked on the JavaFX thread
    	 this.sem.acquire();
         this.frame = new JFrame("Directory Chooser");  
         this.frame.addWindowListener(new java.awt.event.WindowAdapter() {
             @Override
             public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            	 quit();
             }
         });

	        final JFXPanel fxPanel = new JFXPanel();
	        frame.add(fxPanel);
	        frame.setSize(700,200);
	        frame.setLocation(700, 500);
	        frame.setVisible(true);
	        frame.setAlwaysOnTop(true);
	        Platform.runLater(new Runnable() {
	            @Override
	            public void run() {
	        	  try {
					initFX(fxPanel);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            }
	       });
	        
	        
    }

  private  void initFX(JFXPanel fxPanel) throws Exception {
	  

	  javafx.stage.DirectoryChooser dirChooser = new javafx.stage.DirectoryChooser();
	  if (this.directory != null)
	  {
		  dirChooser.setInitialDirectory(new File(this.directory));
	  }
	  
	  
	  GridPane grid = new GridPane();    	
	  grid.setHgap(10);
      grid.setVgap(12);
      grid.setAlignment(Pos.CENTER);
  
 	  Label label = new Label();
 	  label.setStyle("-fx-font-size: 12pt;");
 	  label.setText(this.message);
 	  TextField textElement = new TextField ();  
 	  textElement.setStyle("-fx-font-size: 12pt;");
 	  textElement.setPrefWidth(500);
 	  Button browse= new Button("Browse");
	  browse.setStyle("-fx-font-size: 12pt;-fx-base: #b4b4b4");
	  browse.setPrefWidth(100);
 	  Button ok= new Button("OK");
 	  ok.setPrefWidth(100);
	  ok.setStyle("-fx-font-size: 12pt;");
 	  Button cancel= new Button("Cancel");
	  cancel.setStyle("-fx-font-size: 12pt;");
 	  cancel.setPrefWidth(100);
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
      fxPanel.setScene(scene);
     

  	 browse.setOnAction((e)->{
  		 Window win =  fxPanel.getScene().getWindow();
		File dir = dirChooser.showDialog(win);
		setFileName(dir.getAbsolutePath());
		textElement.setText(dir.getAbsolutePath());

 	 });
      
 	 ok.setOnAction((e)->{
		 this.frame.setVisible(false);
	     quit();

	 });
 	 cancel.setOnAction((e)->{
		 this.frame.setVisible(false);
	     quit();

	 });
      




   }
  
	private  void setFileName(String dirname) {
		 this.dirname = dirname;
	}


	private  void quit() {
		 this.sem.release();
	}
}

	


