package com.model;

public class Country {

	private String name;
	private String description;
	private String image;

	public Country() {
	}

	public Country(String name, String description, String dob, String country,
			String height, String spouse, String children, String image) {
		super();
		this.name = name;
		this.description = description;
		this.image = image;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
