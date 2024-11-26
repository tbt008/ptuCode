package com.example.oj.common;

public enum Language {
    C(1, "C"),
    CPP( 2, "C++"),
    JAVA( 3, "Java"),
    PYTHON( 4, "Python");

    Language( Integer id,String name) {
        this.id = id;
        this.name = name;
    }

    public static boolean judgeById(Integer language) {
        for (Language value : values()){
            if (value.getId().equals(language)){
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    private Integer id;
    private String name;
}
