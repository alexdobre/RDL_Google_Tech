package com.therdl.client.view.cssbundles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface Resources extends ClientBundle {
	//Is necessary instantiates the Resources using GWT.create(Class) technique is used under the hood to instruct the compiler to use deferred binding.
	public static final Resources INSTANCE =  GWT.create(Resources.class);


    @CssResource.NotStrict
	@Source("closure.css")
    public CssResource editorCss();

    @CssResource.NotStrict
    @Source("closureList.css")
    public CssResource listCss();

    @Source("tabdev.js")
    public TextResource widjdevList();

    @Source("snipeditor.js")
    public TextResource dialogView();

    @Source("RDL_logo_large copy.gif")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource landingImage();



}
