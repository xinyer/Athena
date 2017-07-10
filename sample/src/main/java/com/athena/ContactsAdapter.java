package com.athena;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.athena.library.data.SearchableEntity;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<SearchableEntity> searchableEntityList = new ArrayList<>();

    public ContactsAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_contacts, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchableEntity entity = this.searchableEntityList.get(position);
        ContactsEntity contacts = ContactsManager.getInstance().getContacts(entity.getKeyValue());
        holder.tvName.setText(contacts.getName());
        holder.tvPhone.setText(contacts.getPhone());
    }

    @Override
    public int getItemCount() {
        return this.searchableEntityList.size();
    }

    public void update(List<SearchableEntity> list) {
        this.searchableEntityList.clear();
        this.searchableEntityList.addAll(list);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvPhone;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
        }
    }
}
