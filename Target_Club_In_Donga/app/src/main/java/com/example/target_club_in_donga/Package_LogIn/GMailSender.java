package com.example.target_club_in_donga.Package_LogIn;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GMailSender extends javax.mail.Authenticator {
    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private Session session;

    // 이메일 인증번호 (1)
    private String emailCode;

    static {
        Security.addProvider(new com.example.target_club_in_donga.Package_LogIn.JSSEProvider());
    }

    public GMailSender(String user, String password) {
        this.user = user;
        this.password = password;

        /*
        // 이메일 인증번호 (2)
        emailCode = createEmailCode();
        */

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        // 구글에서 지원하는 smtp 정보를 받아와 MimeMessage 객체에 전달해준다.
        session = Session.getDefaultInstance(props, this);
    }

    /*
        // 로컬 변수 대체 방지? 이 구분이 없으면 "private String emailCode;" 부분이 경고뜸
        public String getEmailCode() {
            return emailCode;
        } //생성된 이메일 인증코드 반환

        private String createEmailCode() { //이메일 인증코드 생성
            String[] str = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
                    "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
            StringBuilder newCode = new StringBuilder();

            for (int x = 0; x < 8; x++) {
                int random = (int) (Math.random() * str.length);
                newCode.append(str[random]);
            }

            return newCode.toString();
        }
    */
    protected PasswordAuthentication getPasswordAuthentication() {
        //해당 메서드에서 사용자의 계정(id & password)을 받아 인증받으며 인증 실패시 기본값으로 반환됨.
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, String body, String sender, String recipients) throws Exception {
//        try {
            MimeMessage message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain")); // 본문 내용을 byte 단위로 쪼개어 전달
            message.setSender(new InternetAddress(sender)); // 보내는 이의 이메일 설정
            message.setSubject(subject);
            message.setDataHandler(handler);
            if (recipients.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            Transport.send(message);    // 메시지 전달
            Log.v("develop_check","이메일 전송에 성공했습니다.");
//        } catch (Exception e) {
//            Log.e("develop_check", "이메일 전송에 실패했습니다. error_code_send_emailCode");
//            // Toast.makeText(this,"이메일 전송에 실패했습니다.",Toast.LENGTH_SHORT).show(); 에러 뜬다!
//        }
    }

    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }
}  