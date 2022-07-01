package de.ls5.wt2;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import de.ls5.wt2.entity.DBTodos;
import de.ls5.wt2.entity.DBUsers;
import de.ls5.wt2.entity.DBUsersNotesUnion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Transactional
public class StartupBean implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        final DBTodos firstNewsItem = this.entityManager.find(DBTodos.class, 1L);

        final DBUsers adminUser = this.entityManager.find(DBUsers.class, 1L);

        if (adminUser == null) {
            final DBUsers admin = new DBUsers();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setRole("管理员");
            this.entityManager.persist(admin);

            // region 初始化添加两个普通用户
            final DBUsers user1 = new DBUsers();
            user1.setUsername("user1");
            user1.setPassword("user");
            user1.setRole("用户");
            this.entityManager.persist(user1);

            final DBUsers user2 = new DBUsers();
            user2.setUsername("user2");
            user2.setPassword("user");
            user2.setRole("用户");
            this.entityManager.persist(user2);

            // endregion

            // region 初始化创建一条用户user1可见的笔记
            DBTodos dbTodos1 = new DBTodos();
            dbTodos1.setHeadline("I was created by the " + user1.getUsername());
            dbTodos1.setPublishedOn(new Date());
            dbTodos1.setContent("hello,user1!!!");
            this.entityManager.persist(dbTodos1);

            DBUsersNotesUnion dbUsersNotesUnion1 = new DBUsersNotesUnion();
            dbUsersNotesUnion1.setUsername(user1.getUsername());
            dbUsersNotesUnion1.setNoteId(dbTodos1.getId());
            this.entityManager.persist(dbUsersNotesUnion1);
            // endregion

            // region 初始化创建一条用户user2可见的笔记
            DBTodos dbTodos2 = new DBTodos();
            dbTodos2.setHeadline("I was created by the " + user2.getUsername());
            dbTodos2.setPublishedOn(new Date());
            dbTodos2.setContent("hello,user2!!!");
            this.entityManager.persist(dbTodos2);

            DBUsersNotesUnion dbUsersNotesUnion2 = new DBUsersNotesUnion();
            dbUsersNotesUnion2.setUsername(user2.getUsername());
            dbUsersNotesUnion2.setNoteId(dbTodos2.getId());
            this.entityManager.persist(dbUsersNotesUnion2);
            // endregion

        }

        // only initialize once
//        if (firstNewsItem == null) {
//            final DBTodos news = new DBTodos();
//
//            news.setHeadline("Startup");
//            news.setContent("Startup Bean successfully executed");
//            news.setPublishedOn(new Date());
//
//            this.entityManager.persist(news);
//        }

        Query query = entityManager.createNativeQuery("select u.password from DBUsers u where u.username = 'admin'");
        String result = (String) query.getSingleResult();
        System.out.println("Hoffentlich gefundenes Passwort: " + result);
    }
}
