package com.therdl.client.view.widget.editor;


import com.google.gwt.user.client.ui.*;
import com.therdl.client.view.widget.SnipEditorWorkflow;

public class EditorViewHeader  extends Composite {

    SnipEditorWorkflow snipEditorWorkflow;
    private Grid grid;

    public  EditorViewHeader(SnipEditorWorkflow snipEditorWorkflow) {
        super();
        this.snipEditorWorkflow = snipEditorWorkflow;
        grid = new Grid(3,4);
        grid.setStyleName("headerGrid");


        // 1st Row
        categoryListBox = new CategoryListBox("Select a Category");
        grid.setWidget(0, 0,categoryListBox);

        newPost.setStyleName("newPost");
        grid.setWidget(0, 1,  newPost);

        delete.setStyleName("deleteButton");
        grid.setWidget(0, 2,  delete);

        submit.setStyleName("blogsubmit-Button");
        grid.setWidget(1, 0,  submit);

        submitEdit.setStyleName("blogSubmitEdit-Button");
        grid.setWidget(1, 1,  submitEdit);

        postListBox = new DynamicPostListBox(snipEditorWorkflow);
        grid.setWidget(1, 2, postListBox);


        initWidget(grid);


    }

    private Label editCatagory = new Label("Catagory");

    private Button submit = new Button("Submit");
    private Button submitEdit = new Button("Submit Edit");
    private Button delete = new Button("Delete Post");
    private Button newPost = new Button("New Post");


    private CategoryListBox categoryListBox;
    private DynamicPostListBox postListBox;


    public Label getEditCatagory() {
        return editCatagory;
    }
    public Button getSubmit() {
        return submit;
    }
    public Button getSubmitEdit() {
        return submitEdit;
    }
    public Button getDelete() {
        return delete;
    }

    public Button getNewPost() {
        return newPost;
    }
    public DynamicPostListBox getPostListBox() {
        return postListBox;
    }



    public void setWidth(int width) {

        grid.setPixelSize(width, 85);
    }







}
