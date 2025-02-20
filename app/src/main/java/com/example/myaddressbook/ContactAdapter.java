package com.example.myaddressbook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ListItemHolder> implements Filterable {

    private MainActivity mainActivity;
    private List<Contact> listOfContacts;
    private List<Contact> filteredContactList;

    public ContactAdapter(MainActivity mainActivity, List<Contact> listOfContacts) {
        this.mainActivity = mainActivity;
        this.listOfContacts = listOfContacts;

        this.filteredContactList = listOfContacts;

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
//        Contact c = listOfContacts.get(position);
        Contact c = filteredContactList.get(position);

        holder.txtContactName.setText(c.getFullName());

        byte[] img = c.getProfileImage();
        if (img != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            holder.profileImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return filteredContactList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
//            List<Contact> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredContactList = listOfContacts;
//                filteredList.addAll(filteredContactList);
            } else {
                List<Contact> filteredList = new ArrayList<>();
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Contact item : filteredContactList) {
                    if (item.getFullName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
                filteredContactList = filteredList;
            }

            FilterResults results = new FilterResults();
            results.values = filteredContactList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
//            listOfContacts.clear();
//            listOfContacts.addAll((List) results.values);
            filteredContactList = (List<Contact>) results.values;
            notifyDataSetChanged();
        }
    };

    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        CircleImageView profileImage;
        TextView txtContactName;

        public ListItemHolder(View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImageCircleView);
            txtContactName = itemView.findViewById(R.id.lblContactName);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {

            mainActivity.showContact(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            mainActivity.showContextMenu(getAdapterPosition());
            return true;
        }


    }
}
