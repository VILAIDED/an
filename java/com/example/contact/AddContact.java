package com.example.contact;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.contact.databinding.ActivityAddContactBinding;
import com.example.contact.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class AddContact extends AppCompatActivity {
    ActivityAddContactBinding binding;
    private ContactDao contactDao;
    private Contact contact;
    private int contactId;
    AppDatabase appDatabase;
    private static final int SELECT_PIC = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddContactBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.addTool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        String title =  intent.getStringExtra("title");
        binding.addTool.setTitle(title);
        binding.addTool.inflateMenu(R.menu.menu_add);
        appDatabase = AppDatabase.getInstance(this);
        contactDao = appDatabase.contactDao();
        binding.profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),SELECT_PIC);
            }
        });

        contactId = intent.getIntExtra("contactId",0);
        if(contactId != 0){

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Contact c = contactDao.getContactById(contactId).get(0);
                    contact = c;
                    binding.edFullname.setText(c.getName());
                    binding.edPhone.setText(c.getPhone());
                    binding.edEmail.setText(c.getEmail());
                    byte[] bImg = contact.getImage();
                    if(bImg != null){
                        Bitmap b = BitmapFactory.decodeByteArray(bImg, 0, bImg.length);
                        binding.profileImg.setImageBitmap(b);
                    }
                }
            });




        }
    }
    void AddItem(){
        String phone = binding.edPhone.getText().toString();
        String name = binding.edFullname.getText().toString();
       // Toast.makeText(this, "Phone or Name cant empty", Toast.LENGTH_SHORT).show();
        String email = binding.edEmail.getText().toString();
        BitmapDrawable bd = (BitmapDrawable) binding.profileImg.getDrawable();
        Bitmap b = bd.getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 1, bos);
        byte[] img = bos.toByteArray();
        if(!("".equals(phone) || "".equals(name))){
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    if(contactId != 0){
                        contactDao.updateAll(new Contact(contactId,name,phone,email,img));
                    }else{
                        contactDao.insertAll(new Contact(name,phone,email,img));
                    }
//                    if(img != null){
//                        contactDao.insertAll(new Contact(name,phone,email,img));
//                    }else{
//                        contactDao.insertAll(new Contact(name,phone,email));
//                    }
                }
            });
            setResult(RESULT_OK);
            finish();
        }else{
            Toast.makeText(this, "Phone or Name cant empty", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu:
                AddItem();
                break;
            default:break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_PIC){

                Uri selectImgUri = data.getData();
                if(null != selectImgUri){
                    String path = getRealPathFromURI(getApplicationContext(),selectImgUri);
                    Log.i("Image upload : ", "Image Path : " + path);
                    binding.profileImg.setImageURI(selectImgUri);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}