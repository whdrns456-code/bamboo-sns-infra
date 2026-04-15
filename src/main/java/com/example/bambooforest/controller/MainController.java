package com.example.bambooforest.controller;


import com.example.bambooforest.model.Comment;
import com.example.bambooforest.model.Posts;
import com.example.bambooforest.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    final private BoardService boardService;

    // 1. 첫 접속 시 로그인 페이지로 이동 (localhost:8080)
    @GetMapping("/write")
    public String write() {
        return "write";
    }
    // 2. 로그인 페이지 명시적 호출 (localhost:8080/login)
    @GetMapping("/login")
    public String login() {
        return "index";
    }

    @GetMapping("/mypage")
    public String mypage(Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();

            if ("noblejinmo".equals(username)) {
                return "nobleMypage";
            }
        }
        return "mypage";
    }
    // 3. 회원가입 페이지 이동 (localhost:8080/signup)
    @GetMapping("/signup")
    public String signup() {
        return "signup"; // templates/signup.html 이 있어야 함
    }

    // 4. 로그인 성공 후 메인 페이지 이동 (localhost:8080/main)
    @GetMapping("/main")
    public String mainPage(Authentication authentication, Model model) {
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
        }

        java.util.Map<String, Object> rootData = boardService.getRootNotices(0, 3);

        model.addAttribute("rootPosts", rootData.get("posts"));
        model.addAttribute("totalRootPages", rootData.get("totalPages"));

        // 일반 게시글 (기존 로직)
        List<Posts> postList = boardService.findAllPosts();
        model.addAttribute("posts", postList);

        return "main";
    }
    @GetMapping("/api/board/more")
    @ResponseBody
    public List<Posts> getMorePosts(@RequestParam(defaultValue = "0") int lastPostNo) {
        return boardService.findMorePosts(lastPostNo, 9); //
    }


    @GetMapping("/board/detail/{postNo}")
    public String getPostDetail(@PathVariable("postNo") Long postNo, Model model) {
        // 1. 조회수 증가 (상세 페이지 접속 시 +1)
        boardService.plusViewCount(postNo);

        List<Comment> comments = boardService.getComments(postNo);

        model.addAttribute("comments", comments);


        // 2. 게시글 상세 데이터 조회
        Posts post = boardService.findPostByNo(postNo);

        // 3. 모델에 담아 HTML로 전달
        model.addAttribute("post", post);

        return "board/detail"; // templates/board/detail.html 경로
    }


}