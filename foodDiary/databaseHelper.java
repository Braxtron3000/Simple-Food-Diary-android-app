package com.simple.foodDiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class databaseHelper extends SQLiteOpenHelper {

    public static final String FAVORITES_TABLE = "FAVORITES_TABLE";
    public static final String COLUMN_FOODNAME = "FOODNAME";
    public static final String COLUMN_CALORIES = "CALORIES";
    public static final String COLUMN_FAT = "FAT";
    public static final String COLUMN_CARBS = "CARBS";
    public static final String COLUMN_PROTEIN = "PROTEIN";
    public static final String COLUMN_ID = "ID";



   /* public com.simple.crapapp1.databaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }*/

    public databaseHelper(@Nullable Context context) {
        super(context, "favorites.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = " CREATE TABLE IF NOT EXISTS " + FAVORITES_TABLE + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FOODNAME + " TEXT, " + COLUMN_CALORIES + " TEXT," + COLUMN_FAT + " TEXT, " + COLUMN_CARBS + " TEXT, " + COLUMN_PROTEIN + " TEXT) ";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public String addOne(FoodModel foodModel) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        SQLiteDatabase database2 = this.getReadableDatabase();
        String queryString = " SELECT * FROM " + FAVORITES_TABLE + " WHERE " + COLUMN_FOODNAME + " = " + "\'" + foodModel.getName() + "\'";
        Cursor cursor = database2.rawQuery(queryString, null);
        //}catch (Exception OperationCanceledException){

        Boolean isEqual=false;
        try {
            if (cursor.moveToFirst() && foodModel.getName().equals(cursor.getString(1)))isEqual=true;
        }catch (CursorIndexOutOfBoundsException e){
            isEqual=false;
        }


        if (cursor.moveToFirst()) {
            //if it's greater than zero it's there

            cursor.close();
            return foodModel.getName()+" is already a favorite!";

        } else if (!cursor.moveToFirst()){
            //if it's less than zero it's not there

            contentValues.put(COLUMN_FOODNAME, foodModel.getName());
            contentValues.put(COLUMN_CALORIES, foodModel.getCalories());
            contentValues.put(COLUMN_FAT, foodModel.getFat());
            contentValues.put(COLUMN_CARBS, foodModel.getCarbs());
            contentValues.put(COLUMN_PROTEIN, foodModel.getProtein());

            long insert = database.insert(FAVORITES_TABLE, null, contentValues);
            database.close();

            return foodModel.getName()+" saved to favorites";
        }
        return "there was an issue saving to favorites";}


    public List<String> getAllFood() {
        List<FoodModel> returnList = new ArrayList<>();
        List<String> stringList = new ArrayList<>();


        String queryString = "SELECT * FROM " + FAVORITES_TABLE;

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {

            do {
                int foodID = cursor.getInt(0);
                String foodName = cursor.getString(1);
                int calories = cursor.getInt(2);
                int fat = cursor.getInt(3);
                int carbs = cursor.getInt(4);
                int protein = cursor.getInt(5);

                FoodModel newFood = new FoodModel(foodID, foodName, calories, fat, carbs, protein);
                returnList.add(newFood);
                stringList.add(newFood.getName());

            } while (cursor.moveToNext());

        } else {
        }

        cursor.close();
        database.close();

        return stringList;
    }

    public List<String> getFoodBySearch(String getThisFood) {
        List<FoodModel> returnList = new ArrayList<>();
        List<String> stringList = new ArrayList<>();
        String queryString = " SELECT * FROM " + FAVORITES_TABLE + " WHERE " + COLUMN_FOODNAME + " LIKE " + "\'%" + getThisFood + "%\'";

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(queryString, null);


        if (cursor.moveToFirst()) {

            do {
                int foodID = cursor.getInt(0);
                String foodName = cursor.getString(1);
                int calories = cursor.getInt(2);
                int fat = cursor.getInt(3);
                int carbs = cursor.getInt(4);
                int protein = cursor.getInt(5);

                FoodModel newFood = new FoodModel(foodID, foodName, calories, fat, carbs, protein);
                returnList.add(newFood);
                stringList.add(newFood.getName());

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return stringList;
    }

    public List<FoodModel> getFoodModel(String getThisFood) {
        List<FoodModel> returnList = new ArrayList<>();
        String queryString = " SELECT * FROM " + FAVORITES_TABLE + " WHERE " + COLUMN_FOODNAME + " LIKE " + "\'%" + getThisFood + "%\'";

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(queryString, null);


        if (cursor.moveToFirst()) {

            do {
                int foodID = cursor.getInt(0);
                String foodName = cursor.getString(1);
                int calories = cursor.getInt(2);
                int fat = cursor.getInt(3);
                int carbs = cursor.getInt(4);
                int protein = cursor.getInt(5);

                FoodModel newFood = new FoodModel(foodID, foodName, calories, fat, carbs, protein);
                returnList.add(newFood);

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return returnList;
    }





    public boolean deleteOne(String foodposition) {
        SQLiteDatabase database = getWritableDatabase();

        String queryString = " DELETE FROM " + FAVORITES_TABLE + " WHERE " + COLUMN_FOODNAME + " = " + "\'" + foodposition + "\'";

        Cursor cursor = database.rawQuery(queryString, null);

        /*if(cursor.moveToFirst())return true;
        else return false;*/
        //cursor.close();
        //database.close();
        return cursor.moveToFirst();

    }

}
