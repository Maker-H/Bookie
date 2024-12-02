//package web.bookie.util;
//
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//import web.bookie.data.BookRepository;
//import web.bookie.domain.Book;
//
//import java.io.IOException;
//
//@Component
//public class MultipartFileToByteArrayConverter implements Converter<MultipartFile, byte[]> {
//
//    @Override
//    public byte[] convert(MultipartFile source) {
//        try {
//            return source.getBytes(); // MultipartFile을 byte[]로 변환
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to convert MultipartFile to byte[]", e);
//        }
//    }
//}
