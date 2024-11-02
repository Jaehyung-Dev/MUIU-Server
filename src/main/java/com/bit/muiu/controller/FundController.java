package com.bit.muiu.controller;

import com.bit.muiu.dto.FundPostDto;
import com.bit.muiu.dto.FundRecordDto;
import com.bit.muiu.dto.ResponseDto;
import com.bit.muiu.entity.Member;
import com.bit.muiu.repository.MemberRepository;
import com.bit.muiu.service.FundService;
import com.bit.muiu.service.NaverCloudStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.bit.muiu.common.FileUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@CrossOrigin(origins = "https://www.%EB%A7%88%EC%9D%8C%EC%9D%B4%EC%9D%8C.site") // 프론트엔드 도메인 허용
@RestController
@RequestMapping("/api/fund")
@RequiredArgsConstructor
@Slf4j
public class FundController {

    private final FundService fundService;
    private final MemberRepository memberRepository;
    private final NaverCloudStorageService naverCloudStorageService;
    private final FileUtils fileUtils;

    @PostMapping(value = "/post", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createFundPost(
            @RequestPart("fundPostDto") FundPostDto fundPostDto,
            @RequestPart(value = "uploadFiles", required = false) MultipartFile[] uploadFiles,
            @AuthenticationPrincipal UserDetails userDetails) {

        ResponseDto<FundPostDto> responseDto = new ResponseDto<>();

        try {
            log.info("Controller: post fundPostDto: {}", fundPostDto);
            log.info("Controller: post uploadFiles: {}", uploadFiles);
            if (userDetails == null) {
                throw new RuntimeException("사용자 인증 정보가 없습니다. 로그인 후 다시 시도해주세요.");
            }

            String loggedInUsername = userDetails.getUsername();
            fundPostDto.setUsername(loggedInUsername);

            // 이미지 파일 업로드 및 URL 설정
            if (uploadFiles != null) {
                for (MultipartFile file : uploadFiles) {
                    if (!file.isEmpty()) {
                        String imageUrl = fundService.uploadImage(file);  // 새 메서드 사용
                        fundPostDto.setMainImage(imageUrl);  // 이미지 URL을 설정
                    }
                }
            }

            // 최종적으로 DB에 저장
            FundPostDto savedFundPost = fundService.createFundPost(fundPostDto);
            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("Fund post created successfully");
            responseDto.setItem(savedFundPost);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            log.error("Error while creating fund post: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Failed to create fund post");
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }



    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            log.info("여기 컨트롤러. post file: {}", file);
            String imageUrl = fundService.uploadImage(file);  // 업로드한 이미지의 URL 받기
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("이미지 업로드 실패");
        }
    }




    @GetMapping("/posts")
    public ResponseEntity<List<FundPostDto>> getAllPosts() {
        try {
            List<FundPostDto> posts = fundService.getAllPosts();
            log.info("1번 로직, 션의 posts 데이터 확인: {}", posts);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            log.error("Error retrieving fund posts: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<FundPostDto> getPostById(@PathVariable Long id) {
        try {
            FundPostDto post = fundService.getPostById(id);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            log.error("Error retrieving post: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity<?> updateFundPost(
            @PathVariable Long postId,
            @RequestBody FundPostDto fundPostDto) {

        ResponseDto<FundPostDto> responseDto = new ResponseDto<>();

        try {
            fundService.updateFundPost(postId, fundPostDto);

            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("Fund post updated successfully");
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("Error updating fund post: ", e);

            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Error updating fund post: " + e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }


    // 게시글 삭제
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<?> deleteFundPost(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // 사용자 인증 확인
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access.");
            }

            fundService.deleteFundPost(postId); // 게시글 삭제 서비스 호출
            return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 삭제 실패: " + e.getMessage());
        }
    }


    // 기부내역 insert
    @PostMapping("/payment")
    public ResponseEntity<?> savePaymentRecord(@RequestBody FundRecordDto fundRecordDto) {
        log.info("fundRecordDto 데이터 확인: {}", fundRecordDto);
        try {
            fundService.savePaymentRecord(fundRecordDto);
            return ResponseEntity.ok("결제 내역이 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 내역 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // 기부내역 조회
    @GetMapping("/records")
    public ResponseEntity<List<FundRecordDto>> getDonationRecords(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            // 인증된 사용자의 username 가져옴
            String username = userDetails.getUsername();

            // username으로 Member 찾기
            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            Long userId = member.getId();

            // 기부 기록 조회
            List<FundRecordDto> records = fundService.getDonationRecords(userId);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 기부금액 합산
    @PutMapping("/updateCurrentAmount/{postId}")
    public ResponseEntity<Void> updateCurrentAmount(@PathVariable Long postId) {
        fundService.updateCurrentAmountForPost(postId);
        return ResponseEntity.ok().build();
    }







}
