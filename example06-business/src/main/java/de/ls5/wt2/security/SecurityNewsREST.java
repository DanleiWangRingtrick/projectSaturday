//package de.ls5.wt2.security;
//
//import java.util.Date;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Order;
//import javax.persistence.criteria.Root;
//
//import de.ls5.wt2.entity.DBTodos;
//import de.ls5.wt2.entity.DBTodos_;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Transactional
//@RestController
//@RequestMapping(path = "rest/security/news")
//public class SecurityNewsREST {
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @GetMapping(path = "newest",
//                produces = MediaType.APPLICATION_JSON_VALUE)
//    public DBTodos readNewestNews() {
//        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
//        final CriteriaQuery<DBTodos> query = builder.createQuery(DBTodos.class);
//
//        final Root<DBTodos> from = query.from(DBTodos.class);
//
//        final Order order = builder.desc(from.get(DBTodos_.publishedOn));
//
//        query.select(from).orderBy(order);
//
//        return this.entityManager.createQuery(query).setMaxResults(1).getSingleResult();
//    }
//
//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<DBTodos> readAllAsJSON() {
//        final CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
//        final CriteriaQuery<DBTodos> query = builder.createQuery(DBTodos.class);
//
//        final Root<DBTodos> from = query.from(DBTodos.class);
//
//        query.select(from);
//
//        return this.entityManager.createQuery(query).getResultList();
//    }
//
//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
//                 produces = MediaType.APPLICATION_JSON_VALUE)
//    public DBTodos createUnsafe(@RequestBody final DBTodos param) {
//
//        final DBTodos news = new DBTodos();
//
//        news.setHeadline(param.getHeadline());
//        // news.setContent(param.getContent());
//        news.setPublishedOn(new Date());
//
//        this.entityManager.persist(news);
//        this.entityManager.flush();
//
//        int upd = this.entityManager.createNativeQuery(
//                "UPDATE DBNews SET content = '" + param.getContent() + "' WHERE id = " + news.getId() + ';')
//                                    .executeUpdate();
//
//        System.err.println(upd);
//
//        return this.entityManager.find(DBTodos.class, news.getId());
//    }
//}
