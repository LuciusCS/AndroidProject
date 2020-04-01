package com.example.glidemodule.resources;

import com.example.glidemodule.utils.Tool;

public class Path {

    private String path;  //例如  ac43474d52403e60fe21894520a67d6f417a6868994c145eeb26712472a78311

    /**
     * 加密前 sha256 (https://imgconvert.csdnimg.cn/aHR0cHM6Ly9tbWJpei5xcGljLmNuL21tYml6X3BuZy90cm01Vk1lRnA5blVESWdHaWJkVnlFSnk3MmlieVdXOElDWGlhamZDNlliNTc3YnVRTUdtYmUxR1hxSmhwVjNvUzhyWktENTIzODNVZDFpYkh6VTJDakxrUUEvNjQw?x-oss-process=image/format,png)
     * 加密后  ac43474d52403e60fe21894520a67d6f417a6868994c145eeb26712472a78311
     * @param path
     */

    public Path(String path) {
        this.path = Tool.getSHA256StrJava(path);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
