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

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import chat.MessageActivity;
import model.ChatModel;
import model.User;

public class ChatFragment extends Fragment {
    private SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy.MM.dd.hh:mm");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_chat,container,false);

        RecyclerView recyclerView = view.findViewById(R.id.chatfragment_recyclerview);
        recyclerView.setAdapter(new ChatRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));


        return view;
    }
    class ChatRecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private  String uid;

        
        private List<ChatModel> chatModels = new ArrayList<>();
        private ArrayList<String> destnationUsers = new ArrayList<>();
        public ChatRecyclerViewAdapter() {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot item : snapshot.getChildren()){
                        chatModels.add(item.getValue(ChatModel.class));
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);


            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

            final CustomViewHolder customViewHolder = (CustomViewHolder)holder;
            String destinationUid = null;
            //챗방에 있는 유저를 체크
            for (String user: chatModels.get(position).users.keySet()){
                if (!user.equals(uid)){
                    destinationUid =user;
                    destnationUsers.add(destinationUid);
                }
            }
            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    Glide.with(customViewHolder.itemView.getContext())
                            .load(user.profileImageUrl)
                            .apply(new RequestOptions().circleCrop())
                            .into(customViewHolder.imageView);

                    customViewHolder.textView_title.setText(user.userName);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //메시지 내림차순후 마지막 메시지 키값가져옴
            Map<String, ChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
            commentMap.putAll(chatModels.get(position).comments);
            String lastMessageKey  = (String)commentMap.keySet().toArray()[0];
            customViewHolder.textView_last_message.setText(chatModels.get(position).comments.get(lastMessageKey).message);

            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getView().getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid",destnationUsers.get(position));

                    ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(getView().getContext(),R.anim.fromright,R.anim.toleft );
                   startActivity(intent,activityOptions.toBundle());
                }
            });

     simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

     long unixTime =(long) chatModels.get(position).comments.get(lastMessageKey).timestamp;
     Date date  = new Date(unixTime);
     customViewHolder.textView_timestmap.setText(simpleDateFormat.format(date));

        }



        @Override
        public int getItemCount() {
            return chatModels.size();
        }
        private class CustomViewHolder extends  RecyclerView.ViewHolder{
            public ImageView imageView;
            public TextView textView_title;
            public TextView textView_last_message;
            public TextView textView_timestmap;


            public CustomViewHolder(View view){
                super(view);
            imageView = view.findViewById(R.id.chatitem_iamgeview);
            textView_title =view.findViewById(R.id.chatitem_textview_title);
            textView_last_message = view.findViewById(R.id.chatitem_textview_lastmessage);
                textView_timestmap = view.findViewById(R.id.chatitem_textview_timestamp);
            }
        }
    }
}
