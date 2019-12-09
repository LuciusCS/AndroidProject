package demo.lucius.baselib.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 用于表示请求返回的Repository信息
 */
public class RepoBean {

    //用于表示Repo id
    private int id;
    //用于表示RepoName
    private String name;

    //用于表示完整名称
    @SerializedName("full_name")
    private String fullName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
