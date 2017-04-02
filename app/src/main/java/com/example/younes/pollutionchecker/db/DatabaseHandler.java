package com.example.younes.pollutionchecker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.younes.pollutionchecker.model.CityObject;
import com.example.younes.pollutionchecker.model.Data;
import com.example.younes.pollutionchecker.model.GlobalObject;
import com.example.younes.pollutionchecker.model.MessageObject;
import com.example.younes.pollutionchecker.model.RxsObject;
import com.example.younes.pollutionchecker.model.SearchObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class with methods to store/update/delete in private SQLite database
 * Created by elyandoy on 27/03/2017.
 */

public class DatabaseHandler implements DatabaseStruct{

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    public DatabaseHandler(Context mCtx) {
        this.mCtx = mCtx;
    }

    /**
     * Method to
     */
    public void open() {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }

    public void add(SearchObject mObj) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, mObj.getIdx());
        values.put(KEY_LOCATION_CITY, mObj.getCityName());
        mDb.insert(TABLE_LOCATION, null, values);
    }

    public List<GlobalObject> getAllObj() {

        String[] columns = new String[] {
                KEY_ID,
                KEY_LOCATION_CITY
        };
        Cursor cursor = mDb.query(TABLE_LOCATION, columns, "1", null, null, null, null);
        int iPostId = cursor.getColumnIndex(KEY_ID);
        int iPostCity = cursor.getColumnIndex(KEY_LOCATION_CITY);

        List<GlobalObject> objects = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                GlobalObject obj = new GlobalObject();
                RxsObject rxs = new RxsObject();
                MessageObject msg = new MessageObject();
                CityObject city = new CityObject();
                ArrayList<Data> datas = new ArrayList<>();
                Data data = new Data();

                city.setIdx(cursor.getInt(iPostId));
                city.setName(cursor.getString(iPostCity));

                obj.setRxs(rxs);
                data.setMsg(msg);
                datas.add(data);
                rxs.setObs(datas);
                msg.setCity(city);
                objects.add(obj);

            } while(cursor.moveToNext());
        }

        return objects;
    }

    public void removeObj(GlobalObject mObj) {
        mDb.delete(TABLE_LOCATION, KEY_ID + "= ?", new String[]{String.valueOf(mObj.getRxs().getObs().get(0).getMsg().getCity().getIdx())});
    }

    public void close() {
        mDbHelper.close();
    }
}
