package com.example.infomatrix.database;

import android.content.Context;
import android.support.annotation.Nullable;

import com.example.infomatrix.models.HistoryLog;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DBManager {

    private static DBManager instance;

    private Realm realm;
    private HistoryLog historyLog;

    private DBManager() {
        realm = Realm.getDefaultInstance();
    }

    public static DBManager getInstance() {
        if (instance == null) {
            return new DBManager();
        }
        return instance;
    }

    /**
     * Inserting data to the Realm database
     * @param uuid - auto generated uuid
     * @param code - user's code
     * @param action - action code
     * @param comment - any comments
     */
    public void insertHistoryLog(String uuid, String code, String action, String comment) {
        realm.beginTransaction();
        historyLog = realm.createObject(HistoryLog.class, uuid);
        historyLog.setCode(code);
        historyLog.setAction(action);
        historyLog.setComment(comment);
        realm.commitTransaction();
    }

    /**
     * Fetch all historyLog from the Realm database
     * @return all historyLog
     */
    public List<HistoryLog> getAllHistoryLogs() {
        RealmQuery<HistoryLog> query = realm.where(HistoryLog.class);
        return query.findAll();
    }

    /**
     * Delete all data from database, it should be called after successful uploading historyLog to the server
     */
    public void deleteHistoryLogsAfterUploadToServer() {
        final RealmResults<HistoryLog> result = realm.where(HistoryLog.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.deleteAllFromRealm();
            }
        });
    }

}
