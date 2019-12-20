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
import static com.automationanywhere.commandsdk.model.DataType.BOOLEAN;

import java.io.File;
import java.util.concurrent.Semaphore;
import javax.swing.JFrame;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DateTimeValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.commandsdk.annotations.BotCommand;
import com.automationanywhere.commandsdk.annotations.CommandPkg;
import com.automationanywhere.commandsdk.annotations.Execute;
import com.automationanywhere.commandsdk.annotations.Idx;
import com.automationanywhere.commandsdk.annotations.Pkg;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;

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
@CommandPkg(label="Date Picker", name="DatePicker", description="Choose a Date", icon="",
node_label="DatePicker", return_type=DataType.DATETIME, return_label="Date", return_required=true)
public class DatePicker  {
	
	private static Semaphore sem;
	private JFrame frame = null;
	private String dirname="";
	private DateTimeValue datevalue;
	
	@Execute
	public DateTimeValue action(@Idx(index = "1", type = AttributeType.CHECKBOX) @Pkg(label = "Start-End Date", default_value_type = BOOLEAN) @NotEmpty Boolean startend
					   ) throws Exception
	{
		
		this.datevalue = new DateTimeValue();

		  if (this.sem == null) {
		      this.sem = new Semaphore(1);
		    }

		  initAndShowGUI(startend);
		   this.sem.acquire();
		   this.sem.release();
		   
		   return this.datevalue;
    
	}


    private  void initAndShowGUI(Boolean startend) throws Exception  {
        // This method is invoked on the JavaFX thread
        // This method is invoked on the JavaFX thread
    	 this.sem.acquire();
         this.frame = new JFrame("Date Pickerr");  
         this.frame.addWindowListener(new java.awt.event.WindowAdapter() {
             @Override
             public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            	 quit();
             }
         });

	        final JFXPanel fxPanel = new JFXPanel();
	        frame.add(fxPanel);
	        frame.setSize(500,200);
	        frame.setLocation(700, 500);
	        frame.setVisible(true);
	        frame.setAlwaysOnTop(true);
	        Platform.runLater(new Runnable() {
	            @Override
	            public void run() {
	        	  try {
					initFX(fxPanel,startend);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            }
	       });
	        
	        
    }

  private  void initFX(JFXPanel fxPanel,Boolean startend) throws Exception {
	  
	  
	  GridPane grid = new GridPane();    	
	  grid.setHgap(10);
      grid.setVgap(12);
      grid.setAlignment(Pos.CENTER);
  

	  javafx.scene.control.DatePicker datepicker1 = new javafx.scene.control.DatePicker();
	  javafx.scene.control.DatePicker datepicker2 = new javafx.scene.control.DatePicker();
	  datepicker1.setStyle("-fx-font-size: 12pt;");
	  datepicker2.setStyle("-fx-font-size: 12pt;");
	  
 	  Label label1 = new Label();
 	  label1.setStyle("-fx-font-size: 12pt;");
 	  label1.setText("Start");
 	  Label label2 = new Label();
 	  label2.setStyle("-fx-font-size: 12pt;");
 	  label2.setText("End");
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
 	  
 	  GridPane innergridstart = new GridPane();
 	  innergridstart.setAlignment(Pos.CENTER);
      HBox hbstart = new HBox();
 	  hbstart.getChildren().addAll(label1,datepicker1);
 	  hbstart.setSpacing(10.0);
 	  innergridstart.add(hbstart, 0,1);
 	  
 	  GridPane innergridend = new GridPane();
 	  innergridend.setAlignment(Pos.CENTER);
      HBox hbend = new HBox();
 	  hbend.getChildren().addAll(label2,datepicker2);
 	  hbend.setSpacing(10.0);
 	  innergridend.add(hbend, 0,1);

 	  if (startend)
 	  {
 		  grid.add(innergridstart, 0,0);
 		  grid.add(innergridend, 0,1);
 	 	  grid.add(innergridbut, 0,2);

 	  }
 	  else
 	  {
 		  grid.add(innergridstart, 0,1);
 	 	  grid.add(innergridbut, 0,2);

 	  }
 	  
    	 
      Scene  scene  =  new  Scene(grid, Color.WHITE);
      fxPanel.setScene(scene);
     
      
	  datepicker1.setOnAction((e)->{

		 });

	  datepicker2.setOnAction((e)->{

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
  

	private  void quit() {
		 this.sem.release();
	}
}

	


