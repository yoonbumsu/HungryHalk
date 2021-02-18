package Fragment;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hungrytalk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import chat.MessageActivity;
import model.User;


public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile,container,false);

       RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
       recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new ProfileFragmentRecyclerViewAdapter());
        return view;
    }
    class ProfileFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
      List<User> users;
        public ProfileFragmentRecyclerViewAdapter(){
            users = new ArrayList<>();
           final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                           //db에서 불러옴
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    users.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                       User user = snapshot.getValue(User.class);

                       if (myUid.equals(user.uid)){
                           continue;
                       }
                       users.add(user);
                    }
                    notifyDataSetChanged();//새로고침
                }

                @Override
                public void onCancelled( DatabaseError error) {

                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_user_list,parent,false);


            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
           Glide.with(holder
                    .itemView.getContext())
                    .load(users.get(position).profileImageUrl)
                    .apply(new RequestOptions()
                            .circleCrop())
                    .into(((CustomViewHolder)holder).imageView);
            ((CustomViewHolder)holder).textView.setText(users.get(position).userName);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getView().getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid",users.get(position).uid);
                  ActivityOptions options = null;
                  options = ActivityOptions.makeCustomAnimation(getView().getContext(),R.anim.fromright,R.anim.toleft);

                            startActivity(intent, options.toBundle());



                }

                });
        }


        @Override
        public int getItemCount() {
            return users.size();
        }
        private class CustomViewHolder extends RecyclerView.ViewHolder {

            public ImageView imageView;
            public TextView textView;

            public  CustomViewHolder(View view){
                super(view);

                imageView=(ImageView)view.findViewById(R.id.photoImageitem);

                textView =  (TextView)view.findViewById(R.id.nameitem);
            }
        }
    }

}
