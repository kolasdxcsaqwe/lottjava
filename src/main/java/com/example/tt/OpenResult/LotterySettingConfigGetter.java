package com.example.tt.OpenResult;

import com.example.tt.Bean.*;
import com.example.tt.TtApplication;
import com.example.tt.dao.*;
import com.example.tt.utils.RedisCache;
import com.example.tt.utils.Strings;
import com.google.gson.Gson;

public class LotterySettingConfigGetter {

    private final int roomId = 10029;

    private long expireTime=10*60*1000;

    LotteryOpenBeanMapper lotteryOpenBeanMapper;

    Lottery1SettingMapper lottery1SettingMapper;

    Lottery2SettingMapper lottery2SettingMapper;

    Lottery3SettingMapper lottery3SettingMapper;

    Lottery4SettingMapper lottery4SettingMapper;

    Lottery5SettingMapper lottery5SettingMapper;

    Lottery6SettingMapper lottery6SettingMapper;

    Lottery7SettingMapper lottery7SettingMapper;

    Lottery8SettingMapper lottery8SettingMapper;

    Lottery9SettingMapper lottery9SettingMapper;

    Lottery10SettingMapper lottery10SettingMapper;

    Lottery11SettingMapper lottery11SettingMapper;

    Lottery12SettingMapper lottery12SettingMapper;

    Lottery13SettingMapper lottery13SettingMapper;

    Lottery14SettingMapper lottery14SettingMapper;

    Lottery15SettingMapper lottery15SettingMapper;

    Lottery16SettingMapper lottery16SettingMapper;

    Lottery17SettingMapper lottery17SettingMapper;

    Lottery18SettingMapper lottery18SettingMapper;

    Lottery19SettingMapper lottery19SettingMapper;

    private static LotterySettingConfigGetter result;

    Gson gson;

    public static LotterySettingConfigGetter getInstance() {
        if (result == null) {
            result = new LotterySettingConfigGetter();
        }
        return result;
    }

    public LotterySettingConfigGetter() {
        lotteryOpenBeanMapper = TtApplication.getContext().getBean(LotteryOpenBeanMapper.class);
        lottery1SettingMapper = TtApplication.getContext().getBean(Lottery1SettingMapper.class);
        lottery2SettingMapper = TtApplication.getContext().getBean(Lottery2SettingMapper.class);
        lottery3SettingMapper = TtApplication.getContext().getBean(Lottery3SettingMapper.class);
        lottery4SettingMapper = TtApplication.getContext().getBean(Lottery4SettingMapper.class);
        lottery5SettingMapper = TtApplication.getContext().getBean(Lottery5SettingMapper.class);
        lottery6SettingMapper = TtApplication.getContext().getBean(Lottery6SettingMapper.class);
        lottery7SettingMapper = TtApplication.getContext().getBean(Lottery7SettingMapper.class);
        lottery8SettingMapper = TtApplication.getContext().getBean(Lottery8SettingMapper.class);
        lottery9SettingMapper = TtApplication.getContext().getBean(Lottery9SettingMapper.class);
        lottery10SettingMapper = TtApplication.getContext().getBean(Lottery10SettingMapper.class);
        lottery11SettingMapper = TtApplication.getContext().getBean(Lottery11SettingMapper.class);
        lottery12SettingMapper = TtApplication.getContext().getBean(Lottery12SettingMapper.class);
        lottery13SettingMapper = TtApplication.getContext().getBean(Lottery13SettingMapper.class);
        lottery14SettingMapper = TtApplication.getContext().getBean(Lottery14SettingMapper.class);
        lottery15SettingMapper = TtApplication.getContext().getBean(Lottery15SettingMapper.class);
        lottery16SettingMapper = TtApplication.getContext().getBean(Lottery16SettingMapper.class);
        lottery17SettingMapper = TtApplication.getContext().getBean(Lottery17SettingMapper.class);
        lottery18SettingMapper = TtApplication.getContext().getBean(Lottery18SettingMapper.class);
        lottery19SettingMapper = TtApplication.getContext().getBean(Lottery19SettingMapper.class);
        gson = new Gson();
    }

    public Lottery1Setting getLottery1Setting() {
        String lottery1Setting1Str = RedisCache.getInstance().get("Lottery1Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery1Setting1Str)) {
            Lottery1Setting lottery1Setting = lottery1SettingMapper.selectByRoomId(roomId);
            lottery1Setting1Str = gson.toJson(lottery1Setting);
            RedisCache.getInstance().set("Lottery1Setting", lottery1Setting1Str,expireTime);
            return lottery1Setting;
        }
        return gson.fromJson(lottery1Setting1Str, Lottery1Setting.class);
    }

    public Lottery2Setting getLottery2Setting() {
        String lottery2Setting1Str = RedisCache.getInstance().get("Lottery2Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery2Setting1Str)) {
            Lottery2Setting lottery2Setting = lottery2SettingMapper.selectByRoomId(roomId);
            lottery2Setting1Str = gson.toJson(lottery2Setting);
            RedisCache.getInstance().set("Lottery2Setting", lottery2Setting1Str,expireTime);
            return lottery2Setting;
        }
        return gson.fromJson(lottery2Setting1Str, Lottery2Setting.class);
    }

