package com.example.michael.spendingtracker.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.michael.spendingtracker.R;
import com.example.michael.spendingtracker.SQLite.TransactionContract;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {
    private Context mContext;
    private Cursor mCursor;

    public TransactionAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.transaction_list_item, parent, false);
        return new TransactionHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
            return;
        String name = mCursor.getString(
                mCursor.getColumnIndex(TransactionContract.TransactionEntry.COLUMN_TRANSACTION_ITEM));
        long id = mCursor.getLong(
                mCursor.getColumnIndex(TransactionContract.TransactionEntry._ID));
        String category = mCursor.getString(
                mCursor.getColumnIndex(TransactionContract.TransactionEntry.COLUMN_TRANSACTION_CATEGORY));
        double cost = mCursor.getDouble(
                mCursor.getColumnIndex(TransactionContract.TransactionEntry.COLUMN_TRANSACTION_COST));
        int quantity = mCursor.getInt(
                mCursor.getColumnIndex(TransactionContract.TransactionEntry.COLUMN_TRANSACTION_QUANTITY));
        String timestamp = mCursor.getString(
                mCursor.getColumnIndex(TransactionContract.TransactionEntry.COLUMN_TRANSACTION_TIMESTAMP));

        holder.ItemName.setText(name);
        holder.ItemCategory.setText(category);
        holder.ItemCost.setText(String.valueOf(cost));
        holder.ItemQuantity.setText(String.valueOf(quantity));
        holder.ItemTimestamp.setText(timestamp);
        holder.itemView.setTag(id);

    }
    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }

    class TransactionHolder extends RecyclerView.ViewHolder {
        TextView ItemName, ItemCategory, ItemCost, ItemQuantity, ItemTimestamp;

        public TransactionHolder(View itemView) {
            super(itemView);
            ItemName = (TextView) itemView.findViewById(R.id.itemName);
            ItemCategory = (TextView) itemView.findViewById(R.id.itemCategory);
            ItemCost = (TextView) itemView.findViewById(R.id.itemCost);
            ItemQuantity = (TextView) itemView.findViewById(R.id.itemQuantity);
            ItemTimestamp = (TextView) itemView.findViewById(R.id.itemTimestamp);
        }
    }
}