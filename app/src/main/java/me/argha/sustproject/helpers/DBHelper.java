package me.argha.sustproject.helpers;

/**
 * Created by ARGHA K ROY on 5/24/2015.
 */
/*

public class DBHelper extends SQLiteOpenHelper {

    */
/* For downloaded forms*//*

    public static final String COL_ID = "id";
    public static final String COL_FORM_ID = "form_id";
    public static final String COL_FORM_DATA = "form_data";
    public static final String COL_FORM_NAME = "form_name";

    */
/*For saved forms*//*

    public static final String COL_SAVED_FORM_DATA = "form_saved_data";
    public static final String COL_SAVE_TIME = "form_save_time";

    public DBHelper(Context context) {
        super(context, AppConst.DB_NAME, null, AppConst.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + AppConst.DB_TABLE_DOWNLOADED_FORMS + "("
                + COL_ID + " INTEGER PRIMARY KEY,"
                + COL_FORM_ID + " VARCHAR(10),"
                + COL_FORM_NAME + " VARCHAR(10),"
                + COL_FORM_DATA + " TEXT )";
        db.execSQL(query);

        query = "CREATE TABLE " + AppConst.DB_TABLE_SAVED_FORMS + "("
                + COL_ID + " INTEGER PRIMARY KEY,"
                + COL_FORM_ID + " VARCHAR(10),"
                + COL_SAVE_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + COL_SAVED_FORM_DATA + " TEXT )";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AppConst.DB_TABLE_SAVED_FORMS);
        db.execSQL("DROP TABLE IF EXISTS " + AppConst.DB_TABLE_DOWNLOADED_FORMS);
        // Create tables again
        onCreate(db);
    }

    public void saveDownloadedForm(String formId,String formName,String formData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FORM_ID, formId);
        values.put(COL_FORM_NAME, formName);
        values.put(COL_FORM_DATA, formData);

        db.insert(AppConst.DB_TABLE_DOWNLOADED_FORMS, null, values);
        db.close();
    }

    public ArrayList<Form> getAllForms() {
        ArrayList<Form> downLoadedForms = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + AppConst.DB_TABLE_DOWNLOADED_FORMS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Form form=new Form(cursor.getString(0),cursor.getString(1),cursor.getString(2));
                downLoadedForms.add(form);
            } while (cursor.moveToNext());
        }
        db.close();
        return downLoadedForms;
    }

    public String getFormFields(String formId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(AppConst.DB_TABLE_DOWNLOADED_FORMS, new String[]{COL_FORM_DATA}, COL_FORM_ID + "=?",
                new String[]{formId}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        String formData=cursor.getString(0);
        db.close();
        return formData;
    }

    public void deleteAllDownloadedForms() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AppConst.DB_TABLE_DOWNLOADED_FORMS, null,null);
        db.close();
    }

    public void saveUserForm(String formId,String formData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FORM_ID, formId);
        values.put(COL_SAVED_FORM_DATA, formData);

        db.insert(AppConst.DB_TABLE_SAVED_FORMS, null, values);
        db.close();
    }

    public void updateUserForm(String dataId,String formData){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_SAVED_FORM_DATA, formData);
        // updating row
        db.update(AppConst.DB_TABLE_SAVED_FORMS, values, COL_ID + " = ?",
                new String[]{dataId});
    }

    public ArrayList<HashMap<String,String>> getAllSavedForms(){
        ArrayList<HashMap<String,String>> savedForms = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + AppConst.DB_TABLE_SAVED_FORMS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HashMap<String,String> map=new HashMap<>();
                map.put("form_id",cursor.getString(1));
                map.put("form_data",cursor.getString(3));
                savedForms.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        return savedForms;
    }
    public ArrayList<HashMap<String,String>> getAllSavedForms(String formId){
        ArrayList<HashMap<String,String>> savedForms = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + AppConst.DB_TABLE_SAVED_FORMS+" WHERE "+COL_FORM_ID+" = "+formId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HashMap<String,String> map=new HashMap<>();
                map.put("data_id",cursor.getString(0));
                map.put("data",cursor.getString(3));
                savedForms.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        return savedForms;
    }

    public void deleteSavedFormDataById(String dataId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AppConst.DB_TABLE_SAVED_FORMS, COL_ID + " = ?",
                new String[] { String.valueOf(dataId) });
        db.close();
    }
    public void deleteSavedFormDataByFormId(String formId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AppConst.DB_TABLE_SAVED_FORMS, COL_FORM_ID + " = ?",
                new String[] { String.valueOf(formId) });
        db.close();
    }

    public void deleteAllSavedFormData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AppConst.DB_TABLE_SAVED_FORMS, null,null);
        db.close();
    }
}
*/
