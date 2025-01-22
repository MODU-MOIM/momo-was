package com.example.momowas.photo.service;

import com.example.momowas.feed.domain.Feed;
import com.example.momowas.feed.service.FeedService;
import com.example.momowas.photo.domain.Photo;
import com.example.momowas.photo.repository.PhotoRepository;
import com.example.momowas.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final S3Service s3Service;
    private final FeedService feedService;

    /* 피드 사진 생성 */
    public void createPhoto(List<MultipartFile> files, Long feedId) throws IOException {
        Feed feed = feedService.findFeedById(feedId);
        AtomicInteger sequence = new AtomicInteger(0);

        List<Photo> photos = s3Service.uploadImages(files, "feed").stream()
                .map(url -> Photo.of(url, sequence.incrementAndGet(), feed))
                .collect(Collectors.toList());

        photoRepository.saveAll(photos);
    }
}
