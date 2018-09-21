package com.example.sanamyavarpour.contact_list.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "contact")
public class Contact {

        @SerializedName("name")
        private String name;
        @SerializedName("phone")
        @PrimaryKey
        @NonNull
        private String phone;

        public String getName() {
            return name;
        }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
    }
}
