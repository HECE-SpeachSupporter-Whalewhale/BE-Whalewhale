package com.whalewhale.speachsupporter;// 클래스를 이 파일에서 사용하기 위해 있다.

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class ItemController {
    //상품과 관련된 API 보관 예정 public 쓰면 다른 폴더에서도 사용가능 일반적으로 public 씀

    //롬복 없이 사용 하는 방법
    private final ItemRepository itemRepository;//db입출력 함수 들어있다
    private final ItemService itemService;
//    @Autowired//new ItemRepository() 하나 뽑아서 itemRepository 변수에 넣으라고 시킨다
//    public ItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }


    @GetMapping("/list")
    String list(Model model){
        //JPA로 데이터 입출력 하려면 1. repository만들기, 2.DB입출력 원하는 클래스에 repository 등록, 3. 사용
        List<Item> result = itemRepository.findAll();
        model.addAttribute("items", result);

        return "list.html";
    }

    @GetMapping("/write")
    String write(){
        return "write.html";
    }

    //url 파라미터 문법 사용
    @GetMapping("/detail/{id}")

    String detail(@PathVariable Long id, Model model) {

        Optional<Item> result = itemRepository.findById(id);
        //데이터를 못 찾아오는 상황도 있으므로 optional 사용함
        if (result.isPresent()//자료가 있다면
        ) {
            model.addAttribute("data", result.get());
            System.out.println(result.get());
            return "detail.html";
        } else {
            return "redirect:/list";
        }

    }


//함수 하나에는 하나의 기능
    @PostMapping("/add")
    String addPost(String title, Integer price){
        //1.new Class() 할 클래스에 @Service 2. 사용할 곳에서 변수로 등록하기
        //의존성 주입 Dependency injection 1.object 여러개 안 뽑아도 되서 효율적이다 2. 클래스간의 커플링을 줄일 수 있다
        itemService.saveItem(title, price);
        return "redirect:/list";
    }

    @GetMapping ("/edit/{id}")
    String edit(Model model, @PathVariable Long id) {

        Optional<Item> result = itemRepository.findById(id);
        if (result.isPresent()){
            model.addAttribute("data", result.get());
            return "edit.html";
        }else{
            return "redirect:/list";
        }

    }

    @PostMapping("/edit")
    String editItem(@RequestParam String title,@RequestParam Integer price, Long id) {
        Item item = new Item();
        item.setId(id);
        item.setTitle(title);
        item.setPrice(price);
        itemRepository.save(item);

        return "redirect:/list";
    }
//    var item = new Item();
//    item.id = 1L;
//    itemRepository.save(item)

}
