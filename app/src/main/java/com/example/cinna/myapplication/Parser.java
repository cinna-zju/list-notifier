package com.example.cinna.myapplication;

import java.io.InputStream;
import java.util.List;

/**
 * Created by cinna on 2017/2/3.
 */

public interface Parser {
    public List<item> parse(InputStream is) throws Exception;


}
