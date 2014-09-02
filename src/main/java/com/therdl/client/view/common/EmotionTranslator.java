package com.therdl.client.view.common;

import com.therdl.client.RDL;
import com.therdl.shared.Emotion;

/**
 * Encapsulates the binding between the emotion enum instance and it's internationalized text message
 */
public class EmotionTranslator {

	public static String getMessage(Emotion emo){

		switch (emo){
			case PASSION: return RDL.getI18n().emotionPASSION();
			case BOREDOM: return RDL.getI18n().emotionBOREDOM();
			case INTEREST: return RDL.getI18n().emotionINTEREST();
			case PANIC: return RDL.getI18n().emotionPANIC();
			case DELIGHT: return RDL.getI18n().emotionDELIGHT();
			case AVERSION: return RDL.getI18n().emotionAVERSION();
			case POLITE: return RDL.getI18n().emotionPOLITE();
			case DOUBT: return RDL.getI18n().emotionDOUBT();
			case ADMIRATION: return RDL.getI18n().emotionADMIRATION();
			case ENVY: return RDL.getI18n().emotionENVY();
			case AMUSED: return RDL.getI18n().emotionAMUSED();
			case INDIFFERENCE: return RDL.getI18n().emotionINDIFFERENCE();
			case HOPE: return RDL.getI18n().emotionHOPE();
			case FEAR: return RDL.getI18n().emotionFEAR();
			case GRATITUDE: return RDL.getI18n().emotionGRATITUDE();
			case ANGER: return RDL.getI18n().emotionANGER();
			case JOY: return RDL.getI18n().emotionJOY();
			case SORROW: return RDL.getI18n().emotionSORROW();
			case RELIEF: return RDL.getI18n().emotionRELIEF();
			case FRUSTRATION: return RDL.getI18n().emotionFRUSTRATION();
			case CALM: return RDL.getI18n().emotionCALM();
			case STRESS: return RDL.getI18n().emotionSTRESS();
			case LOVE: return RDL.getI18n().emotionLOVE();
			case HATE: return RDL.getI18n().emotionHATE();
			case SATISFACTION: return RDL.getI18n().emotionSATISFACTION();
			case SHAME: return RDL.getI18n().emotionSHAME();
			case CONFIDENCE: return RDL.getI18n().emotionCONFIDENCE();
			case CONFUSION: return RDL.getI18n().emotionCONFUSION();
		}
		return null;
	}

	public static String getBackground(Emotion emo){
		switch (emo){
		case PASSION: return "#fad8c7";
		case BOREDOM: return "#eb6420";
		case INTEREST: return "#f7c6c7";
		case PANIC: return "#e11d21";
		case DELIGHT: return "#bfd4f2";
		case AVERSION: return "#0052cc";
		case POLITE: return "#fef2c0";
		case DOUBT: return "#fbca04";
		case ADMIRATION: return "#bfe5bf";
		case ENVY: return "#009800";
		case AMUSED: return "#d4c5f9";
		case INDIFFERENCE: return "#5319e7";
		case HOPE: return "#fef2c0";
		case FEAR: return "#fbca04";
		case GRATITUDE: return "#f7c6c7";
		case ANGER: return "#e11d21";
		case JOY: return "#bfdadc";
		case SORROW: return "#006b75";
		case RELIEF: return "#fad8c7";
		case FRUSTRATION: return "#eb6420";
		case CALM: return "#bfd4f2";
		case STRESS: return "#0052cc";
		case LOVE: return "#d4c5f9";
		case HATE: return "#5319e7";
		case SATISFACTION: return "#c7def8";
		case SHAME: return "#207de5";
		case CONFIDENCE: return "#bfdadc";
		case CONFUSION: return "#006b75";
		}
		return null;
	}
}

