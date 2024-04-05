package com.shop.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/*  @Configuration : 해당 클래스가 Application 설정을 담당함을 나타냄
    빈(Bean)정의, property 설정, 컴포넌트 스캔과 같은 설정 작업을 수행
    XML 파일을 대체하여 JAVA 코드로 Application 설정을 정의
*/
@Configuration
// 생성자 주입
@RequiredArgsConstructor
public class JPAConfig {    // config : 환경설정 / JPAConfig : JPA환경설정

    private final EntityManager em;//EntityManager 영속성 컨텍스트에 접근하여 엔티티에 대한 DB 작업을 제공

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em){
        return new JPAQueryFactory(em);
    }
}
