package hello.itemservice.web.basic;


import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository; //Autowired 생략 + 롬복

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "/basic/items";
    }

    @GetMapping("/{itemId}")
    public String items(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    /**
     * @ModelAttribute에는 한 가지 기능이 더 있는데,
     * 바로 모델(Model)에 @ModelAttribute 로 지정한 객체를 자동으로 넣어주는 것이다.
     * - key 값은 @ModelAttribute 에 지정한 name(value) 속성, 없으면 클래스명 첫자 소문자
     */
    @PostMapping("/add")
    public String save(@ModelAttribute Item item, Model model) {
        itemRepository.save(item);
//        model.addAttribute("item", item); //@ModelAttribute가 자동으로 넣어준다!
        return "/basic/item";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {

        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
