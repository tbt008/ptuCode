package com.example.oj.common;






public enum Language {
    C(1, "C" ,"{\n" +
    "    \"compile\": {\n" +
    "        \"src_name\": \"main.c\",\n" +
    "        \"exe_name\": \"main\",\n" +
    "        \"max_cpu_time\": 3000,\n" +
    "        \"max_real_time\": 5000,\n" +
    "        \"max_memory\": 128 * 1024 * 1024,\n" +
    "        \"compile_command\": \"/usr/bin/gcc -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c99 {src_path} -lm -o {exe_path}\"\n" +
    "    },\n" +
    "    \"run\": {\n" +
    "        \"command\": \"{exe_path}\",\n" +
    "        \"seccomp_rule\": \"c_cpp\",\n" +
    "    }\n" +
    "}"),
    CPP( 2, "C++","{\n" +
                "    \"compile\": {\n" +
                "        \"src_name\": \"main.cpp\",\n" +
                "        \"exe_name\": \"main\",\n" +
                "        \"max_cpu_time\": 3000,\n" +
                "        \"max_real_time\": 5000,\n" +
                "        \"max_memory\": 128 * 1024 * 1024,\n" +
                "        \"compile_command\": \"/usr/bin/g++ -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c++11 {src_path} -lm -o {exe_path}\"\n" +
                "    },\n" +
                "    \"run\": {\n" +
                "        \"command\": \"{exe_path}\",\n" +
                "        \"seccomp_rule\": \"c_cpp\"\n" +
                "    }\n" +
                "}"),
    JAVA( 3, "Java","{\n" +
                "    \"name\": \"java\",\n" +
                "    \"compile\": {\n" +
                "        \"src_name\": \"Main.java\",\n" +
                "        \"exe_name\": \"Main\",\n" +
                "        \"max_cpu_time\": 3000,\n" +
                "        \"max_real_time\": 5000,\n" +
                "        \"max_memory\": -1,\n" +
                "        \"compile_command\": \"/usr/bin/javac {src_path} -d {exe_dir} -encoding UTF8\"\n" +
                "    },\n" +
                "    \"run\": {\n" +
                "        \"command\": \"/usr/bin/java -cp {exe_dir} -XX:MaxRAM={max_memory}k -Djava.security.manager -Dfile.encoding=UTF-8 -Djava.security.policy=/etc/java_policy -Djava.awt.headless=true Main\",\n" +

                "        \"seccomp_rule\": None,\n" +

                "        \"memory_limit_check_only\": 1\n" +
                "    }\n" +
                "}"),
    PYTHON( 4, "Python","{\n" +
                "    \"compile\": {\n" +
                "        \"src_name\": \"solution.py\",\n" +
                "        \"exe_name\": \"__pycache__/solution.cpython-36.pyc\",\n" +
                "        \"max_cpu_time\": 3000,\n" +
                "        \"max_real_time\": 5000,\n" +
                "        \"max_memory\":  (128 * 1024 * 1024) ,\n"+
                "        \"compile_command\": \"/usr/bin/python3 -m py_compile {src_path}\"\n" +
                "    },\n" +
                "    \"run\": {\n" +
                "        \"command\": \"/usr/bin/python3 {exe_path}\",\n" +
                "        \"seccomp_rule\": \"general\"\n" +
                "    }\n" +
                "}");

    Language( Integer id,String name,String langConfig) {
        this.id = id;
        this.name = name;
        this.langConfig=langConfig;
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

    public String getLangConfig() {
        return langConfig;
    }

    private Integer id;
    private String name;

    private String langConfig;
}
