package com.example.go4lunch.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PlaceResponse {
    @SerializedName("address_components")
    ArrayList<AddressComponentResponse> addressComponents;
    @SerializedName("adr_address")
    String address;
    @SerializedName("business_status")
    String businessStatus;
    @SerializedName("curbside_pickup")
    Boolean curbsidePickup;
    @SerializedName("current_opening_hours")
    PlaceOpeningHoursResponse currentOpeningHours;
    @SerializedName("delivery")
    Boolean delivery;
    @SerializedName("dine_in")
    Boolean dineIn;
    @SerializedName("editorial_summary")
    PlaceEditorialSummaryResponse editorialSummary;
    @SerializedName("formatted_address")
    String formattedAddress;
    @SerializedName("formatted_phone_number")
    String formattedPhoneNumber;
    @SerializedName("geometry")
    GeometryResponse geometry;
    @SerializedName("icon")
    String icon;
    @SerializedName("icon_background_color")
    String iconBackgroundColor;
    @SerializedName("icon_mask_base_uri")
    String iconMaskBaseUri;
    @SerializedName("international_phone_number")
    String internationalPhoneNumber;
    @SerializedName("name")
    String name;
    @SerializedName("opening_hours")
    PlaceOpeningHoursResponse openingHours;
    @SerializedName("photos")
    ArrayList<PlacePhotoResponse> photos;
    @SerializedName("place_id")
    String placeId;
    @SerializedName("plus_code")
    PlusCodeResponse plusCode;
    @SerializedName("price_level")
    int priceLevel;
    @SerializedName("rating")
    Double rating;
    @SerializedName("reservable")
    Boolean reservable;
    @SerializedName("reviews")
    ArrayList<PlaceReviewResponse> reviews;
    @SerializedName("secondary_opening_hours")
    PlaceOpeningHoursResponse secondaryOpeningHours;
    @SerializedName("serves_beer")
    Boolean servesBeer;
    @SerializedName("serves_breakfast")
    Boolean servesBreakfast;
    @SerializedName("serves_brunch")
    Boolean servesBrunch;
    @SerializedName("serves_dinner")
    Boolean servesDinner;
    @SerializedName("serves_lunch")
    Boolean servesLunch;
    @SerializedName("serves_vegetarian_food")
    Boolean servesVegetarianFood;
    @SerializedName("serves_wine")
    Boolean servesWine;
    @SerializedName("takeout")
    Boolean takeout;
    @SerializedName("types")
    ArrayList<String> types;
    @SerializedName("url")
    ArrayList<String> url;
    @SerializedName("user_ratings_total")
    int userRatingsTotal;
    @SerializedName("utc_offset")
    int utcOffset;
    @SerializedName("vicinity")
    String vicinity;
    @SerializedName("website")
    String website;
    @SerializedName("wheelchair_accessible_entrance")
    Boolean wheelchairAccessibleEntrance;
}
