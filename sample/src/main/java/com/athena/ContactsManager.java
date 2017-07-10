package com.athena;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsManager {

    private static ContactsManager instance = new ContactsManager();

    private ContactsManager() {

    }

    public static ContactsManager getInstance() {
        return instance;
    }

    private List<ContactsEntity> friends = new ArrayList<>();

    private Map<Object, ContactsEntity> friendMap = new HashMap<>();
    /**
     * 读取手机通讯录db至本地通讯录缓存
     */
    private static final String[] PHONES_PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};

    private static final int PHONES_DISPLAY_NAME_INDEX = 1;
    private static final int PHONES_NUMBER_INDEX = 0;
    private static final int PHONES_CONTACT_ID_INDEX = 2;

    public void initData(ContentResolver resolver) {
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        friends.clear();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String phone = cursor.getString(PHONES_NUMBER_INDEX);
                if (TextUtils.isEmpty(phone)) continue;
                String name = cursor.getString(PHONES_DISPLAY_NAME_INDEX);
                long id = cursor.getLong(PHONES_CONTACT_ID_INDEX);
                ContactsEntity friend = new ContactsEntity(id, name, phone);
                friends.add(friend);
                friendMap.put(id, friend);
            }
            cursor.close();
        }
    }

    public List<ContactsEntity> getContactsList() {
        List<ContactsEntity> list = new ArrayList<>();
        list.addAll(friends);
        return list;
    }

    public ContactsEntity getContacts(Object id) {
        return friendMap.get(id);
    }
}
