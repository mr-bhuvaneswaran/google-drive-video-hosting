package status.com.status;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {

    private Toolbar homeTool;
    private RecyclerView recyclerView;
    private DatabaseReference firebaseDatabase;
    public FirebaseRecyclerAdapter<DataSets,CategoryViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("category");
        firebaseDatabase.keepSynced(true);
        homeTool = (Toolbar) findViewById(R.id.home_bar);
        setSupportActionBar(homeTool);
        getSupportActionBar().setTitle("Status");
        recyclerView = (RecyclerView) findViewById(R.id.home_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<DataSets> options =
                new FirebaseRecyclerOptions.Builder<DataSets>()
                        .setQuery(firebaseDatabase, DataSets.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataSets, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position, @NonNull final DataSets model) {
                holder.setName(model.getName());


                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String name = model.getName();
                        Intent videoIntent = new Intent(Home.this,Video.class);
                        videoIntent.putExtra("category",name);
                        startActivity(videoIntent);
                    }
                });
            }


            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false);
                return new CategoryViewHolder(view);
            }


        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{

        View mview;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void setName(String s){
            TextView category = (TextView) mview.findViewById(R.id.category);
            category.setText(s);
        }
    }
}
