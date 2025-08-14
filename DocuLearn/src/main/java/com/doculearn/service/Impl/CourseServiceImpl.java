package com.doculearn.service.Impl;

import com.doculearn.pojo.Course;
import com.doculearn.pojo.Summary;
import com.doculearn.repositories.CourseRepository;
import com.doculearn.service.CourseService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepo;

//    @PersistenceContext
//    private EntityManager entityManager;
//
//    private static final int PAGE_SIZE = 10;
//
//    public List<Course> getPosts(Map<String, String> params) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
//        Root<Course> root = cq.from(Course.class);
//        cq.select(root);
//
//        List<Predicate> predicates = new ArrayList<>();
//
//        // Tìm theo postId
//        String postIdStr = params.get("postId");
//        if (postIdStr != null && !postIdStr.isEmpty()) {
//            predicates.add(cb.equal(root.get("id"), Integer.parseInt(postIdStr)));
//        }
//
//        // Tìm theo userId
//        String userIdStr = params.get("userId");
//        if (userIdStr != null && !userIdStr.isEmpty()) {
//            predicates.add(cb.equal(root.get("userId").get("id"), Integer.parseInt(userIdStr)));
//        }
//
//        // Tìm theo username
//        String username = params.get("username");
//        if (username != null && !username.isEmpty()) {
//            predicates.add(cb.like(root.get("userId").get("username"), "%" + username + "%"));
//        }
//
//        // Tìm theo content
//        String content = params.get("content");
//        if (content != null && !content.isEmpty()) {
//            predicates.add(cb.like(root.get("content"), "%" + content + "%"));
//        }
//
//        // Tìm theo thời gian
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//        String startDate = params.get("startDate");
//        if (startDate != null && !startDate.isEmpty()) {
//            LocalDateTime start = LocalDateTime.parse(startDate, formatter);
//            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), start));
//        }
//
//        String endDate = params.get("endDate");
//        if (endDate != null && !endDate.isEmpty()) {
//            LocalDateTime end = LocalDateTime.parse(endDate, formatter);
//            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), end));
//        }
//
//        // Thêm điều kiện WHERE
//        cq.where(predicates.toArray(new Predicate[0]));
//
//        // Sắp xếp
//        String order = params.get("order");
//        if ("desc".equalsIgnoreCase(order)) {
//            cq.orderBy(cb.desc(root.get("createdAt")));
//        } else {
//            cq.orderBy(cb.asc(root.get("createdAt")));
//        }
//
//        // Tạo query
//        TypedQuery<Post> query = entityManager.createQuery(cq);
//
//        // Phân trang
//        if (params.containsKey("page")) {
//            int page = Integer.parseInt(params.get("page"));
//            int start = (page - 1) * PAGE_SIZE;
//            query.setFirstResult(start);
//            query.setMaxResults(PAGE_SIZE);
//        }
//
//        return query.getResultList();
//    }
    @Override
    public List<Course> getAllCourses(){
        return  this.courseRepo.findAll();
    }
    @Override
    public List<Summary> getAllSummaryByCourseId(int courseId){
        Optional<Course> courses = this.courseRepo.findById(courseId);
        if(courses.isPresent()){
            Course course = courses.get();
            return  new ArrayList<>(course.getSummaries());

        }
        return new ArrayList<>();
    }

    @Override
    public Course getCourseById(int id){
        Optional<Course> courses = this.courseRepo.findById(id);
        return courses.orElse(null);
    }

    @Override
    public Course createOrUpdateCourse(Course course){
        return  this.courseRepo.save(course);
    }

    @Override
    public void deleteCourse(int id){
        this.courseRepo.deleteById(id);
    }
}
