package com.example.tt.utils;

import com.example.tt.Bean.RequestBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FetchLotteryResultTask {

    static ScheduledExecutorService threadPool = Executors
            .newScheduledThreadPool(3);
    static String pathRequest, pathLog, pathDomainsJson, pathDomainsJsonLog;
    static int secNow = 0;
    static List<RequestBean> list;
    static List<RequestBean> domainList;
    static int period = 600;//检测域名间隔
    static JSONArray jsonDomains = new JSONArray();
    static String checkDomainUrl;
    public static boolean isOpen = true;
	public static boolean isInit = false;


    //https://api.kit9.cn/api/wechat_security/api.php?url=http://baidu.com

    public static Object startProcess(String path, boolean mOpen) {

        Map map=new HashMap();


        File mainFile = new File(path);
        if (!mainFile.exists()) {
            map.put("status", FetchLotteryResultTask.isOpen && FetchLotteryResultTask.isInit ? 1:0);
            return ReturnDataBuilder.error(-999, "路径不存在",map);
        }

        FetchLotteryResultTask.isOpen = mOpen;

        if (isInit) {
            map.put("status", FetchLotteryResultTask.isOpen && FetchLotteryResultTask.isInit ? 1:0);
            return ReturnDataBuilder.makeBaseJSON(map);
        }


        pathRequest = path + File.separator + "urls.json";
        pathDomainsJson = path + File.separator + "domains.json";
        pathLog = path + File.separator + "UrlsRequest.log";
        pathDomainsJsonLog = path + File.separator + "AvailableDomainsJson.json";
        MyLog.e("开始拉取" + pathRequest + " 日志 路径：" + pathLog);

        String text = openFile(pathRequest);
        if (text != null && text.length() > 0) {
            try {
                JSONArray jsonArray = new JSONArray(text);
                if (jsonArray != null && jsonArray.length() > 0) {
                    list = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        RequestBean bean = new RequestBean();
                        bean.setSec(jsonObject.optInt("sec", 2));
                        bean.setUrl(jsonObject.optString("url", ""));
                        list.add(bean);
                    }

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        String textDomains = openFile(pathDomainsJson);
        if (textDomains != null && textDomains.length() > 0) {
            try {
                JSONObject data = new JSONObject(textDomains);
                JSONArray jsonArray = data.optJSONArray("domains");
                period = data.optInt("gap", 600);
                checkDomainUrl = data.optString("checkDomainUrl", "");
                if (data.optString("AvailableDomainsDirectory", "").length() > 0) {
                    pathDomainsJsonLog = data.optString("AvailableDomainsDirectory", "") + File.separator + "AvailableDomainsJson.json";
                    ;
                }

                if (jsonArray != null && jsonArray.length() > 0) {
                    domainList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        RequestBean bean = new RequestBean();
                        bean.setUrl(jsonArray.optString(i, ""));
                        domainList.add(bean);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        threadPool.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (secNow % list.get(i).getSec() == 0) {
                            if (isOpen) {
                                doPostOrGet(list.get(i).getUrl());
                            }
                        }
                    }
                }


                if (period == 0) {
                    period = 600;
                }

                if (secNow % period == 0 && checkDomainUrl != null && checkDomainUrl.toLowerCase().startsWith("http")) {
                    if (domainList != null && domainList.size() > 0) {
                        jsonDomains.clear();
                        for (int i = 0; i < domainList.size(); i++) {
                            if (isOpen) {
                                doamainTest(domainList.get(i).getUrl());
                            }
                        }
                    }
                }

                secNow++;
            }
        }, 0L, 1L, TimeUnit.SECONDS);

        isInit = true;
        map.put("status", FetchLotteryResultTask.isOpen && FetchLotteryResultTask.isInit ? 1:0);
        return ReturnDataBuilder.makeBaseJSON(map);
    }

    private static long getTime(int mYear, int mMonth, int mDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.MONTH, mMonth - 1);
        calendar.set(Calendar.DATE, mDate);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTime().getTime();
    }

	private static void doPostOrGet(String pathUrl) {
        writeFile(pathLog, "请求：--->" + secNow + "---->" + pathUrl + "\r\n", true);
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
            writeFile(pathLog, result, true);
            // 关闭流
            is.close();
            // 断开连接，disconnect是在底层tcp socket链接空闲时才切断，如果正在被其他线程使用就不切断。
            conn.disconnect();

        } catch (Exception e) {
            writeFile(pathLog, pathUrl+"--->"+e.getMessage(), true);
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


	private static void doamainTest(String pathUrl) {

        OutputStreamWriter out = null;
        BufferedReader br = null;
        String result = "";
        try {
            URL url = new URL(checkDomainUrl + pathUrl);
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
            conn.setUseCaches(false);
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

            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject != null) {
                JSONObject dataJson = new JSONObject();
                dataJson.put("url", pathUrl);
                dataJson.put("state", jsonObject.optInt("code", 0));
                jsonDomains.put(dataJson);
                writeFile(pathDomainsJsonLog, jsonDomains.toString(), false);

            }
            // 关闭流
            is.close();
            // 断开连接，disconnect是在底层tcp socket链接空闲时才切断，如果正在被其他线程使用就不切断。
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
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

	private static String openFile(String path) {
        MyLog.e("打开文件：" + path);
        StringBuilder sb = new StringBuilder();
        try {

            InputStream is = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = "";

            while ((line = br.readLine()) != null) {
                MyLog.e(line);
                sb.append(line);
            }
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

	private static void writeFile(String path, String Content, boolean append) {
        File file = new File(path);
        if (file != null && file.exists() && file.length() > 1024 * 1024) {
            file.delete();
        }

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(path, append);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    fileOutputStream, "UTF-8");
            outputStreamWriter.write(Content);
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
