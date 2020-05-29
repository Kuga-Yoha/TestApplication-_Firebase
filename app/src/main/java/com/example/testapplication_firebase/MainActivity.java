package com.example.testapplication_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    static EditText txtID, txtName, txtAdd, txtConNo;
    Button btnSave, btnShow, btnUpdate, btnDelete;
    DatabaseReference dbRef;
    Student std;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtID = findViewById(R.id.EtID);
        txtName=findViewById(R.id.EtName);
        txtAdd=findViewById(R.id.EtAddress);
        txtConNo=findViewById(R.id.EtConNo);

        btnSave=findViewById(R.id.saveBtn);
        btnShow=findViewById(R.id.showBtn);
        btnUpdate=findViewById(R.id.updateBtn);
        btnDelete=findViewById(R.id.deleteBtn);

      std=new Student();

      btnSave.setOnClickListener(new View.OnClickListener(){


          @Override
          public void onClick(View v) {
                saveData();
          }
      });

      btnShow.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              showData();
          }
      });

      btnUpdate.setOnClickListener(new View.OnClickListener(){

          @Override
          public void onClick(View v) {
              updateData();
          }
      });

      btnDelete.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              deleteData();
          }
      });

    }



   // method to clear all inputs
    private void clearControls(){
        txtID.setText("");
        txtName.setText("");
        txtAdd.setText("");
        txtConNo.setText("");

    }


    void saveData(){
        dbRef= FirebaseDatabase.getInstance().getReference().child("Student");

        try{
            if(TextUtils.isEmpty(txtID.getText().toString())){
                Toast.makeText(getApplicationContext(), "Please enter an ID", Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(txtName.getText().toString())){
                Toast.makeText(getApplicationContext(), "Please enter a Name", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(txtAdd.getText().toString())){
                Toast.makeText(getApplicationContext(), "Please enter an Address", Toast.LENGTH_SHORT).show();
            }else {
                std.setID(txtID.getText().toString());
                std.setName(txtName.getText().toString());
                std.setAddress(txtAdd.getText().toString());
                std.setConNo(Integer.parseInt(txtConNo.getText().toString()));

                //Insert into datatbase
              //  dbRef.push().setValue(std);
                  dbRef.child("std_"+std.getID()).setValue(std);
                //feedback the user via a toast
                Toast.makeText(getApplicationContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
                clearControls();

            }

        }catch(NumberFormatException e){
            Toast.makeText(getApplicationContext(), "Invalid Contact Number", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Unexpected Error", Toast.LENGTH_SHORT).show();
            Log.i("Exception", String.valueOf(e));
        }

     }

    void showData(){
        std.setID(txtID.getText().toString());
        DatabaseReference readRef=FirebaseDatabase.getInstance().getReference().child("Student").child("std_"+std.getID());

        readRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    txtID.setText(dataSnapshot.child("id").getValue().toString());
                    txtName.setText(dataSnapshot.child("name").getValue().toString());
                    txtAdd.setText(dataSnapshot.child("address").getValue().toString());
                    txtConNo.setText(dataSnapshot.child("conNo").getValue().toString());
                }else
                    Toast.makeText(getApplicationContext(), "No Source to Display", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void updateData(){
        std.setID(txtID.getText().toString());
        DatabaseReference updateRef= FirebaseDatabase.getInstance().getReference().child("Student");
        updateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("std_"+std.getID())){
                    try{
                        std.setID(txtID.getText().toString().trim());
                        std.setName(txtName.getText().toString().trim());
                        std.setAddress(txtAdd.getText().toString().trim());
                        std.setConNo(Integer.parseInt(txtConNo.getText().toString().trim()));

                        dbRef= FirebaseDatabase.getInstance().getReference().child("Student").child("std_"+std.getID());
                        dbRef.setValue(std);
                        clearControls();
                        //feedback the user via a toast
                        Toast.makeText(getApplicationContext(), "Data Updated successfully", Toast.LENGTH_SHORT).show();




                    }catch(NumberFormatException e){
                        Toast.makeText(getApplicationContext(), "Invalid Contact Number", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "Unexpected Error", Toast.LENGTH_SHORT).show();
                        Log.i("Exception", String.valueOf(e));
                    }
                }  else {
                    Toast.makeText(getApplicationContext(), "No source to update", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    void deleteData() {
        DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference().child("Student");
        std.setID(txtID.getText().toString().trim());
        deleteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("std_"+std.getID())) {

                       dbRef= FirebaseDatabase.getInstance().getReference().child("Student").child("std_"+std.getID());
                       dbRef.removeValue();
                       clearControls();
                       Toast.makeText(getApplicationContext(), "Data deleted succesfully", Toast.LENGTH_SHORT).show();

                } else {
                      Toast.makeText(getApplicationContext(),"No source to delete", Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}
