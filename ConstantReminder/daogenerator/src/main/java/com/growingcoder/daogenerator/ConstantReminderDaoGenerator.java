package com.growingcoder.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class ConstantReminderDaoGenerator {

    private static final String REMINDER = "Reminder";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.growingcoder.constantreminder.data.gen");
        schema.enableKeepSectionsByDefault();

        setupReminder(schema);


        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }

    private static void setupReminder(Schema schema) {
        Entity entity = schema.addEntity(REMINDER);
        entity.setTableName(REMINDER.toUpperCase());

        // id will also be used to send notification and alarm
        entity.addIdProperty().autoincrement();
        entity.addStringProperty("name");
        entity.addLongProperty("dateTime");
    }
}
