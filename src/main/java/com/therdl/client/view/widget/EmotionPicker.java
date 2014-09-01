package com.therdl.client.view.widget;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget for picking out emotions
 */
public class EmotionPicker extends Composite {

	interface EmotionPickerUiBinder extends UiBinder<Widget, EmotionPicker> {
	}

	private static EmotionPickerUiBinder ourUiBinder = GWT.create(EmotionPickerUiBinder.class);

	@UiField
	Modal emotionPickerModal;

	@UiField
	Button btnReset, btnOk;

	private List<String> selectedEmotions;

	public EmotionPicker() {
		ourUiBinder.createAndBindUi(this);
		selectedEmotions = new ArrayList<>();
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
	}

	private void formSelectedEmotions() {

	}
}
