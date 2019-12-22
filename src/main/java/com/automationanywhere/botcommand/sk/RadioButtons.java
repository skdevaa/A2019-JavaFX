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

import java.net.URL;
import java.util.List;
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
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;

import javafx.application.Platform;

import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;





/**
 * @author Stefan Karsten
 *
 */

@BotCommand
@CommandPkg(label="Radio Buttons", name="RadioButtons", description="Dynamic List of Radio Buttons",  icon="jfx.svg",
node_label="Radio Buttons",
return_type=STRING, return_label="Selected", return_required=true)
public class RadioButtons  {
	
	private String choosen = null;
	private static Semaphore sem;
    private String buttonlabel;
    private List<Value> options;
    private GridPane grid;
    private ToggleGroup group;
	private FXWindow window;
    
	
	@Execute
	public Value<String> action(@Idx(index = "1", type = AttributeType.LIST) @Pkg(label = "Options", default_value_type = DataType.LIST) @NotEmpty List<Value> options ,
			   @Idx(index = "2", type = TEXT) @Pkg(label = "Button Label", default_value_type = STRING) @NotEmpty String label,
			   @Idx(index = "3", type = AttributeType.NUMBER) @Pkg(label = "Width", default_value_type = DataType.NUMBER) @NotEmpty Double width,
			   @Idx(index = "4", type = AttributeType.NUMBER) @Pkg(label = "Height", default_value_type = DataType.NUMBER) @NotEmpty Double height) throws Exception
	{
		
		
		 this.options = options;
		 this.buttonlabel = label;
		 this.group = new ToggleGroup();
		 
		  if (this.sem == null) {
		      this.sem = new Semaphore(1);
		    }

	      window = new FXWindow("Radio Buttons", width.intValue(), height.intValue());
	      this.sem.acquire();
	         
	      window.getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
	             @Override
	             public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	            		quit();
	             }
	         });

		     Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		                initFX();
		            }
		       });
		  
		  
		   this.sem.acquire();
		   this.sem.release();
		   
		   
    
		return new StringValue(this.choosen);
	}



  private  void initFX() {
	  
    
	  Button but1= new Button(this.buttonlabel);
	  but1.setOnAction((e)->{
 		 window.getFrame().setVisible(false);
 	     quit();
 	  });
    	 
 	 
	 
	 this.grid = new GridPane();    	
	 this.grid.setHgap(10);
     this.grid.setVgap(12);
	 grid.setAlignment(Pos.CENTER);
	
     
	 List<Value> options = this.options;
	 int row = 0;	 
     for (Value option : options) {
    	 RadioButton optionbutton = new RadioButton(option.get().toString());
    	 optionbutton.setToggleGroup(this.group);
    	 optionbutton.setSelected(false);
    	 this.grid.add(optionbutton, 1,row);
    	 row++;

     }
     this.group.getToggles().get(0).setSelected(true); 
     GridPane innergrid = new GridPane();
	 innergrid.setAlignment(Pos.CENTER);
	 innergrid.add(but1,1,1);
	 this.grid.add(innergrid, 1,row);
	 Scene  scene  =  new  Scene(this.grid, Color.WHITE);
	 URL url = this.getClass().getResource("/css/styles.css");
     scene.getStylesheets().add(url.toExternalForm());
	 window.getPanel().setScene(scene);
    	 
   
  	 }


	private  void quit() {
		 this.choosen = ((RadioButton) this.group.getSelectedToggle()).getText();
		 this.sem.release();
	}
}

	


