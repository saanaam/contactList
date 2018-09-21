package com.example.sanamyavarpour.contact_list.service;



import com.example.sanamyavarpour.contact_list.Model.Contact;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.Call;

public interface ApiInterface {



    @GET("contacts.php")
    Call<List<Contact>> GetContactList ();


}
