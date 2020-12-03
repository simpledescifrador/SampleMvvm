package com.jimac.vetclinicapp.data.models

import com.google.gson.annotations.SerializedName

data class Pet(
    @SerializedName("pet_id") var petId: Int = 0,
    @SerializedName("pet_name") var name: String = "",
    @SerializedName("species_id") var speciesId: Int = 0,
    @SerializedName("species") var species: String = "",
    @SerializedName("breed_id") var breedId: Int = 0,
    @SerializedName("breed") var breed: String = "",
    @SerializedName("age") var age: Int = 0,
    @SerializedName("pet_size_id") var petSizeId: Int = 0,
    @SerializedName("size") var size: String = "",
    @SerializedName("height") var height: String = "",
    @SerializedName("weight") var weight: String = "",
    @SerializedName("gender") var gender: String = "",
    @SerializedName("uploaded_image_url") var petImageUrl: String = "",
    @SerializedName("description") var description: String = "",
    @SerializedName("client_id") var clientId: Int = 0,
    @SerializedName("date_created") var dateCreated: String = ""
)