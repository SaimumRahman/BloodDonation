package com.example.firestore.Model;

public class Donation_History {

    private String donation_id;
    private String request_id;
    private String donation_date;
    private String reciepient_id;
    private String donner_id;

    public Donation_History() {
    }

    public Donation_History(final String donation_id, final String request_id, final String donation_date, final String reciepient_id, final String donner_id) {
        this.donation_id = donation_id;
        this.request_id = request_id;
        this.donation_date = donation_date;
        this.reciepient_id = reciepient_id;
        this.donner_id = donner_id;
    }

    public String getdonation_id() {
        return this.donation_id;
    }

    public void setdonation_id(final String job_id) {
        this.donation_id = job_id;
    }

    public String getRequest_id() {
        return this.request_id;
    }

    public void setRequest_id(final String request_id) {
        this.request_id = request_id;
    }

    public String getDonation_date() {
        return this.donation_date;
    }

    public void setDonation_date(final String donation_date) {
        this.donation_date = donation_date;
    }

    public String getReciepient_id() {
        return this.reciepient_id;
    }

    public void setReciepient_id(final String reciepient_id) {
        this.reciepient_id = reciepient_id;
    }

    public String getDonner_id() {
        return this.donner_id;
    }

    public void setDonner_id(final String donner_id) {
        this.donner_id = donner_id;
    }
}
