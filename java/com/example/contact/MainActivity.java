package com.example.contact;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.contact.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ItemClickListener {
    private ActivityMainBinding binding;
    private ArrayList<Contact> contactList;
    private ArrayList<Contact> allContacts;
    private ContactAdapter contactAdapter;
    private AppDatabase appDatabase;
    private ContactDao contactDao;
    private static final int REQ_CODE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xffF7CAC9));

        appDatabase = AppDatabase.getInstance(this);
        contactDao = appDatabase.contactDao();
        contactList = new ArrayList<>();
        allContacts = new ArrayList<>();
        contactAdapter = new ContactAdapter(contactList,this);
        getList();
        String[] arrColor ={"#34568B","#FF6F61","#6B5B95","#88B04B","#F7CAC9"};

        binding.rvContacts.setAdapter(contactAdapter);
        binding.rvContacts.setLayoutManager(new LinearLayoutManager(this));
        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddContact.class);
                intent.putExtra("title","Add Contact");
                startActivityForResult(intent,REQ_CODE);
            }
        });



//        binding.tvSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                search_name();
//            }
//        });
    }
    void getList(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<Contact> ctList = contactDao.getAllConteact();
                contactList.clear();
                allContacts.clear();
                contactList.addAll(ctList);
                allContacts.addAll(ctList);

            }
        });
        contactAdapter.notifyDataSetChanged();
    }

    void search_name(String search_text){
        ArrayList<Contact> dList = new ArrayList<>();
        for (int i = 0; i < allContacts.size(); i++) {
          if(allContacts.get(i).getName().toUpperCase().contains(search_text.toUpperCase())){
              dList.add(allContacts.get(i));
          }
        }
        contactList.clear();
        contactList.addAll(dList);
        contactAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

//        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem menuItem) {
//
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
//                return false;
//            }
//        };
//        menu.findItem(R.id.search).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Search hear");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search_name(s);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            getList();
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(this,AddContact.class);
        intent.putExtra("title","Edit Contact");
        intent.putExtra("contactId",contactList.get(position).getId());
        startActivityForResult(intent,REQ_CODE);
    }
}