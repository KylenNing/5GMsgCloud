package cc.htdf.msgcloud.common.domain;

import lombok.Data;

import java.util.List;

/**
 * author: JT
 * date: 2020/3/6
 * title:
 */
@Data
public class Page<T> {

    private Long page;

    private Long size;

    private Long pageCount;

    private List<T> list;

    public Page(long page, long size) {
        this.page = page;
        this.size = size;
    }

    public Page(long page, long size, long datacount) {
        this.page = page;
        this.size = size;
        pageCount(datacount);
    }

    public void pageCount(Long dataCount) {
        this.pageCount = dataCount % size == 0 ?
                dataCount/size : (dataCount/size) + 1;

    }

}
