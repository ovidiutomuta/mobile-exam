package ro.ubbcluj.cs.exam.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ro.ubbcluj.cs.exam.BuyItem;
import ro.ubbcluj.cs.exam.EventDetailActivity;
import ro.ubbcluj.cs.exam.EventDetailFragment;
import ro.ubbcluj.cs.exam.Manager;
import ro.ubbcluj.cs.exam.domain.Item;
import ro.ubbcluj.cs.exam.test.R;

public class MyAdapter
        extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private final List<Item> mValues;
    private Manager manager;

    public MyAdapter() {

        mValues = new ArrayList<>();
        manager = new Manager();
    }

    public void addData(Item item) {
        mValues.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        mValues.clear();
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
        holder.mContentView.setText(mValues.get(position).getName());
        holder.mQuantity.setText(String.valueOf(mValues.get(position).getQuantity()));
        holder.mStatus.setText(String.valueOf(mValues.get(position).getStatus()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                // Intent intent = new Intent(context, EventDetailActivity.class);
                // intent.putExtra(EventDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.getId()));
                Intent intent = new Intent(context, BuyItem.class);
                intent.putExtra("name", holder.mItem.getName());
                intent.putExtra("id", String.valueOf(holder.mItem.getId()));
                context.startActivity(intent);
            }
        });
    }

    public void updateItems() {
        List<Item> items = new ArrayList<Item>();
        items = manager.getItems();
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            Item item2 = mValues.get(i);
            if (item.getStatus() != item2.getStatus()) {
                item2.setStatus(item.getStatus());
            }
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mQuantity;
        public final TextView mStatus;
        public Item mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mQuantity = (TextView) view.findViewById(R.id.quantity);
            mStatus = (TextView) view.findViewById(R.id.price);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
