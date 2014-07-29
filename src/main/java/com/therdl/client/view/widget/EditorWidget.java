package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.therdl.client.view.widget.editor.RichTextToolbar;

public class EditorWidget extends Composite {
    interface EditorWidgetUiBinder extends UiBinder<HTMLPanel, EditorWidget> {
    }

    private static EditorWidgetUiBinder ourUiBinder = GWT.create(EditorWidgetUiBinder.class);

    @UiField
    HTMLPanel editorParent;
    RichTextArea richTextArea;

    public EditorWidget() {
        initWidget(ourUiBinder.createAndBindUi(this));
        richTextArea = new RichTextArea();
        richTextArea.setSize("100%", "300px");
        richTextArea.setTabIndex(3);

        /**
         * do not allow to enter more than 10000 characters to textarea
         */
        richTextArea.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent keyDownEvent) {
                RichTextArea textArea = (RichTextArea) keyDownEvent.getSource();
                int keyCode = keyDownEvent.getNativeKeyCode();

                if (textArea.getText().length() > 10000 && !isNotADigit(keyCode)) {
                    keyDownEvent.preventDefault();
                    keyDownEvent.stopPropagation();
                }
            }
        });

        RichTextToolbar toolbar = new RichTextToolbar(richTextArea);
        toolbar.setWidth("100%");
        editorParent.add(toolbar);
        editorParent.add(richTextArea);
    }

    /**
     * checks if pressed key is a special key or not
     *
     * @param keyCode
     * @return
     */
    private boolean isNotADigit(int keyCode) {
        return (keyCode == (char) KeyCodes.KEY_BACKSPACE)
                || (keyCode == (char) KeyCodes.KEY_ESCAPE)
                || (keyCode == (char) KeyCodes.KEY_DELETE)
                || (keyCode == (char) KeyCodes.KEY_HOME)
                || (keyCode == (char) KeyCodes.KEY_END)
                || (keyCode == (char) KeyCodes.KEY_LEFT)
                || (keyCode == (char) KeyCodes.KEY_RIGHT);
    }

    /**
     * allows to get content as HTML from outside of this class
     *
     * @return html content as String
     */
    public String getHTML() {
        return richTextArea.getHTML();
    }

    /**
     * sets html content to rich text area
     *
     * @param html
     */
    public void setHTML(String html) {
        richTextArea.setHTML(html);
    }
}