package de.ls5.wt2.auth;

import com.nimbusds.jose.JOSEException;
import de.ls5.wt2.conf.auth.jwt.JWTLoginData;
import de.ls5.wt2.conf.auth.jwt.JWTUtil;
import de.ls5.wt2.conf.auth.permission.ViewFirstFiveNewsItemsPermission;
import de.ls5.wt2.entity.DBTodos;
import de.ls5.wt2.entity.DBTodos_;
import de.ls5.wt2.entity.DBUsers;
import de.ls5.wt2.entity.DBUsersNotesUnion;
import de.ls5.wt2.enums.UserRoleEnum;
import de.ls5.wt2.model.UpdateUsernameOnVDto;
import de.ls5.wt2.model.UserListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.List;

@Transactional
@RestController
@RequestMapping(path = "rest/auth/jwt")
@Api(value = "WEB - AuthNewsREST", tags = "用户相关接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class JWTREST {

    @Autowired
    private EntityManager entityManager;

    @PostMapping(path = "authenticate",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "登录")
    public ResponseEntity<String> createJWToken(@RequestBody JWTLoginData credentials) throws JOSEException {

        // do some proper lookup
        final String user = credentials.getUsername();
        final String pwd = credentials.getPassword();

        if (!user.equals(pwd)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(JWTUtil.createJWToken(credentials));
    }

    /**
     * @Description: 用户注册
     * @Date: 2022/6/30
     */
    @PostMapping(path = "addUser", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "用户注册")
    public String createUser(@RequestBody final DBUsers param) {
        this.entityManager.persist(param);
        param.setRole(UserRoleEnum.USER.name);
        return param.getUsername();
    }

    /**
     * @Description: 获取用户列表与可见用户列表
     * @Date: 2022/6/30
     */
    @GetMapping(path = "getUserList",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "GET", value = "获取用户列表")
    public ResponseEntity<UserListVo> getUserList(Long noteId) {

        // region 获取所有角色为用户的账号名称
        UserListVo userListVo = new UserListVo();
        Query usernameAllQuery = entityManager.createNativeQuery("SELECT u.username FROM DBUSERS u where u.role=  '" + UserRoleEnum.USER.name + "'");
        List<String> usernameAll = usernameAllQuery.getResultList();
        userListVo.setUsernameAll(usernameAll);
        // endregion

        // region 获取所有角色为用户并且对当前笔记可见的账号名称
        Query usernameOnQuery = entityManager.createNativeQuery("SELECT u.username FROM DBUSERS u where u.username in (select unu.username from DBUSERS_NOTES_UNION unu  where unu.note_id = '" + noteId + "' ) and u.role=  '" + UserRoleEnum.USER.name + "'");
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
    @ApiOperation(httpMethod = "POST", value = "更新当前笔记可见用户")
    public ResponseEntity<UpdateUsernameOnVDto> updateUsernameOn(@RequestBody UpdateUsernameOnVDto updateUsernameOnVDto) {

        Query usernameOnQuery = entityManager.createNativeQuery("delete from DBUSERS_NOTES_UNION  unu  where unu.NOTE_ID = '" + updateUsernameOnVDto.getNoteId() + "' ");
        int i = usernameOnQuery.executeUpdate();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>executeUpdate:" + i);
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
    @ApiOperation(httpMethod = "POST", value = "编辑笔记")
    public ResponseEntity<DBTodos> editNote(@RequestBody DBTodos param) {
        Query updateNoteQuery = entityManager.createNativeQuery("update DBTODOS  set CONTENT  = '" + param.getContent() + "' ,HEADLINE ='" + param.getHeadline() + "' where id ='" + param.getId() + "'");
        updateNoteQuery.executeUpdate();
        return ResponseEntity.ok(param);
    }

}