    public Lottery3Setting getLottery3Setting() {
        String lottery3Setting1Str = RedisCache.getInstance().get("Lottery3Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery3Setting1Str)) {
            Lottery3Setting lottery3Setting = lottery3SettingMapper.selectByRoomId(roomId);
            lottery3Setting1Str = gson.toJson(lottery3Setting);
            RedisCache.getInstance().set("Lottery3Setting", lottery3Setting1Str,expireTime);
            return lottery3Setting;
        }
        return gson.fromJson(lottery3Setting1Str, Lottery3Setting.class);
    }

    public Lottery4Setting getLottery4Setting() {
        String lottery4Setting1Str = RedisCache.getInstance().get("Lottery4Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery4Setting1Str)) {
            Lottery4Setting lottery4Setting = lottery4SettingMapper.selectByRoomId(roomId);
            lottery4Setting1Str = gson.toJson(lottery4Setting);
            RedisCache.getInstance().set("Lottery4Setting", lottery4Setting1Str,expireTime);
            return lottery4Setting;
        }
        return gson.fromJson(lottery4Setting1Str, Lottery4Setting.class);
    }

    public Lottery5Setting getLottery5Setting() {
        String lottery5Setting1Str = RedisCache.getInstance().get("Lottery5Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery5Setting1Str)) {
            Lottery5Setting lottery5Setting = lottery5SettingMapper.selectByRoomId(roomId);
            lottery5Setting1Str = gson.toJson(lottery5Setting);
            RedisCache.getInstance().set("Lottery5Setting", lottery5Setting1Str,expireTime);
            return lottery5Setting;
        }
        return gson.fromJson(lottery5Setting1Str, Lottery5Setting.class);
    }

    public Lottery6Setting getLottery6Setting() {
        String lottery6Setting1Str = RedisCache.getInstance().get("Lottery6Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery6Setting1Str)) {
            Lottery6Setting lottery6Setting = lottery6SettingMapper.selectByRoomId(roomId);
            lottery6Setting1Str = gson.toJson(lottery6Setting);
            RedisCache.getInstance().set("Lottery6Setting", lottery6Setting1Str,expireTime);
            return lottery6Setting;
        }
        return gson.fromJson(lottery6Setting1Str, Lottery6Setting.class);
    }

    public Lottery7Setting getLottery7Setting() {
        String lottery7Setting1Str = RedisCache.getInstance().get("Lottery7Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery7Setting1Str)) {
            Lottery7Setting lottery7Setting = lottery7SettingMapper.selectByRoomId(roomId);
            lottery7Setting1Str = gson.toJson(lottery7Setting);
            RedisCache.getInstance().set("Lottery7Setting", lottery7Setting1Str,expireTime);
            return lottery7Setting;
        }
        return gson.fromJson(lottery7Setting1Str, Lottery7Setting.class);
    }

    public Lottery8Setting getLottery8Setting() {
        String lottery8Setting1Str = RedisCache.getInstance().get("Lottery8Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery8Setting1Str)) {
            Lottery8Setting lottery8Setting = lottery8SettingMapper.selectByRoomId(roomId);
            lottery8Setting1Str = gson.toJson(lottery8Setting);
            RedisCache.getInstance().set("Lottery8Setting", lottery8Setting1Str,expireTime);
            return lottery8Setting;
        }
        return gson.fromJson(lottery8Setting1Str, Lottery8Setting.class);
    }

    public Lottery9Setting getLottery9Setting() {
        String lottery9Setting1Str = RedisCache.getInstance().get("Lottery9Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery9Setting1Str)) {
            Lottery9Setting lottery9Setting = lottery9SettingMapper.selectByRoomId(roomId);
            lottery9Setting1Str = gson.toJson(lottery9Setting);
            RedisCache.getInstance().set("Lottery9Setting", lottery9Setting1Str,expireTime);
            return lottery9Setting;
        }
        return gson.fromJson(lottery9Setting1Str, Lottery9Setting.class);
    }

    public Lottery10Setting getLottery10Setting() {
        String lottery10Setting1Str = RedisCache.getInstance().get("Lottery10Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery10Setting1Str)) {
            Lottery10Setting lottery10Setting = lottery10SettingMapper.selectByRoomId(roomId);
            lottery10Setting1Str = gson.toJson(lottery10Setting);
            RedisCache.getInstance().set("Lottery10Setting", lottery10Setting1Str,expireTime);
            return lottery10Setting;
        }
        return gson.fromJson(lottery10Setting1Str, Lottery10Setting.class);
    }

