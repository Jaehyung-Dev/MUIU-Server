package com.bit.muiu.provider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo{

    //    {
    //        "resultcode": "00",
    //            "message": "success",
    //            "response": {
    //                "id": "32742707",
    //                "email": "example@naver.com",
    //                "name": "홍길동",
    //                "gender": "F",
    //                "birthday": "10-25",
    //                "birthyear": "1990",
    //                "mobile": "010-1234-5678"
    //                 }
    Map<String, Object> attributes;
    Map<String, Object> response;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        // response 객체에 접근하도록 변경
        this.response = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProviderId() {
        return this.response.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return this.response.get("email").toString();
    }

    @Override
    public String getName() {
        return this.response.get("name").toString();
    }

    @Override
    public Date getBirth() {
        String birthYear = response.get("birthyear").toString();
        String birthDay = response.get("birthday").toString();
        String birthDateString = birthYear + "-" + birthDay; // 형식: "YYYY-MM-DD"

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return dateFormat.parse(birthDateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 오류 발생 시 null 반환 (또는 Optional<Date> 사용 가능)
        }
    }

    @Override
    public String getGender() {
        String genderCode = this.response.get("gender").toString();

        if ("M".equals(genderCode)) {
            return "남";
        } else if ("F".equals(genderCode)) {
            return "여";
        } else {
            return "알 수 없음"; // 예외 처리로, 다른 값이 들어왔을 때 대비
        }
    }

    @Override
    public String getMobile() {
        return this.response.get("mobile").toString();
    }
}
