package de.ls5.wt2.model;


//@ApiModel(value = "编辑笔记入参模型")
public class EditNodeDto {

//    @ApiModelProperty(value = "笔记主键", required = true)
    private Long id;

//    @ApiModelProperty(value = "标题", required = true)
    private String headline;

//    @ApiModelProperty(value = "内容", required = true)
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
