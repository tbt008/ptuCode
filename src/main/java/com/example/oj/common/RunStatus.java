package com.example.oj.common;


public enum RunStatus {

    AC(0, "AC"),
    WA(-1, "WA"),
    RE(4, "RE"),
    CLE(1, "CLE"),
    TLE(2, "TLE"),
    MLE(3, "MLE"),
    OLE(7, "OLE"),
    ULE(5, "UE");
//    状态码
        private final int code;
//        描述
        private final String name;
        RunStatus(int code, String name) {
            this.code = code;
            this.name = name;
        }
        public String getName() {
            return name;
        }
        public int getCode() {
            return code;
        }

}
