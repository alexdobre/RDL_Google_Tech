package com.therdl.shared;

import java.util.ArrayList;
import java.util.List;

/**
 * This enum encapsulates the emotions used as user input for content
 */
public enum Emotion {

	PASSION, BOREDOM,
	INTEREST, PANIC,
	DELIGHT, AVERSION,
	POLITE, DOUBT,
	ADMIRATION, ENVY,
	AMUSED, INDIFFERENCE,
	HOPE, FEAR,
	GRATITUDE, ANGER,
	JOY, SORROW,
	RELIEF, FRUSTRATION,
	CALM, STRESS,
	LOVE, HATE,
	SATISFACTION, SHAME;

	private static List<Emotion> posEmoList;
	private static List<Emotion> negEmoList;

	public static List<Emotion> servePosEmoList(){
		if (posEmoList == null){
			buildPosEmoList();
		}
		return posEmoList;
	}

	public static List<Emotion> serveNegEmoList(){
		if (negEmoList == null){
			buildNegEmoList();
		}
		return negEmoList;
	}

	public static boolean isPositive(Emotion emo){
		if (posEmoList == null){
			buildPosEmoList();
		}
		return posEmoList.contains(emo);
	}

	private static void buildNegEmoList() {
		negEmoList= new ArrayList<>(13);
		negEmoList.add(BOREDOM);
		negEmoList.add(PANIC);
		negEmoList.add(AVERSION);
		negEmoList.add(DOUBT);
		negEmoList.add(ENVY);
		negEmoList.add(INDIFFERENCE);
		negEmoList.add(FEAR);
		negEmoList.add(ANGER);
		negEmoList.add(SORROW);
		negEmoList.add(FRUSTRATION);
		negEmoList.add(STRESS);
		negEmoList.add(HATE);
		negEmoList.add(SHAME);
	}

	private static void buildPosEmoList() {
		posEmoList= new ArrayList<>(13);
		posEmoList.add(PASSION);
		posEmoList.add(INTEREST);
		posEmoList.add(DELIGHT);
		posEmoList.add(POLITE);
		posEmoList.add(ADMIRATION);
		posEmoList.add(AMUSED);
		posEmoList.add(HOPE);
		posEmoList.add(GRATITUDE);
		posEmoList.add(JOY);
		posEmoList.add(RELIEF);
		posEmoList.add(CALM);
		posEmoList.add(LOVE);
		posEmoList.add(SATISFACTION);
	}
}
