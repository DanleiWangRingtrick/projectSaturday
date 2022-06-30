package de.ls5.wt2.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "笔记主键主键入参模型")
public class NoteIdDto {

    @ApiModelProperty(value = "笔记主键")
    private Long noteId;

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

}
