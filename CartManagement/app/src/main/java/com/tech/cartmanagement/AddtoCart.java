package com.tech.cartmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tech.cartmanagement.models.CartItems;
import com.tech.cartmanagement.models.LaptopModelClass;

import java.util.ArrayList;
import java.util.List;

public class AddtoCart extends AppCompatActivity {

    Button button;
    ListView listView;
    List<LaptopModelClass> user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addto_cart);

        listView = (ListView) findViewById(R.id.listview);
         button = (Button) findViewById(R.id.gotocart);

        user = new ArrayList<>();
//Go to cart button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddtoCart.this, CartItemList.class);
                startActivity(intent);
            }
        });
// firebase Data base table
        ref = FirebaseDatabase.getInstance().getReference("Laptops");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();
// datasnapshort is as class handaling with data
                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()) {

                    LaptopModelClass laptopModelClass = studentDatasnap.getValue(LaptopModelClass.class);
                    user.add(laptopModelClass);
                }

                MyAdapter adapter = new MyAdapter(AddtoCart.this, R.layout.custom_laptop_item, (ArrayList<LaptopModelClass>) user);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder {
//Add to cart button image view show
        ImageView imageView;
        TextView COL1;
        TextView COL2;
        TextView COL3;
        Button button;
    }

    class MyAdapter extends ArrayAdapter<LaptopModelClass> {
        LayoutInflater inflater;
        Context myContext;
        List<LaptopModelClass> user;


        public MyAdapter(Context context, int resource, ArrayList<LaptopModelClass> objects) {
            super(context, resource, objects);
            myContext = context;
            user = objects;
//It s can show layout  in top of another layout
            inflater = LayoutInflater.from(context);
            int y;
            String barcode;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.custom_laptop_item, null);

                holder.COL1 = (TextView) view.findViewById(R.id.item_id);
                holder.COL2 = (TextView) view.findViewById(R.id.item_name);
                holder.COL3 = (TextView) view.findViewById(R.id.item_price);
                holder.imageView = (ImageView) view.findViewById(R.id.item_image);
                holder.button = (Button) view.findViewById(R.id.viewcartItem);


                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.COL1.setText(user.get(position).getId());
            holder.COL2.setText(user.get(position).getName());
            holder.COL3.setText(user.get(position).getPrice());
            Picasso.get().load(user.get(position).getImage()).into(holder.imageView);
            System.out.println(holder);


            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//AlertDialog in cart
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view = inflater.inflate(R.layout.custom_addtocart, null);
                    dialogBuilder.setView(view);

                    final TextView textView1 = (TextView) view.findViewById(R.id.cId);
                    final TextView textView2 = (TextView) view.findViewById(R.id.cName);
                    final TextView textView3 = (TextView) view.findViewById(R.id.cPrice);
                    final TextView textView4 = (TextView) view.findViewById(R.id.cDescription);
                    final EditText editText = (EditText) view.findViewById(R.id.cQty);
                    final ImageView imageView1 = (ImageView) view.findViewById(R.id.imageItem);
                    final Button buttonAdd = (Button) view.findViewById(R.id.addnow);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final String idd = user.get(position).getId();
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Laptops").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
    // Data retrevw
                            String id = (String) snapshot.child("id").getValue();
                            String name = (String) snapshot.child("name").getValue();
                            String price = (String) snapshot.child("price").getValue();
                            String description = (String) snapshot.child("details").getValue();
                            String image = (String) snapshot.child("image").getValue();

                            textView1.setText(id);
                            textView2.setText(name);
                            textView3.setText(price);
                            textView4.setText(description);
                            Picasso.get().load(image).into(imageView1);

                            buttonAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CartItems");
//edit text in car pop up massge
                                    final String qty = editText.getText().toString();
                                    String Id = textView1.getText().toString();
                                    String Name = textView2.getText().toString();
                                    String Price = textView3.getText().toString();
                                    String Description = textView4.getText().toString();
                                    String image = snapshot.child("image").getValue().toString();
// validation in car quantity
                                    if (qty.isEmpty()) {
                                        editText.setError("Quantity is required");
                                    }else {
//insert
                                        CartItems cartItems = new CartItems(Id,Name,Price,Description,qty,image);
                                        reference.child(Id).setValue(cartItems);

                                        Toast.makeText(AddtoCart.this, "Successfully added", Toast.LENGTH_SHORT).show();

                                        alertDialog.dismiss();
                                    }

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });

                }

            });

            return view;

        }
    }
}