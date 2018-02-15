package com.example.eforezan.kuevents;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomePage extends AppCompatActivity {

    private RecyclerView mEventList;

    private DatabaseReference mDatabase;

    private DatabaseReference sDatabase;

    private FirebaseAuth mAuth;

    private FirebaseUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");

        mEventList = (RecyclerView) findViewById(R.id.event_list);
        mEventList.setHasFixedSize(true);
        mEventList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Event, EventViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(
                Event.class,
                R.layout.event_row,
                EventViewHolder.class,
                mDatabase
                ) {
            @Override
            protected void populateViewHolder(EventViewHolder viewHolder, Event model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setStart_Date(model.getStart_date());

                viewHolder.setLatitude(model.getLatitude());
                viewHolder.setLongitude(model.getLongitude());
                // This is used in second.java




                viewHolder.mView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(HomePage.this, post_key, Toast.LENGTH_LONG).show();
                        Intent singleEventIntent = new Intent(HomePage.this, second.class);
                        singleEventIntent.putExtra("event_id", post_key);
                        startActivity(singleEventIntent);
                    }
                });


                }
                //};
            //}
        };
        mEventList.setAdapter(firebaseRecyclerAdapter);


    }

    public static class EventViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public EventViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }
        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);

        }



        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);

        }

        public void setStart_Date(String start_date){
            TextView post_date=(TextView) mView.findViewById(R.id.post_date);
            post_date.setText(start_date);
        }

        public void setLatitude(double Latitude){
            TextView post_lat = (TextView) mView.findViewById(R.id.post_latitude);
            String lat = Double.toString(Latitude);
            post_lat.setText(lat);

        }

        public void setLongitude(double Longitude){
            TextView post_lon = (TextView) mView.findViewById(R.id.post_longitude);
            String longi = Double.toString(Longitude);
            post_lon.setText(longi);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        sDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        switch (item.getItemId()) {
            case R.id.action_logout:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));

                break;

            case R.id.action_add:

                String uid = mCurrentUser.getUid().toString().trim();

                sDatabase.child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String role = (String) dataSnapshot.child("role").getValue();

                        if (role.equals("Admin")){
                            Toast.makeText(HomePage.this, "You are admin", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(HomePage.this, PostActivity.class));

                        }

                        if (role.equals("Normal"))
                        {
                            View b = findViewById(R.id.action_add);
                            b.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



               // Toast.makeText(HomePage.this, uid, Toast.LENGTH_LONG).show();





                break;

        }

        return super.onOptionsItemSelected(item);
    }
}


