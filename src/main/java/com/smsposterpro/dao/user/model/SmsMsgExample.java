package com.smsposterpro.dao.user.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmsMsgExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SmsMsgExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andSendPhoneNumIsNull() {
            addCriterion("send_phone_num is null");
            return (Criteria) this;
        }

        public Criteria andSendPhoneNumIsNotNull() {
            addCriterion("send_phone_num is not null");
            return (Criteria) this;
        }

        public Criteria andSendPhoneNumEqualTo(String value) {
            addCriterion("send_phone_num =", value, "sendPhoneNum");
            return (Criteria) this;
        }

        public Criteria andSendPhoneNumNotEqualTo(String value) {
            addCriterion("send_phone_num <>", value, "sendPhoneNum");
            return (Criteria) this;
        }

        public Criteria andSendPhoneNumGreaterThan(String value) {
            addCriterion("send_phone_num >", value, "sendPhoneNum");
            return (Criteria) this;
        }

        public Criteria andSendPhoneNumGreaterThanOrEqualTo(String value) {
            addCriterion("send_phone_num >=", value, "sendPhoneNum");
            return (Criteria) this;
        }

        public Criteria andSendPhoneNumLessThan(String value) {
            addCriterion("send_phone_num <", value, "sendPhoneNum");
            return (Criteria) this;
        }

        public Criteria andSendPhoneNumLessThanOrEqualTo(String value) {
            addCriterion("send_phone_num <=", value, "sendPhoneNum");
            return (Criteria) this;
        }

        public Criteria andSendPhoneNumLike(String value) {
            addCriterion("send_phone_num like", value, "sendPhoneNum");
            return (Criteria) this;
        }

        public Criteria andSendPhoneNumNotLike(String value) {
            addCriterion("send_phone_num not like", value, "sendPhoneNum");
            return (Criteria) this;
        }

        public Criteria andSendPhoneNumIn(List<String> values) {
            addCriterion("send_phone_num in", values, "sendPhoneNum");
            return (Criteria) this;
        }

        public Criteria andSendPhoneNumNotIn(List<String> values) {
            addCriterion("send_phone_num not in", values, "sendPhoneNum");
            return (Criteria) this;
        }

        public Criteria andSendPhoneNumBetween(String value1, String value2) {
            addCriterion("send_phone_num between", value1, value2, "sendPhoneNum");
            return (Criteria) this;
        }

        public Criteria andSendPhoneNumNotBetween(String value1, String value2) {
            addCriterion("send_phone_num not between", value1, value2, "sendPhoneNum");
            return (Criteria) this;
        }

        public Criteria andContextIsNull() {
            addCriterion("context is null");
            return (Criteria) this;
        }

        public Criteria andContextIsNotNull() {
            addCriterion("context is not null");
            return (Criteria) this;
        }

        public Criteria andContextEqualTo(String value) {
            addCriterion("context =", value, "context");
            return (Criteria) this;
        }

        public Criteria andContextNotEqualTo(String value) {
            addCriterion("context <>", value, "context");
            return (Criteria) this;
        }

        public Criteria andContextGreaterThan(String value) {
            addCriterion("context >", value, "context");
            return (Criteria) this;
        }

        public Criteria andContextGreaterThanOrEqualTo(String value) {
            addCriterion("context >=", value, "context");
            return (Criteria) this;
        }

        public Criteria andContextLessThan(String value) {
            addCriterion("context <", value, "context");
            return (Criteria) this;
        }

        public Criteria andContextLessThanOrEqualTo(String value) {
            addCriterion("context <=", value, "context");
            return (Criteria) this;
        }

        public Criteria andContextLike(String value) {
            addCriterion("context like", value, "context");
            return (Criteria) this;
        }

        public Criteria andContextNotLike(String value) {
            addCriterion("context not like", value, "context");
            return (Criteria) this;
        }

        public Criteria andContextIn(List<String> values) {
            addCriterion("context in", values, "context");
            return (Criteria) this;
        }

        public Criteria andContextNotIn(List<String> values) {
            addCriterion("context not in", values, "context");
            return (Criteria) this;
        }

        public Criteria andContextBetween(String value1, String value2) {
            addCriterion("context between", value1, value2, "context");
            return (Criteria) this;
        }

        public Criteria andContextNotBetween(String value1, String value2) {
            addCriterion("context not between", value1, value2, "context");
            return (Criteria) this;
        }

        public Criteria andSendTimeIsNull() {
            addCriterion("send_time is null");
            return (Criteria) this;
        }

        public Criteria andSendTimeIsNotNull() {
            addCriterion("send_time is not null");
            return (Criteria) this;
        }

        public Criteria andSendTimeEqualTo(Date value) {
            addCriterion("send_time =", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeNotEqualTo(Date value) {
            addCriterion("send_time <>", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeGreaterThan(Date value) {
            addCriterion("send_time >", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("send_time >=", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeLessThan(Date value) {
            addCriterion("send_time <", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeLessThanOrEqualTo(Date value) {
            addCriterion("send_time <=", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeIn(List<Date> values) {
            addCriterion("send_time in", values, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeNotIn(List<Date> values) {
            addCriterion("send_time not in", values, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeBetween(Date value1, Date value2) {
            addCriterion("send_time between", value1, value2, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeNotBetween(Date value1, Date value2) {
            addCriterion("send_time not between", value1, value2, "sendTime");
            return (Criteria) this;
        }

        public Criteria andRecPhoneIsNull() {
            addCriterion("rec_phone is null");
            return (Criteria) this;
        }

        public Criteria andRecPhoneIsNotNull() {
            addCriterion("rec_phone is not null");
            return (Criteria) this;
        }

        public Criteria andRecPhoneEqualTo(String value) {
            addCriterion("rec_phone =", value, "recPhone");
            return (Criteria) this;
        }

        public Criteria andRecPhoneNotEqualTo(String value) {
            addCriterion("rec_phone <>", value, "recPhone");
            return (Criteria) this;
        }

        public Criteria andRecPhoneGreaterThan(String value) {
            addCriterion("rec_phone >", value, "recPhone");
            return (Criteria) this;
        }

        public Criteria andRecPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("rec_phone >=", value, "recPhone");
            return (Criteria) this;
        }

        public Criteria andRecPhoneLessThan(String value) {
            addCriterion("rec_phone <", value, "recPhone");
            return (Criteria) this;
        }

        public Criteria andRecPhoneLessThanOrEqualTo(String value) {
            addCriterion("rec_phone <=", value, "recPhone");
            return (Criteria) this;
        }

        public Criteria andRecPhoneLike(String value) {
            addCriterion("rec_phone like", value, "recPhone");
            return (Criteria) this;
        }

        public Criteria andRecPhoneNotLike(String value) {
            addCriterion("rec_phone not like", value, "recPhone");
            return (Criteria) this;
        }

        public Criteria andRecPhoneIn(List<String> values) {
            addCriterion("rec_phone in", values, "recPhone");
            return (Criteria) this;
        }

        public Criteria andRecPhoneNotIn(List<String> values) {
            addCriterion("rec_phone not in", values, "recPhone");
            return (Criteria) this;
        }

        public Criteria andRecPhoneBetween(String value1, String value2) {
            addCriterion("rec_phone between", value1, value2, "recPhone");
            return (Criteria) this;
        }

        public Criteria andRecPhoneNotBetween(String value1, String value2) {
            addCriterion("rec_phone not between", value1, value2, "recPhone");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}