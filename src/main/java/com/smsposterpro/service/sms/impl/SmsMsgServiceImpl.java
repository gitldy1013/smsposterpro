package com.smsposterpro.service.sms.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.smsposterpro.core.model.PageData;
import com.smsposterpro.core.model.PageWrap;
import com.smsposterpro.core.utils.ExampleBuilder;
import com.smsposterpro.dao.SmsMsgMapper;
import com.smsposterpro.dao.model.SmsMsg;
import com.smsposterpro.dao.model.SmsMsgExample;
import com.smsposterpro.service.sms.SmsMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * SMSService实现
 *
 * @author 136****3167
 * @date 2020/10/20 12:20
 */
@Service
public class SmsMsgServiceImpl implements SmsMsgService {

    @Autowired
    private SmsMsgMapper smsMsgMapper;

    @Override
    public Integer create(SmsMsg smsMsg) {
        smsMsgMapper.insertSelective(smsMsg);
        return smsMsg.getId();
    }

    @Override
    public void deleteById(Integer id) {
        smsMsgMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByIdInBatch(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    @Override
    public void updateById(SmsMsg smsMsg) {
        smsMsgMapper.updateByPrimaryKeySelective(smsMsg);
    }

    @Override
    public void updateByIdInBatch(List<SmsMsg> smsMsgs) {
        if (CollectionUtils.isEmpty(smsMsgs)) {
            return;
        }
        for (SmsMsg smsMsg : smsMsgs) {
            this.updateById(smsMsg);
        }
    }

    @Override
    public SmsMsg findById(Integer id) {
        return smsMsgMapper.selectByPrimaryKey(id);
    }

    @Override
    public SmsMsg findOne(SmsMsg smsMsg) {
        ExampleBuilder<SmsMsgExample, SmsMsgExample.Criteria> builder = ExampleBuilder.create(SmsMsgExample.class, SmsMsgExample.Criteria.class);
        List<SmsMsg> smsMsgs = smsMsgMapper.selectByExample(builder.buildExamplePack(smsMsg).getExample());
        if (smsMsgs.size() > 0) {
            return smsMsgs.get(0);
        }
        return null;
    }

    @Override
    public List<SmsMsg> findList(SmsMsg smsMsg) {
        ExampleBuilder<SmsMsgExample, SmsMsgExample.Criteria> builder = ExampleBuilder.create(SmsMsgExample.class, SmsMsgExample.Criteria.class);
        return smsMsgMapper.selectByExample(builder.buildExamplePack(smsMsg).getExample());
    }

    @Override
    public PageData<SmsMsg> findPage(PageWrap<SmsMsg> pageWrap) {
        PageHelper.startPage(pageWrap.getPage(), pageWrap.getCapacity());
        ExampleBuilder<SmsMsgExample, SmsMsgExample.Criteria> builder = ExampleBuilder.create(SmsMsgExample.class, SmsMsgExample.Criteria.class);
        ExampleBuilder.ExamplePack<SmsMsgExample, SmsMsgExample.Criteria> pack = builder.buildExamplePack(pageWrap.getModel());
        pack.getExample().setOrderByClause(pageWrap.getOrderByClause());
        return PageData.from(new PageInfo<>(smsMsgMapper.selectByExample(pack.getExample())));
    }

    @Override
    public long count(SmsMsg smsMsg) {
        ExampleBuilder<SmsMsgExample, SmsMsgExample.Criteria> builder = ExampleBuilder.create(SmsMsgExample.class, SmsMsgExample.Criteria.class);
        return smsMsgMapper.countByExample(builder.buildExamplePack(smsMsg).getExample());
    }
}
