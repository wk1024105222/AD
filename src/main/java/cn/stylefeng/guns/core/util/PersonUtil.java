package cn.stylefeng.guns.core.util;

import cn.stylefeng.guns.modular.schedule.ScheduledTasks;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class PersonUtil {
    private static final Logger logger = LoggerFactory.getLogger(PersonUtil.class);

    public static Map<String, Object> beanToMap(Object obj) {
        Map<String, Object> params = new HashMap<String, Object>(0);
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!"class".equals(name)) {
                    params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     * 获取一个文件的md5值(可处理大文件)
     * @return md5 value
     */
    public static String getMD5(File file) {
        BigInteger bigInt = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            bigInt = new BigInteger(1, md.digest());

//            System.out.println("文件md5值：" + bigInt.toString(16));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bigInt.toString(16);
    }


    public static String sendMsg(String request) {
        Socket socket = null;
        OutputStream os = null;
        PrintWriter pw = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;
        try {
            socket = new Socket("localhost", 4000);

            os = socket.getOutputStream();
            pw = new PrintWriter(os);

            pw.write(request);
            pw.flush();
            PersonUtil.logger.info("请求报文"+request);
            socket.shutdownOutput();

            is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String resMsg = null;
            while ((resMsg = br.readLine()) != null) {
                PersonUtil.logger.info("接收报文"+resMsg);
                result =  resMsg;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                is.close();
                pw.close();
                os.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static boolean sendMail(JavaMailSender mailSender, String from, String[] to, String subject, String text, File file, String fileName  ) {
        MimeMessage message=mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);

            helper.setSubject(subject);
            helper.setText(text);
            FileSystemResource fsr=new FileSystemResource(file);
            helper.addAttachment(fileName,fsr);
            mailSender.send(message);
            PersonUtil.logger.info("带附件的邮件发送成功");

        }catch (Exception e){
            e.printStackTrace();
            PersonUtil.logger.info("发送带附件的邮件失败");
            return false;
        }
        return true;
    }
}
