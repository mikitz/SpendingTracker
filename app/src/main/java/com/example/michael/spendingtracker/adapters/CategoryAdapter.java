package com.example.michael.spendingtracker.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.michael.spendingtracker.R;
import com.example.michael.spendingtracker.SQLite.CategoryContract;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private Context mContext;
    private Cursor mCursor;
    SQLiteDatabase mDb;

    public CategoryAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.categories_list_item, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
            return;
        String name = mCursor.getString(
                mCursor.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_CATEGORY_NAME));
        long id = mCursor.getLong(
                mCursor.getColumnIndex(CategoryContract.CategoryEntry._ID));
        int sum = mCursor.getInt(
                mCursor.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_CATEGORY_SUM));

        holder.catName.setText(name);
        holder.itemView.setTag(id);
        holder.catSpent.setText(String.valueOf(sum));
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

    class CategoryHolder extends RecyclerView.ViewHolder {
        TextView catName, catSpent, catPercent;

        public CategoryHolder(View itemView) {
            super(itemView);
            catName = (TextView) itemView.findViewById(R.id.catName);
            catSpent = (TextView) itemView.findViewById(R.id.catSpent);
            catPercent = (TextView) itemView.findViewById(R.id.catPercent);
        }
    }
}