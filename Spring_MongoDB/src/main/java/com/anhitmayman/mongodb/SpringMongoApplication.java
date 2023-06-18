package com.anhitmayman.mongodb;

import com.anhitmayman.mongodb.document.Address;
import com.anhitmayman.mongodb.document.Gender;
import com.anhitmayman.mongodb.document.Student;
import com.anhitmayman.mongodb.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class SpringMongoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMongoApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository, MongoTemplate mongoTemplate) {
        return args -> {
            String email = "SangNguyen2@gmail.com";

            Address address = new Address(
                    "Viet Nam",
                    "Ho Chi Minh",
                    "HCM"
            );
            Student student = new Student(
                    null,
                    "Sang",
                    "Nguyen",
                    email,
                    Gender.MALE,
                    address,
                    Arrays.asList("Computer Science", "Maths"),
                    BigDecimal.TEN,
                    LocalDateTime.now()
            );

            /*
            * Queries from method name
            * */
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
            if (studentOptional.isPresent()) {
                System.out.println(student + " already exists");
            } else {
                System.out.println("Inserting student " + student);
                studentRepository.save(student);
            }
        };
    }

    private void usingMongoTemplateAndQuery(StudentRepository studentRepository, MongoTemplate mongoTemplate, String email, Student student) throws IllegalAccessException {
        /*
         * Mongo template allows us to basically use queries
         * */
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        List<Student> studentList = mongoTemplate.find(query, Student.class);

        if (studentList.size() > 1) {
            throw new IllegalAccessException("found many students with email " + email);
        }

        if (studentList.isEmpty()) {
            System.out.println("Inserting student " + student);
            studentRepository.save(student);
        } else {
            System.out.println(student + " already exists");
        }
    }

}
