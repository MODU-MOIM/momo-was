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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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
    public void updatePhoto(List<MultipartFile> newPhotos, Feed feed) throws IOException, NoSuchAlgorithmException {
        List<Photo> origPhotos = feed.getPhotos(); //원래 이미지 리스트

        if (newPhotos==null || newPhotos.isEmpty()) { //새로운 이미지가 없고
            if(origPhotos!=null || !origPhotos.isEmpty()){ //기존 이미지가 존재한다면
                photoRepository.deleteAll(origPhotos); //기존 이미지 삭제
            }
            return;
        }

        //업로드할 이미지 찾기
        Map<MultipartFile,Integer> photosToUpload=new HashMap<>();
        int seq=1;

        for (MultipartFile newPhoto : newPhotos) {
            String newPhotoHash=HashUtil.getFileHash(newPhoto); //새로운 이미지의 해시값
            Photo existingPhoto = getExistingPhoto(origPhotos, newPhotoHash);

            if(existingPhoto!=null){ //기존 이미지 존재
                if(existingPhoto.getSequence() != seq){ //순서 다르면 수정
                    existingPhoto.updateSequence(seq);
                }
            }else{ //기존에 없는 이미지. 새로 추가해야함.
                photosToUpload.put(newPhoto, seq);
            }
            seq++;
        }
        uploadNewPhotos(photosToUpload, feed); //photosToUpload를 업로드

        //삭제할 이미지 찾기
        List<String> newPhotoHashs = new ArrayList<>();
        for(MultipartFile newPhoto:newPhotos){
            newPhotoHashs.add(HashUtil.getFileHash(newPhoto));
        }

        List<Photo> photosToDelete = getPhotosToDelete(origPhotos, newPhotoHashs);
        photoRepository.deleteAll(photosToDelete); //photosToDelete를 삭제
    }

    private Photo getExistingPhoto(List<Photo> origPhotos, String newPhotoHash) {
        return origPhotos.stream()
                .filter(photo -> photo.getHash().equals(newPhotoHash))
                .findAny()
                .orElse(null);
    }

    private void uploadNewPhotos(Map<MultipartFile,Integer> photosToUpload, Feed feed) throws NoSuchAlgorithmException, IOException {
        for(MultipartFile uploadPhoto:photosToUpload.keySet()){
            String photoUrl = s3Service.uploadImage(uploadPhoto, "feed");
            String photoHash = HashUtil.getFileHash(uploadPhoto);
            int sequence=photosToUpload.get(uploadPhoto);
            photoRepository.save(Photo.of(photoUrl,photoHash,sequence,feed));
        }
    }

    private List<Photo> getPhotosToDelete(List<Photo> origPhotos, List<String> newPhotoHashs) {
        return origPhotos.stream()
                .filter(photo -> !newPhotoHashs.contains(photo.getHash()))
                .toList();
    }
}
