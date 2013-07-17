package com.therdl.client.view.widget.editor;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.therdl.client.view.widget.SnipEditorWorkflow;

public class DynamicPostListBox extends Composite {



    final SnipEditorWorkflow snipEditorWorkflow;

    private String title = "Select a Post";

    private ListBox dropBox;

    public DynamicPostListBox(final SnipEditorWorkflow snipEditorWorkflow) {
        this.snipEditorWorkflow = snipEditorWorkflow;
        dropBox = new ListBox(false);

        FlowPanel dropBoxPanel = new FlowPanel();
        dropBoxPanel.add(new HTML(title));
        dropBoxPanel.add(dropBox);
        initWidget(dropBoxPanel);
        dropBoxPanel.setStyleName("postListBoxRightBar");

        dropBox.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {


             int k = dropBox.getSelectedIndex();
             String content =  dropBox.getValue(k);

             snipEditorWorkflow.setContent(content);


            }
        });


    }


    public void addItem(String key, String value) {

            dropBox.addItem(key, value );



    }

}
