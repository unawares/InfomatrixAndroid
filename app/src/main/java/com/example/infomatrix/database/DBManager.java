package com.example.infomatrix.database;

import com.example.infomatrix.models.HistoryLogRealmObject;
import com.example.infomatrix.models.UserRealmObject;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class DBManager {

    private static DBManager instance;

    private Realm realm;

    private DBManager() {
        realm = Realm.getDefaultInstance();
    }

    public static DBManager getInstance() {
        if (instance == null) {
            return new DBManager();
        }
        return instance;
    }

    public void insertHistoryLog(String uuid, String fullName, String action, Date date) {
        realm.beginTransaction();
        HistoryLogRealmObject historyLogRealmObject = realm.createObject(HistoryLogRealmObject.class, uuid);
        historyLogRealmObject.setFullName(fullName);
        historyLogRealmObject.setAction(action);
        historyLogRealmObject.setDate(date);
        realm.commitTransaction();
    }

    public void insertUser(String fullName, String code, int role, boolean isFood, boolean isTransport) {
        realm.commitTransaction();
        UserRealmObject userRealmObject = realm.createObject(UserRealmObject.class, code);
        userRealmObject.setFullName(fullName);
        userRealmObject.setRole(role);
        userRealmObject.setTransport(isTransport);
        userRealmObject.setFood(isFood);
        realm.commitTransaction();
    }

    public List<HistoryLogRealmObject> getAllHistoryLogs() {
        RealmQuery<HistoryLogRealmObject> query = realm.where(HistoryLogRealmObject.class);
        return query.sort("date", Sort.DESCENDING).findAll();
    }

    public List<UserRealmObject> getAllUsers() {
        RealmQuery<UserRealmObject> query = realm.where(UserRealmObject.class);
        return query.sort(new String[]{"role", "fullName"}, new Sort[]{Sort.ASCENDING, Sort.ASCENDING}).findAll();
    }

    public void updateHistoryLogs(List<HistoryLogRealmObject> historyLogRealmObjects) {
        realm.beginTransaction();
        realm.insertOrUpdate(historyLogRealmObjects);
        realm.commitTransaction();
    }

    public void updateUsers(List<UserRealmObject> userRealmObjects) {
        realm.beginTransaction();
        realm.insertOrUpdate(userRealmObjects);
        realm.commitTransaction();
    }

    public UserRealmObject getUser(String code) {
        return realm.where(UserRealmObject.class).equalTo("code", code).findFirst();
    }

    public void deleteHistoryLogsAfterUploadToServer() {
        final RealmResults<HistoryLogRealmObject> result = realm.where(HistoryLogRealmObject.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.deleteAllFromRealm();
            }
        });
    }

    public void deleteUsers() {
        final RealmResults<UserRealmObject> result = realm.where(UserRealmObject.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.deleteAllFromRealm();
            }
        });
    }

}
