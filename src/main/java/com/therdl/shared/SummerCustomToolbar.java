package com.therdl.shared;

import org.gwtbootstrap3.extras.summernote.client.ui.base.Toolbar;

/**
 * Created by rc185213 on 1/29/2015.
 */
public class SummerCustomToolbar extends Toolbar {


    public SummerCustomToolbar() {
        this.setShowCodeViewButton(false);
        this.setShowBoldButton(true);
        this.setShowClearButton(true);
        this.setShowColorButton(true);
        this.setShowFontSizeButton(true);
        this.setShowFullScreenButton(true);
        this.setShowHelpButton(true);
        this.setShowInsertLinkButton(true);
        this.setShowInsertPictureButton(true);
        this.setShowInsertTableButton(true);
        this.setShowInsertVideoButton(true);
        this.setShowItalicButton(true);
        this.setShowLineHeightButton(true);
        this.setShowOrderedListButton(true);
        this.setShowParagraphButton(true);
        this.setShowStyleButton(true);
        this.setShowUnderlineButton(true);
        this.setShowUnorderedListButton(true);

    }
}
