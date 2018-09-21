package com.example.sanamyavarpour.contact_list.Adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;

import android.widget.Filter;

import com.example.sanamyavarpour.contact_list.Model.Contact;
import com.example.sanamyavarpour.contact_list.R;

import java.util.ArrayList;
import java.util.List;

public class ContactRecyclerAdapter  extends RecyclerView.Adapter <ContactRecyclerAdapter.ViewHolder>
        implements Filterable {

    private Context context;
    private List<Contact> contactList;
    private List<Contact> contactListFiltered;

    public ContactRecyclerAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.contactListFiltered = contactList;

    }

    public ContactRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate( R.layout.item_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactRecyclerAdapter.ViewHolder holder, int position) {

        holder.tvName.setText( contactList.get( position ).getName() );
        holder.tvPhone.setText( contactList.get( position ).getPhone() );
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }


    void updateData(List<Contact> contacts) {
        this.contactList = contacts;
        contactList.addAll( contacts );
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvPhone;
        public ViewHolder(View itemView) {
            super( itemView );

            tvName = itemView.findViewById( R.id.tvName );
            tvPhone = itemView.findViewById( R.id.tvPhone );
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact row : contactList) {

                        if (row.getPhone().contains(charString)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
