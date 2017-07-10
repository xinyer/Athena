package com.athena;


import com.athena.library.annotation.T9SearchKey;
import com.athena.library.annotation.T9Searchable;
import com.athena.library.annotation.T9SortableEntity;
import com.athena.library.data.PinyinType;

@T9SortableEntity(DataSrcWeight = 1)
public class ContactsEntity {

    @T9SearchKey
    private String key;

    @T9Searchable(PinyinType = PinyinType.ALL_PIN_AND_HEAD_PIN, MatchFieldSortWeight = 2)
    private String name;

    @T9Searchable(MatchFieldSortWeight = 1)
    private String phone;

    public ContactsEntity(String key, String name, String phone) {
        this.key = key;
        this.name = name;
        this.phone = phone;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}

