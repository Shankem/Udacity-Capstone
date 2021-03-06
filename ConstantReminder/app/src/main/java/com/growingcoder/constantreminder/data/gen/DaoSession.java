package com.growingcoder.constantreminder.data.gen;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.growingcoder.constantreminder.data.gen.Reminder;

import com.growingcoder.constantreminder.data.gen.ReminderDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig reminderDaoConfig;

    private final ReminderDao reminderDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        reminderDaoConfig = daoConfigMap.get(ReminderDao.class).clone();
        reminderDaoConfig.initIdentityScope(type);

        reminderDao = new ReminderDao(reminderDaoConfig, this);

        registerDao(Reminder.class, reminderDao);
    }
    
    public void clear() {
        reminderDaoConfig.getIdentityScope().clear();
    }

    public ReminderDao getReminderDao() {
        return reminderDao;
    }

}
