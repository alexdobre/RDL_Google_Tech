package com.therdl.client.view.widget.editor;


import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;

public class DynamicPostListBox extends Composite {





    private String title = "Select a Post";

    private ListBox dropBox;

    public DynamicPostListBox() {

        dropBox = new ListBox(false);

        FlowPanel dropBoxPanel = new FlowPanel();
        dropBoxPanel.add(new HTML(title));
        dropBoxPanel.add(dropBox);
        initWidget(dropBoxPanel);
        dropBoxPanel.setStyleName("postListBoxRightBar");


    }


    public void addItem(String key, String value) {

            dropBox.addItem(key, value );
    }

}
