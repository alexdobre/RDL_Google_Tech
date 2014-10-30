package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.therdl.client.view.EmotionView;
import com.therdl.client.view.SnipEditView;
import com.therdl.client.view.common.EmotionTranslator;
import com.therdl.client.view.common.ViewUtils;
import com.therdl.shared.Emotion;
import com.therdl.shared.beans.SnipBean;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Container;
import org.gwtbootstrap3.client.ui.DropDown;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.LinkedGroupItem;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Widget for picking out emotions
 */
public class EmotionPicker extends Composite {
	private static Logger log = Logger.getLogger(EmotionPicker.class.getName());

	interface EmotionPickerUiBinder extends UiBinder<Widget, EmotionPicker> {
	}

	private static EmotionPickerUiBinder ourUiBinder = GWT.create(EmotionPickerUiBinder.class);

	@UiField
	Modal emotionPickerModal;

	@UiField
	Button btnReset, btnOk;

	@UiField
	TextBox emotionAutocomplete;

	@UiField
	InputGroup dropdownInputGroup;

	@UiField
	DropDownMenu autocompleteMenu;

	@UiField
	Container emoContainer;

	private EmotionView parentView;
	private AutoBean<SnipBean> currentSnipBean;

	private List<LinkedGroupItem> posEmoList;
	private List<LinkedGroupItem> negEmoList;
	private Map<LinkedGroupItem, EmoElements> emoMap;

	public EmotionPicker(EmotionView parentView, AutoBean<SnipBean> currentSnipBean) {
		log.info("EmotionPicker constructor BEGIN");
		this.parentView = parentView;
		this.currentSnipBean = currentSnipBean;
		ourUiBinder.createAndBindUi(this);
		populateListGroups();
	}

	public void show() {
		refreshEmotions();
		emotionPickerModal.show();
	}

	public void hide() {
		emotionPickerModal.hide();
	}


	public void reset() {
		if (currentSnipBean != null && currentSnipBean.as().getEmotions() != null) {
			currentSnipBean.as().getEmotions().clear();
		}
		for (Map.Entry entry : emoMap.entrySet()) {
			EmoElements elements = (EmoElements) entry.getValue();
			setActive(false, elements.getParent(), elements.getEmo(), elements.getLabel());
		}
	}

	private void refreshEmotions() {
		if (currentSnipBean != null && currentSnipBean.as().getEmotions() != null) {
			for (Map.Entry entry : emoMap.entrySet()) {
				EmoElements elements = (EmoElements) entry.getValue();
				setActive(currentSnipBean.as().getEmotions().contains(elements.getEmo().name()),
						elements.getParent(), elements.getEmo(), elements.getLabel());
			}
		} else {
			reset();
		}
	}

	@UiHandler("btnReset")
	public void onResetClicked(ClickEvent event) {
		reset();
	}

	@UiHandler("btnOk")
	public void onOkClicked(ClickEvent event) {
		parentView.displayEmotions();
		emotionPickerModal.hide();
	}

	@UiHandler("emotionAutocomplete")
	public void onKeyDown(KeyDownEvent keyDownEvent) {
		autocompletePass();
	}

	private void autocompletePass() {
		String currentText = emotionAutocomplete.getText();
		autocompleteMenu.clear();
		if (currentText == null || currentText.isEmpty()) return;
		for (String emoShard : ViewUtils.getPieceMealEmotions()) {
			if (emoShard.matches("(.*)" + currentText + "(.*)")) {
				//we have a match
				autocompleteMenu.add(buildAutocompleteItem(emoShard));
			}
		}
		if (autocompleteMenu.getWidgetCount() != 0) {
			dropdownInputGroup.addStyleName("open");
		} else {
			dropdownInputGroup.removeStyleName("open");
		}
	}

