//package com.sparta.springtrello.config.s3;
//
//import org.springframework.web.multipart.MultipartFile;
//
//public class FileValidator {
//
//    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
//    private static final String[] SUPPORTED_TYPES = {
//            "image/jpeg",
//            "image/png",
//            "application/pdf",
//            "text/csv"
//    };
//
//    public static void validateFile(MultipartFile file) {
//        // 파일이 null인지 확인
//        if (file == null || file.isEmpty()) {
//            throw new IllegalArgumentException("파일이 없습니다.");
//        }
//
//        // 파일 크기 검사
//        if (file.getSize() > MAX_FILE_SIZE) {
//            throw new IllegalArgumentException("파일 크기가 5MB를 초과합니다.");
//        }
//
//        // 파일 형식 검사
//        boolean isValidType = false;
//        for (String type : SUPPORTED_TYPES) {
//            if (file.getContentType() != null && file.getContentType().equals(type)) {
//                isValidType = true;
//                break;
//            }
//        }
//
//        if (!isValidType) {
//            throw new IllegalArgumentException("지원되지 않는 파일 형식입니다.");
//        }
//    }
//}