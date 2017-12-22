package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;


@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private MenuDao MenuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String justmenu(Model model){

        model.addAttribute("title", "view Menu");
        Iterable<Menu> menus= MenuDao.findAll();
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println(menus);
        model.addAttribute("menus",menus);

        return "menu/index";

    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddMenuForm(Model model) {
        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());
        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@ModelAttribute @Valid Menu newMenu, Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");

            System.out.println("****************************");
            System.out.println(newMenu.getName());

            return "menu/add";
        }

        System.out.println(newMenu.getName());
        MenuDao.save(newMenu);
        return "redirect:view/" + newMenu.getId();
    }

    @RequestMapping(value="/view/{Id}", method = RequestMethod.GET)
    public String viewMenu(@PathVariable(value="Id") int ID,Model model) {

        model.addAttribute("title", "Add Menu");
        Menu menu = MenuDao.findOne(ID);
        model.addAttribute(menu);

        return "menu/view";
    }


    @RequestMapping(value="/add-item/{Id}", method = RequestMethod.GET)
    public String addItem(@PathVariable(value="Id") int ID,Model model) {

        Menu menu = MenuDao.findOne(ID);
        model.addAttribute("title", "Add Menu");

        model.addAttribute("form", new AddMenuItemForm(menu,cheeseDao.findAll() ));

        return "menu/add-item";
    }

    @RequestMapping(value="/add-item", method = RequestMethod.POST)
    public String addItem(@ModelAttribute @Valid AddMenuItemForm form, Errors errors, Model model) {


        System.out.println(form);
        System.out.println(form);

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");

            System.out.println("****************************");
            System.out.println(form.getCheeses());

            return "menu/add-item";
        }

        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+ form.getCheeseId());
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + form.getId());

        Cheese theCheese = cheeseDao.findOne(form.getCheeseId());
        Menu theMenu = MenuDao.findOne(form.getId());
        theMenu.addItem(theCheese);
        MenuDao.save(theMenu);


        return "redirect:view/" + theMenu.getId();
    }


}
