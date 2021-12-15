package com.longluo.ebookreader.model.flag;


public enum BookFormat {
    TXT("txt"), PDF("pdf"), EPUB("epub"), NB("nb"), NONE("none");

    private final String name;

    BookFormat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
