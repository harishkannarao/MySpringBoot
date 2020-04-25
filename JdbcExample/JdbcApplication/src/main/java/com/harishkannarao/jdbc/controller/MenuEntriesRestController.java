package com.harishkannarao.jdbc.controller;

import com.harishkannarao.jdbc.dao.MenuEntriesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/menuentries", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MenuEntriesRestController extends AbstractBaseController{
    private MenuEntriesDao menuEntriesDao;

    @Autowired
    public MenuEntriesRestController(@Qualifier("myMenuEntriesDao") MenuEntriesDao menuEntriesDao) {
        this.menuEntriesDao = menuEntriesDao;
    }

    @GetMapping
    public List<String> getAllMenuEntries() {
        return menuEntriesDao.getMenuEntries();
    }
}
