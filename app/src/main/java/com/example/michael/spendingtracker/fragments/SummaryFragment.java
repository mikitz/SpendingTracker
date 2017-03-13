package com.example.michael.spendingtracker.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michael.spendingtracker.R;
import com.example.michael.spendingtracker.adapters.CategoryAdapter;
import com.example.michael.spendingtracker.SQLite.CategoryContract;
import com.example.michael.spendingtracker.SQLite.DBHelper;

public class SummaryFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private CategoryAdapter mAdapter;
    SQLiteDatabase mDb;

    public SummaryFragment() {
        // Required empty public constructor
    }

    public static SummaryFragment newInstance(String param1, String param2) {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RecyclerView categoryRecyclerView;
        categoryRecyclerView = (RecyclerView) this.getActivity().findViewById(R.id.category_recylerview);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        DBHelper DBHelper = new DBHelper(this.getActivity());
        mDb = DBHelper.getReadableDatabase();
        Cursor cursor = getAllCategories();
        mAdapter = new CategoryAdapter(this.getActivity(), cursor);

        categoryRecyclerView.setAdapter(mAdapter);
    }

    private Cursor getAllCategories() {
        return mDb.query(
                CategoryContract.CategoryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                CategoryContract.CategoryEntry.COLUMN_CATEGORY_NAME
        );
    }

    public void onResume() {
        super.onResume();
        mAdapter.swapCursor(getAllCategories());
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
