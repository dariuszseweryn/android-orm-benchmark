package com.littleinc.orm_benchmark.realm;

import android.content.Context;
import android.util.Log;
import com.littleinc.orm_benchmark.BenchmarkExecutable;
import com.littleinc.orm_benchmark.util.Util;
import io.realm.Realm;

import java.sql.SQLException;

import static com.littleinc.orm_benchmark.util.Util.getRandomString;

public enum RealmExecutor implements BenchmarkExecutable {

    INSTANCE;
    private Context context;

    private Realm getRealm() {
        return Realm.getInstance(context, false);
    }

    @Override
    public int getProfilerId() {
        return 4;
    }

    @Override
    public String getOrmName() {
        return "Realm";
    }

    @Override
    public void init(Context context, boolean useInMemoryDb) {
        this.context = context;
    }

    @Override
    public long createDbStructure() throws SQLException {
        return 0; // there is no need
    }

    @Override
    public long writeWholeData() throws SQLException {
        TempUser users[] = new TempUser[NUM_USER_INSERTS];
        for (int i = 0; i < users.length; i++) {
            TempUser newUser = new TempUser(getRandomString(10), getRandomString(10), i);
            users[i] = newUser;
        }

        TempMessage messages[] = new TempMessage[NUM_MESSAGE_INSERTS];
        for (int i = 0; i < messages.length; i++) {
            TempMessage newMessage = new TempMessage();
            newMessage.setCommandId(i);
            newMessage.setSortedBy(System.nanoTime());
            newMessage.setContent(Util.getRandomString(100));
            newMessage.setClientId(System.currentTimeMillis());
            newMessage
                    .setSenderId(Math.round(Math.random() * NUM_USER_INSERTS));
            newMessage
                    .setChannelId(Math.round(Math.random() * NUM_USER_INSERTS));
            newMessage.setCreatedAt((int) (System.currentTimeMillis() / 1000L));

            messages[i] = newMessage;
        }

        long start = System.nanoTime();
        Realm realm = getRealm();
        realm.beginTransaction();

        try {
            for (TempUser user : users) {
                User newUser = realm.createObject(User.class);
                user.setUser(newUser);
            }
            Log.d(RealmExecutor.class.getSimpleName(), "Done, wrote "
                    + NUM_USER_INSERTS + " users");

            for (TempMessage message : messages) {
                Message newMessage = realm.createObject(Message.class);
                message.setMessage(newMessage);
            }

            Log.d(RealmExecutor.class.getSimpleName(), "Done, wrote "
                    + NUM_MESSAGE_INSERTS + " messages");
        } finally {
            realm.commitTransaction();
        }
        return System.nanoTime() - start;
    }

    @Override
    public long readWholeData() throws SQLException {
        long start = System.nanoTime();
        Realm realm = getRealm();
        Log.d(RealmExecutor.class.getSimpleName(),
                "Read, " + realm.allObjects(Message.class).size()
                        + " rows");
        return System.nanoTime() - start;
    }

    @Override
    public long readIndexedField() throws SQLException {
        long start = System.nanoTime();
        Realm realm = getRealm();
        Log.d(RealmExecutor.class.getSimpleName(),
                "Read (not indexed!), "
                        + realm.where(Message.class)
                        .equalTo("commandId", LOOK_BY_INDEXED_FIELD)
                        .findAll().size() + " rows");
        return System.nanoTime() - start;
    }

    @Override
    public long readSearch() throws SQLException {
        long start = System.nanoTime();
        Realm realm = getRealm();
        Log.d(RealmExecutor.class.getSimpleName(),
                "Read, "
                        + realm.where(Message.class)
                        .contains("content", SEARCH_TERM)
                        .findAll().size()
                        + " rows (all!)");
        return System.nanoTime() - start;
    }

    @Override
    public long dropDb() throws SQLException {
        long start = System.nanoTime();
        Realm realm = getRealm();
        realm.beginTransaction();
        realm.clear(User.class);
        realm.clear(Message.class);
        realm.commitTransaction();
        return System.nanoTime() - start;
    }

    private class TempUser {
        String firstName;
        String lastName;
        int id;

        public TempUser(String firstName, String lastName, int id) {

            this.firstName = firstName;
            this.lastName = lastName;
            this.id = id;
        }

        public void setUser(User user) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setId(id);
        }
    }

    private class TempMessage {
        int commandId;
        long sortedBy;
        String content;
        long clientId;
        long senderId;
        long channelId;
        int createdAt;


        public void setCommandId(int commandId) {
            this.commandId = commandId;
        }

        public void setSortedBy(long sortedBy) {
            this.sortedBy = sortedBy;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setClientId(long clientId) {
            this.clientId = clientId;
        }

        public void setSenderId(long senderId) {
            this.senderId = senderId;
        }

        public void setChannelId(long channelId) {
            this.channelId = channelId;
        }

        public void setCreatedAt(int createdAt) {
            this.createdAt = createdAt;
        }

        public void setMessage(Message newMessage) {
            newMessage.setCommandId(commandId);
            newMessage.setSortedBy(sortedBy);
            newMessage.setContent(content);
            newMessage.setClientId(clientId);
            newMessage.setSenderId(senderId);
            newMessage.setChannelId(channelId);
            newMessage.setCreatedAt(createdAt);
        }
    }
}
