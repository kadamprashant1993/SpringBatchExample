package com.org.model;

public class JobBean implements Cloneable {
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	 public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	

}
