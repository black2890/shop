package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import com.shop.repository.Item.ItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired //필드 주입
    ItemRepository itemRepository;

    /* 테스트 코드 2.5에서 삭제
    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }
    */

    public void createItemList(){
        for(int i=1; i<=10; i++){
            Item item = new Item();
            item.setItemNm("테스트 상품"+i);
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    public void createItemList2(){
        for(int i=1; i<=5; i++){
            Item item = new Item();
            item.setItemNm("테스트 상품"+i);
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
        for(int i=6; i<=10; i++){
            Item item = new Item();
            item.setItemNm("테스트 상품"+i);
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    // find + (Entity)이름 생략 + By + 변수 이름
    // -> find + (Item) + By + ItemNm
    // SELECT * FROM Item WHERE itemNm = "테스트 상품1";
    @Test   //단위 테스트를 수행하는 메서드 정의
    @DisplayName("상품명 조회 테스트")  //테스트 메서드(테스트 클래스) 이름을 "상품명 조회 테스트"로 설정
    public void findByItemNmTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    // find + (Entity)이름 생략 + By + 변수 이름 + 쿼리 메소드 추가 조건(OR) + 변수 이름
    // -> find + (Item) + By + ItemNm + Or + ItemDetail
    // SELECT * FROM Item WHERE itemNm = "테스트 상품1" OR itemDetail = "테스트 상품 상세 설명5";
    @Test   //단위 테스트를 수행하는 메서드 정의
    @DisplayName("상품명, 상품상세 설명 or 테스트")  //테스트 메서드(테스트 클래스) 이름을 "상품명, 상품 상세 설명 or 테스트"로 설정
    public void findByItemNmOrItemDetailTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    // find + (Entity)이름 생략 + By + 변수 이름 + 쿼리 메소드 추가 조건(LessThan)
    // -> find + (Item) + By + Price + LessThan
    // SELECT * FROM Item WHERE price<10005;
    @Test   //단위 테스트를 수행하는 메서드 정의
    @DisplayName("가격 LessThan 테스트")     //테스트 메서드(테스트 클래스) 이름을 "가격 LessThan 테스트"로 설정
    public void findByPriceLessThanTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    // find + (Entity)이름 생략 + By + 변수 이름 + 쿼리 메소드 추가 조건(LessThan) + 쿼리 메소드2 추가 조건(OrderBy) + 변수 이름 + 쿼리 메소드2 부가 조건(Desc)
    // -> find + (Item) + By + Price + LessThan + OrderBy + Price + Desc
    // SELECT * FROM Item WHERE price<10005 Order by Price Desc;
    @Test   //단위 테스트를 수행하는 메서드 정의
    @DisplayName("가격 내림차순 조회 테스트")      //테스트 메서드(테스트 클래스) 이름을 "가격 내림차순 조회 테스트"로 설정
    public void findByPriceLessThanOrderByPriceDesc(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test   //단위 테스트를 수행하는 메서드 정의
    @DisplayName("@Query를 이용한 상품 조회 테스트")      //테스트 메서드(테스트 클래스) 이름을 "가격 내림차순 조회 테스트"로 설정
    public void findByItemDetailTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test   //단위 테스트를 수행하는 메서드 정의
    @DisplayName("@Query에서 Native Query 속성을 이용한 상품 조회 테스트")      //테스트 메서드(테스트 클래스) 이름을 "가격 내림차순 조회 테스트"로 설정
    public void findByItemDetailByNative(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test   //단위 테스트를 수행하는 메서드 정의
    @DisplayName("@Query에서 Native Query 속성을 이용한 상품 조회 테스트")      //테스트 메서드(테스트 클래스) 이름을 "가격 내림차순 조회 테스트"로 설정
    public void queryDslTest(){
        this.createItemList();
        //ItemRepositoryCustomImple 메소드로 구현
//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
//        QItem qItem = QItem.item;
//        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
//                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
//                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
//                .orderBy(qItem.price.desc());
//
//        List<Item> itemList = query.fetch();
//
//        for(Item item : itemList){
//            System.out.println(item.toString());
//        }
//        for(Item item : itemRepository.queryDslTest()){
//            System.out.println(item.toString());
//        }
    }

    @Test   //단위 테스트를 수행하는 메서드 정의
    @DisplayName("@Query에서 Native Query 속성을 이용한 상품 조회 테스트2")      //테스트 메서드(테스트 클래스) 이름을 "가격 내림차순 조회 테스트"로 설정
    public void queryDslTest2(){

        this.createItemList2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;

        String itemDetail = "테스트 상품 상세 설명";
        int price = 1003;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));

        if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> itemPagingResult =
        itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPagingResult.getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem: resultItemList){
            System.out.println(resultItem.toString());
        }
    }
}