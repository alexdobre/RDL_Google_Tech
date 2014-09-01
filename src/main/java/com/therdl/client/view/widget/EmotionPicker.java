package com.therdl.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.therdl.client.view.SnipEditView;
import com.therdl.client.view.common.EmotionTranslator;
import com.therdl.shared.Emotion;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Container;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.LinkedGroupItem;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.HeadingSize;

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
	Container emoContainer;

	private SnipEditView parentView;

	private List<String> selectedEmotions;
	private List<LinkedGroupItem> posEmoList;
	private List<LinkedGroupItem> negEmoList;
	private Map<String, Emotion> emoMap;

	public EmotionPicker(SnipEditView parentView) {
		this.parentView = parentView;
		ourUiBinder.createAndBindUi(this);
		selectedEmotions = new ArrayList<>();
		populateListGroups();
	}

	public void show() {
		emotionPickerModal.show();
	}

	public void hide() {
		emotionPickerModal.hide();
	}

	public List<String> getSelectedEmotions() {
		formSelectedEmotions();
		return selectedEmotions;
	}

	public void setSelectedEmotions(List<String> selectedEmotions) {
		this.selectedEmotions = selectedEmotions;
	}

	public void reset() {
		selectedEmotions.clear();
		for (LinkedGroupItem emo : posEmoList) {
			emo.setActive(false);
		}
		for (LinkedGroupItem emo : negEmoList) {
			emo.setActive(false);
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

	private void formSelectedEmotions() {
		log.info("Forming selected emotions");
		selectedEmotions.clear();

		for (int i = 0; i < posEmoList.size(); i++) {
			LinkedGroupItem grpItem = posEmoList.get(i);
			if (grpItem.isActive()) {
				selectedEmotions.add(Emotion.servePosEmoList().get(i).name());
			}
		}
		for (int i = 0; i < negEmoList.size(); i++) {
			LinkedGroupItem grpItem = negEmoList.get(i);
			if (grpItem.isActive()) {
				selectedEmotions.add(Emotion.serveNegEmoList().get(i).name());
			}
		}
	}

	private void populateListGroups() {

		List<Emotion> posEmoList = Emotion.servePosEmoList();
		List<Emotion> negEmoList = Emotion.serveNegEmoList();
		this.posEmoList = new ArrayList<>(13);
		this.negEmoList = new ArrayList<>(13);
		this.emoMap = new HashMap<>(26);

		for (int i = 0; i < posEmoList.size(); i++) {
			Row emoRow = new Row();
			emoContainer.add(emoRow);
			Column posColumn = new Column(ColumnSize.MD_6);
			posColumn.getElement().getStyle().setProperty("padding", "2px");
			posColumn.getElement().getStyle().setProperty("textAlign", "center");
			emoRow.add(posColumn);
			Column negColumn = new Column(ColumnSize.MD_6);
			negColumn.getElement().getStyle().setProperty("padding", "2px");
			negColumn.getElement().getStyle().setProperty("textAlign", "center");
			emoRow.add(negColumn);

			LinkedGroupItem posEmo = buildLinkedGroupItem(EmotionTranslator.getMessage(posEmoList.get(i)));
			this.posEmoList.add(posEmo);
			this.emoMap.put(posEmo.getText(), posEmoList.get(i));
			posColumn.add(posEmo);

			LinkedGroupItem negEmo = buildLinkedGroupItem(EmotionTranslator.getMessage(negEmoList.get(i)));
			this.negEmoList.add(negEmo);
			this.emoMap.put(negEmo.getText(), negEmoList.get(i));
			negColumn.add(negEmo);
		}
	}

	private LinkedGroupItem buildLinkedGroupItem(String text) {
		LinkedGroupItem linkedGroupItem = new LinkedGroupItem();
		Heading heading = new Heading(HeadingSize.H4);
		heading.setText(text);
		linkedGroupItem.getElement().getStyle().setProperty("padding", "2px");
		linkedGroupItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				LinkedGroupItem source = ((LinkedGroupItem) clickEvent.getSource());
				source.setActive(!source.isActive());
			}
		});
		linkedGroupItem.add(heading);
		return linkedGroupItem;
	}
}
