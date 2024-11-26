package com.example.oj.common;


public enum RunStatus {

    AC(1, "Accepted"),
    WA(0, "Wrong Answer"),
    RE(2, "Runtime Error"),
    CE(3, "Compile Error"),
    TLE(4, "Time Limit Exceed"),
    MLE(5, "Memory Limit Exceed"),
    PE(6, "Presentation Error"),
    OLE(7, "Output Limit Exceed"),
    ULE(8, "Unknown Error");
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
