package de.ls5.wt2.auth;

import com.nimbusds.jose.JOSEException;
import de.ls5.wt2.conf.auth.jwt.JWTLoginData;
import de.ls5.wt2.conf.auth.jwt.JWTUtil;
import de.ls5.wt2.entity.DBTodos;
import de.ls5.wt2.entity.DBUsers;
import de.ls5.wt2.entity.DBUsersNotesUnion;
import de.ls5.wt2.enums.UserRoleEnum;
import de.ls5.wt2.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Transactional
@RestController
@RequestMapping(path = "rest/auth/jwt")
//@Api(value = "WEB - AuthNewsREST", tags = "接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class JWTREST {

    @Autowired
    private EntityManager entityManager;

    @PostMapping(path = "authenticate",
            consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "POST", value = "登录")
    public ResponseEntity<String> createJWToken(@RequestBody JWTLoginData credentials) throws JOSEException {

        // do some proper lookup
        final String user = credentials.getUsername();
        final String password = credentials.getPassword();
        Query usernameQuery = entityManager.createNativeQuery("SELECT u.id,u.username,u.password,u.role FROM DBUSERS u where u.username=  '" + user + "'", DBUsers.class);
        List<DBUsers> userList = usernameQuery.getResultList();
        if (userList.isEmpty()) {
            return new ResponseEntity<>("用户名不存在！", HttpStatus.UNAUTHORIZED);
        }
        if (!userList.get(0).getPassword().equals(password)) {
            return new ResponseEntity<>("密码错误！", HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(JWTUtil.createJWToken(credentials));
    }

    /**
     * @Description: 用户注册
     * @Date: 2022/6/30
     */
    @PostMapping(path = "addUser", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "POST", value = "用户注册")
    public ResponseEntity<String> createUser(@RequestBody AddUserDto addUserDto) {

        Query usernameQuery = entityManager.createNativeQuery("select u.username from DBUSERS u where u.username = '" + addUserDto.getUsername() + "' ");
        List<String> usernameList = usernameQuery.getResultList();

        if (!usernameList.isEmpty()) {
            return new ResponseEntity<>("账户已存在！", HttpStatus.UNAUTHORIZED);
        }

        DBUsers dbUsers = new DBUsers();
        dbUsers.setUsername(addUserDto.getUsername());
        dbUsers.setPassword(addUserDto.getPassword());
        dbUsers.setRole(UserRoleEnum.USER.name);
        this.entityManager.persist(dbUsers);
        return ResponseEntity.ok(addUserDto.getUsername());
    }

    /**
     * @Description: 获取用户列表与可见用户列表
     * @Date: 2022/6/30
     */
    @GetMapping(path = "getUserList",
            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "获取用户列表")
    public ResponseEntity<UserListVo> getUserList(NoteIdDto noteIdDto) {

        // region 获取所有角色为用户的账号名称
        UserListVo userListVo = new UserListVo();
        Query usernameAllQuery = entityManager.createNativeQuery("SELECT u.username FROM DBUSERS u where u.role=  '" + UserRoleEnum.USER.name + "'");
        List<String> usernameAll = usernameAllQuery.getResultList();
        userListVo.setUsernameAll(usernameAll);
        // endregion

        // region 获取所有角色为用户并且对当前笔记可见的账号名称
        Query usernameOnQuery = entityManager.createNativeQuery("SELECT u.username FROM DBUSERS u where u.username in (select unu.username from DBUSERS_NOTES_UNION unu  where unu.note_id = '" + noteIdDto.getNoteId() + "' ) and u.role=  '" + UserRoleEnum.USER.name + "'");
        List<String> usernameOn = usernameOnQuery.getResultList();
        userListVo.setUsernameOn(usernameOn);
        // endregion

        return ResponseEntity.ok(userListVo);
    }

    /**
     * @Description: 更新当前笔记可见用户
     * @Date: 2022/6/30
     */
    @PostMapping(path = "updateUsernameOn", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "POST", value = "更新当前笔记可见用户")
    public ResponseEntity<UpdateUsernameOnVDto> updateUsernameOn(@RequestBody UpdateUsernameOnVDto updateUsernameOnVDto) {

        Query usernameOnQuery = entityManager.createNativeQuery("delete from DBUSERS_NOTES_UNION  unu  where unu.NOTE_ID = '" + updateUsernameOnVDto.getNoteId() + "' ");
        usernameOnQuery.executeUpdate();
        for (String item : updateUsernameOnVDto.getUsernameOn()) {
            DBUsersNotesUnion dbUsersNotesUnion = new DBUsersNotesUnion();
            dbUsersNotesUnion.setUsername(item);
            dbUsersNotesUnion.setNoteId(updateUsernameOnVDto.getNoteId());
            this.entityManager.persist(dbUsersNotesUnion);
        }
        return ResponseEntity.ok(updateUsernameOnVDto);
    }

    /**
     * @Description: 编辑笔记
     * @Date: 2022/6/30
     */
    @PostMapping(path = "editNote", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "POST", value = "编辑笔记")
    public ResponseEntity<DBTodos> editNote(@RequestBody EditNodeDto editNodeDto) {
        DBTodos dbTodos = new DBTodos();
        dbTodos.setId(editNodeDto.getId());
        dbTodos.setHeadline(editNodeDto.getHeadline());
        dbTodos.setContent(editNodeDto.getContent());
        Query updateNoteQuery = entityManager.createNativeQuery("update DBTODOS  set CONTENT  = '" + editNodeDto.getContent() + "' ,HEADLINE ='" + editNodeDto.getHeadline() + "' where id ='" + editNodeDto.getId() + "'");
        updateNoteQuery.executeUpdate();
        return ResponseEntity.ok(dbTodos);
    }

    /**
     * @Description: 删除笔记
     * @Date: 2022/6/30
     */
    @PostMapping(path = "deleteNote", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "POST", value = "删除笔记")
    public ResponseEntity<Integer> deleteNote(@RequestBody NoteIdDto noteIdDto) {
        Query deleteNoteQuery = entityManager.createNativeQuery("delete from DBTODOS  where id ='" + noteIdDto.getNoteId() + "'");
        int i = deleteNoteQuery.executeUpdate();

        Query deleteDBUsersNotesUnionQuery = entityManager.createNativeQuery("delete from DBUSERS_NOTES_UNION  where NOTE_ID ='" + noteIdDto.getNoteId() + "'");
        deleteDBUsersNotesUnionQuery.executeUpdate();

        return ResponseEntity.ok(i);
    }

}

