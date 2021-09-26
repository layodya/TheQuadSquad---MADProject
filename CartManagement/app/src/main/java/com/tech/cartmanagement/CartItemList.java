package com.tech.cartmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.HashMap;
import java.util.List;

public class CartItemList extends AppCompatActivity {

    ListView listView;
    List<CartItems> user;
    DatabaseReference ref;
    String idd;
    Button faq ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_item_list);

        faq = findViewById(R.id.btn_faq);

        listView = (ListView) findViewById(R.id.cartList);
        user = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("CartItems");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()) {

                    CartItems cartItems = studentDatasnap.getValue(CartItems.class);
                    user.add(cartItems);
                }

                MyAdapter adapter = new MyAdapter(CartItemList.this, R.layout.custom_cart_item, (ArrayList<CartItems>) user);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    static class ViewHolder {

        ImageView imageView1;
        ImageButton imageButton1;
        ImageButton imageButton2;
        TextView COL1;
        TextView COL2;
        TextView COL3;
    }

    class MyAdapter extends ArrayAdapter<CartItems> {
        LayoutInflater inflater;
        Context myContext;
        List<CartItems> user;


        public MyAdapter(Context context, int resource, ArrayList<CartItems> objects) {
            super(context, resource, objects);
            myContext = context;
            user = objects;
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
                view = inflater.inflate(R.layout.custom_cart_item, null);

                holder.COL1 = (TextView) view.findViewById(R.id.cartName);
                holder.COL2 = (TextView) view.findViewById(R.id.cartPrice);
                holder.COL3 = (TextView) view.findViewById(R.id.cartQty);
                holder.imageView1 = (ImageView) view.findViewById(R.id.cartimage);
                holder.imageButton1 = (ImageButton) view.findViewById(R.id.cartEdit);
                holder.imageButton2 = (ImageButton) view.findViewById(R.id.cartDelete);


                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.COL1.setText(user.get(position).getName());
            holder.COL2.setText(user.get(position).getPrice());
            holder.COL3.setText(user.get(position).getQty());
            Picasso.get().load(user.get(position).getImage()).into(holder.imageView1);
            System.out.println(holder);

            idd = user.get(position).getId();

            //delete button
            holder.imageButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("Do you want to delete this item?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    final String idd = user.get(position).getId();
                                    FirebaseDatabase.getInstance().getReference("CartItems").child(idd).removeValue();
                                    //remove function not written
                                    Toast.makeText(myContext, "Item deleted successfully", Toast.LENGTH_SHORT).show();

                                }
                            })

                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();
                }
            });

            // update button
            holder.imageButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view1 = inflater.inflate(R.layout.custom_update_cart, null);
                    dialogBuilder.setView(view1);

                    final TextView textView1 = (TextView) view1.findViewById(R.id.cIdU);
                    final TextView textView2 = (TextView) view1.findViewById(R.id.cNameU);
                    final TextView textView3 = (TextView) view1.findViewById(R.id.cPriceU);
                    final TextView textView4 = (TextView) view1.findViewById(R.id.cDescriptionU);
                    final EditText editText = (EditText) view1.findViewById(R.id.cQtyU);
                    final ImageView imageView1 = (ImageView) view1.findViewById(R.id.imageItemU);
                    final Button buttonupdate = (Button) view1.findViewById(R.id.update);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final String idd = user.get(position).getId();
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CartItems").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = (String) snapshot.child("id").getValue();
                            String name = (String) snapshot.child("name").getValue();
                            String price = (String) snapshot.child("price").getValue();
                            String description = (String) snapshot.child("details").getValue();
                            String qty = (String) snapshot.child("qty").getValue();
                            String image = (String) snapshot.child("image").getValue();

                            textView1.setText(id);
                            textView2.setText(name);
                            textView3.setText(price);
                            textView4.setText(description);
                            editText.setText(qty);
//Picasso is a library it will handel  the imagees , Grdle scripts -> 2nd bulild .gradle -> last
                            Picasso.get().load(image).into(imageView1);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    buttonupdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String qty = editText.getText().toString();


                            if (qty.equals("")) {
                                editText.setError("Quantity is required");
                            }else {
//update funsion
// hasmap is class like a arry
                                HashMap map = new HashMap();
                                map.put("qty", qty);
                                reference.updateChildren(map);

                                Toast.makeText(CartItemList.this, "Updated successfully", Toast.LENGTH_SHORT).show();

                                alertDialog.dismiss();
                            }
                        }
                    });
                }
            });

            faq.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    startActivity(new Intent(getApplicationContext(),FAQ.class));
                }
            });

            return view;

        }
    }

}