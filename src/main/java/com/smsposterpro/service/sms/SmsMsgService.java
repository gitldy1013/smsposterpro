package com.smsposterpro.service.sms;

import com.smsposterpro.core.model.PageData;
import com.smsposterpro.core.model.PageWrap;
import com.smsposterpro.dao.model.SmsMsg;

import java.util.List;

/**
 * SMSService定义
 *
 * @author 136****3167
 * @date 2020/10/20 12:20
 */
public interface SmsMsgService {

    /**
     * 创建
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    Integer create(SmsMsg smsMsg);

    /**
     * 主键删除
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    void deleteById(Integer id);

    /**
     * 批量主键删除
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    void deleteByIdInBatch(List<Integer> ids);

    /**
     * 主键更新
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    void updateById(SmsMsg SmsMsg);

    /**
     * 批量主键更新
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    void updateByIdInBatch(List<SmsMsg> SmsMsgs);

    /**
     * 主键查询
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    SmsMsg findById(Integer id);

    /**
     * 条件查询单条记录
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    SmsMsg findOne(SmsMsg smsMsgmsMsg);

    /**
     * 条件查询
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    List<SmsMsg> findList(SmsMsg smsMsg);

    /**
     * 分页查询
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    PageData<SmsMsg> findPage(PageWrap<SmsMsg> pageWrap);

    /**
     * 条件统计
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    long count(SmsMsg smsMsg);
}
