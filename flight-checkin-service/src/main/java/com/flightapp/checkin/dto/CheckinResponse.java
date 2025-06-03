package com.flightapp.checkin.dto;

public class CheckinResponse {
    private String message;
    private String confirmationNumber;

    public CheckinResponse(String message, String confirmationNumber) {
        this.message = message;
        this.confirmationNumber = confirmationNumber;
    }

    public String getMessage() {
        return message;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

	@Override
	public String toString() {
		return "CheckinResponse [message=" + message + ", confirmationNumber=" + confirmationNumber + "]";
	}
    
}
