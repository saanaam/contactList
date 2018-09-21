package com.example.sanamyavarpour.contact_list;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.sanamyavarpour.contact_list.Adapter.ContactRecyclerAdapter;
import com.example.sanamyavarpour.contact_list.Database.AppDataBase;
import com.example.sanamyavarpour.contact_list.Database.ContactDao;
import com.example.sanamyavarpour.contact_list.Model.Contact;
import com.example.sanamyavarpour.contact_list.service.ApiClient;
import com.example.sanamyavarpour.contact_list.service.ApiInterface;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;



public class MainActivity extends AppCompatActivity  {
    private RecyclerView contactsRecyclerView;
    private ContactRecyclerAdapter contactRecyclerAdapter;
    private ProgressDialog dialog;
    private ContactDao mContactDao;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        setUi();


        mContactDao = Room.databaseBuilder(this, AppDataBase.class, "contact")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .getContactDAO();

        contactList();

    }

        private void contactList(){

            ApiInterface apiinterface = ApiClient.getClient().create(ApiInterface.class);
            retrofit2.Call<List<Contact>> call = apiinterface.GetContactList();

            call.enqueue(new Callback<List<Contact>>(){

                @Override
                public void onResponse(retrofit2.Call<List<Contact>> call, Response<List<Contact>> response) {
                    if (response.code()==200){

                        List<Contact> contactTemp = response.body();

                        setUpRecyclerView(contactTemp);


                        for (int i = 0 ; i< contactTemp.size() ; i++) {
                            Contact contactdb = new Contact();
                            contactdb.setName( contactTemp.get( i ).getName() );
                            contactdb.setPhone( contactTemp.get( i ).getPhone() );

                            try {
                                mContactDao.insert(contactdb);
                                setResult(RESULT_OK);
                                finish();

                            } catch (SQLiteConstraintException e) {
                                Toast.makeText(MainActivity.this, "contact doesnt add", Toast.LENGTH_SHORT).show();

                            }

                        }


                    }else if(response.code()==403 ){

                        Toast.makeText(MainActivity.this, " token : invalid", Toast.LENGTH_LONG);
                    }else if (response.code()==404){
                        Toast.makeText(MainActivity.this, " 404 Not Found", Toast.LENGTH_LONG);
                    }
                }
                @Override
                public void onFailure(retrofit2.Call<List<Contact>> call, Throwable t) {
//                    showNoConnectionDialog(MainActivity.this);

                    setUpRecyclerView( mContactDao.getContacts() );

                }
            });

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


    void setUi (){
        contactsRecyclerView = findViewById(R.id.contactRecycler);

    }

}