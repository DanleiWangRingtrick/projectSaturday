package de.ls5.wt2.auth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import de.ls5.wt2.conf.auth.permission.ViewFirstFiveNewsItemsPermission;
import de.ls5.wt2.entity.DBTodos;
import de.ls5.wt2.entity.DBTodos_;
import de.ls5.wt2.entity.DBUsers;
import de.ls5.wt2.entity.DBUsersNotesUnion;
import de.ls5.wt2.enums.UserRoleEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.subject.Subject;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequestMapping(path = {"rest/auth/session/news", "rest/auth/basic/news", "rest/auth/jwt/news"})
//@Api(value = "WEB - AuthNewsREST", tags = "业务接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AuthNewsREST {

    @Autowired
    private EntityManager entityManager;

    /**
     * @Description: 获取最新一条笔记
     * @Date: 2022/6/30
     */
    @GetMapping(path = "newest", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "POST", value = "获取最新一条笔记")
    public ResponseEntity<DBTodos> readNewestNews() {

        final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String username = subject.getPrincipal().toString();
        Query queryRole = entityManager.createNativeQuery("select u.role from DBUsers u where u.username = '" + username + "' order by u.id desc");
        String role = (String) queryRole.getSingleResult();
        //管理员可以看全部笔记
        if (UserRoleEnum.ADMIN.name.equals(role)) {
            final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            final CriteriaQuery<DBTodos> query = builder.createQuery(DBTodos.class);
            final Root<DBTodos> from = query.from(DBTodos.class);
            query.select(from);
            final List<DBTodos> resultList = this.entityManager.createQuery(query).getResultList();
            if (!resultList.isEmpty()) {
                return ResponseEntity.ok(resultList.get(0));
            } else {
                return ResponseEntity.ok(null);
            }

        }
        //用户角色只能看配置为可见的笔记
        else {
            Query nativeQuery = entityManager.createNativeQuery("select  t.id as id  ,t.content  as content ,t.headline as headline ,t.PUBLISHED_ON  as publishedOn  from DBTodos t left join DBUSERS_NOTES_UNION   unt on t.id = unt.NOTE_ID  left join DBUsers  u on u.username =  unt.username   where u.username = '" + username + "'", DBTodos.class);
            List<DBTodos> resultList = nativeQuery.getResultList();
            if (!resultList.isEmpty()) {
                return ResponseEntity.ok(resultList.get(0));
            } else {
                return ResponseEntity.ok(null);
            }
        }
//
//        // Attribute based permission check using permissions
////        final Subject subject = SecurityUtils.getSubject();
//        final Permission firstFiveNewsItemsPermission = new ViewFirstFiveNewsItemsPermission(result);
//
//        if (!subject.isPermitted(firstFiveNewsItemsPermission)) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//
//        return ResponseEntity.ok(result.get(0));
    }

    /**
     * @Description: 获取笔记列表
     * @Date: 2022/6/30
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "GET", value = "获取笔记列表")
    public ResponseEntity<List<DBTodos>> readAllAsJSON() {
        final Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String username = subject.getPrincipal().toString();
        Query queryRole = entityManager.createNativeQuery("select u.role from DBUsers u where u.username = '" + username + "'");
        String role = (String) queryRole.getSingleResult();
        //管理员可以看全部笔记
        if (UserRoleEnum.ADMIN.name.equals(role)) {
            final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            final CriteriaQuery<DBTodos> query = builder.createQuery(DBTodos.class);
            final Root<DBTodos> from = query.from(DBTodos.class);
            query.select(from);
            final List<DBTodos> resultList = this.entityManager.createQuery(query).getResultList();
            return ResponseEntity.ok(resultList);
        }
        //用户角色只能看配置为可见的笔记
        else {
            Query nativeQuery = entityManager.createNativeQuery("select  t.id as id  ,t.content  as content ,t.headline as headline ,t.PUBLISHED_ON  as publishedOn  from DBTodos t left join DBUSERS_NOTES_UNION   unt on t.id = unt.NOTE_ID  left join DBUsers  u on u.username =  unt.username   where u.username = '" + username + "'", DBTodos.class);
            List<DBTodos> resultList = nativeQuery.getResultList();
            return ResponseEntity.ok(resultList);
        }
    }

    /**
     * @Description: 新增笔记
     * @Date: 2022/6/30
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(httpMethod = "POST", value = "新增笔记")
    public DBTodos create(@RequestBody final DBTodos param) {

        String username = SecurityUtils.getSubject().getPrincipal().toString();
        final DBTodos news = new DBTodos();

        news.setHeadline(param.getHeadline());
        news.setContent(param.getContent());
        news.setPublishedOn(new Date());

        this.entityManager.persist(news);

        DBUsersNotesUnion dbUsersNotesUnion = new DBUsersNotesUnion();

        dbUsersNotesUnion.setUsername(username);
        dbUsersNotesUnion.setNoteId(news.getId());

        this.entityManager.persist(dbUsersNotesUnion);

        return news;
    }




}