    public Lottery11Setting getLottery11Setting() {
        String lottery11Setting1Str = RedisCache.getInstance().get("Lottery11Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery11Setting1Str)) {
            Lottery11Setting lottery11Setting = lottery11SettingMapper.selectByRoomId(roomId);
            lottery11Setting1Str = gson.toJson(lottery11Setting);
            RedisCache.getInstance().set("Lottery11Setting", lottery11Setting1Str,expireTime);
            return lottery11Setting;
        }
        return gson.fromJson(lottery11Setting1Str, Lottery11Setting.class);
    }

    public Lottery12Setting getLottery12Setting() {
        String lottery12Setting1Str = RedisCache.getInstance().get("Lottery12Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery12Setting1Str)) {
            Lottery12Setting lottery12Setting = lottery12SettingMapper.selectByRoomId(roomId);
            lottery12Setting1Str = gson.toJson(lottery12Setting);
            RedisCache.getInstance().set("Lottery12Setting", lottery12Setting1Str,expireTime);
            return lottery12Setting;
        }
        return gson.fromJson(lottery12Setting1Str, Lottery12Setting.class);
    }

    public Lottery13Setting getLottery13Setting() {
        String lottery13Setting1Str = RedisCache.getInstance().get("Lottery13Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery13Setting1Str)) {
            Lottery13Setting lottery13Setting = lottery13SettingMapper.selectByRoomId(roomId);
            lottery13Setting1Str = gson.toJson(lottery13Setting);
            RedisCache.getInstance().set("Lottery13Setting", lottery13Setting1Str,expireTime);
            return lottery13Setting;
        }
        return gson.fromJson(lottery13Setting1Str, Lottery13Setting.class);
    }

    public Lottery14Setting getLottery14Setting() {
        String lottery14Setting1Str = RedisCache.getInstance().get("Lottery14Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery14Setting1Str)) {
            Lottery14Setting lottery14Setting = lottery14SettingMapper.selectByRoomId(roomId);
            lottery14Setting1Str = gson.toJson(lottery14Setting);
            RedisCache.getInstance().set("Lottery14Setting", lottery14Setting1Str,expireTime);
            return lottery14Setting;
        }
        return gson.fromJson(lottery14Setting1Str, Lottery14Setting.class);
    }

    public Lottery15Setting getLottery15Setting() {
        String lottery15Setting1Str = RedisCache.getInstance().get("Lottery15Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery15Setting1Str)) {
            Lottery15Setting lottery15Setting = lottery15SettingMapper.selectByRoomId(roomId);
            lottery15Setting1Str = gson.toJson(lottery15Setting);
            RedisCache.getInstance().set("Lottery15Setting", lottery15Setting1Str,expireTime);
            return lottery15Setting;
        }
        return gson.fromJson(lottery15Setting1Str, Lottery15Setting.class);
    }

    public Lottery16Setting getLottery16Setting() {
        String lottery16Setting1Str = RedisCache.getInstance().get("Lottery16Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery16Setting1Str)) {
            Lottery16Setting lottery16Setting = lottery16SettingMapper.selectByRoomId(roomId);
            lottery16Setting1Str = gson.toJson(lottery16Setting);
            RedisCache.getInstance().set("Lottery16Setting", lottery16Setting1Str,expireTime);
            return lottery16Setting;
        }
        return gson.fromJson(lottery16Setting1Str, Lottery16Setting.class);
    }

    public Lottery17Setting getLottery17Setting() {
        String lottery17Setting1Str = RedisCache.getInstance().get("Lottery17Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery17Setting1Str)) {
            Lottery17Setting lottery17Setting = lottery17SettingMapper.selectByRoomId(roomId);
            lottery17Setting1Str = gson.toJson(lottery17Setting);
            RedisCache.getInstance().set("Lottery17Setting", lottery17Setting1Str,expireTime);
            return lottery17Setting;
        }
        return gson.fromJson(lottery17Setting1Str, Lottery17Setting.class);
    }

    public Lottery18Setting getLottery18Setting() {
        String lottery18Setting1Str = RedisCache.getInstance().get("Lottery18Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery18Setting1Str)) {
            Lottery18Setting lottery18Setting = lottery18SettingMapper.selectByRoomId(roomId);
            lottery18Setting1Str = gson.toJson(lottery18Setting);
            RedisCache.getInstance().set("Lottery18Setting", lottery18Setting1Str,expireTime);
            return lottery18Setting;
        }
        return gson.fromJson(lottery18Setting1Str, Lottery18Setting.class);
    }

    public Lottery19Setting getLottery19Setting() {
        String lottery19Setting1Str = RedisCache.getInstance().get("Lottery19Setting");
        if (Strings.isEmptyOrNullAmongOf(lottery19Setting1Str)) {
            Lottery19Setting lottery19Setting = lottery19SettingMapper.selectByRoomId(roomId);
            lottery19Setting1Str = gson.toJson(lottery19Setting);
            RedisCache.getInstance().set("Lottery19Setting", lottery19Setting1Str,expireTime);
            return lottery19Setting;
        }
        return gson.fromJson(lottery19Setting1Str, Lottery19Setting.class);
    }
}
