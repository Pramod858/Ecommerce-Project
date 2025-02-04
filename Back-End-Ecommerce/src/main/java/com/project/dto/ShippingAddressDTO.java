package com.project.dto;

import com.project.entity.ShippingAddress;
import com.project.entity.User;

public class ShippingAddressDTO {

	    private Long id;

	    private User user;

	    private String streetAddress;

	    private String city;

	    private String state;

	    private String zipCode;

	    private String country;

		public ShippingAddressDTO() {
		}
		
		public static ShippingAddressDTO fromEntity(ShippingAddress shippingAddress) {
	        ShippingAddressDTO dto = new ShippingAddressDTO();
	        dto.setId(shippingAddress.getId());
	        dto.setStreetAddress(shippingAddress.getStreetAddress());
	        dto.setCity(shippingAddress.getCity());
	        dto.setState(shippingAddress.getState());
	        dto.setZipCode(shippingAddress.getZipCode());
	        dto.setCountry(shippingAddress.getCountry());
	        return dto;
	    }

		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public String getStreetAddress() {
			return streetAddress;
		}

		public void setStreetAddress(String streetAddress) {
			this.streetAddress = streetAddress;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getZipCode() {
			return zipCode;
		}

		public void setZipCode(String zipCode) {
			this.zipCode = zipCode;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}
	    
	    
}
