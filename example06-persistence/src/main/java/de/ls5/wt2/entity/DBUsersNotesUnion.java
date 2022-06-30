package de.ls5.wt2.entity;

import javax.persistence.Entity;

/**
 * @Description: 用户与笔记关联表实体类
 * @Date: 2022/6/30
 */
@Entity
public class DBUsersNotesUnion extends DBIdentified {


    /**
     * @Description: 用户名
     */
    private String username;

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    /**
     * @Description: 笔记表Id
     */
    private Long noteId;




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}