package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.therdl.client.view.widget.editor.RichTextToolbar;

public class EditorWidget extends Composite{
    interface EditorWidgetUiBinder extends UiBinder<HTMLPanel, EditorWidget> {
    }

    private static EditorWidgetUiBinder ourUiBinder = GWT.create(EditorWidgetUiBinder.class);

    @UiField
    HTMLPanel editorParent;
    RichTextArea richTextArea;

    public EditorWidget() {
        initWidget(ourUiBinder.createAndBindUi(this));
        richTextArea = new RichTextArea();
        richTextArea.setSize("100%","300px");
        RichTextToolbar toolbar = new RichTextToolbar(richTextArea);
        toolbar.setWidth("100%");
        editorParent.add(toolbar);
        editorParent.add(richTextArea);
    }

    public String getHTML() {
        return richTextArea.getHTML();
    }

    public void setHTML(String html) {
        richTextArea.setHTML(html);
    }
}