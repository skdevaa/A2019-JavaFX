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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;




/**
 * @author Stefan Karsten
 *
 */

@BotCommand
@CommandPkg(label="Two Buttons", name="TwoButtons", description="Two Buttons", icon="",
node_label="Two ButtonsI",
return_type=STRING, return_label="Show Two Buttons", return_required=true)
public class TwoButtons  {
	
	private String pressed = null;
	private static Semaphore sem;
	private JFrame frame = null;
	
	@Execute
	public Value<String> action(@Idx(index = "1", type = TEXT) @Pkg(label = "Title", default_value_type = STRING) @NotEmpty String title ,
								@Idx(index = "2", type = TEXT) @Pkg(label = "Label", default_value_type = STRING) @NotEmpty String label ,
								@Idx(index = "3", type = TEXT) @Pkg(label = "Button1", default_value_type = STRING) @NotEmpty String button1, 
		  						@Idx(index = "4", type = TEXT) @Pkg(label = "Button2", default_value_type = STRING) @NotEmpty String button2) throws Exception {
		
		
		  if (this.sem == null) {
		      this.sem = new Semaphore(1);
		    }
		  String[] args = null;
		  

		  initAndShowGUI(title,label,button1,button2);
		   this.sem.acquire();
		   this.sem.release();
    
		return new StringValue(this.pressed);
	}


    private  void initAndShowGUI(String title,String label, String button1, String button2) throws Exception  {
        // This method is invoked on the JavaFX thread
    	 this.sem.acquire();
         this.frame = new JFrame(title);  
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
	                initFX(fxPanel,label,button1,button2);
	            }
	       });
	        
    }

  private  void initFX(JFXPanel fxPanel,String label, String button1, String button2) {
	  
    
	  	 Label lab = new Label(label);
    	 Button but1= new Button(button1);
    	 Button but2 = new Button(button2);
    	 
 	    lab.setStyle("-fx-font-size: 12pt;");
 	    but1.setStyle("-fx-font-size: 12pt;");
 	    but2.setStyle("-fx-font-size: 12pt;");
    	 
    	 but1.setOnAction((e)->{
    		 this.frame.setVisible(false);
    	     this.pressed = button1;
    	     quit();

    	 });
    	 but2.setOnAction((e)->{
    		 this.frame.setVisible(false);
    	     this.pressed = button2;
    	     quit();

    	 });
    	 
    	 GridPane grid = new GridPane();
    	 grid.setAlignment(Pos.CENTER);
    	 grid.setHgap(10);
    	 grid.setVgap(12);
    	 
    	 GridPane innergridlabel = new GridPane();
    	 innergridlabel.setAlignment(Pos.CENTER);
    	 innergridlabel.add(lab, 1,0);
    	 grid.add(innergridlabel,1,1);
    	    
    	 HBox hbButtons = new HBox();
    	 hbButtons.getChildren().addAll(but1,but2);
    	 hbButtons.setSpacing(10.0);    	    
    	 GridPane innergridbut = new GridPane();
    	 innergridbut.setAlignment(Pos.CENTER);
    	 innergridbut.add(hbButtons, 1,0);
    	 grid.add(innergridbut,1,2);
    	    
    	 grid.setPrefWidth(400);

    	 Scene  scene  =  new  Scene(grid, Color.WHITE);

         fxPanel.setScene(scene);
    	 
   
  	 }


	public  void quit() {
		 this.sem.release();
	}
}

	


