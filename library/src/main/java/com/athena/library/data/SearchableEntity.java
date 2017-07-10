package com.athena.library.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 从普通实体抽象出来的可以搜索的实体，只包括可以搜索的字段
 */
public class SearchableEntity {

    private MatchDegree matchDegree;

    /**
     * 键名字,标记Entity的唯一性
     */
    private String keyName;

    /**
     * 键值,标记Entity的唯一性
     */
    private Object keyValue;

    /**
     * 所有可以搜索的字段
     */
    private List<SearchableField> fields = new ArrayList<>();

    /**
     * 匹配的字段
     */
    private SearchableField matchedField;

    /**
     * 排序条件中“数据来源”的权重
     * 比如 好友=2,陌生人=1
     */
    private int DataSrcSortWeight = 1;

    public void setKey(String keyName, Object keyValue) {
        this.keyName = keyName;
        this.keyValue = keyValue;
    }

    public Object getKeyValue() {
        return keyValue;
    }

    public String getKeyName() {
        return keyName;
    }

    public SearchableField getMatchField() {
        return matchedField;
    }

    /**
     * 添加SearchableField，按照MatchFieldSortWeight由大到小的顺序排列
     *
     * @param field
     */
    public void addSearchableField(SearchableField field) {
        if (fields.isEmpty()) fields.add(field);
        else {
            boolean isAdded = false;
            for (int i = 0; i < fields.size(); i++) {
                SearchableField tmpField = fields.get(i);
                if (field.MatchFieldSortWeight >= tmpField.MatchFieldSortWeight) {
                    fields.add(i, field);
                    isAdded = true;
                    break;
                }
            }
            if (!isAdded) fields.add(fields.size(), field);
        }
    }

    /**
     * 逐个字段和关键词比较
     * 此处应该有个顺序
     *
     * @param keyword
     * @return
     */
    public boolean compare(String keyword) {
        matchDegree = MatchDegree.MATCH_NO;
        for (SearchableField field : fields) {
            matchDegree = field.compare(keyword, DataSrcSortWeight);
            if (matchDegree != MatchDegree.MATCH_NO) {
                matchedField = field;
                return true;
            }
        }
        return false;
    }

    public void setDataSrcSortWeight(int weight) {
        DataSrcSortWeight = weight;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------------------\n");
        sb.append("KeyName:" + keyName + "\tKeyValue:" + keyValue
                + "\tDataSrcWeight:" + DataSrcSortWeight + "\n");
        for (SearchableField searchableField : fields) {
            sb.append(searchableField.toString());
            sb.append("\n");
        }
        sb.append("\n----------------------------------------");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        return  (o instanceof SearchableEntity) && this.keyValue.equals(((SearchableEntity) o).keyValue);
    }
}
