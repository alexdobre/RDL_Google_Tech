package com.therdl.client.view.widget.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.ui.client.ValueBoxEditorDecorator;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * This is the main editor in the RDL, it is the widget used to modify, create and save a Snip
 * @author Alex
 *
 */
public class SnipEditor extends Composite  {

	   interface Binder extends UiBinder<Widget, SnipEditor> {}
	  
	  @UiField ValueBoxEditorDecorator<String> title;
	  
	  @UiField
      TextBox titleBox;
	  @UiField (provided=true)RichTextArea content = new RichTextArea();

	  
	  public SnipEditor() {		   
		    initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
		  }
	  public void focus() {
		    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
		      public void execute() {
		    	  titleBox.setFocus(true);
		      }
		    });
		  }


    public String getContentAsText() {

        return content.getText();

    }

    public String getContentAsHtml() {

        return content.getHTML();

    }


    public String getTitle() {

        return  titleBox.getText();

    }

    public void setEditorTitle(String s) {

         titleBox.setText(s);

    }


    public void setContent(String s) {

        content.setHTML(s);

    }




}