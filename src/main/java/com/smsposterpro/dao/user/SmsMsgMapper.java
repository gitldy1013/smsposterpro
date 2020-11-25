package com.smsposterpro.dao.user;

import com.smsposterpro.dao.user.model.SmsMsg;
import com.smsposterpro.dao.user.model.SmsMsgExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsMsgMapper {
    int countByExample(SmsMsgExample example);

    int deleteByExample(SmsMsgExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SmsMsg record);

    int insertSelective(SmsMsg record);

    List<SmsMsg> selectByExample(SmsMsgExample example);

    SmsMsg selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SmsMsg record, @Param("example") SmsMsgExample example);

    int updateByExample(@Param("record") SmsMsg record, @Param("example") SmsMsgExample example);

    int updateByPrimaryKeySelective(SmsMsg record);

    int updateByPrimaryKey(SmsMsg record);
}
