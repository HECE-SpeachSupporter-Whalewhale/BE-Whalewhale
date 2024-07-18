package com.whalewhale.speachsupporter;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//비즈니스 로직은 주로 서비스라고 이름짓는다.
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public void saveItem(String title,Integer price){
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);

        // Item 객체를 DB에 저장
        // db 입출력 하려면? 1. repository interface 만들고, 2. 등록하고, 3. 사용
        itemRepository.save(item);
    }

}
