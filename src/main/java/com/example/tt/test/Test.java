package com.example.tt.test;

import com.example.tt.Bean.QXCOrder;
import com.example.tt.OpenResult.AnyChooseCalWin;
import com.example.tt.utils.*;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Test {

    public final static int[] zu3={1,2,3,3,3,3,4,5,4,5,5,4,5,5,4,5,5,4,5,4,3,3,3,1,2,1};
    public final static int[] zu6={1,1,2,3,4,5,7,8,9,10,10,10,10,9,8,7,5,4,3,2,1,1};

    public static Stack<Character> stack = new Stack<Character>();
    public static Stack<Integer> stackIndex = new Stack<Integer>();

    static int win = 0;
    static int count=0;
    static protected LinkedHashMap<Integer, Integer> coordinateIndexMap = new LinkedHashMap<>();
    static ScheduledExecutorService threadPool = Executors
            .newScheduledThreadPool(3);

    public static void main(String[] args) {
        String kai = "12345";
        String bet = "1924530";
        int type = 3;// 选 2
//        System.err.println(removeSameNum(kai));
        System.err.println("方式数--》" + calculateOrderAnyChoose(removeSameNum(kai).length(), type));
//
//        System.err.println("WIN--->" + AnyChooseCalWin.getInstance().getWinTimes(kai, bet, type));

//        JSONObject jsonObject=new JSONObject();
//        try {
//            jsonObject.put("gamName","ry3");
//            jsonObject.put("orderPrice",6);
//            JSONArray jsonArray=new JSONArray();
//            jsonArray.put(makeJsonObj("0","453156"));
//            jsonArray.put(makeJsonObj("4","4342"));
//            jsonObject.put("code",jsonArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        System.err.println(jsonObject.toString());
        //豹子 10/220 顺子8/220 对子
//        cal("12345","123",3,0,0);
//        StringBuilder sbContent=new StringBuilder();
//        sbContent.append("1   ").append('4').append(116);
//        System.err.println(sbContent);
//        int period=120;
//        int startQihao=23343;
//        long qishiTime=1702781820000l;
//        System.err.println(TimeUtils.convertToDetailTime(qishiTime));
//        System.err.println(AnyChooseCalWin.getInstance().getWinTimes("2103","213",3));
//        for (int i = 0; i < 1000; i++) {
//            coordinateIndexMap.put(i,i%2);
//        }
//
//
//        threadPool.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                synchronized (coordinateIndexMap)
//                {
////                    Integer yy=coordinateIndexMap.get(1);
////                    yy++;
////                    coordinateIndexMap.put(1,yy);
////                    coordinateIndexMap.remove(1000-yy);
////                    coordinateIndexMap.keySet();
//                }
//                coordinateIndexMap.put(coordinateIndexMap.size()+1,0);
//
//            }
//        },0,200, TimeUnit.MILLISECONDS);
//
//        for (int i = 0; i < 1000; i++) {
//                LinkedHashMap<Integer, Integer> map=(LinkedHashMap<Integer, Integer>)coordinateIndexMap.clone();
//                map.keySet();
//        }

//        cal("00145".toCharArray(),3,0,0);
       System.err.println(GameIndex.PL5GameTypeCode.values());
        Map<Integer,String[]> map =new HashMap<>();
        map.put(0,new String[]{"0","2"});
        map.put(1,new String[]{"1","2"});
        map.put(2,new String[]{"0","3"});
        map.put(3,new String[]{"0","3"});
        map.put(4,new String[]{"1","2"});

       System.err.println("xcxxx-->"+calDXDS("01311",map,0,1,2,3,4));
    }

    public static int calDXDS(String openResult, Map<Integer,String[]> map,Integer...positions)
    {
        int win=0;
        for (int i = 0; i < positions.length; i++) {
            String codes[]=map.get(positions[i]);
            if(Strings.isEmptyOrNullAmongOf(codes))
            {
                return 0;
            }
            else
            {
                int result=openResult.charAt(positions[i])-'0';
                for (int j = 0; j < codes.length; j++) {
                    int bet=codes[j].charAt(0)-'0';
                    switch (bet)
                    {
                        case 0:
                            if(result>4)
                            {
                                win++;
                            }
                            break;
                        case 1:
                            if(result<5)
                            {
                                win++;
                            }
                            break;
                        case 2:
                            if(result%2!=0)
                            {
                                win++;
                            }
                            break;
                        case 3:
                            if(result%2==0)
                            {
                                win++;
                            }
                            break;
                    }
                }

            }
        }

        return win;
    }

    private static void test()
    {
        String[] betNums={"1","0","5"};
        String openResult="519";
        int resultCodeSum=openResult.charAt(0)+openResult.charAt(1)+openResult.charAt(2)-'0'-'0'-'0';
        int count=0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < betNums.length; j++) {
                if(betNums[j].charAt(0)==openResult.charAt(i))
                {
                    count++;
                }
            }
        }
        System.err.println("count--->"+count);
        System.err.println("sum--->"+resultCodeSum);
    }

    public static String syncBull(Integer[] cards) {
        // 计算总点数
        int sums = 0;
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] > 10) {
                cards[i] = 10;
            }
            sums += cards[i];
        }
        //牛牛
        if (sums % 10 == 0) {
            return "牛牛";
        }
        // 牛丁 ~ 牛九
        int bull = 0;
        for (int i = 0; i < cards.length - 2; i++) {
            int cardI = cards[i];
            for (int j = i + 1; j < cards.length; j++) {
                int cardJ = cards[j];
                for (int k = j + 1; k < cards.length; k++) {
                    int cardK = cards[k];
                    int total = cardI + cardJ + cardK;
                    if (total % 10 == 0) {
                        int n = (sums - total) % 10;
                        bull = bull < n ? n : bull;
                    }
                }
            }
        }
        if (bull == 0) {
            return "无牛";
        } else {
            return "牛" + bull ;
        }
    }

    private static void f(char[] shu, int targ, int cur) {
        // TODO Auto-generated method stub
        if(cur == targ) {
            System.out.println(stack);
            return;
        }

        for(int i=0;i<shu.length;i++) {
            stack.add(shu[i]);
            f(shu, targ, cur+1);
            stack.pop();
        }
    }

    private static void doPostOrGet(String pathUrl) {
        OutputStreamWriter out = null;
        BufferedReader br = null;
        String result = "";
        try {
            URL url = new URL(pathUrl);
            // 打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 请求方式
            // conn.setRequestMethod("POST");
            conn.setRequestMethod("GET");

            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type",
                    "application/json;charset=utf-8");

            // DoOutput设置是否向httpUrlConnection输出，DoInput设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);

            /**
             * 下面的三句代码，就是调用第三方http接口
             */
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数即数据
            // out.write(data);
            // flush输出流的缓冲
            out.flush();

            /**
             * 下面的代码相当于，获取调用第三方http接口后返回的结果
             */
            // 获取URLConnection对象对应的输入流
            InputStream is = conn.getInputStream();
            // 构造一个字符流缓存
            br = new BufferedReader(new InputStreamReader(is));
            String str = "";
            while ((str = br.readLine()) != null) {
                result += str + "\n";
            }
//			MyLog.e(pathUrl);
//			System.out.println(result);
            MyLog.e( result);
            // 关闭流
            is.close();
            // 断开连接，disconnect是在底层tcp socket链接空闲时才切断，如果正在被其他线程使用就不切断。
            conn.disconnect();

        } catch (Exception e) {
            MyLog.e(pathUrl+"--->"+e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static JSONObject makeJsonObj(String pos,String codes)
    {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("pos",pos);
            jsonObject.put("codes",codes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     *
     * @param shu   元素
     * @param targ  要选多少个元素
     * @param has   当前有多少个元素
     * @param cur   当前选到的下标
     * 1    2   3     //开始下标到2
     * 1    2   4     //然后从3开始
     */
    private static void cal(String shu, String bet, int targ, int has, int cur) {
        if(has == targ) {
            System.err.println(stack);
            count++;
            for (int i = 0; i < stack.size(); i++) {
                boolean hasChar= Strings.hasContainsChar(bet,stack.get(i));
                if(hasChar)
                {
                    if(i==stack.size()-1)
                    {
                        win++;
                    }
                }
                else
                {
                    break;
                }
            }
            return;
        }

        for(int i=cur;i<shu.length();i++) {
            if(!stack.contains(shu.charAt(i))) {
                stack.add(shu.charAt(i));
                cal(shu, bet,targ, has+1, i);
                stack.pop();
            }
        }

    }

    private static String removeSameNum(String num) {
        StringBuilder sb = new StringBuilder();
        int pos = 0;
        for (int i = 0; i < num.length(); i++) {
            boolean isSame = false;
            for (int j = i + 1; j < num.length(); j++) {
                if (num.charAt(i) == num.charAt(j)) {
                    isSame = true;
                    break;
                }
            }
            if (!isSame) {
                sb.append(num.charAt(i));
            }
        }
        return sb.toString();
    }

    private static boolean hasContainChar(String target, char arg) {
        boolean hasInKai = false;
        for (int j = 0; j < target.length(); j++) {
            if (target.charAt(j) == arg) {
                hasInKai = true;
            }
        }
        return hasInKai;
    }

    //choose 用户选中 当前彩种任选几
    private static int calculateOrderAnyChoose(int choose, int need) {
        int n = 1;
        int nm = 1;
        int m = 1;

        for (int i = 0; i < choose; i++) {
            n = n * (i + 1);
            if (choose - need - i > 0) {
                nm = nm * (choose - need - i);
            }
            if (need - i > 0) {
                m = m * (need - i);
            }
        }
        return n / nm / m;
    }
}
