package com.therdl.client.view.widget.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.ui.client.ValueBoxEditorDecorator;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.dto.SnipProxy;
import com.therdl.client.view.widget.RichTextToolbar;
import com.google.gwt.user.client.ui.RichTextArea;

/**
 * This is the main editor in the RDL, it is the widget used to modify, create and save a Snip
 * @author Alex
 *
 */
public class SnipEditor extends Composite implements Editor<SnipProxy> {
	  interface Binder extends UiBinder<Widget, SnipEditor> {
	  }
	  
	  @UiField ValueBoxEditorDecorator<String> title;
	  
	  @UiField Focusable titleBox;
	  @UiField (provided=true)RichTextArea content = new RichTextArea();
	  
	  @UiField (provided=true)RichTextToolbar richTextToolbar = new RichTextToolbar(content);
	  
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
}