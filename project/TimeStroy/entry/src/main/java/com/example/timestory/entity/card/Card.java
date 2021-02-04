package com.example.timestory.entity.card;

import java.io.Serializable;

public class Card implements Serializable {
    private int cardId;//卡片标识符
    private String cardName;//卡片名称
    private int cardType;//卡片分类
    private String cardInfo;//卡片简介
    private String cardPicture;//卡片图片
    private String cardStory;//卡片故事
    private String cardCreator;//创建人
    private long cardCreationTime;//创建时间
    private int dynastyId;//卡片所属朝代

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public String getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(String cardInfo) {
        this.cardInfo = cardInfo;
    }

    public String getCardPicture() {
        return cardPicture;
    }

    public void setCardPicture(String cardPicture) {
        this.cardPicture = cardPicture;
    }

    public String getCardStory() {
        return cardStory;
    }

    public void setCardStory(String cardStory) {
        this.cardStory = cardStory;
    }

    public String getCardCreator() {
        return cardCreator;
    }

    public void setCardCreator(String cardCreator) {
        this.cardCreator = cardCreator;
    }

    public long getCardCreationTime() {
        return cardCreationTime;
    }

    public void setCardCreationTime(long cardCreationTime) {
        this.cardCreationTime = cardCreationTime;
    }

    public int getDynastyId() {
        return dynastyId;
    }

    public void setDynastyId(int dynastyId) {
        this.dynastyId = dynastyId;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardId=" + cardId +
                ", cardName='" + cardName + '\'' +
                ", cardType=" + cardType +
                ", cardInfo='" + cardInfo + '\'' +
                ", cardPicture='" + cardPicture + '\'' +
                ", cardStory='" + cardStory + '\'' +
                ", cardCreator='" + cardCreator + '\'' +
                ", cardCreationTime=" + cardCreationTime +
                ", dynastyId=" + dynastyId +
                '}';
    }
}
