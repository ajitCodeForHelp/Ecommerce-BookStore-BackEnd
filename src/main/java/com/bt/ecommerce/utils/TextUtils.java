package com.bt.ecommerce.utils;

import com.bt.ecommerce.primary.repository.SettingRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class TextUtils {

    @Autowired
    private SettingRepository settingRepository;

    public static String generate4DigitOTP() {
        if (Const.SystemSetting.TestMode) {
            return "9999";
        }
        Random rnd = new Random();
        int number = rnd.nextInt(9999);
        return String.format("%04d", number);
    }

    public static boolean isEmpty(String string) {
        if (null == string) return true;
        return string.length() == 0;
    }

    public static boolean isEmpty(Long value) {
        if (null == value) return true;
        return value <= 0;
    }

    public static boolean isEmpty(Integer value) {
        if (null == value) return true;
        return value <= 0;
    }

    public static boolean isEmpty(Double value) {
        if (null == value) return true;
        return value <= 0;
    }

    public static boolean isEmpty(List list) {
        return (list == null || list.isEmpty());
    }

    public static Long getValidValue(Long oldValue, Long newValue) {
        return TextUtils.isEmpty(newValue) ? oldValue : newValue;
    }

    public static Double getValidValue(Double oldValue, Double newValue) {
        return TextUtils.isEmpty(newValue) ? oldValue : newValue;
    }

    public static String getValidValue(String oldValue, String newValue) {
        return TextUtils.isEmpty(newValue) ? oldValue : newValue;
    }

    public static LocalDate getValidValue(Long newValue, LocalDate oldValue) {
        if (TextUtils.isEmpty(newValue)) {
            return oldValue;
        }
        return DateUtils.getLocalDate(newValue);
    }

    public static boolean isNumeric(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static Long currentSessionId = 0L;

    public static void resetCurrentSessionId() {
        currentSessionId = 0L;
    }

    public static String formatMoneyAmount(double moneyAmount) {
        DecimalFormat format = new DecimalFormat("##.00");
        return format.format(moneyAmount);
    }

    public static String getRandomKeyString(int length) {
        // String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ-abcdefghijklmnopqrstuvwxyz_0123456789";
        String alphaNumeric = "ABCDEFGHJKMNOPQRSTUVWXYZ";
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int random = (int) ((Math.random() * 1000) % alphaNumeric.length());
            buffer.append(alphaNumeric.charAt(random));
        }
        String generatedString = buffer.toString();
        return generatedString;
    }

    public static String getEncodedPassword(String password) {
        if (isEmpty(password)) {
            return null;
        }
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public static boolean matchPassword(String rawPassword, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }

    public static String md5encryption(String text) {
        if (isEmpty(text)) {
            return null;
        }
        String hashtext = null;
        try {
            String plaintext = text;
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(plaintext.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
        } catch (Exception e1) {
        }
        return hashtext;
    }

    public static String get10CharRandomCode() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString.toUpperCase();
    }

    public static String getOrderReferenceId(Long orderId) {
        return "ORD-" + TextUtils.convertNumberTo10Digit(orderId);
    }

    public static String convertNumberTo10Digit(Long number) {
        return String.format("%010d", number);
    }

    public static String get10DigitTransactionId(Long number) {
        return "TXN-" + convertNumberTo10Digit(number);
    }

    public static String getPaymentRequestTransactionId(Long number) {
        if (Const.SystemSetting.TestMode)
            return "TEST-TXN-" + convertNumberTo10Digit(number);
        else {
            return "TXNL-" + convertNumberTo10Digit(number);
        }
    }

    public static String getListToStringCSV(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        StringBuffer csv = new StringBuffer();
        for (String image : list) {
            if (csv.length() > 0) {
                csv.append(",");
            }
            csv.append(image);
        }
        return csv.toString();
    }

    public static List<String> getStringCSVToList(String stringCSV) {
        if (isEmpty(stringCSV)) {
            return null;
        }
        return new ArrayList<>(Arrays.asList(stringCSV.split(",")));
    }

    public static String removeSpecialCharacterFromString(String str) {
        return str.replaceAll("[^a-zA-Z0-9]", " ");
    }

    public static String getUniqueKey() {
        return UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString();
    }

    public static String getJsonString(Object obj) {
        return new Gson().toJson(obj);
    }

    public static String generate6DigitRandomNumber() {
        if (true) {
            return "000000";
        }
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    public static BigDecimal get2DecimalPlaceValue(double value) {
        return new BigDecimal(String.format("%.2f", value));
    }

    public static String getCombinedIdsByDash(List<Long> ids) {
        // Sort If Required.
        ArrayList<Long> idList = new ArrayList<>(ids);
        Collections.sort(idList);
        StringBuffer combinedKey = new StringBuffer("");
        for (Long id : idList) {
            if (combinedKey.length() > 0) {
                combinedKey.append("-");
            }
            combinedKey.append(id);
        }
        return combinedKey.toString();
    }

    public static String validateTitle(String title) {
        if (isEmpty(title)) {
            return null;
        }
        return title.toLowerCase().replaceAll("\\s+", "_");
    }


    private static final String ENCRYPTION_KEY = "abc123"; // Key used for deriving encryption key

    public static String encrypt(String clearText) throws Exception {
        clearText = "{\"CorporateId\": 1162, \"CorporateName\": \"Emitra test\", \"JobroleId\": 96, \"Candidate_SSOID\": \"AJITSINGHRATHORE27\", \"Candidate_Name\": \"AJIT SINGH RATHORE\", \"Candidate_Email\": \"ajitsinghrathore27@gmail.com\",\"EmployeeNumber\": \"RJUreD730000\", \"Candidate_Mobile\": \"8209165015\", \"InActiveStatus\": 1, \"JWTToken\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6IjEiLCJncm91cHNpZCI6IjEiLCJuYmYiOjE3Mzg1NjQ2MTYsImV4cCI6MTczODU4NjIxNiwiaWF0IjoxNzM4NTY0NjE2LCJpc3MiOiJpc3N1ZXIiLCJhdWQiOiJodHRwOi8vZ2Mtc29sdXRpb25zLm5ldCJ9.8fmEobW10M9mL0ThDbYlKk4k7NdMVF-tlzV8ugtn4-M\"\"SSOToken\":\"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiRU1JVFJBIl0sInVzZXJfbmFtZSI6IkFKSVRTSU5HSFJBVEhPUkUyNyIsInNjb3BlIjpbIndyaXRlIiwicmVhZCJdLCJleHAiOjE3MzgzMzE4OTIsImF1dGhvcml0aWVzIjpbIlJPTEVfNCIsIlJPTEVfMSIsIlJPTEVfMiIsIlJPTEVfNyIsIlJPTEVfOCIsIlJPTEVfNSIsIlJPTEVfNiIsIlJPTEVfMTkiLCJST0xFXzE4IiwiUk9MRV8xNSIsIlJPTEVfMTQiLCJST0xFXzE3IiwiUk9MRV8xNiIsIlJPTEVfMzMiLCJST0xFXzExIiwiUk9MRV8zMiIsIlJPTEVfMTAiLCJST0xFXzEzIiwiUk9MRV8xMiIsIlJPTEVfMzEiLCJST0xFXzkiLCJST0xFXzMwIiwiUk9MRV8yOSIsIlJPTEVfU0lHTklOIiwiUk9MRV8yNiIsIlJPTEVfMjUiLCJST0xFXzI4IiwiUk9MRV8yNyIsIlJPTEVfMjIiLCJST0xFXzIxIiwiUk9MRV8yNCIsIlJPTEVfMjMiLCJST0xFXzIwIiwiUk9MRV9BRE1JTiJdLCJqdGkiOiI4Y2E4N2NiYS02MzRiLTRmNWMtYjcyZS02ZjEwNjY4YjQwNTQiLCJjbGllbnRfaWQiOiJURVNUIn0.FY2kRg-WlnBGe1_K3f6O4u6Vg0ennGZpI6itdTrWkdkgFpqBa864N2Y5g-thJcxNVVbz19eJQMUGJGFo3ZlFpUQWGZo5TPdhjUcPTF4pVXQ-i3zRwJZ4d3VLbIm2ri_oU8AzY3bBruZNz1rZ8OhQdGWfg9wgaAtbEsNx_G-YSua75z-x457_nE4axYRP4d3QVOmzXtdxemajWcx8wHXPm37JB-7EBp4KCKc-Y18ifEyfoC1BDAtkRMCaozwLI7vuwlksS6S_wv_Q7jRqXJuKLHHNJeDlrM4UnJLTqqovWIVsNfJO3_pa31biuP4xlVFeyy1LX-7BzlxzO7_pjAV1TQ\",\"Return_Url\":\"http://localhost:8080/emitraApps/invalidate\"}";
        byte[] clearBytes = clearText.getBytes(StandardCharsets.UTF_16LE);

        // Derive key and IV from the password
        byte[] salt = new byte[] { 0x49, 0x76, 0x61, 0x6e, 0x20, 0x4d, 0x65, 0x64, 0x76, 0x65, 0x64, 0x65, 0x76 };
        PBEKeySpec pbeKeySpec = new PBEKeySpec(ENCRYPTION_KEY.toCharArray(), salt, 1000, 256);  // 1000 iterations for better security
        SecretKey secretKey = new SecretKeySpec(javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(pbeKeySpec).getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);  // Generate random IV

        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        byte[] encrypted = cipher.doFinal(clearBytes);

        // Combine the IV and the encrypted bytes (IV is needed for decryption)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(iv);
        outputStream.write(encrypted);

        // Return base64-encoded string of combined result (IV + encrypted data)
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }


    public static String decrypt(String cipherText) throws Exception {
        cipherText =  "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";
        byte[] data = Base64.getDecoder().decode(cipherText);

        // Extract IV and encrypted data
        byte[] iv = new byte[16];
        System.arraycopy(data, 0, iv, 0, iv.length);
        byte[] encryptedData = new byte[data.length - iv.length];
        System.arraycopy(data, iv.length, encryptedData, 0, encryptedData.length);

        // Derive key from the same method used for encryption
        byte[] salt = new byte[] { 0x49, 0x76, 0x61, 0x6e, 0x20, 0x4d, 0x65, 0x64, 0x76, 0x65, 0x64, 0x65, 0x76 };
        PBEKeySpec pbeKeySpec = new PBEKeySpec(ENCRYPTION_KEY.toCharArray(), salt, 1000, 256); // 1000 iterations for better security
        SecretKey secretKey = new SecretKeySpec(javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(pbeKeySpec).getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        byte[] decrypted = cipher.doFinal(encryptedData);

        // Return the decrypted string
        return new String(decrypted, StandardCharsets.UTF_16LE);
    }


//    public static String getStringOfLength10(String title) {
//        if (isEmpty(title)) {
//            return null;
//        }
//        title = validateTitle(title);
//        if (title.length() <= 10) {
//            return title;
//        }
//        return title.substring(0, 10);
//    }
}