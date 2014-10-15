package com.therdl.client.app;

import com.therdl.client.presenter.func.GrabSnipFunc;
import com.therdl.client.presenter.func.impl.GrabSnipFuncImpl;

/**
 * Provides ready made implementations of Func interfaces
 */
public class FuncFactory {

	public static GrabSnipFunc createGrabSnipFunc() {
		return new GrabSnipFuncImpl();
	}
}
