package com.example.myaddressbook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ListItemHolder> {

    private static final String TAG = "ContactAdapter";
    private MainActivity mainActivity;
    private List<Contact> listOfContacts;


    public ContactAdapter(MainActivity mainActivity, List<Contact> listOfContacts) {
        this.mainActivity = mainActivity;
        this.listOfContacts = listOfContacts;
    }

    @NonNull
    @Override
    public ContactAdapter.ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_contacts, parent, false);

        ListItemHolder viewHolder = new ListItemHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ListItemHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder called.");

        Contact c = listOfContacts.get(position);
        holder.txtContactName.setText(c.getFullName());
//        holder.profileImage.setImageResource(R.drawable.profile_image);

        byte [] img = c.getProfileImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(img,0, img.length);
         holder.profileImage.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return listOfContacts.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView profileImage;
        TextView txtContactName;

        public ListItemHolder(View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImageCircleView);
            txtContactName = itemView.findViewById(R.id.lblContactName);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            Log.d(TAG, txtContactName.getText().toString());
        }

        @Override
        public void onClick(View view) {

            mainActivity.showContact(getAdapterPosition());

        }
    }
}
