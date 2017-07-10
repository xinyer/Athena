package com.athena.library.data;

import android.text.TextUtils;

import com.athena.library.utils.ChnToSpell;
import com.athena.library.utils.SortManager;
import com.athena.library.utils.T9Utils;


/**
 * 可搜索的字段
 */
public class SearchableField {

    /**
     * 字段名字，原实体类的变量名字
     */
    private String fieldName;

    /**
     * 字段值
     */
    private String fieldValue;

    /**
     * 汉字转拼音类型
     *
     * @see PinyinType
     */
    private PinyinType pinyinType;

    /**
     * 字段值不拼，全拼，首字母拼
     */
    private String valueNoPinyin, valueCompletePinyin, valueInitialPinyin;

    /**
     * 匹配的拼音类型
     */
    private PinyinType matchingPinyinType = PinyinType.NO;

    /**
     * 匹配的位置
     */
    private int index = -1;

    /**
     * 匹配的长度
     */
    private int len;

    /**
     * 排序条件中“匹配字段”的权重
     * 比如 remark=3,name=2, phone=1
     */
    int MatchFieldSortWeight = 1;

    private long sortWeight;

    public SearchableField(String fieldName, String fieldValue, PinyinType pinyinType) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.pinyinType = pinyinType;
        chnToSpell();
    }

    private void chnToSpell() {
        if (pinyinType == PinyinType.INITIAL) {
            String pinyin = ChnToSpell.MakeSpellCode(fieldValue, ChnToSpell.TRANS_MODE_PINYIN_INITIAL);
            valueInitialPinyin = T9Utils.stringToNumber(pinyin);
        } else if (pinyinType == PinyinType.COMPLETE) {
            String pinyin = ChnToSpell.MakeSpellCode(fieldValue, ChnToSpell.TRANS_MODE_QUAN_PIN);
            valueCompletePinyin = T9Utils.stringToNumber(pinyin);
        } else if (pinyinType == PinyinType.ALL) {
            String pinyin1 = ChnToSpell.MakeSpellCode(fieldValue, ChnToSpell.TRANS_MODE_PINYIN_INITIAL);
            valueInitialPinyin = T9Utils.stringToNumber(pinyin1);
            String pinyin2 = ChnToSpell.MakeSpellCode(fieldValue, ChnToSpell.TRANS_MODE_QUAN_PIN);
            valueCompletePinyin = T9Utils.stringToNumber(pinyin2);
        } else {
            valueNoPinyin = fieldValue;
        }
    }

    MatchDegree compare(String keyword, int dataSrc) {
        return compare(keyword, pinyinType, dataSrc);
    }

    private MatchDegree compare(String keyword, PinyinType pinyinType, int dataSrc) {
        if (TextUtils.isEmpty(keyword)) {
            return MatchDegree.MATCH_NO;
        }
        switch (pinyinType) {
            case COMPLETE:
                return  compareAllPin(keyword, dataSrc);
            case INITIAL:
                return compareHeadPin(keyword, dataSrc);
            case ALL:
                /*先比较首拼，再比较全拼*/
                MatchDegree matchDegree = compareHeadPin(keyword, dataSrc);
                /*首拼匹配返回，否则匹配全拼*/
                if (matchDegree!=MatchDegree.MATCH_NO) return matchDegree;
                return compareAllPin(keyword, dataSrc);
            case NO:
                return compareNoPin(keyword, dataSrc);
            default:
        }
        return MatchDegree.MATCH_NO;
    }

    /**
     * 比较全拼字段值
     * @param keyword
     * @param dataSrc
     * @return
     */
    private MatchDegree compareAllPin(String keyword, int dataSrc) {
        MatchDegree matchDegree = MatchDegree.MATCH_NO;
        len = keyword.length();
        index = -1;
        sortWeight=0;
        char firstChar = getFieldValue().charAt(0);
        matchingPinyinType = PinyinType.COMPLETE;

        if (valueCompletePinyin.equals(keyword)) {
            index=0;
            matchDegree = MatchDegree.MATCH_COMPLETE;
            sortWeight = SortManager.calculateSortWeight(dataSrc,
                    SortConstants.MATCH_DEGREE.equals,
                    MatchFieldSortWeight,
                    firstChar, 0);
        } else if (valueCompletePinyin.startsWith(keyword)) {
            index=0;
            matchDegree = MatchDegree.MATCH_START;
            sortWeight = SortManager.calculateSortWeight(dataSrc,
                    SortConstants.MATCH_DEGREE.starts,
                    MatchFieldSortWeight,
                    firstChar, 0);
        } else if (valueCompletePinyin.contains(keyword)) {
            index = valueCompletePinyin.indexOf(keyword);
            if (T9Utils.isMatchAllPin(index, fieldValue)) {
                matchDegree = MatchDegree.MATCH_PARTIAL;
                sortWeight = SortManager.calculateSortWeight(dataSrc,
                        SortConstants.MATCH_DEGREE.contains,
                        MatchFieldSortWeight,
                        firstChar, index);
            } else {
                matchDegree = MatchDegree.MATCH_NO;
                sortWeight=0;
            }
        } else {
            matchingPinyinType = PinyinType.NO;

        }
        return matchDegree;
    }

    /**
     * 比较首字母拼字段值
     * @param keyword
     * @param dataSrc
     * @return
     */
    private MatchDegree compareHeadPin(String keyword, int dataSrc) {
        MatchDegree matchDegree = MatchDegree.MATCH_NO;
        len = keyword.length();
        index = -1;
        sortWeight = 0;
        char firstChar = getFieldValue().charAt(0);
        matchingPinyinType = PinyinType.INITIAL;

        if (valueInitialPinyin.equals(keyword)) {
            index=0;
            matchDegree = MatchDegree.MATCH_COMPLETE;
            sortWeight = SortManager.calculateSortWeight(dataSrc,
                    SortConstants.MATCH_DEGREE.equals,
                    MatchFieldSortWeight,
                    firstChar, 0);
        } else if (valueInitialPinyin.startsWith(keyword)) {
            index=0;
            matchDegree = MatchDegree.MATCH_START;
            sortWeight = SortManager.calculateSortWeight(dataSrc,
                    SortConstants.MATCH_DEGREE.starts,
                    MatchFieldSortWeight,
                    firstChar, 0);
        } else if (valueInitialPinyin.contains(keyword)) {
            index = valueInitialPinyin.indexOf(keyword);
            matchDegree = MatchDegree.MATCH_PARTIAL;
            sortWeight = SortManager.calculateSortWeight(dataSrc,
                    SortConstants.MATCH_DEGREE.contains,
                    MatchFieldSortWeight,
                    firstChar, index);
        } else {
            matchingPinyinType = PinyinType.NO;
            sortWeight=0;
        }

        return matchDegree;
    }

    /**
     * 比较原值字段值
     * @param keyword
     * @param dataSrc
     * @return
     */
    private MatchDegree compareNoPin(String keyword, int dataSrc) {
        MatchDegree matchDegree = MatchDegree.MATCH_NO;
        len = keyword.length();
        index = -1;
        sortWeight = 0;
        char firstChar = getFieldValue().charAt(0);
        matchingPinyinType = PinyinType.NO;

        if (valueNoPinyin.equals(keyword)) {
            index=0;
            matchDegree = MatchDegree.MATCH_COMPLETE;
            sortWeight = SortManager.calculateSortWeight(dataSrc,
                    SortConstants.MATCH_DEGREE.equals,
                    MatchFieldSortWeight,
                    firstChar, 0);
        } else if (valueNoPinyin.startsWith(keyword)) {
            index=0;
            matchDegree = MatchDegree.MATCH_START;
            sortWeight = SortManager.calculateSortWeight(dataSrc,
                    SortConstants.MATCH_DEGREE.starts,
                    MatchFieldSortWeight,
                    firstChar, 0);
        } else if (valueNoPinyin.contains(keyword)) {
            index = valueNoPinyin.indexOf(keyword);
            matchDegree = MatchDegree.MATCH_PARTIAL;
            sortWeight = SortManager.calculateSortWeight(dataSrc,
                    SortConstants.MATCH_DEGREE.contains,
                    MatchFieldSortWeight,
                    firstChar, index);
        } else {

            matchingPinyinType = PinyinType.NO;
            sortWeight=0;
        }
        return matchDegree;
    }

    /**
     * 获取字段名字
     *
     * @return
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * 获取字段值
     *
     * @return
     */
    public String getFieldValue() {
        return fieldValue;
    }

    /**
     * 获取匹配字段的位置
     *
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * 获取匹配的长度
     *
     */
    public int getLen() {
        return len;
    }

    public PinyinType getMatchingPinyinType() {
        return matchingPinyinType;
    }

    public void setMatchFieldSortWeight(int weight) {
        MatchFieldSortWeight = weight;
    }

    public long getSortWeight() {
        return sortWeight;
    }
    @Override
    public String toString() {
        return "FieldName:" + fieldName + "\t FieldValue:" + fieldValue
                + "\t PinyinType:" + pinyinType + "\tMatchFieldSortWeight:" + MatchFieldSortWeight;
    }

}
