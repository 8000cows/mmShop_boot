package com.utils;

import com.entity.Admin;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class SecurityUtil {
    // 处理编码与解码
    private final static Base64.Decoder decoder = Base64.getDecoder();
    private final static Base64.Encoder encoder = Base64.getEncoder();

    public static String createToken(Admin admin) throws IOException {
        SimpleDateFormat sif = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        //当前时间加 5分钟 * 6 ，作为Token的有效时间
        Date dateAfter = new Date(date.getTime() + 300000 * 6);
        String endTime = sif.format(dateAfter);

        ObjectMapper om = new ObjectMapper();
        // 拼接一个字符串对象( 伪json格式)
        String jsonStr = "\"id:" + admin.getaId() +",admin:"+ admin.getaName() + ",endTime:" + endTime+"\"";
        String json = om.readTree(jsonStr).toString();
        String token = json + ";" + MD5Util.getMD5(json);

        // 将token转换成base64
        return encoder.encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }

    public static int parseToken(HttpServletRequest req) throws Exception{
        // 进行非null判断，增加程序健壮性
        if(req.getHeader("token")!=null){
            String str = new String(decoder.decode(req.getHeader("token")), StandardCharsets.UTF_8);
            //String str = new String(decoder.decode(req.getParameter("token")), StandardCharsets.UTF_8);
            //将token切割成两部分
            //str:"id:1,admin:xxx,endTime:2022-03-18 19:18:43";fb675266364f697519dac6d1e6ec1da3
            String ahead = str.substring(0, str.indexOf(";"));
            String behind = str.substring(str.indexOf(";") + 1, str.length());

            //时间从字符串中截取出来
            String timeAttr = ahead.substring(ahead.indexOf("e")+1,ahead.length()-1);
            String deadTime = timeAttr.substring(timeAttr.indexOf(":") + 1);

            //将字符串的时间转为long类型的数据进行比较
            SimpleDateFormat sif = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date before = sif.parse(deadTime);

            long beforeTime = before.getTime();
            long nowTime = new Date().getTime();

            long diff = beforeTime - nowTime;

            //解析当前token对象的id，与管理员的名字
            int id = Integer.parseInt(ahead.substring(ahead.indexOf("i") + 3, ahead.indexOf(",")));
            String name = ahead.substring(ahead.indexOf("n") + 2, ahead.indexOf("e")-1);
            System.out.println("name: "+name);

            // 将前端传来的token与自己的加密规则进行比较
            if (behind.equals(MD5Util.getMD5(ahead))) {
                if (diff < 0) {
                    // Token有效时间已过，请重新登录!
                    return 1;
                } else {
                    //outer.println("前端请求的业务处理结果，后端已送到，请接受!");
                    //outer.println("Token:"+createToken(new Admin(id,name)));
                    // 前端请求的业务处理结果，后端已送到，请接受!
                    req.setAttribute("token",createToken(new Admin(id,name)));
                    return 2;
                }
            } else {
                // Token信息有误!
                return 3;
            }
        }
        // 当返回值为0时，说明请求头中没有token
        return 0;

    }

}
