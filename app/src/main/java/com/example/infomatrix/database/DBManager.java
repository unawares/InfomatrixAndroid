package com.example.infomatrix.database;

import android.content.Context;
import android.support.annotation.Nullable;

import com.example.infomatrix.models.Logs;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DBManager {

    Context context;
    Realm realm = null;
    Logs logs = null;

    public DBManager(Context context) {
        this.context = context;
    }

    /**
     * Create an instance of Realm database
     */
    public void createDBInstance() {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    /**
     * Inserting data to the Realm database
     * @param id - auto generated id
     * @param username - user's fullname
     * @param action - action code
     * @param comment - any comments
     */
    public void insertLogsToDB(Integer id, String username, String action, String comment) {
        realm.beginTransaction();
        logs = realm.createObject(Logs.class);
        logs.setId(id);
        logs.setUsername(username);
        logs.setAction(action);
        logs.setComment(comment);
        realm.commitTransaction();
    }

    /**
     * Fetch all logs from the Realm database
     * @return all logs
     */
    public Logs[] getAllLogsFromDB(@Nullable String username) {
        RealmQuery<Logs> query = realm.where(Logs.class);

        // Add query conditions, if you need
        query.equalTo("name", "John");
        query.or().equalTo("name", "Peter");

        // Execute the query
        RealmResults<Logs> result1 = query.findAll();

        // Or alternatively do the same all at once (the "Fluent interface")
        result1 = realm.where(Logs.class)
                .equalTo("name", "John")
                .or()
                .equalTo("name", "Peter")
                .findAll();

        return (Logs[]) result1.toArray();
    }

    /**
     * Delete all data from database, it should be called after successful uploading logs to the server
     */
    public void deleteLogsAfterUploadToServer() {
        final RealmResults<Logs> result = realm.where(Logs.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.deleteAllFromRealm();
            }
        });
    }

}
