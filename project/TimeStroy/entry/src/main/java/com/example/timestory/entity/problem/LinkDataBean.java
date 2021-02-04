package com.example.timestory.entity.problem;

import java.io.Serializable;

/**
 * @Description: 连线的原数据
 * @Version
 */
public class LinkDataBean implements Serializable {
    /**
     * content : chair
     * q_num : 0
     * col : 0
     * row : 0
     */
    private String content;
    private int q_num;
    private int col;
    private int row;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getQ_num() {
        return q_num;
    }

    public void setQ_num(int q_num) {
        this.q_num = q_num;
    }


    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public String toString() {
        return "LinkDataBean{" +
                "content='" + content + '\'' +
                ", q_num=" + q_num +
                ", col=" + col +
                ", row=" + row +
                '}';
    }
}
