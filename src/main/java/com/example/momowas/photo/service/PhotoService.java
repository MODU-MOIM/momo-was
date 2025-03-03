package com.example.momowas.photo.service;

import com.example.momowas.feed.domain.Feed;
import com.example.momowas.feed.service.FeedService;
import com.example.momowas.hash.HashUtil;
import com.example.momowas.photo.domain.Photo;
import com.example.momowas.photo.repository.PhotoRepository;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final S3Service s3Service;

    /* 피드 사진 생성 */
    @Transactional
    public void createPhoto(List<MultipartFile> files, Feed feed) throws IOException, NoSuchAlgorithmException {
        List<Photo> photos=new ArrayList<>();
        int seq=1;

        for (MultipartFile file : files) {
            String imageUrl = s3Service.uploadImage(file, "feed");
            String imageHash = HashUtil.getFileHash(file);
            photos.add(Photo.of(imageUrl, imageHash, seq++, feed));
        }

        photoRepository.saveAll(photos);
    }

    /* 피드 사진 수정 */
    @Transactional
    public void updatePhoto(List<MultipartFile> files, Feed feed) throws IOException, NoSuchAlgorithmException {
        photoRepository.deleteByFeed(feed); //기존 사진 삭제
        if (files!=null && !files.isEmpty()) {
            createPhoto(files, feed); //새로운 사진으로 저장
        }
    }
}
