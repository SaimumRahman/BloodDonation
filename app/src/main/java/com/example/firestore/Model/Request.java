package com.example.firestore.Model;

public class Request {

    private String recipient_id;
    private String donner_id;
    private String preferred_location;
    private String relationship_with_blood_recipient;
    private String alternate_mobile_number;
    private String requested_time_frame_to;
    private String requested_time_frame_from;
    private String requested_blood;
    private String status;
    private String assigned_date;
    private String main_recipient_id;

    public Request() {
    }

    public Request(final String recipient_id, final String donner_id, final String preferred_location, final String relationship_with_blood_recipient, final String alternate_mobile_number, final String requested_time_frame_to, final String requested_time_frame_from, final String requested_blood, final String status, final String assigned_date, final String main_recipient_id) {
        this.recipient_id = recipient_id;
        this.donner_id = donner_id;
        this.preferred_location = preferred_location;
        this.relationship_with_blood_recipient = relationship_with_blood_recipient;
        this.alternate_mobile_number = alternate_mobile_number;
        this.requested_time_frame_to = requested_time_frame_to;
        this.requested_time_frame_from = requested_time_frame_from;
        this.requested_blood = requested_blood;
        this.status = status;
        this.assigned_date = assigned_date;
        this.main_recipient_id = main_recipient_id;
    }

    public String getRecipient_id() {
        return this.recipient_id;
    }

    public void setRecipient_id(final String recipient_id) {
        this.recipient_id = recipient_id;
    }

    public String getDonner_id() {
        return this.donner_id;
    }

    public void setDonner_id(final String donner_id) {
        this.donner_id = donner_id;
    }

    public String getPreferred_location() {
        return this.preferred_location;
    }

    public void setPreferred_location(final String preferred_location) {
        this.preferred_location = preferred_location;
    }

    public String getRelationship_with_blood_recipient() {
        return this.relationship_with_blood_recipient;
    }

    public void setRelationship_with_blood_recipient(final String relationship_with_blood_recipient) {
        this.relationship_with_blood_recipient = relationship_with_blood_recipient;
    }

    public String getAlternate_mobile_number() {
        return this.alternate_mobile_number;
    }

    public void setAlternate_mobile_number(final String alternate_mobile_number) {
        this.alternate_mobile_number = alternate_mobile_number;
    }

    public String getRequested_time_frame_to() {
        return this.requested_time_frame_to;
    }

    public void setRequested_time_frame_to(final String requested_time_frame_to) {
        this.requested_time_frame_to = requested_time_frame_to;
    }

    public String getRequested_time_frame_from() {
        return this.requested_time_frame_from;
    }

    public void setRequested_time_frame_from(final String requested_time_frame_from) {
        this.requested_time_frame_from = requested_time_frame_from;
    }

    public String getRequested_blood() {
        return this.requested_blood;
    }

    public void setRequested_blood(final String requested_blood) {
        this.requested_blood = requested_blood;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getAssigned_date() {
        return this.assigned_date;
    }

    public void setAssigned_date(final String assigned_date) {
        this.assigned_date = assigned_date;
    }

    public String getMain_recipient_id() {
        return this.main_recipient_id;
    }

    public void setMain_recipient_id(final String main_recipient_id) {
        this.main_recipient_id = main_recipient_id;
    }
}
