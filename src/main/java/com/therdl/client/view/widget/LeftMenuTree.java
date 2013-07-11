package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class LeftMenuTree extends Composite {

	private static LeftMenuTreeUiBinder uiBinder = GWT
			.create(LeftMenuTreeUiBinder.class);
	@UiField Tree leftMenuTree;
	@UiField TreeItem snipsFavorites;
	@UiField TreeItem favorites;
	@UiField TreeItem fastCapsFavorites;
	@UiField TreeItem materialFavorites;
	@UiField TreeItem myGroups;
	@UiField TreeItem authored;
	@UiField TreeItem serviceFavorites;
	@UiField TreeItem authoredSnips;
	@UiField TreeItem authoredFastCaps;
	@UiField TreeItem authoredMaterials;
	@UiField TreeItem authoredServices;

	interface LeftMenuTreeUiBinder extends UiBinder<Widget, LeftMenuTree> {
	}

	public LeftMenuTree() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
