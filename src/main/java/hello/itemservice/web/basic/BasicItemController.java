package hello.itemservice.web.basic;


import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    /*@PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, Model model) {
        itemRepository.save(item);
        model.addAttribute("item", item); //@ModelAttribute가 자동으로 넣어준다!
        return "/basic/item";
    }*/
    /*
    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "/basic/item"; //새로고침 시 마지막에 요청한 행위를 재요청하여 재등록됨..!
    }
    */
    /*
    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
        //redirect 시에도 @ModelAttribute에 있는 값을 쓸 수 있다.
        return "redirect:/basic/items/"+item.getId(); //PRG 패턴 적용 -> 마지막 요청을 GET으로 변경
    }*/

    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true); //남은 속성값은 쿼리파라미터 형식으로 들어감
        return "redirect:/basic/items/{itemId}"; //redirectAttribute의 값을 넣어줌. url 인코딩도 해결됨

    }
    /*
    * redirect:/basic/items/{itemId}
    * - pathVariable 바인딩: {itemId}
    * - 나머지는 쿼리 파라미터로 처리: ?status=true
    * => //http://localhost:8080/basic/items/3?status=true
    */

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/basic/editForm";
    }
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}"; //컨트롤러에 매핑된 @PathVariable 의 값은 redirect 에도 사용 할 수 있다.
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
