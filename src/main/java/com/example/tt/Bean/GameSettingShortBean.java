package com.example.tt.Bean;

public class GameSettingShortBean {

    String gameOpen;
    Integer fengTime;

    public String getGameOpen() {
        return gameOpen;
    }

    public void setGameOpen(String gameOpen) {
        this.gameOpen = gameOpen;
    }

    public Integer getFengTime() {
        return fengTime;
    }

    public void setFengTime(Integer fengTime) {
        this.fengTime = fengTime;
    }

    public static GameSettingShortBean beanConverter(Object object){
        GameSettingShortBean gameSettingShortBean=new GameSettingShortBean();

        if(object instanceof Lottery1Setting)
        {
            Lottery1Setting lottery1Setting=(Lottery1Setting)object;
            gameSettingShortBean.setFengTime(lottery1Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery1Setting.getGameopen());
        }
        else if(object instanceof Lottery2Setting)
        {
            Lottery2Setting lottery2Setting=(Lottery2Setting)object;
            gameSettingShortBean.setFengTime(lottery2Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery2Setting.getGameopen());
        }
        else if(object instanceof Lottery3Setting)
        {
            Lottery3Setting lottery3Setting=(Lottery3Setting)object;
            gameSettingShortBean.setFengTime(lottery3Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery3Setting.getGameopen());
        }
        else if(object instanceof Lottery4Setting)
        {
            Lottery4Setting lottery4Setting=(Lottery4Setting)object;
            gameSettingShortBean.setFengTime(lottery4Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery4Setting.getGameopen());
        }
        else if(object instanceof Lottery5Setting)
        {
            Lottery5Setting lottery5Setting=(Lottery5Setting)object;
            gameSettingShortBean.setFengTime(lottery5Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery5Setting.getGameopen());
        }
        else if(object instanceof Lottery6Setting)
        {
            Lottery6Setting lottery6Setting=(Lottery6Setting)object;
            gameSettingShortBean.setFengTime(lottery6Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery6Setting.getGameopen());
        }
        else if(object instanceof Lottery7Setting)
        {
            Lottery7Setting lottery7Setting=(Lottery7Setting)object;
            gameSettingShortBean.setFengTime(lottery7Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery7Setting.getGameopen());
        }
        else if(object instanceof Lottery8Setting)
        {
            Lottery8Setting lottery8Setting=(Lottery8Setting)object;
            gameSettingShortBean.setFengTime(lottery8Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery8Setting.getGameopen());
        }
        else if(object instanceof Lottery9Setting)
        {
            Lottery9Setting lottery9Setting=(Lottery9Setting)object;
            gameSettingShortBean.setFengTime(lottery9Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery9Setting.getGameopen());
        }
        else if(object instanceof Lottery10Setting)
        {
            Lottery10Setting lottery10Setting=(Lottery10Setting)object;
            gameSettingShortBean.setFengTime(lottery10Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery10Setting.getGameopen());
        }
        else if(object instanceof Lottery11Setting)
        {
            Lottery11Setting lottery11Setting=(Lottery11Setting)object;
            gameSettingShortBean.setFengTime(lottery11Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery11Setting.getGameopen());
        }
        else if(object instanceof Lottery12Setting)
        {
            Lottery12Setting lottery12Setting=(Lottery12Setting)object;
            gameSettingShortBean.setFengTime(lottery12Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery12Setting.getGameopen());
        }
        else if(object instanceof Lottery13Setting)
        {
            Lottery13Setting lottery13Setting=(Lottery13Setting)object;
            gameSettingShortBean.setFengTime(lottery13Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery13Setting.getGameopen());
        }
        else if(object instanceof Lottery14Setting)
        {
            Lottery14Setting lottery14Setting=(Lottery14Setting)object;
            gameSettingShortBean.setFengTime(lottery14Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery14Setting.getGameopen());
        }
        else if(object instanceof Lottery15Setting)
        {
            Lottery15Setting lottery15Setting=(Lottery15Setting)object;
            gameSettingShortBean.setFengTime(lottery15Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery15Setting.getGameopen());
        }
        else if(object instanceof Lottery16Setting)
        {
            Lottery16Setting lottery16Setting=(Lottery16Setting)object;
            gameSettingShortBean.setFengTime(lottery16Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery16Setting.getGameopen());
        }
        else if(object instanceof Lottery17Setting)
        {
            Lottery17Setting lottery17Setting=(Lottery17Setting)object;
            gameSettingShortBean.setFengTime(lottery17Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery17Setting.getGameopen());
        }
        else if(object instanceof Lottery18Setting)
        {
            Lottery18Setting lottery18Setting=(Lottery18Setting)object;
            gameSettingShortBean.setFengTime(lottery18Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery18Setting.getGameopen());
        }
        else if(object instanceof Lottery19Setting)
        {
            Lottery19Setting lottery19Setting=(Lottery19Setting)object;
            gameSettingShortBean.setFengTime(lottery19Setting.getFengtime());
            gameSettingShortBean.setGameOpen(lottery19Setting.getGameopen());
        }

        return gameSettingShortBean;
    }

}
