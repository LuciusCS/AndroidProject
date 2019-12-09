package demo.lucius.baselib.bean;

import androidx.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 创建接收服务器返回数据的类
 */
public class SearchRepoBean implements ParameterizedType {

    //用于表示仓库数量
    private int totalCount;
    //用于表示是否为完整结果
    private boolean incompleteResults;
    //用于表示持有的所有的仓库类
    private List<RepoBean>items;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public void setIncompleteResults(boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    public List<RepoBean> getItems() {
        return items;
    }

    public void setItems(List<RepoBean> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public Type[] getActualTypeArguments() {
        return new Type[0];
    }

    @NonNull
    @Override
    public Type getRawType() {
        return null;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
