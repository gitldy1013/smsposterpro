package com.smsposterpro.core.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页请求参数
 *
 * @author 136****3167
 * @date 2020/10/20 12:20
 */
@Data
@ApiModel("分页对象")
public class PageWrap<M> {

    @ApiModelProperty("条件参数")
    private M model;

    @ApiModelProperty("目标页")
    private int page;

    @ApiModelProperty("一页多少行")
    private int capacity;

    @ApiModelProperty("排序参数")
    private List<SortData> sorts;

    /**
     * 处理异常排序对象
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    public List<SortData> getSorts() {
        List<SortData> sorts = new ArrayList<>();
        if (this.sorts == null) {
            return sorts;
        }
        for (SortData sort : this.sorts) {
            if (sort.getProperty() == null || sort.getProperty().trim().length() == 0) {
                continue;
            }
            if (sort.getDirection() == null || sort.getDirection().trim().length() == 0 || (!sort.getDirection().trim().equalsIgnoreCase("asc") && !sort.getDirection().trim().equalsIgnoreCase("desc"))) {
                continue;
            }
            sorts.add(sort);
        }
        return sorts;
    }

    /**
     * 处理异常页码
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    public int getPage() {
        return page <= 0 ? 1 : page;
    }

    /**
     * 处理异常页容量
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    public int getCapacity() {
        return capacity <= 0 ? 10 : capacity;
    }

    /**
     * 获取排序字符串
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    @ApiModelProperty(hidden = true)
    public String getOrderByClause() {
        List<SortData> sorts = this.getSorts();
        if (sorts.size() == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (SortData sortData : sorts) {
            stringBuilder.append(sortData.getProperty().trim());
            stringBuilder.append(" ");
            stringBuilder.append(sortData.getDirection().trim());
            stringBuilder.append(",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    /**
     * 排序对象
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    @Data
    @ApiModel("排序对象")
    public static class SortData {

        @ApiModelProperty("排序字段")
        private String property;

        @ApiModelProperty("排序方向（ASC：升序，DESC：降序）")
        private String direction;
    }
}
