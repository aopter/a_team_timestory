package com.example.timestroy.entity.card;

public class UserCard {
    private Integer userCardId;//用户卡片标识符
    private Integer cardCount;//卡片数量

    private Card cardListVO;//卡片

    public Integer getUserCardId() {
        return userCardId;
    }

    public void setUserCardId(Integer userCardId) {
        this.userCardId = userCardId;
    }

    public Integer getCardCount() {
        return cardCount;
    }

    public void setCardCount(Integer cardCount) {
        this.cardCount = cardCount;
    }

    public Card getCardListVO() {
        return cardListVO;
    }

    public void setCardListVO(Card cardListVO) {
        this.cardListVO = cardListVO;
    }

    @Override
    public String toString() {
        return "UserCard{" +
                "userCardId=" + userCardId +
                ", cardCount=" + cardCount +
                ", cardListVO=" + cardListVO +
                '}';
    }
}