	private AnchorListItem buildAutocompleteItem(String emoShard) {
		AnchorListItem menuItem = new AnchorListItem();
		menuItem.setText(emoShard);
		menuItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				Anchor menuItem = (Anchor) clickEvent.getSource();
				String emoShard = menuItem.getText();
				//search for an emotion group to make active
				for (Map.Entry entry : emoMap.entrySet()) {
					EmoElements elements = (EmoElements) entry.getValue();
					if (EmotionTranslator.getMessage(elements.getEmo()).contains(emoShard)) {
						//we have a match
						if (!elements.getParent().isActive()) {
							setActive(true, elements.getParent(), elements.getEmo(), elements.getLabel());
							// we add the backing bean emotion
							if (currentSnipBean.as().getEmotions() == null){
								currentSnipBean.as().setEmotions(new ArrayList<String>(1));
							}
							if (!currentSnipBean.as().getEmotions().contains(elements.getEmo().name())){
								currentSnipBean.as().getEmotions().add(elements.getEmo().name());
							}
						}
					}
				}
			}
		});
		return menuItem;
	}

	private void populateListGroups() {

		List<Emotion> posEmoList = Emotion.servePosEmoList();
		List<Emotion> negEmoList = Emotion.serveNegEmoList();
		this.posEmoList = new ArrayList<LinkedGroupItem>(13);
		this.negEmoList = new ArrayList<LinkedGroupItem>(13);
		this.emoMap = new HashMap<LinkedGroupItem, EmoElements> (26);

		for (int i = 0; i < posEmoList.size(); i++) {
			Row emoRow = new Row();
			emoContainer.add(emoRow);
			Column posColumn = new Column(ColumnSize.MD_6);
			posColumn.getElement().getStyle().setProperty("padding", "1px");
			posColumn.getElement().getStyle().setProperty("textAlign", "center");
			emoRow.add(posColumn);
			Column negColumn = new Column(ColumnSize.MD_6);
			negColumn.getElement().getStyle().setProperty("padding", "1px");
			negColumn.getElement().getStyle().setProperty("textAlign", "center");
			emoRow.add(negColumn);

			LinkedGroupItem posEmo = buildLinkedGroupItem(EmotionTranslator.getMessage(posEmoList.get(i)), posEmoList.get(i));
			this.posEmoList.add(posEmo);
			posColumn.add(posEmo);

			LinkedGroupItem negEmo = buildLinkedGroupItem(EmotionTranslator.getMessage(negEmoList.get(i)), negEmoList.get(i));
			this.negEmoList.add(negEmo);
			negColumn.add(negEmo);
		}
	}

	private LinkedGroupItem buildLinkedGroupItem(String text, Emotion emo) {
		LinkedGroupItem linkedGroupItem = new LinkedGroupItem();
		FlowPanel flowPanel = new FlowPanel();
		linkedGroupItem.add(flowPanel);

		//Color square
		Span colorSquare = new Span();
		colorSquare.addStyleName("color-square");
		colorSquare.getElement().getStyle().setProperty("backgroundColor", EmotionTranslator.getBackground(emo));
		flowPanel.add(colorSquare);

		//Text
		Span label = new Span();
		colorSquare.addStyleName("label-color");
		label.setText(text);

		//Parent
		linkedGroupItem.addStyleName("emo-select");
		linkedGroupItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				LinkedGroupItem source = ((LinkedGroupItem) clickEvent.getSource());
				source.setActive(!source.isActive());
				if (currentSnipBean.as().getEmotions() == null) {
					currentSnipBean.as().setEmotions(new ArrayList<String>(1));
				}
				EmoElements elements = emoMap.get(source);
				// if the item is now active
				if (source.isActive()) {
					log.info(elements.getEmo().name() + "is now active - adding to emo list");
					if (!currentSnipBean.as().getEmotions().contains(elements.getEmo().name())) {
						currentSnipBean.as().getEmotions().add(elements.getEmo().name());
					}
				} else {
					log.info(elements.getEmo().name() + "is now inactive - removing from emo list");
					if (currentSnipBean.as().getEmotions().contains(elements.getEmo().name())) {
						currentSnipBean.as().getEmotions().remove(elements.getEmo().name());
					}
				}
				setActive(source.isActive(), source, elements.getEmo(), elements.getLabel());
				log.info("List status after click: " + currentSnipBean.as().getEmotions());
			}
		});
		flowPanel.add(label);

		//add elements
		EmoElements elements = new EmoElements(colorSquare, emo, linkedGroupItem, label);
		if (currentSnipBean.as().getEmotions() != null && currentSnipBean.as().getEmotions().
				contains(emo.name())) {
			setActive(true, linkedGroupItem, emo, label);
		} else {
			setActive(false, linkedGroupItem, emo, label);
		}
		emoMap.put(linkedGroupItem, elements);
		return linkedGroupItem;
	}

	private void setActive(boolean active, LinkedGroupItem item, Emotion emo, Span label) {
		item.setActive(active);

		if (item.isActive()) {
			item.getElement().getStyle().setProperty("backgroundColor",
					EmotionTranslator.getBackground(emo));
			//if emo is positive we keep the text black
			if (Emotion.isPositive(emo)) {
				label.getElement().getStyle().setProperty("color", "rgb(85, 85, 85)");
			}
			//we remove the border
			item.getElement().getStyle().setProperty("borderColor", "#ffffff");
		} else {
			item.getElement().getStyle().setProperty("backgroundColor", "#ffffff");
			item.getElement().getStyle().clearBorderColor();
		}
	}

	private static class EmoElements {
		private Span colorSquare;
		private Emotion emo;
		private LinkedGroupItem parent;
		private Span label;

		private EmoElements(Span colorSquare, Emotion emo, LinkedGroupItem parent, Span label) {
			this.colorSquare = colorSquare;
			this.emo = emo;
			this.parent = parent;
			this.label = label;
		}

		public Span getColorSquare() {
			return colorSquare;
		}

		public void setColorSquare(Span colorSquare) {
			this.colorSquare = colorSquare;
		}

		public Emotion getEmo() {
			return emo;
		}

		public void setEmo(Emotion emo) {
			this.emo = emo;
		}

		public LinkedGroupItem getParent() {
			return parent;
		}

		public void setParent(LinkedGroupItem parent) {
			this.parent = parent;
		}

		public Span getLabel() {
			return label;
		}

		public void setLabel(Span label) {
			this.label = label;
		}
	}

	public AutoBean<SnipBean> getCurrentSnipBean() {
		return currentSnipBean;
	}

	public void setCurrentSnipBean(AutoBean<SnipBean> currentSnipBean) {
		this.currentSnipBean = currentSnipBean;
	}
}
