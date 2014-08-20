package com.therdl.shared.exceptions;

import java.io.Serializable;

/**
 * An exception that has a code attached to it
 */
public abstract class RdlCodedException extends  Exception implements Serializable{

	private String code;

	protected RdlCodedException (String code){
		super(code);
		this.code = code;
	}

	public String getCode(){
		return code;
	}
}
