package com.therdl.client.view.widget.editor;


import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;

public class CategoryListBox extends Composite {


    private	String[] categories = {" Teach & Learn (Ideas) ","Talk & Listen (Stories)", " Propose & Vote  (Improvements)",
                                                "Offer & Rate (Services)"};

    private String category = "Talk & Listen (Stories)";

    public String getCategory() {
        return showCategory(dropBox.getSelectedIndex());

    }

    private  ListBox dropBox;

    public CategoryListBox(String title)  {

        dropBox = new ListBox(false);
        for(String s : categories)  dropBox.addItem(s);


        FlowPanel dropBoxPanel = new FlowPanel();
        dropBoxPanel.add(new HTML(title));
        dropBoxPanel.add(dropBox);
        initWidget(dropBoxPanel);
        dropBoxPanel.setStyleName("postListBox");


    }
    //alphebetical
    public String showCategory( int l) {


        switch (l) {
            case 0:
                category = categories[0];
                break;
            case 1:
                category = categories[1];
                break;
            case 2:
                category = categories[2];
                break;
            case 3:
                category = categories[3];
                break;
            case 4:
                category = categories[4];
                break;

        }
        return category;

    }

    public ListBox getDropBox() {
        return dropBox;
    }






}
