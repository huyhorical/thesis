package com.kdoctor.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.kdoctor.models.Function;
import com.kdoctor.models.Vaccine;
import com.kdoctor.services.VaccineService;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Huy on 10/29/2017.
 */

public class DbManager extends SQLiteOpenHelper {

    private static DbManager dbManager;
    public static DbManager getInstance(Context context){
        if (dbManager == null){
            dbManager = new DbManager(context);
        }
        return dbManager;
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Kdoctor.db";

    public static final String DRUGS = "DRUGS";
    public static final String SICKNESSES = "SICKNESSES";
    public static final String DIAGNOSIS = "DIAGNOSIS";
    public static final String VACCINES = "VACCINES";
    public static final String FUNCTIONS = "FUNCTIONS";
    public static final String CODES = "CODES";

    public DbManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;

        if (!isTableExists(db, CODES)) {
            query = "create table " + CODES + " ("
                    + "VALUE text primary key,"
                    + "CATEGORY_DATA_PATH text, "
                    + "CATEGORY_NAME text, "
                    + "CATEGORY_ACTION text, "
                    + "DATE text)";
            db.execSQL(query);
        }

        if (!isTableExists(db, DRUGS)) {
            query = "create table " + DRUGS + " ("
                    + "ID integer primary key,"
                    + "NAME text, "
                    + "PRODUCER text, "
                    + "COMPONENT text, "
                    + "USES text, "
                    + "GUIDE text, "
                    + "CAUTION text, "
                    + "IMAGE_URL text, "
                    + "NOTE text, "
                    + "SELECTED int)";
            db.execSQL(query);
        }

        if (!isTableExists(db, SICKNESSES)) {
            query = "create table " + SICKNESSES + " ("
                    + "ID integer primary key,"
                    + "CATEGORY_ID integer, "
                    + "NAME text, "
                    + "SUMMARY text, "
                    + "PROGNOSTIC text, "
                    + "TREATMENT text, "
                    + "IMAGE_URL text, "
                    + "NOTE text, "
                    + "SELECTED int)";
            db.execSQL(query);
        }

        if (!isTableExists(db, DIAGNOSIS)) {
            query = "create table " + DIAGNOSIS + " ("
                    + "ID integer primary key AUTOINCREMENT,"
                    + "RESULT text, "
                    + "CODE text, "
                    + "DATE text)";
            db.execSQL(query);
        }

        if (!isTableExists(db, VACCINES)) {
            query = "create table " + VACCINES + " ("
                    + "ID integer primary key,"
                    + "ACTIVITY text, "
                    + "START_MONTH integer, "
                    + "END_MONTH integer, "
                    + "NOTE text, "
                    + "MESSAGE text, "
                    + "ALARM_DATE text, "
                    + "READ int, "
                    + "SELECTED int)";
            db.execSQL(query);
        }

        if (!isTableExists(db, FUNCTIONS)) {
            query = "create table " + FUNCTIONS + " ("
                    + "TAB text,"
                    + "FUNCTION text, "
                    + "STATUS text," +
                    "PRIMARY KEY (TAB, FUNCTION))";
            db.execSQL(query);

            List<Function> functions = new ArrayList<Function>();
            functions.add(new Function(VACCINES, "DISPLAY_TYPE", "ALL"));
            functions.add(new Function(VACCINES, "REMINDER", "OFF"));
            functions.add(new Function(VACCINES, "BIRTHDAY_FILTER", "OFF"));

            for (Function f :
                    functions) {
                ContentValues values = new ContentValues();
                values.put("TAB", f.getTab());
                values.put("FUNCTION", f.getFunction());
                values.put("STATUS", f.getStatus());
                db.insert(FUNCTIONS, null, values);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean isTableExists(SQLiteDatabase database, String tableName) {
        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void insertRecord(String tableName, ContentValues record){
        getWritableDatabase().insert(tableName, null, record);
        close();
    }

    public List getRecords(String tableName, Class aClass){
        MicroOrm microOrm = new MicroOrm();
        List results = new ArrayList<>();

        Cursor cursor = getReadableDatabase().query(tableName, null, null, null, null, null, null);
        results = microOrm.listFromCursor(cursor, aClass);
        cursor.close();
        close();
        return results;
    }

    public void deleteRecord(String tableName, String column, String value){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(tableName, column+"=?", new String[]{value});
    }

    public void deleteAllRecord(String tableName){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(tableName, null, null);
    }

    public void selectRecord(String tableName, Object o, int id){
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            database.delete(tableName, "id=?", new String[]{Integer.toString(id)});
            database.insert(tableName, null, objectToContentValues(o));
            database.setTransactionSuccessful();
        }
        catch(Exception e) {
            Log.i("DbManager.java",e.getMessage());
        }
        finally {
            database.endTransaction();
        }
        close();
        VaccineService.setVaccines(DbManager.getInstance(null).getRecords(VACCINES, Vaccine.class));
    }

    public void updateRecords(String tableName, List list) {

        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {

            database.delete(tableName, null, null);

            for (Object o: list
                    ) {
                ContentValues values = objectToContentValues(o);
                //if (values != null){
                    database.insert(tableName, null, values);
                //}
            }

            database.setTransactionSuccessful();
        }
        catch(Exception e) {
            Log.i("DbManager.java",e.getMessage());
        }
        finally {
            database.endTransaction();
        }
        close();

    }

    public static ContentValues objectToContentValues(Object o) throws IllegalAccessException {

        ContentValues cv = new ContentValues();
        for (Field field : o.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getName().equals("serialVersionUID")){
                continue;
            }
            Object value = field.get(o);
            if (value instanceof Double || value instanceof Integer || value instanceof String || value instanceof Boolean
                    || value instanceof Long || value instanceof Float || value instanceof Short) {
                cv.put(field.getAnnotation(Column.class).value(), value.toString());
            } else if (value instanceof Date) {
                cv.put(field.getAnnotation(Column.class).value(), new SimpleDateFormat("dd/MM/yyyy").format((Date)value));
            }
        }
        return cv;
    }
/*
    public static ContentValues objectToContentValues(Object o, Field... ignoredFields) {
        try {
            ContentValues values = new ContentValues();
            List<Field> fieldsToIgnore = Arrays.asList(ignoredFields);

            for(Field field : o.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                if(fieldsToIgnore.contains(field))
                    continue;
                //if (field.getName().equals("serialVersionUID")){
                //    continue;
                //}

                Object value = field.get(o);
                if(value != null) {
                    //This part just makes sure the content values can handle the field
                    if(value instanceof Double || value instanceof Integer || value instanceof String || value instanceof Boolean
                            || value instanceof Long || value instanceof Float || value instanceof Short) {
                        values.put(field.getAnnotation(Column.class).value(), value.toString());
                    }
                    else if (value instanceof Date)
                        values.put(field.getAnnotation(Column.class).value(), new SimpleDateFormat("dd/MM/yyyy").format((Date)value));
                    else
                        throw new IllegalArgumentException("value could not be handled by field: " + value.toString());
                }
            }

            return values;
        } catch(Exception e) {
            throw new NullPointerException("content values failed to build");
        }
    }
    */
}
