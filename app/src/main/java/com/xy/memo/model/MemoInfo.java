package com.xy.memo.model;

import java.io.Serializable;

/**
 * 备忘录实体类
 *
 * @author xy 2017/11/29.
 */

public class MemoInfo implements Serializable{
    public Integer id;
    public String memoTitle; // 备忘录标题
    public Integer memoType; // 模板类型
    public String memoContent; // 备忘录内容
    public Long insertTime; // 创建时间
    public Boolean isChecked = false; // 是否选中
    public Boolean isMultiMode = false; // 是否处于多选状态
    public String tag; // 标签
}
