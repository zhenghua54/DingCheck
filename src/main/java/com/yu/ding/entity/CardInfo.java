package com.yu.ding.entity;


/**
 * @author MrYu(YWG)
 * @date 2022-03-06 13:37:00
 * 打卡信息
 */
public class CardInfo {
    /**
     * 打卡时间
     */
    private Long cardTime;
    /**
     * 打卡类型
     */
    private String sourceType;
    /**
     * 打卡地点
     */
    private String userAddress;
//
//    public CardInfo() {
//    }

    public CardInfo(Long cardTime, String sourceType, String userAddress) {
        this.cardTime = cardTime;
        this.sourceType = sourceType;
        this.userAddress = userAddress;
    }

    public Long getCardTime() {
        return cardTime;
    }

    public void setCardTime(Long cardTime) {
        this.cardTime = cardTime;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
    public int compare(CardInfo another){
        return this.cardTime < another.getCardTime() ? -1 : ((this.cardTime.equals(another.getCardTime())) ? 0 : 1);
    }

    @Override
    public String toString() {
        return "CardInfo{" +
                "cardTime=" + cardTime +
                ", sourceType='" + sourceType + '\'' +
                ", userAddress='" + userAddress + '\'' +
                '}';
    }
}
