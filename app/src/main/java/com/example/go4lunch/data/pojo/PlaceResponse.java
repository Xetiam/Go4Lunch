package com.example.go4lunch.data.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class PlaceResponse {
    @JsonProperty("address_components")
    @Expose
    ArrayList<AddressComponentResponse> addressComponents;
    @JsonProperty("adr_address")
    @Expose
    String address;
    @JsonProperty("business_status")
    @Expose
    String businessStatus;
    @JsonProperty("curbside_pickup")
    @Expose
    Boolean curbsidePickup;
    @JsonProperty("current_opening_hours")
    @Expose
    PlaceOpeningHoursResponse currentOpeningHours;
    @JsonProperty("delivery")
    @Expose
    Boolean delivery;
    @JsonProperty("dine_in")
    @Expose
    Boolean dineIn;
    @JsonProperty("editorial_summary")
    @Expose
    PlaceEditorialSummaryResponse editorialSummary;
    @JsonProperty("formatted_address")
    @Expose
    String formattedAddress;
    @JsonProperty("formatted_phone_number")
    @Expose
    String formattedPhoneNumber;
    @JsonProperty("geometry")
    @Expose
    GeometryResponse geometry;
    @JsonProperty("icon")
    @Expose
    String icon;
    @JsonProperty("icon_background_color")
    @Expose
    String iconBackgroundColor;
    @JsonProperty("icon_mask_base_uri")
    @Expose
    String iconMaskBaseUri;
    @JsonProperty("international_phone_number")
    @Expose
    String internationalPhoneNumber;
    @JsonProperty("name")
    @Expose
    String name;
    @JsonProperty("opening_hours")
    @Expose
    PlaceOpeningHoursResponse openingHours;
    @JsonProperty("photos")
    @Expose
    ArrayList<PlacePhotoResponse> photos;
    @JsonProperty("place_id")
    @Expose
    String placeId;
    @JsonProperty("plus_code")
    @Expose
    PlusCodeResponse plusCode;
    @JsonProperty("price_level")
    @Expose
    int priceLevel;
    @JsonProperty("rating")
    @Expose
    int rating;
    @JsonProperty("reservable")
    @Expose
    Boolean reservable;
    @JsonProperty("reviews")
    @Expose
    ArrayList<PlaceReviewResponse> reviews;
    @JsonProperty("secondary_opening_hours")
    @Expose
    PlaceOpeningHoursResponse secondaryOpeningHours;
    @JsonProperty("serves_beer")
    @Expose
    Boolean servesBeer;
    @JsonProperty("serves_breakfast")
    @Expose
    Boolean servesBreakfast;
    @JsonProperty("serves_brunch")
    @Expose
    Boolean servesBrunch;
    @JsonProperty("serves_dinner")
    @Expose
    Boolean servesDinner;
    @JsonProperty("serves_lunch")
    @Expose
    Boolean servesLunch;
    @JsonProperty("serves_vegetarian_food")
    @Expose
    Boolean servesVegetarianFood;
    @JsonProperty("serves_wine")
    @Expose
    Boolean servesWine;
    @JsonProperty("takeout")
    @Expose
    Boolean takeout;
    @JsonProperty("types")
    @Expose
    ArrayList<String> types;
    @JsonProperty("url")
    @Expose
    ArrayList<String> url;
    @JsonProperty("user_ratings_total")
    @Expose
    int userRatingsTotal;
    @JsonProperty("utc_offset")
    @Expose
    int utcOffset;
    @JsonProperty("vicinity")
    @Expose
    String vicinity;
    @JsonProperty("website")
    @Expose
    String website;

    @JsonProperty("wheelchair_accessible_entrance")
    Boolean wheelchairAccessibleEntrance;
    public Boolean getWheelchairAccessibleEntrance() {
        return this.wheelchairAccessibleEntrance;
    }
    public void setWheelchairAccessibleEntrance(Boolean wheelchairAccessibleEntrance) {
        this.wheelchairAccessibleEntrance = wheelchairAccessibleEntrance;
    }
}
