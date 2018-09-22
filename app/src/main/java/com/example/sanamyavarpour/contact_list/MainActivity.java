package com.example.sanamyavarpour.contact_list;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.sanamyavarpour.contact_list.adapter.ContactRecyclerAdapter;
import com.example.sanamyavarpour.contact_list.database.AppDataBase;
import com.example.sanamyavarpour.contact_list.database.ContactDao;
import com.example.sanamyavarpour.contact_list.model.Contact;
import com.example.sanamyavarpour.contact_list.service.ApiClient;
import com.example.sanamyavarpour.contact_list.service.ApiInterface;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;



public class MainActivity extends AppCompatActivity  {
    private RecyclerView contactsRecyclerView;
    private ContactRecyclerAdapter contactRecyclerAdapter;
    private ProgressDialog dialog;

    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main);
        setUi();
        contactList();

    }

    private void contactList(){

        ApiInterface apiinterface = ApiClient.getClient().create(ApiInterface.class);
        retrofit2.Call<List<Contact>> call = apiinterface.GetContactList();

        call.enqueue(new Callback<List<Contact>>(){

            @Override
            public void onResponse(retrofit2.Call<List<Contact>> call, Response<List<Contact>> response) {
                switch (response.code())
                {
                    case 200:
                        List<Contact> contactTemp = response.body();
                        setUpRecyclerView(contactTemp);
                        saveContactsToDB(contactTemp);

                        break;
                    case  403:
                        Toast.makeText(MainActivity.this, " token : invalid", Toast.LENGTH_LONG).show();
                        break;
                    case 404:
                        Toast.makeText(MainActivity.this, " 404 Not Found", Toast.LENGTH_LONG).show();
                        break;
                }
            }
            @Override
            public void onFailure(retrofit2.Call<List<Contact>> call, Throwable t) {
                showNoConnectionDialog(MainActivity.this);
                setUpRecyclerView( Application.getDB().getContactDAO().getContacts() );

            }
        });

    }
    void saveContactsToDB(List<Contact> contacts)
    {
        try {
            for (int i = 0 ; i< contacts.size() ; i++) {
                Contact contactdb = new Contact();
                contactdb.setName( contacts.get( i ).getName() );
                contactdb.setPhone( contacts.get( i ).getPhone() );

                Application.getDB().getContactDAO().insert(contactdb);
                setResult(RESULT_OK);
                finish();

            }

        }
        catch (SQLiteConstraintException e) {

            System.out.println( e.getMessage());
        }

    }
    private void setUpRecyclerView(List<Contact> contacts) {
        contactRecyclerAdapter = new ContactRecyclerAdapter(MainActivity.this, contacts);

        {
            RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            contactsRecyclerView.setLayoutManager(linearLayoutManager);
            contactsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            contactsRecyclerView.setAdapter(contactRecyclerAdapter);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                contactRecyclerAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                contactRecyclerAdapter.getFilter().filter(query);
                return false;
            }
        });

        return true;
    }

    //link to open network settings programmatically
    public static void showNoConnectionDialog(Context ctx1) {
        final Context ctx = ctx1;
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(true);
        builder.setMessage("internet connection is failed please connect to internet");
        builder.setTitle("no connection");
        builder.setPositiveButton("turn on phone network", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ctx.startActivity(new Intent( Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        builder.setNegativeButton("cancle", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                return;
            }
        });
        builder.show();
    }

    void setUi (){
        contactsRecyclerView = findViewById(R.id.contactRecycler);

    }

}