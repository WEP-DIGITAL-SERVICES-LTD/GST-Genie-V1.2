package com.wepindia.pos.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.wep.common.app.Database.DatabaseHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;


public class FragmentPayPerUse extends Fragment {

    Context myContext ;
    public Activity myActivity ;
    ListView ll_meteringData;
    private MessageDialog MsgBox;
    public DatabaseHandler dbpayPerUse;
    CursorAdapter adapter;

    String[] columns = new String[] {
            "_id",
            "InvoiceDate",
            "BillCount"
    };

    // the XML defined views which the data will be bound to
    int[] to = new int[] {
            R.id.tv_id,
            R.id.tv_InvoiceDate,
            R.id.tv_InvoiceNo
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_pay_per_use, container, false);
        myActivity = getActivity();
        myContext = getContext();
        init(view);
        return view;

    }

    void init(View view)
    {
        MsgBox = new MessageDialog(myActivity);
        dbpayPerUse = new DatabaseHandler(myContext);
        dbpayPerUse.OpenDatabase();
        ll_meteringData = (ListView) view.findViewById(R.id.ll_meteringData);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();

    }

    void loadData()
    {
        new loaddataAsync().execute();
    }
    class loaddataAsync extends AsyncTask<Void, Void, Void>{

        ProgressDialog pd = new ProgressDialog(myContext);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Loading data...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Cursor cursor = dbpayPerUse.getMeteringData();
            if (cursor!=null && cursor.moveToNext())
            {
                adapter = new SimpleCursorAdapter(
                        myContext, R.layout.row_metering_data,  cursor,  columns,    to,     0);

            }else
                adapter = null;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(adapter ==null)
                MsgBox.Show("Information","No data to upload");
            ll_meteringData.setAdapter(adapter);

            pd.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbpayPerUse.CloseDatabase();
    }
}
