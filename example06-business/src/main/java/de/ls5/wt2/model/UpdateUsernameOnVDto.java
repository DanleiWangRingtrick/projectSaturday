package de.ls5.wt2.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "更新当前笔记可见用户入参模型")
public class UpdateUsernameOnVDto {

    @ApiModelProperty(value = "可见用户集合")
    private List<String> usernameOn;

    @ApiModelProperty(value = "笔记主键")
    private Long noteId;

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public List<String> getUsernameOn() {
        return usernameOn;
    }

    public void setUsernameOn(List<String> usernameOn) {
        this.usernameOn = usernameOn;
    }

}
