package com.example.tt.Controller;

import com.example.tt.Bean.PCOrderBean;
import com.example.tt.dao.*;
import com.example.tt.utils.JSONException;
import com.example.tt.utils.JSONObject;
import com.example.tt.utils.ReturnDataBuilder;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class FetchLotteryResultController {

    @Autowired(required = false)
    PCOrderBeanMapper pcOrderBeanMapper;

    @Autowired(required = false)
    LotteryOpenBeanMapper lotteryOpenBeanMapper;

    @Autowired(required = false)
    Lottery1SettingMapper lottery1SettingMapper;

    @Autowired(required = false)
    Lottery2SettingMapper lottery2SettingMapper;

    @Autowired(required = false)
    Lottery3SettingMapper lottery3SettingMapper;

    @Autowired(required = false)
    Lottery4SettingMapper lottery4SettingMapper;

    @Autowired(required = false)
    Lottery5SettingMapper lottery5SettingMapper;

    @Autowired(required = false)
    Lottery6SettingMapper lottery6SettingMapper;

    @Autowired(required = false)
    Lottery7SettingMapper lottery7SettingMapper;

    @Autowired(required = false)
    Lottery8SettingMapper lottery8SettingMapper;

    @Autowired(required = false)
    Lottery9SettingMapper lottery9SettingMapper;

    @Autowired(required = false)
    Lottery10SettingMapper lottery10SettingMapper;

    @Autowired(required = false)
    Lottery11SettingMapper lottery11SettingMapper;

    @Autowired(required = false)
    Lottery12SettingMapper lottery12SettingMapper;

    @Autowired(required = false)
    Lottery13SettingMapper lottery13SettingMapper;

    @Autowired(required = false)
    Lottery14SettingMapper lottery14SettingMapper;

    @Autowired(required = false)
    Lottery15SettingMapper lottery15SettingMapper;

    @Autowired(required = false)
    Lottery16SettingMapper lottery16SettingMapper;

    @Autowired(required = false)
    Lottery17SettingMapper lottery17SettingMapper;

    @Autowired(required = false)
    Lottery18SettingMapper lottery18SettingMapper;

    @Autowired(required = false)
    Lottery19SettingMapper lottery19SettingMapper;


}

