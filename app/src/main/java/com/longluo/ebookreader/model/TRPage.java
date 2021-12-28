package com.longluo.ebookreader.model;

import java.util.List;


public class TRPage {
    private long begin;
    private long end;
    private List<String> lines;

    private String wholePageStr;

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public String getLineToString() {
        String text = "";
        if (lines != null) {
            for (String line : lines) {
                text += line;
            }
        }

        return text;
    }

    public String getWholePageStr() {
        return wholePageStr;
    }

    public void setWholePageStr() {
        this.wholePageStr = getLineToString();
    }
}
