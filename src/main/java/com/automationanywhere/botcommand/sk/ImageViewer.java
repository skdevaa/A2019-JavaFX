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
import static com.automationanywhere.commandsdk.model.DataType.FILE;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.concurrent.Semaphore;

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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;


import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Window;





/**
 * @author Stefan Karsten
 *
 */

@BotCommand
@CommandPkg(label="Image Viewer", name="ImageViewer", description="Shows Image",  icon="jfx.svg",
node_label="ImageViewer")
public class ImageViewer  {
	
	private static Semaphore sem;
	private FXWindow window;
	private String filename="";
	private String directory = null;
	private String message = "";
	
	@Execute
	public Value<String> action(@Idx(index = "1", type = AttributeType.FILE) @Pkg(label = "Image", default_value_type = DataType.FILE) @NotEmpty String imagefile) throws Exception
	{
		
		

		  if (this.sem == null) {
		      this.sem = new Semaphore(1);
		    }
		  
		  String fileurl = Paths.get(imagefile).toUri().toURL().toString();
		  Image image = new Image(fileurl);


	      this.sem.acquire();
	      
	      window = new FXWindow("Image View",800,700);
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
		        		  ImageView imageview=new ImageView(image);
		        		  imageview.setPreserveRatio(true);
		        		  imageview.setFitHeight(window.getFrame().getWidth());
		        		  imageview.setFitWidth(window.getFrame().getHeight()-100);
		        	      Label label = new Label("Scale Image");
		        	      Slider slider = new Slider();
		        	      slider.setMin(0);
		        	        
		        	        // The maximum value.
		        	        slider.setMax(10);
		        	        // Current value
		        	        slider.setValue(1);
		        	        slider.setShowTickLabels(true);
		        	        slider.setShowTickMarks(true);
		        	        slider.setBlockIncrement(0.1);
		        	        
		        	        slider.valueProperty().addListener(new ChangeListener<Number>() {
		        	        	 
		        	            @Override
		        	            public void changed(ObservableValue<? extends Number> observable, //
		        	                  Number oldValue, Number newValue) {
		        	    
		        	               imageview.setScaleX(newValue.doubleValue());
		        	               imageview.setScaleY(newValue.doubleValue());
		        	            }
		        	         });
		        	 
		        	        VBox root = new VBox();
		        	        root.setAlignment(Pos.CENTER);
		        	        root.setPadding(new Insets(20));
		        	        root.setSpacing(10);
		        	        root.getChildren().addAll(imageview,label, slider);
			        		Scene  scene  =  new  Scene(root, Color.WHITE);
			        	 	URL url = this.getClass().getResource("/css/styles.css");
			        	 	scene.getStylesheets().add(url.toExternalForm());
			        	 	window.getPanel().setScene(scene);

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



	private  void quit() {
		 this.sem.release();
	}
}

	


